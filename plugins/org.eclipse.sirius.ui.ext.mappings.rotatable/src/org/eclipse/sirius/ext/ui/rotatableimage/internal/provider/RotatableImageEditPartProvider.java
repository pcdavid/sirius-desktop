/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/

package org.eclipse.sirius.ext.ui.rotatableimage.internal.provider;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.CustomStyle;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.DiagramPackage;
import org.eclipse.sirius.ext.ui.rotatableimage.RotatableImagePlugin;
import org.eclipse.sirius.ext.ui.rotatableimage.internal.editPart.RotatableBorderedNodeEditPart;
import org.eclipse.sirius.ext.ui.rotatableimage.internal.editPart.RotatableImageBasedOnFirstConnectionEditPart;
import org.eclipse.sirius.ext.ui.rotatableimage.internal.editPart.RotatableNodeEditPart;

/**
 * Specific Edit Part Provider for rotatable image.
 * 
 * @author nlepine
 * @author hmarchadour
 * @author arichard
 * @author mbats
 */
public class RotatableImageEditPartProvider extends AbstractEditPartProvider {

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider#getNodeEditPartClass(org.eclipse.gmf.runtime.notation.View)
     */
    @Override
    protected Class<?> getNodeEditPartClass(final View view) {
        final EObject semanticElement = ViewUtil.resolveSemanticElement(view);
        Class<?> editPart = super.getNodeEditPartClass(view);
        if (semanticElement instanceof CustomStyle) {
            final CustomStyle customStyle = (CustomStyle) semanticElement;
            if (rotatableImageExists(customStyle)) {
                if (isBorderedNode(semanticElement)) {
                    editPart = RotatableBorderedNodeEditPart.class;
                } else {
                    editPart = RotatableImageBasedOnFirstConnectionEditPart.class;
                }
            }
        } else if (view instanceof Node) {
            @SuppressWarnings("unchecked")
            List<Object> children = view.getChildren();
            for (Object child : children) {
                if (child instanceof View) {
                    final EObject childSemanticElement = ViewUtil.resolveSemanticElement((View) child);
                    if (childSemanticElement instanceof CustomStyle) {
                        final CustomStyle customStyle = (CustomStyle) childSemanticElement;
                        if (rotatableImageExists(customStyle) && !isBorderedNode(semanticElement)) {
                            editPart = RotatableNodeEditPart.class;
                        }
                    }
                }
            }
        }
        return editPart;
    }

    /**
     * Check if a node is a bordered node.
     * 
     * @param object
     *            the node
     * @return True if the node is a bordered node otherwise false
     */
    private boolean isBorderedNode(EObject object) {
        boolean result = false;
        if (object instanceof CustomStyle) {
            EObject container = object.eContainer();
            if (container instanceof DNode) {
                result = isBorderedNode(container);
            }
        } else if (DiagramPackage.Literals.ABSTRACT_DNODE__OWNED_BORDERED_NODES.equals(object.eContainingFeature())) {
            result = true;
        }
        return result;
    }

    /**
     * Check if the custom style has an associated rotatable image defined.
     * 
     * @param customStyle
     *            The custom style
     * @return True a rotatable image with the same identifier exists, otherwise
     *         false
     */
    private boolean rotatableImageExists(CustomStyle customStyle) {
        return RotatableImagePlugin.getDefault().getRotatableImage(customStyle.getId()) != null;
    }
}
