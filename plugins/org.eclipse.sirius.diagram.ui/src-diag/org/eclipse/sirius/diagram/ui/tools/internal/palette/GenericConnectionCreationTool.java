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
package org.eclipse.sirius.diagram.ui.tools.internal.palette;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.palette.PaletteToolEntry;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.EdgeTarget;
import org.eclipse.sirius.diagram.description.AbstractNodeMapping;
import org.eclipse.sirius.diagram.description.DiagramElementMapping;
import org.eclipse.sirius.diagram.description.EdgeMapping;
import org.eclipse.sirius.diagram.description.tool.ContainerCreationDescription;
import org.eclipse.sirius.diagram.description.tool.EdgeCreationDescription;
import org.eclipse.sirius.diagram.description.tool.NodeCreationDescription;
import org.eclipse.sirius.diagram.tools.api.command.IDiagramCommandFactoryProvider;
import org.eclipse.sirius.diagram.ui.edit.api.part.IDiagramElementEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DDiagramEditPart;
import org.eclipse.sirius.diagram.ui.tools.api.command.GMFCommandWrapper;
import org.eclipse.sirius.diagram.ui.tools.internal.part.SiriusDiagramGraphicalViewer;
import org.eclipse.sirius.viewpoint.description.tool.AbstractToolDescription;
import org.eclipse.sirius.viewpoint.description.tool.MappingBasedToolDescription;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PlatformUI;

/**
 * The Generic Edge Creation Tool.
 * 
 * @author fbarbin
 *
 */
public class GenericConnectionCreationTool extends ConnectionCreationTool {
    private PaletteRoot paletteRoot;

    private List<ConnectionCreationToolEntry> connectionToolEntries;

    private List<PaletteToolEntry> elementCreationToolEntries;

    @Override
    public void activate() {
        super.activate();
        if (paletteRoot == null) {
            paletteRoot = getDomain().getPaletteViewer().getPaletteRoot();
        }
        getAllEntries();
    }

    private List<ConnectionCreationToolEntry> getAllEntries() {
        this.connectionToolEntries = new ArrayList<>();
        this.elementCreationToolEntries = new ArrayList<>();
        List<ConnectionCreationToolEntry> entries = new ArrayList<>();
        for (Object container : paletteRoot.getChildren()) {
            PaletteContainer paletteContainer = (PaletteContainer) container;
            getToolFromContainer(paletteContainer);
        }
        return entries;
    }

    private void getToolFromContainer(PaletteContainer paletteContainer) {
        for (Object entry : paletteContainer.getChildren()) {
            if (entry instanceof ConnectionCreationToolEntry) {
                connectionToolEntries.add((ConnectionCreationToolEntry) entry);
            } else if (entry instanceof PaletteToolEntry) {
                elementCreationToolEntries.add((PaletteToolEntry) entry);

            } else if (entry instanceof PaletteContainer) {
                getToolFromContainer((PaletteContainer) entry);

            }
        }
    }

    @Override
    protected Command getCommand() {
        Command commandToReturn = UnexecutableCommand.INSTANCE;
        CreateConnectionRequest request = (CreateConnectionRequest) getTargetRequest();
        if (RequestConstants.REQ_CONNECTION_START.equals(request.getType())) {
            commandToReturn = handleConnectionStart(request);
        } else if (RequestConstants.REQ_CONNECTION_END.equals(request.getType())) {
            commandToReturn = handleConnectionEnd(request);

        }
        return commandToReturn;
    }

    /**
     * Return the first executable command found within the available edge connection tools.
     * 
     * @param request
     * @return
     */
    private Command handleConnectionStart(CreateConnectionRequest request) {
        EditPart editPart = getTargetEditPart();
        for (ConnectionCreationToolEntry toolEntry : connectionToolEntries) {
            Optional<EdgeCreationDescription> optional = getCreationCreationDescription(EdgeCreationDescription.class, toolEntry);
            if (optional.isPresent()) {
                request.setFactory(new GenericConnectionRequestCreationFactory(optional.get()));
                Command currentCommand = editPart.getCommand(request);
                if (currentCommand != null && currentCommand.canExecute()) {
                    return currentCommand;
                }
            }
        }
        return UnexecutableCommand.INSTANCE;
    }

