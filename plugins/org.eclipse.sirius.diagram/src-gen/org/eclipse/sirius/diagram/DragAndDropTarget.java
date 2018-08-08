/**
 * Copyright (c) 2007, 2013 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *
 */
package org.eclipse.sirius.diagram;

import org.eclipse.sirius.diagram.description.DragAndDropTargetDescription;
import org.eclipse.sirius.viewpoint.IdentifiedElement;

/**
 * <!-- begin-user-doc --> A representation of the model object ' <em><b>Drag And Drop Target</b></em>'. <!--
 * end-user-doc -->
 *
 * <!-- begin-model-doc --> A DragAndDropTarget is an element that can managed drop requests. <!-- end-model-doc -->
 *
 *
 * @see org.eclipse.sirius.diagram.DiagramPackage#getDragAndDropTarget()
 * @model
 * @generated
 */
public interface DragAndDropTarget extends IdentifiedElement {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Return the description of this Drag&Drop
     * Target. <!-- end-model-doc -->
     *
     * @model kind="operation"
     * @generated
     */
    DragAndDropTargetDescription getDragAndDropDescription();

} // DragAndDropTarget
