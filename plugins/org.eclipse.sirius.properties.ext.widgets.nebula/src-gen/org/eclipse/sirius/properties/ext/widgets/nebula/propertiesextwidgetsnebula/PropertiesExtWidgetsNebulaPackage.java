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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.sirius.properties.PropertiesPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.PropertiesExtWidgetsNebulaFactory
 * @model kind="package"
 * @generated
 */
public interface PropertiesExtWidgetsNebulaPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "propertiesextwidgetsnebula"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/sirius/properties/1.0.0/ext/widgets/nebula"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "properties-ext-widgets-nebula"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PropertiesExtWidgetsNebulaPackage eINSTANCE = org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.PropertiesExtWidgetsNebulaPackageImpl
			.init();

	/**
	 * The meta object id for the '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.ExtNebulaRichTextDescriptionImpl <em>Ext Nebula Rich Text Description</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.ExtNebulaRichTextDescriptionImpl
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.PropertiesExtWidgetsNebulaPackageImpl#getExtNebulaRichTextDescription()
	 * @generated
	 */
	int EXT_NEBULA_RICH_TEXT_DESCRIPTION = 0;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXT_NEBULA_RICH_TEXT_DESCRIPTION__IDENTIFIER = PropertiesPackage.WIDGET_DESCRIPTION__IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Label Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXT_NEBULA_RICH_TEXT_DESCRIPTION__LABEL_EXPRESSION = PropertiesPackage.WIDGET_DESCRIPTION__LABEL_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Help Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXT_NEBULA_RICH_TEXT_DESCRIPTION__HELP_EXPRESSION = PropertiesPackage.WIDGET_DESCRIPTION__HELP_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Is Enabled Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXT_NEBULA_RICH_TEXT_DESCRIPTION__IS_ENABLED_EXPRESSION = PropertiesPackage.WIDGET_DESCRIPTION__IS_ENABLED_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Value Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXT_NEBULA_RICH_TEXT_DESCRIPTION__VALUE_EXPRESSION = PropertiesPackage.WIDGET_DESCRIPTION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Initial Operation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXT_NEBULA_RICH_TEXT_DESCRIPTION__INITIAL_OPERATION = PropertiesPackage.WIDGET_DESCRIPTION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXT_NEBULA_RICH_TEXT_DESCRIPTION__STYLE = PropertiesPackage.WIDGET_DESCRIPTION_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Conditional Styles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXT_NEBULA_RICH_TEXT_DESCRIPTION__CONDITIONAL_STYLES = PropertiesPackage.WIDGET_DESCRIPTION_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Ext Nebula Rich Text Description</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXT_NEBULA_RICH_TEXT_DESCRIPTION_FEATURE_COUNT = PropertiesPackage.WIDGET_DESCRIPTION_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.ExtNebulaRichTextWidgetStyleImpl <em>Ext Nebula Rich Text Widget Style</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.ExtNebulaRichTextWidgetStyleImpl
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.PropertiesExtWidgetsNebulaPackageImpl#getExtNebulaRichTextWidgetStyle()
	 * @generated
	 */
	int EXT_NEBULA_RICH_TEXT_WIDGET_STYLE = 1;

	/**
	 * The feature id for the '<em><b>Label Font Name Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXT_NEBULA_RICH_TEXT_WIDGET_STYLE__LABEL_FONT_NAME_EXPRESSION = PropertiesPackage.WIDGET_STYLE__LABEL_FONT_NAME_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Label Font Size Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXT_NEBULA_RICH_TEXT_WIDGET_STYLE__LABEL_FONT_SIZE_EXPRESSION = PropertiesPackage.WIDGET_STYLE__LABEL_FONT_SIZE_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Label Background Color</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXT_NEBULA_RICH_TEXT_WIDGET_STYLE__LABEL_BACKGROUND_COLOR = PropertiesPackage.WIDGET_STYLE__LABEL_BACKGROUND_COLOR;

	/**
	 * The feature id for the '<em><b>Label Foreground Color</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXT_NEBULA_RICH_TEXT_WIDGET_STYLE__LABEL_FOREGROUND_COLOR = PropertiesPackage.WIDGET_STYLE__LABEL_FOREGROUND_COLOR;

	/**
	 * The feature id for the '<em><b>Label Font Format</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXT_NEBULA_RICH_TEXT_WIDGET_STYLE__LABEL_FONT_FORMAT = PropertiesPackage.WIDGET_STYLE__LABEL_FONT_FORMAT;

	/**
	 * The number of structural features of the '<em>Ext Nebula Rich Text Widget Style</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXT_NEBULA_RICH_TEXT_WIDGET_STYLE_FEATURE_COUNT = PropertiesPackage.WIDGET_STYLE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.ExtNebulaRichTextWidgetConditionalStyleImpl <em>Ext Nebula Rich Text Widget Conditional Style</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.ExtNebulaRichTextWidgetConditionalStyleImpl
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.PropertiesExtWidgetsNebulaPackageImpl#getExtNebulaRichTextWidgetConditionalStyle()
	 * @generated
	 */
	int EXT_NEBULA_RICH_TEXT_WIDGET_CONDITIONAL_STYLE = 2;

	/**
	 * The feature id for the '<em><b>Precondition Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXT_NEBULA_RICH_TEXT_WIDGET_CONDITIONAL_STYLE__PRECONDITION_EXPRESSION = PropertiesPackage.WIDGET_CONDITIONAL_STYLE__PRECONDITION_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXT_NEBULA_RICH_TEXT_WIDGET_CONDITIONAL_STYLE__STYLE = PropertiesPackage.WIDGET_CONDITIONAL_STYLE_FEATURE_COUNT
			+ 0;

	/**
	 * The number of structural features of the '<em>Ext Nebula Rich Text Widget Conditional Style</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXT_NEBULA_RICH_TEXT_WIDGET_CONDITIONAL_STYLE_FEATURE_COUNT = PropertiesPackage.WIDGET_CONDITIONAL_STYLE_FEATURE_COUNT
			+ 1;

	/**
	 * Returns the meta object for class '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription <em>Ext Nebula Rich Text Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ext Nebula Rich Text Description</em>'.
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription
	 * @generated
	 */
	EClass getExtNebulaRichTextDescription();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription#getValueExpression <em>Value Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value Expression</em>'.
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription#getValueExpression()
	 * @see #getExtNebulaRichTextDescription()
	 * @generated
	 */
	EAttribute getExtNebulaRichTextDescription_ValueExpression();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription#getInitialOperation <em>Initial Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Initial Operation</em>'.
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription#getInitialOperation()
	 * @see #getExtNebulaRichTextDescription()
	 * @generated
	 */
	EReference getExtNebulaRichTextDescription_InitialOperation();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription#getStyle <em>Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Style</em>'.
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription#getStyle()
	 * @see #getExtNebulaRichTextDescription()
	 * @generated
	 */
	EReference getExtNebulaRichTextDescription_Style();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription#getConditionalStyles <em>Conditional Styles</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Conditional Styles</em>'.
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription#getConditionalStyles()
	 * @see #getExtNebulaRichTextDescription()
	 * @generated
	 */
	EReference getExtNebulaRichTextDescription_ConditionalStyles();

	/**
	 * Returns the meta object for class '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextWidgetStyle <em>Ext Nebula Rich Text Widget Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ext Nebula Rich Text Widget Style</em>'.
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextWidgetStyle
	 * @generated
	 */
	EClass getExtNebulaRichTextWidgetStyle();

	/**
	 * Returns the meta object for class '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextWidgetConditionalStyle <em>Ext Nebula Rich Text Widget Conditional Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ext Nebula Rich Text Widget Conditional Style</em>'.
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextWidgetConditionalStyle
	 * @generated
	 */
	EClass getExtNebulaRichTextWidgetConditionalStyle();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextWidgetConditionalStyle#getStyle <em>Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Style</em>'.
	 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextWidgetConditionalStyle#getStyle()
	 * @see #getExtNebulaRichTextWidgetConditionalStyle()
	 * @generated
	 */
	EReference getExtNebulaRichTextWidgetConditionalStyle_Style();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	PropertiesExtWidgetsNebulaFactory getPropertiesExtWidgetsNebulaFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.ExtNebulaRichTextDescriptionImpl <em>Ext Nebula Rich Text Description</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.ExtNebulaRichTextDescriptionImpl
		 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.PropertiesExtWidgetsNebulaPackageImpl#getExtNebulaRichTextDescription()
		 * @generated
		 */
		EClass EXT_NEBULA_RICH_TEXT_DESCRIPTION = eINSTANCE.getExtNebulaRichTextDescription();

		/**
		 * The meta object literal for the '<em><b>Value Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXT_NEBULA_RICH_TEXT_DESCRIPTION__VALUE_EXPRESSION = eINSTANCE
				.getExtNebulaRichTextDescription_ValueExpression();

		/**
		 * The meta object literal for the '<em><b>Initial Operation</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXT_NEBULA_RICH_TEXT_DESCRIPTION__INITIAL_OPERATION = eINSTANCE
				.getExtNebulaRichTextDescription_InitialOperation();

		/**
		 * The meta object literal for the '<em><b>Style</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXT_NEBULA_RICH_TEXT_DESCRIPTION__STYLE = eINSTANCE.getExtNebulaRichTextDescription_Style();

		/**
		 * The meta object literal for the '<em><b>Conditional Styles</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXT_NEBULA_RICH_TEXT_DESCRIPTION__CONDITIONAL_STYLES = eINSTANCE
				.getExtNebulaRichTextDescription_ConditionalStyles();

		/**
		 * The meta object literal for the '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.ExtNebulaRichTextWidgetStyleImpl <em>Ext Nebula Rich Text Widget Style</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.ExtNebulaRichTextWidgetStyleImpl
		 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.PropertiesExtWidgetsNebulaPackageImpl#getExtNebulaRichTextWidgetStyle()
		 * @generated
		 */
		EClass EXT_NEBULA_RICH_TEXT_WIDGET_STYLE = eINSTANCE.getExtNebulaRichTextWidgetStyle();

		/**
		 * The meta object literal for the '{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.ExtNebulaRichTextWidgetConditionalStyleImpl <em>Ext Nebula Rich Text Widget Conditional Style</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.ExtNebulaRichTextWidgetConditionalStyleImpl
		 * @see org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.PropertiesExtWidgetsNebulaPackageImpl#getExtNebulaRichTextWidgetConditionalStyle()
		 * @generated
		 */
		EClass EXT_NEBULA_RICH_TEXT_WIDGET_CONDITIONAL_STYLE = eINSTANCE.getExtNebulaRichTextWidgetConditionalStyle();

		/**
		 * The meta object literal for the '<em><b>Style</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXT_NEBULA_RICH_TEXT_WIDGET_CONDITIONAL_STYLE__STYLE = eINSTANCE
				.getExtNebulaRichTextWidgetConditionalStyle_Style();

	}

} //PropertiesExtWidgetsNebulaPackage
