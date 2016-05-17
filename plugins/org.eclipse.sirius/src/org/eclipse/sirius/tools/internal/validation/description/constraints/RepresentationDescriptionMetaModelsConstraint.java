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
import java.util.Set;

import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.sirius.business.api.componentization.ISiriusComponent;
import org.eclipse.sirius.common.tools.api.interpreter.EPackageLoadingCallback.EPackageDeclaration;
import org.eclipse.sirius.common.tools.api.interpreter.EPackageLoadingCallback.EPackageDeclarationSource;
import org.eclipse.sirius.common.tools.internal.interpreter.BundleClassLoading;
import org.eclipse.sirius.tools.internal.validation.AbstractConstraint;
import org.eclipse.sirius.viewpoint.Messages;
import org.eclipse.sirius.viewpoint.description.RepresentationDescription;
import org.eclipse.sirius.viewpoint.description.RepresentationExtensionDescription;

import com.google.common.collect.Sets;

/**
 * Constraint ensuring that there is at least one meta-model associated to
 * RepresentationDescription and that these meta-models are also defined as
 * dependencies of the VSP when the odesign is owned by a VSP.
 * 
 * @author <a href="mailto:laurent.fasani@obeo.fr">Laurent Fasani</a>
 */
public class RepresentationDescriptionMetaModelsConstraint extends AbstractConstraint {

    @Override
    public IStatus validate(IValidationContext ctx) {
        final EObject target = ctx.getTarget();

        Collection<IStatus> statuses = Sets.newLinkedHashSet();
        if (target instanceof RepresentationDescription || target instanceof RepresentationExtensionDescription) {
            Collection<EPackage> availableMetamodels = getAvailableMetamodels(target);
            if (availableMetamodels.isEmpty()) {
                statuses.add(ctx.createFailureStatus(Messages.RepresentationDescriptionMetaModelsConstraint_noMetaModel));
            } else {
                Resource vsmResource = target.eResource();
                if (vsmResource != null) {
                    URI vsmURI = vsmResource.getURI();
                    String projectName = vsmURI.segment(1);
                    if (isVSMOwnedByVSP(projectName)) {
                        statuses.addAll(checkMetamodelAsVSPDependency(ctx, projectName, availableMetamodels));
                    }
                }
            }
        }

        final IStatus returnStatus = getCompositeStatus(ctx, statuses);
        return returnStatus;
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

    private boolean isVSMOwnedByVSP(String projectName) {
        IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(ISiriusComponent.ID);
        if (extensionPoint != null) {
            IExtension[] extensions = extensionPoint.getExtensions();
            for (IExtension iExtension : extensions) {
                IContributor contributor = iExtension.getContributor();
                if (projectName.equals(contributor.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check that the <code>associatedMetamodels</code> are defined in the
     * transitive dependencies of the <code>projectName</code> plug-in.
     * 
     * @param ctx
     *            the validation context
     * @param projectName
     *            the projectName that should be a bundle
     * @param associatedMetamodels
     *            the meta-models that have to be checked.
     * @return
     */
    @SuppressWarnings("restriction")
    private Collection<IStatus> checkMetamodelAsVSPDependency(IValidationContext ctx, String projectName, Iterable<EPackage> associatedMetamodels) {
        Collection<IStatus> statuses = Sets.newLinkedHashSet();

        BundleClassLoading bundleClassLoading = new BundleClassLoading();
        Collection<EPackageDeclarationSource> ecoreDeclarationSources = bundleClassLoading.findEcoreDeclarations(Sets.newHashSet(projectName), Sets.<String> newHashSet());
        Set<String> mmPackagesNsUriFromVSP = Sets.newHashSet();
        for (EPackageDeclarationSource ePackageDeclarationSource : ecoreDeclarationSources) {
            Collection<EPackageDeclaration> ePackageDeclarations = ePackageDeclarationSource.getEPackageDeclarations();
            for (EPackageDeclaration ePackageDeclaration : ePackageDeclarations) {
                mmPackagesNsUriFromVSP.add(ePackageDeclaration.getNsURI());
            }
        }
        for (EPackage ePackage : associatedMetamodels) {
            String name = ePackage.getName();
            String nsURI = ePackage.getNsURI();
            if (!mmPackagesNsUriFromVSP.contains(nsURI)) {
                statuses.add(ctx.createFailureStatus(MessageFormat.format(Messages.RepresentationDescriptionMetaModelsConstraint_metaModelRequiredAsVSPDependency, name, projectName)));
            }
        }

        return statuses;
    }
}
