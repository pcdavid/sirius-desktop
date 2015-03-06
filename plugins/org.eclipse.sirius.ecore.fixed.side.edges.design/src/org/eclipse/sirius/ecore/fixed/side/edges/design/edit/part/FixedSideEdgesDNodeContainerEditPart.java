/*******************************************************************************    
 * Copyright (c) 2015 Obeo and others. All rights 
 * reserved. This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which accompanies this    
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html  
 *  
 * Contributors: Obeo - initial API and implementation  
 *******************************************************************************/
package org.eclipse.sirius.ecore.fixed.side.edges.design.edit.part;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderedNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.SlidableAnchor;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeContainerEditPart;

/**
 * {@link DNodeContainerEditPart} used to override the source and target
 * {@link ConnectionAnchor} to fixed location.
 * 
 * @author <a href="mailto:steve.monnier@obeo.fr">Steve Monnier</a>
 *
 */
public class FixedSideEdgesDNodeContainerEditPart extends DNodeContainerEditPart {

    public FixedSideEdgesDNodeContainerEditPart(View view) {
        super(view);
    }

    @Override
    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connEditPart) {
        if (getNodeFigure() instanceof BorderedNodeFigure) {
            return new SlidableAnchor(((BorderedNodeFigure) getNodeFigure()).getMainFigure(), new PrecisionPoint(0.5, 1));
        }
        return super.getSourceConnectionAnchor(connEditPart);
    }

    @Override
    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connEditPart) {
        if (getNodeFigure() instanceof BorderedNodeFigure) {
            return new SlidableAnchor(((BorderedNodeFigure) getNodeFigure()).getMainFigure(), new PrecisionPoint(0.75, 0.25));
        }
        return super.getTargetConnectionAnchor(connEditPart);
    }

}
