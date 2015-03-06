/*******************************************************************************    
 * Copyright (c) 2015 Obeo and others. All rights 
 * reserved. This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which accompanies this    
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html  
 *  
 * Contributors: Obeo - initial API and implementation  
 *******************************************************************************/
package org.eclipse.sirius.ecore.fixed.side.edges.design.figures;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.sirius.diagram.ui.tools.api.figure.AirDefaultSizeNodeFigure;
import org.eclipse.sirius.diagram.ui.tools.api.figure.anchor.AnchorProvider;

/**
 * {@link AirDefaultSizeNodeFigure} used to provide a fixed connection anchors
 * by overriding the createConnectionAnchor method.
 * 
 * @author <a href="mailto:steve.monnier@obeo.fr">Steve Monnier</a>
 *
 */
public class FixedSideEdgesAirDefaultSizeNodeFigure extends AirDefaultSizeNodeFigure {

    public FixedSideEdgesAirDefaultSizeNodeFigure(int width, int height, AnchorProvider anchorProvider) {
        super(width, height, anchorProvider);
    }

    @Override
    protected ConnectionAnchor createConnectionAnchor(final Point p) {
        return createAnchor(new PrecisionPoint(1, 0.25));
    }

}
