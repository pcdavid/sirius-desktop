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

import org.eclipse.sirius.diagram.DiagramFactory;
import org.eclipse.sirius.diagram.WorkspaceImage;
import org.eclipse.sirius.diagram.ui.tools.api.draw2d.ui.figures.FigureUtilities;
import org.eclipse.sirius.diagram.ui.tools.api.figure.WorkspaceImageFigure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

/**
 * Rotatable Workspace Image Figure : switch mode ROTATION or IMAGE, rotate the
 * specific image or display four images in North South East and West.
 * 
 * @author nlepine
 * @author hmarchadour
 * @author mbats
 */
public class RotatableWorkspaceImageFigure extends WorkspaceImageFigure {

    private String southImgPath;

    private String westImgPath;

    private String eastImgPath;

    private String northImgPath;

    private String currentImgPath;

    private int currentImgDirection;

    /**
     * Creates a rotative image
     * 
     * @param path
     *            the path of the top image.
     */
    /**
     * Create a rotatable image.
     * 
     * @param mode
     *            Rotation mode
     * @param northImgPath
     *            The north image path
     * @param westImgPath
     *            The west image path
     * @param southImgPath
     *            The south image path
     * @param eastImgPath
     *            The east image path
     */
    public RotatableWorkspaceImageFigure(int mode, String northImgPath, String westImgPath, String southImgPath, String eastImgPath) {
        super(WorkspaceImageFigure.flyWeightImage(northImgPath));

        this.northImgPath = northImgPath;
        this.southImgPath = southImgPath;
        this.westImgPath = westImgPath;
        this.eastImgPath = eastImgPath;

        this.currentImgPath = northImgPath;
        this.currentImgDirection = SWT.TOP;
        refreshFigure();
    }

    /**
     * Refresh the figure.
     */
    public void refreshFigure() {
        WorkspaceImage createWorkspaceImage = DiagramFactory.eINSTANCE.createWorkspaceImage();
        createWorkspaceImage.setWorkspacePath(currentImgPath);
        refreshFigure(createWorkspaceImage);
    }

    /**
     * Set the south image as the current image.
     */
    public void setSouthImgAsCurrent() {
        if (southImgPath != null) {
            currentImgPath = southImgPath;
        } else {
            currentImgPath = northImgPath;
        }
        currentImgDirection = SWT.DOWN;
        refreshFigure();
    }

    /**
     * Set the west image as the current image.
     */
    public void setWestImgAsCurrent() {
        if (westImgPath != null) {
            currentImgPath = westImgPath;
        } else {
            currentImgPath = northImgPath;
        }
        currentImgDirection = SWT.LEFT;
        refreshFigure();
    }

    /**
     * Set the east image as the current image.
     */
    public void setEastImgAsCurrent() {
        if (eastImgPath != null) {
            currentImgPath = eastImgPath;
        } else {
            currentImgPath = northImgPath;
        }
        currentImgDirection = SWT.RIGHT;
        refreshFigure();
    }

    /**
     * Set the north image as the current image.
     */
    public void setNorthImgAsCurrent() {
        if (northImgPath != null) {
            currentImgPath = northImgPath;
        }
        currentImgDirection = SWT.TOP;
        refreshFigure();
    }

    @Override
    public Image getImage() {
        Image img = super.getImage();
        if (currentImgPath != null && currentImgPath.equals(northImgPath) && currentImgDirection != SWT.TOP) {
            img = FigureUtilities.rotate(img, currentImgDirection);
        }
        return img;
    }

}