    /**
     * Return the first executable command found within the available edge connection tools.
     * 
     * @param request
     * @return
     */
    private Command handleConnectionEnd(CreateConnectionRequest request) {
        EditPart editPart = getTargetEditPart();

        for (ConnectionCreationToolEntry toolEntry : connectionToolEntries) {
            Optional<EdgeCreationDescription> optional = getCreationCreationDescription(EdgeCreationDescription.class, toolEntry);
            if (optional.isPresent()) {
                if (editPart instanceof DDiagramEditPart) {
                    handleDiagramCase(optional.get());
                }
                CreateConnectionRequest newRequest = createNewConnectionRequest(request, optional.get());
                Command currentCommand = editPart.getCommand(newRequest);
                // If at least one of the connection tool is executable, we return an executable command.
                if (currentCommand != null && currentCommand.canExecute()) {
                    return createCompleteCommand(request);
                }
            }
        }
        return UnexecutableCommand.INSTANCE;
    }

    private Command handleDiagramCase(EdgeCreationDescription edgeCreationDescription) {
        for (PaletteToolEntry entry : elementCreationToolEntries) {
            Optional<MappingBasedToolDescription> optional = getCreationCreationDescription(MappingBasedToolDescription.class, entry);
            if (optional.isPresent()) {
                MappingBasedToolDescription description = optional.get();
                List<AbstractNodeMapping> nodeMappings = new ArrayList<>();
                if (description instanceof NodeCreationDescription) {
                    nodeMappings.addAll(((NodeCreationDescription) description).getNodeMappings());
                }

                else if (description instanceof ContainerCreationDescription) {
                    nodeMappings.addAll(((ContainerCreationDescription) description).getContainerMappings());
                }
                for (EdgeMapping edgeMapping : edgeCreationDescription.getEdgeMappings()) {
                    Optional<DiagramElementMapping> optionalDiagramElementMapping = edgeMapping.getAllMappings().stream().filter(mapping -> nodeMappings.contains(mapping)).findFirst();
                    optionalDiagramElementMapping.toString();
                }

            }
        }
        return UnexecutableCommand.INSTANCE;

    }

