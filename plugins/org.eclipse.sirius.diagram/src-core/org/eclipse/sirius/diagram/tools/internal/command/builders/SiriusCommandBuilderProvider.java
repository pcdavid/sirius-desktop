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
package org.eclipse.sirius.diagram.tools.internal.command.builders;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DDiagramElementContainer;
import org.eclipse.sirius.diagram.description.tool.ContainerCreationDescription;
import org.eclipse.sirius.diagram.description.tool.DoubleClickDescription;
import org.eclipse.sirius.diagram.description.tool.NodeCreationDescription;
import org.eclipse.sirius.diagram.tools.api.command.GetCommandBuilderOperation;
import org.eclipse.sirius.diagram.tools.api.command.ICommandBuilderProvider;
import org.eclipse.sirius.tools.internal.command.builders.CommandBuilder;
import org.eclipse.sirius.viewpoint.description.tool.AbstractToolDescription;

public class SiriusCommandBuilderProvider extends AbstractProvider implements ICommandBuilderProvider {

    @Override
    public boolean provides(IOperation operation) {
        if (operation instanceof GetCommandBuilderOperation) {
            GetCommandBuilderOperation gcbo = (GetCommandBuilderOperation) operation;
            return getCommandBuilder(gcbo.getAbstractToolDescription(), gcbo.getTarget()) != null;
        }
        return false;
    }

    @Override
    public CommandBuilder getCommandBuilder(AbstractToolDescription description, Object target) {
        CommandBuilder result = null;
        if (description instanceof NodeCreationDescription) {
            if (target instanceof DDiagram) {
                result = new NodeCreationCommandBuilder((NodeCreationDescription) description, (DDiagram) target);
            } else if (target instanceof DDiagramElement) {
                result = new NodeCreationCommandBuilder((NodeCreationDescription) description, (DDiagramElement) target);
            }
        } else if (description instanceof ContainerCreationDescription) {
            if (target instanceof DDiagram) {
                result = new ContainerCreationCommandBuilder((ContainerCreationDescription) description, (DDiagram) target);
            }
            if (target instanceof DDiagramElementContainer) {
                result = new ContainerCreationCommandBuilder((ContainerCreationDescription) description, (DDiagramElementContainer) target);
            }
        } else if (description instanceof DoubleClickDescription) {
            if (target instanceof DDiagramElement) {
                result = new DoubleClickCommandBuilder((DoubleClickDescription) description, (DDiagramElement) target);
            }
        }
        return result;
    }
}
