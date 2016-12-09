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
import org.eclipse.sirius.viewpoint.description.tool.InitialOperation;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Multi
 * Text Description</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.sirius.properties.MultiTextDescription#getAttributeOwnerExpression
 * <em>Attribute Owner Expression</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.MultiTextDescription#getAttributeNameExpression
 * <em>Attribute Name Expression</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.MultiTextDescription#getInitialOperation
 * <em>Initial Operation</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.MultiTextDescription#getStyle
 * <em>Style</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.MultiTextDescription#getConditionalStyles
 * <em>Conditional Styles</em>}</li>
 * </ul>
 *
 * @see org.eclipse.sirius.properties.PropertiesPackage#getMultiTextDescription()
 * @model
 * @generated
 */
public interface MultiTextDescription extends WidgetDescription {
    /**
     * Returns the value of the '<em><b>Attribute Owner Expression</b></em>'
     * attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Attribute Owner Expression</em>' attribute
     * isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Attribute Owner Expression</em>' attribute.
     * @see #setAttributeOwnerExpression(String)
     * @see org.eclipse.sirius.properties.PropertiesPackage#getMultiTextDescription_AttributeOwnerExpression()
     * @model dataType="org.eclipse.sirius.viewpoint.description.InterpretedExpression"
     * @generated
     */
    String getAttributeOwnerExpression();

    /**
     * Sets the value of the
     * '{@link org.eclipse.sirius.properties.MultiTextDescription#getAttributeOwnerExpression
     * <em>Attribute Owner Expression</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Attribute Owner Expression</em>'
     *            attribute.
     * @see #getAttributeOwnerExpression()
     * @generated
     */
    void setAttributeOwnerExpression(String value);

    /**
     * Returns the value of the '<em><b>Attribute Name Expression</b></em>'
     * attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Attribute Name Expression</em>' attribute
     * isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Attribute Name Expression</em>' attribute.
     * @see #setAttributeNameExpression(String)
     * @see org.eclipse.sirius.properties.PropertiesPackage#getMultiTextDescription_AttributeNameExpression()
     * @model dataType="org.eclipse.sirius.viewpoint.description.InterpretedExpression"
     * @generated
     */
    String getAttributeNameExpression();

    /**
     * Sets the value of the
     * '{@link org.eclipse.sirius.properties.MultiTextDescription#getAttributeNameExpression
     * <em>Attribute Name Expression</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Attribute Name Expression</em>'
     *            attribute.
     * @see #getAttributeNameExpression()
     * @generated
     */
    void setAttributeNameExpression(String value);

    /**
     * Returns the value of the '<em><b>Initial Operation</b></em>' containment
     * reference. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Initial Operation</em>' containment reference
     * isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Initial Operation</em>' containment
     *         reference.
     * @see #setInitialOperation(InitialOperation)
     * @see org.eclipse.sirius.properties.PropertiesPackage#getMultiTextDescription_InitialOperation()
     * @model containment="true" required="true"
     * @generated
     */
    InitialOperation getInitialOperation();

    /**
     * Sets the value of the
     * '{@link org.eclipse.sirius.properties.MultiTextDescription#getInitialOperation
     * <em>Initial Operation</em>}' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Initial Operation</em>' containment
     *            reference.
     * @see #getInitialOperation()
     * @generated
     */
    void setInitialOperation(InitialOperation value);

    /**
     * Returns the value of the '<em><b>Style</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Style</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Style</em>' containment reference.
     * @see #setStyle(MultiTextWidgetStyle)
     * @see org.eclipse.sirius.properties.PropertiesPackage#getMultiTextDescription_Style()
     * @model containment="true"
     * @generated
     */
    MultiTextWidgetStyle getStyle();

    /**
     * Sets the value of the
     * '{@link org.eclipse.sirius.properties.MultiTextDescription#getStyle
     * <em>Style</em>}' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Style</em>' containment reference.
     * @see #getStyle()
     * @generated
     */
    void setStyle(MultiTextWidgetStyle value);

    /**
     * Returns the value of the '<em><b>Conditional Styles</b></em>' containment
     * reference list. The list contents are of type
     * {@link org.eclipse.sirius.properties.MultiTextWidgetConditionalStyle}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Conditional Styles</em>' containment reference
     * list isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Conditional Styles</em>' containment
     *         reference list.
     * @see org.eclipse.sirius.properties.PropertiesPackage#getMultiTextDescription_ConditionalStyles()
     * @model containment="true"
     * @generated
     */
    EList<MultiTextWidgetConditionalStyle> getConditionalStyles();

} // MultiTextDescription
