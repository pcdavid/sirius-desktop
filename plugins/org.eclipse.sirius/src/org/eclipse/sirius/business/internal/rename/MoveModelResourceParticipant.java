/*******************************************************************************
 * Copyright (c) 2018 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.internal.rename;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.mapping.IResourceChangeDescriptionFactory;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.MoveParticipant;
import org.eclipse.ltk.core.refactoring.participants.ResourceChangeChecker;

/**
 * A participant to participate in refactorings that move elements.
 * 
 * @author <a href="mailto:jessy.mallet@obeo.fr">Jessy MALLET</a>
 *
 */
public class MoveModelResourceParticipant extends MoveParticipant {

    /**
     * URI changes detected during renaming.
     */
    private Set<URIChange> changes = new HashSet<URIChange>();

    @Override
    protected boolean initialize(Object element) {
        if (element instanceof IResource) {
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "Model Resource Move"; //$NON-NLS-1$
    }

    @Override
    public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context) throws OperationCanceledException {

        // Store resource directly affected by the move
        ResourceChangeChecker checker = context.getChecker(ResourceChangeChecker.class);
        IResourceChangeDescriptionFactory deltaFactory = checker.getDeltaFactory();
        IResourceDelta[] affectedChildren = deltaFactory.getDelta().getAffectedChildren();

        return verifyAffectedChildren(affectedChildren);
    }

    /**
     * Get resource directly changed by the move.
     * 
     * @param affectedChildren
     *            resource tree affected by the change, either containing resource moved or moved it self.
     */
    private RefactoringStatus verifyAffectedChildren(IResourceDelta[] affectedChildren) {

        for (IResourceDelta resourceDelta : affectedChildren) {
            if (resourceDelta.getResource() instanceof IFile && resourceDelta.getResource().getFullPath() != null) {
                InMemoryResourceURIChange.appendInMemoryURIChanges(changes, resourceDelta);

            } else {
                return verifyAffectedChildren(resourceDelta.getAffectedChildren());
            }
        }

        return new RefactoringStatus();
    }

    @Override
    public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
        return InMemoryResourceURIChange.toRefactoringChange(changes, pm);
    }

}
