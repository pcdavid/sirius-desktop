/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.properties.editor.properties.sections.properties.groupdescription;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.sirius.editor.editorPlugin.SiriusEditor;
import org.eclipse.sirius.editor.properties.sections.common.AbstractTextPropertySection;
import org.eclipse.sirius.editor.tools.api.assist.TypeContentProposalProvider;
import org.eclipse.sirius.properties.PropertiesPackage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * A section for the domainClass property of a GroupDescription object.
 * 
 * @author sbegaudeau
 */
public class GroupDescriptionDomainClassPropertySectionSpec extends AbstractTextPropertySection {

    /** Help control of the section. */
    protected CLabel help;

    @Override
    public void refresh() {
        super.refresh();

        final String tooltip = getToolTipText();
        if (tooltip != null && help != null) {
            help.setToolTipText(getToolTipText());
        }
    }

    @Override
    protected String getDefaultLabelText() {
        return "DomainClass"; //$NON-NLS-1$
    }

    @Override
    protected String getLabelText() {
        String labelText;
        labelText = super.getLabelText() + ":"; //$NON-NLS-1$
        return labelText;
    }

    @Override
    public EAttribute getFeature() {
        return PropertiesPackage.eINSTANCE.getAbstractGroupDescription_DomainClass();
    }

    @Override
    protected Object getFeatureValue(String newText) {
        return newText;
    }

    @Override
    protected boolean isEqual(String newText) {
        return getFeatureAsText().equals(newText);
    }

    @Override
    public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
        super.createControls(parent, tabbedPropertySheetPage);
        /*
         * We set the color as it's a TypeName
         */
        text.setBackground(SiriusEditor.getColorRegistry().get("green"));

        text.setToolTipText(getToolTipText());

        help = getWidgetFactory().createCLabel(composite, "");
        FormData data = new FormData();
        data.top = new FormAttachment(text, 0, SWT.TOP);
        data.left = new FormAttachment(nameLabel);
        help.setLayoutData(data);
        help.setImage(getHelpIcon());
        help.setToolTipText(getToolTipText());
        nameLabel.setFont(SiriusEditor.getFontRegistry().get("required"));

        TypeContentProposalProvider.bindCompletionProcessor(this, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPropertyDescription() {
        return "The type of the root diagram element. For instance you may want to create an UML2 Class diagram,\\n then the root domain class will probably be 'Package'. On the other side if you want a Class Diagram\\n displaying the whole model, then the root domain class is 'Model' in UML2.";
    }

}
