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

import org.eclipse.emf.common.util.URI;
import org.eclipse.sirius.viewpoint.description.DescriptionPackage;

import com.google.common.base.Preconditions;

/**
 * Represents a minimal description of a {@link ViewPoint} .
 * 
 * @author <a href="mailto:laurent.fasani@obeo.fr">Laurent Fasani</a>
 */
public final class ViewPointDescriptor {

    private final URI resourceURI;

    private final String name;

    /**
     * This constructor is used by EMF to deserialized the instances of this
     * class.
     * 
     * @param serializedString
     *            used to initialize the fields of the class. This string has
     *            been previously serialized with <code>toString()</code>.
     */
    public ViewPointDescriptor(String serializedString) {
        this(getURIFromSerializedString(serializedString), getNameFromSerializedString(serializedString));
    }

    /**
     * This constructor allows initializing the characterizing fields of this
     * class.
     * 
     * @param uri
     *            uri of the viewpoint resource
     * @param name
     *            name of the viewpoint
     */
    public ViewPointDescriptor(URI uri, String name) {
        this.resourceURI = Preconditions.checkNotNull(uri);
        this.name = Preconditions.checkNotNull(name);
    }

    private static URI getURIFromSerializedString(String serializedString) {
        URI uri = null;
        String[] split = serializedString.split("#");
        if (split.length == 2) {
            uri = URI.createURI(split[0], true, URI.FRAGMENT_FIRST_SEPARATOR);
        }
        return uri;
    }

    private static String getNameFromSerializedString(String serializedString) {
        String name = null;
        String[] split = serializedString.split("#");
        if (split.length == 2) {
            String[] split2 = split[1].split("'");
            if (split2[0].contains(DescriptionPackage.eINSTANCE.getGroup_OwnedViewpoints().getName()) && split2.length > 1) {
                name = split2[1];
            }
        }
        return name;
    }

    /**
     * Return the URI of the resource.
     * 
     * @return the uri of the resource
     */
    public URI getResourceURI() {
        return resourceURI;
    }

    /**
     * Return the name of the viewpoint.
     * 
     * @return the name of the viewpoint
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return resourceURI.toString() + " " + name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        result = prime * result + resourceURI.hashCode();
        return result;
    }

    // CHECKSTYLE:OFF
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        ViewPointDescriptor other = (ViewPointDescriptor) obj;
        if (!name.equals(other.name))
            return false;
        if (!resourceURI.equals(other.resourceURI))
            return false;
        return true;
    }
    // CHECKSTYLE:ON
}
