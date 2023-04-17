/**
 * 
 */
package org.eclipse.sirius.diagram.ui.internal.refresh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.Note;
import org.eclipse.sirius.diagram.ui.tools.api.util.GMFNotationHelper;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;

import com.google.common.collect.Iterables;

/**
 * @author Laurent Redor
 *
 */
public class CanonicalSynchronizerResult {

    /**
     * The created {@link View}s during the canonical refresh of the diagram.
     */
    Set<View> createdViews;

    /**
     * The orphaned {@link View}s detected during the canonical refresh of the diagram and to delete after a potential
     * reconciliation of pure graphical elements ({@link Note}, {@link Text}, NoteAttachment or RepresentationLink).
     */
    List<View> orphanedViews;

    Map<EObject, Optional<View>> dDiagramElementToViewCache;

    Map<EObject, Optional<View>> semanticElementToViewCache;

    /**
     * Default constructor.
     */
    public CanonicalSynchronizerResult() {
        createdViews = new LinkedHashSet<>();
        orphanedViews = new ArrayList<View>();
        dDiagramElementToViewCache = new HashMap<>();
        semanticElementToViewCache = new HashMap<>();
    }

    public void addCreatedViews(Set<View> createdViewsToAdd) {
        createdViews.addAll(createdViewsToAdd);
    }

    public void addOrphanedViews(List<View> orphanedViewsToAdd) {
        orphanedViews.addAll(orphanedViewsToAdd);
    }

    public Set<View> getCreatedNodeViews() {
        return createdViews;
    }

    public void reconciliateOrphanedViews() {
        for (View orphanedView : orphanedViews) {
            if (orphanedView.isSetElement()) {
                // TODO : Maybe also check if element is DSemanticDecorator...
                // Check if the orphaned view has incoming or outgoing noteAttachments
                Set<Edge> edges = new HashSet<>();
                edges.addAll(orphanedView.getSourceEdges());
                edges.addAll(orphanedView.getTargetEdges());
                // Iterables.addAll(edges, Iterables.filter(node.getTargetEdges(), Edge.class));
                //
                // Stream.concat(orphanedView.getSourceEdges()., orphanedView.getTargetEdges())
                for (Edge edge : edges) {
                    if (GMFNotationHelper.isNoteAttachment(edge)) {
                        // Try to reconciliate with one of the created elements
                        // Firstly with the DDiagramElement (the DDiagramElement remains the same if the mapping is the
                        // same before and after the Sirius refresh
                        EObject dDiagramElement = orphanedView.getElement();
                        Optional<View> optionalCorrespondingView = getUniqueCreatedViewForDDiagramElement(dDiagramElement);
                        if (optionalCorrespondingView.isPresent()) {
                            if (orphanedView.equals(edge.getSource())) {
                                edge.setSource(optionalCorrespondingView.get());
                            } else {
                                edge.setTarget(optionalCorrespondingView.get());
                            }
                        } else if (dDiagramElement instanceof DSemanticDecorator) {
                            // Secondly with the semantic element
                            optionalCorrespondingView = getUniqueCreatedViewForSemanticElement(((DSemanticDecorator) dDiagramElement).getTarget());
                            if (optionalCorrespondingView.isPresent()) {
                                if (orphanedView.equals(edge.getSource())) {
                                    edge.setSource(optionalCorrespondingView.get());
                                } else {
                                    edge.setTarget(optionalCorrespondingView.get());
                                }
                            }
                        }
                        // TODO : If there something specific to do when there is no reconciliation (delete
                        // NoteAttachment
                        // ghost?)
                    }
                }
            }
            // Check if the orphaned view contains a Note, a Text or a LinkRepresentation
            // TODO:
        }
    }

    public Optional<View> getUniqueCreatedViewForDDiagramElement(EObject dDiagramElement) {
        Optional<View> result = dDiagramElementToViewCache.get(dDiagramElement);
        if (result == null) {
            List<View> candidates = new ArrayList<>();
            for (View view : createdViews) {
                if (dDiagramElement.equals(view.getElement())) {
                    candidates.add(view);
                    if (candidates.size() > 1) {
                        break;
                    }
                }
            }
            if (candidates.isEmpty() || candidates.size() > 1) {
                result = Optional.empty();
            } else {
                result = Optional.of(candidates.get(0));
            }
            dDiagramElementToViewCache.put(dDiagramElement, result);
        }
        return result;
    }

