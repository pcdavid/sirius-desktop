/*******************************************************************************
 * Copyright (c) 2007, 2017 THALES GLOBAL SERVICES and Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.internal.session.danalysis;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.sirius.business.api.query.ResourceQuery;
import org.eclipse.sirius.business.api.session.ReloadingPolicy.Action;
import org.eclipse.sirius.business.api.session.SessionListener;
import org.eclipse.sirius.common.tools.api.resource.ResourceSetSync;
import org.eclipse.sirius.common.tools.api.resource.ResourceSetSync.ResourceStatus;
import org.eclipse.sirius.common.tools.api.resource.ResourceSyncClient;
import org.eclipse.sirius.tools.api.command.semantic.RemoveSemanticResourceCommand;
import org.eclipse.sirius.viewpoint.Messages;
import org.eclipse.sirius.viewpoint.SiriusPlugin;
import org.eclipse.sirius.viewpoint.SyncStatus;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

/**
 * A {@link ResourceSyncClient} that updates a session according to the resource changes it is notified of.
 * 
 * @author pcdavid
 */
public class SessionResourcesSynchronizer implements ResourceSyncClient {
    private final DAnalysisSessionImpl session;

    /**
     * Creates a new synchronizer.
     * 
     * @param session
     *            the session to synchronize.
     */
    public SessionResourcesSynchronizer(DAnalysisSessionImpl session) {
        this.session = Preconditions.checkNotNull(session);
    }

    @Override
    public void statusChanged(final Resource resource, final ResourceStatus oldStatus, final ResourceStatus newStatus) {
        // This method is called for each change in turn,
        // while the ResourceSetSync is still gathering/analyzing notifications.
        // Do nothing in this case, and defer all the processing to the
        // statusesChanged method, which is called only once will all the
        // changes already known and aggregated.
    }

    @Override
    public void statusesChanged(Collection<ResourceStatusChange> changes) {
        processNewStatusesBlockingOtherThreads(this.session, changes);
    }

    /*
     * Using a static method so that concurrent access from other threads are blocked until the end of the processing.
     */
    private static void processNewStatusesBlockingOtherThreads(DAnalysisSessionImpl session, Collection<ResourceStatusChange> changes) {
        if (session.isOpen()) {
            final IProgressMonitor pm = new NullProgressMonitor();
            final Multimap<ResourceStatus, Resource> newStatuses = getImpactingNewStatuses(session, changes);
            boolean allResourcesSync = allResourcesAreInSync(session);

            boolean shouldClose = false;
            final Set<Resource> resourcestoRemove = Sets.newLinkedHashSet();
            final Set<Resource> resourcestoReload = Sets.newLinkedHashSet();
            for (final ResourceStatus newStatus : newStatuses.keySet()) {
                switch (newStatus) {
                case SYNC:
                    if (allResourcesSync) {
                        session.notifyListeners(SessionListener.SYNC);
                    }
                    break;
                case CHANGED:
                    session.notifyListeners(SessionListener.DIRTY);
                    break;
                case EXTERNAL_CHANGED:
                case CONFLICTING_CHANGED:
                case CONFLICTING_DELETED:
                case DELETED:
                case CHANGES_CANCELED:
                    Iterator<Resource> it = newStatuses.get(newStatus).iterator();
                    while (!shouldClose && it.hasNext()) {
                        Resource resource = it.next();
                        /*
                         * if the project was renamed, deleted or closed we should not try to reload any thing, this
                         * does not make sense
                         */
                        shouldClose = isInProjectDeletedRenamedOrClosed(resource);
                    }
                    if (!shouldClose) {

                        for (Resource resource : newStatuses.get(newStatus)) {
                            try {
                                List<Action> actions = session.getReloadingPolicy().getActions(session, resource, newStatus);
                                for (Action action : actions) {
                                    switch (action) {
                                    case RELOAD:
                                        resourcestoReload.add(resource);
                                        break;
                                    case REMOVE:
                                        resourcestoRemove.add(resource);
                                        break;
                                    default:
                                        break;
                                    }
                                }
                                // CHECKSTYLE:OFF
                            } catch (final Exception e) {
                                // CHECKSTYLE:ON
                                SiriusPlugin.getDefault().error(MessageFormat.format(Messages.SessionResourcesSynchronizer_cantHandleResourceChangeMsg, resource.getURI()), e);

                            }
                        }
                    }
                    break;
                default:
                    break;
                }
            }

            if (shouldClose) {
                session.close(pm);
            } else if (session.isOpen()) {
                removeOrReloadImpactedResources(session, pm, resourcestoRemove, resourcestoReload);
                // Reload were launched, get global status.
                allResourcesSync = allResourcesAreInSync(session);
                if (allResourcesSync) {
                    session.notifyListeners(SessionListener.SYNC);
                } else {
                    session.notifyListeners(SessionListener.DIRTY);
                }

                // delete session only if no more aird file is open
                if (session.getAllSessionResources().isEmpty()) {
                    session.close(pm);
                } else {
                    if (allResourcesSync) {
                        session.setSynchronizationStatus(SyncStatus.SYNC);
                    } else {
                        session.setSynchronizationStatus(SyncStatus.DIRTY);
                    }
                }
            }

        }
    }

