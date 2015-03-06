/*******************************************************************************    
 * Copyright (c) 2015 Obeo and others. All rights 
 * reserved. This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which accompanies this    
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html  
 *  
 * Contributors: Obeo - initial API and implementation  
 *******************************************************************************/
package org.eclipse.sirius.ecore.fixed.side.edges.design.edit.part;

import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeContainerEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeEditPart;

/**
 * {@link AbstractEditPartProvider} used to provide the specific
 * {@link GraphicalEditPart} required to override the default oblique router to
 * allow connections to have fixed anchors and drawn entirely even if going over
 * the source or target node.
 * 
 * @author <a href="mailto:steve.monnier@obeo.fr">Steve Monnier</a>
 *
 */
public class FixedSideEdgesEditPartProvider extends AbstractEditPartProvider {


    @Override
    protected Class getNodeEditPartClass(View view) {
        if (view.getElement() instanceof DNodeContainer && view.getType().equals(Integer.toString(DNodeContainerEditPart.VISUAL_ID))
                && "FSED_EPackage".equals(((DNodeContainer) view.getElement()).getMapping().getName())) {
            return FixedSideEdgesDNodeContainerEditPart.class;
        } else if (view.getElement() instanceof DNode && view.getType().equals(Integer.toString(DNodeEditPart.VISUAL_ID)) && "FSED_EClass".equals(((DNode) view.getElement()).getMapping().getName())) {
            return FixedSideEdgesDNodeEditPart.class;
        }
        return super.getNodeEditPartClass(view);
    }

    @Override
    protected Class getDiagramEditPartClass(View view) {
        return FixedSideEdgesDDiagramEditPart.class;
    }

    @Override
    protected Class getEdgeEditPartClass(View view) {
        if (view.getElement() instanceof DEdge
                && ("FSED_EPackages2EPackages".equals(((DEdge) view.getElement()).getMapping().getName()) || "FSED_EPackages2EClasses".equals(((DEdge) view.getElement()).getMapping().getName()))) {
            return FixedSideEdgeEditPart.class;
        }
        return super.getEdgeEditPartClass(view);
    }

}
