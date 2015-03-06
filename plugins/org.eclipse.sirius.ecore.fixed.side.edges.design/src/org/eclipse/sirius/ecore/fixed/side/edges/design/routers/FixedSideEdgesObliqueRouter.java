/*******************************************************************************    
 * Copyright (c) 2015 Obeo and others. All rights 
 * reserved. This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which accompanies this    
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html  
 *  
 * Contributors: Obeo - initial API and implementation  
 *******************************************************************************/
package org.eclipse.sirius.ecore.fixed.side.edges.design.routers;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ObliqueRouter;

/**
 * {@link ObliqueRouter} that overrides routeLine in order to skip the final
 * step which calls resetEndPointsToEdge.
 * 
 * @author <a href="mailto:steve.monnier@obeo.fr">Steve Monnier</a>
 *
 */
public class FixedSideEdgesObliqueRouter extends ObliqueRouter {

    @Override
    public void routeLine(Connection conn, int nestedRoutingDepth, PointList newLine) {
        // get the original line
        if (!checkSelfRelConnection(conn, newLine) && !checkShapesIntersect(conn, newLine)) {
            removePointsInViews(conn, newLine);
        }
    }

}
