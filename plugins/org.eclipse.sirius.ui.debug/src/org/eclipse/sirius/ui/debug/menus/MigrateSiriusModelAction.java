package org.eclipse.sirius.ui.debug.menus;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.sirius.business.api.query.FileQuery;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class MigrateSiriusModelAction implements IObjectActionDelegate {

    private ISelection selectedResources;

    @Override
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        selectedResources = selection;
    }

    @Override
    public void run(IAction action) {
        if (selectedResources instanceof IStructuredSelection) {
            try {
                PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress() {

                    @Override
                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                        Collection<URI> sessionFiles = Lists.newArrayList();
                        Collection<URI> vsmFiles = Lists.newArrayList();

                        extractResourcesToMigrateFromSelection(sessionFiles, vsmFiles);
                        checkCancel(monitor);

                        Iterable<URI> toMigrate = Iterables.concat(vsmFiles, sessionFiles);
                        int i = 0;
                        int size = Iterables.size(toMigrate);
                        monitor.beginTask("Sirius migration (" + vsmFiles.size() + " VSMs, " + sessionFiles.size() + " aird files)", size);

                        for (URI uri : toMigrate) {
                            monitor.subTask(i++ + "/" + size + " - " + uri.toPlatformString(true));
                            ResourceSet rs = new ResourceSetImpl();
                            Resource resource = rs.getResource(uri, true);
                            try {
                                resource.save(Collections.emptyMap());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            monitor.worked(1);
                            checkCancel(monitor);
                        }
                    }

                    /**
                     * Check if the user cancel the current task.
                     * 
                     * @param monitor
                     *            The monitor to deal with.
                     */
                    private void checkCancel(IProgressMonitor monitor) {
                        if (monitor.isCanceled()) {
                            throw new OperationCanceledException();
                        }
                    }
                });
            } catch (final InvocationTargetException e) {
                e.printStackTrace();
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void extractResourcesToMigrateFromSelection(final Collection<URI> sessionFiles, final Collection<URI> vsmFiles) {
        IResourceVisitor visitor = new IResourceVisitor() {

            @Override
            public boolean visit(IResource resource) throws CoreException {
                if (resource instanceof IFile && !resource.getFullPath().toOSString().contains("do_not_migrate")) {
                    IFile file = (IFile) resource;
                    FileQuery fileQuery = new FileQuery(file);
                    if (fileQuery.isSessionResourceFile()) {
                        sessionFiles.add(URI.createPlatformResourceURI(file.getFullPath().toString(), true));
                    } else if (fileQuery.isVSMFile()) {
                        vsmFiles.add(URI.createPlatformResourceURI(file.getFullPath().toString(), true));
                    }
                }
                return true;
            }
        };

        Iterable<IResource> selection = Iterables.filter(((IStructuredSelection) selectedResources).toList(), IResource.class);
        for (IResource res : selection) {
            try {
                res.accept(visitor);
            } catch (CoreException e) {
                // TODO later
            }
        }
    }
}
