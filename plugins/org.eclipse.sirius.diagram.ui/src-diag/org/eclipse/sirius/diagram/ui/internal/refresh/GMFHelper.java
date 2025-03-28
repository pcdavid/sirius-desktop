/*******************************************************************************
 * Copyright (c) 2011, 2024 THALES GLOBAL SERVICES and others.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.internal.refresh;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.FontStyle;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Location;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.Size;
import org.eclipse.gmf.runtime.notation.Style;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.sirius.common.tools.api.resource.FileProvider;
import org.eclipse.sirius.common.tools.api.util.StringUtil;
import org.eclipse.sirius.common.ui.tools.api.util.EclipseUIUtil;
import org.eclipse.sirius.diagram.AbstractDNode;
import org.eclipse.sirius.diagram.ContainerStyle;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DDiagramElementContainer;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.DNodeList;
import org.eclipse.sirius.diagram.WorkspaceImage;
import org.eclipse.sirius.diagram.business.api.query.DDiagramElementQuery;
import org.eclipse.sirius.diagram.model.business.internal.query.DDiagramElementContainerExperimentalQuery;
import org.eclipse.sirius.diagram.model.business.internal.query.DNodeContainerExperimentalQuery;
import org.eclipse.sirius.diagram.ui.business.api.query.NodeQuery;
import org.eclipse.sirius.diagram.ui.business.api.query.ViewQuery;
import org.eclipse.sirius.diagram.ui.business.internal.query.DNodeContainerQuery;
import org.eclipse.sirius.diagram.ui.business.internal.query.DNodeQuery;
import org.eclipse.sirius.diagram.ui.edit.api.part.AbstractDiagramElementContainerEditPart;
import org.eclipse.sirius.diagram.ui.edit.api.part.IDiagramNameEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.AbstractDNodeContainerCompartmentEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeContainer2EditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeList2EditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeListEditPart;
import org.eclipse.sirius.diagram.ui.internal.refresh.borderednode.CanonicalDBorderItemLocator;
import org.eclipse.sirius.diagram.ui.part.SiriusVisualIDRegistry;
import org.eclipse.sirius.diagram.ui.provider.DiagramUIPlugin;
import org.eclipse.sirius.diagram.ui.provider.Messages;
import org.eclipse.sirius.diagram.ui.tools.api.layout.LayoutUtils;
import org.eclipse.sirius.diagram.ui.tools.api.util.GMFNotationHelper;
import org.eclipse.sirius.ext.gmf.runtime.gef.ui.figures.AlphaDropShadowBorder;
import org.eclipse.sirius.ext.gmf.runtime.gef.ui.figures.IContainerLabelOffsets;
import org.eclipse.sirius.ext.gmf.runtime.gef.ui.figures.LabelBorderStyleIds;
import org.eclipse.sirius.ui.business.api.dialect.DialectEditor;
import org.eclipse.sirius.ui.business.api.session.IEditingSession;
import org.eclipse.sirius.ui.business.api.session.SessionUIManager;
import org.eclipse.sirius.ui.tools.api.color.VisualBindingManager;
import org.eclipse.sirius.viewpoint.BasicLabelStyle;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.sirius.viewpoint.description.style.LabelBorderStyleDescription;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;

/**
 * GMF Helper.
 * 
 * @author edugueperoux
 */
public final class GMFHelper {

    /**
     * see org.eclipse.sirius.diagram.ui.internal.edit.parts. AbstractDNodeContainerCompartmentEditPart.DEFAULT_MARGIN
     * the top value is the DEFAULT_MARGIN + the InvisibleResizableCompartmentFigure top Inset (1px), also corresponding
     * to IContainerLabelOffsets.LABEL_OFFSET.
     */
    private static final Insets FREEFORM_CONTAINER_INSETS = new Insets(IContainerLabelOffsets.LABEL_OFFSET, AbstractDNodeContainerCompartmentEditPart.DEFAULT_MARGIN,
            AbstractDNodeContainerCompartmentEditPart.DEFAULT_MARGIN, AbstractDNodeContainerCompartmentEditPart.DEFAULT_MARGIN);

    private static final Insets LIST_CONTAINER_INSETS = new Insets(4, 0, 0, 0);

    private static final Insets HV_STACK_CONTAINER_INSETS = new Insets(4, 0, 0, 0);

    /**
     * The gap in pixels between the Label's icon and its text
     * (org.eclipse.sirius.ext.gmf.runtime.gef.ui.figures.SiriusWrapLabel.getIconTextGap()).
     */
    private static final int ICON_TEXT_GAP = 3;

    private static Map<Node, Rectangle> boundsCache = new HashMap<>();

    private GMFHelper() {
        // Helper to not instantiate
    }

    /**
     * Get the absolute location relative to the origin (Diagram).
     * 
     * @param node
     *            the GMF Node
     * @param insetsAware
     *            true to consider the draw2D figures insets. <strong>Warning:</strong> Those insets are based on the
     *            current Sirius editParts and could become wrong if a developer customizes them.
     * @param adaptBorderNodeLocation
     *            Useful for specific border nodes, like in sequence diagrams, to center the border nodes if the
     *            coordinate is 0 (x for EAST or WEST side, y for NORTH or SOUTH side)
     * 
     * @return the absolute location of the node relative to the origin (Diagram)
     */
    public static Point getAbsoluteLocation(Node node, boolean insetsAware, boolean adaptBorderNodeLocation) {
        // TODO: Location of title "DNode*NameEditPart", x coordinate, can be wrong according to label alignment. This
        // problem is not yet handled.

        Point location = new Point(0, 0);
        LayoutConstraint layoutConstraint = node.getLayoutConstraint();
        if (layoutConstraint instanceof Bounds gmfBounds && !(new ViewQuery(node).isRegion())) {
            // The bounds is computed for horizontal or vertical regions, even if it is stored in GMF data
            location.setX(gmfBounds.getX());
            location.setY(gmfBounds.getY());
        }
        ViewQuery viewQuery = new ViewQuery(node);
        if (adaptBorderNodeLocation && viewQuery.isBorderedNode() && layoutConstraint instanceof Bounds gmfBounds) {
            // manage location of bordered node with closest side
            if (node.getElement() instanceof DNode dNode && dNode.eContainer() instanceof AbstractDNode parentAbstractDNode && parentAbstractDNode.getOwnedBorderedNodes().contains(dNode)) {
                Node parentNode = (Node) node.eContainer();
                if (parentNode.getLayoutConstraint() instanceof Bounds parentBounds) {
                    int position = CanonicalDBorderItemLocator.findClosestSideOfParent(new Rectangle(gmfBounds.getX(), gmfBounds.getY(), gmfBounds.getWidth(), gmfBounds.getHeight()),
                            new Rectangle(parentBounds.getX(), parentBounds.getY(), parentBounds.getWidth(), parentBounds.getHeight()));
                    centerLocationIfZero(location, position, parentBounds, gmfBounds);
                }
            }
        }
        if (viewQuery.isListCompartment()) {
            // Translate the compartment to be just below the the title, the x coordinate is also the same (same parent
            // insets)
            Rectangle titleBounds = getAbsoluteBounds(getPreviousChild(node), true, false, false, false);
            location.translate(titleBounds.preciseX(), titleBounds.preciseY() + titleBounds.preciseHeight());
            // Translate from the spacing (5 pixels)
            location.translate(0, IContainerLabelOffsets.LABEL_OFFSET);
        } else if (viewQuery.isListItem()) {
            if (node.eContainer() instanceof Node container) {
                if (container.getChildren().get(0) == node) {
                    Point parentNodeLocation = getAbsoluteLocation(container, insetsAware, false);
                    location.translate(parentNodeLocation);
                    if (insetsAware) {
                        translateWithInsets(location, node);
                    }
                } else {
                    // Translate from the previous children
                    Rectangle previousChildBounds = getAbsoluteBounds(getPreviousChild(node), true, false, false, false);
                    location.translate(previousChildBounds.preciseX(), previousChildBounds.preciseY() + previousChildBounds.preciseHeight());
                }
            }
        } else if (viewQuery.isRegionContainerCompartment()) {
            // Translate the compartment to be just below the the title, the x coordinate is also the same (same parent
            // insets)
            Rectangle titleBounds = getAbsoluteBounds(getPreviousChild(node), true, false, false, false);
            location.translate(titleBounds.preciseX(), titleBounds.preciseY() + titleBounds.preciseHeight());
            // Translate from the spacing (5 pixels)
            location.translate(0, IContainerLabelOffsets.LABEL_OFFSET);
        } else if (viewQuery.isVerticalRegion()) {
            if (node.eContainer() instanceof Node container) {
                if (container.getChildren().get(0) == node) {
                    Point parentNodeLocation = getAbsoluteLocation(container, insetsAware, false);
                    location.translate(parentNodeLocation);
                    if (insetsAware) {
                        translateWithInsets(location, node);
                    }
                } else {
                    // Translate from the previous children
                    Rectangle previousChildBounds = getAbsoluteBounds(getPreviousChild(node), true, false, false, false);
                    location.translate(previousChildBounds.preciseX(), previousChildBounds.preciseY() + previousChildBounds.preciseHeight());
                }
            }
        } else if (viewQuery.isHorizontalRegion()) {
            if (node.eContainer() instanceof Node container) {
                if (container.getChildren().get(0) == node) {
                    Point parentNodeLocation = getAbsoluteLocation(container, insetsAware, false);
                    location.translate(parentNodeLocation);
                    if (insetsAware) {
                        translateWithInsets(location, node);
                    }
                } else {
                    // Translate from the previous children
                    Rectangle previousChildBounds = getAbsoluteBounds(getPreviousChild(node), true, false, false, false);
                    location.translate(previousChildBounds.preciseX() + previousChildBounds.preciseWidth(), previousChildBounds.preciseY());
                }
            }
        } else if (node.eContainer() instanceof Node container) {
            Point parentNodeLocation = getAbsoluteLocation(container, insetsAware, true);
            location.translate(parentNodeLocation);
            if (insetsAware) {
                translateWithInsets(location, node);
            }
        }
        return location;
    }

