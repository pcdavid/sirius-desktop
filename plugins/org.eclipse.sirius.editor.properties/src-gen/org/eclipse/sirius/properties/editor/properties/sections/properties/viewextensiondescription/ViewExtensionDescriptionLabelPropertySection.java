/*******************************************************************************
 * Copyright (c) 2016, 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.properties.editor.properties.sections.properties.viewextensiondescription;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.sirius.editor.properties.sections.description.identifiedelement.IdentifiedElementLabelPropertySection;
import org.eclipse.sirius.properties.PropertiesPackage;

// End of user code imports

/**
 * A section for the label property of a ViewExtensionDescription object.
 */
public class ViewExtensionDescriptionLabelPropertySection extends IdentifiedElementLabelPropertySection {
    /**
     * @see org.eclipse.sirius.properties.editor.properties.sections.AbstractTextPropertySection#getFeature()
     */
    @Override
    public EAttribute getFeature() {
        return PropertiesPackage.eINSTANCE.getViewExtensionDescription_Label();
    }

}
