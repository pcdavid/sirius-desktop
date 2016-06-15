/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ui.debug;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.LabelEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.GraphicalFilter;
import org.eclipse.sirius.diagram.ui.business.internal.operation.ShiftDirectBorderedNodesOperation;
import org.eclipse.sirius.diagram.ui.internal.edit.commands.DistributeCommand;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNode4EditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeContainerEditPart;
import org.eclipse.sirius.diagram.ui.tools.internal.actions.distribute.DistributeAction;
import org.eclipse.sirius.diagram.ui.tools.internal.edit.command.CommandFactory;
import org.eclipse.sirius.viewpoint.DRepresentationElement;
import org.eclipse.sirius.viewpoint.Decoration;
import org.eclipse.sirius.viewpoint.Style;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.IWizardDescriptor;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Show the hierarchy of edit parts for the selection
 * 
 * @author <a href="mailto:mickael.lanoe@obeo.fr">Mickael LANOE</a>
 *
 */
final class ShowEditPartsHierarchy implements Runnable {
    int OFFSET = 10;

    /** Sirius debug view */
    private final SiriusDebugView view;

    /**
     * @param view
     */
    ShowEditPartsHierarchy(SiriusDebugView view) {
        this.view = view;
    }

    @SuppressWarnings("restriction")
    @Override
    public void run() {
        // openWizard("fr.obeo.dsl.viewpoint.collab.ui.sharedprojectwizard");
        if (view.selection instanceof EditPart) {
            EditPart part = (EditPart) view.selection;

            if (part instanceof DNodeContainerEditPart) {
                DNodeContainerEditPart dncep = (DNodeContainerEditPart) part;
                List<DNode4EditPart> list = Lists.newArrayList(Iterables.filter(dncep.getChildren(), DNode4EditPart.class));
                DNode4EditPart first = list.get(0);
                DNode4EditPart last = list.get(0);
                for (DNode4EditPart dn4ep : Iterables.filter(dncep.getChildren(), DNode4EditPart.class)) {
                    if (dn4ep.getFigure().getBounds().getCopy().x() < first.getFigure().getBounds().getCopy().x()) {
                        first = dn4ep;
                    }
                    if (dn4ep.getFigure().getBounds().getRight().getCopy().x() > last.getFigure().getBounds().getRight().getCopy().x()) {
                        last = dn4ep;
                    }
                }
                int delta1 = dncep.getFigure().getBounds().getCopy().x() - first.getFigure().getBounds().getCopy().x() + OFFSET;
                first.getFigure().getBounds().translate(delta1, 0);
                int delta2 = dncep.getFigure().getBounds().getRight().getCopy().x() - last.getFigure().getBounds().getRight().getCopy().x() - OFFSET;
                last.getFigure().getBounds().translate(delta2, 0);
                EObject semanticElement = ((DRepresentationElement) ((View) dncep.getModel()).getElement()).getTarget();
                Session session = SessionManager.INSTANCE.getSession(semanticElement);
                TransactionalEditingDomain ted = session.getTransactionalEditingDomain();
                CompositeTransactionalCommand ctc = new CompositeTransactionalCommand(ted, "Distribute border nodes");
                ctc.compose(CommandFactory.createICommand(ted, new ShiftDirectBorderedNodesOperation(Lists.newArrayList((Node) first.getModel()), new Dimension(delta1, 0))));
                ctc.compose(CommandFactory.createICommand(ted, new ShiftDirectBorderedNodesOperation(Lists.newArrayList((Node) last.getModel()), new Dimension(delta2, 0))));
                ctc.compose(new DistributeCommand(ted, Lists.newArrayList(Iterables.filter(list, IGraphicalEditPart.class)), DistributeAction.GAPS_HORIZONTALLY));
                dncep.getDiagramEditDomain().getDiagramCommandStack().execute(new ICommandProxy(ctc));
            }
        }
    }

