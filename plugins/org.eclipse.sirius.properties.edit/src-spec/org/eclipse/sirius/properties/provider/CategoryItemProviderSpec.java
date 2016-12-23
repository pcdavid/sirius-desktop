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
import org.eclipse.sirius.properties.GroupDescription;
import org.eclipse.sirius.properties.PageDescription;
import org.eclipse.sirius.properties.PropertiesFactory;
import org.eclipse.sirius.properties.PropertiesPackage;

/**
 * Subclass used to not have to modify the generated code.
 *
 * @author sbegaudeau
 */
public class CategoryItemProviderSpec extends CategoryItemProvider {

    /**
     * The constructor.
     *
     * @param adapterFactory
     *            The adapter factory
     */
    public CategoryItemProviderSpec(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    @Override
    public String getText(Object object) {
        String label = ((Category) object).getIdentifier();
        return label == null || label.length() == 0 ? getString("_UI_Category_type") : //$NON-NLS-1$
                label;
    }

    @Override
    protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
        PageDescription page = PropertiesFactory.eINSTANCE.createPageDescription();
        page.setLabelExpression("Page"); //$NON-NLS-1$
        page.setSemanticCandidateExpression(ViewExtensionDescriptionItemProvider.DEFAULT_SEMANTIC_CANDIDATES_EXPRESSION);
        newChildDescriptors.add(createChildParameter(PropertiesPackage.Literals.CATEGORY__PAGES, page));

        GroupDescription group = PropertiesFactory.eINSTANCE.createGroupDescription();
        group.setLabelExpression("Group"); //$NON-NLS-1$
        group.setSemanticCandidateExpression(ViewExtensionDescriptionItemProvider.DEFAULT_SEMANTIC_CANDIDATES_EXPRESSION);
        newChildDescriptors.add(createChildParameter(PropertiesPackage.Literals.CATEGORY__GROUPS, group));
    }
}
