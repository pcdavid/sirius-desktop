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
package org.eclipse.sirius.tests.unit.common.rename;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.ltk.core.refactoring.PerformChangeOperation;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;
import org.eclipse.ltk.internal.core.refactoring.resource.RenameResourceProcessor;
import org.eclipse.sirius.business.api.resource.ResourceDescriptor;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.tests.SiriusTestsPlugin;
import org.eclipse.sirius.tests.support.api.SiriusDiagramTestCase;
import org.eclipse.sirius.tests.unit.diagram.modeler.ecore.EcoreModeler;
import org.eclipse.sirius.ui.tools.api.project.ModelingProjectManager;
import org.eclipse.sirius.viewpoint.DAnalysis;

/**
 * Test rename of semantic resource is supported by session.
 * 
 * @author jmallet
 */
public class RenameSemanticElementTest extends SiriusDiagramTestCase {

    private static final String PATH = "/data/unit/semantic/rename/";

    private static final String SEMANTIC_RESOURCE_NAME = "MyNew.ecore";

    private static final String AIRD_RESOURCE_NAME = "representations.aird";

    private static final String SEMANTIC_RESOURCE_RENAMED = "MyNewRenamed.ecore";

    protected static final String REPRESENTATION_DECRIPTION_NAME = "root package entities";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        copyFilesToTestProject(SiriusTestsPlugin.PLUGIN_ID, PATH, SEMANTIC_RESOURCE_NAME, AIRD_RESOURCE_NAME);
        genericSetUp(TEMPORARY_PROJECT_NAME + "/" + SEMANTIC_RESOURCE_NAME, EcoreModeler.MODELER_PATH, TEMPORARY_PROJECT_NAME + "/" + AIRD_RESOURCE_NAME);
    }

    /**
     * Test rename of semantic resource in non modeling project with open
     * session.
     * 
     * @throws CoreException
     *             if exception occurred during renaming
     * 
     */
    public void testRenameResourceInNonModelingProjectWithOpenSession() throws CoreException {
        // check initial state
        checkOneResourceInSession(SEMANTIC_RESOURCE_NAME);
        DSemanticDiagram diagram = (DSemanticDiagram) getRepresentationsByName(REPRESENTATION_DECRIPTION_NAME).toArray()[0];
        assertNotNull(diagram);

        renameSemanticResource();

        // Check diagram can be refresh (no proxy) and session is attached to
        // the new rename resource.
        refresh(diagram);
        checkOneResourceInSession(SEMANTIC_RESOURCE_RENAMED);
    }

    /**
     * Test rename of semantic resource in modeling project with open session.
     * 
     * @throws CoreException
     *             if exception occurred during renaming
     * 
     */
    public void testRenameResourceInModelingProjectWithOpenSession() throws CoreException {
        // convert to modeling project
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(TEMPORARY_PROJECT_NAME);
        ModelingProjectManager.INSTANCE.convertToModelingProject(project, new NullProgressMonitor());

        // check initial state
        checkOneResourceInSession(SEMANTIC_RESOURCE_NAME);
        DSemanticDiagram diagram = (DSemanticDiagram) getRepresentationsByName(REPRESENTATION_DECRIPTION_NAME).toArray()[0];
        assertNotNull(diagram);

        renameSemanticResource();

        // Check diagram can be refresh (no proxy) and session is attached to
        // the new rename resource.
        refresh(diagram);
        checkOneResourceInSession(SEMANTIC_RESOURCE_RENAMED);
    }

    /**
     * Test rename of semantic resource in modeling project with close session.
     * 
     * @throws CoreException
     *             if exception occurred during renaming
     * 
     */
    public void testRenameResourceInModelingProjectWithCloseSession() throws CoreException {
        // convert to modeling project
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(TEMPORARY_PROJECT_NAME);
        ModelingProjectManager.INSTANCE.convertToModelingProject(project, new NullProgressMonitor());

        // check initial state
        checkOneResourceInSession(SEMANTIC_RESOURCE_NAME);
        DSemanticDiagram diagram = (DSemanticDiagram) getRepresentationsByName(REPRESENTATION_DECRIPTION_NAME).toArray()[0];
        assertNotNull(diagram);
        URI mainAnalysisURI = session.getSessionResource().getURI();

        session.close(new NullProgressMonitor());
        renameSemanticResource();

        // Open
        session = SessionManager.INSTANCE.getSession(mainAnalysisURI, new NullProgressMonitor());
        assertFalse("The session should not be opened", session.isOpen());
        session.open(new NullProgressMonitor());

        // Check diagram can be refresh (no proxy) and session is attached to
        // the new rename resource.
        refresh(diagram);
        checkOneResourceInSession(SEMANTIC_RESOURCE_RENAMED);
    }

    /**
     * Test rename of semantic resource in non modeling project with close
     * session.
     * 
     * @throws CoreException
     *             if exception occurred during renaming
     * 
     */
    public void testRenameResourceInNonModelingProjectWithCloseSession() throws CoreException {
        // check initial state
        checkOneResourceInSession(SEMANTIC_RESOURCE_NAME);
        DSemanticDiagram diagram = (DSemanticDiagram) getRepresentationsByName(REPRESENTATION_DECRIPTION_NAME).toArray()[0];
        assertNotNull(diagram);
        URI mainAnalysisURI = session.getSessionResource().getURI();

        session.close(new NullProgressMonitor());
        renameSemanticResource();

        // Open
        session = SessionManager.INSTANCE.getSession(mainAnalysisURI, new NullProgressMonitor());
        assertFalse("The session should not be opened", session.isOpen());
        session.open(new NullProgressMonitor());

        // Check diagram can be refresh (no proxy) and session is attached to
        // the new rename resource.
        refresh(diagram);
        checkOneResourceInSession(SEMANTIC_RESOURCE_RENAMED);
    }

    private void checkOneResourceInSession(String resourceName) {
        DAnalysis dAnalysis = (DAnalysis) session.getSessionResource().getContents().get(0);
        EList<ResourceDescriptor> semanticResources = dAnalysis.getSemanticResources();
        List<String> semanticResourcesName = semanticResources.stream().map(res -> res.getResourceURI().lastSegment()).collect(Collectors.toList());
        assertEquals("Session should have only one resource.", 1, semanticResourcesName.size());
        assertEquals("Resource should be renamed such as " + resourceName, resourceName, semanticResourcesName.get(0));
    }

    /**
     * Rename semantic resource using RenameRefactoring.
     * 
     * @throws CoreException
     *             if exception occurred during renaming
     */
    private void renameSemanticResource() throws CoreException {
        URI semanticResourceURI = URI.createPlatformResourceURI("/" + TEMPORARY_PROJECT_NAME + "/" + SEMANTIC_RESOURCE_NAME, true);
        String platformString = semanticResourceURI.toPlatformString(true);
        IResource semanticResource = ResourcesPlugin.getWorkspace().getRoot().findMember(platformString);
        RenameResourceProcessor processor = new RenameResourceProcessor(semanticResource);
        processor.setNewResourceName(SEMANTIC_RESOURCE_RENAMED);
        RenameRefactoring refactoring = new RenameRefactoring(processor);
        RefactoringStatus checkAllConditions = refactoring.checkAllConditions(new NullProgressMonitor());
        PerformChangeOperation performChangeOperation = new PerformChangeOperation(refactoring.createChange(new NullProgressMonitor()));
        performChangeOperation.run(new NullProgressMonitor());
    }
}
