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

import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.sirius.diagram.DiagramPlugin;
import org.eclipse.sirius.tools.internal.command.builders.CommandBuilder;
import org.eclipse.sirius.viewpoint.description.tool.AbstractToolDescription;

/**
 * The CommandBuilderService is responsible to provide command builders for sirius tool executions. Not all abstract
 * tools are wired through this service. At the moment only NodeCreation/ContainerCreationTools. Additional tools will
 * be added later.
 * 
 * @author Felix Dorner <felix.dorner@gmail.com>*
 */
public class CommandBuilderService extends Service implements ICommandBuilderProvider {

    /**
     * The command builder service singleton.
     */
    public static final CommandBuilderService INSTANCE = new CommandBuilderService();

    static {
        INSTANCE.configureProviders(DiagramPlugin.ID, "commandBuilderProviders"); //$NON-NLS-1$
    }

    @Override
    public CommandBuilder getCommandBuilder(AbstractToolDescription description, Object target) {
        return (CommandBuilder) execute(ExecutionStrategy.FIRST, new GetCommandBuilderOperation(description, target));
    }

}
