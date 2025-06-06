/*******************************************************************************
 * Copyright (c) 2022, 2024 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.diagram.ui.business.internal.migration;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.LayoutConstraint;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.Size;
import org.eclipse.sirius.business.api.migration.AbstractRepresentationsFileMigrationParticipant;
import org.eclipse.sirius.business.api.query.DViewQuery;
import org.eclipse.sirius.common.tools.api.util.StringUtil;
import org.eclipse.sirius.diagram.CollapseFilter;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DDiagramElementContainer;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.ResizeKind;
import org.eclipse.sirius.diagram.description.style.WorkspaceImageDescription;
import org.eclipse.sirius.diagram.tools.api.DiagramPlugin;
import org.eclipse.sirius.diagram.ui.business.api.query.DDiagramGraphicalQuery;
import org.eclipse.sirius.diagram.ui.business.api.query.NodeQuery;
import org.eclipse.sirius.diagram.ui.business.internal.query.WorkspaceImageQuery;
import org.eclipse.sirius.diagram.ui.provider.Messages;
import org.eclipse.sirius.diagram.ui.tools.api.layout.LayoutUtils;
import org.eclipse.sirius.ext.base.Option;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.DView;
import org.eclipse.sirius.viewpoint.description.style.StyleDescription;
import org.osgi.framework.Version;

/**
 * This migration participant resizes nodes with Workspace Image style description. The size of some GMF nodes was
 * inconsistent with the size of their draw2D representations. See bug 576423.
 * 
 * @author Glenn Plouhinec
 *
 */
@SuppressWarnings("removal")
public class WorkspaceImageGMFBoundsMigrationParticipant extends AbstractRepresentationsFileMigrationParticipant {

    /**
     * Migration version.
     */
    public static final Version MIGRATION_VERSION = new Version("15.4.1.202403191723"); //$NON-NLS-1$

    /**
     * The previous migration version of this participant. This one introduced a wrong size migration for collapsed
     * nodes.
     */
    public static final Version PREVIOUS_MIGRATION_VERSION = new Version("15.0.0.202201261500"); //$NON-NLS-1$

    @Override
    public Version getMigrationVersion() {
        return MIGRATION_VERSION;
    }

    @SuppressWarnings({ "deprecation" })
    @Override
    protected void postLoad(DAnalysis dAnalysis, Version loadedVersion) {
        if (loadedVersion.compareTo(MIGRATION_VERSION) < 0) {
            // Whether the first version of this migration participant was already applied. In this case, we might need
            // to fix a wrong GMF bounds modification.
            boolean previousMigrationApplied = loadedVersion.compareTo(PREVIOUS_MIGRATION_VERSION) >= 0;

            StringBuilder sb = new StringBuilder(Messages.WorkspaceImageGMFBoundsMigrationParticipant_title);
            EList<DView> ownedViews = dAnalysis.getOwnedViews();
            boolean migrationOccurred = false;
            for (DView view : ownedViews) {
                List<DRepresentationDescriptor> loadedRepresentationsDescriptors = new DViewQuery(view).getLoadedRepresentationsDescriptors();
                for (DRepresentationDescriptor descriptor : loadedRepresentationsDescriptors) {
                    if (descriptor.getRepresentation() instanceof DDiagram) {
                        DDiagram dDiagram = (DDiagram) descriptor.getRepresentation();
                        DDiagramGraphicalQuery query = new DDiagramGraphicalQuery(dDiagram);
                        Option<Diagram> optionalGmfDiagram = query.getAssociatedGMFDiagram();
                        if (optionalGmfDiagram.some()) {
                            boolean migrationOccurredInCurrentDiag = resizeWorkspaceImageGmfBoundsOfDiagram(previousMigrationApplied, sb, dDiagram, optionalGmfDiagram.get());
                            migrationOccurred = migrationOccurredInCurrentDiag || migrationOccurred;
                        }
                    }
                }
            }
            if (migrationOccurred) {
                DiagramPlugin.getDefault().logInfo(sb.toString());
                migrationOccurred = false;
            }
        }
    }

