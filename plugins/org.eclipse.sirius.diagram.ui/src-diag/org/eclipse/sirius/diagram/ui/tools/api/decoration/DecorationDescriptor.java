/*******************************************************************************
 * Copyright (c) 2016 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.tools.api.decoration;

import org.eclipse.draw2d.IFigure;
import org.eclipse.sirius.viewpoint.description.DecorationDistributionDirection;
import org.eclipse.sirius.viewpoint.description.Position;
import org.eclipse.swt.graphics.Image;

/**
 * Simple POJO that contains informations needed to display decoration.
 * 
 * @author <a href="mailto:laurent.fasani@obeo.fr">Laurent Fasani</a>
 */
public class DecorationDescriptor {

    /**
     * Display priority.
     */
    public enum DisplayPriority {
        /**
         * Highest priority.
         */
        HIGH_PRIORITY(0),
        /**
         * Normal priority.
         */
        NORMAL_PRIORITY(1);
        private final int value;

        DisplayPriority(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    };

    /**
     * Display priority.
     */
    public enum DecorationType {
        /**
         * Decoration on shape.
         */
        SHAPE,
        /**
         * Decoration on connection.
         */
        CONNECTION;
    };

    /**
     * Decoration tooltip if defined as string.
     */
    protected String tooltipAsString;

    /**
     * Decoration tooltip if defined as string.
     */
    protected IFigure tooltipAsFigure;

    private Integer displayPriority;

    private Position position;

    private DecorationDistributionDirection distributionDirection;

    private Image decorationAsImage;

    private IFigure decorationAsFigure;

    private String name;

    public Integer getDisplayPriority() {
        return displayPriority;
    }

    public void setDisplayPriority(Integer displayPriority) {
        this.displayPriority = displayPriority;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public DecorationDistributionDirection getDistributionDirection() {
        return distributionDirection;
    }

    public void setDistributionDirection(DecorationDistributionDirection distributionDirection) {
        this.distributionDirection = distributionDirection;
    }

    public Image getDecorationAsImage() {
        return decorationAsImage;
    }

    public void setDecorationAsImage(Image decorationAsImage) {
        this.decorationAsImage = decorationAsImage;
    }

    public IFigure getDecorationAsFigure() {
        return decorationAsFigure;
    }

    public void setDecorationAsFigure(IFigure decorationAsFigure) {
        this.decorationAsFigure = decorationAsFigure;
    }

    public String getTooltipAsString() {
        return tooltipAsString;
    }

    public void setTooltipAsString(String tooltipAsString) {
        this.tooltipAsString = tooltipAsString;
    }

    public IFigure getTooltipAsFigure() {
        return tooltipAsFigure;
    }

    public void setTooltipAsFigure(IFigure tooltipAsFigure) {
        this.tooltipAsFigure = tooltipAsFigure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
