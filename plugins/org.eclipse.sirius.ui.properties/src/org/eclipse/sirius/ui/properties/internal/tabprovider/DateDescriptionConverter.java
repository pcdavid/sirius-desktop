/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ui.properties.internal.tabprovider;

import java.text.MessageFormat;
import java.util.Map;

import org.eclipse.eef.EEFDateConditionalStyle;
import org.eclipse.eef.EEFDateDescription;
import org.eclipse.eef.EEFDateStyle;
import org.eclipse.eef.EEF_DATE_DISPLAY;
import org.eclipse.eef.EefFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.properties.DateDescription;
import org.eclipse.sirius.properties.TextDescription;
import org.eclipse.sirius.ui.properties.api.AbstractDescriptionConverter;
import org.eclipse.sirius.ui.properties.api.DescriptionCache;
import org.eclipse.sirius.ui.properties.internal.Messages;
import org.eclipse.sirius.viewpoint.description.tool.InitialOperation;

/**
 * This class is used to convert the description of the date widgets.
 * 
 * @author arichard
 */
public class DateDescriptionConverter extends AbstractDescriptionConverter {

    @Override
    public boolean canHandle(EObject description) {
        return description instanceof TextDescription;
    }

    @Override
    public EObject convert(EObject description, Map<String, Object> parameters, DescriptionCache cache) {
        if (description instanceof DateDescription) {
            DateDescription dateDescription = (DateDescription) description;

            EEFDateDescription eefDateDescription = EefFactory.eINSTANCE.createEEFDateDescription();
            eefDateDescription.setIdentifier(dateDescription.getIdentifier());
            eefDateDescription.setHelpExpression(dateDescription.getHelpExpression());
            eefDateDescription.setLabelExpression(dateDescription.getLabelExpression());
            eefDateDescription.setIsEnabledExpression(dateDescription.getIsEnabledExpression());

            eefDateDescription.setValueExpression(dateDescription.getValueExpression());
            eefDateDescription.setDisplay(EEF_DATE_DISPLAY.get(dateDescription.getDisplay().getValue()));
            
            InitialOperation initialOperation = dateDescription.getInitialOperation();
            eefDateDescription.setEditExpression(this.getExpressionForOperation(initialOperation));

            cache.put(description, eefDateDescription);

            eefDateDescription.setStyle(this.convertEObject(dateDescription.getStyle(), parameters, cache, EEFDateStyle.class));
            eefDateDescription.getConditionalStyles().addAll(this.convertCollection(dateDescription.getConditionalStyles(), parameters, cache, EEFDateConditionalStyle.class));

            return eefDateDescription;
        } else {
            throw new IllegalArgumentException(MessageFormat.format(Messages.IDescriptionConverter_InvalidDescriptionType, description.getClass().getName(), DateDescription.class.getName()));
        }
    }
}
