/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ext.ui.rotatableimage.internal.editPart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeListener;
import org.eclipse.gmf.runtime.diagram.ui.editparts.AbstractBorderedShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderedShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderedNodeFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.IBorderItemLocator;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.CustomStyle;
import org.eclipse.sirius.diagram.ui.edit.api.part.IDiagramBorderNodeEditPart;
import org.eclipse.sirius.diagram.ui.tools.api.figure.locator.DBorderItemLocator;
import org.eclipse.sirius.ext.ui.rotatableimage.RotatableImagePlugin;
import org.eclipse.sirius.ext.ui.rotatableimage.internal.RotatableImageExtension;
import org.eclipse.sirius.ext.ui.rotatableimage.internal.editPart.listener.PropagateFigureListenerAtConnectionFigure;
import org.eclipse.sirius.ext.ui.rotatableimage.internal.figure.RotatableWorkspaceImageFigure;
import org.eclipse.sirius.ext.ui.rotatableimage.internal.figure.listener.RotatableImageListener;

/**
 * Edit Part for bordered node rotatable image.
 * 
 * @author arichard
 * @author mbats
 */
public class RotatableBorderedNodeEditPart extends AbstractRotatableImageEditPart {

    /**
     * The rotatable bordered node edit part.
     * 
     * @param view
     *            The GMF view
     */
    public RotatableBorderedNodeEditPart(View view) {
        super(view);
    }

    @Override
    public void figureHasChanged() {
        if (getContainerBorderedNodeFigure() != null) {
            int side = DBorderItemLocator.findClosestSideOfParent(primaryShape.getBounds(), getContainerBorderedNodeFigure().getBounds());
            switch (side) {
            case PositionConstants.SOUTH:
                setFigureAtSouth();
                break;
            case PositionConstants.NORTH:
                setFigureAtNorth();
                break;
            case PositionConstants.WEST:
                setFigureAtWest();
                break;
            case PositionConstants.EAST:
                setFigureAtEast();
                break;
            default:
                setFigureAtNorth();
                break;
            }
        }

    }

    @Override
    protected IFigure createNodeShape() {
        CustomStyle imageStyle = (CustomStyle) resolveSemanticElement();

        RotatableImageExtension imageExtension = RotatableImagePlugin.getDefault().getRotatableImage(imageStyle.getId());

        primaryShape = new RotatableWorkspaceImageFigure(imageExtension.getMode(), imageExtension.getNorthImage(), imageExtension.getWestImagePath(), imageExtension.getSouthImage(),
                imageExtension.getEastImagePath());

        listener = new RotatableImageListener(this);
        primaryShape.addFigureListener(listener);

        EditPart parentEditPart = getParent();
        if (parentEditPart instanceof GraphicalEditPart) {
            GraphicalEditPart parentGraphicalEditPart = (GraphicalEditPart) parentEditPart;
            NodeListener dEdgeEditPartListener = new PropagateFigureListenerAtConnectionFigure(listener);
            parentGraphicalEditPart.addNodeListener(dEdgeEditPartListener);
        }

        return primaryShape;
    }

    /**
     * Get the container bordered node figure.
     * 
     * @return the bordered node figure
     */
    private BorderedNodeFigure getContainerBorderedNodeFigure() {
        BorderedNodeFigure borderedNodeFigure = null;
        IBorderItemLocator borderItemLocator = getBorderItemLocator();
        if (borderItemLocator != null) {
            IBorderedShapeEditPart borderNodeEditPart = getContainerBorderedNodeEditPart();
            if (borderNodeEditPart != null) {
                borderedNodeFigure = borderNodeEditPart.getBorderedFigure();
            }
        }
        return borderedNodeFigure;
    }

    /**
     * Get the container bordered node edit part.
     * 
     * @return the container bordered node edit part
     */
    private IBorderedShapeEditPart getContainerBorderedNodeEditPart() {
        IGraphicalEditPart current = this;
        IBorderedShapeEditPart borderedNodeEditPart = null;
        while (current != null && borderedNodeEditPart == null) {
            if (current instanceof AbstractBorderedShapeEditPart) {
                borderedNodeEditPart = (IBorderedShapeEditPart) current;
            }
            current = (IGraphicalEditPart) current.getParent();
        }
        return borderedNodeEditPart;
    }

    /**
     * Get the border item locator.
     * 
     * @return the border item locator
     */
    private IBorderItemLocator getBorderItemLocator() {
        IBorderItemLocator borderItemLocator = null;
        IDiagramBorderNodeEditPart borderNodeEditPart = getDiagramBorderedNodeEditPart();

        if (borderNodeEditPart instanceof IBorderItemEditPart) {
            borderItemLocator = ((IBorderItemEditPart) borderNodeEditPart).getBorderItemLocator();
        }

        return borderItemLocator;
    }

    /**
     * Get the diagram bordered node edit part.
     * 
     * @return the diagram bordered node edit part
     */
    private IDiagramBorderNodeEditPart getDiagramBorderedNodeEditPart() {
        EditPart current = this;
        IDiagramBorderNodeEditPart borderNodeEditPart = null;
        while (current != null && borderNodeEditPart == null) {
            if (current instanceof IDiagramBorderNodeEditPart) {
                borderNodeEditPart = (IDiagramBorderNodeEditPart) current;
            }
            current = current.getParent();
        }
        return borderNodeEditPart;
    }

}
