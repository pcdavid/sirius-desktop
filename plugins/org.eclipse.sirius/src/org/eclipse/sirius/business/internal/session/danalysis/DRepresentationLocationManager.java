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

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.business.api.helper.SiriusUtil;
import org.eclipse.sirius.common.tools.api.util.EclipseUtil;
import org.eclipse.sirius.viewpoint.DRepresentation;

import com.google.common.collect.Lists;

/**
 * This manager is responsible to retrieve or create the {@link Resource} of a {@link DRepresentation}.
 * 
 * @author fbarbin
 *
 */
public class DRepresentationLocationManager {

    /**
     * Get or creates (if it does not exist) the corresponding {@link Resource} for the given representation.
     * 
     * @param representation
     *            the current representation.
     * 
     * @param aird
     *            the aird resource in which the representation will be referenced.
     * @return the representation resource.
     */
    public Resource getOrCreateRepresentationResource(DRepresentation representation, Resource aird) {
        if (aird != null && aird.getURI().isPlatformResource()) {
            ResourceSet resourceSet = aird.getResourceSet();
            URI uri = getURI(representation, aird, resourceSet);
            Resource newResource = aird.getResourceSet().createResource(uri);
            return newResource;
        }
        return null;
    }

    private URI getURI(DRepresentation representation, Resource aird, ResourceSet resourceSet) {
        URI uri = getURIFromExtensionPoint(representation, aird);
        if (uri == null) {
            int count = 0;
            String pathName = createURIString(aird, representation, count++);
            uri = URI.createPlatformResourceURI(pathName, true);
            while (resourceSet.getResource(uri, false) != null) {
                pathName = createURIString(aird, representation, count++);
                uri = URI.createPlatformResourceURI(pathName, true);
            }
        }
        return uri;
    }

    private URI getURIFromExtensionPoint(DRepresentation representation, Resource aird) {
        List<DRepresentationLocationRule> extensionPointRules = Lists.newArrayList();
        extensionPointRules.addAll(EclipseUtil.getExtensionPlugins(DRepresentationLocationRule.class, DRepresentationLocationRule.ID, DRepresentationLocationRule.CLASS_ATTRIBUTE));
        for (DRepresentationLocationRule locationRule : extensionPointRules) {
            URI uri = locationRule.getResourceURI(representation, aird);
            if (uri != null) {
                return uri;
            }
        }
        return null;
    }

    private String createURIString(Resource aird, DRepresentation representation, int count) {
        String fileName = representation.getName().replace(' ', '_');
        if (count > 0) {
            fileName += String.valueOf(count);
        }
        return aird.getURI().toPlatformString(true).replace(".aird", "_aird") + "/" + fileName + "." + SiriusUtil.REPRESENTATION_FILE_EXTENSION; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
}
