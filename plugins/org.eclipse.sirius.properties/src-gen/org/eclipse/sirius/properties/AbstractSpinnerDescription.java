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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Abstract Spinner Description</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.sirius.properties.AbstractSpinnerDescription#getValueExpression <em>Value
 * Expression</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.AbstractSpinnerDescription#getDigitsExpression <em>Digits
 * Expression</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.AbstractSpinnerDescription#getIncrementExpression <em>Increment
 * Expression</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.AbstractSpinnerDescription#getMinExpression <em>Min Expression</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.AbstractSpinnerDescription#getMaxExpression <em>Max Expression</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.AbstractSpinnerDescription#getInitialOperation <em>Initial
 * Operation</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.AbstractSpinnerDescription#getStyle <em>Style</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.AbstractSpinnerDescription#getConditionalStyles <em>Conditional
 * Styles</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.AbstractSpinnerDescription#getExtends <em>Extends</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.AbstractSpinnerDescription#getFilterConditionalStylesFromExtendedSpinnerExpression
 * <em>Filter Conditional Styles From Extended Spinner Expression</em>}</li>
 * </ul>
 *
 * @see org.eclipse.sirius.properties.PropertiesPackage#getAbstractSpinnerDescription()
 * @model abstract="true"
 * @generated
 */
public interface AbstractSpinnerDescription extends AbstractWidgetDescription, AbstractOverrideDescription {
    /**
     * Returns the value of the '<em><b>Value Expression</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value Expression</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Value Expression</em>' attribute.
     * @see #setValueExpression(String)
     * @see org.eclipse.sirius.properties.PropertiesPackage#getAbstractSpinnerDescription_ValueExpression()
     * @model dataType="org.eclipse.sirius.viewpoint.description.InterpretedExpression"
     * @generated
     */
    String getValueExpression();

    /**
     * Sets the value of the '{@link org.eclipse.sirius.properties.AbstractSpinnerDescription#getValueExpression
     * <em>Value Expression</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Value Expression</em>' attribute.
     * @see #getValueExpression()
     * @generated
     */
    void setValueExpression(String value);

    /**
     * Returns the value of the '<em><b>Digits Expression</b></em>' attribute. The default value is <code>"0"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Digits Expression</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Digits Expression</em>' attribute.
     * @see #setDigitsExpression(String)
     * @see org.eclipse.sirius.properties.PropertiesPackage#getAbstractSpinnerDescription_DigitsExpression()
     * @model default="0" dataType="org.eclipse.sirius.viewpoint.description.InterpretedExpression"
     * @generated
     */
    String getDigitsExpression();

    /**
     * Sets the value of the '{@link org.eclipse.sirius.properties.AbstractSpinnerDescription#getDigitsExpression
     * <em>Digits Expression</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Digits Expression</em>' attribute.
     * @see #getDigitsExpression()
     * @generated
     */
    void setDigitsExpression(String value);

    /**
     * Returns the value of the '<em><b>Increment Expression</b></em>' attribute. The default value is <code>"1"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Increment Expression</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Increment Expression</em>' attribute.
     * @see #setIncrementExpression(String)
     * @see org.eclipse.sirius.properties.PropertiesPackage#getAbstractSpinnerDescription_IncrementExpression()
     * @model default="1" dataType="org.eclipse.sirius.viewpoint.description.InterpretedExpression"
     * @generated
     */
    String getIncrementExpression();

    /**
     * Sets the value of the '{@link org.eclipse.sirius.properties.AbstractSpinnerDescription#getIncrementExpression
     * <em>Increment Expression</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Increment Expression</em>' attribute.
     * @see #getIncrementExpression()
     * @generated
     */
    void setIncrementExpression(String value);

    /**
     * Returns the value of the '<em><b>Min Expression</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Min Expression</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Min Expression</em>' attribute.
     * @see #setMinExpression(String)
     * @see org.eclipse.sirius.properties.PropertiesPackage#getAbstractSpinnerDescription_MinExpression()
     * @model dataType="org.eclipse.sirius.viewpoint.description.InterpretedExpression"
     * @generated
     */
    String getMinExpression();

    /**
     * Sets the value of the '{@link org.eclipse.sirius.properties.AbstractSpinnerDescription#getMinExpression <em>Min
     * Expression</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Min Expression</em>' attribute.
     * @see #getMinExpression()
     * @generated
     */
    void setMinExpression(String value);

