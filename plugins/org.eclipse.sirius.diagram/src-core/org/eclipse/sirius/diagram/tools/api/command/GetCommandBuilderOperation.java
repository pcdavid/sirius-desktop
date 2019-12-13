/*******************************************************************************
 * Copyright (c) 2012, 2019 THALES GLOBAL SERVICES.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.tools.api.command;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.sirius.viewpoint.description.tool.AbstractToolDescription;

public class GetCommandBuilderOperation implements IOperation {

    private final AbstractToolDescription description;

    private final Object target;

    /**
     * Create an operation to provide a command builder for the given description/target.
     * 
     * @param diagram
     *            the targeted diagram
     * @param description
     *            the tool description
     */
    GetCommandBuilderOperation(AbstractToolDescription description, Object target) {
        this.description = description;
        this.target = target;
    }

    public AbstractToolDescription getAbstractToolDescription() {
        return description;
    }

    public Object getTarget() {
        return target;
    }

    @Override
    public Object execute(IProvider provider) {
        return ((ICommandBuilderProvider) provider).getCommandBuilder(description, target);
    }

}
