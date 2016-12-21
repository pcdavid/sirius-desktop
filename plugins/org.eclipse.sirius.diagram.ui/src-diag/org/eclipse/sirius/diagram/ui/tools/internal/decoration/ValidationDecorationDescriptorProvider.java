/*******************************************************************************
 * Copyright (c) 2016 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.tools.internal.decoration;

import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.DiagramPlugin;
import org.eclipse.sirius.diagram.ui.business.api.view.SiriusGMFHelper;
import org.eclipse.sirius.diagram.ui.edit.api.part.IDiagramElementEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DDiagramEditPart;
import org.eclipse.sirius.diagram.ui.part.SiriusVisualIDRegistry;
import org.eclipse.sirius.diagram.ui.provider.DiagramUIPlugin;
import org.eclipse.sirius.diagram.ui.provider.Messages;
import org.eclipse.sirius.diagram.ui.tools.api.decoration.AbstractSiriusDecorationDescriptorProvider;
import org.eclipse.sirius.diagram.ui.tools.api.decoration.DecorationDescriptor;
import org.eclipse.sirius.diagram.ui.tools.api.decoration.DecorationDescriptor.DisplayPriority;
import org.eclipse.sirius.diagram.ui.tools.api.decoration.SiriusDecorationDescriptorProvider;
import org.eclipse.sirius.viewpoint.description.DecorationDistributionDirection;
import org.eclipse.sirius.viewpoint.description.Position;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Lists;

/**
 * A {@link SiriusDecorationDescriptorProvider} that corresponds to EMF
 * validation decorations.
 * 
 * @author <a href="mailto:laurent.fasani@obeo.fr">Laurent Fasani</a>
 */
public class ValidationDecorationDescriptorProvider extends AbstractSiriusDecorationDescriptorProvider implements SiriusDecorationDescriptorProvider {
    private static final String NAME = "validationStatus"; //$NON-NLS-1$

    private static final String MARKER_TYPE = DiagramUIPlugin.ID + ".diagnostic"; //$NON-NLS-1$

    @Override
    public boolean provides(IDiagramElementEditPart editPart) {
        boolean provides = false;

        if (editPart instanceof GraphicalEditPart || editPart instanceof AbstractConnectionEditPart) {
            Object model = editPart.getModel();
            if (model instanceof View) {
                View view = (View) model;
                if (!(view instanceof Edge) && !view.isSetElement()) {
                    return provides;
                }
                if (DDiagramEditPart.MODEL_ID.equals(SiriusVisualIDRegistry.getModelID(view)) && (view instanceof Edge || view.isSetElement())) {
                    EditDomain ed = editPart.getViewer().getEditDomain();
                    if (ed instanceof DiagramEditDomain) {
                        provides = DDiagramEditPart.MODEL_ID.equals(SiriusVisualIDRegistry.getModelID(view));
                    }
                }
            }
        }
        return provides;
    }

    @Override
    public List<DecorationDescriptor> createDecorationDescriptors(IDiagramElementEditPart editPart) {

        View view = (View) editPart.getModel();
        // query for all the validation markers of the current resource
        String elementId = SiriusGMFHelper.getViewId(view);
        if (elementId != null) {

            // Directly retrieve the main Session resource
            // (session.getSessionResource()) as we know we put the marker on
            // it.
            Session currentSession = null;
            Resource viewResource = view.eResource();
            ResourceSet currentRs = viewResource.getResourceSet();
            for (Session session : SessionManager.INSTANCE.getSessions()) {
                if (currentRs == session.getTransactionalEditingDomain().getResourceSet()) {
                    currentSession = session;
                    break;
                }
            }
            Resource markedResource = currentSession == null ? null : currentSession.getSessionResource();
            IResource resource = WorkspaceSynchronizer.getFile(markedResource);

            if (resource != null && resource.exists()) {
                IMarker[] markers = null;
                try {
                    markers = resource.findMarkers(MARKER_TYPE, true, IResource.DEPTH_INFINITE);
                } catch (CoreException e) {
                    DiagramPlugin.getDefault().logError(Messages.StatusDecorator_validationMarkersFailureMsg, e);
                }
                if (markers != null && markers.length > 0) {
                    return doCreateDecorationDescriptors(markers, elementId);
                }
            }
        }

        return null;

    }

    private List<DecorationDescriptor> doCreateDecorationDescriptors(IMarker[] markers, String elementId) {
        int severity = IMarker.SEVERITY_INFO;
        IMarker foundMarker = null;
        Label toolTip = null;
        for (int i = 0; i < markers.length; i++) {
            IMarker marker = markers[i];
            String attribute = marker.getAttribute(org.eclipse.gmf.runtime.common.ui.resources.IMarker.ELEMENT_ID, ""); //$NON-NLS-1$
            if (attribute.equals(elementId)) {
                int nextSeverity = marker.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
                Image nextImage = getImage(nextSeverity);
                if (foundMarker == null) {
                    foundMarker = marker;
                    toolTip = new Label(marker.getAttribute(IMarker.MESSAGE, ""), //$NON-NLS-1$
                            nextImage);
                } else {
                    if (toolTip.getChildren().isEmpty()) {
                        Label comositeLabel = new Label();
                        FlowLayout fl = new FlowLayout(false);
                        fl.setMinorSpacing(0);
                        comositeLabel.setLayoutManager(fl);
                        comositeLabel.add(toolTip);
                        toolTip = comositeLabel;
                    }
                    toolTip.add(new Label(marker.getAttribute(IMarker.MESSAGE, ""), //$NON-NLS-1$
                            nextImage));
                }
                severity = (nextSeverity > severity) ? nextSeverity : severity;
            }
        }
        if (foundMarker == null) {
            return null;
        }

        // add decoration
        DecorationDescriptor decoDesc = new DecorationDescriptor();
        decoDesc.setName(NAME);
        decoDesc.setPosition(Position.NORTH_EAST_LITERAL);
        decoDesc.setDistributionDirection(DecorationDistributionDirection.HORIZONTAL);
        decoDesc.setDisplayPriority(DisplayPriority.HIGH_PRIORITY.getValue());
        decoDesc.setDecorationAsImage(getImage(severity));
        decoDesc.setTooltipAsFigure(toolTip);

        return Lists.newArrayList(decoDesc);
    }

    private Image getImage(int severity) {
        String imageName = ISharedImages.IMG_OBJS_ERROR_TSK;
        switch (severity) {
        case IMarker.SEVERITY_ERROR:
            imageName = ISharedImages.IMG_OBJS_ERROR_TSK;
            break;
        case IMarker.SEVERITY_WARNING:
            imageName = ISharedImages.IMG_OBJS_WARN_TSK;
            break;
        default:
            imageName = ISharedImages.IMG_OBJS_INFO_TSK;
        }
        return PlatformUI.getWorkbench().getSharedImages().getImage(imageName);
    }
}
