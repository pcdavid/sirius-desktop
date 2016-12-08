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
package org.eclipse.sirius.properties;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration
 * '<em><b>Date Style</b></em>', and utility methods for working with them. <!--
 * end-user-doc -->
 *
 * @see org.eclipse.sirius.properties.PropertiesPackage#getDateStyle()
 * @model
 * @generated
 */
public enum DateStyle implements Enumerator {
    /**
     * The '<em><b>CALENDAR</b></em>' literal object. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #CALENDAR_VALUE
     * @generated
     * @ordered
     */
    CALENDAR(0, "CALENDAR", "CALENDAR"), //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * The '<em><b>DATE</b></em>' literal object. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #DATE_VALUE
     * @generated
     * @ordered
     */
    DATE(1, "DATE", "DATE"), //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * The '<em><b>TIME</b></em>' literal object. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #TIME_VALUE
     * @generated
     * @ordered
     */
    TIME(2, "TIME", "TIME"); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * The '<em><b>CALENDAR</b></em>' literal value. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>CALENDAR</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @see #CALENDAR
     * @model
     * @generated
     * @ordered
     */
    public static final int CALENDAR_VALUE = 0;

    /**
     * The '<em><b>DATE</b></em>' literal value. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>DATE</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @see #DATE
     * @model
     * @generated
     * @ordered
     */
    public static final int DATE_VALUE = 1;

    /**
     * The '<em><b>TIME</b></em>' literal value. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>TIME</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @see #TIME
     * @model
     * @generated
     * @ordered
     */
    public static final int TIME_VALUE = 2;

    /**
     * An array of all the '<em><b>Date Style</b></em>' enumerators. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private static final DateStyle[] VALUES_ARRAY = new DateStyle[] { CALENDAR, DATE, TIME, };

    /**
     * A public read-only list of all the '<em><b>Date Style</b></em>'
     * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public static final List<DateStyle> VALUES = Collections.unmodifiableList(Arrays.asList(DateStyle.VALUES_ARRAY));

    /**
     * Returns the '<em><b>Date Style</b></em>' literal with the specified
     * literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param literal
     *            the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static DateStyle get(String literal) {
        for (DateStyle result : DateStyle.VALUES_ARRAY) {
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Date Style</b></em>' literal with the specified name.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param name
     *            the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static DateStyle getByName(String name) {
        for (DateStyle result : DateStyle.VALUES_ARRAY) {
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Date Style</b></em>' literal with the specified
     * integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static DateStyle get(int value) {
        switch (value) {
        case CALENDAR_VALUE:
            return CALENDAR;
        case DATE_VALUE:
            return DATE;
        case TIME_VALUE:
            return TIME;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private final int value;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private final String name;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private final String literal;

    /**
     * Only this class can construct instances. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    private DateStyle(int value, String name, String literal) {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public int getValue() {
        return value;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getLiteral() {
        return literal;
    }

    /**
     * Returns the literal value of the enumerator, which is its string
     * representation. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        return literal;
    }

} // DateStyle
