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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.sirius.business.api.helper.SiriusUtil;
import org.eclipse.sirius.viewpoint.Messages;
import org.eclipse.sirius.viewpoint.SiriusPlugin;

/**
 * Compute dependencies between resource.
 * 
 * @author <a href="mailto:jessy.mallet@obeo.fr">Jessy MALLET</a>
 *
 */
public class WorkspaceDependencies {

    /**
     * Message use when something goes wrong during dependencies collect.
     */
    public static final String ERROR_MSG = Messages.DependenciesCollectProcess_errorMsg;

    /**
     * Compute Model Dependency Graph, look for dependencies in aird representation file.
     * 
     * @param wks
     *            the workspace
     * @return Model Dependency Graph built
     */
    public ModelDependenciesGraph buildModelDependencyGraph(IWorkspace wks) {
        final ModelDependenciesGraph graph = new ModelDependenciesGraph(new DependenciesCollector());

        try {
            wks.getRoot().accept(new IResourceVisitor() {

                @Override
                public boolean visit(IResource resource) throws CoreException {
                    if (resource instanceof IFile && resource.getFileExtension() != null
                            && (SiriusUtil.SESSION_RESOURCE_EXTENSION.equals(resource.getFileExtension()) || SiriusUtil.REPRESENTATION_FILE_EXTENSION.equals(resource.getFileExtension()))) {
                        graph.registerModel(URI.createPlatformResourceURI(resource.getFullPath().toOSString(), true));
                    }
                    return true;
                }
            });
        } catch (CoreException e) {
            SiriusPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, SiriusPlugin.ID, ERROR_MSG, e));
        }
        return graph;
    }

}
