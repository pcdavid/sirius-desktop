/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.unit.api.modelingproject;

import java.util.HashMap;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIException;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.sirius.business.api.resource.LoadEMFResource;
import org.eclipse.sirius.tests.SiriusTestsPlugin;
import org.eclipse.sirius.tests.support.api.SiriusTestCase;
import org.eclipse.sirius.tests.unit.diagram.modeler.ecore.EcoreModeler;
import org.eclipse.sirius.tools.api.command.ICommandFactory;
import org.eclipse.sirius.ui.tools.internal.actions.nature.ModelingToggleNatureAction;
import org.eclipse.ui.ISources;

import com.google.common.collect.Lists;

/**
 * Test Bug #454585.
 * 
 * @author Florian Barbin
 *
 */
public class LoadingResourceWithErrorsTest extends SiriusTestCase implements EcoreModeler {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        genericSetUp();
    }

    public void testAddingResourceInErrors() {
        closeWelcomePage();
        // convert to modeling project (necessary to reproduce this issue)
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(TEMPORARY_PROJECT_NAME);
        createAndExecuteConvertAction(project);

        copyFilesToTestProject(SiriusTestsPlugin.PLUGIN_ID, "/data/unit/modelers/ecore/", TEST_SEMANTIC_MODEL_FILENAME);
        IFile file = checkFileExists(project, TEST_SEMANTIC_MODEL_FILENAME);

        checkIsLoadableWithError(file);
    }

    private void checkIsLoadableWithError(IFile file) {
        ResourceSet set = new ResourceSetImpl();
        String path = file.getFullPath().toOSString();
        final URI uri = URI.createPlatformResourceURI(path, true);
        Resource res = set.getResource(uri, true);
        res.getErrors().add(new XMIException("error"));
        LoadEMFResource runnable = new LoadEMFResource(set, file);
        runnable.run();
        assertTrue("There shouldn't be errors raised", errors.isEmpty());
    }

    private IFile checkFileExists(IProject project, String name) {
        IFile file = project.getFile(name);
        assertNotNull("The file " + name + " should exists", file);
        assertTrue("The file " + name + " should exists", file.exists());
        return file;
    }

    private void createAndExecuteConvertAction(IProject project) {
        try {
            ModelingToggleNatureAction toogleProject = new ModelingToggleNatureAction();
            EvaluationContext evaluationContext = new EvaluationContext(null, Lists.newArrayList(project));
            evaluationContext.addVariable(ISources.ACTIVE_CURRENT_SELECTION_NAME, new StructuredSelection(project));
            @SuppressWarnings("rawtypes")
            ExecutionEvent event = new ExecutionEvent(null, new HashMap(), null, evaluationContext);
            toogleProject.execute(event);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Override
    protected ICommandFactory getCommandFactory() {
        return null;
    }

}
