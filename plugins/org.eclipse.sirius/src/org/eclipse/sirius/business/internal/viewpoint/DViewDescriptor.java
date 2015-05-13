/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.internal.viewpoint;

import java.util.List;
import com.google.common.collect.Lists;

/**
 * Represents a {@link DView} with only basic informations.
 * 
 * @author <a href="mailto:laurent.fasani@obeo.fr">Laurent Fasani</a>
 */
public final class DViewDescriptor {

    /**
     * DView.viewpoint
     */
    private ViewPointDescriptor viewPoint;

    /**
     * DView.ownedRepresentations
     */
    private List<DRepresentationDescriptor> ownedRepresentations = Lists.newArrayList();

    /**
     * Default constructor of this class.
     */
    public DViewDescriptor() {
    }

    public ViewPointDescriptor getViewPoint() {
        return viewPoint;
    }

    public void setViewPoint(ViewPointDescriptor viewPoint) {
        this.viewPoint = viewPoint;
    }

    public List<DRepresentationDescriptor> getOwnedRepresentations() {
        return ownedRepresentations;
    }

    public void setOwnedRepresentations(List<DRepresentationDescriptor> ownedRepresentations) {
        this.ownedRepresentations = ownedRepresentations;
    }
}
