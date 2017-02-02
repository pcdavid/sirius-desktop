/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.properties.editor.properties.sections.properties.pagedescription;

import org.eclipse.sirius.editor.editorPlugin.SiriusEditor;
import org.eclipse.sirius.editor.tools.api.assist.TypeContentProposalProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * A section for the labelExpression property of a PageDescription object.
 * 
 * @author sbegaudeau
 */
public class PageDescriptionLabelExpressionPropertySectionSpec extends PageDescriptionLabelExpressionPropertySection {

    @Override
    protected String getLabelText() {
        String labelText;
        labelText = super.getLabelText() + "*:"; //$NON-NLS-1$
        return labelText;
    }

    @Override
    public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
        super.createControls(parent, tabbedPropertySheetPage);
        /*
         * We set the color as it's a InterpretedExpression
         */
        text.setBackground(SiriusEditor.getColorRegistry().get("yellow"));

        nameLabel.setFont(SiriusEditor.getFontRegistry().get("required"));

        TypeContentProposalProvider.bindPluginsCompletionProcessors(this, text);
    }
}
