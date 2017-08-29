/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ui.properties.ext.widgets.nebula.internal;

import org.eclipse.eef.common.api.utils.I18N;
import org.eclipse.eef.common.api.utils.I18N.TranslatableMessage;

/**
 * Utility class used for the internationalization.
 * 
 * @author arichard
 */
public class Messages {

	static {
        I18N.initializeMessages(Messages.class, SiriusUiPropertiesExtWidgetsNebulaPlugin.INSTANCE);
    }

    // CHECKSTYLE:OFF
    @TranslatableMessage
    public static String IDescriptionConverter_InvalidDescriptionType;
    
 // CHECKSTYLE:ON

    private Messages() {
        // Prevents instanciation.
    }
}
