/*******************************************************************************
 * Copyright (c) 2011, 2015 THALES GLOBAL SERVICES and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.tools.internal.commands.emf;

import java.util.Collection;
import java.util.Set;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CopyToClipboardCommand;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.ui.provider.Messages;
import org.eclipse.sirius.diagram.ui.tools.api.layout.SiriusLayoutDataManager;
import org.eclipse.sirius.diagram.ui.tools.internal.clipboard.SiriusClipboardManager;
import org.eclipse.sirius.diagram.ui.tools.internal.layout.data.extension.LayoutDataManagerRegistry;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * A copy command which is a proxy to {@link CopyToClipboardCommand} but set the
 * copy result to a viewpoint clipboard.
 *
 * @author mporhel
 */
public class CopyToSiriusClipboardCommand extends RecordingCommand implements AbstractCommand.NonDirtying {
    /**
     * The elements to copy.
     */
    private final Collection<DSemanticDecorator> dElementsToCopy = Sets.newHashSet();

    /**
     * The semantic elements to copy.
     */
    private final Collection<EObject> elementsToCopy = Sets.newHashSet();

    private final TransactionalEditingDomain domain;

    private Command copyCommand;

    private Set<IGraphicalEditPart> selectedEditParts;

    /**
     * Creates a new {@link CopyToSiriusClipboardCommand}.
     *
     * The links between elements and semanticElements will be copied. Others
     * references will target original objects.
     *
     * @param domain
     *            the editing domain.
     * @param dElements
     *            the elements to copy.
     * @param semanticElements
     *            the semantic elements to copy.
     * @param graphicalEditParts
     *            the copied edit parts.
     */
    public CopyToSiriusClipboardCommand(final TransactionalEditingDomain domain, final Collection<DSemanticDecorator> dElements, Collection<EObject> semanticElements,
            Set<IGraphicalEditPart> graphicalEditParts) {
        super(domain, Messages.CopyToSiriusClipboardCommand_label);
        this.domain = domain;
        this.dElementsToCopy.addAll(dElements);
        this.elementsToCopy.addAll(semanticElements);
        this.selectedEditParts = graphicalEditParts;
    }

    /**
     * This returns the collection of objects to be copied to the clipboard.
     *
     * @return the object to copy.
     */
    public Collection<?> getSourceObjects() {
        return Lists.newArrayList(Iterables.concat(dElementsToCopy, elementsToCopy));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean prepare() {
        if (!getSourceObjects().isEmpty()) {
            copyCommand = CopyToClipboardCommand.create(domain, getSourceObjects());
        }
        return copyCommand != null ? copyCommand.canExecute() : true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doExecute() {
        if (copyCommand != null) {
            copyCommand.execute();
        } else {
            domain.setClipboard(null);
        }
        DDiagram dDiagram = null;
        DSemanticDecorator firstDecorator = dElementsToCopy.iterator().next();
        if (firstDecorator instanceof DDiagram) {
            dDiagram = (DDiagram) firstDecorator;
        } else if (firstDecorator instanceof DDiagramElement) {
            dDiagram = ((DDiagramElement) firstDecorator).getParentDiagram();
        }
        if (dDiagram != null) {
            for (SiriusLayoutDataManager layoutDataManager : LayoutDataManagerRegistry.getAllSiriusLayoutDataManagers()) {
                layoutDataManager.clearLayoutData();
            }
            for (IGraphicalEditPart graphicalEditPart : selectedEditParts) {
                for (SiriusLayoutDataManager layoutDataManager : LayoutDataManagerRegistry.getSiriusLayoutDataManagers(dDiagram)) {
                    layoutDataManager.storeLayoutData(graphicalEditPart);
                }
            }
        }
        // Hook domain clipboard.
        SiriusClipboardManager.getInstance().setSiriusClipboard(domain);
    }

    /**
     * This gives an abbreviated name using this object's own class' name,
     * without package qualification, followed by a space separated list of
     * <tt>field:value</tt> pairs.
     *
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return copyCommand.toString();
    }
}
