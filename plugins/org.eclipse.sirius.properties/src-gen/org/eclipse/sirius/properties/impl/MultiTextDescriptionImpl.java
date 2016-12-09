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
import org.eclipse.sirius.properties.MultiTextDescription;
import org.eclipse.sirius.properties.MultiTextWidgetConditionalStyle;
import org.eclipse.sirius.properties.MultiTextWidgetStyle;
import org.eclipse.sirius.properties.PropertiesPackage;
import org.eclipse.sirius.viewpoint.description.tool.InitialOperation;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Multi
 * Text Description</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.sirius.properties.impl.MultiTextDescriptionImpl#getAttributeOwnerExpression
 * <em>Attribute Owner Expression</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.impl.MultiTextDescriptionImpl#getAttributeNameExpression
 * <em>Attribute Name Expression</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.impl.MultiTextDescriptionImpl#getInitialOperation
 * <em>Initial Operation</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.impl.MultiTextDescriptionImpl#getStyle
 * <em>Style</em>}</li>
 * <li>{@link org.eclipse.sirius.properties.impl.MultiTextDescriptionImpl#getConditionalStyles
 * <em>Conditional Styles</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MultiTextDescriptionImpl extends WidgetDescriptionImpl implements MultiTextDescription {
    /**
     * The default value of the '{@link #getAttributeOwnerExpression()
     * <em>Attribute Owner Expression</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getAttributeOwnerExpression()
     * @generated
     * @ordered
     */
    protected static final String ATTRIBUTE_OWNER_EXPRESSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAttributeOwnerExpression()
     * <em>Attribute Owner Expression</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getAttributeOwnerExpression()
     * @generated
     * @ordered
     */
    protected String attributeOwnerExpression = MultiTextDescriptionImpl.ATTRIBUTE_OWNER_EXPRESSION_EDEFAULT;

    /**
     * The default value of the '{@link #getAttributeNameExpression()
     * <em>Attribute Name Expression</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getAttributeNameExpression()
     * @generated
     * @ordered
     */
    protected static final String ATTRIBUTE_NAME_EXPRESSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAttributeNameExpression()
     * <em>Attribute Name Expression</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getAttributeNameExpression()
     * @generated
     * @ordered
     */
    protected String attributeNameExpression = MultiTextDescriptionImpl.ATTRIBUTE_NAME_EXPRESSION_EDEFAULT;

    /**
     * The cached value of the '{@link #getInitialOperation() <em>Initial
     * Operation</em>}' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getInitialOperation()
     * @generated
     * @ordered
     */
    protected InitialOperation initialOperation;

    /**
     * The cached value of the '{@link #getStyle() <em>Style</em>}' containment
     * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getStyle()
     * @generated
     * @ordered
     */
    protected MultiTextWidgetStyle style;

    /**
     * The cached value of the '{@link #getConditionalStyles() <em>Conditional
     * Styles</em>}' containment reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getConditionalStyles()
     * @generated
     * @ordered
     */
    protected EList<MultiTextWidgetConditionalStyle> conditionalStyles;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected MultiTextDescriptionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return PropertiesPackage.Literals.MULTI_TEXT_DESCRIPTION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getAttributeOwnerExpression() {
        return attributeOwnerExpression;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setAttributeOwnerExpression(String newAttributeOwnerExpression) {
        String oldAttributeOwnerExpression = attributeOwnerExpression;
        attributeOwnerExpression = newAttributeOwnerExpression;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.MULTI_TEXT_DESCRIPTION__ATTRIBUTE_OWNER_EXPRESSION, oldAttributeOwnerExpression, attributeOwnerExpression));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getAttributeNameExpression() {
        return attributeNameExpression;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setAttributeNameExpression(String newAttributeNameExpression) {
        String oldAttributeNameExpression = attributeNameExpression;
        attributeNameExpression = newAttributeNameExpression;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.MULTI_TEXT_DESCRIPTION__ATTRIBUTE_NAME_EXPRESSION, oldAttributeNameExpression, attributeNameExpression));
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PropertiesPackage.MULTI_TEXT_DESCRIPTION__INITIAL_OPERATION, oldInitialOperation, newInitialOperation);
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
                msgs = ((InternalEObject) initialOperation).eInverseRemove(this, InternalEObject.EOPPOSITE_FEATURE_BASE - PropertiesPackage.MULTI_TEXT_DESCRIPTION__INITIAL_OPERATION, null, msgs);
            }
            if (newInitialOperation != null) {
                msgs = ((InternalEObject) newInitialOperation).eInverseAdd(this, InternalEObject.EOPPOSITE_FEATURE_BASE - PropertiesPackage.MULTI_TEXT_DESCRIPTION__INITIAL_OPERATION, null, msgs);
            }
            msgs = basicSetInitialOperation(newInitialOperation, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.MULTI_TEXT_DESCRIPTION__INITIAL_OPERATION, newInitialOperation, newInitialOperation));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public MultiTextWidgetStyle getStyle() {
        return style;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetStyle(MultiTextWidgetStyle newStyle, NotificationChain msgs) {
        MultiTextWidgetStyle oldStyle = style;
        style = newStyle;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PropertiesPackage.MULTI_TEXT_DESCRIPTION__STYLE, oldStyle, newStyle);
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
    public void setStyle(MultiTextWidgetStyle newStyle) {
        if (newStyle != style) {
            NotificationChain msgs = null;
            if (style != null) {
                msgs = ((InternalEObject) style).eInverseRemove(this, InternalEObject.EOPPOSITE_FEATURE_BASE - PropertiesPackage.MULTI_TEXT_DESCRIPTION__STYLE, null, msgs);
            }
            if (newStyle != null) {
                msgs = ((InternalEObject) newStyle).eInverseAdd(this, InternalEObject.EOPPOSITE_FEATURE_BASE - PropertiesPackage.MULTI_TEXT_DESCRIPTION__STYLE, null, msgs);
            }
            msgs = basicSetStyle(newStyle, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.MULTI_TEXT_DESCRIPTION__STYLE, newStyle, newStyle));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<MultiTextWidgetConditionalStyle> getConditionalStyles() {
        if (conditionalStyles == null) {
            conditionalStyles = new EObjectContainmentEList<MultiTextWidgetConditionalStyle>(MultiTextWidgetConditionalStyle.class, this, PropertiesPackage.MULTI_TEXT_DESCRIPTION__CONDITIONAL_STYLES);
        }
        return conditionalStyles;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__INITIAL_OPERATION:
            return basicSetInitialOperation(null, msgs);
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__STYLE:
            return basicSetStyle(null, msgs);
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__CONDITIONAL_STYLES:
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
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__ATTRIBUTE_OWNER_EXPRESSION:
            return getAttributeOwnerExpression();
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__ATTRIBUTE_NAME_EXPRESSION:
            return getAttributeNameExpression();
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__INITIAL_OPERATION:
            return getInitialOperation();
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__STYLE:
            return getStyle();
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__CONDITIONAL_STYLES:
            return getConditionalStyles();
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
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__ATTRIBUTE_OWNER_EXPRESSION:
            setAttributeOwnerExpression((String) newValue);
            return;
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__ATTRIBUTE_NAME_EXPRESSION:
            setAttributeNameExpression((String) newValue);
            return;
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__INITIAL_OPERATION:
            setInitialOperation((InitialOperation) newValue);
            return;
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__STYLE:
            setStyle((MultiTextWidgetStyle) newValue);
            return;
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__CONDITIONAL_STYLES:
            getConditionalStyles().clear();
            getConditionalStyles().addAll((Collection<? extends MultiTextWidgetConditionalStyle>) newValue);
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
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__ATTRIBUTE_OWNER_EXPRESSION:
            setAttributeOwnerExpression(MultiTextDescriptionImpl.ATTRIBUTE_OWNER_EXPRESSION_EDEFAULT);
            return;
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__ATTRIBUTE_NAME_EXPRESSION:
            setAttributeNameExpression(MultiTextDescriptionImpl.ATTRIBUTE_NAME_EXPRESSION_EDEFAULT);
            return;
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__INITIAL_OPERATION:
            setInitialOperation((InitialOperation) null);
            return;
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__STYLE:
            setStyle((MultiTextWidgetStyle) null);
            return;
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__CONDITIONAL_STYLES:
            getConditionalStyles().clear();
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
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__ATTRIBUTE_OWNER_EXPRESSION:
            return MultiTextDescriptionImpl.ATTRIBUTE_OWNER_EXPRESSION_EDEFAULT == null ? attributeOwnerExpression != null
                    : !MultiTextDescriptionImpl.ATTRIBUTE_OWNER_EXPRESSION_EDEFAULT.equals(attributeOwnerExpression);
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__ATTRIBUTE_NAME_EXPRESSION:
            return MultiTextDescriptionImpl.ATTRIBUTE_NAME_EXPRESSION_EDEFAULT == null ? attributeNameExpression != null
                    : !MultiTextDescriptionImpl.ATTRIBUTE_NAME_EXPRESSION_EDEFAULT.equals(attributeNameExpression);
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__INITIAL_OPERATION:
            return initialOperation != null;
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__STYLE:
            return style != null;
        case PropertiesPackage.MULTI_TEXT_DESCRIPTION__CONDITIONAL_STYLES:
            return conditionalStyles != null && !conditionalStyles.isEmpty();
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
        result.append(" (attributeOwnerExpression: "); //$NON-NLS-1$
        result.append(attributeOwnerExpression);
        result.append(", attributeNameExpression: "); //$NON-NLS-1$
        result.append(attributeNameExpression);
        result.append(')');
        return result.toString();
    }

} // MultiTextDescriptionImpl
