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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.sirius.properties.ControlDescription;
import org.eclipse.sirius.properties.WidgetConditionalStyle;
import org.eclipse.sirius.properties.WidgetDescription;
import org.eclipse.sirius.properties.WidgetStyle;

import org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.PropertiesExtWidgetsNebulaPackage
 * @generated
 */
public class PropertiesExtWidgetsNebulaAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static PropertiesExtWidgetsNebulaPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PropertiesExtWidgetsNebulaAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = PropertiesExtWidgetsNebulaPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject) object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PropertiesExtWidgetsNebulaSwitch<Adapter> modelSwitch = new PropertiesExtWidgetsNebulaSwitch<Adapter>() {
		@Override
		public Adapter caseExtNebulaRichTextDescription(ExtNebulaRichTextDescription object) {
			return createExtNebulaRichTextDescriptionAdapter();
		}

		@Override
		public Adapter caseExtNebulaRichTextWidgetStyle(ExtNebulaRichTextWidgetStyle object) {
			return createExtNebulaRichTextWidgetStyleAdapter();
		}

		@Override
		public Adapter caseExtNebulaRichTextWidgetConditionalStyle(ExtNebulaRichTextWidgetConditionalStyle object) {
			return createExtNebulaRichTextWidgetConditionalStyleAdapter();
		}

		@Override
		public Adapter caseControlDescription(ControlDescription object) {
			return createControlDescriptionAdapter();
		}

		@Override
		public Adapter caseWidgetDescription(WidgetDescription object) {
			return createWidgetDescriptionAdapter();
		}

		@Override
		public Adapter caseWidgetStyle(WidgetStyle object) {
			return createWidgetStyleAdapter();
		}

		@Override
		public Adapter caseWidgetConditionalStyle(WidgetConditionalStyle object) {
			return createWidgetConditionalStyleAdapter();
		}

		@Override
		public Adapter defaultCase(EObject object) {
			return createEObjectAdapter();
		}
	};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject) target);
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription <em>Ext Nebula Rich Text Description</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription
	 * @generated
	 */
	public Adapter createExtNebulaRichTextDescriptionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextWidgetStyle <em>Ext Nebula Rich Text Widget Style</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextWidgetStyle
	 * @generated
	 */
	public Adapter createExtNebulaRichTextWidgetStyleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextWidgetConditionalStyle <em>Ext Nebula Rich Text Widget Conditional Style</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextWidgetConditionalStyle
	 * @generated
	 */
	public Adapter createExtNebulaRichTextWidgetConditionalStyleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.sirius.properties.ControlDescription <em>Control Description</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.sirius.properties.ControlDescription
	 * @generated
	 */
	public Adapter createControlDescriptionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.sirius.properties.WidgetDescription <em>Widget Description</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.sirius.properties.WidgetDescription
	 * @generated
	 */
	public Adapter createWidgetDescriptionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.sirius.properties.WidgetStyle <em>Widget Style</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.sirius.properties.WidgetStyle
	 * @generated
	 */
	public Adapter createWidgetStyleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.sirius.properties.WidgetConditionalStyle <em>Widget Conditional Style</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.sirius.properties.WidgetConditionalStyle
	 * @generated
	 */
	public Adapter createWidgetConditionalStyleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //PropertiesExtWidgetsNebulaAdapterFactory