    private static Node getPreviousChild(Node searchedNode) {
        Node previousChild = null;
        boolean found = false;
        if (searchedNode.eContainer() instanceof Node container) {
            for (Iterator<View> children = container.getChildren().iterator(); children.hasNext() && !found; /* */) {
                View child = children.next();
                if (child instanceof Node nodeChild) {
                    if (searchedNode == nodeChild) {
                        found = true;
                    } else {
                        previousChild = nodeChild;
                    }
                }
            }
        }
        if (found) {
            return previousChild;
        } else {
            return null;
        }
    }

    /**
     * Return the top-left insets of this <code>container</code>. The insets also considers its border.
     * 
     * @param container
     *            The container for which we wish to have the insets. This {@link Node} must correspond to a container,
     *            otherwise, {0,0} is returned
     * @return the top-left insets of this <code>container</code>
     */
    public static Dimension getTopLeftInsets(Node container) {
        Dimension result = new Dimension(0, 0);
        NodeQuery nodeQuery = new NodeQuery(container);
        if (nodeQuery.isContainer()) {
            EObject element = container.getElement();
            if (element instanceof DDiagramElementContainer ddec) {
                // RegionContainer do not have containers insets
                if (ddec instanceof DNodeContainer dNodeContainer) {
                    if (new DNodeContainerExperimentalQuery(dNodeContainer).isRegionContainer()) {
                        result.setHeight(HV_STACK_CONTAINER_INSETS.top);
                    } else if (hasFullLabelBorder(ddec)) {
                        result.setHeight(FREEFORM_CONTAINER_INSETS.top);
                    } else if (new DDiagramElementContainerExperimentalQuery(ddec).isRegion()) {
                        // No margin
                    } else {
                        result.setWidth(FREEFORM_CONTAINER_INSETS.left);
                        result.setHeight(FREEFORM_CONTAINER_INSETS.top);
                    }
                } else if (element instanceof DNodeList) {
                    result.setWidth(LIST_CONTAINER_INSETS.left);
                    result.setHeight(LIST_CONTAINER_INSETS.top);
                }
                if (!new DDiagramElementContainerExperimentalQuery(ddec).isRegion()) {
                    Dimension borderSize = getBorderSize(ddec);
                    result.setWidth(result.width() + borderSize.width());
                    result.setHeight(result.height() + borderSize.height());
                }
            }
        } else if (nodeQuery.isListCompartment()) {
            // Add the corresponding margin of {1, 4, 0, 4} of
            // org.eclipse.sirius.diagram.ui.internal.edit.parts.AbstractDNodeListCompartmentEditPart.createFigure(),
            // more precisely at the end of the method, the margin is the evaluation of
            // "result.getContentPane().getBorder()".
            result.setWidth(4);
            result.setHeight(1);
        } else if (nodeQuery.isRegionContainerCompartment()) {
            // Add the corresponding OneLineMarginBorder of {1, 0, 0, 0} corresponding to
            // org.eclipse.sirius.diagram.ui.edit.internal.part.DiagramContainerEditPartOperation.refreshBorder(AbstractDiagramElementContainerEditPart,
            // ViewNodeContainerFigureDesc, ContainerStyle)
            // TODO : 1 should be replace by borderSize (style.getBorderSize().intValue())
            result.setWidth(0);
            result.setHeight(1);
        }
        return result;
    }

    /**
     * Return the top-left insets of the container of this <code>node</code>. The insets also considers its border (the
     * size of the border).
     * 
     * @param node
     *            The current node
     * @param searchFirstParentContainer
     *            true to call recursively until finding a Node container, {@link NodeQuery#isContainer()}, false
     *            otherwise
     * @return the top-left insets of the container of this <code>node</code>
     */
    public static Dimension getContainerTopLeftInsets(Node node, boolean searchFirstParentContainer) {
        Dimension result = new Dimension(0, 0);
        EObject nodeContainer = node.eContainer();
        if (nodeContainer instanceof Node parentNode) {
            NodeQuery nodeQuery = new NodeQuery(parentNode);
            if (nodeQuery.isContainer() || nodeQuery.isListCompartment() || nodeQuery.isRegionContainerCompartment()) {
                result = getTopLeftInsets(parentNode);
            } else if (searchFirstParentContainer) {
                result = getContainerTopLeftInsets(parentNode, searchFirstParentContainer);
            }
        }
        return result;
    }

