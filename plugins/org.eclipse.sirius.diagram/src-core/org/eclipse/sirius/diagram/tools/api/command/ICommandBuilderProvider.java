/*******************************************************************************
 * Copyright (c) 2019 THALES GLOBAL SERVICES.
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

import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.sirius.tools.internal.command.builders.CommandBuilder;
import org.eclipse.sirius.viewpoint.description.tool.AbstractToolDescription;

/**
 * A CommandBuilderProvider creates CommandBuilders for sirius tool execution. This mechanism allows to redefine the
 * semantics for a given tool.
 * 
 * @TODO some commands take more than one parameter...
 * 
 * @author Felix Dorner <felix.dorner@gmail.com>
 *
 */
public interface ICommandBuilderProvider extends IProvider {

    /**
     * Returns a command builder for the given tool description and the given target.
     * 
     * @param description
     *            the tool description
     * @param target
     *            the target on which the tool is executed
     * @return a command builder, or null if this provider doesn't support the given tool/target combination
     */
    CommandBuilder getCommandBuilder(AbstractToolDescription description, Object target);

}
