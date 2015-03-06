/*******************************************************************************    
 * Copyright (c) 2015 Obeo and others. All rights 
 * reserved. This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which accompanies this    
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html  
 *  
 * Contributors: Obeo - initial API and implementation  
 *******************************************************************************/
package org.eclipse.sirius.ecore.fixed.side.edges.design.edit.part;

import org.eclipse.draw2d.AutomaticRouter;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.ConnectionLayerEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.FanRouter;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.ObliqueRouter;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Routing;
import org.eclipse.gmf.runtime.notation.RoutingStyle;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DEdgeEditPart;
import org.eclipse.sirius.ecore.fixed.side.edges.design.routers.FixedSideEdgesObliqueRouter;

/**
 * Specific {@link DEdgeEditPart} that install a
 * {@link FixedSideEdgesObliqueRouter} instead of the generic
 * {@link ObliqueRouter}.
 * 
 * @author <a href="mailto:steve.monnier@obeo.fr">Steve Monnier</a>
 *
 */
public class FixedSideEdgeEditPart extends DEdgeEditPart {

    private ConnectionRouter obliqueRouter;

    public FixedSideEdgeEditPart(View view) {
        super(view);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void installRouter() {
        ConnectionLayer cLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
        RoutingStyle style = (RoutingStyle) ((View) getModel()).getStyle(NotationPackage.Literals.ROUTING_STYLE);

        if (style != null && cLayer instanceof ConnectionLayerEx) {

            ConnectionLayerEx cLayerEx = (ConnectionLayerEx) cLayer;
            Routing routing = style.getRouting();
            if (Routing.MANUAL_LITERAL == routing) {
                // getConnectionFigure().setConnectionRouter(
                // cLayerEx.getObliqueRouter());
                if (obliqueRouter == null) {
                    AutomaticRouter router = new FanRouter();
                    router.setNextRouter(new FixedSideEdgesObliqueRouter());
                    obliqueRouter = router;
                }
                getConnectionFigure().setConnectionRouter(obliqueRouter);
            } else if (Routing.RECTILINEAR_LITERAL == routing) {
                getConnectionFigure().setConnectionRouter(cLayerEx.getRectilinearRouter());
            } else if (Routing.TREE_LITERAL == routing) {
                getConnectionFigure().setConnectionRouter(cLayerEx.getTreeRouter());
            }

        }

        refreshRouterChange();
    }

}