    private boolean resizeWorkspaceImageGmfBoundsOfDiagram(boolean previousMigrationApplied, StringBuilder sb, DDiagram dDiagram, Diagram gmfDiagram) {
        boolean migrationOccurredInCurrentDiag = false;
        String representationName = StringUtil.EMPTY_STRING;
        TreeIterator<EObject> childIterator = gmfDiagram.eAllContents();
        while (childIterator.hasNext()) {
            EObject eObject = childIterator.next();
            if (eObject instanceof Node && ((Node) eObject).getLayoutConstraint() instanceof Size) {
                Node node = (Node) eObject;
                if (resizeWorkspaceImageGMFBounds(node, previousMigrationApplied)) {
                    migrationOccurredInCurrentDiag = true;
                    representationName = dDiagram.getName();
                }
            }
        }
        if (migrationOccurredInCurrentDiag) {
            sb.append(MessageFormat.format(Messages.WorkspaceImageGMFBoundsMigrationParticipant_GMFBoundsResized, representationName));
        }
        return migrationOccurredInCurrentDiag;
    }

    private boolean resizeWorkspaceImageGMFBounds(Node node, boolean previousMigrationApplied) {
        boolean resized = false;
        if (node.getElement() instanceof DNode) {
            DNode dnode = (DNode) node.getElement();
            if (dnode.getStyle() != null) {
                StyleDescription description = dnode.getStyle().getDescription();
                resized = resizeGMFNode(previousMigrationApplied, node, description, dnode.getWidth(), dnode.getHeight());
            }
        } else if (node.getElement() instanceof DDiagramElementContainer) {
            DDiagramElementContainer dDiagramElementContainer = (DDiagramElementContainer) node.getElement();
            if (dDiagramElementContainer.getStyle() != null) {
                StyleDescription description = dDiagramElementContainer.getStyle().getDescription();
                resized = resizeGMFNode(previousMigrationApplied, node, description, dDiagramElementContainer.getWidth(), dDiagramElementContainer.getHeight());
            }
        }
        return resized;
    }

    private boolean resizeGMFNode(boolean previousMigrationApplied, Node node, StyleDescription description, Integer diagramElementWidth, Integer diagramElementHeight) {
        boolean resized = false;
        LayoutConstraint layoutConstraint = node.getLayoutConstraint();
        if (description instanceof WorkspaceImageDescription && layoutConstraint instanceof Size) {
            Size nodeSize = (Size) layoutConstraint;
            WorkspaceImageDescription workspaceImageDescription = (WorkspaceImageDescription) description;
            WorkspaceImageQuery workspaceImageQuery = new WorkspaceImageQuery(workspaceImageDescription);
            Dimension fixedDimension = null;
            String sizeComputationExpression = workspaceImageDescription.getSizeComputationExpression();
            if (ResizeKind.NONE == workspaceImageDescription.getResizeKind().getValue()) {
                fixedDimension = handleUnauthorizedResize(diagramElementWidth, diagramElementHeight, workspaceImageQuery, sizeComputationExpression);
            } else {
                fixedDimension = handleAuthorizedResize(nodeSize, diagramElementWidth, diagramElementHeight, workspaceImageQuery, sizeComputationExpression);
            }
            if (fixedDimension != null) {
                NodeQuery nodeQuery = new NodeQuery(node);
                boolean isCollasped = nodeQuery.isCollapsed();
                if (isCollasped) {
                    resized = handleCollapsedResize(previousMigrationApplied, node, nodeSize, fixedDimension, nodeQuery);
                } else if (!previousMigrationApplied && (nodeSize.getHeight() != fixedDimension.height || nodeSize.getWidth() != fixedDimension.width)) {
                    nodeSize.setWidth(fixedDimension.width);
                    nodeSize.setHeight(fixedDimension.height);
                    resized = true;
                }
            }
        }
        return resized;
    }

