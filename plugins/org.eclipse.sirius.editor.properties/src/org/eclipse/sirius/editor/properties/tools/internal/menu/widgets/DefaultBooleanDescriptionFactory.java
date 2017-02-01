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
import org.eclipse.sirius.properties.CheckboxDescription;
import org.eclipse.sirius.properties.CheckboxWidgetConditionalStyle;
import org.eclipse.sirius.properties.CheckboxWidgetStyle;
import org.eclipse.sirius.properties.PropertiesFactory;
import org.eclipse.sirius.properties.core.internal.EditSupportSpec;
import org.eclipse.sirius.viewpoint.FontFormat;
import org.eclipse.sirius.viewpoint.description.tool.ChangeContext;
import org.eclipse.sirius.viewpoint.description.tool.InitialOperation;
import org.eclipse.sirius.viewpoint.description.tool.ToolFactory;

/**
 * The factory used to handle booleans.
 * 
 * @author sbegaudeau
 */
@SuppressWarnings({ "checkstyle:multiplestringliterals" })
public class DefaultBooleanDescriptionFactory implements IDefaultWidgetDescriptionFactory {

    @Override
    public boolean canCreate(EClass domainClass, EStructuralFeature eStructuralFeature) {
        EObject eObject = domainClass.getEPackage().getEFactoryInstance().create(domainClass);
        EditSupportSpec editSupportSpec = new EditSupportSpec(null, eObject);
        return editSupportSpec.needsCheckboxWidget(eStructuralFeature);
    }

    @Override
    public DefaultWidgetDescription create(EClass domainClass, EStructuralFeature eStructuralFeature) {
        CheckboxDescription description = PropertiesFactory.eINSTANCE.createCheckboxDescription();

        description.setIsEnabledExpression("aql:self.eClass().getEStructuralFeature('" + eStructuralFeature.getName() + "').changeable");
        description.setHelpExpression("aql:input.emfEditServices(self).getDescription(self.eClass().getEStructuralFeature('" + eStructuralFeature.getName() + "'))");
        description.setLabelExpression("aql:input.emfEditServices(self).getText(self.eClass().getEStructuralFeature('" + eStructuralFeature.getName() + "'))");
        description.setValueExpression("aql:self." + eStructuralFeature.getName());

        InitialOperation initialOperation = ToolFactory.eINSTANCE.createInitialOperation();
        ChangeContext changeContext = ToolFactory.eINSTANCE.createChangeContext();
        changeContext.setBrowseExpression("aql:input.emfEditServices(self).setValue(self.eClass().getEStructuralFeature('" + eStructuralFeature.getName() + "'), newValue)");
        initialOperation.setFirstModelOperations(changeContext);

        description.setInitialOperation(initialOperation);
        if (eStructuralFeature.getLowerBound() == 1) {
            CheckboxWidgetConditionalStyle conditionalStyle = PropertiesFactory.eINSTANCE.createCheckboxWidgetConditionalStyle();
            conditionalStyle.setPreconditionExpression("aql:self.eClass().getEStructuralFeature('" + eStructuralFeature.getName() + "').lowerBound = 1");
            CheckboxWidgetStyle widgetStyle = PropertiesFactory.eINSTANCE.createCheckboxWidgetStyle();
            widgetStyle.getLabelFontFormat().add(FontFormat.BOLD_LITERAL);
            conditionalStyle.setStyle(widgetStyle);
            description.getConditionalStyles().add(conditionalStyle);
        }

        String label = MessageFormat.format(Messages.DefaultBooleanDescriptionFactory_widgetLabel, eStructuralFeature.eClass().getName(), eStructuralFeature.getName());
        return new DefaultWidgetDescription(label, description);
    }

}
