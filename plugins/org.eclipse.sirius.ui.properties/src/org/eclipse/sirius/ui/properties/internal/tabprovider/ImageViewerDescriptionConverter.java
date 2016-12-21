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

import org.eclipse.eef.EEFImageViewerConditionalStyle;
import org.eclipse.eef.EEFImageViewerDescription;
import org.eclipse.eef.EEFImageViewerStyle;
import org.eclipse.eef.EefFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.properties.ImagePickerDescription;
import org.eclipse.sirius.properties.ImageViewerDescription;
import org.eclipse.sirius.ui.properties.api.AbstractDescriptionConverter;
import org.eclipse.sirius.ui.properties.api.DescriptionCache;
import org.eclipse.sirius.ui.properties.internal.Messages;
import org.eclipse.sirius.viewpoint.description.tool.InitialOperation;

/**
 * This class is used to convert the description of the image viewer widgets.
 * 
 * @author arichard
 */
public class ImageViewerDescriptionConverter extends AbstractDescriptionConverter {

    @Override
    public boolean canHandle(EObject description) {
        return description instanceof ImageViewerDescription;
    }

    @Override
    public EObject convert(EObject description, Map<String, Object> parameters, DescriptionCache cache) {
        if (description instanceof ImageViewerDescription) {
            ImageViewerDescription imageViewerDescription = (ImageViewerDescription) description;

            EEFImageViewerDescription eefImageViewerDescription = EefFactory.eINSTANCE.createEEFImageViewerDescription();
            eefImageViewerDescription.setIdentifier(imageViewerDescription.getIdentifier());
            eefImageViewerDescription.setHelpExpression(imageViewerDescription.getHelpExpression());
            eefImageViewerDescription.setLabelExpression(imageViewerDescription.getLabelExpression());
            eefImageViewerDescription.setIsEnabledExpression(imageViewerDescription.getIsEnabledExpression());

            eefImageViewerDescription.setPathExpression(imageViewerDescription.getPathExpression());

            if (description instanceof ImagePickerDescription) {
                eefImageViewerDescription.setWithPicker(true);
            }

            InitialOperation initialOperation = imageViewerDescription.getInitialOperation();
            eefImageViewerDescription.setEditExpression(this.getExpressionForOperation(initialOperation));

            cache.put(description, eefImageViewerDescription);

            eefImageViewerDescription.setStyle(this.convertEObject(imageViewerDescription.getStyle(), parameters, cache, EEFImageViewerStyle.class));
            eefImageViewerDescription.getConditionalStyles().addAll(this.convertCollection(imageViewerDescription.getConditionalStyles(), parameters, cache, EEFImageViewerConditionalStyle.class));

            return eefImageViewerDescription;
        } else {
            throw new IllegalArgumentException(MessageFormat.format(Messages.IDescriptionConverter_InvalidDescriptionType, description.getClass().getName(), ImageViewerDescription.class.getName()));
        }
    }
}
