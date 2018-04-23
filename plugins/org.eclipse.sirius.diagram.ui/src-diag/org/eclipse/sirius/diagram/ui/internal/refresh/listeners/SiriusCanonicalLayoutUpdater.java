/*******************************************************************************
 * Copyright (c) 2011, 2018 THALES GLOBAL SERVICES and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.internal.refresh.listeners;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.sirius.diagram.ui.internal.refresh.layout.SiriusCanonicalLayoutHandler;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.sirius.diagram.ui.tools.internal.editor.DDiagramEditorImpl;

public class SiriusCanonicalLayoutUpdater extends ResourceSetListenerImpl {

    private DDiagramEditor diagramEditor;

    /**
     * Constructor used to do a layout on all created views child (directly or indirectly) of Diagram. </br>
     * NOTE : to use at diagram representation opening.
     * 
     * @param domain
     *            the {@link TransactionalEditingDomain} on which executes this command
     * @param dDiagram
     *            the {@link DiagramEditPart} on which do the layout
     */
    public SiriusCanonicalLayoutUpdater(TransactionalEditingDomain domain, DDiagramEditor diagramEditor) {
        super(NotificationFilter.NOT_TOUCH);
        this.diagramEditor = diagramEditor;
        domain.addResourceSetListener(this);
    }

    @Override
    public boolean isPrecommitOnly() {
        return true;
    }

    @Override
    public boolean isAggregatePrecommitListener() {
        return true;
    }

    @Override
    public Command transactionAboutToCommit(ResourceSetChangeEvent event) throws RollbackException {
        Command cmd = null;
        DiagramEditPart diagramEditPart = ((DDiagramEditorImpl) diagramEditor).getDiagramEditPart();
        if (diagramEditPart != null) {
            cmd = SiriusCanonicalLayoutHandler.getArrangeCommand(diagramEditPart);
        }
        return cmd;
    }

    /**
     * Dispose this listener.
     */
    public void dispose() {
        if (getTarget() != null) {
            getTarget().removeResourceSetListener(this);
        }
        diagramEditor = null;
    }
}
