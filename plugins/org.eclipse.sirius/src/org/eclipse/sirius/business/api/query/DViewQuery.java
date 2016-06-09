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
package org.eclipse.sirius.business.api.query;

import java.util.List;

import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.DView;

import com.google.common.collect.Lists;

/**
 * A class aggregating all the queries (read-only!) having a {@link DView} as a
 * starting point.
 * 
 * @author lfasani
 */
public final class DViewQuery {
    private static boolean activateTrace;
    static {
        activateTrace = Boolean.parseBoolean(System.getProperty("activate_trace_getLoadedRepresentations", Boolean.FALSE.toString())); //$NON-NLS-1$
    }

    private DView dView;

    /**
     * Create a new query.
     * 
     * @param dView
     *            the {@link DView} to query.
     */
    public DViewQuery(DView dView) {
        this.dView = dView;
    }

    /**
     * Get the {@link DRepresentation}s which are already loaded in the dView
     * resourceSet.
     * 
     * @return the loaded {@link DRepresentation}s
     */
    public List<DRepresentation> getLoadedRepresentations() {
        if (activateTrace) {
            Thread.dumpStack();
        }
        List<DRepresentation> representations = Lists.newArrayList();
        for (DRepresentationDescriptor repDescriptor : dView.getOwnedRepresentationDescriptors()) {
            DRepresentation representation = repDescriptor.getRepresentation();
            if (representation != null) {
                representations.add(representation);
            }
        }
        return representations;
    }

}
