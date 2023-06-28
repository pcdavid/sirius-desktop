/*******************************************************************************
 * Copyright (c) 2023 THALES GLOBAL SERVICES and others.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.Note;
import org.eclipse.sirius.diagram.ui.business.api.query.NodeQuery;
import org.eclipse.sirius.diagram.ui.provider.DiagramUIPlugin;
import org.eclipse.sirius.diagram.ui.tools.api.util.GMFNotationHelper;
import org.eclipse.sirius.diagram.ui.tools.internal.preferences.SiriusDiagramUiInternalPreferencesKeys;
import org.eclipse.sirius.ecore.extender.business.internal.accessor.ecore.EMFUtil;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;

/**
 * Result of GMF refresh:
 * 
 * This class contain list of created nodes, edges, and list of orphaned nodes and edges
 * 
 * This class provide some method for process created and orphaned nodes and edges
 * 
 * @author Laurent Redor
 */
public class CanonicalSynchronizerResult {

    /**
     * The created Node {@link View}s during the canonical refresh of the diagram.
     */
    Set<View> createdNodes;

    /**
     * The created Edge {@link View}s during the canonical refresh of the diagram.
     */
    List<Edge> createdEdges;

    /**
     * The orphaned Node {@link View}s detected during the canonical refresh of the diagram and to delete after a
     * potential reconciliation of pure graphical elements ({@link Note}, {@link Text}, NoteAttachment or
     * RepresentationLink).
     */
    List<View> orphanedNodes;

    /**
     * The orphaned Edge {@link View}s detected during the canonical refresh of the diagram and to delete after a
     * potential reconciliation of pure graphical elements ({@link Note}, {@link Text}, NoteAttachment or
     * RepresentationLink).
     */
    Set<Edge> orphanedEdges;

    /**
     * Note/Text attached to an orphaned view
     */
    Set<Node> partialOrphanedNotes;

    /*
     * Shortcut for the name of preference "Remove/Hide note when annoted element is hidden or remove"
     */
    private static String PREF_RM_NOTE_NAME = SiriusDiagramUiInternalPreferencesKeys.PREF_REMOVE_HIDE_NOTE_WHEN_ANNOTED_ELEMENT_HIDDEN_OR_REMOVE.name();

    public CanonicalSynchronizerResult() {
        createdNodes = new LinkedHashSet<View>();
        createdEdges = new ArrayList<Edge>();
        orphanedNodes = new ArrayList<View>();
        orphanedEdges = new HashSet<Edge>();
        partialOrphanedNotes = new HashSet<Node>();
    }

    public void addCreatedNodes(Set<View> createdNodesToAdd) {
        createdNodes.addAll(createdNodesToAdd);
    }

    public void addCreatedEdges(List<Edge> createdEdgesToAdd) {
        createdEdges.addAll(createdEdgesToAdd);
    }

    public void addOrphanedNodes(List<View> orphanedNodesToAdd) {
        orphanedNodes.addAll(orphanedNodesToAdd);
    }

    public void addOrphanedEdges(List<Edge> orphanedEdgesToAdd) {
        orphanedEdges.addAll(orphanedEdgesToAdd);
    }

    public Set<View> getCreatedNodes() {
        return createdNodes;
    }

    public List<Edge> getCreatedEdges() {
        return createdEdges;
    }

    /**
     * @return All created nodes and edges (need computation)
     */
    public Set<View> getCreatedViews() {
        var allViews = new HashSet<View>();
        allViews.addAll(createdNodes);
        allViews.addAll(createdEdges);
        return allViews;
    }

    public List<View> getOrphanedNodes() {
        return orphanedNodes;
    }

    public Set<Edge> getOrphanedEdges() {
        return orphanedEdges;
    }

    /**
     * Move all Notes Attachment of oldView (source or target is oldView) to newView.
     * 
     * Note: views can be any node or edge type
     * 
     * @param oldView
     *            The GMF view that have source and target note attachment
     * @param newView
     *            The GMF view that will have source and target note attachment of oldView
     */
    private void reconnectNoteAttachment(View oldView, View newView) {
        // copy List to avoid problem (read/write conflict)
        var sourceEdges = new ArrayList<Edge>(oldView.getSourceEdges());
        var targetEdges = new ArrayList<Edge>(oldView.getTargetEdges());

        sourceEdges.stream() //
                .filter(GMFNotationHelper::isNoteAttachment) //
                .forEach(edge -> edge.setSource(newView));
        targetEdges.stream() //
                .filter(GMFNotationHelper::isNoteAttachment) //
                .forEach(edge -> edge.setTarget(newView));
    }

    /**
     * Move all direct notes from GMF view fromView to view toView.
     */
    private void moveNotes(View fromView, View toView) {
        var children = new ArrayList<View>(fromView.getChildren());
        children.stream().filter(view -> { // filter notes and texts
            if (view instanceof Node node) {
                return GMFNotationHelper.isNote(node) || GMFNotationHelper.isTextNote(node);
            } else {
                return false;
            }
        }).map(Node.class::cast).forEach(note -> {
            toView.getPersistedChildren().add(note); // move
        });
    }

