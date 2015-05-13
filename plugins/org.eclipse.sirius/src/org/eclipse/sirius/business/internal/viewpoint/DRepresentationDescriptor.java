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

import com.google.common.base.Preconditions;

/**
 * Represents a {@link DRepresentation} with only basic informations.
 * 
 * @author <a href="mailto:laurent.fasani@obeo.fr">Laurent Fasani</a>
 */
public final class DRepresentationDescriptor {

    // CHECKSTYLE:OFF
    public enum DialectEnum {
        DIAGRAM, TREE, TABLE
        // CHECKSTYLE:ON
    }

    /**
     * DRepresentation.name
     */
    private String name;

    /**
     * DRepresentation.name
     */
    private DialectEnum dialect;

    /**
     * Default constructor of this class.
     * 
     * @param name
     *            name of the representation
     * @param dialect
     *            type of the dialect
     */
    public DRepresentationDescriptor(String name, DialectEnum dialect) {
        this.name = Preconditions.checkNotNull(name);
        this.dialect = Preconditions.checkNotNull(dialect);
    }

    public String getName() {
        return name;
    }

    public DialectEnum getDialect() {
        return dialect;
    }
}