    /**
     * Return the bottom-right insets of the container of this <code>node</code>. The insets also considers its border.
     * 
     * @param container
     *            The container for which we wish to have the insets. This {@link Node} must correspond to a container,
     *            otherwise, {0,0} is returned
     * @return the bottom-right insets of this <code>container</code>
     */
    private static Dimension getBottomRightInsets(Node container) {
        Dimension result = new Dimension(0, 0);
        NodeQuery nodeQuery = new NodeQuery(container);
        if (nodeQuery.isContainer()) {
            EObject element = container.getElement();
            if (element instanceof DDiagramElementContainer ddec) {
                // RegionContainer do not have containers insets
                if (ddec instanceof DNodeContainer dNodeContainer) {
                    if (new DNodeContainerExperimentalQuery(dNodeContainer).isRegionContainer()) {
                        result.setWidth(LIST_CONTAINER_INSETS.right);
                        result.setHeight(LIST_CONTAINER_INSETS.bottom);
                        // TODO: to verify
                        Dimension borderSize = getBorderSize(ddec);
                        result.setWidth(result.width() + borderSize.width());
                        result.setHeight(result.height() + borderSize.height());
                    } else if (new DDiagramElementContainerExperimentalQuery(ddec).isRegion()) {
                        // No margin, except the border size
                        Dimension borderSize = getBorderSize(ddec);
                        result.setWidth(result.width() + borderSize.width());
                        result.setHeight(result.height() + borderSize.height());
                    } else {
                        if (hasFullLabelBorder(ddec)) {
                            // TODO : Not sure about that, to verify
                            result.setHeight(FREEFORM_CONTAINER_INSETS.bottom);
                        } else {
                            result.setWidth(FREEFORM_CONTAINER_INSETS.right);
                            result.setHeight(FREEFORM_CONTAINER_INSETS.bottom);
                        }
                        Dimension borderSize = getBorderSize(ddec);
                        // Added twice as this insets is used to compute the "global" size including the border
                        result.setWidth(result.width() + (borderSize.width() * 2));
                        result.setHeight(result.height() + (borderSize.height() * 2));
                    }
                } else if (ddec instanceof DNodeList) {
                    result.setWidth(LIST_CONTAINER_INSETS.right);
                    result.setHeight(LIST_CONTAINER_INSETS.bottom);
                    // TODO: to verify
                    Dimension borderSize = getBorderSize(ddec);
                    result.setWidth(result.width() + borderSize.width());
                    result.setHeight(result.height() + borderSize.height());
                }
            }
        } else if (nodeQuery.isListCompartment()) {
            // Add the corresponding margin of {1, 4, 0, 4} of
            // org.eclipse.sirius.diagram.ui.internal.edit.parts.AbstractDNodeListCompartmentEditPart.createFigure(),
            // more precisely at the end of the method, the margin is the evaluation of
            // "result.getContentPane().getBorder()".
            result.setWidth(4);
            result.setHeight(0);
        }
        return result;
    }

    /**
     * Return the top-left insets of the container of this <code>node</code> that is after the label. The insets also
     * considers its border.
     * 
     * @param node
     *            The current node
     * @param searchFirstParentContainer
     *            true to call recursively until finding a Node container, {@link NodeQuery#isContainer()}, false
     *            otherwise
     * @return the top-left insets of the container of this <code>node</code>
     */
    public static Dimension getContainerTopLeftInsetsAfterLabel(Node node, boolean searchFirstParentContainer) {
        Dimension result = new Dimension(0, 0);
        EObject nodeContainer = node.eContainer();
        if (nodeContainer instanceof Node parentNode) {
            NodeQuery nodeQuery = new NodeQuery(parentNode);
            if (nodeQuery.isContainer()) {
                EObject element = parentNode.getElement();
                if (element instanceof DDiagramElementContainer dDiagramElementContainer) {
                    result.setWidth(FREEFORM_CONTAINER_INSETS.left);
                    result.setHeight(FREEFORM_CONTAINER_INSETS.top);

                    Dimension borderSize = getBorderSize(dDiagramElementContainer);
                    result.setWidth(result.width() + borderSize.width());
                    result.setHeight(result.height() + borderSize.height());
                }
            } else if (searchFirstParentContainer) {
                result = getContainerTopLeftInsets(parentNode, searchFirstParentContainer);
            }
        }
        return result;
    }

    /**
     * Get the border size of the <code>ddec</code> ({@link DDiagramElementContainer}).
     * 
     * @param ddec
     *            The {@link DDiagramElementContainer}
     * @return the border size of the diagram element container.
     */
    public static Dimension getBorderSize(DDiagramElementContainer ddec) {
        Dimension result = new Dimension(0, 0);
        int borderSize = 0;
        ContainerStyle containerStyle = ddec.getOwnedStyle();
        if (containerStyle != null && containerStyle.getBorderSize() != null) {
            borderSize = containerStyle.getBorderSize().intValue();
        }
        DDiagramElementContainerExperimentalQuery regionQuery = new DDiagramElementContainerExperimentalQuery(ddec);
        if (regionQuery.isRegionInHorizontalStack()) {
            result.setWidth(isFirstRegion(ddec) ? 0 : borderSize);
            result.setHeight(1);
        } else if (regionQuery.isRegionInVerticalStack()) {
            result.setWidth(0);
            result.setHeight(isFirstRegion(ddec) ? 1 : borderSize);
        } else {
            result.setWidth(borderSize);
            result.setHeight(borderSize);
        }
        return result;
    }

    /**
     * Shift the current node absolute location by the container insets.
     * 
     * @param locationToTranslate
     *            the current computed location that will be translated by the container insets.
     * @param currentNode
     *            the current node for which we translate location. We do not change the currentNode bounds.
     */
    private static void translateWithInsets(Point locationToTranslate, Node currentNode) {
        NodeQuery nodeQuery = new NodeQuery(currentNode);
        // Border nodes are not concerned by those insets.
        if (!nodeQuery.isBorderedNode()) {
            locationToTranslate.translate(getContainerTopLeftInsets(currentNode, false));
            if (currentNode.eContainer() instanceof Node container && new ViewQuery(currentNode).isListItem() && container.getChildren().get(0) == currentNode) {
                // This is the first list item, add a one margin border over it.
                locationToTranslate.translate(0, 1);
            }
        }
    }

    private static boolean hasFullLabelBorder(DDiagramElementContainer ddec) {
        Optional<LabelBorderStyleDescription> labelBorderStyle = new DDiagramElementContainerExperimentalQuery(ddec).getLabelBorderStyle();
        return labelBorderStyle.isPresent() && LabelBorderStyleIds.LABEL_FULL_BORDER_STYLE_FOR_CONTAINER_ID.equals(labelBorderStyle.get().getId());
    }

    private static boolean isFirstRegion(DDiagramElementContainer ddec) {
        EObject potentialRegionContainer = ddec.eContainer();
        if (potentialRegionContainer instanceof DNodeContainer dNodeContainer) {
            Optional<DDiagramElement> optionalDDiagramElement = dNodeContainer.getOwnedDiagramElements().stream().filter(DDiagramElementContainer.class::isInstance).findFirst();
            return optionalDDiagramElement.isPresent() && ddec == optionalDDiagramElement.get();
        }
        return false;
    }

    /**
     * Shift the current node absolute bounds location by the container insets.
     * 
     * @param boundsToTranslate
     *            the current computed bounds that will be translated by the container insets.
     * @param currentNode
     *            the current node for which we translate bounds. We do not change the currentNode bounds.
     */
    private static void translateWithInsets(Rectangle boundsToTranslate, Node currentNode) {
        Point location = boundsToTranslate.getLocation();
        translateWithInsets(location, currentNode);
        boundsToTranslate.setLocation(location);
    }

    /**
     * Compute the location of a GMF node.
     * 
     * @param node
     *            the node whose location to compute.
     * @return the location of the node.
     * @deprecated Use getAbsoluteLocation instead. This method will be removed in future.
     */
    @Deprecated
    public static Point getLocation(Node node) {
        return getAbsoluteLocation(node, true, true);
    }

