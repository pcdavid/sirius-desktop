/*******************************************************************************
 * Copyright (c) 2017 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.tools.internal.decoration;

import java.util.Set;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.CreateDecoratorsOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget;
import org.eclipse.sirius.diagram.ui.edit.api.part.IDiagramElementEditPart;
import org.eclipse.sirius.diagram.ui.tools.api.decoration.SiriusDecorationDescriptorProvider;
import org.eclipse.sirius.diagram.ui.tools.api.decoration.SiriusDecorationProviderRegistry;

import com.google.common.collect.Sets;

/**
 * This {@link IDecoratorProvider} should be the only one for Sirius Diagram.
 * Indeed, it will install a {@link IDecorator} that will handle fixed Sirius
 * decorations and decorations provided in the VSM.
 * 
 * @author <a href="mailto:laurent.fasani@obeo.fr">Laurent Fasani</a>
 */
public class SiriusDecoratorProvider extends AbstractProvider implements IDecoratorProvider {

    private static final String KEY = "siriusDecorator"; //$NON-NLS-1$

    /**
     * Allows to globally activate or deactivate the decoration in sirius
     * diagram.
     */
    private static boolean isActivateSiriusDecoration = true;

    public static boolean isActivateSiriusDecoration() {
        return isActivateSiriusDecoration;
    }

    /**
     * Indicates if Sirius decorator must be installed.
     * 
     * @param isActivateSiriusDecorationNew
     *            true if if Sirius decorator must be installed
     */
    public static void setActivateSiriusDecoration(boolean isActivateSiriusDecorationNew) {
        SiriusDecoratorProvider.isActivateSiriusDecoration = isActivateSiriusDecorationNew;
    }

    @Override
    public boolean provides(IOperation operation) {
        if (isActivateSiriusDecoration || !(operation instanceof CreateDecoratorsOperation)) {
            return false;
        }

        boolean provide = false;
        final IDecoratorTarget decoratorTarget = ((CreateDecoratorsOperation) operation).getDecoratorTarget();
        final EditPart editPart = decoratorTarget.getAdapter(EditPart.class);
        if (editPart instanceof IDiagramElementEditPart) {
            provide = true;
        }
        return provide;
    }

    @Override
    public void createDecorators(IDecoratorTarget decoratorTarget) {
        final EditPart editPart = decoratorTarget.getAdapter(EditPart.class);
        if (editPart instanceof IDiagramElementEditPart) {
            Set<SiriusDecorationDescriptorProvider> decorationDescriptorProviders = Sets.newHashSet();
            for (SiriusDecorationDescriptorProvider decorationDescriptorProvider : SiriusDecorationProviderRegistry.INSTANCE.getDecorationDescriptorProviders()) {
                if (decorationDescriptorProvider.provides((IDiagramElementEditPart) editPart)) {
                    decorationDescriptorProviders.add(decorationDescriptorProvider);
                }
            }
            decoratorTarget.installDecorator(KEY, new SiriusGenericDecorator(decoratorTarget, decorationDescriptorProviders));
        }
    }

}
