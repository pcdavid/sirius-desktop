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
package org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.sirius.properties.ControlDescription;
import org.eclipse.sirius.properties.WidgetConditionalStyle;
import org.eclipse.sirius.properties.WidgetDescription;
import org.eclipse.sirius.properties.WidgetStyle;

import org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.PropertiesExtWidgetsNebulaPackage
 * @generated
 */
public class PropertiesExtWidgetsNebulaSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static PropertiesExtWidgetsNebulaPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PropertiesExtWidgetsNebulaSwitch() {
		if (modelPackage == null) {
			modelPackage = PropertiesExtWidgetsNebulaPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION: {
			ExtNebulaRichTextDescription extNebulaRichTextDescription = (ExtNebulaRichTextDescription) theEObject;
			T result = caseExtNebulaRichTextDescription(extNebulaRichTextDescription);
			if (result == null)
				result = caseWidgetDescription(extNebulaRichTextDescription);
			if (result == null)
				result = caseControlDescription(extNebulaRichTextDescription);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_WIDGET_STYLE: {
			ExtNebulaRichTextWidgetStyle extNebulaRichTextWidgetStyle = (ExtNebulaRichTextWidgetStyle) theEObject;
			T result = caseExtNebulaRichTextWidgetStyle(extNebulaRichTextWidgetStyle);
			if (result == null)
				result = caseWidgetStyle(extNebulaRichTextWidgetStyle);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_WIDGET_CONDITIONAL_STYLE: {
			ExtNebulaRichTextWidgetConditionalStyle extNebulaRichTextWidgetConditionalStyle = (ExtNebulaRichTextWidgetConditionalStyle) theEObject;
			T result = caseExtNebulaRichTextWidgetConditionalStyle(extNebulaRichTextWidgetConditionalStyle);
			if (result == null)
				result = caseWidgetConditionalStyle(extNebulaRichTextWidgetConditionalStyle);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		default:
			return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Ext Nebula Rich Text Description</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ext Nebula Rich Text Description</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExtNebulaRichTextDescription(ExtNebulaRichTextDescription object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Ext Nebula Rich Text Widget Style</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ext Nebula Rich Text Widget Style</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExtNebulaRichTextWidgetStyle(ExtNebulaRichTextWidgetStyle object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Ext Nebula Rich Text Widget Conditional Style</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ext Nebula Rich Text Widget Conditional Style</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExtNebulaRichTextWidgetConditionalStyle(ExtNebulaRichTextWidgetConditionalStyle object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Control Description</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Control Description</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseControlDescription(ControlDescription object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Widget Description</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Widget Description</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseWidgetDescription(WidgetDescription object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Widget Style</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Widget Style</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseWidgetStyle(WidgetStyle object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Widget Conditional Style</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Widget Conditional Style</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseWidgetConditionalStyle(WidgetConditionalStyle object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //PropertiesExtWidgetsNebulaSwitch
