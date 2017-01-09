/**
 * Copyright (c) 2016, 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *
 */
package org.eclipse.sirius.properties;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Documented Element Description</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.sirius.properties.DocumentedElementDescription#getDocumentation
 * <em>Documentation</em>}</li>
 * </ul>
 *
 * @see org.eclipse.sirius.properties.PropertiesPackage#getDocumentedElementDescription()
 * @model abstract="true"
 * @generated
 */
public interface DocumentedElementDescription extends EObject {
    /**
     * Returns the value of the '<em><b>Documentation</b></em>' attribute. The
     * default value is <code>""</code>. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Documentation</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Documentation</em>' attribute.
     * @see #setDocumentation(String)
     * @see org.eclipse.sirius.properties.PropertiesPackage#getDocumentedElementDescription_Documentation()
     * @model default=""
     * @generated
     */
    String getDocumentation();

    /**
     * Sets the value of the
     * '{@link org.eclipse.sirius.properties.DocumentedElementDescription#getDocumentation
     * <em>Documentation</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Documentation</em>' attribute.
     * @see #getDocumentation()
     * @generated
     */
    void setDocumentation(String value);

} // DocumentedElementDescription