    private static void centerLocationIfZero(Point location, int position, Bounds parentBounds, Bounds gmfBounds) {
        switch (position) {
        case PositionConstants.NORTH, PositionConstants.SOUTH:
            if (location.x == 0) {
                location.setX(location.x + (parentBounds.getWidth() - gmfBounds.getWidth()) / 2);
            }
            break;
        case PositionConstants.WEST, PositionConstants.EAST:
            if (location.y == 0) {
                location.setY(location.y + (parentBounds.getHeight() - gmfBounds.getHeight()) / 2);
            }
            break;
        default:
            break;
        }
    }

    /**
     * Get the absolute bounds relative to the origin (Diagram).
     * 
     * @param node
     *            the GMF Node
     * @param insetsAware
     *            true to consider the draw2D figures insets. <strong>Warning:</strong> Those insets are based on the
     *            current Sirius editParts and could become wrong if a developer customizes them.
     * @param boxForConnection
     *            true if we want to have the bounds used to compute connection anchor from source or target, false
     *            otherwise
     * @param recursiveGetBounds
     *            true if this method is called from a parent "getBounds" call, false otherwise.
     * @param adaptBorderNodeLocation
     *            Useful for specific border nodes, like in sequence diagrams, to center the border nodes if the
     *            coordinate is 0 (x for EAST or WEST side, y for NORTH or SOUTH side)
     * 
     * @return the absolute bounds of the node relative to the origin (Diagram)
     */
    public static Rectangle getAbsoluteBounds(Node node, boolean insetsAware, boolean boxForConnection, boolean recursiveGetBounds, boolean adaptBorderNodeLocation) {
        if (!recursiveGetBounds) {
            boundsCache.clear();
        }
        Rectangle result;
        if (boundsCache.containsKey(node)) {
            result = boundsCache.get(node);
        } else {
            Point location = getAbsoluteLocation(node, insetsAware, adaptBorderNodeLocation);
            Rectangle bounds = getBounds(node, false, false, boxForConnection, recursiveGetBounds, location);
            result = new PrecisionRectangle(location.preciseX(), location.preciseY(), bounds.preciseWidth(), bounds.preciseHeight());
            if (recursiveGetBounds) {
                boundsCache.put(node, result);
            }
        }
        return result.getCopy();
    }

    /**
     * Get the absolute bounds relative to the origin (Diagram).
     * 
     * @param edge
     *            the GMF Node
     * 
     * @return the absolute bounds of the edge relative to the origin (Diagram)
     */
    public static Optional<Rectangle> getAbsoluteBounds(Edge edge) {
        return getAbsoluteBounds(edge, false, false);
    }

    /**
     * Get the absolute bounds relative to the origin (Diagram).
     * 
     * @param edge
     *            the GMF Node
     * @param insetsAware
     *            true to consider the draw2D figures insets. <strong>Warning:</strong> Those insets are based on the
     *            current Sirius editParts and could become wrong if a developer customizes them.
     * @param boxForConnection
     *            true if we want to have the bounds used to compute connection anchor from source or target, false
     *            otherwise
     * 
     * @return the absolute bounds of the edge relative to the origin (Diagram)
     */
    public static Optional<Rectangle> getAbsoluteBounds(Edge edge, boolean insetsAware, boolean boxForConnection) {
        // Workaround for canonical refresh about edge on edge
        Optional<Rectangle> optionalSourceBounds = getAbsoluteBounds(edge.getSource(), insetsAware, boxForConnection);
        Optional<Rectangle> optionalTargetBounds = getAbsoluteBounds(edge.getTarget(), insetsAware, boxForConnection);
        if (optionalSourceBounds.isPresent() && optionalTargetBounds.isPresent()) {
            return Optional.ofNullable(optionalSourceBounds.get().union(optionalTargetBounds.get()));
        }
        return Optional.empty();
    }

    /**
     * Get the absolute bounds relative to the origin (Diagram).
     * 
     * @param view
     *            the GMF Node or Edge
     * 
     * @return an optional absolute bounds of the node or edge relative to the origin (Diagram)
     */
    public static Optional<Rectangle> getAbsoluteBounds(View view) {
        return getAbsoluteBounds(view, false);
    }

    /**
     * Get the absolute bounds relative to the origin (Diagram).
     * 
     * @param view
     *            the GMF Node or Edge
     * @param insetsAware
     *            true to consider the draw2D figures insets. <strong>Warning:</strong> Those insets are based on the
     *            current Sirius editParts and could become wrong if a developer customizes them.
     * 
     * @return an optional absolute bounds of the node or edge relative to the origin (Diagram)
     */
    public static Optional<Rectangle> getAbsoluteBounds(View view, boolean insetsAware) {
        return getAbsoluteBounds(view, insetsAware, false);
    }

    /**
     * Get the absolute bounds relative to the origin (Diagram).
     * 
     * @param view
     *            the GMF Node or Edge
     * @param insetsAware
     *            true to consider the draw2D figures insets. <strong>Warning:</strong> Those insets are based on the
     *            current Sirius editParts and could become wrong if a developer customizes them.
     * @param boxForConnection
     *            true if we want to have the bounds used to compute connection anchor from source or target, false
     *            otherwise
     * 
     * @return an optional absolute bounds of the node or edge relative to the origin (Diagram)
     */
    public static Optional<Rectangle> getAbsoluteBounds(View view, boolean insetsAware, boolean boxForConnection) {
        Optional<Rectangle> result = Optional.empty();
        if (view instanceof Node) {
            result = Optional.ofNullable(getAbsoluteBounds((Node) view, insetsAware, boxForConnection, false, true));
        } else if (view instanceof Edge) {
            result = getAbsoluteBounds((Edge) view, insetsAware, boxForConnection);
        }
        return result;
    }

    /**
     * Compute the bounds of a GMF node.
     * 
     * @param node
     *            the node whose bounds to compute.
     * @return the bounds of the node.
     */
    public static Rectangle getBounds(Node node) {
        return getBounds(node, false);
    }

    /**
     * Compute the bounds of a GMF node.
     * 
     * @param node
     *            the node whose bounds to compute.
     * @param useFigureForAutoSizeConstraint
     *            true to use figure for auto size constraint
     * @return the bounds of the node.
     */
    public static Rectangle getBounds(Node node, boolean useFigureForAutoSizeConstraint) {
        return getBounds(node, useFigureForAutoSizeConstraint, false);
    }

    /**
     * Compute the bounds of a GMF node.
     * 
     * @param node
     *            the node whose bounds to compute.
     * @param useFigureForAutoSizeConstraint
     *            true to use figure for auto size constraint
     * @param forceFigureAutoSize
     *            if useFigureForAutoSizeConstraint and if the found edit part supports it, force auto size and validate
     *            the parent to get the auto-sized dimension (during auto-size for example)
     * @return the bounds of the node.
     */
    public static Rectangle getBounds(Node node, boolean useFigureForAutoSizeConstraint, boolean forceFigureAutoSize) {
        return getBounds(node, useFigureForAutoSizeConstraint, forceFigureAutoSize, false, false, new Point());
    }

