/*******************************************************************************
 * Copyright (c) 2009, 2009 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.tools.api.draw2d.ui.figures;

import java.util.Iterator;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.AbstractDNodeContainerCompartmentEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

/**
 * A set of methods that are useful when manipulating figures on the real
 * coordinates system of the diagram (and not for the visible area like
 * translateToAbsolute or translateToRelative).
 * 
 * @author <a href="mailto:laurent.redor@obeo.fr">Laurent Redor</a>
 * 
 */
public final class FigureUtilities {
    /**
     * Constructor to prevent instantiation
     */
    private FigureUtilities() {
    }

    /**
     * <p>
     * Returns a Point representing the margins associated to the given figure
     * and its children.
     * </p>
     * <p>
     * Used to correct issues occurring during Node Creation and DnD inside
     * Containers. The bordered node is not concerned by this shift.
     * </p>
     * 
     * @param fig
     *            the figure to consider
     * @param isConcernedBorderedNode
     *            true if the shift concerned bordered node, false otherwise.
     * @param editPart
     *            the current edit part
     * @return a Point representing the margins associated to the given figure
     *         and its children
     */
    public static Point getShiftFromMarginOffset(ResizableCompartmentFigure fig, boolean isConcernedBorderedNode, EditPart editPart) {
        // Ignore shift for creation of bordered node.
        if (isConcernedBorderedNode) {
            return new Point(0, 0);
        }
        Point shift = new Point();
        // DnD and Node Creation in a container add extra x and y values of 5
        // pixels. If the target EditPart is an
        // AbstractDNodeContainerCompartmentEditPart, we consider the shift
        // Margins associated to the figure linked with this editPart
        if (editPart instanceof AbstractDNodeContainerCompartmentEditPart) {
            // Current figure border
            shiftBorderInsets(shift, fig);
            // Children figures
            Iterator<?> childrenFiguresIterator = fig.getChildren().iterator();
            while (childrenFiguresIterator.hasNext()) {
                Object next = childrenFiguresIterator.next();

                if (next instanceof IFigure) {
                    IFigure childrenFigure = (IFigure) next;
                    shiftBorderInsets(shift, childrenFigure);
                }
            }
        }
        return shift;
    }

    private static void shiftBorderInsets(Point shift, IFigure figure) {
        Border border = figure.getBorder();
        if (border != null) {
            Insets insets = border.getInsets(figure);
            if (insets != null) {
                shift.translate(insets.left, insets.top);
            }
        }
    }

    /**
     * Rotate an image. Code from
     * http://dev.eclipse.org/viewcvs/viewvc.cgi/org.eclipse
     * .swt.snippets/src/org/eclipse/swt/snippets/Snippet139.java?view=co
     * 
     * @param image
     *            the source
     * @param direction
     *            : SWT.LEFT will rotate by 90 degrees on the left, SWT.RIGHT
     *            will rotate by 90 degrees on the right, SWT.DOWN will rotate
     *            by 180 degrees.
     * @return the rotated image
     */
    public static Image rotate(Image image, int direction) {
        ImageData srcData = image.getImageData();
        int bytesPerPixel = srcData.bytesPerLine / srcData.width;
        int destBytesPerLine = (direction == SWT.DOWN) ? srcData.width * bytesPerPixel : srcData.height * bytesPerPixel;
        byte[] newData = new byte[srcData.data.length];

        boolean manageAlpha = srcData.alphaData != null;

        byte[] newAlphaData = null;
        if (manageAlpha)
            newAlphaData = new byte[srcData.alphaData.length];

        ImageData imgData = new ImageData((direction == SWT.DOWN) ? srcData.width : srcData.height, (direction == SWT.DOWN) ? srcData.height : srcData.width, srcData.depth, srcData.palette,
                destBytesPerLine, newData);
        if (manageAlpha)
            imgData.alphaData = newAlphaData;
        imgData.alpha = srcData.alpha;
        imgData.palette = srcData.palette;

        for (int srcY = 0; srcY < srcData.height; srcY++) {
            for (int srcX = 0; srcX < srcData.width; srcX++) {
                int destX = 0;
                int destY = 0;

                switch (direction) {
                case SWT.LEFT: // left 90 degrees
                    destX = srcY;
                    destY = srcData.width - srcX - 1;
                    break;
                case SWT.RIGHT: // right 90 degrees
                    destX = srcData.height - srcY - 1;
                    destY = srcX;
                    break;
                case SWT.DOWN: // 180 degrees
                    destX = srcData.width - srcX - 1;
                    destY = srcData.height - srcY - 1;
                    break;
                default:
                    break;
                }

                imgData.setPixel(destX, destY, srcData.getPixel(srcX, srcY));
                if (manageAlpha)
                    imgData.setAlpha(destX, destY, srcData.getAlpha(srcX, srcY));
            }
        }

        // rotate mask
        if (srcData.maskData != null) {
            imgData.maskData = new byte[srcData.maskData.length];
            for (int i = 0; i < imgData.maskData.length; i++) {

                imgData.maskData[i] = srcData.maskData[i];
            }
        }
        imgData.maskPad = srcData.maskPad;

        return new Image(image.getDevice(), imgData);
    }
}
