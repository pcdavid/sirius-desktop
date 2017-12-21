/*******************************************************************************
 * Copyright (c) 2018 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.tools.internal.figure;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.sirius.diagram.ui.tools.api.figure.WorkspaceImageFigure;
import org.eclipse.swt.graphics.Image;

/**
 * A Layer used to paint image.
 * 
 * @author jmallet
 *
 */
public class BackgroundLayerWithImage extends FreeformLayer {

    /* Image used for the diagram background */
    private Image image;

    /* Transparency used to display image in the diagram background */
    private int transparency;

    /**
     * Constructor.
     * 
     * @param path
     *            path of the image to display in background
     * @param theTransparency
     *            transparency of the image to display
     */
    public BackgroundLayerWithImage(String path, int theTransparency) {
        super();
        this.transparency = theTransparency;
        setImage(WorkspaceImageFigure.getImageInstanceFromPath(path));

    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    protected void paintFigure(Graphics graphics) {
        if (getImage() != null) {
            Rectangle targetRect = getBounds().getCopy();
            org.eclipse.swt.graphics.Rectangle imgBox = getImage().getBounds();
            Point location = targetRect.getTopLeft();
            // Set Transparency
            graphics.setAlpha(transparency);

            // Mode with images repeated
            int nbHorizontalImages = targetRect.width / imgBox.width;
            int nbVerticalImages = targetRect.height / imgBox.height;
            for (int i = 0; i < nbHorizontalImages + 1; i++) {
                for (int j = 0; j < nbVerticalImages + 1; j++) {
                    graphics.drawImage(getImage(), location.x + i * imgBox.width, location.y + j * imgBox.height);
                }
            }

            // Mode with stretched image
            // graphics.drawImage(getImage(), 0, 0, imgBox.width, imgBox.height, targetRect.x, targetRect.y,
            // targetRect.width, targetRect.height);

            // Mode with centered image
            // graphics.drawImage(getImage(), location.x + targetRect.width / 2 - imgBox.width / 2, location.y +
            // targetRect.height / 2 - imgBox.height / 2);

            // Mode with image fit as possible to the diagram
            // double scale = Math.min((double) targetRect.width / imgBox.width, (double) targetRect.height /
            // imgBox.height);
            // int imageWidth = (int) (scale * imgBox.width);
            // int imageHeight = (int) (scale * imgBox.height);
            // graphics.drawImage(getImage(), 0, 0, imgBox.width, imgBox.height, location.x + targetRect.width / 2 -
            // imageWidth / 2, location.y + targetRect.height / 2 - imageHeight / 2, imageWidth,
            // imageHeight);
        }
        super.paintFigure(graphics);
    }
}
