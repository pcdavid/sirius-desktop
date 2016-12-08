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

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.sirius.properties.AbstractSpinnerDescription;
import org.eclipse.sirius.properties.PropertiesPackage;
import org.eclipse.sirius.properties.SpinnerDescription;
import org.eclipse.sirius.properties.SpinnerWidgetConditionalStyle;
import org.eclipse.sirius.properties.SpinnerWidgetStyle;
import org.eclipse.sirius.viewpoint.description.tool.InitialOperation;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Abstract Spinner Description</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.sirius.properties.impl.AbstractSpinnerDescriptionImpl#getValueExpression <em>Value
 * Expression</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.impl.AbstractSpinnerDescriptionImpl#getDigitsExpression <em>Digits
 * Expression</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.impl.AbstractSpinnerDescriptionImpl#getIncrementExpression <em>Increment
 * Expression</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.impl.AbstractSpinnerDescriptionImpl#getMinExpression <em>Min
 * Expression</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.impl.AbstractSpinnerDescriptionImpl#getMaxExpression <em>Max
 * Expression</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.impl.AbstractSpinnerDescriptionImpl#getInitialOperation <em>Initial
 * Operation</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.impl.AbstractSpinnerDescriptionImpl#getStyle <em>Style</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.impl.AbstractSpinnerDescriptionImpl#getConditionalStyles <em>Conditional
 * Styles</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.impl.AbstractSpinnerDescriptionImpl#getExtends <em>Extends</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.impl.AbstractSpinnerDescriptionImpl#getFilterConditionalStylesFromExtendedSpinnerExpression
 * <em>Filter Conditional Styles From Extended Spinner Expression</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractSpinnerDescriptionImpl extends AbstractWidgetDescriptionImpl implements AbstractSpinnerDescription {
    /**
     * The default value of the '{@link #getValueExpression() <em>Value Expression</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @see #getValueExpression()
     * @generated
     * @ordered
     */
    protected static final String VALUE_EXPRESSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValueExpression() <em>Value Expression</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @see #getValueExpression()
     * @generated
     * @ordered
     */
    protected String valueExpression = AbstractSpinnerDescriptionImpl.VALUE_EXPRESSION_EDEFAULT;

    /**
     * The default value of the '{@link #getDigitsExpression() <em>Digits Expression</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getDigitsExpression()
     * @generated
     * @ordered
     */
    protected static final String DIGITS_EXPRESSION_EDEFAULT = "0"; //$NON-NLS-1$

    /**
     * The cached value of the '{@link #getDigitsExpression() <em>Digits Expression</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getDigitsExpression()
     * @generated
     * @ordered
     */
    protected String digitsExpression = AbstractSpinnerDescriptionImpl.DIGITS_EXPRESSION_EDEFAULT;

    /**
     * The default value of the '{@link #getIncrementExpression() <em>Increment Expression</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getIncrementExpression()
     * @generated
     * @ordered
     */
    protected static final String INCREMENT_EXPRESSION_EDEFAULT = "1"; //$NON-NLS-1$

    /**
     * The cached value of the '{@link #getIncrementExpression() <em>Increment Expression</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getIncrementExpression()
     * @generated
     * @ordered
     */
    protected String incrementExpression = AbstractSpinnerDescriptionImpl.INCREMENT_EXPRESSION_EDEFAULT;

    /**
     * The default value of the '{@link #getMinExpression() <em>Min Expression</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getMinExpression()
     * @generated
     * @ordered
     */
    protected static final String MIN_EXPRESSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMinExpression() <em>Min Expression</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getMinExpression()
     * @generated
     * @ordered
     */
    protected String minExpression = AbstractSpinnerDescriptionImpl.MIN_EXPRESSION_EDEFAULT;

    /**
     * The default value of the '{@link #getMaxExpression() <em>Max Expression</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getMaxExpression()
     * @generated
     * @ordered
     */
    protected static final String MAX_EXPRESSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMaxExpression() <em>Max Expression</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getMaxExpression()
     * @generated
     * @ordered
     */
    protected String maxExpression = AbstractSpinnerDescriptionImpl.MAX_EXPRESSION_EDEFAULT;

    /**
     * The cached value of the '{@link #getInitialOperation() <em>Initial Operation</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getInitialOperation()
     * @generated
     * @ordered
     */
    protected InitialOperation initialOperation;

    /**
     * The cached value of the '{@link #getStyle() <em>Style</em>}' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getStyle()
     * @generated
     * @ordered
     */
    protected SpinnerWidgetStyle style;

    /**
     * The cached value of the '{@link #getConditionalStyles() <em>Conditional Styles</em>}' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getConditionalStyles()
     * @generated
     * @ordered
     */
    protected EList<SpinnerWidgetConditionalStyle> conditionalStyles;

    /**
     * The cached value of the '{@link #getExtends() <em>Extends</em>}' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getExtends()
     * @generated
     * @ordered
     */
    protected SpinnerDescription extends_;

    /**
     * The default value of the '{@link #getFilterConditionalStylesFromExtendedSpinnerExpression() <em>Filter
     * Conditional Styles From Extended Spinner Expression</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @see #getFilterConditionalStylesFromExtendedSpinnerExpression()
     * @generated
     * @ordered
     */
    protected static final String FILTER_CONDITIONAL_STYLES_FROM_EXTENDED_SPINNER_EXPRESSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFilterConditionalStylesFromExtendedSpinnerExpression() <em>Filter Conditional
     * Styles From Extended Spinner Expression</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getFilterConditionalStylesFromExtendedSpinnerExpression()
     * @generated
     * @ordered
     */
    protected String filterConditionalStylesFromExtendedSpinnerExpression = AbstractSpinnerDescriptionImpl.FILTER_CONDITIONAL_STYLES_FROM_EXTENDED_SPINNER_EXPRESSION_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected AbstractSpinnerDescriptionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return PropertiesPackage.Literals.ABSTRACT_SPINNER_DESCRIPTION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getValueExpression() {
        return valueExpression;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setValueExpression(String newValueExpression) {
        String oldValueExpression = valueExpression;
        valueExpression = newValueExpression;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__VALUE_EXPRESSION, oldValueExpression, valueExpression));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getDigitsExpression() {
        return digitsExpression;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setDigitsExpression(String newDigitsExpression) {
        String oldDigitsExpression = digitsExpression;
        digitsExpression = newDigitsExpression;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__DIGITS_EXPRESSION, oldDigitsExpression, digitsExpression));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getIncrementExpression() {
        return incrementExpression;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIncrementExpression(String newIncrementExpression) {
        String oldIncrementExpression = incrementExpression;
        incrementExpression = newIncrementExpression;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__INCREMENT_EXPRESSION, oldIncrementExpression, incrementExpression));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getMinExpression() {
        return minExpression;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setMinExpression(String newMinExpression) {
        String oldMinExpression = minExpression;
        minExpression = newMinExpression;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__MIN_EXPRESSION, oldMinExpression, minExpression));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getMaxExpression() {
        return maxExpression;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setMaxExpression(String newMaxExpression) {
        String oldMaxExpression = maxExpression;
        maxExpression = newMaxExpression;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__MAX_EXPRESSION, oldMaxExpression, maxExpression));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public InitialOperation getInitialOperation() {
        return initialOperation;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetInitialOperation(InitialOperation newInitialOperation, NotificationChain msgs) {
        InitialOperation oldInitialOperation = initialOperation;
        initialOperation = newInitialOperation;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__INITIAL_OPERATION, oldInitialOperation, newInitialOperation);
            if (msgs == null) {
                msgs = notification;
            } else {
                msgs.add(notification);
            }
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setInitialOperation(InitialOperation newInitialOperation) {
        if (newInitialOperation != initialOperation) {
            NotificationChain msgs = null;
            if (initialOperation != null) {
                msgs = ((InternalEObject) initialOperation).eInverseRemove(this, InternalEObject.EOPPOSITE_FEATURE_BASE - PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__INITIAL_OPERATION, null,
                        msgs);
            }
            if (newInitialOperation != null) {
                msgs = ((InternalEObject) newInitialOperation).eInverseAdd(this, InternalEObject.EOPPOSITE_FEATURE_BASE - PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__INITIAL_OPERATION, null,
                        msgs);
            }
            msgs = basicSetInitialOperation(newInitialOperation, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__INITIAL_OPERATION, newInitialOperation, newInitialOperation));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SpinnerWidgetStyle getStyle() {
        return style;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetStyle(SpinnerWidgetStyle newStyle, NotificationChain msgs) {
        SpinnerWidgetStyle oldStyle = style;
        style = newStyle;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__STYLE, oldStyle, newStyle);
            if (msgs == null) {
                msgs = notification;
            } else {
                msgs.add(notification);
            }
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setStyle(SpinnerWidgetStyle newStyle) {
        if (newStyle != style) {
            NotificationChain msgs = null;
            if (style != null) {
                msgs = ((InternalEObject) style).eInverseRemove(this, InternalEObject.EOPPOSITE_FEATURE_BASE - PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__STYLE, null, msgs);
            }
            if (newStyle != null) {
                msgs = ((InternalEObject) newStyle).eInverseAdd(this, InternalEObject.EOPPOSITE_FEATURE_BASE - PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__STYLE, null, msgs);
            }
            msgs = basicSetStyle(newStyle, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__STYLE, newStyle, newStyle));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<SpinnerWidgetConditionalStyle> getConditionalStyles() {
        if (conditionalStyles == null) {
            conditionalStyles = new EObjectContainmentEList<SpinnerWidgetConditionalStyle>(SpinnerWidgetConditionalStyle.class, this,
                    PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__CONDITIONAL_STYLES);
        }
        return conditionalStyles;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SpinnerDescription getExtends() {
        if (extends_ != null && extends_.eIsProxy()) {
            InternalEObject oldExtends = (InternalEObject) extends_;
            extends_ = (SpinnerDescription) eResolveProxy(oldExtends);
            if (extends_ != oldExtends) {
                if (eNotificationRequired()) {
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__EXTENDS, oldExtends, extends_));
                }
            }
        }
        return extends_;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public SpinnerDescription basicGetExtends() {
        return extends_;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setExtends(SpinnerDescription newExtends) {
        SpinnerDescription oldExtends = extends_;
        extends_ = newExtends;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__EXTENDS, oldExtends, extends_));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getFilterConditionalStylesFromExtendedSpinnerExpression() {
        return filterConditionalStylesFromExtendedSpinnerExpression;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setFilterConditionalStylesFromExtendedSpinnerExpression(String newFilterConditionalStylesFromExtendedSpinnerExpression) {
        String oldFilterConditionalStylesFromExtendedSpinnerExpression = filterConditionalStylesFromExtendedSpinnerExpression;
        filterConditionalStylesFromExtendedSpinnerExpression = newFilterConditionalStylesFromExtendedSpinnerExpression;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__FILTER_CONDITIONAL_STYLES_FROM_EXTENDED_SPINNER_EXPRESSION,
                    oldFilterConditionalStylesFromExtendedSpinnerExpression, filterConditionalStylesFromExtendedSpinnerExpression));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__INITIAL_OPERATION:
            return basicSetInitialOperation(null, msgs);
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__STYLE:
            return basicSetStyle(null, msgs);
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__CONDITIONAL_STYLES:
            return ((InternalEList<?>) getConditionalStyles()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__VALUE_EXPRESSION:
            return getValueExpression();
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__DIGITS_EXPRESSION:
            return getDigitsExpression();
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__INCREMENT_EXPRESSION:
            return getIncrementExpression();
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__MIN_EXPRESSION:
            return getMinExpression();
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__MAX_EXPRESSION:
            return getMaxExpression();
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__INITIAL_OPERATION:
            return getInitialOperation();
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__STYLE:
            return getStyle();
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__CONDITIONAL_STYLES:
            return getConditionalStyles();
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__EXTENDS:
            if (resolve) {
                return getExtends();
            }
            return basicGetExtends();
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__FILTER_CONDITIONAL_STYLES_FROM_EXTENDED_SPINNER_EXPRESSION:
            return getFilterConditionalStylesFromExtendedSpinnerExpression();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__VALUE_EXPRESSION:
            setValueExpression((String) newValue);
            return;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__DIGITS_EXPRESSION:
            setDigitsExpression((String) newValue);
            return;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__INCREMENT_EXPRESSION:
            setIncrementExpression((String) newValue);
            return;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__MIN_EXPRESSION:
            setMinExpression((String) newValue);
            return;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__MAX_EXPRESSION:
            setMaxExpression((String) newValue);
            return;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__INITIAL_OPERATION:
            setInitialOperation((InitialOperation) newValue);
            return;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__STYLE:
            setStyle((SpinnerWidgetStyle) newValue);
            return;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__CONDITIONAL_STYLES:
            getConditionalStyles().clear();
            getConditionalStyles().addAll((Collection<? extends SpinnerWidgetConditionalStyle>) newValue);
            return;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__EXTENDS:
            setExtends((SpinnerDescription) newValue);
            return;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__FILTER_CONDITIONAL_STYLES_FROM_EXTENDED_SPINNER_EXPRESSION:
            setFilterConditionalStylesFromExtendedSpinnerExpression((String) newValue);
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
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__VALUE_EXPRESSION:
            setValueExpression(AbstractSpinnerDescriptionImpl.VALUE_EXPRESSION_EDEFAULT);
            return;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__DIGITS_EXPRESSION:
            setDigitsExpression(AbstractSpinnerDescriptionImpl.DIGITS_EXPRESSION_EDEFAULT);
            return;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__INCREMENT_EXPRESSION:
            setIncrementExpression(AbstractSpinnerDescriptionImpl.INCREMENT_EXPRESSION_EDEFAULT);
            return;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__MIN_EXPRESSION:
            setMinExpression(AbstractSpinnerDescriptionImpl.MIN_EXPRESSION_EDEFAULT);
            return;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__MAX_EXPRESSION:
            setMaxExpression(AbstractSpinnerDescriptionImpl.MAX_EXPRESSION_EDEFAULT);
            return;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__INITIAL_OPERATION:
            setInitialOperation((InitialOperation) null);
            return;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__STYLE:
            setStyle((SpinnerWidgetStyle) null);
            return;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__CONDITIONAL_STYLES:
            getConditionalStyles().clear();
            return;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__EXTENDS:
            setExtends((SpinnerDescription) null);
            return;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__FILTER_CONDITIONAL_STYLES_FROM_EXTENDED_SPINNER_EXPRESSION:
            setFilterConditionalStylesFromExtendedSpinnerExpression(AbstractSpinnerDescriptionImpl.FILTER_CONDITIONAL_STYLES_FROM_EXTENDED_SPINNER_EXPRESSION_EDEFAULT);
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
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__VALUE_EXPRESSION:
            return AbstractSpinnerDescriptionImpl.VALUE_EXPRESSION_EDEFAULT == null ? valueExpression != null : !AbstractSpinnerDescriptionImpl.VALUE_EXPRESSION_EDEFAULT.equals(valueExpression);
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__DIGITS_EXPRESSION:
            return AbstractSpinnerDescriptionImpl.DIGITS_EXPRESSION_EDEFAULT == null ? digitsExpression != null : !AbstractSpinnerDescriptionImpl.DIGITS_EXPRESSION_EDEFAULT.equals(digitsExpression);
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__INCREMENT_EXPRESSION:
            return AbstractSpinnerDescriptionImpl.INCREMENT_EXPRESSION_EDEFAULT == null ? incrementExpression != null
                    : !AbstractSpinnerDescriptionImpl.INCREMENT_EXPRESSION_EDEFAULT.equals(incrementExpression);
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__MIN_EXPRESSION:
            return AbstractSpinnerDescriptionImpl.MIN_EXPRESSION_EDEFAULT == null ? minExpression != null : !AbstractSpinnerDescriptionImpl.MIN_EXPRESSION_EDEFAULT.equals(minExpression);
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__MAX_EXPRESSION:
            return AbstractSpinnerDescriptionImpl.MAX_EXPRESSION_EDEFAULT == null ? maxExpression != null : !AbstractSpinnerDescriptionImpl.MAX_EXPRESSION_EDEFAULT.equals(maxExpression);
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__INITIAL_OPERATION:
            return initialOperation != null;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__STYLE:
            return style != null;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__CONDITIONAL_STYLES:
            return conditionalStyles != null && !conditionalStyles.isEmpty();
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__EXTENDS:
            return extends_ != null;
        case PropertiesPackage.ABSTRACT_SPINNER_DESCRIPTION__FILTER_CONDITIONAL_STYLES_FROM_EXTENDED_SPINNER_EXPRESSION:
            return AbstractSpinnerDescriptionImpl.FILTER_CONDITIONAL_STYLES_FROM_EXTENDED_SPINNER_EXPRESSION_EDEFAULT == null ? filterConditionalStylesFromExtendedSpinnerExpression != null
                    : !AbstractSpinnerDescriptionImpl.FILTER_CONDITIONAL_STYLES_FROM_EXTENDED_SPINNER_EXPRESSION_EDEFAULT.equals(filterConditionalStylesFromExtendedSpinnerExpression);
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
        result.append(" (valueExpression: "); //$NON-NLS-1$
        result.append(valueExpression);
        result.append(", digitsExpression: "); //$NON-NLS-1$
        result.append(digitsExpression);
        result.append(", incrementExpression: "); //$NON-NLS-1$
        result.append(incrementExpression);
        result.append(", minExpression: "); //$NON-NLS-1$
        result.append(minExpression);
        result.append(", maxExpression: "); //$NON-NLS-1$
        result.append(maxExpression);
        result.append(", filterConditionalStylesFromExtendedSpinnerExpression: "); //$NON-NLS-1$
        result.append(filterConditionalStylesFromExtendedSpinnerExpression);
        result.append(')');
        return result.toString();
    }

} // AbstractSpinnerDescriptionImpl
