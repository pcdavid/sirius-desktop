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

package org.eclipse.sirius.ext.ui.rotatableimage.internal;

/**
 * Extension point to describe a rotatable image.
 * 
 * @author nlepine
 * @author hmarchadour
 * @author mbats
 */
public class RotatableImageExtension {

    /**
     * mode : 0 -> 4 images.
     */
    public static final int IMAGE = 0;

    /**
     * mode : 1 image with rotation.
     */
    public static final int ROTATION = 1;

    /**
     * East image attribute corresponding to rotatable image extension point.
     */
    public static final String EAST_IMAGE_ATTRIBUTE = "east";

    /**
     * West image attribute corresponding to rotatable image extension point.
     */
    public static final String WEST_IMAGE_ATTRIBUTE = "west";

    /**
     * South image attribute corresponding to rotatable image extension point.
     */
    public static final String SOUTH_IMAGE_ATTRIBUTE = "south";

    /**
     * North image attribute corresponding to rotatable image extension point.
     */
    public static final String NORTH_IMAGE_ATTRIBUTE = "north";

    /**
     * Identifier attribute corresponding to rotatable image extension point.
     */
    public static final String ID_ATTRIBUTE = "id";

    /**
     * The rotatable image extension point id.
     */
    public static final String ROTATABLE_IMAGE_EXTENSION_POINT_ID = "org.eclipse.sirius.rotatableimage";

    /**
     * Rotatable image extension id.
     */
    protected String id;

    /**
     * Rotatable image extension mode : IMAGE or ROTATION.
     */
    private int mode;

    /**
     * Extension north image path.
     */
    private String northImagePath;

    /**
     * Extension south image path.
     */
    private String southImagePath;

    /**
     * Extension west image path.
     */
    private String westImagePath;

    /**
     * Extension east image path.
     */
    private String eastImagePath;

    /**
     * Constructor.
     * 
     * @param id
     *            The identifier
     * @param northImagePath
     *            The north image path
     * @param southImagePath
     *            The south image path
     * @param westImagePath
     *            The west image path
     * @param eastImagePath
     *            The east image path
     */
    public RotatableImageExtension(String id, String northImagePath, String southImagePath, String westImagePath, String eastImagePath) {
        this.id = id;
        this.northImagePath = northImagePath;
        this.southImagePath = southImagePath;
        this.westImagePath = westImagePath;
        this.eastImagePath = eastImagePath;
        this.mode = ROTATION;
        if (northImagePath != null && southImagePath != null && westImagePath != null && eastImagePath != null) {
            this.mode = IMAGE;
        }
    }

    /**
     * Get the identifier.
     * 
     * @return the identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Get the mode.
     * 
     * @return the mode
     */
    public int getMode() {
        return mode;
    }

    /**
     * Get the north image path.
     * 
     * @return the north image path
     */
    public String getNorthImage() {
        return northImagePath;
    }

    /**
     * Get the south image path.
     * 
     * @return the south image path
     */
    public String getSouthImage() {
        return southImagePath;
    }

    /**
     * Get the west image path.
     * 
     * @return the west image path
     */
    public String getWestImagePath() {
        return westImagePath;
    }

    /**
     * Get the east image path.
     * 
     * @return the east image path
     */
    public String getEastImagePath() {
        return eastImagePath;
    }
}
