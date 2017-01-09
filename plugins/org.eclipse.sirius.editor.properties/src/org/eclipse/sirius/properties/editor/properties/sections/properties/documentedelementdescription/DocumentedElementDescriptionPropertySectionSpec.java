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
package org.eclipse.sirius.properties.editor.properties.sections.properties.documentedelementdescription;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.sirius.editor.properties.sections.description.documentedelement.DocumentedElementDocumentationPropertySection;
import org.eclipse.sirius.properties.PropertiesPackage;

/**
 * Use proper section for eef documented elements.
 * 
 * @author arichard
 */
public class DocumentedElementDescriptionPropertySectionSpec extends DocumentedElementDocumentationPropertySection {

    @Override
    public EAttribute getFeature() {
        return PropertiesPackage.eINSTANCE.getDocumentedElementDescription_Documentation();
    }
}
