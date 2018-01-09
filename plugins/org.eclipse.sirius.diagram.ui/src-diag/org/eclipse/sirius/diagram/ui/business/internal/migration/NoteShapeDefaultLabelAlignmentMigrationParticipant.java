/*******************************************************************************
 * Copyright (c) 2017, 2018 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.business.internal.migration;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.gmf.runtime.notation.TextAlignment;
import org.eclipse.gmf.runtime.notation.TextStyle;
import org.eclipse.sirius.business.api.migration.AbstractRepresentationsFileMigrationParticipant;
import org.eclipse.sirius.business.api.query.DViewQuery;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.business.api.query.DDiagramGraphicalQuery;
import org.eclipse.sirius.diagram.ui.business.api.query.ViewQuery;
import org.eclipse.sirius.diagram.ui.internal.view.factories.SiriusNoteViewFactory;
import org.eclipse.sirius.ext.base.Option;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DView;
import org.osgi.framework.Version;

import com.google.common.collect.Iterables;

/**
 * Default label alignment of Note's shapes have been updated in GMF runtime 1.8.0 (see
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=432387). This migration participant restores the old default alignment
 * value.
 * 
 * @see org.eclipse.sirius.diagram.ui.internal.edit.parts.SiriusDescriptionCompartmentEditPart
 * @see org.eclipse.sirius.diagram.ui.internal.providers.SiriusNoteViewProvider
 * @see org.eclipse.sirius.diagram.ui.internal.view.factories.SiriusNoteViewFactory
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * 
 */
public class NoteShapeDefaultLabelAlignmentMigrationParticipant extends AbstractRepresentationsFileMigrationParticipant {

    /**
     * The Sirius version for which this migration is added.
     */
    private static final Version MIGRATION_VERSION = new Version("12.0.0.201704070000"); //$NON-NLS-1$

    @Override
    public Version getMigrationVersion() {
        return MIGRATION_VERSION;
    }

    @Override
    protected void postLoad(DAnalysis dAnalysis, Version loadedVersion) {
        if (loadedVersion.compareTo(MIGRATION_VERSION) < 0) {
            for (Shape noteShape : getNoteShapes(dAnalysis)) {
                EAnnotation specificStyles = noteShape.getEAnnotation(ViewQuery.SPECIFIC_STYLES);
                if (specificStyles == null) {
                    specificStyles = SiriusNoteViewFactory.createDefaultVerticalAlignmentEAnnotation();
                    noteShape.getEAnnotations().add(specificStyles);
                    for (TextStyle textStyle : Iterables.filter(noteShape.getStyles(), TextStyle.class)) {
                        if (TextAlignment.LEFT_LITERAL == textStyle.getTextAlignment()) {
                            textStyle.setTextAlignment(TextAlignment.CENTER_LITERAL);
                        }
                    }
                }

            }
        }
    }

    /**
     * Get the {@link DDiagram}s contained in the given resource to migrate.
     * 
     * @param dAnalysis
     *            The analysis of the resource to migrate.
     * @return the {@link DDiagram}s contained in the given resource to migrate.
     */
    private Collection<DDiagram> getDiagrams(DAnalysis dAnalysis) {
        Collection<DDiagram> diagrams = new HashSet<>();
        for (DView view : dAnalysis.getOwnedViews()) {
            Iterables.addAll(diagrams, Iterables.filter(new DViewQuery(view).getLoadedRepresentations(), DDiagram.class));
        }
        return diagrams;
    }

    /**
     * Get the {@link Shape}s with Note types contained in the given resource to migrate.
     * 
     * @param dAnalysis
     *            The analysis of the resource to migrate.
     * @return the {@link Shape}s with Note types contained in the given resource to migrate.
     */
    private Collection<Shape> getNoteShapes(DAnalysis dAnalysis) {
        Collection<Shape> shapes = new HashSet<>();
        for (DDiagram dDiagram : getDiagrams(dAnalysis)) {
            shapes.addAll(getNoteShapes(dDiagram));
        }
        return shapes;
    }

    /**
     * Get the {@link Shape}s with Note types contained in the given {@link DDiagram}.
     * 
     * @param dDiagram
     *            the given {@link DDiagram}.
     * @return the {@link Shape}s with Note types contained in the given {@link DDiagram}.
     */
    private Collection<Shape> getNoteShapes(DDiagram dDiagram) {
        Collection<Shape> shapes = new HashSet<>();
        DDiagramGraphicalQuery query = new DDiagramGraphicalQuery(dDiagram);
        Option<Diagram> gmfDiagram = query.getAssociatedGMFDiagram();
        if (gmfDiagram.some()) {
            for (Shape shape : Iterables.filter(gmfDiagram.get().getChildren(), Shape.class)) {
                if (ViewType.NOTE.equals(shape.getType())) {
                    shapes.add(shape);
                }
            }
        }
        return shapes;
    }
}
