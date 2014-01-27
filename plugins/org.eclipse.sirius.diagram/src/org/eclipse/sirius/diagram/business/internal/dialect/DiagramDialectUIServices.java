/*******************************************************************************
 * Copyright (c) 2007, 2010 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.business.internal.dialect;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.image.ImageFileFormat;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.parts.DiagramDocumentEditor;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.sirius.business.api.diagramtype.DiagramTypeDescriptorRegistry;
import org.eclipse.sirius.business.api.diagramtype.IDiagramTypeDescriptor;
import org.eclipse.sirius.business.api.helper.SiriusResourceHelper;
import org.eclipse.sirius.business.api.query.URIQuery;
import org.eclipse.sirius.business.api.session.CustomDataConstants;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.common.tools.DslCommonPlugin;
import org.eclipse.sirius.common.tools.api.util.StringUtil;
import org.eclipse.sirius.diagram.business.api.view.SiriusGMFHelper;
import org.eclipse.sirius.diagram.business.api.view.refresh.CanonicalSynchronizer;
import org.eclipse.sirius.diagram.business.api.view.refresh.CanonicalSynchronizerFactory;
import org.eclipse.sirius.diagram.business.internal.command.CreateAndStoreGMFDiagramCommand;
import org.eclipse.sirius.diagram.edit.api.part.IDDiagramEditPart;
import org.eclipse.sirius.diagram.part.SiriusDiagramEditorPlugin;
import org.eclipse.sirius.diagram.tools.api.editor.DDiagramEditor;
import org.eclipse.sirius.diagram.tools.api.part.DiagramEditPartService;
import org.eclipse.sirius.tools.api.profiler.SiriusTasksKey;
import org.eclipse.sirius.ui.business.api.dialect.DialectEditor;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIServices;
import org.eclipse.sirius.ui.business.api.dialect.ExportFormat;
import org.eclipse.sirius.ui.business.api.dialect.ExportFormat.ExportDocumentFormat;
import org.eclipse.sirius.ui.business.api.session.SessionEditorInput;
import org.eclipse.sirius.ui.business.api.viewpoint.ViewpointSelectionCallback;
import org.eclipse.sirius.ui.business.internal.commands.ChangeViewpointSelectionCommand;
import org.eclipse.sirius.ui.tools.api.actions.export.SizeTooLargeException;
import org.eclipse.sirius.viewpoint.DDiagram;
import org.eclipse.sirius.viewpoint.DDiagramElement;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationElement;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.sirius.viewpoint.DSemanticDiagram;
import org.eclipse.sirius.viewpoint.SiriusPlugin;
import org.eclipse.sirius.viewpoint.description.DescriptionFactory;
import org.eclipse.sirius.viewpoint.description.DescriptionPackage;
import org.eclipse.sirius.viewpoint.description.DiagramDescription;
import org.eclipse.sirius.viewpoint.description.Layer;
import org.eclipse.sirius.viewpoint.description.RepresentationDescription;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.sirius.viewpoint.description.audit.provider.AuditItemProviderAdapterFactory;
import org.eclipse.sirius.viewpoint.description.concern.provider.ConcernItemProviderAdapterFactory;
import org.eclipse.sirius.viewpoint.description.filter.provider.FilterItemProviderAdapterFactory;
import org.eclipse.sirius.viewpoint.description.provider.DescriptionItemProviderAdapterFactory;
import org.eclipse.sirius.viewpoint.description.style.provider.StyleItemProviderAdapterFactory;
import org.eclipse.sirius.viewpoint.description.tool.ToolFactory;
import org.eclipse.sirius.viewpoint.description.tool.provider.ToolItemProviderAdapterFactory;
import org.eclipse.sirius.viewpoint.description.validation.provider.ValidationItemProviderAdapterFactory;
import org.eclipse.sirius.viewpoint.provider.ViewpointItemProviderAdapterFactory;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * The default diagram ui services.
 * 
 * @author cbrun
 */
public class DiagramDialectUIServices implements DialectUIServices {

    /**
     * The label used for the action which refreshes a diagram.
     */
    public static final String REFRESH_DIAGRAM = "Refresh diagram";

    private static final String EXPORT_DIAGRAM_AS_IMAGE_ERROR_ON_CREATE_IMAGE = "The program was not able to create image file ";

