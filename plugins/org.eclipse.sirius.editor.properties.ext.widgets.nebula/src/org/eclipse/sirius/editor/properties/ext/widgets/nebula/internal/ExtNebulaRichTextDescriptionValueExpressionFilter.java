/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.editor.properties.ext.widgets.nebula.internal;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sirius.editor.properties.filters.common.ViewpointPropertyFilter;
import org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.PropertiesExtWidgetsNebulaPackage;

/**
 * The filter used for the property section for the expression used to compute the value.
 * 
 * @author arichard
 */
public class ExtNebulaRichTextDescriptionValueExpressionFilter extends ViewpointPropertyFilter {

    @Override
    protected EStructuralFeature getFeature() {
        return PropertiesExtWidgetsNebulaPackage.eINSTANCE.getExtNebulaRichTextDescription_ValueExpression();
    }

    @Override
    protected boolean isRightInputType(Object arg0) {
        return arg0 instanceof org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription;
    }

}
