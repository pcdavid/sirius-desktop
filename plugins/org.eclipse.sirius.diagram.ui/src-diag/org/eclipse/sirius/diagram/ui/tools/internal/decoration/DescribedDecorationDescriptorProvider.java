/*******************************************************************************
 * Copyright (c) 2016 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.tools.internal.decoration;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.business.api.logger.RuntimeLoggerManager;
import org.eclipse.sirius.business.api.query.DRepresentationElementQuery;
import org.eclipse.sirius.common.tools.DslCommonPlugin;
import org.eclipse.sirius.common.tools.api.interpreter.EvaluationException;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreter;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreterSiriusVariables;
import org.eclipse.sirius.common.tools.api.util.StringUtil;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.business.api.query.EObjectQuery;
import org.eclipse.sirius.diagram.ui.edit.api.part.IDiagramElementEditPart;
import org.eclipse.sirius.diagram.ui.edit.api.part.IDiagramNameEditPart;
import org.eclipse.sirius.diagram.ui.tools.api.decoration.AbstractSiriusDecorationDescriptorProvider;
import org.eclipse.sirius.diagram.ui.tools.api.decoration.DecorationDescriptor;
import org.eclipse.sirius.diagram.ui.tools.api.decoration.DecorationDescriptor.DisplayPriority;
import org.eclipse.sirius.diagram.ui.tools.api.decoration.SiriusDecorationDescriptorProvider;
import org.eclipse.sirius.diagram.ui.tools.api.figure.WorkspaceImageFigure;
import org.eclipse.sirius.tools.api.profiler.SiriusTasksKey;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.sirius.viewpoint.Decoration;
import org.eclipse.sirius.viewpoint.UIState;
import org.eclipse.sirius.viewpoint.description.DecorationDescription;
import org.eclipse.swt.graphics.Image;

import com.google.common.collect.Lists;

/**
 * A {@link SiriusDecorationDescriptorProvider} that provides
 * {@link DecorationDescriptor}s from {@link DecorationDescription}s defined in
 * the VSM.
 * 
 * @author <a href="mailto:laurent.fasani@obeo.fr">Laurent Fasani</a>
 */
public class DescribedDecorationDescriptorProvider extends AbstractSiriusDecorationDescriptorProvider implements SiriusDecorationDescriptorProvider {

    @Override
    public boolean provides(IDiagramElementEditPart editPart) {
        boolean provides = false;
        if (!(editPart instanceof IDiagramNameEditPart)) {
            provides = true;
        }
        return provides;
    }

    @Override
    public List<DecorationDescriptor> createDecorationDescriptors(IDiagramElementEditPart editPart) {
        List<DecorationDescriptor> decorationDescriptors = Lists.newArrayList();
        DDiagramElement element = editPart.resolveDiagramElement();
        if (element != null) {
            DRepresentation parentRepresentation = new DRepresentationElementQuery(element).getParentRepresentation();
            if (parentRepresentation != null) {
                UIState uiState = parentRepresentation.getUiState();
                List<Decoration> decorations = Lists.newArrayList(element.getDecorations());
                decorations.addAll(element.getTransientDecorations());
                for (Decoration decoration : decorations) {
                    DecorationDescriptor decorationDescriptor = initializeDecorationDescriptor(decoration, element, uiState, parentRepresentation);
                    if (decorationDescriptor != null) {
                        decorationDescriptors.add(decorationDescriptor);
                    }
                }
            }
        }
        return decorationDescriptors;
    }

    private DecorationDescriptor initializeDecorationDescriptor(final Decoration decoration, DDiagramElement element, UIState uiState, DRepresentation parentRepresentation) {
        DecorationDescriptor decoDesc = new DecorationDescriptionWithLazyTooltip(decoration.getDescription(), element);
        DecorationDescription description = decoration.getDescription();

        Object imageObject = uiState.getDecorationImage().get(decoration);
        if (imageObject == null) {
            String imageExpression = decoration.getDescription().getImageExpression();
            IInterpreter interpreter = new EObjectQuery(element).getSession().getInterpreter();
            if (interpreter.provides(imageExpression)) {
                imageObject = evaluateDecoration(description, element, imageExpression, parentRepresentation);
            }
            if (!(imageObject instanceof Image) && !(imageObject instanceof IFigure)) {
                if (imageObject instanceof String) {
                    imageObject = WorkspaceImageFigure.getImageInstanceFromPath((String) imageObject);
                } else {
                    imageObject = WorkspaceImageFigure.getImageInstanceFromPath(imageExpression);
                }
            }

            if (imageObject == null) {
                return null;
            } else {
                uiState.getDecorationImage().put(decoration, imageObject);
            }
        }
        if (imageObject instanceof Image) {
            decoDesc.setDecorationAsImage((Image) imageObject);
        } else if (imageObject instanceof IFigure) {
            decoDesc.setDecorationAsFigure((IFigure) imageObject);
        }

        decoDesc.setName(description.getName());
        decoDesc.setPosition(description.getPosition());
        decoDesc.setDistributionDirection(description.getDistributionDirection());
        decoDesc.setDisplayPriority(DisplayPriority.NORMAL_PRIORITY.getValue());

        return decoDesc;
    }

