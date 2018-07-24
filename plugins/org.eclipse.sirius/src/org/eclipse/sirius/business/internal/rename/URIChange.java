/*******************************************************************************
 * Copyright (c) 2018 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.internal.rename;

import org.eclipse.emf.common.util.URI;

/**
 * Class used to store old URI to transform in new URI.
 * 
 * @author <a href="mailto:jessy.mallet@obeo.fr">Jessy MALLET</a>
 *
 */
public class URIChange {

    /**
     * Old URI before renaming.
     */
    private URI oldURI;

    /**
     * New URI after renaming.
     */
    private URI newURI;

    /**
     * Constructor.
     * 
     * @param oldURI
     *            URI of resource before renaming
     * @param newURI
     *            URI of resource after renaming
     */
    public URIChange(URI oldURI, URI newURI) {
        super();
        this.oldURI = oldURI;
        this.newURI = newURI;
    }

    public URI getOldURI() {
        return oldURI;
    }

    public URI getNewURI() {
        return newURI;
    }

    /**
     * Inverse constructor.
     * 
     * @return the inverse URI change.
     */
    public URIChange inverse() {
        return new URIChange(newURI, oldURI);
    }

    @Override
    public String toString() {
        return oldURI.toString() + " => " + newURI.toString(); //$NON-NLS-1$
    }

}
