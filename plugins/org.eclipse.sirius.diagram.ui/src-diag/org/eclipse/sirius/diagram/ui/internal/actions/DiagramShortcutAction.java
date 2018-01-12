/*******************************************************************************
 * Copyright (c) 2009, 2015 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.internal.actions;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.gmf.runtime.notation.ShapeStyle;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.ui.business.api.view.SiriusGMFHelper;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;

class ExtendedViewType extends ViewType {
    /**
     * the note semantic hint
     */
    public static String HYPERLINK = "Hyperlink"; //$NON-NLS-1$

}

/**
 * An external action which marks diagram elements as "pinned" so they are not moved during automatic layout.
 * 
 * @author smonnier
 */
public class DiagramShortcutAction implements IExternalJavaAction {

    @Override
    public boolean canExecute(final Collection<? extends EObject> selections) {
        return selections.size() == 1;
    }

    @Override
    public void execute(final Collection<? extends EObject> selections, final Map<String, Object> parameters) {
        final Object viewObj = parameters.get("view"); //$NON-NLS-1$
        if (viewObj instanceof DDiagramElement) {
            DDiagramElement dDiagramElement = (DDiagramElement) viewObj;
            EObject sel = selections.iterator().next();
            Session session = SessionManager.INSTANCE.getSession(sel);
            View gmfView = SiriusGMFHelper.getGmfView(dDiagramElement, session);

            // GMFNotationHelper.createNote(gmfView, "noteText");
            final Node note = ViewService.createNode(gmfView, ViewType.NOTE, PreferencesHint.USE_DEFAULTS);

            // TODO addition of a style to have the link look like an hyperlink
            // TODO addition of a RepresentationDescriptor in EAnnotation to point the targeted diagram

            if (note instanceof Shape) {
                ((Shape) note).setDescription("hyperlink_to_diagram"); //$NON-NLS-1$
            }

            ShapeStyle createShapeStyle = NotationFactory.eINSTANCE.createShapeStyle();
            createShapeStyle.setDescription("hyperlink_to_diagram"); //$NON-NLS-1$
            note.getStyles().add(createShapeStyle);

        }
    }
}
