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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.sirius.properties.PropertiesPackage;

import org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription;
import org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextWidgetConditionalStyle;
import org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextWidgetStyle;
import org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.PropertiesExtWidgetsNebulaFactory;
import org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.PropertiesExtWidgetsNebulaPackage;

import org.eclipse.sirius.viewpoint.description.DescriptionPackage;

import org.eclipse.sirius.viewpoint.description.tool.ToolPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PropertiesExtWidgetsNebulaPackageImpl extends EPackageImpl implements PropertiesExtWidgetsNebulaPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass extNebulaRichTextDescriptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass extNebulaRichTextWidgetStyleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass extNebulaRichTextWidgetConditionalStyleEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.PropertiesExtWidgetsNebulaPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private PropertiesExtWidgetsNebulaPackageImpl() {
		super(eNS_URI, PropertiesExtWidgetsNebulaFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link PropertiesExtWidgetsNebulaPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static PropertiesExtWidgetsNebulaPackage init() {
		if (isInited)
			return (PropertiesExtWidgetsNebulaPackage) EPackage.Registry.INSTANCE
					.getEPackage(PropertiesExtWidgetsNebulaPackage.eNS_URI);

		// Obtain or create and register package
		PropertiesExtWidgetsNebulaPackageImpl thePropertiesExtWidgetsNebulaPackage = (PropertiesExtWidgetsNebulaPackageImpl) (EPackage.Registry.INSTANCE
				.get(eNS_URI) instanceof PropertiesExtWidgetsNebulaPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
						: new PropertiesExtWidgetsNebulaPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		PropertiesPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		thePropertiesExtWidgetsNebulaPackage.createPackageContents();

		// Initialize created meta-data
		thePropertiesExtWidgetsNebulaPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		thePropertiesExtWidgetsNebulaPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(PropertiesExtWidgetsNebulaPackage.eNS_URI, thePropertiesExtWidgetsNebulaPackage);
		return thePropertiesExtWidgetsNebulaPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getExtNebulaRichTextDescription() {
		return extNebulaRichTextDescriptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getExtNebulaRichTextDescription_ValueExpression() {
		return (EAttribute) extNebulaRichTextDescriptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getExtNebulaRichTextDescription_InitialOperation() {
		return (EReference) extNebulaRichTextDescriptionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getExtNebulaRichTextDescription_Style() {
		return (EReference) extNebulaRichTextDescriptionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getExtNebulaRichTextDescription_ConditionalStyles() {
		return (EReference) extNebulaRichTextDescriptionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getExtNebulaRichTextWidgetStyle() {
		return extNebulaRichTextWidgetStyleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getExtNebulaRichTextWidgetConditionalStyle() {
		return extNebulaRichTextWidgetConditionalStyleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getExtNebulaRichTextWidgetConditionalStyle_Style() {
		return (EReference) extNebulaRichTextWidgetConditionalStyleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PropertiesExtWidgetsNebulaFactory getPropertiesExtWidgetsNebulaFactory() {
		return (PropertiesExtWidgetsNebulaFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		extNebulaRichTextDescriptionEClass = createEClass(EXT_NEBULA_RICH_TEXT_DESCRIPTION);
		createEAttribute(extNebulaRichTextDescriptionEClass, EXT_NEBULA_RICH_TEXT_DESCRIPTION__VALUE_EXPRESSION);
		createEReference(extNebulaRichTextDescriptionEClass, EXT_NEBULA_RICH_TEXT_DESCRIPTION__INITIAL_OPERATION);
		createEReference(extNebulaRichTextDescriptionEClass, EXT_NEBULA_RICH_TEXT_DESCRIPTION__STYLE);
		createEReference(extNebulaRichTextDescriptionEClass, EXT_NEBULA_RICH_TEXT_DESCRIPTION__CONDITIONAL_STYLES);

		extNebulaRichTextWidgetStyleEClass = createEClass(EXT_NEBULA_RICH_TEXT_WIDGET_STYLE);

		extNebulaRichTextWidgetConditionalStyleEClass = createEClass(EXT_NEBULA_RICH_TEXT_WIDGET_CONDITIONAL_STYLE);
		createEReference(extNebulaRichTextWidgetConditionalStyleEClass,
				EXT_NEBULA_RICH_TEXT_WIDGET_CONDITIONAL_STYLE__STYLE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		PropertiesPackage thePropertiesPackage = (PropertiesPackage) EPackage.Registry.INSTANCE
				.getEPackage(PropertiesPackage.eNS_URI);
		DescriptionPackage theDescriptionPackage = (DescriptionPackage) EPackage.Registry.INSTANCE
				.getEPackage(DescriptionPackage.eNS_URI);
		ToolPackage theToolPackage = (ToolPackage) EPackage.Registry.INSTANCE.getEPackage(ToolPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		extNebulaRichTextDescriptionEClass.getESuperTypes().add(thePropertiesPackage.getWidgetDescription());
		extNebulaRichTextWidgetStyleEClass.getESuperTypes().add(thePropertiesPackage.getWidgetStyle());
		extNebulaRichTextWidgetConditionalStyleEClass.getESuperTypes()
				.add(thePropertiesPackage.getWidgetConditionalStyle());

		// Initialize classes and features; add operations and parameters
		initEClass(extNebulaRichTextDescriptionEClass, ExtNebulaRichTextDescription.class,
				"ExtNebulaRichTextDescription", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getExtNebulaRichTextDescription_ValueExpression(),
				theDescriptionPackage.getInterpretedExpression(), "valueExpression", null, 0, 1, //$NON-NLS-1$
				ExtNebulaRichTextDescription.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExtNebulaRichTextDescription_InitialOperation(), theToolPackage.getInitialOperation(), null,
				"initialOperation", null, 1, 1, ExtNebulaRichTextDescription.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExtNebulaRichTextDescription_Style(), this.getExtNebulaRichTextWidgetStyle(), null, "style", //$NON-NLS-1$
				null, 0, 1, ExtNebulaRichTextDescription.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getExtNebulaRichTextDescription_ConditionalStyles(),
				this.getExtNebulaRichTextWidgetConditionalStyle(), null, "conditionalStyles", null, 0, -1, //$NON-NLS-1$
				ExtNebulaRichTextDescription.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(extNebulaRichTextWidgetStyleEClass, ExtNebulaRichTextWidgetStyle.class,
				"ExtNebulaRichTextWidgetStyle", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		initEClass(extNebulaRichTextWidgetConditionalStyleEClass, ExtNebulaRichTextWidgetConditionalStyle.class,
				"ExtNebulaRichTextWidgetConditionalStyle", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getExtNebulaRichTextWidgetConditionalStyle_Style(), this.getExtNebulaRichTextWidgetStyle(), null,
				"style", null, 0, 1, ExtNebulaRichTextWidgetConditionalStyle.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //PropertiesExtWidgetsNebulaPackageImpl
