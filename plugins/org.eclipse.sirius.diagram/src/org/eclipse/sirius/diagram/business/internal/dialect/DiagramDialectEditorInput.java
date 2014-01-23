package org.eclipse.sirius.diagram.business.internal.dialect;

import org.eclipse.emf.common.util.URI;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.ui.business.api.session.SessionEditorInput;
import org.eclipse.sirius.viewpoint.DDiagram;

/**
 * A {@link DDiagram} {@link SessionEditorInput}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 * 
 */
public class DiagramDialectEditorInput extends SessionEditorInput {

    /**
     * The {@link DDiagram}.
     */
    private DDiagram diagram;

    /**
     * Create a new SessionEditorInput with the current session and ui session.
     * 
     * @param uri
     *            element URI.
     * @param name
     *            name of the editor.
     * @param session
     *            the current session.
     * @param the
     *            {@link DDiagram}
     */
    public DiagramDialectEditorInput(final URI uri, final String name, final Session session, final DDiagram diagram) {
        super(uri, name, session);
        this.diagram = diagram;
    }

    /**
     * Gets the attached {@link DDiagram} and forget it.
     * 
     * @return the attached {@link DDiagram}
     */
    public DDiagram getAndForgetDiagram() {
        final DDiagram res = diagram;
        diagram = null;
        return res;
    }

}
