/*******************************************************************************
 * Copyright (c) 2010 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ui.business.api.session;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.sirius.business.api.session.SessionListener;
import org.eclipse.sirius.ui.api.SiriusUiPlugin;
import org.eclipse.sirius.ui.business.api.dialect.DialectEditor;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.ViewpointPackage;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

/**
 * Adapter which update name of editors when diagram name change.
 * 
 * @author pdul
 */
public class EditorNameAdapter extends AdapterImpl {

    /**
     * Session associated to the adapter
     */
    private IEditingSession editingSession;

    /**
     * Create a new adapter to update the editor title when the diagram name
     * change.
     * 
     * @param editingSession
     *            the associated session
     */
    public EditorNameAdapter(final IEditingSession editingSession) {
        this.editingSession = editingSession;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.emf.ecore.util.EContentAdapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
     */
    @Override
    public void notifyChanged(final org.eclipse.emf.common.notify.Notification n) {
        Object notifier = n.getNotifier();

        if (notifier instanceof Adapter) {
            Adapter adapter = (Adapter) notifier;
            Notifier target = adapter.getTarget();
            if (target instanceof DRepresentation) {
                notifier = target;
            }
        }

        // Update session's editors if name property of DSemanticDiagram changed
        if (notifier instanceof DRepresentation && n.getFeatureID(DRepresentation.class) == ViewpointPackage.DREPRESENTATION__NAME) {
            final List<IEditorInput> mades = new ArrayList<IEditorInput>();
            for (IEditorPart editor : editingSession.getEditors()) {
                // Update editor
                if (editor instanceof SessionListener) {
                    ((SessionListener) editor).notify(org.eclipse.ui.IWorkbenchPart.PROP_TITLE);
                }

                // Update editor input if necessary
                if (editor.getEditorInput() instanceof SessionEditorInput && !mades.contains(editor.getEditorInput())) {
                    mades.add(editor.getEditorInput());
                    updateEditorInputName((SessionEditorInput) editor.getEditorInput(), editor);
                }
            }
        }

    }

    /**
     * Update the editor input name field
     * 
     * @param editorInput
     *            the editor input
     * @param editor
     *            the editor part
     */
    private void updateEditorInputName(final SessionEditorInput editorInput, final IEditorPart editor) {
        PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                editorInput.setName(editor.getTitle());
            }
        });
    }

    /**
     * Add this adapter to the DSemanticDiagram associated to the editor.
     * 
     * @param editor
     *            the editor
     */
    public void registerEditor(final DialectEditor editor) {
        final DRepresentation representation = editor.getRepresentation();
        if (representation != null) {
            representation.eAdapters().add(this);
        }
    }

    /**
     * Remove this adapter to the DSemanticDiagram associated to the editor.
     * 
     * @param editor
     *            the editor
     */
    public void unregisterEditor(final DialectEditor editor) {
        final DRepresentation representation = editor.getRepresentation();
        if (representation != null) {
            if (representation.eAdapters().contains(this)) {
                // TODO remove this try/catch once the offline mode
                // will be supported
                try {
                    representation.eAdapters().remove(this);
                } catch (NullPointerException e) {
                    if (SiriusUiPlugin.getPlugin().isDebugging()) {
                        SiriusUiPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, SiriusUiPlugin.ID, "Error while closing the representation, the remote server may be unreachable."));
                    }
                }
            }
        }

    }

}
