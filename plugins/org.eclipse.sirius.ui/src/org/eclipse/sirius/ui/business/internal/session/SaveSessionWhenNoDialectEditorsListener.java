/*******************************************************************************
 * Copyright (c) 2011, 2017 THALES GLOBAL SERVICES and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ui.business.internal.session;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionStatus;
import org.eclipse.sirius.business.internal.session.danalysis.SaveSessionJob;
import org.eclipse.sirius.ui.business.api.preferences.SiriusUIPreferencesKeys;
import org.eclipse.sirius.ui.business.api.session.EditingSessionEvent;
import org.eclipse.sirius.ui.business.api.session.IEditingSession;
import org.eclipse.sirius.ui.business.api.session.SessionUIManager;
import org.eclipse.sirius.viewpoint.provider.SiriusEditPlugin;

/**
 * A listener to resource set change which save session if there is no dialect editor opens.
 * 
 * @author mchauvin
 */
public class SaveSessionWhenNoDialectEditorsListener extends ResourceSetListenerImpl {

    /**
     * The session from which Sirius resources modifications are listened to know if a save must be execute when no
     * editors are opened.
     */
    protected final Session session;

    private Job saveSessionJob;

    private boolean activation = true;

    /**
     * Create a new instance.
     * 
     * @param session
     *            the session.
     */
    public SaveSessionWhenNoDialectEditorsListener(Session session) {
        this.session = session;
    }

    /**
     * Register this listener.
     */
    public void register() {
        session.getTransactionalEditingDomain().addResourceSetListener(this);
    }

    /**
     * Unregister this listener.
     */
    public void unregister() {
        session.getTransactionalEditingDomain().removeResourceSetListener(this);
        saveSessionJob = null;
    }

    @Override
    public boolean isPostcommitOnly() {
        return true;
    }

    @Override
    public void resourceSetChanged(ResourceSetChangeEvent event) {
        super.resourceSetChanged(event);
        if (activation && newMode()) {
            final IEditingSession editingSession = SessionUIManager.INSTANCE.getUISession(session);
            if (session.isOpen() && editingSession != null && editingSession.getEditors().isEmpty() && SessionStatus.DIRTY.equals(session.getStatus())) {
                if (saveSessionJob == null || saveSessionJob.getState() == Job.NONE) {
                    saveSessionJob = new SaveSessionJob(session);
                    saveSessionJob.schedule();
                }
            }
        }
    }

    /**
     * Notify this listener.
     * 
     * @param event
     *            the event
     */
    public void notify(final EditingSessionEvent event) {
        switch (event) {
        case REPRESENTATION_ABOUT_TO_BE_CREATED_BEFORE_OPENING:
            activation = false;
            break;
        case REPRESENTATION_CREATED_BEFORE_OPENING:
            activation = true;
            break;
        default:
            break;
        }
    }

    private boolean newMode() {
        final IPreferenceStore preferenceStore = SiriusEditPlugin.getPlugin().getPreferenceStore();
        return preferenceStore != null && preferenceStore.getBoolean(SiriusUIPreferencesKeys.PREF_SAVE_WHEN_NO_EDITOR.name());
    }

}
