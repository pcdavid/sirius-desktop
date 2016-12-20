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
package org.eclipse.sirius.ext.ui.rotatableimage.internal.figure;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.sirius.diagram.ui.tools.api.figure.AirDefaultSizeNodeFigure;
import org.eclipse.sirius.diagram.ui.tools.api.figure.anchor.AnchorProvider;

/**
 * The rotatable node figure.
 * 
 * @author hmarchadour
 * @author mbats
 */
public class RotatableNodeFigure extends AirDefaultSizeNodeFigure {

    /**
     * Constructor.
     * 
     * @param defSize
     *            The dimension
     * @param anchorProvider
     *            The anchor provider
     */
    public RotatableNodeFigure(Dimension defSize, AnchorProvider anchorProvider) {
        super(defSize, anchorProvider);
    }

    /**
     * Constructor.
     * 
     * @param width
     *            The width
     * @param height
     *            The height
     * @param anchorProvider
     *            The anchor provider
     */
    public RotatableNodeFigure(final int width, final int height, final AnchorProvider anchorProvider) {
        super(width, height, anchorProvider);
    }

    @Override
    public ConnectionAnchor getTargetConnectionAnchorAt(Point p) {
        return new CenteredAnchor(this);
    }

    @Override
    public ConnectionAnchor getSourceConnectionAnchorAt(Point p) {
        return new CenteredAnchor(this);
    }
}
