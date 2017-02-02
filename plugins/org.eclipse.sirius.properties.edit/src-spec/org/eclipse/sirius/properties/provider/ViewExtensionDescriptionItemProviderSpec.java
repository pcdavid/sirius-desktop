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
package org.eclipse.sirius.properties.provider;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.sirius.properties.Category;
import org.eclipse.sirius.properties.PropertiesFactory;
import org.eclipse.sirius.properties.PropertiesPackage;
import org.eclipse.sirius.properties.ViewExtensionDescription;

/**
 * Subclass used to not have to modify the generated code.
 *
 * @author sbegaudeau
 */
public class ViewExtensionDescriptionItemProviderSpec extends ViewExtensionDescriptionItemProvider {

    /**
     * The default expression to use as semanticCandidatesExpression for newly
     * created elements.
     */
    public static final String DEFAULT_SEMANTIC_CANDIDATES_EXPRESSION = "var:self"; //$NON-NLS-1$

    /**
     * The constructor.
     *
     * @param adapterFactory
     *            The adapter factory
     */
    public ViewExtensionDescriptionItemProviderSpec(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    @Override
    public String getText(Object object) {
        String label = ((ViewExtensionDescription) object).getIdentifier();
        return label == null || label.length() == 0 ? getString("_UI_ViewExtensionDescription_type") : //$NON-NLS-1$
                label;
    }

    @Override
    protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
        Category category = PropertiesFactory.eINSTANCE.createCategory();
        category.setIdentifier("Category"); //$NON-NLS-1$
        newChildDescriptors.add(createChildParameter(PropertiesPackage.Literals.VIEW_EXTENSION_DESCRIPTION__CATEGORIES, category));
    }
}
