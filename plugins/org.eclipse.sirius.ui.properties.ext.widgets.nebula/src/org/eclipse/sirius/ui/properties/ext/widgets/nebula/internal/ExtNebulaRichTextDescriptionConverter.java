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

import java.text.MessageFormat;
import java.util.Map;

import org.eclipse.eef.ext.widgets.nebula.eefextwidgetsnebula.EEFExtNebulaRichTextDescription;
import org.eclipse.eef.ext.widgets.nebula.eefextwidgetsnebula.EEFExtNebulaRichTextWidgetStyle;
import org.eclipse.eef.ext.widgets.nebula.eefextwidgetsnebula.EefExtWidgetsNebulaFactory;
import org.eclipse.eef.ext.widgets.nebula.eefextwidgetsnebula.EefExtWidgetsNebulaPackage;
import org.eclipse.eef.ext.widgets.nebula.eefextwidgetsnebula.impl.EEFExtNebulaRichTextConditionalStyleImpl;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.properties.TextDescription;
import org.eclipse.sirius.properties.ext.widgets.nebula.propertiesextwidgetsnebula.ExtNebulaRichTextDescription;
import org.eclipse.sirius.ui.properties.api.DefaultDescriptionConverter;
import org.eclipse.sirius.ui.properties.api.DescriptionCache;
import org.eclipse.sirius.viewpoint.description.tool.InitialOperation;

/**
 * This class is used to convert the Sirius Extension Nebula RichText
 * description into the EEF one.
 * 
 * @author arichard
 */
public class ExtNebulaRichTextDescriptionConverter extends DefaultDescriptionConverter<ExtNebulaRichTextDescription> {

	/**
	 * The constructor.
	 */
	public ExtNebulaRichTextDescriptionConverter() {
		super(ExtNebulaRichTextDescription.class,
				EefExtWidgetsNebulaPackage.Literals.EEF_EXT_NEBULA_RICH_TEXT_DESCRIPTION);
	}

	@Override
	protected EFactory getEFactory() {
		return EefExtWidgetsNebulaFactory.eINSTANCE;
	}

	@Override
	public EObject convert(EObject description, Map<String, Object> parameters, DescriptionCache cache) {
		if (description instanceof ExtNebulaRichTextDescription) {
			ExtNebulaRichTextDescription richTextDescription = (ExtNebulaRichTextDescription) description;

			EEFExtNebulaRichTextDescription eefRichTextDescription = EefExtWidgetsNebulaFactory.eINSTANCE.createEEFExtNebulaRichTextDescription();
			eefRichTextDescription.setIdentifier(richTextDescription.getIdentifier());
			eefRichTextDescription.setHelpExpression(richTextDescription.getHelpExpression());
			eefRichTextDescription.setLabelExpression(richTextDescription.getLabelExpression());
			eefRichTextDescription.setIsEnabledExpression(richTextDescription.getIsEnabledExpression());

			eefRichTextDescription.setValueExpression(richTextDescription.getValueExpression());

			InitialOperation initialOperation = richTextDescription.getInitialOperation();
			eefRichTextDescription.setEditExpression(this.getExpressionForOperation(initialOperation));

			cache.put(description, eefRichTextDescription);

			eefRichTextDescription
					.setStyle(this.convertEObject(richTextDescription.getStyle(), parameters, cache, EEFExtNebulaRichTextWidgetStyle.class));
			eefRichTextDescription.getConditionalStyles().addAll(this.convertCollection(
					richTextDescription.getConditionalStyles(), parameters, cache, EEFExtNebulaRichTextConditionalStyleImpl.class));

			return eefRichTextDescription;
		} else {
			throw new IllegalArgumentException(
					MessageFormat.format(Messages.IDescriptionConverter_InvalidDescriptionType,
							description.getClass().getName(), TextDescription.class.getName()));
		}
	}
}
