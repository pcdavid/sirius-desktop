/*******************************************************************************
 * Copyright (c) 2008, 2010 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ui.business.internal.dialect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.common.tools.api.util.EclipseUtil;
import org.eclipse.sirius.ui.business.api.dialect.DialectEditor;
import org.eclipse.sirius.ui.business.api.dialect.DialectUI;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.ui.business.api.dialect.ExportFormat;
import org.eclipse.sirius.ui.business.api.session.SessionEditorInput;
import org.eclipse.sirius.ui.tools.api.actions.export.SizeTooLargeException;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationElement;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.sirius.viewpoint.SiriusPlugin;
import org.eclipse.sirius.viewpoint.description.RepresentationDescription;
import org.eclipse.ui.IEditorPart;

/**
 * Class able to manage a set of dialects to provides the usual dialect services
 * using the Eclipse environment.
 * 
 * @author cbrun
 * 
 */
public class DialectUIManagerImpl implements DialectUIManager {

    Map<String, DialectUI> dialects = new HashMap<String, DialectUI>();

    /**
     * Init a default manager implementation.
     * 
     * @return a default manager implementation
     */
    public static DialectUIManager init() {
        final DialectUIManagerImpl manager = new DialectUIManagerImpl();
        if (SiriusPlugin.IS_ECLIPSE_RUNNING) {
            final List<DialectUI> parsedDialects = EclipseUtil.getExtensionPlugins(DialectUI.class, DialectUIManager.ID, DialectUIManager.CLASS_ATTRIBUTE);
            for (final DialectUI dialect : parsedDialects) {
                manager.enableDialectUI(dialect);
            }
        }
        return manager;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public void disableDialectUI(final DialectUI dialect) {
        dialects.remove(dialect.getName());
    }

    /**
     * 
     * {@inheritDoc}
     */
    public void enableDialectUI(final DialectUI dialect) {
        dialects.put(dialect.getName(), dialect);
    }

    /**
     * {@inheritDoc}
     */
    public IEditorPart openEditor(Session session, DRepresentation dRepresentation, IProgressMonitor monitor) {
        for (final DialectUI dialect : dialects.values()) {
            final IEditorPart editor = dialect.getServices().openEditor(session, dRepresentation, monitor);
            if (editor != null) {
                return editor;
            }
        }
        return null;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public String getEditorID(DRepresentation dRepresentation) {
        for (final DialectUI dialect : dialects.values()) {
            if (dialect.getServices().canHandle(dRepresentation)) {
                return dialect.getServices().getEditorID(dRepresentation);
            }
        }
        return null;
    }
    
    @Override
    public SessionEditorInput getEditorInput(Session session, DRepresentation dRepresentation) {
        for (final DialectUI dialect : dialects.values()) {
            if (dialect.getServices().canHandle(dRepresentation)) {
                return dialect.getServices().getEditorInput(session, dRepresentation);
            }
        }
        return null;
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    public Collection<CommandParameter> provideNewChildDescriptors() {
        final Collection<CommandParameter> result = new ArrayList<CommandParameter>();
        for (final DialectUI dialect : dialects.values()) {
            result.addAll(dialect.getServices().provideNewChildDescriptors());
        }
        return result;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public Collection<CommandParameter> provideRepresentationCreationToolDescriptors(final Object feature) {
        final Collection<CommandParameter> result = new ArrayList<CommandParameter>();
        for (final DialectUI dialect : dialects.values()) {
            result.addAll(dialect.getServices().provideRepresentationCreationToolDescriptors(feature));
        }
        return result;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public Collection<CommandParameter> provideRepresentationNavigationToolDescriptors(final Object feature) {
        final Collection<CommandParameter> result = new ArrayList<CommandParameter>();
        for (final DialectUI dialect : dialects.values()) {
            result.addAll(dialect.getServices().provideRepresentationNavigationToolDescriptors(feature));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<CommandParameter> provideAdditionalMappings(EObject object) {
        final Collection<CommandParameter> result = new ArrayList<CommandParameter>();
        for (final DialectUI dialect : dialects.values()) {
            result.addAll(dialect.getServices().provideAdditionalMappings(object));
        }
        return result;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public AdapterFactory createAdapterFactory() {
        final ComposedAdapterFactory composed = new ComposedAdapterFactory();
        for (final DialectUI dialect : dialects.values()) {
            composed.addAdapterFactory(dialect.getServices().createAdapterFactory());
        }
        return composed;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.dialect.DialectUIServices#canHandleEditor(org.eclipse.ui.IEditorPart)
     */
    public boolean canHandleEditor(final IEditorPart editorPart) {
        boolean result = false;
        for (final DialectUI dialect : dialects.values()) {
            result = result || dialect.getServices().canHandleEditor(editorPart);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.dialect.DialectUIServices#closeEditor(org.eclipse.ui.IEditorPart,
     *      boolean)
     */
    public boolean closeEditor(final IEditorPart editorPart, final boolean save) {
        boolean result = false;
        for (final DialectUI dialect : dialects.values()) {
            if (dialect.getServices().canHandleEditor(editorPart)) {
                result = result || dialect.getServices().closeEditor(editorPart, save);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.dialect.DialectUIServices#isRepresentationManagedByEditor(org.eclipse.sirius.viewpoint.DRepresentation,
     *      org.eclipse.ui.IEditorPart)
     */
    public boolean isRepresentationManagedByEditor(final DRepresentation representation, final IEditorPart editorPart) {
        for (final DialectUI dialect : dialects.values()) {
            if (dialect.getServices().isRepresentationManagedByEditor(representation, editorPart)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.dialect.DialectUIServices#isRepresentationDescriptionManagedByEditor(org.eclipse.sirius.viewpoint.description.RepresentationDescription,
     *      org.eclipse.ui.IEditorPart)
     */
    public boolean isRepresentationDescriptionManagedByEditor(final RepresentationDescription representationDescription, final IEditorPart editor) {
        for (final DialectUI dialect : dialects.values()) {
            if (dialect.getServices().isRepresentationDescriptionManagedByEditor(representationDescription, editor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.dialect.DialectUIServices#getEditorName(org.eclipse.sirius.viewpoint.DRepresentation)
     */
    public String getEditorName(final DRepresentation representation) {
        for (final DialectUI dialect : dialects.values()) {
            if (dialect.getServices().canHandle(representation)) {
                return dialect.getServices().getEditorName(representation);
            }
        }
        return "";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.dialect.DialectUIServices#canHandle(org.eclipse.sirius.viewpoint.DRepresentation)
     */
    public boolean canHandle(final DRepresentation representation) {
        for (final DialectUI dialect : dialects.values()) {
            if (dialect.getServices().canHandle(representation)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean canExport(ExportFormat format) {
        for (final DialectUI dialect : dialects.values()) {
            if (dialect.getServices().canExport(format)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.dialect.DialectUIServices#export(org.eclipse.sirius.viewpoint.DRepresentation,
     *      org.eclipse.sirius.business.api.session.Session)
     */
    public void export(final DRepresentation representation, final Session session, final IPath path, final ExportFormat format, final IProgressMonitor monitor) throws SizeTooLargeException {
        for (final DialectUI dialect : dialects.values()) {
            if (dialect.getServices().canHandle(representation)) {
                try {
                    dialect.getServices().export(representation, session, path, format, monitor);
                } catch (CoreException exception) {
                    if (exception instanceof SizeTooLargeException) {
                        throw (SizeTooLargeException) exception;
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setSelection(DialectEditor dialectEditor, List<DRepresentationElement> selection) {
        for (final DialectUI dialect : dialects.values()) {
            dialect.getServices().setSelection(dialectEditor, selection);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Collection<DSemanticDecorator> getSelection(DialectEditor editor) {
        for (final DialectUI dialect : dialects.values()) {
            Collection<DSemanticDecorator> selection = dialect.getServices().getSelection(editor);
            if (!selection.isEmpty()) {
                return selection;
            }
        }
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    public Collection<CommandParameter> provideTools(EObject cur) {
        final Collection<CommandParameter> result = new ArrayList<CommandParameter>();
        for (final DialectUI dialect : dialects.values()) {
            result.addAll(dialect.getServices().provideTools(cur));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.dialect.DialectUIServices#getHierarchyLabelProvider(ILabelProvider)
     */
    public ILabelProvider getHierarchyLabelProvider(ILabelProvider currentLabelProvider) {
        ILabelProvider result = currentLabelProvider;
        for (final DialectUI dialect : dialects.values()) {
            ILabelProvider hierarchyLabelProvider = dialect.getServices().getHierarchyLabelProvider(result);
            if (hierarchyLabelProvider != null) {
                result = hierarchyLabelProvider;
            }
        }
        return result;
    }
}