    /**
     * Return the part of container node that can contains notes/texts.
     * 
     * Depending of layout container returns:
     * <ul>
     * <li>Free From: the intern compartment</li>
     * <li>Horizontal/Vertical stack: the container node itself</li>
     * <li>List: nothing, list cannot contain notes or texts</li>
     * </ul>
     */
    private Optional<Node> getNoteContainer(Node node) {
        var query = new NodeQuery(node);
        if (query.isRegionContainer())
            return Optional.of(node);
        else
            return query.getFreeFormContainerCompartment();
    }

    /**
     * Move all direct notes/texts/representation links from container oldNode to container newNode.
     * 
     * If oldNode or newNode is not container, does nothing.
     * 
     * @param oldNode
     *            The GMF view that have note
     * @param newNode
     *            The GMF view that will have note of oldView
     */
    private void moveInternNotes(View oldView, View newView) {
        if (oldView instanceof Node oldNode && newView instanceof Node newNode) {
            // if oldNode have compartment and get it
            getNoteContainer(oldNode).ifPresent(compartmentOld -> {

                // if newNode have compartment and get it
                getNoteContainer(newNode).ifPresent(compartmentNew -> {

                    moveNotes(compartmentOld, compartmentNew);
                });
            });
        }
    }

    /**
     * Check intern notes/texts/representation links and notes attachment of orphanedView and move them in corresponding
     * view if found.
     */
    private void reconciliateOrphanedNode(View orphanedNode) {
        if (orphanedNode.getElement() instanceof DSemanticDecorator dDiagramElement) {
            // Search new view of moved element
            getCorrespondingView(dDiagramElement).ifPresent(correspondingView -> {
                // Change note attachments and contained notes from old view to new view
                reconnectNoteAttachment(orphanedNode, correspondingView);
                moveInternNotes(orphanedNode, correspondingView);
            });
        }
    }

    /**
     * Check intern notes/texts/representation links and notes attachment of all orphaned views and move them in
     * corresponding view if found.
     * 
     * Orphaned views is about to be removed, so, if there are notes/texts/representation links into or notes
     * attachment, there will be removed. But in case of move, there are removal and creation of view, so this method
     * move intern notes/texts/representation links and notes attachment to the new view.
     */
    public void reconciliateOrphanedNodes() {
        for (View orphanedNode : orphanedNodes) {
            // root
            reconciliateOrphanedNode(orphanedNode);

            // All children recursively
            EMFUtil.getTreeStream(orphanedNode, view -> {
                return new ArrayList<View>(view.getChildren());
            }).filter(view -> { // filter on DNode and DContainer
                if (view instanceof Node node) {
                    var query = new NodeQuery(node);
                    return (query.isNode() || query.isContainer());
                } else {
                    return false;
                }
            }).forEach(this::reconciliateOrphanedNode);
        }
    }

    /**
     * Check notes attachment of all orphaned edges and move them in corresponding edges if found.
     * 
     * Orphaned edges is about to be removed, so, if there are notes attachment on a edge, there will be removed. But in
     * case of move, there are removal and creation of edge, so this method move notes attachment to the new edges.
     */
    public void reconciliateOrphanedEdges() {
        for (Edge orphanedEdge : orphanedEdges) {
            if (orphanedEdge.getElement() instanceof DSemanticDecorator dDiagramElement) {
                // Search new view of moved element
                getCorrespondingView(dDiagramElement).ifPresent(correspondingView -> {
                    // Change note attachments from old view to new view
                    reconnectNoteAttachment(orphanedEdge, correspondingView);
                });
            }
        }
    }

    /**
     * Returns associated gmf view of Sirius Element `dDiagramElement` or target of Sirius Element `dDiagramElement` in
     * created views. If no corresponding view found, return `Optional.empty`.
     */
    private Optional<View> getCorrespondingView(DSemanticDecorator dDiagramElement) {
        return getUniqueCreatedViewForDDiagramElement(dDiagramElement) //
                .or(() -> getUniqueCreatedViewForSemanticElement(dDiagramElement.getTarget()));
    }

