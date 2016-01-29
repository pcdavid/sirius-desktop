/*******************************************************************************
 * Copyright (c) 2014, 2016 THALES GLOBAL SERVICES and others. 
 * All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.internal.query;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.business.api.query.EObjectQuery;
import org.eclipse.sirius.ext.base.Option;
import org.eclipse.sirius.ext.base.Options;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.sirius.viewpoint.ViewpointPackage;

/**
 * A Query on {@link DSemanticDecorator}.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
public class DSemanticDecoratorQuery {

    private DSemanticDecorator dSemanticDecorator;

    /**
     * Default constructor.
     * 
     * @param dSemanticDecorator
     *            the {@link DSemanticDecorator} to query
     */
    public DSemanticDecoratorQuery(DSemanticDecorator dSemanticDecorator) {
        this.dSemanticDecorator = dSemanticDecorator;
    }

    /**
     * Tell if the target of specified view is detached.
     * 
     * @return true if the target of specified view is detached, false otherwise
     */
    public boolean hasDetachedTarget() {
        EObject target = dSemanticDecorator.getTarget();
        return target == null || target.eResource() == null;
    }

    /**
     * Get the DAnalysis corresponding to the current representation. 
     * 
     * @return the analysis
     */
    public Option<DAnalysis> getDAnalysis() {
        Option<EObject> dAnalysis = new EObjectQuery(dSemanticDecorator).getFirstAncestorOfType(ViewpointPackage.eINSTANCE.getDAnalysis());
        if (dAnalysis.some()) {
            return Options.newSome((DAnalysis) dAnalysis.get());
        }
        return Options.newNone();
    }

}
