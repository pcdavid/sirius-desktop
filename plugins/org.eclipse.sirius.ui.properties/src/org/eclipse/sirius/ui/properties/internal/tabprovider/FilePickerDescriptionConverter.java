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

import org.eclipse.eef.EEFFilePickerConditionalStyle;
import org.eclipse.eef.EEFFilePickerDescription;
import org.eclipse.eef.EEFFilePickerStyle;
import org.eclipse.eef.EefFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.properties.FilePickerDescription;
import org.eclipse.sirius.ui.properties.api.AbstractDescriptionConverter;
import org.eclipse.sirius.ui.properties.api.DescriptionCache;
import org.eclipse.sirius.ui.properties.internal.Messages;
import org.eclipse.sirius.viewpoint.description.tool.InitialOperation;

/**
 * This class is used to convert the description of the file picker widgets.
 * 
 * @author arichard
 */
public class FilePickerDescriptionConverter extends AbstractDescriptionConverter {

    @Override
    public boolean canHandle(EObject description) {
        return description instanceof FilePickerDescription;
    }

    @Override
    public EObject convert(EObject description, Map<String, Object> parameters, DescriptionCache cache) {
        if (description instanceof FilePickerDescription) {
            FilePickerDescription filePickerDescription = (FilePickerDescription) description;

            EEFFilePickerDescription eefFilePickerDescription = EefFactory.eINSTANCE.createEEFFilePickerDescription();
            eefFilePickerDescription.setIdentifier(filePickerDescription.getIdentifier());
            eefFilePickerDescription.setHelpExpression(filePickerDescription.getHelpExpression());
            eefFilePickerDescription.setLabelExpression(filePickerDescription.getLabelExpression());
            eefFilePickerDescription.setIsEnabledExpression(filePickerDescription.getIsEnabledExpression());

            eefFilePickerDescription.setPathExpression(filePickerDescription.getPathExpression());

            InitialOperation initialOperation = filePickerDescription.getInitialOperation();
            eefFilePickerDescription.setEditExpression(this.getExpressionForOperation(initialOperation));

            cache.put(description, eefFilePickerDescription);

            eefFilePickerDescription.setStyle(this.convertEObject(filePickerDescription.getStyle(), parameters, cache, EEFFilePickerStyle.class));
            eefFilePickerDescription.getConditionalStyles().addAll(this.convertCollection(filePickerDescription.getConditionalStyles(), parameters, cache, EEFFilePickerConditionalStyle.class));

            return eefFilePickerDescription;
        } else {
            throw new IllegalArgumentException(MessageFormat.format(Messages.IDescriptionConverter_InvalidDescriptionType, description.getClass().getName(), FilePickerDescription.class.getName()));
        }
    }
}
