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
package org.eclipse.sirius.editor.properties.tools.internal.menu.widgets;

import java.text.MessageFormat;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sirius.editor.properties.api.DefaultWidgetDescription;
import org.eclipse.sirius.editor.properties.api.IDefaultWidgetDescriptionFactory;
import org.eclipse.sirius.editor.properties.internal.Messages;
import org.eclipse.sirius.properties.PropertiesFactory;
import org.eclipse.sirius.properties.TextAreaDescription;
import org.eclipse.sirius.properties.TextWidgetConditionalStyle;
import org.eclipse.sirius.properties.TextWidgetStyle;
import org.eclipse.sirius.properties.core.internal.EditSupportSpec;
import org.eclipse.sirius.viewpoint.FontFormat;
import org.eclipse.sirius.viewpoint.description.tool.ChangeContext;
import org.eclipse.sirius.viewpoint.description.tool.InitialOperation;
import org.eclipse.sirius.viewpoint.description.tool.ToolFactory;

/**
 * The factory used to create the default multiline text widget.
 * 
 * @author sbegaudeau
 */
@SuppressWarnings({ "checkstyle:multiplestringliterals" })
public class DefaultMultilineTextDescriptionFactory implements IDefaultWidgetDescriptionFactory {

    @Override
    public boolean canCreate(EClass domainClass, EStructuralFeature eStructuralFeature) {
        EObject eObject = domainClass.getEPackage().getEFactoryInstance().create(domainClass);
        EditSupportSpec editSupportSpec = new EditSupportSpec(null, eObject);
        return editSupportSpec.needsTextWidget(eStructuralFeature) && editSupportSpec.isMultiline(eStructuralFeature);
    }

    @Override
    public DefaultWidgetDescription create(EClass domainClass, EStructuralFeature eStructuralFeature) {
        TextAreaDescription description = PropertiesFactory.eINSTANCE.createTextAreaDescription();

        description.setIsEnabledExpression("aql:self.eClass().getEStructuralFeature('" + eStructuralFeature.getName() + "').changeable");
        description.setHelpExpression("aql:input.emfEditServices(self).getDescription(self.eClass().getEStructuralFeature('" + eStructuralFeature.getName() + "'))");
        description.setLabelExpression("aql:input.emfEditServices(self).getText(self.eClass().getEStructuralFeature('" + eStructuralFeature.getName() + "')) + ':'");
        description.setValueExpression("aql:self." + eStructuralFeature.getName());

        InitialOperation initialOperation = ToolFactory.eINSTANCE.createInitialOperation();
        ChangeContext changeContext = ToolFactory.eINSTANCE.createChangeContext();
        changeContext.setBrowseExpression("aql:input.emfEditServices(self).setValue(self.eClass().getEStructuralFeature('" + eStructuralFeature.getName() + "'), newValue)");
        initialOperation.setFirstModelOperations(changeContext);

        description.setInitialOperation(initialOperation);
        if (eStructuralFeature.getLowerBound() == 1) {
            TextWidgetConditionalStyle conditionalStyle = PropertiesFactory.eINSTANCE.createTextWidgetConditionalStyle();
            conditionalStyle.setPreconditionExpression("aql:self.eClass().getEStructuralFeature('" + eStructuralFeature.getName() + "').lowerBound = 1");
            TextWidgetStyle widgetStyle = PropertiesFactory.eINSTANCE.createTextWidgetStyle();
            widgetStyle.getLabelFontFormat().add(FontFormat.BOLD_LITERAL);
            conditionalStyle.setStyle(widgetStyle);
            description.getConditionalStyles().add(conditionalStyle);
        }

        String label = MessageFormat.format(Messages.DefaultMultilineTextDescriptionFactory_widgetLabel, eStructuralFeature.eClass().getName(), eStructuralFeature.getName());
        return new DefaultWidgetDescription(label, description);
    }

}
