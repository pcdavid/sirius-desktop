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
package org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula;

import org.eclipse.emf.common.util.EList;

import org.eclipse.sirius.properties.WidgetDescription;

import org.eclipse.sirius.viewpoint.description.tool.InitialOperation;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ext Nebula Rich Text Description</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription#getValueExpression <em>Value Expression</em>}</li>
 *   <li>{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription#getInitialOperation <em>Initial Operation</em>}</li>
 *   <li>{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription#getStyle <em>Style</em>}</li>
 *   <li>{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription#getConditionalStyles <em>Conditional Styles</em>}</li>
 * </ul>
 *
 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.PropertiesExtWidgetsNebulaPackage#getExtNebulaRichTextDescription()
 * @model
 * @generated
 */
public interface ExtNebulaRichTextDescription extends WidgetDescription {
	/**
	 * Returns the value of the '<em><b>Value Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value Expression</em>' attribute.
	 * @see #setValueExpression(String)
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.PropertiesExtWidgetsNebulaPackage#getExtNebulaRichTextDescription_ValueExpression()
	 * @model dataType="org.eclipse.sirius.viewpoint.description.InterpretedExpression"
	 * @generated
	 */
	String getValueExpression();

	/**
	 * Sets the value of the '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription#getValueExpression <em>Value Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value Expression</em>' attribute.
	 * @see #getValueExpression()
	 * @generated
	 */
	void setValueExpression(String value);

	/**
	 * Returns the value of the '<em><b>Initial Operation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Initial Operation</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Initial Operation</em>' containment reference.
	 * @see #setInitialOperation(InitialOperation)
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.PropertiesExtWidgetsNebulaPackage#getExtNebulaRichTextDescription_InitialOperation()
	 * @model containment="true" required="true"
	 * @generated
	 */
	InitialOperation getInitialOperation();

	/**
	 * Sets the value of the '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription#getInitialOperation <em>Initial Operation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Initial Operation</em>' containment reference.
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
	 * @return the value of the '<em>Style</em>' containment reference.
	 * @see #setStyle(ExtNebulaRichTextWidgetStyle)
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.PropertiesExtWidgetsNebulaPackage#getExtNebulaRichTextDescription_Style()
	 * @model containment="true"
	 * @generated
	 */
	ExtNebulaRichTextWidgetStyle getStyle();

	/**
	 * Sets the value of the '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription#getStyle <em>Style</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Style</em>' containment reference.
	 * @see #getStyle()
	 * @generated
	 */
	void setStyle(ExtNebulaRichTextWidgetStyle value);

	/**
	 * Returns the value of the '<em><b>Conditional Styles</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextWidgetConditionalStyle}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Conditional Styles</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Conditional Styles</em>' containment reference list.
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.PropertiesExtWidgetsNebulaPackage#getExtNebulaRichTextDescription_ConditionalStyles()
	 * @model containment="true"
	 * @generated
	 */
	EList<ExtNebulaRichTextWidgetConditionalStyle> getConditionalStyles();

} // ExtNebulaRichTextDescription
