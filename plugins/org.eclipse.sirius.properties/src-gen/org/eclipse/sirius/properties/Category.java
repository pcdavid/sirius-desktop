/**
 * Copyright (c) 2016 Obeo.
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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Category</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.sirius.properties.Category#getIdentifier
 * <em>Identifier</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.Category#getPages
 * <em>Pages</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.Category#getGroups
 * <em>Groups</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.Category#getOverrides
 * <em>Overrides</em>}</li>
 * </ul>
 *
 * @see org.eclipse.sirius.properties.PropertiesPackage#getCategory()
 * @model
 * @generated
 */
public interface Category extends EObject {
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' attribute. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Identifier</em>' attribute isn't clear, there
     * really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Identifier</em>' attribute.
     * @see #setIdentifier(String)
     * @see org.eclipse.sirius.properties.PropertiesPackage#getCategory_Identifier()
     * @model
     * @generated
     */
    String getIdentifier();

    /**
     * Sets the value of the
     * '{@link org.eclipse.sirius.properties.Category#getIdentifier
     * <em>Identifier</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Identifier</em>' attribute.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(String value);

    /**
     * Returns the value of the '<em><b>Pages</b></em>' containment reference
     * list. The list contents are of type
     * {@link org.eclipse.sirius.properties.PageDescription}. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Pages</em>' containment reference list isn't
     * clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Pages</em>' containment reference list.
     * @see org.eclipse.sirius.properties.PropertiesPackage#getCategory_Pages()
     * @model containment="true"
     * @generated
     */
    EList<PageDescription> getPages();

    /**
     * Returns the value of the '<em><b>Groups</b></em>' containment reference
     * list. The list contents are of type
     * {@link org.eclipse.sirius.properties.GroupDescription}. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Groups</em>' containment reference list isn't
     * clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Groups</em>' containment reference list.
     * @see org.eclipse.sirius.properties.PropertiesPackage#getCategory_Groups()
     * @model containment="true"
     * @generated
     */
    EList<GroupDescription> getGroups();

    /**
     * Returns the value of the '<em><b>Overrides</b></em>' containment
     * reference list. The list contents are of type
     * {@link org.eclipse.sirius.properties.AbstractOverrideDescription}. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Overrides</em>' containment reference list
     * isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Overrides</em>' containment reference list.
     * @see org.eclipse.sirius.properties.PropertiesPackage#getCategory_Overrides()
     * @model containment="true"
     * @generated
     */
    EList<AbstractOverrideDescription> getOverrides();

} // Category
