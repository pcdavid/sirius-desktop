/*******************************************************************************
 * Copyright (c) 2014,2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.unit.common.interpreter.acceleo;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.common.tools.api.interpreter.EvaluationException;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreter;
import org.eclipse.sirius.tests.SiriusTestsPlugin;
import org.eclipse.sirius.tests.support.api.SiriusTestCase;
import org.eclipse.sirius.tools.api.command.ICommandFactory;
import org.eclipse.sirius.tools.api.command.semantic.AddSemanticResourceCommand;

/**
 * Ensures that the semantic meta-models are activated on session interpreter
 * 
 * @author <a href="mailto:laurent.fasani@obeo.fr">Laurent FASANI</a>
 */
public class InterpreterEvaluationTest extends SiriusTestCase {
    private static final String PATH = SiriusTestsPlugin.PLUGIN_ID + "/data/unit/interpreter/";

    // an aird without VP
    private static final String AIRD_MODEL_NAME = "representations.aird";

    private static final String SEMANTIC_MODEL_NAME = "My.component";

    private static final String SEMANTIC_MODEL2_NAME = "My.interactions";

    @Override
    protected void setUp() throws Exception {
        genericSetUp(Collections.singleton(PATH + SEMANTIC_MODEL_NAME), Collections.<String> emptyList(), PATH + AIRD_MODEL_NAME);;
    }

    /**
     * Ensures that the semantic meta-models is activated on session interpreter
     * at session opening.
     */
    public void testSemanticMetaModelOnSiriusInterpreter() {

        String expression = "aql:self.eContents()->filter(component::Component)";

        EObject rootEObject = session.getSemanticResources().iterator().next().getContents().get(0);

        // Evaluate expression
        IInterpreter iInterpreter = session.getInterpreter();
        Collection<EObject> evaluateCollection = Collections.emptyList();
        try {
            evaluateCollection = iInterpreter.evaluateCollection(rootEObject, expression);
        } catch (EvaluationException e) {
        }

        // Check
        assertTrue("The aql interpreter can't find 'component' semantic meta-model", !evaluateCollection.isEmpty());
    }

    /**
     * Ensures that the semantic meta-models is activated on session interpreter
     * at new semantic resource addition.
     */
    public void testSemanticMetaModelOnSiriusInterpreterAfterResourceAddition() {

        String expression = "aql:self.oclIsTypeOf(interactions::Interaction)";

        // Add semantic resource
        AddSemanticResourceCommand addSemanticResourceCommand = new AddSemanticResourceCommand(session, toURI(PATH + SEMANTIC_MODEL2_NAME, ResourceURIType.RESOURCE_PLUGIN_URI),
                new NullProgressMonitor());

        session.getTransactionalEditingDomain().getCommandStack().execute(addSemanticResourceCommand);
        session.save(new NullProgressMonitor());

        // Evaluate expression
        Iterator<Resource> iterator = session.getSemanticResources().iterator();
        iterator.next();
        EObject rootEObject = iterator.next().getContents().get(0);

        boolean evaluateBoolean = false;
        try {
            evaluateBoolean = session.getInterpreter().evaluateBoolean(rootEObject, expression);
        } catch (EvaluationException e) {
        }

        // Check activated MM on Sirius interpreter
        assertTrue("The aql interpreter can't find 'interactions' semantic meta-model corresponding to the added resource", evaluateBoolean);
    }

    @Override
    protected ICommandFactory getCommandFactory() {
        return null;
    }
}
