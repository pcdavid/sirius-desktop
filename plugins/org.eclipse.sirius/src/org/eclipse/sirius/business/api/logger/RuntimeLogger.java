/*******************************************************************************
 * Copyright (c) 2009, 2021 THALES GLOBAL SERVICES.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.api.logger;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Interface that log runtime errors.
 * 
 * @author smonnier
 * 
 */
public interface RuntimeLogger {

    /**
     * Add error entry to the logger.
     * 
     * @param odesignObject
     *            EObject where the error come from
     * @param feature
     *            element of the EObject where the error come from. May be <code>null</code>.
     * @param message
     *            error message we want to display
     */
    default void error(EObject odesignObject, EStructuralFeature feature, String message) {

    }

    /**
     * Add error entry to the logger.
     * 
     * @param odesignObject
     *            EObject where the error come from
     * @param feature
     *            element of the EObject where the error come from. May be <code>null</code>.
     * @param exception
     *            a low-level exception, or <code>null</code> if not applicable
     */
    default void error(EObject odesignObject, EStructuralFeature feature, Throwable exception) {

    }

    /**
     * Add error entry to the logger.
     * 
     * @param odesignObject
     *            EObject where the warning come from
     * @param feature
     *            element of the EObject where the warning come from. May be <code>null</code>.
     * @param exception
     *            a low-level exception, or <code>null</code> if not applicable
     */
    default void warning(EObject odesignObject, EStructuralFeature feature, Throwable exception) {

    }

    /**
     * Add error entry to the logger.
     * 
     * @param odesignObject
     *            EObject where the warning come from
     * @param feature
     *            element of the EObject where the warning come from. May be <code>null</code>.
     * @param message
     *            error message we want to display
     */
    default void warning(EObject odesignObject, EStructuralFeature feature, String message) {

    }

    /**
     * Add error entry to the logger.
     * 
     * @param odesignObject
     *            EObject where the info come from
     * @param feature
     *            element of the EObject where the info come from. May be <code>null</code>.
     * @param message
     *            error message we want to display
     */
    default void info(EObject odesignObject, EStructuralFeature feature, String message) {

    }

    /**
     * Add error entry to the logger.
     * 
     * @param odesignObject
     *            EObject where the info come from
     * @param feature
     *            element of the EObject where the info come from. May be <code>null</code>.
     * @param exception
     *            a low-level exception, or <code>null</code> if not applicable
     */
    default void info(EObject odesignObject, EStructuralFeature feature, Throwable exception) {

    }

    /**
     * Clears all logged entries.
     */
    default void clearAll() {

    }

    /**
     * Clears all logged entries for the EObject.
     * 
     * @param eObject
     *            EObject we want to clearAll logged entries
     */
    default void clear(EObject eObject) {

    }
}