    private Object evaluateDecoration(DecorationDescription decorationDescription, final DDiagramElement element, final String expression, DRepresentation parentRepresentation) {
        DslCommonPlugin.PROFILER.startWork(SiriusTasksKey.CHECK_PRECONDITION_KEY);

        Object result = null;
        EObject semantic = ((DSemanticDecorator) element).getTarget();
        DSemanticDecorator container = (org.eclipse.sirius.viewpoint.DSemanticDecorator) element.eContainer();
        IInterpreter interpreter = new EObjectQuery(element).getSession().getInterpreter();

        if (decorationDescription != null && !decorationDescription.eIsProxy()) {
            result = true;
            if (!StringUtil.isEmpty(expression)) {
                interpreter.setVariable(IInterpreterSiriusVariables.CONTAINER_VIEW, container);
                interpreter.setVariable(IInterpreterSiriusVariables.CONTAINER, container != null ? container.getTarget() : null);
                interpreter.setVariable(IInterpreterSiriusVariables.DIAGRAM, parentRepresentation);
                try {
                    result = interpreter.evaluate(semantic, expression);
                } catch (final EvaluationException e) {
                    RuntimeLoggerManager.INSTANCE.error(decorationDescription, org.eclipse.sirius.viewpoint.description.DescriptionPackage.eINSTANCE.getDecorationDescription_PreconditionExpression(),
                            e);
                }
                interpreter.unSetVariable(IInterpreterSiriusVariables.CONTAINER_VIEW);
                interpreter.unSetVariable(IInterpreterSiriusVariables.CONTAINER);
                interpreter.unSetVariable(IInterpreterSiriusVariables.DIAGRAM);
            }
        }
        DslCommonPlugin.PROFILER.stopWork(SiriusTasksKey.CHECK_PRECONDITION_KEY);
        return result;
    }

    /**
     * This DecorationDescriptor allows to provides the tooltip only when
     * required.
     * 
     * @author lfasani
     *
     */
    private class DecorationDescriptionWithLazyTooltip extends DecorationDescriptor {
        private DecorationDescription decorationDescription;

        private DDiagramElement element;

        public DecorationDescriptionWithLazyTooltip(DecorationDescription decorationDescription, DDiagramElement element) {
            this.decorationDescription = decorationDescription;
            this.element = element;
        }

        @Override
        public IFigure getTooltipAsFigure() {
            if (tooltipAsFigure != null) {
                return tooltipAsFigure;
            }
            String tooltipExpression = decorationDescription.getTooltipExpression();
            if (tooltipExpression != null) {
                IInterpreter interpreter = new EObjectQuery(element).getSession().getInterpreter();
                if (interpreter.provides(tooltipExpression)) {
                    DRepresentation parentRepresentation = new DRepresentationElementQuery(element).getParentRepresentation();
                    if (parentRepresentation != null) {
                        Object evaluated = evaluateDecoration(decorationDescription, element, tooltipExpression, parentRepresentation);
                        if (evaluated instanceof IFigure) {
                            tooltipAsFigure = (IFigure) evaluated;
                        }
                    }
                }
            }
            return tooltipAsFigure;
        }

        @Override
        public String getTooltipAsString() {
            if (tooltipAsString == null) {
                String tooltipExpression = decorationDescription.getTooltipExpression();
                if (tooltipExpression != null) {
                    IInterpreter interpreter = new EObjectQuery(element).getSession().getInterpreter();
                    if (!interpreter.provides(tooltipExpression)) {
                        tooltipAsString = tooltipExpression;
                    } else {
                        try {
                            tooltipExpression = interpreter.evaluateString(element, tooltipExpression);
                            tooltipAsString = tooltipExpression;
                        } catch (EvaluationException e) {
                        }
                    }
                }
            }
            return tooltipAsString;
        }
    }

}
