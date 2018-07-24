/*******************************************************************************
 * Copyright (c) 2018 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.internal.rename;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.sirius.business.api.resource.ResourceDescriptor;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.business.api.session.factory.SessionFactory;
import org.eclipse.sirius.business.api.session.resource.AirdResource;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.Messages;
import org.eclipse.sirius.viewpoint.SiriusPlugin;

/**
 * Class used to update URI of all proxy after updating its parent resource and finally save all the resource.
 * 
 * @author <a href="mailto:jessy.mallet@obeo.fr">Jessy MALLET</a>
 *
 */
public class SerializedResourceURIChange extends Change {

    /**
     * URI of the resource to update.
     */
    private URI resourceToUpdate;

    /**
     * List of URI changes detected during rename or move.
     */
    private Set<URIChange> changes = new LinkedHashSet<URIChange>();

    /**
     * Constructor.
     * 
     * @param resourceToUpdate
     *            resource to update which can contains proxy to solve
     */
    public SerializedResourceURIChange(URI resourceToUpdate) {
        this.resourceToUpdate = resourceToUpdate;
    }

    @Override
    public String getName() {
        return "Change resource uri in file"; //$NON-NLS-1$
    }

    /**
     * Add URI change to list of URI to update.
     * 
     * @param change
     *            the URI change to add
     */
    public void addURIChange(URIChange change) {
        this.changes.add(change);
    }

    @Override
    public void initializeValidationData(IProgressMonitor pm) {

    }

    @Override
    public RefactoringStatus isValid(IProgressMonitor pm) throws CoreException, OperationCanceledException {
        return new RefactoringStatus();
    }

    @Override
    public Change perform(IProgressMonitor pm) throws CoreException {
        if (changes.size() > 0) {

            // retrieve all proxies
            DependenciesCollector collector = new DependenciesCollector();
            Set<EObject> proxies = new LinkedHashSet<EObject>();
            URI uriToUse = resourceToUpdate;
            Resource res = collector.collectAllProxies(uriToUse, proxies);

            // Set new URI for proxies
            for (EObject proxy : proxies) {
                if (proxy instanceof InternalEObject) {
                    URI resolved = ((InternalEObject) proxy).eProxyURI().resolve(uriToUse);
                    String eObjectFragment = resolved.fragment();
                    for (URIChange uriChange : changes) {
                        if (uriChange.getOldURI().equals(resolved.trimFragment())) {
                            ((InternalEObject) proxy).eSetProxyURI(uriChange.getNewURI().appendFragment(eObjectFragment));
                        }
                    }
                }
            }

            // save resource
            if (res != null) {
                if (res instanceof AirdResource) {
                    // URI fileURI = URI.createPlatformResourceURI("resource/testSessionCharge/representationsNew.aird",
                    // true); //$NON-NLS-1$
                    Session session = SessionFactory.INSTANCE.createSession(res.getURI(), new NullProgressMonitor());
                    session.getTransactionalEditingDomain().getCommandStack().execute(new RecordingCommand(session.getTransactionalEditingDomain()) {
                        @Override
                        protected void doExecute() {
                            try {
                                updateSemanticResourceInRepresentation(session, res);
                                res.save(Collections.EMPTY_MAP);
                            } catch (IOException e) {
                                SiriusPlugin.getDefault().error(Messages.SiriusSerializeResource_saveFailedMsg, e);
                            }

                        }
                    });
                } else {
                    for (Session session : SessionManager.INSTANCE.getSessions()) {
                        TransactionalEditingDomain editingDomain = session.getTransactionalEditingDomain();
                        for (Resource resource : new ArrayList<Resource>(editingDomain.getResourceSet().getResources())) {
                            if (resource.getURI().equals(res.getURI())) {
                                session.getTransactionalEditingDomain().getCommandStack().execute(new RecordingCommand(editingDomain) {
                                    @Override
                                    protected void doExecute() {
                                        try {
                                            res.save(Collections.EMPTY_MAP);
                                        } catch (IOException e) {
                                            SiriusPlugin.getDefault().error(Messages.SiriusSerializeResource_saveFailedMsg, e);
                                        }

                                    }
                                });
                            }
                        }
                    }
                }
            }

        }

        return null;
    }

    /**
     * Add new sematic resource and delete useless semantic resource.
     * 
     * @param session
     *            session used to store the representation data
     * @param res
     *            resource to update
     */
    private void updateSemanticResourceInRepresentation(Session session, Resource res) {
        if (res instanceof AirdResource) {
            DAnalysis danalysis = getDAnalysisFromRepresentation(res);
            if (danalysis != null) {
                for (URIChange uriChange : changes) {
                    URI newURI = uriChange.getNewURI();
                    if (getResourceDescriptor(danalysis, newURI) == null) {
                        danalysis.getSemanticResources().add(new ResourceDescriptor(newURI));
                    }
                    URI oldUri = uriChange.getOldURI();
                    ResourceDescriptor resourceDescriptorToRemove = getResourceDescriptor(danalysis, oldUri);
                    if (resourceDescriptorToRemove != null) {
                        danalysis.getSemanticResources().remove(resourceDescriptorToRemove);
                    }
                }
            }
        }
    }

    private DAnalysis getDAnalysisFromRepresentation(Resource res) {
        for (EObject content : res.getContents()) {
            if (content instanceof DAnalysis) {
                return (DAnalysis) content;
            }
        }
        return null;
    }

    private ResourceDescriptor getResourceDescriptor(DAnalysis danalysis, URI resourceUri) {
        for (ResourceDescriptor resourceDescriptor : danalysis.getSemanticResources()) {
            if (resourceUri.equals(resourceDescriptor.getResourceURI())) {
                return resourceDescriptor;
            }
        }
        return null;
    }

    @Override
    public Object getModifiedElement() {
        return null;
    }

}
