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
package org.eclipse.sirius.business.internal.rename;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.emf.common.util.URI;

/**
 * Class used to gather all dependencies between resources.
 * 
 * @author <a href="mailto:jessy.mallet@obeo.fr">Jessy MALLET</a>
 *
 */
public class ModelDependenciesGraph {

    /**
     * Dependencies known.
     */
    private Set<URI> known = new LinkedHashSet<URI>();

    /**
     * key path requires paths in values.
     */
    private HashMap<URI, Set<URI>> requires = new HashMap<URI, Set<URI>>();

    /**
     * key path is required by paths in values.
     */
    private HashMap<URI, Set<URI>> invRequires = new HashMap<URI, Set<URI>>();

    /**
     * Function used to collect all dependencies.
     */
    private Function<URI, Collection<URI>> requireFunction;

    /**
     * Constructor.
     * 
     * @param computeRequirement
     *            function used to collect all dependencies.
     */
    public ModelDependenciesGraph(Function<URI, Collection<URI>> computeRequirement) {
        this.requireFunction = computeRequirement;
    }

    /**
     * Add a new dependency to the graph.
     * 
     * @param modelRequiring
     *            uri of the model requiring the other model.
     * @param requirement
     *            uri of the model which is required.
     */
    private void requires(URI modelRequiring, URI requirement) {
        if (requires.get(modelRequiring) == null) {
            requires.put(modelRequiring, new HashSet<URI>(Arrays.asList(requirement)));
        } else {
            requires.get(modelRequiring).add(requirement);
        }
        if (invRequires.get(requirement) == null) {
            invRequires.put(requirement, new HashSet<URI>(Arrays.asList(modelRequiring)));
        } else {
            invRequires.get(requirement).add(modelRequiring);
        }

    }

    /**
     * Register a path which have not been registered already.
     * 
     * @param modelURI
     *            a model URI to consider in the dependency graph.
     */
    public void registerModel(URI modelURI) {
        known.add(modelURI);
        if (modelURI.isPlatformResource()) {
            for (URI uri : requireFunction.apply(modelURI)) {
                requires(modelURI, uri);
                if (!known.contains(uri)) {
                    registerModel(uri);
                }
            }
        }
    }

    /**
     * Return the list of paths which are required by the given other path.
     * 
     * @param absolutePath
     *            a path.
     * @return the list of paths which are required by the given other path.
     */
    public Collection<URI> getRequirements(URI absolutePath) {
        return requires.get(absolutePath);
    }

    /**
     * Return the list of paths which are required by the given other path.
     * 
     * @param absolutePath
     *            a path.
     * @return the list of paths which are required by the given other path.
     */
    public Collection<URI> getInverseRequirements(URI absolutePath) {
        return invRequires.get(absolutePath);
    }

}
