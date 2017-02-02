/*******************************************************************************
 * Copyright (c) 2017 Obeo.
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

/**
 * A section for the label property of a ViewExtensionDescription object.
 * 
 * @author sbegaudeau
 */
public class ViewExtensionDescriptionLabelPropertySectionSpec extends IdentifiedElementLabelPropertySection {
    @Override
    public EAttribute getFeature() {
        return PropertiesPackage.eINSTANCE.getViewExtensionDescription_Label();
    }

}
