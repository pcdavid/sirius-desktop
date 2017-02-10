/*******************************************************************************
 * Copyright (c) 2017 THALES GLOBAL SERVICES and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.internal.resource;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

/**
 * A specific implementation for *.repfile.
 * 
 * @author Florian Barbin
 *
 */
public class RepFileXMIResourceImpl extends XMIResourceImpl {
    private static boolean activateTrace;
    static {
        activateTrace = Boolean.parseBoolean(System.getProperty("activate_trace_getLoadedRepresentations", Boolean.FALSE.toString())); //$NON-NLS-1$
    }

    /**
     * Default constructor.
     * 
     * @param uri
     *            the resource URI.
     */
    public RepFileXMIResourceImpl(URI uri) {
        super(uri);
    }

    @Override
    protected boolean useUUIDs() {
        return true;
    }

    @Override
    protected boolean useIDAttributes() {
        return false;
    }

    @Override
    public void load(Map<?, ?> options) throws IOException {
        if (activateTrace) {
            Thread.dumpStack();
        } else {
            super.load(options);
        }
    }

}