    /**
     * Compute the bounds of a GMF node.
     * 
     * @param node
     *            the node whose bounds to compute.
     * @param useFigureForAutoSizeConstraint
     *            true to use figure for auto size constraint
     * @param forceFigureAutoSize
     *            if useFigureForAutoSizeConstraint and if the found edit part supports it, force auto size and validate
     *            the parent to get the auto-sized dimension (during auto-size for example)
     * @param boxForConnection
     *            true if we want to have the bounds used to compute connection anchor from source or target, false
     *            otherwise
     * @param recursiveGetBounds
     *            true if this method is called from a parent "getBounds" call, false otherwise
     * @param computedAbsoluteLocation
     *            The absolute location of the <code>node</code>
     * 
     * @return the bounds of the node.
     */
    public static Rectangle getBounds(Node node, boolean useFigureForAutoSizeConstraint, boolean forceFigureAutoSize, boolean boxForConnection, boolean recursiveGetBounds,
            Point computedAbsoluteLocation) {
        PrecisionRectangle bounds = new PrecisionRectangle(computedAbsoluteLocation.preciseX(), computedAbsoluteLocation.preciseY(), 0, 0);
        LayoutConstraint layoutConstraint = node.getLayoutConstraint();
        EObject element = node.getElement();
        if (element instanceof AbstractDNode abstractDNode) {
            if (layoutConstraint instanceof Size size) {
                bounds.setWidth(size.getWidth());
                bounds.setHeight(size.getHeight());
            } else {
                bounds.setWidth(-1);
                bounds.setHeight(-1);
            }
            ViewQuery viewQuery = new ViewQuery(node);
            if (viewQuery.isForNameEditPart() || viewQuery.isListItem()) {
                if (abstractDNode.getName() == null || abstractDNode.getName().length() == 0) {
                    if (bounds.width == -1) {
                        bounds.setWidth(0);
                    }
                    if (bounds.height == -1) {
                        bounds.setHeight(0);
                    }
                } else {

                    // Make a default size for label (this size is purely an average estimate)
                    replaceAutoSize(node, bounds, useFigureForAutoSizeConstraint, getLabelDimension(node, new Dimension(50, 20)), recursiveGetBounds);
                }
            } else {
                replaceAutoSize(node, bounds, useFigureForAutoSizeConstraint, null, recursiveGetBounds);
            }

            if (boxForConnection) {
                // Remove the shadow border size (as it is done in SlidableAnchor.getBox() that calls
                // HandleBounds.getHandleBounds() (for example
                // org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure.getHandleBounds())
                // This insets corresponds to insets of {@link
                // org.eclipse.sirius.diagram.ui.tools.api.figure.AlphaDropShadowBorder#getTransparentInsets()}.
                double shadowBorderSize = getShadowBorderSize(node);
                bounds.setPreciseWidth(bounds.preciseWidth() - shadowBorderSize);
                bounds.setPreciseHeight(bounds.preciseHeight() - shadowBorderSize);
            }
        }
        return bounds;
    }

    /**
     * If the editPart is a container and is not a workspace image or a regions, the default shadow border size is
     * returned. Otherwise, 0 is returned. See
     * {@link AbstractDiagramElementContainerEditPart#addDropShadow(NodeFigure,IFigure)}.
     * 
     * @param editPart
     *            an edit part
     * @return The shadow border size of the edit part
     */
    public static double getShadowBorderSize(Node node) {
        double shadowBorderSize = 0;
        if (isShadowBorderNeeded(node)) {
            shadowBorderSize = AlphaDropShadowBorder.getDefaultShadowSize();
        }
        return shadowBorderSize;
    }

    /**
     * Shadow border is needed for all container except for regions or workspace image styles. These can have a
     * non-rectangular contour and transparent zones which should be kept as is.
     * 
     * @return false for regions and workspace images, true otherwise.
     */
    private static boolean isShadowBorderNeeded(Node node) {
        boolean needShadowBorder = false;
        EObject element = node.getElement();
        ViewQuery viewQuery = new ViewQuery(node);
        if (!viewQuery.isFreeFormCompartment() && !viewQuery.isListCompartment() && !viewQuery.isForNameEditPart() && element instanceof DDiagramElementContainer ddec) {
            needShadowBorder = !(new DDiagramElementContainerExperimentalQuery(ddec).isRegion() || ddec.getOwnedStyle() instanceof WorkspaceImage);
        }
        return needShadowBorder;
    }

    /**
     * This method replace the -1x-1 size with a more realistic size. This size is probably not exactly the same as in
     * draw2d but much closer that -1x-1.
     * 
     * @param node
     *            The GMF node with the auto-size
     * @param bounds
     *            The bounds with auto-size to replace.
     * @param useFigureForAutoSizeConstraint
     *            true to use draw2d figure to get size
     * @param providedDefaultSize
     *            The size used for creation for this kind of <code>node</code>. It is the minimum size.
     * @param recursive
     *            true if this method is called from a "parent" call, false otherwise.
     */
    private static void replaceAutoSize(Node node, PrecisionRectangle bounds, boolean useFigureForAutoSizeConstraint, Dimension providedDefaultSize, boolean recursive) {
        if (bounds.width == -1 || bounds.height == -1) {
            Dimension defaultSize = providedDefaultSize;
            if (providedDefaultSize == null) {
                // if there is no default size, we compute it from the given
                // node.
                EObject element = node.getElement();
                ViewQuery nodeQuery = new ViewQuery(node);
                if (nodeQuery.isFreeFormCompartment() || nodeQuery.isListCompartment()) {
                    defaultSize = new Dimension(ResizableCompartmentFigure.MIN_CLIENT_DP, ResizableCompartmentFigure.MIN_CLIENT_DP);
                    if (node.getChildren().isEmpty() && (nodeQuery.isListCompartment() || nodeQuery.isVerticalRegionContainerCompartment() || nodeQuery.isHorizontalRegionContainerCompartment())) {
                        // Add one margin border (even if empty)
                        defaultSize.expand(0, 1);
                    }
                } else if (element instanceof AbstractDNode abstractDNode) {
                    defaultSize = getDefaultSize(abstractDNode);
                }
            }
            if (useFigureForAutoSizeConstraint) {
                // Use the figure (if founded) to set width and height
                // instead of (-1, -1)
                Optional<GraphicalEditPart> optionalTargetEditPart = getGraphicalEditPart(node);
                // CHECKSTYLE:OFF
                if (optionalTargetEditPart.isPresent()) {
                    GraphicalEditPart graphicalEditPart = optionalTargetEditPart.get();
                    if (graphicalEditPart instanceof AbstractDiagramElementContainerEditPart abstractDiagramElementContainerEditPart) {
                        abstractDiagramElementContainerEditPart.forceFigureAutosize();
                        ((GraphicalEditPart) graphicalEditPart.getParent()).getFigure().validate();
                    }

                    Rectangle figureBounds = graphicalEditPart.getFigure().getBounds();
                    if (bounds.width == -1) {
                        bounds.setWidth(figureBounds.width);
                    }
                    if (bounds.height == -1) {
                        bounds.setHeight(figureBounds.height);
                    }
                } else {
                    // Diagram editor might be initializing and there is no edit
                    // part yet. For regions we might retrieve the previous
                    // known constraints in the GMF model by looking into the
                    // GMF location of the next region as they were computed
                    // from the location and size of the current region.
                    lookForNextRegionLocation(bounds, node);
                }
                // CHECKSTYLE:ON
            } else {
                // Compute the bounds of all children and use the lowest
                // one (y+height) for height and the rightmost one
                // (x+width) for width plus the margin.
                Rectangle childrenBounds = getChildrenBounds(node, recursive);
                // Add the potential shadow border size and the bottom right insets of the node (i.e. container)
                double shadowBorderSize = getShadowBorderSize(node);
                Dimension bottomRightInsets = getBottomRightInsets(node);
                // Do not add bottom right insets and shadow if there is at least one border node on the corresponding
                // side
                int borderNodesSides = PositionConstants.NONE;
                if (recursive) {
                    borderNodesSides = getBorderNodesSides(node, childrenBounds);
                }
                boolean isBorderNodeOnRightSide = (PositionConstants.RIGHT & borderNodesSides) == PositionConstants.RIGHT;
                boolean isBorderNodeOnBottomSide = (PositionConstants.BOTTOM & borderNodesSides) == PositionConstants.BOTTOM;
                childrenBounds.resize(isBorderNodeOnRightSide ? 0 : bottomRightInsets.width() + shadowBorderSize, isBorderNodeOnBottomSide ? 0 : bottomRightInsets.height() + shadowBorderSize);
                // Replace -1 by the new computed values
                if (bounds.width == -1) {
                    bounds.setPreciseWidth(defaultSize.preciseWidth());
                    double deltaWidth = childrenBounds.getRight().preciseX() - bounds.getRight().preciseX();
                    if (deltaWidth > 0) {
                        bounds.resize(deltaWidth, 0);
                    }
                }
                if (bounds.height == -1) {
                    bounds.setPreciseHeight(defaultSize.preciseHeight());
                    double deltaHeight = childrenBounds.getBottom().preciseY() - bounds.getBottom().preciseY();
                    if (deltaHeight > 0) {
                        bounds.resize(0, deltaHeight);
                    }
                }
            }

            if (bounds.width == -1) {
                bounds.setWidth(defaultSize.width);
            }
            if (bounds.height == -1) {
                bounds.setHeight(defaultSize.height);
            }
        }
    }