    private Dimension handleUnauthorizedResize(Integer diagramElementWidth, Integer diagramElementHeight, WorkspaceImageQuery workspaceImageQuery, String sizeComputationExpression) {
        // If the resize is not authorized, reset the GMF node size according to DNode size or image size if
        // auto-sized.
        Dimension fixedDimension = null;
        if (!StringUtil.isEmpty(sizeComputationExpression) && "-1".equals(sizeComputationExpression.trim()) && workspaceImageQuery.doesImageExist()) { //$NON-NLS-1$
            // In this case, ie auto-size, use the real size of the image
            Dimension imageSize = workspaceImageQuery.getDefaultDimension();
            fixedDimension = new Dimension(imageSize);
        } else if (diagramElementWidth != null && diagramElementHeight != null) {
            // Otherwise, use the DNode size
            int width = diagramElementWidth * LayoutUtils.SCALE;
            int height = (int) (diagramElementWidth / workspaceImageQuery.getRatio() * LayoutUtils.SCALE);
            fixedDimension = new Dimension(width, height);
        }
        return fixedDimension;
    }

    private Dimension handleAuthorizedResize(Size nodeSize, Integer diagramElementWidth, Integer diagramElementHeight, WorkspaceImageQuery workspaceImageQuery, String sizeComputationExpression) {
        // If the resize is authorized, this migration changes the GMF node size only if the current GMF node
        // size is the image size (the problem caused by the bug).
        Dimension fixedDimension = null;
        if (!StringUtil.isEmpty(sizeComputationExpression) && !("-1".equals(sizeComputationExpression.trim()) && workspaceImageQuery.doesImageExist())) { //$NON-NLS-1$
            Dimension imageSize = workspaceImageQuery.getDefaultDimension();
            if (nodeSize.getWidth() == imageSize.width && nodeSize.getHeight() == imageSize.height) {
                if (diagramElementWidth != null && diagramElementHeight != null) {
                    int width = diagramElementWidth * LayoutUtils.SCALE;
                    int height = (int) (diagramElementWidth / workspaceImageQuery.getRatio() * LayoutUtils.SCALE);
                    fixedDimension = new Dimension(width, height);
                }
            }
        }
        return fixedDimension;
    }

    private boolean handleCollapsedResize(boolean previousMigrationApplied, Node node, Size nodeSize, Dimension fixedDimension, NodeQuery nodeQuery) {
        boolean resized = false;
        Optional<CollapseFilter> optionalCollapseFilter = getCollapseFilter(node);
        if (optionalCollapseFilter.isPresent()) {
            CollapseFilter collapseFilter = optionalCollapseFilter.get();
            if (previousMigrationApplied) {
                // The GMF size might have been directly modified instead of the Collapsed filter:
                Dimension collapsedSize = nodeQuery.getCollapsedSize();
                if (nodeSize.getHeight() != collapsedSize.height || nodeSize.getWidth() != collapsedSize.width) {
                    nodeSize.setWidth(collapsedSize.width);
                    nodeSize.setHeight(collapsedSize.height);
                    resized = true;
                }
            }
            if (collapseFilter.getHeight() != fixedDimension.height || collapseFilter.getWidth() != fixedDimension.width) {
                collapseFilter.setWidth(fixedDimension.width);
                collapseFilter.setHeight(fixedDimension.height);
                resized = true;
            }
        }
        return resized;
    }

    private Optional<CollapseFilter> getCollapseFilter(Node node) {
        //@formatter:off
        return Optional.ofNullable(node.getElement())
                .stream()
                .filter(DDiagramElement.class::isInstance)
                .map(DDiagramElement.class::cast)
                .map(DDiagramElement::getGraphicalFilters)
                .flatMap(Collection::stream)
                .filter(CollapseFilter.class::isInstance)
                .map(CollapseFilter.class::cast)
                .findFirst();
        //@formatter:on
    }
}
