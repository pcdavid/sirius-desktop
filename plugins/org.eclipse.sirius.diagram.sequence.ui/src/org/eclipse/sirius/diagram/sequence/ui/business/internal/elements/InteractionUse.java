/*******************************************************************************
 * Copyright (c) 2010, 2012 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.sequence.ui.business.internal.elements;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.sequence.description.DescriptionPackage;
import org.eclipse.sirius.diagram.sequence.ui.business.internal.query.SequenceNodeQuery;
import org.eclipse.sirius.diagram.sequence.ui.business.internal.util.RangeSetter;
import org.eclipse.sirius.diagram.sequence.util.Range;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

/**
 * Represents an interaction use / reference.
 * 
 * @author pcdavid
 */
public class InteractionUse extends AbstractFrame {
    /**
     * The visual ID. Same as a normal bordered node.
     * 
     * see org.eclipse.sirius.diagram.internal.edit.parts.
     * DNodeContainerEditPart.VISUAL_ID
     */
    public static final int VISUAL_ID = 2002;

    /**
     * Predicate to check whether a Sirius DDiagramElement represents an
     * execution.
     */
    private static enum SiriusElementPredicate implements Predicate<DDiagramElement> {
        INSTANCE;

        public boolean apply(DDiagramElement input) {
            return AbstractSequenceElement.isSequenceDiagramElement(input, DescriptionPackage.eINSTANCE.getInteractionUseMapping());
        }

    }

    /**
     * Constructor.
     * 
     * @param node
     *            the GMF Node representing the interaction use.
     */
    InteractionUse(Node node) {
        super(node);
        Preconditions.checkArgument(InteractionUse.notationPredicate().apply(node), "The node does not represent an interaction use.");
    }

    /**
     * Returns a predicate to check whether a GMF View represents an execution.
     * 
     * @return a predicate to check whether a GMF View represents an execution.
     */
    public static Predicate<View> notationPredicate() {
        return new NotationPredicate(NotationPackage.eINSTANCE.getNode(), VISUAL_ID, InteractionUse.viewpointElementPredicate());
    }

    /**
     * Returns a predicate to check whether a Sirius DDiagramElement
     * represents an execution.
     * 
     * @return a predicate to check whether a Sirius DDiagramElement
     *         represents an execution.
     */
    public static Predicate<DDiagramElement> viewpointElementPredicate() {
        return SiriusElementPredicate.INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    public Range getVerticalRange() {
        return new SequenceNodeQuery(getNotationNode()).getVerticalRange();
    }

    /**
     * {@inheritDoc}
     */
    public void setVerticalRange(Range range) throws IllegalStateException {
        RangeSetter.setVerticalRange(this, range);
    }

    /**
     * {@inheritDoc}
     */
    public List<ISequenceEvent> getSubEvents() {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canChildOccupy(ISequenceEvent child, Range range) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canChildOccupy(ISequenceEvent child, Range range, List<ISequenceEvent> eventsToIgnore, Collection<Lifeline> lifelines) {
        return false;
    }

}
