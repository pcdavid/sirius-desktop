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
package org.eclipse.sirius.ext.ui.rotatableimage.internal.editPart.listener;

import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.NodeListener;

/**
 * Propagate the figure listener to the connection figure.
 * 
 * @author hmarchadour
 * @author mbats
 */
public class PropagateFigureListenerAtConnectionFigure implements NodeListener {

    private FigureListener figureListener;

    /**
     * Constructor.
     * 
     * @param figureListener
     *            The figure listener
     */
    public PropagateFigureListenerAtConnectionFigure(FigureListener figureListener) {
        this.figureListener = figureListener;
    }

    /**
     * {@inheritDoc}
     * 
     * @see NodeListener#removingSourceConnection(ConnectionEditPart, int)
     */
    @Override
    public void removingSourceConnection(ConnectionEditPart connection, int index) {
        IFigure figure = connection.getFigure();
        figure.removeFigureListener(figureListener);
    }

    /**
     * {@inheritDoc}
     * 
     * @see NodeListener#removingTargetConnection(ConnectionEditPart, int)
     */
    @Override
    public void removingTargetConnection(ConnectionEditPart connection, int index) {
        IFigure figure = connection.getFigure();
        figure.removeFigureListener(figureListener);
    }

    /**
     * {@inheritDoc}
     * 
     * @see NodeListener#sourceConnectionAdded(ConnectionEditPart, int)
     */
    @Override
    public void sourceConnectionAdded(ConnectionEditPart connection, int index) {
        IFigure figure = connection.getFigure();
        figure.addFigureListener(figureListener);
    }

    /**
     * {@inheritDoc}
     * 
     * @see NodeListener#targetConnectionAdded(ConnectionEditPart, int)
     */
    @Override
    public void targetConnectionAdded(ConnectionEditPart connection, int index) {
        IFigure figure = connection.getFigure();
        figure.addFigureListener(figureListener);
    }
}
