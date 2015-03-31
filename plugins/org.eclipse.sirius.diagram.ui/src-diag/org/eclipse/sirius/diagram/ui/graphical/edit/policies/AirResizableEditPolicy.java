/*******************************************************************************
 * Copyright (c) 2008, 2014 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.graphical.edit.policies;

import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.requests.AlignmentRequest;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.ResizeTracker;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetBoundsCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ResizableShapeEditPolicy;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.LabelPosition;
import org.eclipse.sirius.diagram.NodeStyle;
import org.eclipse.sirius.diagram.business.api.query.DNodeQuery;
import org.eclipse.sirius.diagram.description.DiagramElementMapping;
import org.eclipse.sirius.diagram.ui.edit.api.part.IDiagramBorderNodeEditPart;
import org.eclipse.sirius.diagram.ui.edit.api.part.IDiagramElementEditPart;
import org.eclipse.sirius.diagram.ui.edit.api.part.IDiagramNodeEditPart;
import org.eclipse.sirius.diagram.ui.edit.internal.validators.ResizeValidator;
import org.eclipse.sirius.diagram.ui.internal.edit.commands.CenterEditPartEdgesCommand;
import org.eclipse.sirius.diagram.ui.internal.edit.commands.ChangeBendpointsOfEdgesCommand;
import org.eclipse.sirius.diagram.ui.internal.edit.commands.ChildrenAdjustmentCommand;
import org.eclipse.sirius.diagram.ui.tools.api.figure.SiriusWrapLabel;
import org.eclipse.sirius.diagram.ui.tools.api.graphical.edit.styles.IStyleConfigurationRegistry;
import org.eclipse.sirius.diagram.ui.tools.api.graphical.edit.styles.StyleConfiguration;
import org.eclipse.sirius.diagram.ui.tools.internal.ui.NoCopyDragEditPartsTrackerEx;
import org.eclipse.sirius.viewpoint.Style;

/**
 * The specific edit policy to redefine auto size.
 * 
 * @author ymortier
 */
public class AirResizableEditPolicy extends ResizableShapeEditPolicy {

