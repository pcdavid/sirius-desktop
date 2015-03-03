/*******************************************************************************
 * Copyright (c) 2007, 2015 THALES GLOBAL SERVICES and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.internal.session.danalysis;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RunnableWithResult;

/**
 * Encapsulates the behavior of the reload operations of *when*
 * org.eclipse.sirius.business.api.session.ReloadingPolicy.Action.RELOAD action
 * is triggered.
 * 
 * @author mporhel
 */
public class Reloader {

    /**
     * Default behavior is to unload and reload the affected resource.
     * 
     * @param resource
     *            the resource on which change has been detected.
     * @param session
     *            a session impacted by the change
     * @return a IStatus indicating the result of the reload operation.
     * @throws IOException
     *             if an exception occured during load.
     */
    public IStatus reload(final Resource resource, final DAnalysisSessionImpl session) throws IOException {
        /* execute the reload operation as a read-only transaction */
        RunnableWithResult<?> reload = new RunnableWithResult.Impl<Object>() {
            @Override
            public void run() {
                try {
                    session.disableCrossReferencerResolve(resource);
                    resource.unload();
                    session.enableCrossReferencerResolve(resource);

                    resource.load(Collections.EMPTY_MAP);
                    EcoreUtil.resolveAll(resource);
                    session.getSemanticCrossReferencer().resolveProxyCrossReferences(resource);
                } catch (IOException e) {
                    setResult(e);
                }
            }
        };

        try {
            session.getTransactionalEditingDomain().runExclusive(reload);
        } catch (InterruptedException e) {
            // do nothing
        }
        if (reload.getResult() != null) {
            throw (IOException) reload.getResult();
        }

        return reload.getStatus();
    }
}
