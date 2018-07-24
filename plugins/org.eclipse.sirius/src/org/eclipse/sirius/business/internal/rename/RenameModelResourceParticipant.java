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
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;
import org.eclipse.ltk.core.refactoring.participants.ResourceChangeChecker;

/**
 * A participant to participate in refactorings that rename elements.
 * 
 * @author <a href="mailto:jessy.mallet@obeo.fr">Jessy MALLET</a>
 *
 */
public class RenameModelResourceParticipant extends RenameParticipant {

    /**
     * URI changes detected during renaming.
     */
    private Set<URIChange> changes = new HashSet<URIChange>();

    /**
     * Constructor.
     */
    public RenameModelResourceParticipant() {
    }

    @Override
    protected boolean initialize(Object element) {
        if (element instanceof IResource) {
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "Model Resource Rename"; //$NON-NLS-1$
    }

    @Override
    public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context) throws OperationCanceledException {
        changes = new HashSet<URIChange>();

        // Store resource directly affected by the renaming
        ResourceChangeChecker checker = context.getChecker(ResourceChangeChecker.class);
        IResourceChangeDescriptionFactory deltaFactory = checker.getDeltaFactory();
        IResourceDelta[] affectedChildren = deltaFactory.getDelta().getAffectedChildren();
        collectAffectedModels(affectedChildren);

        return new RefactoringStatus();
    }

    /**
     * Get resource directly changed by the rename.
     * 
     * @param affectedChildren
     *            resource tree affected by the change, either containing resource renamed or renamed it self.
     */
    private void collectAffectedModels(IResourceDelta[] affectedChildren) {

        for (IResourceDelta resourceDelta : affectedChildren) {
            if (resourceDelta.getResource() instanceof IFile && resourceDelta.getResource().getFullPath() != null) {

                InMemoryResourceURIChange.appendInMemoryURIChanges(changes, resourceDelta);

            } else if (resourceDelta.getMovedToPath() == null) {
                // main resource is not directly affected, search on its children
                collectAffectedModels(resourceDelta.getAffectedChildren());
            }
        }

    }

    @Override
    public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
        // TODO do not create change if session is loaded/modified
        return InMemoryResourceURIChange.toRefactoringChange(changes, pm);
    }

}
