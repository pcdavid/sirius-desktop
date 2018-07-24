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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;

/**
 * Class used to create Change object that contains the workspace modifications of this participant to be executed after
 * the changes from the refactoring are executed.
 * 
 * @author <a href="mailto:jessy.mallet@obeo.fr">Jessy MALLET</a>
 *
 */
public class InMemoryResourceURIChange extends Change {

    private URIChange uriChange;

    /**
     * Constructor.
     * 
     * @param uriChange
     *            couple of old and new URI
     * 
     */
    public InMemoryResourceURIChange(URIChange uriChange) {
        this.uriChange = uriChange;
    }

    @Override
    public String getName() {
        return "Change resource uri"; //$NON-NLS-1$
    }

    @Override
    public void initializeValidationData(IProgressMonitor pm) {
        // nothing to initialize
    }

    @Override
    public RefactoringStatus isValid(IProgressMonitor pm) throws CoreException, OperationCanceledException {
        return new RefactoringStatus();
    }

    @Override
    public Change perform(IProgressMonitor pm) throws CoreException {

        for (Session session : SessionManager.INSTANCE.getSessions()) {
            TransactionalEditingDomain editingDomain = session.getTransactionalEditingDomain();
            for (Resource res : new ArrayList<Resource>(editingDomain.getResourceSet().getResources())) {
                if (res.getURI().equals(uriChange.getOldURI())) {
                    editingDomain.getCommandStack().execute(new RenameResourceCommand(editingDomain, res, uriChange.getNewURI()));
                }
            }
        }
        return new InMemoryResourceURIChange(uriChange.inverse());
    }

    @Override
    public Object getModifiedElement() {
        return null;
    }

    /**
     * Add old and new URI of resource to rename.
     * 
     * @param changes
     *            list of old and new URI couple to change
     * @param resourceDelta
     *            the resource renamed
     */
    public static void appendInMemoryURIChanges(Set<URIChange> changes, IResourceDelta resourceDelta) {

        URI oldURI = null;
        URI newURI = null;
        if (resourceDelta.getMovedToPath() != null) {
            oldURI = URI.createPlatformResourceURI(resourceDelta.getResource().getFullPath().toPortableString(), true);
            newURI = URI.createPlatformResourceURI(resourceDelta.getMovedToPath().toPortableString(), true);
        }
        if (resourceDelta.getMovedFromPath() != null) {
            oldURI = URI.createPlatformResourceURI(resourceDelta.getMovedFromPath().toPortableString(), true);
            newURI = URI.createPlatformResourceURI(resourceDelta.getResource().getFullPath().toPortableString(), true);
        }

        if (oldURI != null && newURI != null && !isPresent(changes, oldURI, newURI)) {
            changes.add(new URIChange(oldURI, newURI));
        }

    }

    /**
     * Check if change has already been registered.
     * 
     * @param changes
     *            list of old and new URI couple to change
     * @param oldURI
     *            the oldUri of the resource to rename
     * @param newURI
     *            the newURI of the resource to rename
     */
    private static boolean isPresent(Set<URIChange> changes, URI oldURI, URI newURI) {
        for (URIChange change : changes) {
            if (oldURI.equals(change.getOldURI()) && newURI.equals(change.getNewURI())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a Change object that contains the workspace modifications of this participant to be executed after the
     * changes from the refactoring are executed.
     * 
     * @param changes
     *            URI change saved before
     * @param pm
     *            progress monitor to use
     * @return the new Change object which contains the workspace modifications
     */
    public static Change toRefactoringChange(Set<URIChange> changes, IProgressMonitor pm) {
        Change changeModif = null;
        List<Change> refactoringChange = InMemoryResourceURIChange.toRefactoringChanges(changes, pm);

        if (refactoringChange.size() == 1) {
            changeModif = refactoringChange.get(0);
        } else if (refactoringChange.size() > 1) {
            CompositeChange compo = new CompositeChange("update sessions", refactoringChange.toArray(new Change[refactoringChange.size()])); //$NON-NLS-1$
            changeModif = compo;
        }
        return changeModif;
    }

    /**
     * Create multiple Change object, one change object contains one workspace modification.
     * 
     * @param changes
     *            URI change saved before
     * @param pm
     *            progress monitor to use
     * @return the multiple Change object which contains one workspace modifications each one
     */
    public static List<Change> toRefactoringChanges(Set<URIChange> changes, IProgressMonitor pm) {
        List<Change> refactoringChanges = new ArrayList<Change>();
        Map<URI, SerializedResourceURIChange> serializedRefactorings = new HashMap<URI, SerializedResourceURIChange>();
        ModelDependenciesGraph graph = new WorkspaceDependencies().buildModelDependencyGraph(ResourcesPlugin.getWorkspace());
        for (URIChange uriChange : changes) {
            Collection<URI> inverseRequirements = graph.getInverseRequirements(uriChange.getOldURI());
            if (inverseRequirements != null) {
                for (URI toReserialize : inverseRequirements) {
                    SerializedResourceURIChange alreadyHere = serializedRefactorings.get(toReserialize);
                    if (alreadyHere == null) {
                        alreadyHere = new SerializedResourceURIChange(toReserialize);
                        serializedRefactorings.put(toReserialize, alreadyHere);
                    }
                    alreadyHere.addURIChange(uriChange);
                }
            }
        }
        refactoringChanges.addAll(serializedRefactorings.values());
        return refactoringChanges;
    }
}
