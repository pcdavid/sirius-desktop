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

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.sirius.common.tools.internal.resource.WorkspaceBackend;

/**
 * Factory to instantiate the Eclipse Workspace backend.
 * 
 * @author cedric
 *
 */
public class WorkspaceBackendFactory extends ResourceSyncBackendFactory {

    @Override
    public AbstractResourceSyncBackend createBackend(ResourceSyncClient client) {
        IWorkspaceRoot workspaceRoot = EcorePlugin.getWorkspaceRoot();
        if (workspaceRoot != null) {
            return new WorkspaceBackend(client);
        }
        return null;
    }

}
