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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.viewpoint.Messages;

/**
 * Specific command to rename the given resource.
 * 
 * @author jmallet
 * 
 */
public class RenameResourceCommand extends RecordingCommand {

    private Resource resource;

    private URI newUri;

    /**
     * Specific command to rename the given resource.
     * 
     * @param transDomain
     *            the editing domain.
     * @param resource
     *            the descriptor of the representation to rename.
     * @param newUri
     *            the new URI of the resource.
     */
    public RenameResourceCommand(TransactionalEditingDomain transDomain, Resource resource, URI newUri) {
        super(transDomain, Messages.RenameRepresentationCommand_label);
        this.resource = resource;
        this.newUri = newUri;
    }

    @Override
    public boolean canExecute() {
        return resource == null ? false : super.canExecute();
    }

    @Override
    protected void doExecute() {
        resource.setURI(newUri);
    }

}
