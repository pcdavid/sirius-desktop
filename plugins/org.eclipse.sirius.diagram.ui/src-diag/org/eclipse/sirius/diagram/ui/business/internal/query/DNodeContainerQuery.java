/*******************************************************************************
 * Copyright (c) 2014, 2022 THALES GLOBAL SERVICES.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.business.internal.query;

import java.util.Objects;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.FlatContainerStyle;
import org.eclipse.sirius.diagram.model.business.internal.query.DDiagramElementContainerExperimentalQuery;
import org.eclipse.sirius.diagram.model.business.internal.query.DNodeContainerExperimentalQuery;
import org.eclipse.sirius.diagram.ui.tools.api.layout.LayoutUtils;

/**
 * Queries relative to a DNodeContainer.
 * 
 * @author <a href="mailto:laurent.redor@obeo.fr">Laurent Redor</a>
 */
public class DNodeContainerQuery {

    private final DNodeContainer container;

    /**
     * Constructor.
     * 
     * @param container
     *            the container to query.
     */
    public DNodeContainerQuery(DNodeContainer container) {
        this.container = Objects.requireNonNull(container);
    }

    /**
     * Return the default draw2D dimension according to the specified DNodeContainer.
     * 
     * @return the default draw2D dimension according to the specified DNodeContainer.
     */
    public Dimension getDefaultDimension() {
        Dimension defaultSize = new Dimension(LayoutUtils.NEW_DEFAULT_CONTAINER_DIMENSION);

        /*
         * here we need to set a default size to 150, 70 because this was the image size before the 4.0
         */
        if (container.getOwnedStyle() instanceof FlatContainerStyle && !new DDiagramElementContainerExperimentalQuery(container).isRegion()
                && !new DNodeContainerExperimentalQuery(container).isRegionContainer()) {
            defaultSize.setSize(LayoutUtils.DEFAULT_CONTAINER_DIMENSION);
        }
        // Use the width and the height of the DNC if they are set
        if (container.getWidth() != null && container.getWidth().intValue() > 0) {
            defaultSize.setWidth(container.getWidth().intValue() * LayoutUtils.SCALE);
        }
        if (container.getHeight() != null && container.getHeight().intValue() > 0) {
            defaultSize.setHeight(container.getHeight().intValue() * LayoutUtils.SCALE);
        }

        return defaultSize;
    }

}
