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

import org.eclipse.emf.common.util.URI;
import org.eclipse.sirius.business.api.resource.ResourceDescriptor;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * Represents a {@link DAnalysis} with only basic informations.
 * 
 * @author <a href="mailto:laurent.fasani@obeo.fr">Laurent Fasani</a>
 */
public final class DAnalysisDescriptor {

    private final URI resourceURI;

    /**
     * DAnalysis.semanticResources
     */
    private List<ResourceDescriptor> semanticResources = Lists.newArrayList();

    /**
     * DAnalysis.ownedViews
     */
    private List<DViewDescriptor> ownedViews = Lists.newArrayList();

    /**
     * DView.viewpoint
     */
    private List<DAnalysisDescriptor> referencedAnalysis = Lists.newArrayList();

    /**
     * This constructor is used by EMF to deserialized the instances of this
     * class.
     * 
     * @param serializedString
     *            used to initialize the fields of the class. This string has
     *            been previously serialized with <code>toString()</code>.
     */
    public DAnalysisDescriptor(String serializedString) {
        this(getURIFromSerializedString(serializedString));
    }

    /**
     * This constructor allows initializing the characterizing fields of this
     * class.
     * 
     * @param uri
     *            uri of the resource
     */
    public DAnalysisDescriptor(URI uri) {
        resourceURI = Preconditions.checkNotNull(uri);
    }

    private static URI getURIFromSerializedString(String serializedString) {
        URI uri = null;
        String[] split = serializedString.split("#");
        if (split.length == 2) {
            // uri = URI.createURI(split[0], true,
            // URI.FRAGMENT_FIRST_SEPARATOR);
            uri = URI.createPlatformResourceURI(split[0], true);
        }
        return uri;
    }

    /**
     * Return the URI of the resource.
     * 
     * @return the uri of the resource
     */
    public URI getResourceURI() {
        return resourceURI;
    }

    public List<ResourceDescriptor> getSemanticResources() {
        return semanticResources;
    }

    public void setSemanticResources(List<ResourceDescriptor> semanticResources) {
        this.semanticResources = semanticResources;
    }

    public List<DViewDescriptor> getOwnedViews() {
        return ownedViews;
    }

    public void setOwnedViews(List<DViewDescriptor> ownedViews) {
        this.ownedViews = ownedViews;
    }

    public List<DAnalysisDescriptor> getReferencedAnalysis() {
        return referencedAnalysis;
    }

    public void setReferencedAnalysis(List<DAnalysisDescriptor> referencedAnalysis) {
        this.referencedAnalysis = referencedAnalysis;
    }

    @Override
    public String toString() {
        return resourceURI.toString();
    }

    @Override
    public int hashCode() {
        return resourceURI.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        // URI does NOT overload equals method but the instance of URI is
        // unique so we can use the equals method.
        return obj instanceof ResourceDescriptor && this.resourceURI.equals(((DAnalysisDescriptor) obj).resourceURI);
    }
}
