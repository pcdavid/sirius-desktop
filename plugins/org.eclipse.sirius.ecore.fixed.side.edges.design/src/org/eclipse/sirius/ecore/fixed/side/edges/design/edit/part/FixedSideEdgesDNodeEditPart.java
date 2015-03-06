/*******************************************************************************    
 * Copyright (c) 2015 Obeo and others. All rights 
 * reserved. This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which accompanies this    
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html  
 *  
 * Contributors: Obeo - initial API and implementation  
 *******************************************************************************/
package org.eclipse.sirius.ecore.fixed.side.edges.design.edit.part;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeEditPart;
import org.eclipse.sirius.diagram.ui.tools.api.figure.anchor.AnchorProvider;
import org.eclipse.sirius.diagram.ui.tools.api.graphical.edit.styles.IStyleConfigurationRegistry;
import org.eclipse.sirius.diagram.ui.tools.api.graphical.edit.styles.StyleConfiguration;
import org.eclipse.sirius.ecore.fixed.side.edges.design.figures.FixedSideEdgesAirDefaultSizeNodeFigure;
import org.eclipse.sirius.viewpoint.DStylizable;

/**
 * {@link DNodeEditPart} used to provide a new figure that will display fixed
 * connection anchors.
 * 
 * @author <a href="mailto:steve.monnier@obeo.fr">Steve Monnier</a>
 *
 */
public class FixedSideEdgesDNodeEditPart extends DNodeEditPart {

    private DefaultSizeNodeFigure nodePlate = null;

    public FixedSideEdgesDNodeEditPart(View view) {
        super(view);
    }

    /**
     * @not-generated
     */
    protected NodeFigure createNodePlate() {
        DefaultSizeNodeFigure result = new FixedSideEdgesAirDefaultSizeNodeFigure(10, 10, null);
        final EObject eObj = resolveSemanticElement();
        if (eObj instanceof DStylizable && eObj instanceof DDiagramElement) {
            final DStylizable viewNode = (DStylizable) eObj;
            final StyleConfiguration styleConfiguration = IStyleConfigurationRegistry.INSTANCE.getStyleConfiguration(((DDiagramElement) eObj).getDiagramElementMapping(), viewNode.getStyle());
            final AnchorProvider anchorProvider = styleConfiguration.getAnchorProvider();
            result = new FixedSideEdgesAirDefaultSizeNodeFigure(getMapMode().DPtoLP(5), getMapMode().DPtoLP(5), anchorProvider);
            nodePlate = result;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.diagram.edit.api.part.AbstractDiagramBorderNodeEditPart#activate()
     */
    @Override
    public void activate() {
        if (nodePlate instanceof FixedSideEdgesAirDefaultSizeNodeFigure)
            ((FixedSideEdgesAirDefaultSizeNodeFigure) nodePlate).setZoomManager(getZoomManager());
        super.activate();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.diagram.edit.api.part.AbstractDiagramBorderNodeEditPart#deactivate()
     */
    @Override
    public void deactivate() {
        super.deactivate();
        if (nodePlate instanceof FixedSideEdgesAirDefaultSizeNodeFigure)
            ((FixedSideEdgesAirDefaultSizeNodeFigure) nodePlate).setZoomManager(null);
    }

    /**
     * @not-generated
     */
    public IFigure getNodePlate() {
        return nodePlate;
    }

}
