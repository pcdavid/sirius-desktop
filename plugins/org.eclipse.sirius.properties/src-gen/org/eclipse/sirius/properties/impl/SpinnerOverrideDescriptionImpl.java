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
package org.eclipse.sirius.properties.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.sirius.properties.PropertiesPackage;
import org.eclipse.sirius.properties.SpinnerDescription;
import org.eclipse.sirius.properties.SpinnerOverrideDescription;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Spinner Override Description</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.sirius.properties.impl.SpinnerOverrideDescriptionImpl#getOverrides <em>Overrides</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.impl.SpinnerOverrideDescriptionImpl#getFilterConditionalStylesFromOverriddenSpinnerExpression
 * <em>Filter Conditional Styles From Overridden Spinner Expression</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SpinnerOverrideDescriptionImpl extends AbstractSpinnerDescriptionImpl implements SpinnerOverrideDescription {
    /**
     * The cached value of the '{@link #getOverrides() <em>Overrides</em>}' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getOverrides()
     * @generated
     * @ordered
     */
    protected SpinnerDescription overrides;

    /**
     * The default value of the '{@link #getFilterConditionalStylesFromOverriddenSpinnerExpression() <em>Filter
     * Conditional Styles From Overridden Spinner Expression</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @see #getFilterConditionalStylesFromOverriddenSpinnerExpression()
     * @generated
     * @ordered
     */
    protected static final String FILTER_CONDITIONAL_STYLES_FROM_OVERRIDDEN_SPINNER_EXPRESSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFilterConditionalStylesFromOverriddenSpinnerExpression() <em>Filter
     * Conditional Styles From Overridden Spinner Expression</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @see #getFilterConditionalStylesFromOverriddenSpinnerExpression()
     * @generated
     * @ordered
     */
    protected String filterConditionalStylesFromOverriddenSpinnerExpression = SpinnerOverrideDescriptionImpl.FILTER_CONDITIONAL_STYLES_FROM_OVERRIDDEN_SPINNER_EXPRESSION_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected SpinnerOverrideDescriptionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return PropertiesPackage.Literals.SPINNER_OVERRIDE_DESCRIPTION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SpinnerDescription getOverrides() {
        if (overrides != null && overrides.eIsProxy()) {
            InternalEObject oldOverrides = (InternalEObject) overrides;
            overrides = (SpinnerDescription) eResolveProxy(oldOverrides);
            if (overrides != oldOverrides) {
                if (eNotificationRequired()) {
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, PropertiesPackage.SPINNER_OVERRIDE_DESCRIPTION__OVERRIDES, oldOverrides, overrides));
                }
            }
        }
        return overrides;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public SpinnerDescription basicGetOverrides() {
        return overrides;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setOverrides(SpinnerDescription newOverrides) {
        SpinnerDescription oldOverrides = overrides;
        overrides = newOverrides;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.SPINNER_OVERRIDE_DESCRIPTION__OVERRIDES, oldOverrides, overrides));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getFilterConditionalStylesFromOverriddenSpinnerExpression() {
        return filterConditionalStylesFromOverriddenSpinnerExpression;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setFilterConditionalStylesFromOverriddenSpinnerExpression(String newFilterConditionalStylesFromOverriddenSpinnerExpression) {
        String oldFilterConditionalStylesFromOverriddenSpinnerExpression = filterConditionalStylesFromOverriddenSpinnerExpression;
        filterConditionalStylesFromOverriddenSpinnerExpression = newFilterConditionalStylesFromOverriddenSpinnerExpression;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.SPINNER_OVERRIDE_DESCRIPTION__FILTER_CONDITIONAL_STYLES_FROM_OVERRIDDEN_SPINNER_EXPRESSION,
                    oldFilterConditionalStylesFromOverriddenSpinnerExpression, filterConditionalStylesFromOverriddenSpinnerExpression));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case PropertiesPackage.SPINNER_OVERRIDE_DESCRIPTION__OVERRIDES:
            if (resolve) {
                return getOverrides();
            }
            return basicGetOverrides();
        case PropertiesPackage.SPINNER_OVERRIDE_DESCRIPTION__FILTER_CONDITIONAL_STYLES_FROM_OVERRIDDEN_SPINNER_EXPRESSION:
            return getFilterConditionalStylesFromOverriddenSpinnerExpression();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case PropertiesPackage.SPINNER_OVERRIDE_DESCRIPTION__OVERRIDES:
            setOverrides((SpinnerDescription) newValue);
            return;
        case PropertiesPackage.SPINNER_OVERRIDE_DESCRIPTION__FILTER_CONDITIONAL_STYLES_FROM_OVERRIDDEN_SPINNER_EXPRESSION:
            setFilterConditionalStylesFromOverriddenSpinnerExpression((String) newValue);
            return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
        case PropertiesPackage.SPINNER_OVERRIDE_DESCRIPTION__OVERRIDES:
            setOverrides((SpinnerDescription) null);
            return;
        case PropertiesPackage.SPINNER_OVERRIDE_DESCRIPTION__FILTER_CONDITIONAL_STYLES_FROM_OVERRIDDEN_SPINNER_EXPRESSION:
            setFilterConditionalStylesFromOverriddenSpinnerExpression(SpinnerOverrideDescriptionImpl.FILTER_CONDITIONAL_STYLES_FROM_OVERRIDDEN_SPINNER_EXPRESSION_EDEFAULT);
            return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
        case PropertiesPackage.SPINNER_OVERRIDE_DESCRIPTION__OVERRIDES:
            return overrides != null;
        case PropertiesPackage.SPINNER_OVERRIDE_DESCRIPTION__FILTER_CONDITIONAL_STYLES_FROM_OVERRIDDEN_SPINNER_EXPRESSION:
            return SpinnerOverrideDescriptionImpl.FILTER_CONDITIONAL_STYLES_FROM_OVERRIDDEN_SPINNER_EXPRESSION_EDEFAULT == null ? filterConditionalStylesFromOverriddenSpinnerExpression != null
                    : !SpinnerOverrideDescriptionImpl.FILTER_CONDITIONAL_STYLES_FROM_OVERRIDDEN_SPINNER_EXPRESSION_EDEFAULT.equals(filterConditionalStylesFromOverriddenSpinnerExpression);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) {
            return super.toString();
        }

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (filterConditionalStylesFromOverriddenSpinnerExpression: "); //$NON-NLS-1$
        result.append(filterConditionalStylesFromOverriddenSpinnerExpression);
        result.append(')');
        return result.toString();
    }

} // SpinnerOverrideDescriptionImpl
