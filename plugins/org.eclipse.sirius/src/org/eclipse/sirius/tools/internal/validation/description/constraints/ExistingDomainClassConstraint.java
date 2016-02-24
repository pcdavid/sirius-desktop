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
package org.eclipse.sirius.tools.internal.validation.description.constraints;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.sirius.ecore.extender.business.api.accessor.ModelAccessor;
import org.eclipse.sirius.tools.internal.validation.AbstractConstraint;
import org.eclipse.sirius.viewpoint.Messages;
import org.eclipse.sirius.viewpoint.SiriusPlugin;
import org.eclipse.sirius.viewpoint.description.DescriptionPackage;
import org.eclipse.sirius.viewpoint.description.IdentifiedElement;
import org.eclipse.sirius.viewpoint.description.RepresentationDescription;
import org.eclipse.sirius.viewpoint.description.RepresentationExtensionDescription;

import com.google.common.collect.Sets;

/**
 * Constraint ensuring that a DomainClass exists in the declared meta-models of
 * the parent Representation description.
 * 
 * @author <a href="mailto:laurent.fasani@obeo.fr">Laurent Fasani</a>
 */
public class ExistingDomainClassConstraint extends AbstractConstraint {

    private static final Pattern DOMAIN_CLASS_PATTERN = Pattern.compile("\\w+\\.\\w+"); //$NON-NLS-1$

    @Override
    public IStatus validate(final IValidationContext ctx) {
        Collection<IStatus> statuses = Sets.newLinkedHashSet();
        final EObject eObj = ctx.getTarget();

        final EMFEventType eventType = ctx.getEventType();
        // In the case of batch mode.
        if (eventType == EMFEventType.NULL) {
            for (EStructuralFeature feature : eObj.eClass().getEAllStructuralFeatures()) {
                if (feature.getEType().equals(DescriptionPackage.eINSTANCE.getTypeName())) {
                    statuses.add(checkError(ctx, feature, eObj));
                }
            }
        }

        final IStatus returnStatus = getCompositeStatus(ctx, statuses);
        return returnStatus;
    }

    private IStatus checkError(final IValidationContext ctx, final EStructuralFeature domainClassFeature, final EObject eObj) {
        IStatus result = null;

        final Object value = eObj.eGet(domainClassFeature);
        if (value instanceof String) {
            final String classNameString = (String) value;
            Matcher matcher = DOMAIN_CLASS_PATTERN.matcher(classNameString);
            if (matcher.matches()) {
                String[] split = classNameString.split("\\."); //$NON-NLS-1$
                String packageName = split[0];
                String className = split[1];

                // check that prefix is an available package
                boolean packageFound = false;
                IdentifiedElement parentDescription = getRepresentationDescription(eObj);
                Collection<EPackage> availableMetamodels = getAvailableMetamodels(parentDescription);
                for (EPackage ePackage : availableMetamodels) {
                    if (ePackage.getName().equals(packageName)) {
                        packageFound = true;
                        break;
                    }
                }
                if (packageFound) {
                    final ModelAccessor extPackage = SiriusPlugin.getDefault().getModelAccessorRegistry().getModelAccessor(eObj);
                    if (!extPackage.eIsKnownType(className)) {
                        result = ctx.createFailureStatus(MessageFormat.format(Messages.ExistingDomainClassConstraint_classNotFoundInMetaModels, classNameString, parentDescription.getName()));
                    }
                } else {
                    result = ctx.createFailureStatus(MessageFormat.format(Messages.ExistingDomainClassConstraint_classNotFoundInMetaModels, classNameString, parentDescription.getName()));
                }
            } else {
                result = ctx.createFailureStatus(Messages.ExistingDomainClassConstraint_badDomainClassPattern);
            }
        }
        return result;
    }

    /**
     * Return the Meta-models associated to the containing parent description.
     * 
     * @param parentDescription
     *            the parentDescription.
     * @return the available meta-models.
     */
    private Collection<EPackage> getAvailableMetamodels(final EObject parentDescription) {
        Collection<EPackage> metamodel = null;
        if (parentDescription instanceof RepresentationDescription) {
            metamodel = ((RepresentationDescription) parentDescription).getMetamodel();
        } else if (parentDescription instanceof RepresentationExtensionDescription) {
            metamodel = ((RepresentationExtensionDescription) parentDescription).getMetamodel();
        }
        return metamodel;
    }
}
