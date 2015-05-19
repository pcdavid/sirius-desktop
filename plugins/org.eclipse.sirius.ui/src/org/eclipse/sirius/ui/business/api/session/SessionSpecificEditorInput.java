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

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.ui.api.SiriusUiPlugin;
import org.eclipse.sirius.ui.business.api.editor.SpecificEditorInputTranformer;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.ui.IMemento;

/**
 * Specific editor input for specific editor providing model path, viewpoint URI
 * and representation description name.
 * 
 * @author mchauvin
 * @since 0.9.0
 */
public class SessionSpecificEditorInput extends SessionEditorInput {

    private static final String VIEWPOINT_URI = "VIEWPOINT_URI";

    private static final String REPRESENTATION_DESCRIPTION_NAME = "REPRESENTATION_DESCRIPTION_NAME";

    private static final String SEMANTIC_MODEL_PATH = "SEMANTIC_MODEL_PATH";

    private IPath mySemanticModelPath;

    private URI mySiriusURI;

    private String myRepresentationDescriptionName;

    /**
     * Create a new SessionEditorInput with a memento.
     * 
     * @param memento
     *            a bit of information kept by the platform.
     */
    public SessionSpecificEditorInput(final IMemento memento) {
        super(memento);
    }

    /**
     * Create a new SessionSpecificEditorInput with the current session and ui
     * session.
     * 
     * @param uri
     *            element URI.
     * @param name
     *            name of the editor.
     * @param session
     *            the current session.
     */
    public SessionSpecificEditorInput(URI uri, String name, Session session) {
        super(uri, name, session);
    }

    /**
     * Init the editor input.
     * 
     * @param semanticModelPath
     *            the semantic model path
     * @param viewpointURI
     *            the viewpoint URI
     * @param representationDescriptionName
     *            the representation description name
     */
    public void init(final IPath semanticModelPath, final URI viewpointURI, final String representationDescriptionName) {
        this.mySemanticModelPath = semanticModelPath;
        this.mySiriusURI = viewpointURI;
        this.myRepresentationDescriptionName = representationDescriptionName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.session.SessionEditorInput#saveState(org.eclipse.ui.IMemento)
     */
    @Override
    public void saveState(IMemento memento) {
        super.saveState(memento);
        if (mySiriusURI != null) {
            memento.putString(VIEWPOINT_URI, mySiriusURI.toString());
        }
        if (myRepresentationDescriptionName != null) {
            memento.putString(REPRESENTATION_DESCRIPTION_NAME, myRepresentationDescriptionName);
        }
        if (mySemanticModelPath != null) {
            memento.putString(SEMANTIC_MODEL_PATH, mySemanticModelPath.toString());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.session.SessionEditorInput#loadState(org.eclipse.ui.IMemento)
     */
    @Override
    protected void loadState(IMemento memento) {
        super.loadState(memento);
        restoreValuesFromMemento(memento);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.session.SessionEditorInput#getSession(org.eclipse.emf.transaction.TransactionalEditingDomain,
     *      org.eclipse.ui.IMemento, org.eclipse.emf.common.util.URI)
     */
    @Override
    protected Session getSession(URI sessionModelURI) {
        final URI uriWithoutFragment = sessionModelURI.trimFragment();
        return createSessionFromURIAndMemento(uriWithoutFragment);
    }

    private Session createSessionFromURIAndMemento(final URI uri) {
        SpecificEditorInputTranformer tranformer = new SpecificEditorInputTranformer();
        tranformer.init(mySiriusURI, myRepresentationDescriptionName);
        try {
            final DRepresentation representation = tranformer.createSessionAndRepresentation(mySemanticModelPath, uri.toString());
            return SessionManager.INSTANCE.getSession(((DSemanticDecorator) representation).getTarget());
        } catch (final IOException exception) {
            SiriusUiPlugin.getPlugin().getLog().log(new Status(IStatus.ERROR, SiriusUiPlugin.ID, "Failing of EditorInput transformation.", exception));
        } catch (CoreException exception) {
            SiriusUiPlugin.getPlugin().getLog().log(exception.getStatus());
        }
        return null;
    }

    private void restoreValuesFromMemento(final IMemento memento) {
        mySemanticModelPath = new Path(memento.getString(SEMANTIC_MODEL_PATH));
        mySiriusURI = URI.createURI(memento.getString(VIEWPOINT_URI));
        myRepresentationDescriptionName = memento.getString(REPRESENTATION_DESCRIPTION_NAME);
    }
}