    public Optional<View> getUniqueCreatedViewForSemanticElement(EObject semanticElement) {
        Optional<View> result = semanticElementToViewCache.get(semanticElement);
        if (result == null) {
            List<View> candidates = new ArrayList<>();
            for (View view : createdViews) {
                if (view.getElement() instanceof DSemanticDecorator && semanticElement.equals(((DSemanticDecorator) view.getElement()).getTarget())) {
                    candidates.add(view);
                    if (candidates.size() > 1) {
                        break;
                    }
                }
            }
            if (candidates.isEmpty() || candidates.size() > 1) {
                result = Optional.empty();
            } else {
                result = Optional.of(candidates.get(0));
            }
            semanticElementToViewCache.put(semanticElement, result);
        }
        return result;
    }

    /**
     * Deletes a list of views. The views will be deleted <tt>if</tt> their semantic element has also been deleted.
     * 
     * @param views
     *            the {@link View}s to delete
     * @return <tt>true</tt> if the host editpart should be refreshed; either one one of the supplied views was deleted
     *         or has been reparented.
     */
    public void deleteOrphanedViews() {
        deleteViews(orphanedViews);
    }

    protected boolean deleteViews(Collection<? extends View> views) {
        for (View view : views) {
            // if (!(view instanceof Connector && "NoteAttachment".equals(view.getType()))) { //$NON-NLS-1$
                // Before removing this view, we must identify incoming or outgoing
                // edges of this view or of one of its children to delete them just
                // after. Indeed, an Edge without source (or target) must not exist.
                List<Edge> edgesToDelete = getIncomingOutgoingEdges(view);

                // org.eclipse.gmf.runtime.diagram.core.util.ViewUtil.destroy(v) is
                // no more needed, simply remove the view
                // from its container, DanglinRefRemovalTrigger will complete
                // the work. This prevents GMF to install its
                // CrossReferencerAdapter

                // TODO:
                // for (View edgeToDelete : edgesToDelete) {
                // if (edgeToDelete instanceof Connector && "NoteAttachment".equals(edgeToDelete.getType())) {
                // //$NON-NLS-1$
                // // Do not remove Note Attachment and let them as "ghost", ie without source or target. A
                // // reconciliation,
                // // or deletion, will be done later, in a case of a drag'n'drop of an element for example.
                // // Data is added to the Note Attachment to allow this reconciliation later
                // // NoteAttachmentCache.createAdapterOnNoteAttachment((Connector) edgeToDelete);
                // // TODO:
                // }
                // }

                EcoreUtil.remove(view);
                // Then remove incoming or outgoing edges (with the same rules for incoming or outgoing of thess edges
                deleteViews(edgesToDelete);
                // }
        }
        return !views.isEmpty();
    }

    /**
     * Get all incoming and outgoing edges of this <code>view</code> or of one of its children (except NoteAttachment).
     * 
     * @param view
     *            the concern view
     * @return list of edges
     */
    private List<Edge> getIncomingOutgoingEdges(View view) {
        List<Edge> edgesToDelete = new ArrayList<>();
        // edgesToDelete.addAll(view.getSourceEdges().stream().filter(Edge.class::isInstance).map(Edge.class::cast).filter(edge
        // -> edge.getType().equals("NoteAttachement")).collect(Collectors.toList())); //$NON-NLS-1$
        // edgesToDelete.addAll(view.getTargetEdges().stream().filter(Edge.class::isInstance).map(Edge.class::cast).filter(edge
        // -> edge.getType().equals("NoteAttachement")).collect(Collectors.toList())); //$NON-NLS-1$
        Iterables.addAll(edgesToDelete, Iterables.filter(view.getSourceEdges(), Edge.class));
        Iterables.addAll(edgesToDelete, Iterables.filter(view.getTargetEdges(), Edge.class));

        for (View child : getViewChildren(view)) {
            edgesToDelete.addAll(getIncomingOutgoingEdges(child));
        }
        return edgesToDelete;
    }

    private List<View> getViewChildren(final View current) {
        return new ArrayList<>(current.getChildren());
    }
}