    /**
     * Returns the value of the '<em><b>Max Expression</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Max Expression</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Max Expression</em>' attribute.
     * @see #setMaxExpression(String)
     * @see org.eclipse.sirius.properties.PropertiesPackage#getAbstractSpinnerDescription_MaxExpression()
     * @model dataType="org.eclipse.sirius.viewpoint.description.InterpretedExpression"
     * @generated
     */
    String getMaxExpression();

    /**
     * Sets the value of the '{@link org.eclipse.sirius.properties.AbstractSpinnerDescription#getMaxExpression <em>Max
     * Expression</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Max Expression</em>' attribute.
     * @see #getMaxExpression()
     * @generated
     */
    void setMaxExpression(String value);

    /**
     * Returns the value of the '<em><b>Initial Operation</b></em>' containment reference. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Initial Operation</em>' containment reference isn't clear, there really should be more
     * of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Initial Operation</em>' containment reference.
     * @see #setInitialOperation(InitialOperation)
     * @see org.eclipse.sirius.properties.PropertiesPackage#getAbstractSpinnerDescription_InitialOperation()
     * @model containment="true" required="true"
     * @generated
     */
    InitialOperation getInitialOperation();

    /**
     * Sets the value of the '{@link org.eclipse.sirius.properties.AbstractSpinnerDescription#getInitialOperation
     * <em>Initial Operation</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Initial Operation</em>' containment reference.
     * @see #getInitialOperation()
     * @generated
     */
    void setInitialOperation(InitialOperation value);

    /**
     * Returns the value of the '<em><b>Style</b></em>' containment reference. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Style</em>' containment reference isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Style</em>' containment reference.
     * @see #setStyle(SpinnerWidgetStyle)
     * @see org.eclipse.sirius.properties.PropertiesPackage#getAbstractSpinnerDescription_Style()
     * @model containment="true"
     * @generated
     */
    SpinnerWidgetStyle getStyle();

    /**
     * Sets the value of the '{@link org.eclipse.sirius.properties.AbstractSpinnerDescription#getStyle <em>Style</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Style</em>' containment reference.
     * @see #getStyle()
     * @generated
     */
    void setStyle(SpinnerWidgetStyle value);

    /**
     * Returns the value of the '<em><b>Conditional Styles</b></em>' containment reference list. The list contents are
     * of type {@link org.eclipse.sirius.properties.SpinnerWidgetConditionalStyle}. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Conditional Styles</em>' containment reference list isn't clear, there really should
     * be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Conditional Styles</em>' containment reference list.
     * @see org.eclipse.sirius.properties.PropertiesPackage#getAbstractSpinnerDescription_ConditionalStyles()
     * @model containment="true"
     * @generated
     */
    EList<SpinnerWidgetConditionalStyle> getConditionalStyles();

    /**
     * Returns the value of the '<em><b>Extends</b></em>' reference. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Extends</em>' reference isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Extends</em>' reference.
     * @see #setExtends(SpinnerDescription)
     * @see org.eclipse.sirius.properties.PropertiesPackage#getAbstractSpinnerDescription_Extends()
     * @model
     * @generated
     */
    SpinnerDescription getExtends();

    /**
     * Sets the value of the '{@link org.eclipse.sirius.properties.AbstractSpinnerDescription#getExtends
     * <em>Extends</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Extends</em>' reference.
     * @see #getExtends()
     * @generated
     */
    void setExtends(SpinnerDescription value);

    /**
     * Returns the value of the '<em><b>Filter Conditional Styles From Extended Spinner Expression</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Filter Conditional Styles From Extended Spinner Expression</em>' attribute isn't
     * clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Filter Conditional Styles From Extended Spinner Expression</em>' attribute.
     * @see #setFilterConditionalStylesFromExtendedSpinnerExpression(String)
     * @see org.eclipse.sirius.properties.PropertiesPackage#getAbstractSpinnerDescription_FilterConditionalStylesFromExtendedSpinnerExpression()
     * @model dataType="org.eclipse.sirius.viewpoint.description.InterpretedExpression"
     * @generated
     */
    String getFilterConditionalStylesFromExtendedSpinnerExpression();

    /**
     * Sets the value of the
     * '{@link org.eclipse.sirius.properties.AbstractSpinnerDescription#getFilterConditionalStylesFromExtendedSpinnerExpression
     * <em>Filter Conditional Styles From Extended Spinner Expression</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Filter Conditional Styles From Extended Spinner Expression</em>' attribute.
     * @see #getFilterConditionalStylesFromExtendedSpinnerExpression()
     * @generated
     */
    void setFilterConditionalStylesFromExtendedSpinnerExpression(String value);

} // AbstractSpinnerDescription
