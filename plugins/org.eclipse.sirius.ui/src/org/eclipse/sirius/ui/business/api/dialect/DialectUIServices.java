/*******************************************************************************
 * Copyright (c) 2008 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ui.business.api.dialect;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.ui.business.api.session.SessionEditorInput;
import org.eclipse.sirius.ui.tools.api.actions.export.SizeTooLargeException;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationElement;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.sirius.viewpoint.description.RepresentationDescription;
import org.eclipse.ui.IEditorPart;

/**
 * User-interface centric services for dialects.
 * 
 * @author cbrun
 * 
 */
public interface DialectUIServices {

    /**
     * Return the list of new child descriptors provided on the Sirius type.
     * 
     * @return the list of new child descriptors provided on the Sirius type.
     */
    Collection<CommandParameter> provideNewChildDescriptors();

    /**
     * Return the list of creation tool descriptors provided for a given parent
     * feature.
     * 
     * @param feature
     *            the feature containing the creation tools.
     * @return the list of creation tool descriptors provided on creation tool
     *         container.
     */
    Collection<CommandParameter> provideRepresentationCreationToolDescriptors(Object feature);

    /**
     * Return the list of navigation tool descriptors provided for a given
     * parent feature.
     * 
     * @param feature
     *            the feature containing the navigation tools.
     * @return the list of navigation tool descriptors provided on navigation
     *         tool container.
     */
    Collection<CommandParameter> provideRepresentationNavigationToolDescriptors(Object feature);

    /**
     * Return the list of tool descriptors provided to appear in ToolSections.
     * 
     * @param object
     *            the context of the child creation.
     * 
     * @return the list of tool descriptors provided by the dialect.
     */
    Collection<CommandParameter> provideTools(EObject object);

    /**
     * Return the list of additional mapping descriptors to appear inside a
     * layer, which can depend on the representation type.
     * 
     * @param object
     *            the context of the child creation.
     * 
     * @return the list of additional mappings provided by the dialect.
     */
    Collection<CommandParameter> provideAdditionalMappings(EObject object);

    /**
     * Open an editor for the given representation (if only you know about this
     * kind of representation).
     * 
     * @param dRepresentation
     *            {@link DRepresentation} to open.
     * @param session
     *            the current {@link Session}.
     * @param monitor
     *            the {@link IProgressMonitor} to use
     * @return the opened editor.
     */
    IEditorPart openEditor(Session session, DRepresentation dRepresentation, IProgressMonitor monitor);

    /**
     * Gets the editor ID for the given representation.
     * @param dRepresentation
     *            {@link DRepresentation} to open.
     * 
     * @return the editor ID for the given representation if any, <code>null</code> otherwise.
     */
    String getEditorID(DRepresentation dRepresentation);
    
    /**
     * Gets the {@link SessionEditorInput} for the given {@link Session} and {@link DRepresentation}. 
     *  
     * @param session
     *            the current {@link Session}.
     * @param dRepresentation
     *            {@link DRepresentation} to open.
     * @return the {@link SessionEditorInput} for the given {@link Session} and {@link DRepresentation}, or <code>null</code> if none could be created
     */
    SessionEditorInput getEditorInput(Session session, DRepresentation dRepresentation);    
    
    /**
     * Sets the given {@link DRepresentationElement}s as current selection for
     * the editor.
     * 
     * @param dialectEditor
     *            the editor which needs a selection update.
     * @param selection
     *            the selection to set.
     */
    void setSelection(DialectEditor dialectEditor, List<DRepresentationElement> selection);

    /**
     * Asks for the selection of the given editor.
     * 
     * @param editor
     *            the editor to request.
     * @return the selected {@link DRepresentationElement}s
     */
    Collection<DSemanticDecorator> getSelection(DialectEditor editor);

    /**
     * Get the editor name.
     * 
     * @param representation
     *            the representation from which to retrieve the editor name
     * @return the editor name to use
     */
    String getEditorName(final DRepresentation representation);

    /**
     * Creates a new {@link AdapterFactory} used in the description editor for
     * the provided types.
     * 
     * @return a new {@link AdapterFactory} used in the description editor for
     *         the provided types.
     */
    AdapterFactory createAdapterFactory();

    /**
     * Tell whether the dialect is able to close this editor.
     * 
     * @param editorPart
     *            the editor to close.
     * @return true if the dialect manage such {@link IEditorPart}, false
     *         otherwise.
     */
    boolean canHandleEditor(IEditorPart editorPart);

    /**
     * Close an editor (if only you know about this kind of {@link IEditorPart})
     * and execute the specification operations.
     * 
     * @param editor
     *            the editor to close
     * @param save
     *            <code>true</code> to save the editor contents if required
     *            (recommended), and <code>false</code> to discard any unsaved
     *            changes
     * @return <code>true</code> if the editor was successfully closed, and
     *         <code>false</code> if the editor is still open
     */
    boolean closeEditor(IEditorPart editor, boolean save);

    /**
     * Check if this representation if managed by this editor.
     * 
     * @param representation
     *            representation representation to check.
     * @param editorPart
     *            the editor to check.
     * @return true if the editor manages the representation, false otherwise
     */
    boolean isRepresentationManagedByEditor(DRepresentation representation, IEditorPart editorPart);

    /**
     * Check if this representation description is managed by this editor.
     * 
     * @param representationDescription
     *            representation description to check.
     * @param editorPart
     *            the editor to check.
     * @return true if the editor manages the representation, false otherwise
     */
    boolean isRepresentationDescriptionManagedByEditor(RepresentationDescription representationDescription, IEditorPart editorPart);

    /**
     * Check that this dialect is able to export in the given export format.
     * 
     * @param format
     *            the export format
     * @return <code>true</code> if it can, <code>false</code> otherwise
     * @since 0.9.0
     */
    boolean canExport(ExportFormat format);

    /**
     * Export as image or as HTML/CSV document a representation.
     * 
     * @param representation
     *            representation to export as image.
     * @param session
     *            the current session.
     * @param path
     *            the file path
     * @param format
     *            the export format.
     * @param monitor
     *            the progress monitor
     * @throws SizeTooLargeException
     *             if size image to export is too large.
     */
    void export(DRepresentation representation, Session session, IPath path, ExportFormat format, IProgressMonitor monitor) throws SizeTooLargeException;

    /**
     * Tell whether the dialect is able to handle the given representation.
     * 
     * @param representation
     *            representation to export as image.
     * @return true if the dialect can handle the representation, false
     *         otherwise.
     */
    boolean canHandle(DRepresentation representation);

    /**
     * Return Hierachical LabelProvider.
     * 
     * @param currentLabelProvider
     *            the current label provider. If the current label is null, the
     *            result can be null
     * @return a LabelProvider
     */
    ILabelProvider getHierarchyLabelProvider(ILabelProvider currentLabelProvider);
}
