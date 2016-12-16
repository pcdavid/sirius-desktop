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
package org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PropertiesExtWidgetsNebulaFactoryImpl extends EFactoryImpl implements PropertiesExtWidgetsNebulaFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static PropertiesExtWidgetsNebulaFactory init() {
		try {
			PropertiesExtWidgetsNebulaFactory thePropertiesExtWidgetsNebulaFactory = (PropertiesExtWidgetsNebulaFactory) EPackage.Registry.INSTANCE
					.getEFactory(PropertiesExtWidgetsNebulaPackage.eNS_URI);
			if (thePropertiesExtWidgetsNebulaFactory != null) {
				return thePropertiesExtWidgetsNebulaFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new PropertiesExtWidgetsNebulaFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PropertiesExtWidgetsNebulaFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION:
			return createExtNebulaRichTextDescription();
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_WIDGET_STYLE:
			return createExtNebulaRichTextWidgetStyle();
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_WIDGET_CONDITIONAL_STYLE:
			return createExtNebulaRichTextWidgetConditionalStyle();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ExtNebulaRichTextDescription createExtNebulaRichTextDescription() {
		ExtNebulaRichTextDescriptionImpl extNebulaRichTextDescription = new ExtNebulaRichTextDescriptionImpl();
		return extNebulaRichTextDescription;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ExtNebulaRichTextWidgetStyle createExtNebulaRichTextWidgetStyle() {
		ExtNebulaRichTextWidgetStyleImpl extNebulaRichTextWidgetStyle = new ExtNebulaRichTextWidgetStyleImpl();
		return extNebulaRichTextWidgetStyle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ExtNebulaRichTextWidgetConditionalStyle createExtNebulaRichTextWidgetConditionalStyle() {
		ExtNebulaRichTextWidgetConditionalStyleImpl extNebulaRichTextWidgetConditionalStyle = new ExtNebulaRichTextWidgetConditionalStyleImpl();
		return extNebulaRichTextWidgetConditionalStyle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PropertiesExtWidgetsNebulaPackage getPropertiesExtWidgetsNebulaPackage() {
		return (PropertiesExtWidgetsNebulaPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static PropertiesExtWidgetsNebulaPackage getPackage() {
		return PropertiesExtWidgetsNebulaPackage.eINSTANCE;
	}

} //PropertiesExtWidgetsNebulaFactoryImpl
