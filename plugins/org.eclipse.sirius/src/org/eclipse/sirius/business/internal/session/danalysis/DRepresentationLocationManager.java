/*******************************************************************************
 * Copyright (c) 2017 THALES GLOBAL SERVICES and others,
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.internal.session.danalysis;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DView;

/**
 * This manager is responsible to retrieve or create the {@link Resource} of a
 * {@link DRepresentation}.
 * 
 * @author fbarbin
 *
 */
public class DRepresentationLocationManager {

    /**
     * Gets or creates (if it does not exist) the corresponding {@link Resource}
     * for the given representation.
     * 
     * @param representation
     *            the current representation.
     * @param dView
     *            the representation dview.
     * @return the representation resource.
     */
    public Resource getOrCreateRepresentationResource(DRepresentation representation, DView dView) {
        Resource aird = dView.eResource();
        if (aird != null && aird.getURI().isPlatformResource()) {
            ResourceSet resourceSet = aird.getResourceSet();
            URI uri = getURI(representation, aird, resourceSet);
            return aird.getResourceSet().createResource(uri);
        }

        return null;
    }

    private URI getURI(DRepresentation representation, Resource aird, ResourceSet resourceSet) {
        int count = 0;
        String pathName = createURIString(aird, representation, count++);
        URI uri = URI.createPlatformResourceURI(pathName, true);
        while (resourceSet.getResource(uri, false) != null) {
            pathName = createURIString(aird, representation, count++);
            uri = URI.createPlatformResourceURI(pathName, true);
        }
        return uri;
    }

    private String createURIString(Resource aird, DRepresentation representation, int count) {
        String fileName = representation.getName();
        if (count > 0) {
            fileName += String.valueOf(count);
        }
        return aird.getURI().toPlatformString(true).replace(".aird", "_aird") + "/" + fileName + ".repfile"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    }
}
