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

import java.util.List;

import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.ui.edit.api.part.IStyleEditPart;

/**
 * Edit part for rotatable image based on the first connection.
 * 
 * @author hmarchadour
 * @author mbats
 */
public class RotatableImageBasedOnFirstConnectionEditPart extends AbstractRotatableImageEditPart implements IStyleEditPart {

    private static final int BASE_ANGLE = 45;

    /**
     * Constructor.
     * 
     * @param view
     *            the GMF view.
     */
    public RotatableImageBasedOnFirstConnectionEditPart(View view) {
        super(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void figureHasChanged() {
        EditPart parentEditPart = getParent();
        if (parentEditPart instanceof GraphicalEditPart) {
            GraphicalEditPart parentGraphicalEditPart = (GraphicalEditPart) parentEditPart;
            @SuppressWarnings("unchecked")
            List<Object> sourceConnections = parentGraphicalEditPart.getSourceConnections();
            @SuppressWarnings("unchecked")
            List<Object> targetConnections = parentGraphicalEditPart.getTargetConnections();
            if (sourceConnections.size() > 0) {
                Object sourceConnection = sourceConnections.get(0);
                if (sourceConnection instanceof ConnectionNodeEditPart) {
                    ConnectionNodeEditPart dEdgeSourceConnection = (ConnectionNodeEditPart) sourceConnection;
                    PolylineConnection polylineConnection = (PolylineConnection) dEdgeSourceConnection.getFigure();
                    double angle = getFirstSegmentAngle(polylineConnection);
                    if (angle > BASE_ANGLE && angle <= BASE_ANGLE * 3) {
                        setFigureAtSouth();
                    } else if (angle > BASE_ANGLE * 3 && angle <= BASE_ANGLE * 5) {
                        setFigureAtEast();
                    } else if (angle > BASE_ANGLE * 5 && angle <= BASE_ANGLE * 7) {
                        setFigureAtNorth();
                    } else if ((angle > BASE_ANGLE * 7 && angle <= BASE_ANGLE * 8) || (angle >= 0 && angle <= BASE_ANGLE * 3)) {
                        setFigureAtWest();
                    }

                }
            } else if (targetConnections.size() > 0) {
                Object targetConnection = targetConnections.get(0);
                if (targetConnection instanceof ConnectionNodeEditPart) {
                    ConnectionNodeEditPart dEdgeSourceConnection = (ConnectionNodeEditPart) targetConnection;
                    PolylineConnection polylineConnection = (PolylineConnection) dEdgeSourceConnection.getFigure();
                    double angle = getFirstSegmentAngle(polylineConnection);
                    if (angle > BASE_ANGLE && angle <= BASE_ANGLE * 3) {
                        setFigureAtNorth();
                    } else if (angle > BASE_ANGLE * 3 && angle <= BASE_ANGLE * 5) {
                        setFigureAtWest();
                    } else if (angle > BASE_ANGLE * 5 && angle <= BASE_ANGLE * 7) {
                        setFigureAtSouth();
                    } else if ((angle > BASE_ANGLE * 7 && angle <= BASE_ANGLE * 8) || (angle >= 0 && angle <= BASE_ANGLE * 3)) {
                        setFigureAtEast();
                    }

                }
            }
        }
    }

}
