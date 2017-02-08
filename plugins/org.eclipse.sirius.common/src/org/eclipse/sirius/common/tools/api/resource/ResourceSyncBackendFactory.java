/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.common.tools.api.resource;

import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * 
 * Class responsible for instantiating a new resource synchronization backend.
 * 
 * @author cedric
 *
 */
public abstract class ResourceSyncBackendFactory {
    /**
     * Create a new backend which will get installed on a {@link ResourceSet}.
     * 
     * @param host
     *            the instance which should receive the resource status change
     *            notifications.
     * @return a newly created backend or null.
     */
    public abstract AbstractResourceSyncBackend createBackend(ResourceSyncClient host);

}