    /**
     * {@inheritDoc}
     */
    public IEditorPart openEditor(Session session, final DRepresentation dRepresentation, IProgressMonitor monitor) {
        DialectEditor result = null;
        try {
            monitor.beginTask("diagram opening", 15);
            DslCommonPlugin.PROFILER.startWork(SiriusTasksKey.OPEN_DIAGRAM_KEY);

            if (dRepresentation instanceof DSemanticDiagram) {
                final IEditorInput editorInput = getEditorInput(session, dRepresentation);
                if (editorInput != null) {
                    monitor.worked(1);
                    monitor.subTask("diagram editor opening : " + dRepresentation.getName());
                    RunnableWithResult<DialectEditor> runnable = new RunnableWithResult.Impl<DialectEditor>() {

                        public void run() {
                            final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                            try {
                                IEditorPart editorPart = page.openEditor(editorInput, getEditorID(dRepresentation));
                                if (editorPart instanceof DialectEditor) {
                                    setResult((DialectEditor) editorPart);
                                }
                            } catch (final PartInitException e) {
                                SiriusDiagramEditorPlugin.getInstance().logError("diagram editor opening error", e);
                            }
                        }

                    };
                    PlatformUI.getWorkbench().getDisplay().syncExec(runnable);
                    monitor.worked(10);
                    if (runnable.getResult() != null) {
                        result = runnable.getResult();
                    }
                    monitor.worked(3);
                }
            }
            DslCommonPlugin.PROFILER.stopWork(SiriusTasksKey.OPEN_DIAGRAM_KEY);
        } finally {
            monitor.done();
        }
        return result;
    }

