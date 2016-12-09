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

import org.eclipse.eef.EEFMultiTextConditionalStyle;
import org.eclipse.eef.EEFMultiTextDescription;
import org.eclipse.eef.EEFMultiTextStyle;
import org.eclipse.eef.EefFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.properties.MultiTextDescription;
import org.eclipse.sirius.ui.properties.api.AbstractDescriptionConverter;
import org.eclipse.sirius.ui.properties.api.DescriptionCache;
import org.eclipse.sirius.ui.properties.internal.Messages;
import org.eclipse.sirius.viewpoint.description.tool.InitialOperation;

/**
 * This class is used to convert the description of the multi text widgets.
 * 
 * @author arichard
 */
public class MultiTextDescriptionConverter extends AbstractDescriptionConverter {

    @Override
    public boolean canHandle(EObject description) {
        return description instanceof MultiTextDescription;
    }

    @Override
    public EObject convert(EObject description, Map<String, Object> parameters, DescriptionCache cache) {
        if (description instanceof MultiTextDescription) {
            MultiTextDescription multiTextDescription = (MultiTextDescription) description;

            EEFMultiTextDescription eefMultiTextDescription = EefFactory.eINSTANCE.createEEFMultiTextDescription();
            eefMultiTextDescription.setIdentifier(multiTextDescription.getIdentifier());
            eefMultiTextDescription.setHelpExpression(multiTextDescription.getHelpExpression());
            eefMultiTextDescription.setLabelExpression(multiTextDescription.getLabelExpression());
            eefMultiTextDescription.setIsEnabledExpression(multiTextDescription.getIsEnabledExpression());

            eefMultiTextDescription.setAttributeOwnerExpression(multiTextDescription.getAttributeOwnerExpression());
            eefMultiTextDescription.setAttributeNameExpression(multiTextDescription.getAttributeNameExpression());

            InitialOperation initialOperation = multiTextDescription.getInitialOperation();
            eefMultiTextDescription.setEditExpression(this.getExpressionForOperation(initialOperation));
            
            cache.put(description, eefMultiTextDescription);

            eefMultiTextDescription.setStyle(this.convertEObject(multiTextDescription.getStyle(), parameters, cache, EEFMultiTextStyle.class));
            eefMultiTextDescription.getConditionalStyles().addAll(this.convertCollection(multiTextDescription.getConditionalStyles(), parameters, cache, EEFMultiTextConditionalStyle.class));

            return eefMultiTextDescription;
        } else {
            throw new IllegalArgumentException(MessageFormat.format(Messages.IDescriptionConverter_InvalidDescriptionType, description.getClass().getName(), MultiTextDescription.class.getName()));
        }
    }
}