    private Command createCompleteCommand(CreateConnectionRequest request) {
        DiagramEditor editor = (DiagramEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        EditPartViewer editPartViewer = getCurrentViewer();
        EdgeTarget source = getEdgeTargetFromEditPart(request.getSourceEditPart());
        EdgeTarget target = getEdgeTargetFromEditPart(request.getTargetEditPart());
        if (editPartViewer instanceof SiriusDiagramGraphicalViewer) {

            TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(source);
            return new ICommandProxy(new GMFCommandWrapper(editingDomain, new GenericEdgeCreationToolCommand(editingDomain, editPartViewer.getControl(), editor, request, connectionToolEntries)));

        }
        return UnexecutableCommand.INSTANCE;
    }

    private EdgeTarget getEdgeTargetFromEditPart(EditPart editPart) {
        if (editPart instanceof IDiagramElementEditPart) {
            DDiagramElement element = ((IDiagramElementEditPart) editPart).resolveDiagramElement();
            if (element instanceof EdgeTarget) {
                return (EdgeTarget) element;
            }
        }
        return null;
    }

    private Command getCommand(EdgeTarget source, EdgeTarget target, ConnectionCreationToolEntry entry, TransactionalEditingDomain domain, IDiagramCommandFactoryProvider cmdFactoryProvider) {
        Optional<EdgeCreationDescription> edgeCreationDescriptionOptional = getCreationCreationDescription(EdgeCreationDescription.class, entry);
        if (edgeCreationDescriptionOptional.isPresent()) {
            return new ICommandProxy(new GMFCommandWrapper(domain, cmdFactoryProvider.getCommandFactory(domain).buildCreateEdgeCommandFromTool(source, target, edgeCreationDescriptionOptional.get())));
        }
        return UnexecutableCommand.INSTANCE;
    }

    private <E extends MappingBasedToolDescription> Optional<E> getCreationCreationDescription(Class<E> type, CreationToolEntry entry) {
        Object toolProperty = entry.getToolProperty(CreationTool.PROPERTY_CREATION_FACTORY);
        Optional<E> edgeCreationDescriptionOptional = Optional.ofNullable(toolProperty).filter(CreationFactory.class::isInstance).map(factory -> ((CreationFactory) toolProperty).getNewObject())
                .filter(type::isInstance).map(type::cast);
        return edgeCreationDescriptionOptional;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private CreateConnectionRequest createNewConnectionRequest(CreateConnectionRequest request, EdgeCreationDescription description) {
        CreateConnectionRequest connectionRequest = new CreateConnectionRequest();
        connectionRequest.setLocation(request.getLocation());
        connectionRequest.setTargetEditPart(request.getTargetEditPart());
        connectionRequest.setStartCommand(request.getStartCommand());
        connectionRequest.setSourceEditPart(request.getSourceEditPart());
        connectionRequest.setType(request.getType());
        connectionRequest.setSnapToEnabled(request.isSnapToEnabled());
        connectionRequest.setFactory(new GenericConnectionRequestCreationFactory(description));
        Map extendedData = new HashMap(request.getExtendedData());
        extendedData.put("edge.creation.description", description); //$NON-NLS-1$
        connectionRequest.setExtendedData(extendedData);

        return connectionRequest;
    }

    private class GenericEdgeCreationToolCommand extends RecordingCommand {
        private Control control;

        private DiagramEditor editor;

        private List<ConnectionCreationToolEntry> toolEntries;

        private CreateConnectionRequest request;

        GenericEdgeCreationToolCommand(TransactionalEditingDomain domain, Control control, DiagramEditor editor, CreateConnectionRequest request, List<ConnectionCreationToolEntry> toolEntries) {
            super(domain, "Generic Edge Creation Tool"); //$NON-NLS-1$
            this.control = control;
            this.editor = editor;
            this.toolEntries = new ArrayList<>();
            this.toolEntries.addAll(toolEntries);
            this.request = request;
        }

        @Override
        protected void doExecute() {
            Menu menu = new Menu(control);
            IDiagramCommandFactoryProvider cmdFactoryProvider = (IDiagramCommandFactoryProvider) editor.getAdapter(IDiagramCommandFactoryProvider.class);

            for (ConnectionCreationToolEntry entry : toolEntries) {
                Optional<EdgeCreationDescription> optional = getCreationCreationDescription(EdgeCreationDescription.class, entry);
                if (optional.isPresent()) {
                    CreateConnectionRequest connectionRequest = createNewConnectionRequest(request, optional.get());
                    Command currentCommand = request.getTargetEditPart().getCommand(connectionRequest);
                    createMenuItem(entry, menu,
                            currentCommand/* getCommand(source, target, entry, domain, cmdFactoryProvider) */);
                }
            }
            new MenuItem(menu, SWT.SEPARATOR);
            menu.setVisible(true);

            // Modal menu
            Display display = menu.getDisplay();
            while (!menu.isDisposed() && menu.isVisible()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
            // CHECKSTYLE:OFF
            while (display.readAndDispatch()); // needed, to get the selection event, which is fired AFTER the menu is
            // hidden. See
            // ODFOREA-2405
            // CHECKSTYLE:ON
            if (!menu.isDisposed()) {
                menu.dispose();
            }
            // }
        }

        private void createMenuItem(ConnectionCreationToolEntry entry, Menu menu, Command command) {
            final MenuItem item = new MenuItem(menu, SWT.CASCADE);
            ImageDescriptor descriptor = entry.getSmallIcon();
            if (descriptor != null) {
                item.setImage(descriptor.createImage());
            }

            item.setText(entry.getLabel());
            item.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    super.widgetSelected(e);
                    command.execute();
                }
            });

        }

    }

    /**
     * A specific CreationFactory .
     * 
     * @author fbarbin
     *
     */
    private class GenericConnectionRequestCreationFactory implements CreationFactory {

        private AbstractToolDescription abstractToolDescription;

        /**
         * Default constructor
         * 
         * @param abstractToolDescription
         *            the Connection Creation tool description.
         */
        GenericConnectionRequestCreationFactory(AbstractToolDescription abstractToolDescription) {
            this.abstractToolDescription = abstractToolDescription;
        }

        @Override
        public Object getNewObject() {
            return abstractToolDescription;
        }

        @Override
        public Object getObjectType() {
            return abstractToolDescription.getClass();
        }

    }
}
