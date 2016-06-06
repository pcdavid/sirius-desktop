/******************************************************************************
 * Copyright (c) 2002, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 ****************************************************************************/

package org.eclipse.sirius.diagram.ui.tools.internal.actions.layout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.ui.edit.api.part.IDDiagramEditPart;
import org.eclipse.sirius.diagram.ui.edit.api.part.IDiagramNameEditPart;
import org.eclipse.sirius.diagram.ui.provider.DiagramUIPlugin;
import org.eclipse.sirius.diagram.ui.provider.Messages;
import org.eclipse.sirius.diagram.ui.tools.api.image.DiagramImagesPath;
import org.eclipse.sirius.diagram.ui.tools.api.layout.SiriusLayoutDataManager;
import org.eclipse.sirius.diagram.ui.tools.api.ui.actions.ActionIds;
import org.eclipse.sirius.diagram.ui.tools.internal.layout.data.extension.LayoutDataManagerRegistry;
import org.eclipse.sirius.ext.base.Option;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.google.common.collect.Lists;

/**
 * Copy the layout of the selected diagram or of the selected diagram elements.
 * 
 * @author <a href="mailto:laurent.redor@obeo.fr">Laurent Redor</a>
 */
public class CopyLayoutAction extends AbstractCopyPasteLayoutAction {

    /**
     * Default constructor.
     * 
     * @param workbenchPage
     *            the active workbench page
     * @param actionWorkbenchPart
     *            the part concerned by this action. Could be null.
     */
    public CopyLayoutAction(final IWorkbenchPage workbenchPage, IWorkbenchPart actionWorkbenchPart) {
        super(workbenchPage, actionWorkbenchPart);

        setText(Messages.CopyLayoutAction_text);
        setAccelerator(SWT.CTRL | SWT.SHIFT | SWT.ALT | 'C');
        setId(ActionIds.COPY_LAYOUT);
        setToolTipText(Messages.CopyLayoutAction_toolTipText);

        setImageDescriptor(DiagramUIPlugin.Implementation.getBundledImageDescriptor(DiagramImagesPath.COPY_LAYOUT_ICON));
        setDisabledImageDescriptor(DiagramUIPlugin.Implementation.getBundledImageDescriptor(DiagramImagesPath.COPY_LAYOUT_DISABLED_ICON));
        setHoverImageDescriptor(DiagramUIPlugin.Implementation.getBundledImageDescriptor(DiagramImagesPath.COPY_LAYOUT_ICON));
    }

    /**
     * Default constructor.
     * 
     * @param workbenchPage
     *            the active workbench page
     */
    public CopyLayoutAction(final IWorkbenchPage workbenchPage) {
        this(workbenchPage, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#getCommandLabel()
     */
    @Override
    protected String getCommandLabel() {
        return Messages.CopyLayoutAction_commandLabel;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#getCommand()
     */
    @Override
    protected Command getCommand() {
        // Create a compound command to hold the store layout commands
        final CompoundCommand doStoreLayoutsCmd = new CompoundCommand(Messages.CopyLayoutAction_storeLayoutCommandLabel);
        doStoreLayoutsCmd.add(new Command(Messages.CopyLayoutAction_clrearPreviousLayoutDateCommandLabel) {
            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.gef.commands.Command#canUndo()
             */
            // FIXME LRE : this should be undoable, and it should not be a
            // command in the stack ! !
            @Override
            public boolean canUndo() {
                return false;
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.gef.commands.Command#canExecute()
             */
            @Override
            public boolean canExecute() {
                return super.canExecute();
            }

            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.gef.commands.Command#execute()
             */
            @Override
            public void execute() {
                for (SiriusLayoutDataManager layoutDataManager : LayoutDataManagerRegistry.getAllSiriusLayoutDataManagers()) {
                    layoutDataManager.clearLayoutData();
                }
            }
        });
        DiagramEditPart diagramEditPart = getDiagramEditPart();
        if (diagramEditPart instanceof IDDiagramEditPart) {
            final Option<DDiagram> diagram = ((IDDiagramEditPart) diagramEditPart).resolveDDiagram();
            if (diagram.some()) {
                // Clean the selection to keep only one data if both node and
                // its label are selected.
                List<IGraphicalEditPart> selectedEditParts = cleanSelectedObjects(getSelectedObjects());
                // For each selected edit part, store its layout.
                for (final IGraphicalEditPart toStore : selectedEditParts) {
                    doStoreLayoutsCmd.add(new ICommandProxy(new CopyLayoutDataCommand(toStore.getEditingDomain(), diagram.get(), toStore)));
                }
            }
        }
        return doStoreLayoutsCmd.unwrap();
    }

    /**
     * Remove label from selection if its parent's node is also selected.
     * 
     * @param selectedObjects
     *            The current selected objects.
     * @return The current selected {@link IGraphicalEditPart}
     */
    private List<IGraphicalEditPart> cleanSelectedObjects(List<?> selectedObjects) {
        List<IGraphicalEditPart> result = Lists.newArrayList();
        // Transform List to Set to optimize the contains() called in the below
        // loop
        final Set<Object> selection = new HashSet<Object>(selectedObjects);
        for (Object selectedObject : selection) {
            boolean isLabelOfSelectedParent = (selectedObject instanceof IDiagramNameEditPart) && selection.contains(((IDiagramNameEditPart) selectedObject).getParent());
            if (!isLabelOfSelectedParent) {
                result.add((IGraphicalEditPart) selectedObject);
            }
        }
        return result;
    }

    /**
     * A command allowing to copy layout data.
     */
    private final class CopyLayoutDataCommand extends AbstractTransactionalCommand {

        private IGraphicalEditPart toStore;

        private DDiagram dDiagram;

        /**
         * Default constructor.
         * 
         * @param domain
         *            the editing domain on which this command will be executed
         * @param dDiagram
         *            the {@link DDiagram} from which the layout will be copied
         * @param editPartToStore
         *            the edit part to store
         */
        public CopyLayoutDataCommand(TransactionalEditingDomain domain, DDiagram dDiagram, IGraphicalEditPart editPartToStore) {
            super(domain, Messages.CopyLayoutDataCommand_label, null);
            this.dDiagram = dDiagram;
            this.toStore = editPartToStore;
        }

        @Override
        protected CommandResult doExecuteWithResult(final IProgressMonitor monitor, final IAdaptable info) throws ExecutionException {
            for (SiriusLayoutDataManager layoutDataManager : LayoutDataManagerRegistry.getSiriusLayoutDataManagers(dDiagram)) {
                layoutDataManager.storeLayoutData(toStore);
            }
            return CommandResult.newOKCommandResult();
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand#doUndo(org.eclipse.core.runtime.IProgressMonitor,
         *      org.eclipse.core.runtime.IAdaptable)
         */
        @Override
        protected IStatus doUndo(final IProgressMonitor monitor, final IAdaptable info) throws ExecutionException {
            for (SiriusLayoutDataManager layoutDataManager : LayoutDataManagerRegistry.getSiriusLayoutDataManagers(dDiagram)) {
                layoutDataManager.clearLayoutData();
            }
            return super.doUndo(monitor, info);
        }
    }
}
