/**
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 */
package org.eclipse.sirius.ui.properties.ext.widgets.nebula.internal;

import org.eclipse.eef.ext.widgets.nebula.eefextwidgetsnebula.EefExtWidgetsNebulaFactory;
import org.eclipse.eef.ext.widgets.nebula.eefextwidgetsnebula.EefExtWidgetsNebulaPackage;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextWidgetStyle;
import org.eclipse.sirius.ui.properties.api.DefaultStyleDescriptionConverter;

/**
 * Converter for the style of the Nebula RichText widget.
 * 
 * @author arichard
 */
public class ExtNebulaRichTextDescriptionStyleConverter extends DefaultStyleDescriptionConverter<ExtNebulaRichTextWidgetStyle> {

    /**
     * The constructor.
     */
    public ExtNebulaRichTextDescriptionStyleConverter() {
        super(ExtNebulaRichTextWidgetStyle.class, EefExtWidgetsNebulaPackage.Literals.EEF_EXT_NEBULA_RICH_TEXT_WIDGET_STYLE);
    }

    @Override
    protected EFactory getEFactory() {
        return EefExtWidgetsNebulaFactory.eINSTANCE;
    }

}