    public void openWizard(String id) {
        // First see if this is a "new wizard".
        IWizardDescriptor descriptor = PlatformUI.getWorkbench().getNewWizardRegistry().findWizard(id);
        // If not check if it is an "import wizard".
        if (descriptor == null) {
            descriptor = PlatformUI.getWorkbench().getImportWizardRegistry().findWizard(id);
        }
        // Or maybe an export wizard
        if (descriptor == null) {
            descriptor = PlatformUI.getWorkbench().getExportWizardRegistry().findWizard(id);
        }
        try {
            // Then if we have a wizard, open it.
            if (descriptor != null) {
                IWizard wizard = descriptor.createWizard();
                WizardDialog wd = new WizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), wizard);
                wd.setTitle(wizard.getWindowTitle());
                wd.open();
            }
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    private void showEditParts(EditPart part, int level, StringBuilder out) {
        addLevel(level, out);
        addEditPart(part, level, out);
        out.append("\n");
        for (EditPart child : Iterables.filter(part.getChildren(), EditPart.class)) {
            showEditParts(child, level + 1, out);
        }
    }

    private void addLevel(int level, StringBuilder out) {
        for (int i = 0; i < level; i++) {
            out.append("  ");
        }
    }

    private void addEditPart(EditPart part, int level, StringBuilder out) {
        out.append(part.getClass().getSimpleName());
        Object model = part.getModel();
        if (model instanceof Node) {
            if (!(part instanceof LabelEditPart || part instanceof ListCompartmentEditPart)) {
                out.append(": ");
                Node node = (Node) model;
                EObject element = node.getElement();
                if (element instanceof DDiagramElement) {
                    DDiagramElement diagramElement = (DDiagramElement) element;
                    addDDiagramElement(diagramElement, level, out);
                } else {
                    out.append(element.getClass().getSimpleName());
                }
            }
        } else {
            out.append(": ");
            out.append(model.getClass().getSimpleName());
        }
    }

    private void addDDiagramElement(DDiagramElement element, int level, StringBuilder out) {
        int subLevel = level + 1;
        out.append(element.getClass().getSimpleName());
        if (!element.isVisible()) {
            out.append("\n");
            addLevel(subLevel, out);
            out.append("invisible");
        }

        Style style = element.getStyle();
        if (style != null) {
            out.append("\n");
            addLevel(subLevel, out);
            out.append("style: ");
            out.append(style.getClass().getSimpleName());
        }

        EObject target = element.getTarget();
        List<EObject> semanticElements = element.getSemanticElements();
        if (semanticElements != null && !semanticElements.isEmpty()) {
            for (EObject semanticElement : semanticElements) {
                out.append("\n");
                addLevel(subLevel, out);
                out.append("semantic element: ");
                addSemanticElement(semanticElement, out);
            }
        } else if (target != null) {
            out.append("\n");
            addLevel(subLevel, out);
            out.append("target: ");
            addSemanticElement(target, out);
        }

        List<GraphicalFilter> filters = element.getGraphicalFilters();
        if (filters != null) {
            for (GraphicalFilter filter : filters) {
                out.append("\n");
                addLevel(subLevel, out);
                out.append("graphical filter: ");
                out.append(filter.getClass().getSimpleName());
            }
        }

        List<Decoration> decorations = element.getDecorations();
        if (decorations != null) {
            for (Decoration decoration : decorations) {
                out.append("\n");
                addLevel(subLevel, out);
                out.append("decoration: ");
                out.append(decoration.getClass().getSimpleName());
            }
        }
    }

    private void addSemanticElement(EObject element, StringBuilder out) {
        EClass eClass = element.eClass();
        EStructuralFeature feature = eClass.getEStructuralFeature("name");
        if (feature != null) {
            Object value = element.eGet(feature);
            out.append(eClass.getName());
            out.append(" (name: ");
            out.append(value);
            out.append(")");
        } else {
            out.append(element.toString());
        }
    }

}