    private static void removeOrReloadImpactedResources(DAnalysisSessionImpl session, final IProgressMonitor pm, final Set<Resource> resourcestoRemove, final Set<Resource> resourcestoReload) {
        if (resourcestoReload.size() > 0) {
            session.notifyListeners(SessionListener.ABOUT_TO_BE_REPLACED);
        }
        final TransactionalEditingDomain ted = session.getTransactionalEditingDomain();

        if (resourcestoReload.size() > 0 || resourcestoRemove.size() > 0) {

            for (Resource toRemove : resourcestoRemove) {
                removeResourceFromSession(session, toRemove, pm);
            }

            final Set<Resource> airdResources = Sets.newLinkedHashSet();
            for (Resource toReload : resourcestoReload) {
                ResourceQuery resourceQuery = new ResourceQuery(toReload);
                if (resourceQuery.isRepresentationsResource()) {
                    airdResources.add(toReload);
                }
            }
            List<Resource> resourcesBeforeReload = Lists.newArrayList(ted.getResourceSet().getResources());
            try {
                reloadResources(session, resourcestoReload, airdResources);
            } catch (IOException e) {
                SiriusPlugin.getDefault().error(MessageFormat.format(Messages.SessionResourcesSynchronizer_cantHandleResourceChangeMsg, resourcestoReload.toString()), e);
            }

            for (Resource airdResource : airdResources) {
                if (airdResource.getURI().equals(session.getSessionResource().getURI())) {
                    session.sessionResourceReloaded(airdResource);
                }
            }
            session.discoverAutomaticallyLoadedResources(resourcesBeforeReload);

        }
        if (resourcestoReload.size() > 0) {
            session.notifyListeners(SessionListener.REPLACED);
        }
    }

    /**
     * Check if this resource is in an non-existent project or a closed project.
     * 
     * @param resource
     *            the resource to check
     * @return true if this resource is in an non-existent project or a closed project, false otherwise
     */
    private static boolean isInProjectDeletedRenamedOrClosed(Resource resource) {
        IFile file = WorkspaceSynchronizer.getFile(resource);
        if (file != null) {
            IProject project = file.getProject();
            if (project != null) {
                return !project.exists() || !project.isOpen();
            }
        }
        return false;
    }

    private static void reloadResources(DAnalysisSessionImpl session, final Collection<Resource> resources, Set<Resource> airdResources) throws IOException {

        TransactionalEditingDomain ted = session.getTransactionalEditingDomain();

        /* execute the reload operation as a read-only transaction */
        RunnableWithResult<?> reload = new RunnableWithResult.Impl<Object>() {
            @Override
            public void run() {
                final Set<Resource> airdResources = Sets.newLinkedHashSet();
                for (Resource resource : resources) {
                    session.disableCrossReferencerResolve(resource);
                    resource.unload();
                    session.enableCrossReferencerResolve(resource);
                    try {
                        resource.load(Collections.EMPTY_MAP);
                        EcoreUtil.resolveAll(resource);
                        session.getSemanticCrossReferencer().resolveProxyCrossReferences(resource);
                    } catch (IOException e) {
                        setResult(e);
                    }
                    if (new ResourceQuery(resource).isRepresentationsResource()) {
                        airdResources.add(resource);
                    }
                }
                for (Resource aird : airdResources) {
                    new AnalysisResourceReloadedCommand(session, ted, aird).execute();
                }
            }
        };
        if (airdResources.size() > 0) {
            /*
             * aird resources are requiring a read-write transaction and should be processed after the other resources.
             */
            /*
             * Wrapping the commands in a global one to make sure only one post-commit gets called for the whole
             * operation.
             */
            ted.getCommandStack().execute(new RecordingCommand(ted) {
                @Override
                public boolean canUndo() {
                    return false;
                }

                @Override
                public boolean canRedo() {
                    return false;
                }

                @Override
                protected void doExecute() {
                    reload.run();
                    reload.setStatus(Status.OK_STATUS);
                }
            });
        } else {
            try {
                ted.runExclusive(reload);
            } catch (InterruptedException e) {
                // do nothing
            }
        }
        if (reload.getResult() != null) {
            throw (IOException) reload.getResult();
        } else if (reload.getStatus() != null && !reload.getStatus().isOK()) {
            SiriusPlugin.getDefault().error(Messages.SessionResourcesSynchronizer_reloadOperationFailErrorMsg, null);
        }

    }

