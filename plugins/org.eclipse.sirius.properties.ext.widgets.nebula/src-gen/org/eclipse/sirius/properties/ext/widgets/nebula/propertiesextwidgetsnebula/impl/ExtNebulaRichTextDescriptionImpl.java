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

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription;
import org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextWidgetConditionalStyle;
import org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextWidgetStyle;
import org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.PropertiesExtWidgetsNebulaPackage;

import org.eclipse.sirius.properties.impl.WidgetDescriptionImpl;

import org.eclipse.sirius.viewpoint.description.tool.InitialOperation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ext Nebula Rich Text Description</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.ExtNebulaRichTextDescriptionImpl#getValueExpression <em>Value Expression</em>}</li>
 *   <li>{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.ExtNebulaRichTextDescriptionImpl#getInitialOperation <em>Initial Operation</em>}</li>
 *   <li>{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.ExtNebulaRichTextDescriptionImpl#getStyle <em>Style</em>}</li>
 *   <li>{@link org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.impl.ExtNebulaRichTextDescriptionImpl#getConditionalStyles <em>Conditional Styles</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExtNebulaRichTextDescriptionImpl extends WidgetDescriptionImpl implements ExtNebulaRichTextDescription {
	/**
	 * The default value of the '{@link #getValueExpression() <em>Value Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValueExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String VALUE_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValueExpression() <em>Value Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValueExpression()
	 * @generated
	 * @ordered
	 */
	protected String valueExpression = VALUE_EXPRESSION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInitialOperation() <em>Initial Operation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialOperation()
	 * @generated
	 * @ordered
	 */
	protected InitialOperation initialOperation;

	/**
	 * The cached value of the '{@link #getStyle() <em>Style</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStyle()
	 * @generated
	 * @ordered
	 */
	protected ExtNebulaRichTextWidgetStyle style;

	/**
	 * The cached value of the '{@link #getConditionalStyles() <em>Conditional Styles</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConditionalStyles()
	 * @generated
	 * @ordered
	 */
	protected EList<ExtNebulaRichTextWidgetConditionalStyle> conditionalStyles;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExtNebulaRichTextDescriptionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PropertiesExtWidgetsNebulaPackage.Literals.EXT_NEBULA_RICH_TEXT_DESCRIPTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getValueExpression() {
		return valueExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setValueExpression(String newValueExpression) {
		String oldValueExpression = valueExpression;
		valueExpression = newValueExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__VALUE_EXPRESSION,
					oldValueExpression, valueExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public InitialOperation getInitialOperation() {
		return initialOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetInitialOperation(InitialOperation newInitialOperation, NotificationChain msgs) {
		InitialOperation oldInitialOperation = initialOperation;
		initialOperation = newInitialOperation;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__INITIAL_OPERATION,
					oldInitialOperation, newInitialOperation);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setInitialOperation(InitialOperation newInitialOperation) {
		if (newInitialOperation != initialOperation) {
			NotificationChain msgs = null;
			if (initialOperation != null)
				msgs = ((InternalEObject) initialOperation).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE
								- PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__INITIAL_OPERATION,
						null, msgs);
			if (newInitialOperation != null)
				msgs = ((InternalEObject) newInitialOperation).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE
								- PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__INITIAL_OPERATION,
						null, msgs);
			msgs = basicSetInitialOperation(newInitialOperation, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__INITIAL_OPERATION,
					newInitialOperation, newInitialOperation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ExtNebulaRichTextWidgetStyle getStyle() {
		return style;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetStyle(ExtNebulaRichTextWidgetStyle newStyle, NotificationChain msgs) {
		ExtNebulaRichTextWidgetStyle oldStyle = style;
		style = newStyle;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__STYLE, oldStyle, newStyle);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStyle(ExtNebulaRichTextWidgetStyle newStyle) {
		if (newStyle != style) {
			NotificationChain msgs = null;
			if (style != null)
				msgs = ((InternalEObject) style).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE
								- PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__STYLE,
						null, msgs);
			if (newStyle != null)
				msgs = ((InternalEObject) newStyle).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE
								- PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__STYLE,
						null, msgs);
			msgs = basicSetStyle(newStyle, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__STYLE, newStyle, newStyle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ExtNebulaRichTextWidgetConditionalStyle> getConditionalStyles() {
		if (conditionalStyles == null) {
			conditionalStyles = new EObjectContainmentEList<ExtNebulaRichTextWidgetConditionalStyle>(
					ExtNebulaRichTextWidgetConditionalStyle.class, this,
					PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__CONDITIONAL_STYLES);
		}
		return conditionalStyles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__INITIAL_OPERATION:
			return basicSetInitialOperation(null, msgs);
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__STYLE:
			return basicSetStyle(null, msgs);
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__CONDITIONAL_STYLES:
			return ((InternalEList<?>) getConditionalStyles()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__VALUE_EXPRESSION:
			return getValueExpression();
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__INITIAL_OPERATION:
			return getInitialOperation();
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__STYLE:
			return getStyle();
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__CONDITIONAL_STYLES:
			return getConditionalStyles();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__VALUE_EXPRESSION:
			setValueExpression((String) newValue);
			return;
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__INITIAL_OPERATION:
			setInitialOperation((InitialOperation) newValue);
			return;
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__STYLE:
			setStyle((ExtNebulaRichTextWidgetStyle) newValue);
			return;
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__CONDITIONAL_STYLES:
			getConditionalStyles().clear();
			getConditionalStyles().addAll((Collection<? extends ExtNebulaRichTextWidgetConditionalStyle>) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__VALUE_EXPRESSION:
			setValueExpression(VALUE_EXPRESSION_EDEFAULT);
			return;
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__INITIAL_OPERATION:
			setInitialOperation((InitialOperation) null);
			return;
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__STYLE:
			setStyle((ExtNebulaRichTextWidgetStyle) null);
			return;
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__CONDITIONAL_STYLES:
			getConditionalStyles().clear();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__VALUE_EXPRESSION:
			return VALUE_EXPRESSION_EDEFAULT == null ? valueExpression != null
					: !VALUE_EXPRESSION_EDEFAULT.equals(valueExpression);
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__INITIAL_OPERATION:
			return initialOperation != null;
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__STYLE:
			return style != null;
		case PropertiesExtWidgetsNebulaPackage.EXT_NEBULA_RICH_TEXT_DESCRIPTION__CONDITIONAL_STYLES:
			return conditionalStyles != null && !conditionalStyles.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (valueExpression: "); //$NON-NLS-1$
		result.append(valueExpression);
		result.append(')');
		return result.toString();
	}

} //ExtNebulaRichTextDescriptionImpl
