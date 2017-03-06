/*******************************************************************************
 * Copyright (c) 2017 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.tools.api.decoration;

import java.util.List;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecorator;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.sirius.diagram.ui.edit.api.part.IDiagramElementEditPart;

/**
 * Represents a provider able to give the decoration descriptions of a given {@link IDiagramElementEditPart}.
 * 
 * @author <a href="mailto:laurent.fasani@obeo.fr">Laurent Fasani</a>
 */
public interface SiriusDecorationDescriptorProvider {

    /**
     * Return decoration descriptions of a given {@link IDiagramElementEditPart} .
     * 
     * @param diagramEditPart
     *            EditPart of the diagram element
     * @return the decoration descriptions
     */
    List<DecorationDescriptor> getDecorationDescriptors(IDiagramElementEditPart diagramEditPart);

    /**
     * Tell if the provider can provide {@link DecorationDescriptor}s for the given editPart.
     * 
     * @param editPart
     *            the editPart
     * 
     * @return the boolean value.
     */
    boolean provides(IDiagramElementEditPart editPart);

    /**
     * Activates this provider for this {@link GraphicalEditPart}. The provider might need to hook listeners. These
     * listeners should be unhooked in <code>deactivate()</code>. </br>
     * The decorator is provided so that it can be refreshed according to event the provider could listen to.
     * 
     * @see #deActivate(GraphicalEditPart)
     * 
     * @param decoratorTarget
     *            the decorator target
     * @param decorator
     *            the decorator
     * @param editPart
     *            the editPart
     */
    void activate(IDecoratorTarget decoratorTarget, IDecorator decorator, GraphicalEditPart editPart);

    /**
     * Deactivates this provider, the inverse of {@link #activate()}. Deactivate is called when the editPart decorator
     * is deactivated. </br>
     * Deactivate unhooks any listeners that have been added.
     * 
     * @see #activate(GraphicalEditPart)
     * 
     * @param editPart
     *            the editPart
     * @param decorator
     *            the decorator
     */
    void deActivate(GraphicalEditPart editPart, IDecorator decorator);
}
