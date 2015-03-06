/*******************************************************************************    
 * Copyright (c) 2015 Obeo and others. All rights 
 * reserved. This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which accompanies this    
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html  
 *  
 * Contributors: Obeo - initial API and implementation  
 *******************************************************************************/
package org.eclipse.sirius.ecore.fixed.side.edges.design.edit.policies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.sirius.diagram.ui.graphical.edit.policies.AirXYLayoutEditPolicy;
import org.eclipse.sirius.diagram.ui.graphical.edit.policies.RegionResizableEditPolicy;

/**
 * {@link AirXYLayoutEditPolicy} used to provide a specific child
 * {@link EditPolicy}.
 * 
 * @author <a href="mailto:steve.monnier@obeo.fr">Steve Monnier</a>
 *
 */
public class FixedSideEdgesAirXYLayoutEditPolicy extends AirXYLayoutEditPolicy {

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.XYLayoutEditPolicy#createChildEditPolicy(org.eclipse.gef.EditPart)
     */
    @Override
    protected EditPolicy createChildEditPolicy(final EditPart child) {
        EditPolicy childEditPolicy = super.createChildEditPolicy(child);
        if(! (childEditPolicy instanceof RegionResizableEditPolicy) && ! (childEditPolicy instanceof RegionResizableEditPolicy)) {
            childEditPolicy = new FixedSideEdgesAirResizableEditPolicy();
        }
        return childEditPolicy;
    }

}
