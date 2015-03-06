/*******************************************************************************    
 * Copyright (c) 2015 Obeo and others. All rights 
 * reserved. This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which accompanies this    
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html  
 *  
 * Contributors: Obeo - initial API and implementation  
 *******************************************************************************/
package org.eclipse.sirius.ecore.fixed.side.edges.design.commands;

import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.sirius.diagram.ui.graphical.edit.policies.SetConnectionBendpointsAccordingToExtremityMoveCommmand;
import org.eclipse.sirius.diagram.ui.internal.edit.commands.ChangeBendpointsOfEdgesCommand;
import org.eclipse.sirius.ext.base.Option;

/**
 * {@link ChangeBendpointsOfEdgesCommand} used to remove the
 * {@link SetConnectionBendpointsAccordingToExtremityMoveCommmand} added in
 * {@link ChangeBendpointsOfEdgesCommand#getBendpointsChangedCommand(TransactionalEditingDomain, Point, ConnectionEditPart, List, boolean)}
 * .
 * 
 * @author <a href="mailto:steve.monnier@obeo.fr">Steve Monnier</a>
 *
 */
public class FixedSideEdgesChangeBendpointsOfEdgesCommand extends ChangeBendpointsOfEdgesCommand {

    public FixedSideEdgesChangeBendpointsOfEdgesCommand(IGraphicalEditPart movedPart, PrecisionPoint moveDelta) {
        super(movedPart, moveDelta);
    }

    @Override
    protected Option<CompositeTransactionalCommand> getBendpointsChangedCommand(TransactionalEditingDomain transactionalEditingDomain, Point moveDelta, ConnectionEditPart connectionEditPart,
            List<AbstractGraphicalEditPart> allMovedEditParts, boolean sourceMove) {
        Option<CompositeTransactionalCommand> bendpointsChangedCommandOption = super.getBendpointsChangedCommand(transactionalEditingDomain, moveDelta, connectionEditPart, allMovedEditParts,
                sourceMove);
        if (bendpointsChangedCommandOption.some()) {
            CompositeTransactionalCommand compositeTransactionalCommand = bendpointsChangedCommandOption.get();
            SetConnectionBendpointsAccordingToExtremityMoveCommmand setConnectionBendpointsAccordingToExtremityMoveCommmand = null;
            ListIterator<IUndoableOperation> commandIterator = compositeTransactionalCommand.listIterator();
            while (setConnectionBendpointsAccordingToExtremityMoveCommmand == null && commandIterator.hasNext()) {
                IUndoableOperation next = commandIterator.next();
                if (next instanceof SetConnectionBendpointsAccordingToExtremityMoveCommmand) {
                    setConnectionBendpointsAccordingToExtremityMoveCommmand = (SetConnectionBendpointsAccordingToExtremityMoveCommmand) next;
                }
            }
            compositeTransactionalCommand.remove(setConnectionBendpointsAccordingToExtremityMoveCommmand);
        }

        return bendpointsChangedCommandOption;
    }

}
