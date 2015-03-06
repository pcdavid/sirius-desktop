/*******************************************************************************    
 * Copyright (c) 2015 Obeo and others. All rights 
 * reserved. This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which accompanies this    
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html  
 *  
 * Contributors: Obeo - initial API and implementation  
 *******************************************************************************/
package org.eclipse.sirius.ecore.fixed.side.edges.design.edit.part;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DDiagramEditPart;
import org.eclipse.sirius.ecore.fixed.side.edges.design.edit.policies.FixedSideEdgesAirXYLayoutEditPolicy;

/**
 * {@link DDiagramEditPart} used to provide a specific LAYOUT_ROLE.
 * 
 * @author <a href="mailto:steve.monnier@obeo.fr">Steve Monnier</a>
 *
 */
public class FixedSideEdgesDDiagramEditPart extends DDiagramEditPart {

    public FixedSideEdgesDDiagramEditPart(View view) {
        super(view);
    }

    @Override
    protected void createDefaultEditPolicies() {
        // TODO Auto-generated method stub
        super.createDefaultEditPolicies();
        removeEditPolicy(EditPolicy.LAYOUT_ROLE);
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new FixedSideEdgesAirXYLayoutEditPolicy());
    }

}
