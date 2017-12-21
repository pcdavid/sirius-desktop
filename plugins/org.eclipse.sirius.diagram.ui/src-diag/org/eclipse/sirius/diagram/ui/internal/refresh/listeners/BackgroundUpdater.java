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
package org.eclipse.sirius.diagram.ui.internal.refresh.listeners;

import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.common.ui.tools.api.util.EclipseUIUtil;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.ui.tools.internal.editor.DDiagramEditorImpl;
import org.eclipse.sirius.viewpoint.description.ColorDescription;
import org.eclipse.sirius.viewpoint.description.ComputedColor;
import org.eclipse.sirius.viewpoint.description.InterpolatedColor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * A ResourceSet listener to refresh the diagram background if it is defined by Interpolated or computed color.
 * 
 * @author jmallet
 */
public class BackgroundUpdater extends ResourceSetListenerImpl {

    private DDiagram dDiagram;

    private static final NotificationFilter FEATURES_TO_REFACTOR_BACKGROUND = NotificationFilter.NOT_TOUCH;

    /**
     * Default constructor.
     * 
     * @param domain
     *            {@link TransactionalEditingDomain}
     * @param dDiagram
     *            {@link DDiagram}.
     */
    public BackgroundUpdater(TransactionalEditingDomain domain, DDiagram dDiagram) {
        super(FEATURES_TO_REFACTOR_BACKGROUND);
        domain.addResourceSetListener(this);
        this.dDiagram = dDiagram;
    }

    /**
     * Reinit the background.
     */
    public static void refreshBackground() {
        EclipseUIUtil.displayAsyncExec(new Runnable() {
            @Override
            public void run() {
                IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                if (activeWorkbenchWindow != null) {
                    IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
                    if (activePage != null) {
                        IEditorPart activeEditor = activePage.getActiveEditor();
                        if (activeEditor instanceof DDiagramEditorImpl) {
                            ((DDiagramEditorImpl) activeEditor).getDiagramEditPart().refresh();
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean isPostcommitOnly() {
        return true;
    }

    /**
     * Forces a refresh of the toolbar if needed.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void resourceSetChanged(ResourceSetChangeEvent event) {
        if (dDiagram instanceof DSemanticDiagram) {
            ColorDescription backgroundColor = ((DSemanticDiagram) dDiagram).getDescription().getBackgroundColor();
            if (backgroundColor instanceof InterpolatedColor || backgroundColor instanceof ComputedColor) {
                refreshBackground();
            }
        }
    }

    /**
     * Dispose this listener.
     */
    public void dispose() {
        if (getTarget() != null) {
            getTarget().removeResourceSetListener(this);
        }
    }
}