    /**
     * {@inheritDoc}
     * 
     * Use our own ResizeTracker to set the "flag"
     * SiriusResizeTracker.FIX_CHILDREN_KEY when the corresponding shortcut is
     * pressed.
     */
    @Override
    protected ResizeTracker getResizeTracker(int direction) {
        return new SiriusResizeTracker((GraphicalEditPart) getHost(), direction);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.ResizableShapeEditPolicy#getAutoSizeCommand(org.eclipse.gef.Request)
     */
    @Override
    protected Command getAutoSizeCommand(final Request request) {
        if (this.getHost() instanceof IDiagramNodeEditPart) {
            final EObject eObject = ((IGraphicalEditPart) this.getHost()).resolveSemanticElement();
            if (eObject instanceof DNode) {
                final NodeStyle nodeStyle = (NodeStyle) ((DNode) eObject).getStyle();
                if (nodeStyle != null && nodeStyle.getLabelPosition() == LabelPosition.NODE_LITERAL) {
                    final DNode viewNode = (DNode) eObject;
                    final SiriusWrapLabel label = ((IDiagramNodeEditPart) getHost()).getNodeLabel();
                    final Style style = viewNode.getStyle();
                    final DiagramElementMapping mapping = viewNode.getDiagramElementMapping();
                    final DefaultSizeNodeFigure defaultSizeNodeFigure = (DefaultSizeNodeFigure) ((IDiagramNodeEditPart) this.getHost()).getMainFigure();
                    final StyleConfiguration styleConfiguration = IStyleConfigurationRegistry.INSTANCE.getStyleConfiguration(mapping, style);
                    final Dimension newDim = styleConfiguration.fitToText(viewNode, label, defaultSizeNodeFigure);
                    final TransactionalEditingDomain transactionalEditingDomain = TransactionUtil.getEditingDomain(viewNode);
                    final SetBoundsCommand setBoundsCommand = new SetBoundsCommand(transactionalEditingDomain, "Auto Size",
                            new EObjectAdapter(((IGraphicalEditPart) this.getHost()).getNotationView()), newDim);
                    return new ICommandProxy(setBoundsCommand);
                }
            }
        }
        return super.getAutoSizeCommand(request);
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#getMoveCommand(org.eclipse.gef.requests.ChangeBoundsRequest)
     */
    @Override
    protected Command getMoveCommand(final ChangeBoundsRequest request) {
        Command result = super.getMoveCommand(request);

        if (getHost().getParent() != null) {
            if (getHost() instanceof IGraphicalEditPart) {
                IGraphicalEditPart hostPart = (IGraphicalEditPart) getHost();
                CompositeTransactionalCommand ctc = new CompositeTransactionalCommand(hostPart.getEditingDomain(), result.getLabel());
                ctc.add(new CommandProxy(result));
                ctc.add(new ChangeBendpointsOfEdgesCommand(hostPart, new PrecisionPoint(request.getMoveDelta())));
                result = new ICommandProxy(ctc);
            }
            return result;
        } else {
            return null;
        }
    }

    @Override
    protected Command getAlignCommand(AlignmentRequest request) {
        Command result = super.getAlignCommand(request);

        Point delta = request.getMoveDelta();
        if (getHost() instanceof AbstractGraphicalEditPart) {
            Rectangle locationAndSize = new PrecisionRectangle(((AbstractGraphicalEditPart) getHost()).getFigure().getBounds());
            ((AbstractGraphicalEditPart) getHost()).getFigure().translateToAbsolute(locationAndSize);
            Rectangle newLocationAndSize = request.getTransformedRectangle(locationAndSize);
            delta = newLocationAndSize.getTopLeft().getTranslated(locationAndSize.getTopLeft().getNegated());
        }
        if (getHost() instanceof IGraphicalEditPart) {
            IGraphicalEditPart hostPart = (IGraphicalEditPart) getHost();
            CompositeTransactionalCommand ctc = new CompositeTransactionalCommand(hostPart.getEditingDomain(), result.getLabel());
            ctc.add(new CommandProxy(result));
            // The primary selection is ignored because it is not moved (it is
            // the reference used to align other selected edit parts).
            ctc.add(new ChangeBendpointsOfEdgesCommand(hostPart, new PrecisionPoint(delta), true));
            result = new ICommandProxy(ctc);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.gef.editpolicies.ResizableEditPolicy#createSelectionHandles()
     */
    @Override
    protected List createSelectionHandles() {
        List selectionHandles = super.createSelectionHandles();
        for (Object selectionHandle : selectionHandles) {
            // For each created MoveHandle
            if (selectionHandle instanceof MoveHandle) {
                // We set a drag tracker that will not cause the duplication of
                // graphical elements when holding "Ctrl" key down and moving an
                // element
                ((MoveHandle) selectionHandle).setDragTracker(new NoCopyDragEditPartsTrackerEx(getHost()));
            }
        }
        return selectionHandles;
    }

    /**
     * Operand can only be vertically resized.
     * 
     * {@inheritDoc}
     */
    @Override
    protected void createResizeHandle(List handles, int direction) {
        EditPart host = getHost();
        if (host instanceof IDiagramNodeEditPart || host instanceof IDiagramBorderNodeEditPart) {
            DNode node = (DNode) ((IDiagramElementEditPart) host).resolveDiagramElement();
            DNodeQuery query = new DNodeQuery(node);

            if (!query.allowsVerticalResize() && (PositionConstants.NORTH_SOUTH & direction) == direction) {
                return;
            }

            if (!query.allowsHorizontalResize() && (PositionConstants.EAST_WEST & direction) == direction) {
                return;
            }
        }
        super.createResizeHandle(handles, direction);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.gef.editpolicies.ResizableEditPolicy#getResizeCommand(org.eclipse.gef.requests.ChangeBoundsRequest)
     */
    @Override
    protected Command getResizeCommand(ChangeBoundsRequest request) {
        Command result = UnexecutableCommand.INSTANCE;

        boolean valid = true;
        ResizeValidator resizeValidator = new ResizeValidator(request);
        valid = resizeValidator.validate();

        if (valid) {
            ICommand solution = buildResizeCommand(request);
            if (solution != null) {
                result = new ICommandProxy(solution);
            }
        }

        // we add a command to keep the edges centered (if they should be)
        if (result != null && getHost() instanceof IGraphicalEditPart) {
            CenterEditPartEdgesCommand centerEditPartEdgesCommand = new CenterEditPartEdgesCommand((IGraphicalEditPart) getHost(), request);
            result = result.chain(new ICommandProxy(centerEditPartEdgesCommand));
        }
        return result;
    }

    /**
     * Build a specific command from the request that resize the edit part as
     * the normal command and change the location of children if needed:
     * <UL>
     * <LI>border nodes: to avoid an unexpected change of side</LI>
     * <LI>children nodes (container or not): The GMF coordinates of these nodes
     * are moved in order to keep these nodes at the same location graphically
     * (on screen). The GMF coordinates of these nodes are relative to its
     * parent.</LI>
     * </UL>
     * 
     * @param request
     *            the resize request
     * @return <code>null</code> or a Command
     */
    private ICommand buildResizeCommand(ChangeBoundsRequest request) {
        ICommand result = null;
        Command cmd = super.getResizeCommand(request);
        if (getHost() instanceof IGraphicalEditPart) {
            CompositeTransactionalCommand ctc = new CompositeTransactionalCommand(((IGraphicalEditPart) getHost()).getEditingDomain(), cmd.getLabel());
            ctc.add(new CommandProxy(cmd));
            ctc.add(new ChildrenAdjustmentCommand((IGraphicalEditPart) getHost(), request));
            result = ctc;
        } else if (cmd != null) {
            result = new CommandProxy(cmd);
        }
        return result;
    }
}
