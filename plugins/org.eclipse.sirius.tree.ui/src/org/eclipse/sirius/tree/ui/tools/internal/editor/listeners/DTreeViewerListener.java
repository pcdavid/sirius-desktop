/*******************************************************************************
 * Copyright (c) 2011 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tree.ui.tools.internal.editor.listeners;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.tools.api.command.SiriusCommand;
import org.eclipse.sirius.tree.DTreeItem;
import org.eclipse.sirius.tree.business.api.interaction.DTreeItemUserInteraction;
import org.eclipse.sirius.tree.business.internal.dialect.common.viewpoint.GlobalContext;
import org.eclipse.sirius.tree.business.internal.helper.RefreshTreeElementTask;
import org.eclipse.sirius.tree.ui.provider.TreeUIPlugin;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

/**
 * A {@link ITreeViewerListener} to update the DTree model when a SWT TreeItem
 * is collapsed/expanded.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
public class DTreeViewerListener implements ITreeViewerListener {

    private Session session;

    private TransactionalEditingDomain domain;

    /**
     * Default constructor.
     * 
     * @param session
     *            {@link Session}
     */
    public DTreeViewerListener(Session session) {
        this.session = session;
        this.domain = session.getTransactionalEditingDomain();
    }

    /**
     * {@inheritDoc}
     */
    public void treeExpanded(final TreeExpansionEvent event) {
        if (event.getElement() instanceof DTreeItem) {
            DTreeItem dTreeItem = (DTreeItem) event.getElement();
            if (!dTreeItem.isExpanded()) {
                final CommandStack commandStack = domain.getCommandStack();
                final CompoundCommand expandDTreeItemCmd = new CompoundCommand("Expand " + dTreeItem.getName() + " tree item");
                final SiriusCommand result = new SiriusCommand(domain);
                result.getTasks().add(new RefreshTreeElementTask((DTreeItem) event.getElement(), domain));
                expandDTreeItemCmd.append(result);

                IWorkbench wb = PlatformUI.getWorkbench();
                IProgressService ps = wb.getProgressService();
                try {
                    ps.busyCursorWhile(new IRunnableWithProgress() {
                        public void run(IProgressMonitor pm) {
                            expandDTreeItemCmd.append(new TreeFoldingRecordingCommand(session, event, true, pm));
                            commandStack.execute(expandDTreeItemCmd);
                        }
                    });
                } catch (InvocationTargetException e) {
                    TreeUIPlugin.INSTANCE.log(new Status(IStatus.ERROR, TreeUIPlugin.ID, "Error while expanding the tree.", e));
                } catch (InterruptedException e) {
                    TreeUIPlugin.INSTANCE.log(new Status(IStatus.WARNING, TreeUIPlugin.ID, "Interruption while expanding the tree.", e));
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void treeCollapsed(final TreeExpansionEvent event) {
        if (event.getElement() instanceof DTreeItem) {
            DTreeItem dTreeItem = (DTreeItem) event.getElement();
            if (dTreeItem.isExpanded()) {
                final CommandStack commandStack = domain.getCommandStack();
                final CompoundCommand expandDTreeItemCmd = new CompoundCommand("Collapse " + dTreeItem.getName() + " tree item");

                IWorkbench wb = PlatformUI.getWorkbench();
                IProgressService ps = wb.getProgressService();
                try {
                    ps.busyCursorWhile(new IRunnableWithProgress() {
                        public void run(IProgressMonitor pm) {
                            expandDTreeItemCmd.append(new TreeFoldingRecordingCommand(session, event, false, pm));
                            commandStack.execute(expandDTreeItemCmd);
                        }
                    });
                } catch (InvocationTargetException e) {
                    TreeUIPlugin.INSTANCE.log(new Status(IStatus.ERROR, TreeUIPlugin.ID, "Error while collapsing the tree.", e));
                } catch (InterruptedException e) {
                    TreeUIPlugin.INSTANCE.log(new Status(IStatus.WARNING, TreeUIPlugin.ID, "Interruption while collapsing the tree.", e));
                }
            }
        }
    }

    /**
     * EMF Command to synchronize the DTree according to a
     * {@link TreeExpansionEvent}.
     * 
     * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban
     *         Dugueperoux</a>
     */
    class TreeFoldingRecordingCommand extends RecordingCommand {

        private Session session;

        private TreeExpansionEvent event;

        private boolean expand;

        private IProgressMonitor monitor;

        public TreeFoldingRecordingCommand(Session session, TreeExpansionEvent event, boolean expand, IProgressMonitor monitor) {
            super(session.getTransactionalEditingDomain());
            this.session = session;
            this.event = event;
            this.expand = expand;
            this.monitor = monitor;
        }

        @Override
        protected void doExecute() {
            GlobalContext ctx = new GlobalContext(session.getModelAccessor(), session);
            if (expand) {
                new DTreeItemUserInteraction((DTreeItem) event.getElement(), ctx).expand(monitor);
            } else {
                new DTreeItemUserInteraction((DTreeItem) event.getElement(), ctx).collapse(monitor);
            }
        }
    }
}