    /**
     * Returns associated gmf view of Sirius Element `dDiagramElement` in created views. If no corresponding view found,
     * return `Optional.empty`.
     */
    private Optional<View> getUniqueCreatedViewForDDiagramElement(DSemanticDecorator dDiagramElement) {
        List<View> candidates = new ArrayList<>();
        for (View view : this.getCreatedViews()) {
            if (dDiagramElement.equals(view.getElement())) {
                candidates.add(view);
                if (candidates.size() > 1) { // more than 1 view is ambiguous for reconciliation
                    break;
                }
            }
        }
        if (candidates.size() == 1) {
            return Optional.of(candidates.get(0));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Returns associated gmf view of real semantic element (not sirius element) of in created views. If no
     * corresponding view found, return `Optional.empty`.
     */
    private Optional<View> getUniqueCreatedViewForSemanticElement(EObject semanticElement) {
        List<View> candidates = new ArrayList<>();
        for (View view : this.getCreatedViews()) {
            if (view.getElement() instanceof DSemanticDecorator dDiagramElement && semanticElement.equals(dDiagramElement.getTarget())) {
                candidates.add(view);
                if (candidates.size() > 1) { // more than 1 view is ambiguous for reconciliation
                    break;
                }
            }
        }
        if (candidates.size() == 1) {
            return Optional.of(candidates.get(0));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Deletes all orphaned nodes (removed or moved nodes)
     */
    public void deleteOrphanedNodes() {
        orphanedNodes.stream().forEach(EcoreUtil::remove);
        orphanedNodes.clear();
    }

    /**
     * Deletes all orphaned edges (removed or moved edges)
     */
    public void deleteOrphanedEdges() {
        orphanedEdges.stream().forEach(EcoreUtil::remove);
        orphanedEdges.clear();
    }

    /**
     * Compute all edges adjacent to the nodes to be deleted and adds them to the list.
     */
    public void computeAttachedEdgeToNodes() {
        computeAttachedEdges(orphanedNodes);
    }

    /**
     * Compute all edges adjacent to the edges to be deleted and adds them to the list (and compute attached edge).
     */
    public void computeAttachedEdgeToEdges() {
        computeAttachedEdges(orphanedEdges);
    }

    private <T extends View> void computeAttachedEdges(Collection<T> views) {
        var removed = new HashSet<T>();
        while (!views.isEmpty()) {
            T view = views.stream().findAny().orElseThrow(); // get an element
            if (removed.add(view)) {
                // Before removing this view, we must identify incoming or outgoing
                // edges of this view or of one of its children to delete them just
                // after. Indeed, an Edge without source (or target) must not exist.
                List<Edge> edgesToDelete = getIncomingOutgoingEdges(view);
                this.addOrphanedEdges(edgesToDelete);
            }
            views.removeIf(v -> view == v);
        }
        views.addAll(removed);
    }

    /**
     * Get all incoming and outgoing edges of this <code>view</code> or of all of its children.
     * 
     * @param view
     *            the concern view
     * @return list of edges
     */
    private List<Edge> getIncomingOutgoingEdges(View view) {
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
     * Collect all attached notes or texts of view
     */
    private void collectAttachedNotes(View view) {
        // get all attached notes
        List<Edge> noteAttachments = getIncomingOutgoingEdges(view).stream() //
                .filter(GMFNotationHelper::isNoteAttachment).toList();

        noteAttachments.stream().flatMap(edge -> {
            return Stream.of(edge.getSource(), edge.getTarget());
        }).filter(attachedView -> { // all nodes linked to note attachment: filter notes/texts
            if (attachedView instanceof Node attachedNode) {
                return GMFNotationHelper.isNote(attachedNode) || GMFNotationHelper.isTextNote(attachedNode);
            } else {
                return false;
            }
        }).map(Node.class::cast).forEach(attachedNote -> { // for each note
            partialOrphanedNotes.add(attachedNote);
        });
    }

    /**
     * Collect all note attached to orphaned node.
     * 
     * Only if preference is enabled for remove note when remove attached element is enabled, otherwise this method does
     * nothing
     */
    public void collectNoteAttachedNode() {
        if (DiagramUIPlugin.getPlugin().getPreferenceStore().getBoolean(PREF_RM_NOTE_NAME)) {
            for (View orphanedNode : orphanedNodes) {
                collectAttachedNotes(orphanedNode);
            }
        }
    }

    /**
     * Collect all note attached to orphaned edges
     * 
     * Only if preference is enabled for remove note when remove attached element is enabled, otherwise this method does
     * nothing
     */
    public void collectNoteAttachedEdge() {
        if (DiagramUIPlugin.getPlugin().getPreferenceStore().getBoolean(PREF_RM_NOTE_NAME)) {
            for (Edge orphanedEdge : orphanedEdges) {
                collectAttachedNotes(orphanedEdge);
            }
        }
    }

    /**
     * Mark to remove all notes attached to nodes that will be removed.
     * 
     * Before calling this function, you need to collect all note attached to view that will be removed (using method
     * collectNoteAttachedNode and collectNoteAttachedEdge)
     * 
     * Only if preference is enabled for remove note when remove attached element is enabled, otherwise this method does
     * nothing
     */
    public void deleteOrphanedNotes() {
        if (DiagramUIPlugin.getPlugin().getPreferenceStore().getBoolean(PREF_RM_NOTE_NAME)) {
            for (Node note : partialOrphanedNotes) {
                List<Edge> sourceEdges = note.getSourceEdges();
                List<Edge> targetEdges = note.getTargetEdges();

                Stream<Edge> validEdges = Stream.concat(sourceEdges.stream(), targetEdges.stream()) //
                        .filter(edge -> edge.eContainer() != null);

                // remove unattached notes/texts
                if (validEdges.count() == 0) {
                    EcoreUtil.remove(note);
                }
            }
            partialOrphanedNotes.clear();
        }
    }
}