    /**
     * Synchronizes the GMF diagram model according to the viewpoint
     * DSemanticDiagram model.
     * 
     * @param diagram
     *            the GMF diagram model to synchronize.
     */
    private void synchronizeDiagram(final Diagram diagram) {
        CanonicalSynchronizer canonicalSynchronizer = CanonicalSynchronizerFactory.INSTANCE.createCanonicalSynchronizer(diagram);
        canonicalSynchronizer.storeViewsToArrange(false);
        canonicalSynchronizer.synchronize();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.dialect.DialectUIServices#getEditorID(org.eclipse.sirius.viewpoint.DRepresentation)
     */
    public String getEditorID(DRepresentation dRepresentation) {
        return DDiagramEditor.EDITOR_ID;
    }
    
    @Override
    public SessionEditorInput getEditorInput(Session session, DRepresentation dRepresentation) {
        final SessionEditorInput res;
        
        if (dRepresentation instanceof DSemanticDiagram) {
            final DSemanticDiagram diag = (DSemanticDiagram) dRepresentation;
            Diagram gmfDiag = SiriusGMFHelper.getGmfDiagram(diag);

            if (gmfDiag == null) {
                /*
                 * we have our diagrams but not the gmf ones => old aird
                 * version or corrupted file
                 */
                TransactionalEditingDomain domain = session.getTransactionalEditingDomain();
                domain.getCommandStack().execute(new CreateAndStoreGMFDiagramCommand(session, diag));

                gmfDiag = SiriusGMFHelper.getGmfDiagram(diag);
            }

            if (gmfDiag != null) {
                final URI uri = EcoreUtil.getURI(gmfDiag);
                final String editorName = DialectUIManager.INSTANCE.getEditorName(dRepresentation);
                res = new DiagramDialectEditorInput(uri, editorName, session,(DSemanticDiagram) dRepresentation);
            } else {
                res = null;
            }
        } else {
            res = null;
        }
        
        return res;
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.dialect.DialectUIServices#canHandleEditor(org.eclipse.ui.IEditorPart)
     */
    public boolean canHandleEditor(final IEditorPart editorPart) {
        return editorPart instanceof DiagramDocumentEditor;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.dialect.DialectUIServices#closeEditor(org.eclipse.ui.IEditorPart,
     *      boolean)
     */
    public boolean closeEditor(final IEditorPart editorPart, final boolean save) {
        boolean result = false;
        if (editorPart instanceof DiagramDocumentEditor) {
            try {
                ((DiagramDocumentEditor) editorPart).getDiagramEditPart().deactivate();
            } catch (final NullPointerException e) {
                // we might have an exception closing an editor which is
                // already in trouble
                SiriusDiagramEditorPlugin.getInstance().getLog()
                        .log(new Status(IStatus.WARNING, SiriusDiagramEditorPlugin.ID, "Error while deactivating the representation, the remote server may be unreachable."));
            }

            try {
                ((DiagramDocumentEditor) editorPart).close(save);
            } catch (final NullPointerException e) {
                // we might have an exception closing an editor which is
                // already in trouble
                if (SiriusDiagramEditorPlugin.getInstance().isDebugging()) {
                    SiriusDiagramEditorPlugin.getInstance().getLog()
                            .log(new Status(IStatus.WARNING, SiriusDiagramEditorPlugin.ID, "Error while closing the representation, the remote server may be unreachable."));
                }
            }

            // We suppose it is closed.
            result = true;
        }
        return result;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public Collection<CommandParameter> provideNewChildDescriptors() {
        final Collection<CommandParameter> newChilds = new ArrayList<CommandParameter>();
        newChilds.add(new CommandParameter(null, DescriptionPackage.Literals.VIEWPOINT__OWNED_REPRESENTATIONS, DescriptionFactory.eINSTANCE.createDiagramDescription()));
        newChilds.add(new CommandParameter(null, DescriptionPackage.Literals.VIEWPOINT__OWNED_REPRESENTATIONS, DescriptionFactory.eINSTANCE.createDiagramImportDescription()));
        newChilds.add(new CommandParameter(null, DescriptionPackage.Literals.VIEWPOINT__OWNED_REPRESENTATION_EXTENSIONS, DescriptionFactory.eINSTANCE.createDiagramExtensionDescription()));
        newChilds.addAll(this.getDiagramTypesCreation());
        return newChilds;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public Collection<CommandParameter> provideRepresentationCreationToolDescriptors(final Object feature) {
        final Collection<CommandParameter> newChilds = new ArrayList<CommandParameter>();
        newChilds.add(new CommandParameter(null, feature, ToolFactory.eINSTANCE.createDiagramCreationDescription()));
        return newChilds;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public Collection<CommandParameter> provideRepresentationNavigationToolDescriptors(final Object feature) {
        final Collection<CommandParameter> newChilds = new ArrayList<CommandParameter>();
        newChilds.add(new CommandParameter(null, feature, ToolFactory.eINSTANCE.createDiagramNavigationDescription()));
        return newChilds;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public AdapterFactory createAdapterFactory() {
        final ComposedAdapterFactory factory = new ComposedAdapterFactory();
        for (final IDiagramTypeDescriptor diagramTypeDescriptor : DiagramTypeDescriptorRegistry.getInstance().getAllDiagramTypeDescriptors()) {
            AdapterFactory aFactory = diagramTypeDescriptor.getDiagramDescriptionProvider().getAdapterFactory();
            if (aFactory != null) {
                factory.addAdapterFactory(aFactory);
            }
        }
        factory.addAdapterFactory(new DescriptionItemProviderAdapterFactory());
        factory.addAdapterFactory(new ViewpointItemProviderAdapterFactory());
        factory.addAdapterFactory(new StyleItemProviderAdapterFactory());
        factory.addAdapterFactory(new ToolItemProviderAdapterFactory());
        factory.addAdapterFactory(new FilterItemProviderAdapterFactory());
        factory.addAdapterFactory(new ValidationItemProviderAdapterFactory());
        factory.addAdapterFactory(new AuditItemProviderAdapterFactory());
        factory.addAdapterFactory(new ConcernItemProviderAdapterFactory());
        return factory;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.dialect.DialectUIServices#isRepresentationManagedByEditor(org.eclipse.sirius.viewpoint.DRepresentation,
     *      org.eclipse.ui.IEditorPart)
     */
    public boolean isRepresentationManagedByEditor(final DRepresentation representation, final IEditorPart editorPart) {
        boolean isRepresentationManagedByEditor = false;
        if (editorPart instanceof DiagramDocumentEditor) {
            final DiagramDocumentEditor diagramDocumentEditor = (DiagramDocumentEditor) editorPart;
            DiagramEditPart diagramEditPart = diagramDocumentEditor.getDiagramEditPart();
            if (diagramEditPart instanceof IDDiagramEditPart) {
                IDDiagramEditPart idDiagramEditPart = (IDDiagramEditPart) diagramEditPart;
                if (idDiagramEditPart.resolveSemanticElement().equals(representation)) {
                    isRepresentationManagedByEditor = true;
                }
            }
        }
        return isRepresentationManagedByEditor;
    }

    // FXIME unit test this
    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.dialect.DialectUIServices#isRepresentationDescriptionManagedByEditor(org.eclipse.sirius.viewpoint.description.RepresentationDescription,
     *      org.eclipse.ui.IEditorPart)
     */
    public boolean isRepresentationDescriptionManagedByEditor(final RepresentationDescription representationDescription, final IEditorPart editor) {
        if (editor instanceof DiagramDocumentEditor) {
            final DiagramDocumentEditor diagramDocumentEditor = (DiagramDocumentEditor) editor;
            final EObject element = diagramDocumentEditor.getDiagram().getElement();
            if (element instanceof DSemanticDiagram) {
                final DSemanticDiagram dSemanticDiagram = (DSemanticDiagram) element;
                final DiagramDescription description = dSemanticDiagram.getDescription();
                return EcoreUtil.equals(description, representationDescription);
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.dialect.DialectUIServices#canHandle(org.eclipse.sirius.viewpoint.DRepresentation)
     */
    public boolean canHandle(final DRepresentation representation) {
        return representation instanceof DSemanticDiagram;
    }

    /**
     * {@inheritDoc}
     */
    public boolean canExport(ExportFormat format) {
        if (format.getDocumentFormat().equals(ExportDocumentFormat.NONE) || (format.getDocumentFormat().equals(ExportDocumentFormat.HTML) && DiagramEditPartService.canExportToHtml())) {
            return true;
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

        final boolean exportToHtml = exportToHtml(format);
        final String imageFileExtension = getImageFileExtension(format);
        final IPath correctPath = getRealPath(path, exportToHtml);

        final Shell shell = new Shell();
        try {

            final Collection<EObject> data = session.getServices().getCustomData(CustomDataConstants.GMF_DIAGRAMS, representation);
            for (final EObject dataElement : data) {
                if (dataElement instanceof Diagram) {
                    final Diagram diagram = (Diagram) dataElement;
                    synchronizeDiagram(diagram);

                    final DiagramEditPartService tool = new DiagramEditPartService();
                    if (exportToHtml) {
                        tool.exportToHtml();
                    }
                    final DiagramEditPart diagramEditPart = tool.createDiagramEditPart(diagram, shell, PreferencesHint.USE_DEFAULTS);

                    try {

                        /* refresh to avoid blank images */
                        diagramEditPart.getRoot().refresh();

                        /* validate to have all nodes in the right position */
                        diagramEditPart.getFigure().validate();
                        /*
                         * In the case of connection on EditParts created during
                         * first Refresh they will not appear until we refresh a
                         * second time Example of such cases are exchanges on
                         * DFI (mch)
                         */
                        diagramEditPart.getRoot().refresh();
                        /*
                         * flush the viewer to have all connections and ports
                         */
                        diagramEditPart.getRoot().getViewer().flush();

                        /* do the effective export */
                        tool.copyToImage(diagramEditPart, correctPath, ImageFileFormat.resolveImageFormat(imageFileExtension), monitor);

                        // We finally ensure that the image has been created
                        if (!new File(correctPath.toOSString()).exists()) {
                            throw new CoreException(new Status(IStatus.ERROR, SiriusPlugin.ID, EXPORT_DIAGRAM_AS_IMAGE_ERROR_ON_CREATE_IMAGE + correctPath));
                        }
                    } catch (final CoreException exception) {
                        if (exception instanceof SizeTooLargeException) {
                            throw (SizeTooLargeException) exception;
                        }
                        SiriusPlugin.getDefault().error(EXPORT_DIAGRAM_AS_IMAGE_ERROR_ON_CREATE_IMAGE + correctPath, exception);
                    } finally {
                        diagramEditPart.deactivate();
                        // Memory leak : also disposing the
                        // DiagramGraphicalViewer associated to this
                        // DiagramEditPart
                        diagramEditPart.getViewer().flush();
                        diagramEditPart.getViewer().getEditDomain().getCommandStack().flush();
                        diagramEditPart.getViewer().getControl().dispose();
                        ((DiagramEditDomain) diagramEditPart.getViewer().getEditDomain()).removeViewer(diagramEditPart.getViewer());
                    }
                }
            }

        } finally {
            disposeShell(shell);
        }
    }

    private void disposeShell(final Shell shell) {
        Display.getCurrent().asyncExec(new Runnable() {
            public void run() {
                shell.dispose();
            }
        });
    }

    private boolean exportToHtml(final ExportFormat format) {
        return format.getDocumentFormat().equals(ExportDocumentFormat.HTML);
    }

    private String getImageFileExtension(final ExportFormat format) {
        return format.getImageFormat().getName();
    }

    private IPath getRealPath(final IPath path, final boolean exportToHtml) {
        if (exportToHtml) {
            return path.removeFileExtension().addFileExtension("html");
        } else {
            return path;
        }
    }

    /**
     * Returns command parameters to create diagram type description.
     * 
     * @return command parameters to create diagram type description.
     */
    private Collection<CommandParameter> getDiagramTypesCreation() {
        final Collection<CommandParameter> result = new HashSet<CommandParameter>();
        for (final IDiagramTypeDescriptor diagramTypeDescriptor : DiagramTypeDescriptorRegistry.getInstance().getAllDiagramTypeDescriptors()) {
            final CommandParameter typeCommandParameter = new CommandParameter(null, DescriptionPackage.Literals.VIEWPOINT__OWNED_REPRESENTATIONS, diagramTypeDescriptor
                    .getDiagramDescriptionProvider().createDiagramDescription());
            result.add(typeCommandParameter);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.dialect.DialectUIServices#getEditorName(org.eclipse.sirius.viewpoint.DRepresentation)
     */
    public String getEditorName(final DRepresentation representation) {
        String editorName = representation.getName();
        if (StringUtil.isEmpty(editorName)) {
            editorName = "New Diagram";
        }
        return editorName;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<CommandParameter> provideTools(EObject context) {
        Collection<CommandParameter> toolsParameters = Lists.newArrayList();
        for (final IDiagramTypeDescriptor diagramTypeDescriptor : DiagramTypeDescriptorRegistry.getInstance().getAllDiagramTypeDescriptors()) {
            DiagramDescription diagramType = diagramTypeDescriptor.getDiagramDescriptionProvider().createDiagramDescription();
            if (hasParentOfType(context, diagramType.eClass())) {
                toolsParameters.addAll(diagramTypeDescriptor.getDiagramDescriptionProvider().collectToolCommands(context));
            }
        }
        return toolsParameters;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<CommandParameter> provideAdditionalMappings(EObject context) {
        Collection<CommandParameter> mappings = Lists.newArrayList();
        for (final IDiagramTypeDescriptor diagramTypeDescriptor : DiagramTypeDescriptorRegistry.getInstance().getAllDiagramTypeDescriptors()) {
            DiagramDescription diagramType = diagramTypeDescriptor.getDiagramDescriptionProvider().createDiagramDescription();
            if (hasParentOfType(context, diagramType.eClass())) {
                mappings.addAll(diagramTypeDescriptor.getDiagramDescriptionProvider().collectMappingsCommands());
            }
        }
        return mappings;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.dialect.DialectUIServices#getHierarchyLabelProvider(ILabelProvider)
     * 
     */
    public ILabelProvider getHierarchyLabelProvider(ILabelProvider currentLabelProvider) {
        return new HierarchyLabelProvider(currentLabelProvider);
    }

    /**
     * {@inheritDoc}
     */
    public void setSelection(DialectEditor dialectEditor, List<DRepresentationElement> selection) {
        if (dialectEditor instanceof DiagramEditor && selection != null && !selection.isEmpty()) {
            DiagramEditor diagramEditor = (DiagramEditor) dialectEditor;
            List<EditPart> selectedParts = Lists.newArrayList();
            final EditPartViewer graphicalViewer = diagramEditor.getDiagramGraphicalViewer();

            Iterable<DDiagramElement> ddeSelection = Iterables.filter(selection, DDiagramElement.class);
            if (!Iterables.isEmpty(ddeSelection) && graphicalViewer != null) {
                Session session = SessionManager.INSTANCE.getSession(ddeSelection.iterator().next().getTarget());
                for (DDiagramElement dRepresentationElement : ddeSelection) {
                    EditPart selectedEditPart = getEditPart(dRepresentationElement, graphicalViewer, session);
                    if (selectedEditPart != null) {
                        selectedParts.add(selectedEditPart);
                    }
                }
            }
            if (graphicalViewer != null) {
                graphicalViewer.setSelection(new StructuredSelection(selectedParts));
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.sirius.ui.business.api.dialect.DialectUIServices#getSelection(org.eclipse.sirius.ui.business.api.dialect.DialectEditor)
     */
    public Collection<DSemanticDecorator> getSelection(DialectEditor editor) {
        Collection<DSemanticDecorator> selection = Sets.newLinkedHashSet();
        if (editor instanceof DiagramEditor) {
            DiagramEditor dEditor = (DiagramEditor) editor;
            IDiagramGraphicalViewer graphicalViewer = dEditor.getDiagramGraphicalViewer();

            if (graphicalViewer != null) {
                for (IGraphicalEditPart ep : Iterables.filter(graphicalViewer.getSelectedEditParts(), IGraphicalEditPart.class)) {
                    View view = ep.getNotationView();
                    if (view != null && view.getElement() instanceof DSemanticDecorator) {
                        selection.add((DSemanticDecorator) view.getElement());
                    }
                }
            }
        }
        return selection;
    }

    /**
     * Get the editPart corresponding to this diagram element.<BR>
     * The editPart is search in the active editor.
     * 
     * @param diagramElement
     *            the diagram element
     * @param graphicalViewer
     *            the editor containing the editPart
     * @param session
     *            the current session
     * 
     * @return the editPart corresponding to the diagram element given as
     *         parameter or null if any
     */
    protected IGraphicalEditPart getEditPart(final DDiagramElement diagramElement, final EditPartViewer graphicalViewer, Session session) {
        IGraphicalEditPart result = null;
        final View gmfView = getGmfView(diagramElement, session);
        final Map<?, ?> editPartRegistry = graphicalViewer.getEditPartRegistry();
        if (editPartRegistry != null) {
            final Object editPart = editPartRegistry.get(gmfView);
            if (editPart instanceof IGraphicalEditPart) {
                result = (IGraphicalEditPart) editPart;
            }
        }
        return result;
    }

    /**
     * Get the GMF view from the diagram element.
     * 
     * @param diagramElement
     *            the diagram element
     * @param session
     *            the current session
     * @return the view which has as element the diagram element given as
     *         parameter or null if any
     */
    protected View getGmfView(final DDiagramElement diagramElement, Session session) {
        return getGmfView(diagramElement, View.class, session);
    }

    /**
     * Get the GMF view from the diagram element.
     * 
     * @param <T>
     *            generic type
     * @param diagramElement
     *            the diagram element
     * @param clazz
     *            The type of the desired view
     * @param session
     *            the current session
     * @return the view which has as element the diagram element given as
     *         parameter or null if any
     */
    @SuppressWarnings("unchecked")
    private <T> T getGmfView(final EObject diagramElement, final Class<T> clazz, Session session) {
        if (session != null) {
            ECrossReferenceAdapter crossReference = session.getSemanticCrossReferencer();
            if (crossReference == null) {
                crossReference = new ECrossReferenceAdapter();
                diagramElement.eResource().eAdapters().add(crossReference);
            }
            for (org.eclipse.emf.ecore.EStructuralFeature.Setting setting : crossReference.getInverseReferences(diagramElement)) {
                if (clazz.isInstance(setting.getEObject()) && setting.getEStructuralFeature() == NotationPackage.eINSTANCE.getView_Element()) {
                    return (T) setting.getEObject();
                }
            }
        }
        return null;
    }

    private boolean hasParentOfType(EObject element, EClass eClass) {
        EObject context = element;
        boolean found = context.eClass() == eClass;
        while (!found && context != null) {
            found = context.eClass() == eClass;
            context = context.eContainer();
        }
        return found;
    }

}
