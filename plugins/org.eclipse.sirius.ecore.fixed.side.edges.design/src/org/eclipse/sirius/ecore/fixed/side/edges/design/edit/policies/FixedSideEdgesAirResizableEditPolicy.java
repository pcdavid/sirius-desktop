/*******************************************************************************    
 * Copyright (c) 2015 Obeo and others. All rights 
 * reserved. This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which accompanies this    
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html  
 *  
 * Contributors: Obeo - initial API and implementation  
 *******************************************************************************/
package org.eclipse.sirius.ecore.fixed.side.edges.design.edit.policies;

import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.sirius.diagram.ui.graphical.edit.policies.AirResizableEditPolicy;
import org.eclipse.sirius.ecore.fixed.side.edges.design.commands.FixedSideEdgesChangeBendpointsOfEdgesCommand;

/**
 * {@link AirResizableEditPolicy} used to provide a specific move
 * {@link Command}.
 * 
 * @author <a href="mailto:steve.monnier@obeo.fr">Steve Monnier</a>
 *
 */
public class FixedSideEdgesAirResizableEditPolicy extends AirResizableEditPolicy {

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#getMoveCommand(org.eclipse.gef.requests.ChangeBoundsRequest)
     */
    @Override
    protected Command getMoveCommand(final ChangeBoundsRequest request) {
        ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_MOVE_CHILDREN);
        req.setEditParts(getHost());

        req.setMoveDelta(request.getMoveDelta());
        req.setSizeDelta(request.getSizeDelta());
        req.setLocation(request.getLocation());
        req.setExtendedData(request.getExtendedData());
        Command result = getHost().getParent().getCommand(req);
        // Command result = super.getMoveCommand(request);

        if (getHost().getParent() != null) {
            if (getHost() instanceof IGraphicalEditPart) {
                IGraphicalEditPart hostPart = (IGraphicalEditPart) getHost();
                CompositeTransactionalCommand ctc = new CompositeTransactionalCommand(hostPart.getEditingDomain(), result.getLabel());
                ctc.add(new CommandProxy(result));
                ctc.add(new FixedSideEdgesChangeBendpointsOfEdgesCommand(hostPart, new PrecisionPoint(request.getMoveDelta())));
                result = new ICommandProxy(ctc);
            }
            return result;
        } else {
            return null;
        }
    }

}
