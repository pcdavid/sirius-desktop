/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.properties.core.internal.validation.description.constraints;

import org.eclipse.sirius.ext.base.I18N;
import org.eclipse.sirius.ext.base.I18N.TranslatableMessage;
import org.eclipse.sirius.properties.core.internal.SiriusPropertiesCorePlugin;

/**
 * Utility class used for the internationalization.
 * 
 * @author mbats
 */
public final class Messages {

    static {
        I18N.initializeMessages(Messages.class, SiriusPropertiesCorePlugin.INSTANCE);
    }

    // CHECKSTYLE:OFF

    @TranslatableMessage
    public static String PropertiesConstraint_InitialOperationPredicateFailure;

    @TranslatableMessage
    public static String PropertiesConstraint_IfsPredicateFailure;

    @TranslatableMessage
    public static String PropertiesConstraint_IteratorPredicateFailure;

    @TranslatableMessage
    public static String PropertiesConstraint_IterableExpressionPredicateFailure;

    @TranslatableMessage
    public static String PropertiesConstraint_WidgetPredicateFailure;

    @TranslatableMessage
    public static String PropertiesConstraint_PredicateExpressionPredicateFailure;

    // CHECKSTYLE:ON

    private Messages() {
        // Prevents instantiation.
    }
}