    /**
     * Remove a resource from the current session and close the current session if it contains no more analysis
     * resource.
     * 
     * @param session
     *            the session.
     * @param resource
     *            The resource to remove
     */
    private static void removeResourceFromSession(DAnalysisSessionImpl session, Resource resource, IProgressMonitor pm) {
        if (session.getAllSemanticResources().contains(resource)) {
            session.getTransactionalEditingDomain().getCommandStack().execute(new RemoveSemanticResourceCommand(session, resource, new NullProgressMonitor(), false));
        } else if (session.getAllSessionResources().contains(resource)) {
            session.removeAnalysis(resource);
        }
        if (session.getResources().contains(resource)) {
            for (final EObject root : Lists.newArrayList(resource.getContents())) {
                EcoreUtil.remove(root);
            }
            session.getResources().remove(resource);
        }

    }

    /**
     * Indicates whether all resources (semantic and Danalysises) of this Session are whether
     * {@link ResourceStatus#SYNC} or {@link ResourceStatus#READONLY}.
     * 
     * @return true if all resources (semantic and Danalysises) of this Session are whether {@link ResourceStatus#SYNC}
     *         or {@link ResourceStatus#READONLY}, false otherwise
     */

    protected boolean allResourcesAreInSync() {
        return allResourcesAreInSync(session);
    }

    /**
     * Indicates whether all resources (semantic and Danalysises) of this Session are whether
     * {@link ResourceStatus#SYNC} or {@link ResourceStatus#READONLY}.
     * 
     * @param session
     *            the session.
     * @return true if all resources (semantic and Danalysises) of this Session are whether {@link ResourceStatus#SYNC}
     *         or {@link ResourceStatus#READONLY}, false otherwise
     */
    protected static boolean allResourcesAreInSync(DAnalysisSessionImpl session) {
        return checkResourcesAreInSync(getAllSessionResources(session));
    }

    /**
     * Indicates whether considered resources are whether {@link ResourceStatus#SYNC} or
     * {@link ResourceStatus#READONLY}.
     * 
     * @param resourcesToConsider
     *            the resources to inspect.
     * @return true if all considered are whether {@link ResourceStatus#SYNC} or {@link ResourceStatus#READONLY}, false
     *         otherwise
     */
    protected static boolean checkResourcesAreInSync(Iterable<? extends Resource> resourcesToConsider) {
        boolean allResourceAreInSync = true;
        for (Resource resource : resourcesToConsider) {
            ResourceStatus status = ResourceSetSync.getStatus(resource);
            // Test also resource.modified field in case ResourceStatus ==
            // UNKNOWN
            allResourceAreInSync = status == ResourceStatus.SYNC || !resource.isModified();
            if (!allResourceAreInSync) {
                break;
            }
        }
        return allResourceAreInSync;
    }

    private static Multimap<ResourceStatus, Resource> getImpactingNewStatuses(DAnalysisSessionImpl session, Collection<ResourceStatusChange> changes) {
        Multimap<ResourceStatus, Resource> impactingNewStatuses = LinkedHashMultimap.create();
        Multimap<ResourceStatus, Resource> representationResourcesNewStatuses = LinkedHashMultimap.create();
        Iterable<Resource> semanticOrControlledresources = getAllSemanticResources(session);
        Set<Resource> representationResources = session.getAllSessionResources();
        for (ResourceStatusChange change : changes) {
            if (session.isResourceOfSession(change.getResource(), semanticOrControlledresources)) {
                impactingNewStatuses.put(change.getNewStatus(), change.getResource());
            } else if (session.isResourceOfSession(change.getResource(), representationResources)) {
                representationResourcesNewStatuses.put(change.getNewStatus(), change.getResource());
            }
        }
        // Add session resource impacting status after semantic ones.
        impactingNewStatuses.putAll(representationResourcesNewStatuses);
        return impactingNewStatuses;
    }

    private static Iterable<Resource> getAllSessionResources(DAnalysisSessionImpl session) {
        return Iterables.concat(session.getSemanticResources(), session.getAllSessionResources(), session.getControlledResources());
    }

    private static Iterable<Resource> getAllSemanticResources(DAnalysisSessionImpl session) {
        return Iterables.concat(session.getSemanticResources(), session.getControlledResources());
    }

}
