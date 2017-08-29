/**
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *
 */
package org.eclipse.sirius.properties.provider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.command.CommandParameter;

/**
 * Subclass used to not have to modify the generated code.
 *
 * @author arichard
 */
public class ToolbarActionItemProviderSpec extends ToolbarActionItemProvider {

    /**
     * The constructor.
     *
     * @param adapterFactory
     *            The adapter factory
     */
    public ToolbarActionItemProviderSpec(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    @Override
    protected CommandParameter createChildParameter(Object feature, Object child) {
        PropertiesItemProviderAdapterFactory.addNoopNavigationOperations(child);
        return super.createChildParameter(feature, child);
    }
}