    private static int getBorderNodesSides(Node container, Rectangle containerChildrenBounds) {
        int result = PositionConstants.NONE;
        for (ListIterator<View> children = container.getChildren().listIterator(); children.hasNext(); /* */) {
            View child = children.next();
            if (child instanceof Node nodeChild && new NodeQuery(nodeChild).isBorderedNode()) {
                Rectangle borderNodeBounds = getAbsoluteBounds(nodeChild, true, false, false, false);
                if (borderNodeBounds.preciseX() == containerChildrenBounds.preciseX()) {
                    result = result | PositionConstants.LEFT;
                }
                if (borderNodeBounds.preciseY() == containerChildrenBounds.preciseY()) {
                    result = result | PositionConstants.TOP;
                }
                if (borderNodeBounds.preciseX() + borderNodeBounds.preciseWidth() == containerChildrenBounds.preciseX() + containerChildrenBounds.preciseWidth()) {
                    result = result | PositionConstants.RIGHT;
                }
                if (borderNodeBounds.preciseY() + borderNodeBounds.preciseHeight() == containerChildrenBounds.preciseY() + containerChildrenBounds.preciseHeight()) {
                    result = result | PositionConstants.BOTTOM;
                }
            }
        }
        return result;
    }

    private static void lookForNextRegionLocation(Rectangle bounds, Node node) {
        EObject element = node.getElement();
        if (element instanceof DDiagramElementContainer ddec && node.eContainer() instanceof Node nodeContainer) {
            DDiagramElementContainerExperimentalQuery query = new DDiagramElementContainerExperimentalQuery(ddec);
            boolean isRegion = query.isRegion();
            EList children = nodeContainer.getChildren();
            int currentIndex = children.indexOf(node);
            if (!(currentIndex != 0 && bounds.equals(new Rectangle(0, 0, -1, -1)))) {
                // We are not in the case of a new region insertion (in this
                // case, we use the default size)
                int nextIndex = currentIndex + 1;
                if (isRegion && nextIndex != 0 && nextIndex < children.size() && children.get(nextIndex) instanceof Node nextNode) {
                    int visualID = SiriusVisualIDRegistry.getVisualID(nextNode.getType());
                    if (DNodeContainer2EditPart.VISUAL_ID == visualID || DNodeListEditPart.VISUAL_ID == visualID || DNodeList2EditPart.VISUAL_ID == visualID) {
                        // DNodeContainerEditPart.VISUAL_ID == visualID is not
                        // checked as a region cannot be a
                        // DNodeContainerEditPart as it is directly contained by
                        // the diagram part.
                        LayoutConstraint layoutConstraint = nextNode.getLayoutConstraint();
                        if (layoutConstraint instanceof Location nextLocation) {
                            // Update only the parent stack direction if some
                            // layout has already been done.
                            if (bounds.width == -1 && query.isRegionInHorizontalStack() && nextLocation.getX() != 0) {
                                bounds.setWidth(nextLocation.getX() - bounds.x);
                            }
                            if (bounds.height == -1 && query.isRegionInVerticalStack() && nextLocation.getY() != 0) {
                                bounds.setHeight(nextLocation.getY() - bounds.y);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns a new Point representing the bottom right point of all bounds of children of this Node. Useful for Node
     * with size of -1x-1 to be more accurate (but it is still not necessarily the same size that draw2d).
     * 
     * @param container
     *            the node whose bottom right corner is to compute.
     * @param considerBorderNode
     *            true to consider border nodes when computing the bottom right corner point, false otherwise.
     * 
     * @return Point at the bottom right of the rectangle
     */
    private static Rectangle getChildrenBounds(Node container, boolean considerBorderNodes) {
        Rectangle result = null;
        if (container.getChildren().isEmpty()) {
            result = new PrecisionRectangle();
        }
        ViewQuery containerViewQuery = new ViewQuery(container);
        if (containerViewQuery.isListContainer() || containerViewQuery.isListCompartment() || containerViewQuery.isVerticalRegionContainerCompartment()) {
            if (!container.getChildren().isEmpty()) {
                Node lastChild = getLastChild(container, considerBorderNodes);
                result = getAbsoluteBounds(lastChild, true, false, true, false);
            }
        } else {
            for (ListIterator<View> children = container.getChildren().listIterator(); children.hasNext(); /* */) {
                View child = children.next();
                // The border nodes are ignored, except if it is expected to consider it (auto-size of a container with
                // children having border nodes)
                if (child instanceof Node nodeChild && (considerBorderNodes || !(new NodeQuery(nodeChild).isBorderedNode()))) {
                    Rectangle childAbsoluteBounds = getAbsoluteBounds(nodeChild, true, false, true, false);
                    if (result == null) {
                        result = childAbsoluteBounds.getCopy();
                    } else {
                        // Make union of the child bounds and its parent bounds
                        result.union(childAbsoluteBounds);
                    }
                }
            }
        }
        return result;
    }

    private static Node getLastChild(Node container, boolean considerBorderNodes) {
        for (int i = container.getChildren().size() - 1; i >= 0; i--) {
            Node currentNode = (Node) container.getChildren().get(i);
            if (considerBorderNodes || !new NodeQuery(currentNode).isBorderedNode()) {
                return currentNode;
            }
        }
        return null;
    }

    private static Dimension getDefaultSize(AbstractDNode abstractDNode) {
        Dimension defaultSize = new Dimension(-1, -1);
        if (abstractDNode instanceof DNode dNode) {
            defaultSize = new DNodeQuery(dNode).getDefaultDimension();
        } else if (abstractDNode instanceof DNodeContainer dNodeContainer) {
            defaultSize = new DNodeContainerQuery(dNodeContainer).getDefaultDimension();
        } else if (abstractDNode instanceof DNodeList) {
            defaultSize = LayoutUtils.NEW_DEFAULT_CONTAINER_DIMENSION;
        }
        return defaultSize;
    }

    /**
     * Return an option with the editPart corresponding to the <code>view</code> in the current diagram or an empty
     * Option if there is no corresponding editPart.
     * 
     * @param view
     *            The view element that is searched
     * @return The optional corresponding edit part.
     */
    public static Optional<GraphicalEditPart> getGraphicalEditPart(View view) {
        if (view != null) {
            Diagram gmfDiagram = view.getDiagram();
            // Try the active editor first (most likely case in practice)
            IEditorPart editor = EclipseUIUtil.getActiveEditor();
            if (isEditorFor(editor, gmfDiagram)) {
                return getGraphicalEditPart(view, (DiagramEditor) editor);
            } else if (gmfDiagram.getElement() instanceof DDiagram dDiagram) {
                // Otherwise check all active Sirius editors
                for (IEditingSession uiSession : SessionUIManager.INSTANCE.getUISessions()) {
                    DialectEditor dialectEditor = uiSession.getEditor(dDiagram);
                    if (isEditorFor(dialectEditor, gmfDiagram)) {
                        return getGraphicalEditPart(view, (DiagramEditor) dialectEditor);
                    }
                }
            }
        }
        return Optional.empty();
    }

    private static boolean isEditorFor(IEditorPart editor, Diagram diagram) {
        return editor instanceof DiagramEditor diagramEditor && diagramEditor.getDiagram() == diagram;
    }

    /**
     * Return an option with the editPart corresponding to the <code>view</code> in the current diagram or an empty
     * Option if there is no corresponding editPart.
     * 
     * @param view
     *            The view element that is searched
     * @param editor
     *            the editor where looking for the edit part.
     * @return The optional corresponding edit part.
     */
    public static Optional<GraphicalEditPart> getGraphicalEditPart(View view, DiagramEditor editor) {
        Optional<GraphicalEditPart> result = Optional.empty();
        final Map<?, ?> editPartRegistry = editor.getDiagramGraphicalViewer().getEditPartRegistry();
        final EditPart targetEditPart = (EditPart) editPartRegistry.get(view);
        if (targetEditPart instanceof GraphicalEditPart graphicalEditPart) {
            result = Optional.of(graphicalEditPart);
        }
        return result;
    }

    /**
     * Get the points list computed from GMF bendpoints according to source side for the <code>edgeEditPart</code>.
     * 
     * @param edgeEditPart
     *            The concerned edge edit part.
     * @return Points list
     * @throws IllegalArgumentException
     *             when the edgeEditPart is not as expected
     */
    public static List<Point> getPointsFromSource(ConnectionEditPart edgeEditPart) throws IllegalArgumentException {
        if (edgeEditPart.getModel() instanceof Edge gmfEdge && edgeEditPart.getFigure() instanceof Connection connectionFigure) {
            List<Point> result = new ArrayList<>();
            Point srcAnchorLoc = connectionFigure.getSourceAnchor().getReferencePoint();
            connectionFigure.translateToRelative(srcAnchorLoc);

            RelativeBendpoints bp = (RelativeBendpoints) gmfEdge.getBendpoints();
            for (int i = 0; i < bp.getPoints().size(); i++) {
                RelativeBendpoint rbp = (RelativeBendpoint) bp.getPoints().get(i);
                Point fromSrc = srcAnchorLoc.getTranslated(rbp.getSourceX(), rbp.getSourceY());
                result.add(fromSrc);
            }
            return result;
        }
        throw new IllegalArgumentException(Messages.GMFHelper_invalidEdgeModelAndFigure);
    }

    /**
     * Get the points list computed from GMF bendpoints according to target side for the <code>edgeEditPart</code>.
     * 
     * @param edgeEditPart
     *            The concerned edge edit part.
     * @return Points list
     * @throws IllegalArgumentException
     *             when the edgeEditPart is not as expected
     */
    public static List<Point> getPointsFromTarget(ConnectionEditPart edgeEditPart) throws IllegalArgumentException {
        if (edgeEditPart.getModel() instanceof Edge gmfEdge && edgeEditPart.getFigure() instanceof Connection connectionFigure) {
            List<Point> result = new ArrayList<>();
            Point tgtAnchorLoc = connectionFigure.getTargetAnchor().getReferencePoint();
            connectionFigure.translateToRelative(tgtAnchorLoc);

            RelativeBendpoints bp = (RelativeBendpoints) gmfEdge.getBendpoints();
            for (int i = 0; i < bp.getPoints().size(); i++) {
                RelativeBendpoint rbp = (RelativeBendpoint) bp.getPoints().get(i);
                Point fromTgt = tgtAnchorLoc.getTranslated(rbp.getTargetX(), rbp.getTargetY());
                result.add(fromTgt);
            }
            return result;
        }
        throw new IllegalArgumentException(Messages.GMFHelper_invalidEdgeModelAndFigure);
    }

    /**
     * Compute the size of a label. This method uses "UI data" to retrieve the label size. It must be called in UI
     * thread to have right result, otherwise <code>defaultDimension</code> will be used.
     * 
     * @param node
     *            the node, corresponding to a DNodeNameEditPart, whose size to compute.
     * @param defaultDimension
     *            the default dimension to use if it is not possible to get a "real" size
     * @return the size of the label.
     */
    public static Dimension getLabelDimension(Node node, Dimension defaultDimension) {
        Dimension labelSize = defaultDimension;
        ViewQuery viewQuery = new ViewQuery(node);
        EObject element = node.getElement();
        if (element instanceof DDiagramElement dDiagramElement) {
            org.eclipse.sirius.viewpoint.Style siriusStyle = dDiagramElement.getStyle();
            if (!new DDiagramElementQuery(dDiagramElement).isLabelHidden() && siriusStyle instanceof BasicLabelStyle bls) {
                String fontName = (String) viewQuery.getDefaultValue(NotationPackage.Literals.FONT_STYLE__FONT_NAME);
                Optional<Style> optionalStyle = getFontStyleOf(node);
                if (optionalStyle.isPresent() && optionalStyle.get() instanceof FontStyle fontStyle) {
                    String currentFontName = fontStyle.getFontName();
                    if (currentFontName != null && !currentFontName.isEmpty()) {
                        // Use the defined font name in the node if it is defined.
                        fontName = currentFontName;
                    }
                }
                Font defaultFont = VisualBindingManager.getDefault().getFontFromLabelStyle(bls, fontName);
                try {
                    labelSize = FigureUtilities.getStringExtents(dDiagramElement.getName(), defaultFont);
                    if (bls.isShowIcon()) {
                        // Also consider the icon size
                        Dimension iconDimension = getIconDimension(dDiagramElement, bls);
                        labelSize.setHeight(Math.max(labelSize.height(), iconDimension.height));
                        labelSize.setWidth(labelSize.width() + ICON_TEXT_GAP + iconDimension.width);
                    }
                } catch (SWTException e) {
                    // Probably an "Invalid thread access" (FigureUtilities
                    // creates a new Shell to compute the label size). So in
                    // this case, we use the default size.
                }
            }
        }
        return labelSize;
    }

    /**
     * Get the font style to use for this node. As in
     * {@link org.eclipse.sirius.diagram.ui.edit.api.part.DiagramNameEditPartOperation#getFontStyle(IDiagramNameEditPart)},
     * the parent style is used if no one is defined for the node.
     * 
     * @param node
     *            The node to use
     * @return The font style to use for this node
     */
    private static Optional<Style> getFontStyleOf(Node node) {
        Style style = node.getStyle(NotationPackage.eINSTANCE.getFontStyle());
        if (style == null && node.eContainer() instanceof View container) {
            style = container.getStyle(NotationPackage.eINSTANCE.getFontStyle());
        }
        return Optional.ofNullable(style);
    }

    private static Dimension getIconDimension(DSemanticDecorator dSemanticDecorator, BasicLabelStyle bls) {
        ImageDescriptor descriptor = null;
        EObject target = dSemanticDecorator.getTarget();
        if (!StringUtil.isEmpty(bls.getIconPath())) {
            String iconPath = bls.getIconPath();
            final File imageFile = FileProvider.getDefault().getFile(new Path(iconPath));
            if (imageFile != null && imageFile.exists() && imageFile.canRead()) {
                try {
                    descriptor = DiagramUIPlugin.Implementation.findImageDescriptor(imageFile.toURI().toURL());
                } catch (MalformedURLException e) {
                    // Do nothing here
                }
            }
        } else if (target != null) {
            final IItemLabelProvider labelProvider = (IItemLabelProvider) DiagramUIPlugin.getPlugin().getItemProvidersAdapterFactory().adapt(target, IItemLabelProvider.class);
            if (labelProvider != null) {
                descriptor = ExtendedImageRegistry.getInstance().getImageDescriptor(labelProvider.getImage(target));
            }
        }

        if (descriptor == null) {
            descriptor = ImageDescriptor.getMissingImageDescriptor();
        }
        Image icon = DiagramUIPlugin.getPlugin().getImage(descriptor);
        return new Dimension(icon.getBounds().width, icon.getBounds().height);
    }

    /**
     * Get all incoming and outgoing edges of this <code>view</code> or of all of its children.
     * 
     * @param view
     *            the concern view
     * @return list of edges
     */
    private static List<Edge> getIncomingOutgoingEdges(View view) {
        List<Edge> edgesToDelete = new ArrayList<>();
        edgesToDelete.addAll(view.getSourceEdges());
        edgesToDelete.addAll(view.getTargetEdges());

        List<View> children = view.getChildren();
        for (View child : children) {
            edgesToDelete.addAll(getIncomingOutgoingEdges(child));
        }
        return edgesToDelete;
    }

    /**
     * Get all incoming and outgoing edges of all <code>views</code> or of all of their children.
     * 
     * @param view
     *            the concern views
     * @return list of edges
     */
    public static List<Edge> getAttachedEdges(Collection<View> views) {
        return views.stream().flatMap(view -> getIncomingOutgoingEdges(view).stream()).toList();
    }

    /**
     * Return all incoming and outgoing edges of all edges recursively
     * 
     * @param edges
     *            collection of edges for which we want all incoming and outgoing edges
     * @return all incoming and outgoing edges of all edges recursively
     */
    public static List<Edge> getAttachedEdgesRecursively(Collection<? extends View> edges) {
        var resursivlyAttachedEdges = new ArrayList<Edge>();
        var remainingEdges = new ArrayList<View>(edges);
        while (!remainingEdges.isEmpty()) {
            // Here we take view (take = remove from list), we put it aside (in removed).
            // Then, if the view has not already been processed:
            // we collect incoming and outgoing edges and we add these edges to the orphan edges list.
            View view = remainingEdges.stream().findAny().orElseThrow(); // get an element

            // Before removing this view, we must identify incoming or outgoing
            // edges of this view or of one of its children to delete them just
            // after. Indeed, an Edge without source (or target) must not exist.
            List<Edge> attachedEdges = getIncomingOutgoingEdges(view);
            // remove edges already present
            attachedEdges.removeAll(resursivlyAttachedEdges);
            attachedEdges.removeAll(remainingEdges);

            remainingEdges.addAll(attachedEdges);
            resursivlyAttachedEdges.addAll(attachedEdges);

            remainingEdges.removeIf(v -> view == v);
        }
        return resursivlyAttachedEdges;
    }

    /**
     * Return all attached notes/texts/representation links (i.e. PGE: pure graphical elements) of a view.
     * 
     * @return The list of attached PGE
     */
    public static List<Node> getAttachedPGE(View view) {
        // get all attached notes
        List<Edge> noteAttachments = getIncomingOutgoingEdges(view).stream() //
                .filter(GMFNotationHelper::isNoteAttachment).toList();

        return noteAttachments.stream().flatMap(edge -> Stream.of(edge.getSource(), edge.getTarget())).filter(attachedView -> { // all
                                                                                                                                // nodes
                                                                                                                                // linked
                                                                                                                                // to
                                                                                                                                // note
                                                                                                                                // attachment:
                                                                                                                                // filter
                                                                                                                                // notes/texts
            if (attachedView instanceof Node attachedNode) {
                return GMFNotationHelper.isNote(attachedNode) || GMFNotationHelper.isTextNote(attachedNode);
            } else {
                return false;
            }
        }).map(Node.class::cast).toList();
    }

    /**
     * Return all attached notes/texts/representation links (i.e. PGE: pure graphical elements) of all views.
     * 
     * @return The list of attached PGE
     */
    public static List<Node> getAttachedPGE(Collection<? extends View> views) {
        return views.stream().flatMap(view -> GMFHelper.getAttachedPGE(view).stream()).toList();
    }

    /**
     * Remove all notes/texts/representation links (i.e. PGE: pure graphical elements) attached to removed nodes and
     * without other attachment.
     * 
     * The PGE is removed if all attached element are removed. The PGE is hidden if all visible attached element are
     * removed. If the PGE has at least one remaining attached element, the PGE is not removed and note hidden.
     * 
     * @param candidatesPGE
     *            Collection of all candidate PGE
     */
    public static void deleteDetachedPGE(Collection<Node> candidatesPGE) {
        for (Node pureGraphicalElement : candidatesPGE) {
            List<Edge> sourceEdges = pureGraphicalElement.getSourceEdges();
            List<Edge> targetEdges = pureGraphicalElement.getTargetEdges();

            List<Edge> validEdges = Stream.concat(sourceEdges.stream(), targetEdges.stream()) //
                    .filter(edge -> edge.eContainer() != null).toList();

            // remove unattached notes/texts
            if (validEdges.isEmpty()) {
                EcoreUtil.remove(pureGraphicalElement);
            } else {
                Stream<Edge> visibleEdges = validEdges.stream().filter(View::isVisible);

                // hide notes/texts attached to invisible element
                if (visibleEdges.count() == 0) {
                    pureGraphicalElement.setVisible(false);
                }
            }
        }
    }

    /**
     * Return the list of elements filtered by visible connection.
     * 
     * This method take the collection of node <code>elements</code> and return a filtered list this collection. The
     * condition to keep an element are as follows:
     * <ul>
     * <li>there are no edges attached to element,</li>
     * <li>or all edges attached to element are hidden or blacklisted (in <code>excludedEdges</code>)</li>
     * </ul>
     * 
     * @param elements
     *            the initial collection to be filtered
     * @param excludedEdges
     *            the black list for edges we want to exclude
     * @return the filtered list
     */
    public static List<Node> getElementWithoutVisibleConnection(Collection<Node> elements, Collection<Edge> excludedEdges) {
        return elements.stream().filter(element -> {
            List<Edge> sourceEdges = element.getSourceEdges();
            List<Edge> targetEdges = element.getTargetEdges();

            // All incoming and outgoing edges of element
            Stream<Edge> edges = Stream.concat(sourceEdges.stream(), targetEdges.stream());

            // Filter on edges by visibility attribute and excluded edges
            // Return node with none attached edges (except excluded edges or hidden edges)
            return edges.noneMatch(edge -> edge.isVisible() && !excludedEdges.contains(edge));
        }).toList();
    }
}
