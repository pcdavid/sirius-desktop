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
package org.eclipse.sirius.diagram.ui.tools.api.decoration;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecorator;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.ui.edit.api.part.AbstractDiagramListEditPart;
import org.eclipse.sirius.diagram.ui.edit.api.part.AbstractDiagramNameEditPart;
import org.eclipse.sirius.diagram.ui.edit.api.part.IDiagramElementEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeListEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeListElementEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeListNameEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeNameEditPart;

/**
 * Abstract implementation used for sub classes that implement
 * SiriusDecorationDescriptorProvider.
 * 
 * @author lfasani
 *
 */
public abstract class AbstractSiriusDecorationDescriptorProvider implements SiriusDecorationDescriptorProvider {

    @Override
    public void activate(IDecorator decorator, GraphicalEditPart editPart) {
        // do nothing
    }

    @Override
    public void deActivate(GraphicalEditPart editPart) {
        // do nothing
    }

    @Override
    public boolean provides(IDiagramElementEditPart editPart) {
        return false;
    }

    @Override
    public List<DecorationDescriptor> getDecorationDescriptors(IDiagramElementEditPart diagramEditPart) {
        List<DecorationDescriptor> decorationDescriptors = null;

        final View view = (View) diagramEditPart.getModel();
        if (view != null && (shouldConsiderDetachedViews() || view.eResource() != null)) {
            if (shouldBeDecorated(diagramEditPart)) {
                decorationDescriptors = createDecorationDescriptors(diagramEditPart);
            }
        }

        return decorationDescriptors;
    }

    /**
     * Create {@link DecorationDescriptor}.
     * 
     * @param diagramEditPart
     *            the edit part
     * @return the created {@link DecorationDescriptor}s
     */
    protected abstract List<DecorationDescriptor> createDecorationDescriptors(IDiagramElementEditPart diagramEditPart);

    /**
     * Indicates whether this decorator should consider detached {@link View}s
     * (i.e. {@link View}s which eResource() is null).
     * 
     * @return true if this decorator should consider detached {@link View}s,
     *         false otherwise.
     */
    protected boolean shouldConsiderDetachedViews() {
        return false;
    }

    /**
     * Check if the edit part respect conditions to be decorate.
     * 
     * @param editPart
     *            the editPart to check
     * @return true if the editPart respect conditions to be decorate, false
     *         otherwise
     */
    protected boolean shouldBeDecorated(final EditPart editPart) {
        boolean shouldBeDecorated = true;
        if (editPart == null || editPart.getParent() == null || editPart.getRoot() == null || editPart.getViewer() == null) {
            shouldBeDecorated = false;
        } else if (editPart instanceof AbstractDiagramNameEditPart && !(editPart instanceof DNodeListElementEditPart)) {
            /* Check that the editPart is not a name dEditPart */
            shouldBeDecorated = false;
        }
        return shouldBeDecorated;
    }

    /**
     * Indicates if the given editPart should contain decorations according to
     * its type. For example, {@link DNodeListNameEditPart}s should not be
     * decorated.
     * 
     * @param editPart
     *            the edit part to inspect
     * @return true if the given editPart should contain decorations, false
     *         otherwise
     */
    public boolean isDecorableEditPart(IDiagramElementEditPart editPart) {
        boolean result = true;
        if (editPart instanceof DNodeNameEditPart) {
            EditPart parentEditPart = editPart.getParent();
            if (!(parentEditPart instanceof DNodeListEditPart) && !(parentEditPart instanceof AbstractDiagramListEditPart)) {
                result = false;
            }
        } else if (editPart instanceof DNodeListNameEditPart) {
            result = false;
        } else if (editPart instanceof DNodeListElementEditPart) {
            // We only decorate DNodeListElementEditParts if the semantic
            // element is different from parent editpart
            EditPart parentEditPart = editPart.getParent();
            if (parentEditPart.getModel() instanceof View && editPart.getNotationView() != null) {
                View parentView = (View) parentEditPart.getModel();
                result = parentView.getElement() != null && !parentView.getElement().equals(editPart.getNotationView().getElement());
            }
        }
        return result;
    }

    /**
     * Tells if the decoration added by this decorator should be visible at
     * image export or print. By default true is returned to have decoration
     * visible at image export and print. Override this method to change this
     * behavior.
     * 
     * @param editPart
     *            the EditPart
     * @return true to have decorations visible at image export and at print
     */
    protected boolean shouldBeVisibleAtPrint(EditPart editPart) {
        return true;
    }
}
