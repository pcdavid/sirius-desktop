/*******************************************************************************
 * Copyright (c) 2010, 2014 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.swtbot.suite;

import org.eclipse.sirius.tests.swtbot.ArrangeAllTest;
import org.eclipse.sirius.tests.swtbot.CascadingSiriusURITest;
import org.eclipse.sirius.tests.swtbot.ChildrenPositionStabilityAfterParentResizeTest;
import org.eclipse.sirius.tests.swtbot.DiagramPrintTest;
import org.eclipse.sirius.tests.swtbot.DiagramZoomTest;
import org.eclipse.sirius.tests.swtbot.DirectEditWithInputLabelTest;
import org.eclipse.sirius.tests.swtbot.DndWorkspaceSupportTest;
import org.eclipse.sirius.tests.swtbot.DragNDropTest;
import org.eclipse.sirius.tests.swtbot.EdgeCreationTest;
import org.eclipse.sirius.tests.swtbot.HideRevealDiagramElementsLabelsTest;
import org.eclipse.sirius.tests.swtbot.InitializeSessionTest;
import org.eclipse.sirius.tests.swtbot.NavigateToNewRepresentationTest;
import org.eclipse.sirius.tests.swtbot.PopupMenuTest;
import org.eclipse.sirius.tests.swtbot.ToolCreationPositionTest;
import org.eclipse.sirius.tests.swtbot.editor.vsm.VSMFieldTest;
import org.eclipse.sirius.tests.swtbot.layout.EdgeCopyPasteFormatTest;
import org.eclipse.sirius.tests.swtbot.layout.StraightenToTest;
import org.eclipse.sirius.tests.swtbot.std.STD001;
import org.eclipse.sirius.tests.swtbot.std.STD006;
import org.eclipse.sirius.tests.swtbot.std.STD007;
import org.eclipse.sirius.tests.swtbot.std.STD008;
import org.eclipse.sirius.tests.swtbot.std.STD009;
import org.eclipse.sirius.tests.swtbot.std.STD010;
import org.eclipse.sirius.tests.swtbot.std.STD011;
import org.eclipse.sirius.tests.swtbot.std.STD013;
import org.eclipse.sirius.tests.swtbot.std.STD017;
import org.eclipse.sirius.tests.swtbot.std.STD018;
import org.eclipse.sirius.tests.swtbot.std.STD019;
import org.eclipse.sirius.tests.swtbot.std.STD022;
import org.eclipse.sirius.tests.swtbot.std.STD025;
import org.eclipse.sirius.tests.swtbot.std.STD028;
import org.eclipse.sirius.tests.swtbot.std.STD030;
import org.eclipse.sirius.tests.swtbot.std.STD043;
import org.eclipse.sirius.tests.swtbot.std.STD044;
import org.eclipse.sirius.tests.swtbot.std.STD048;
import org.eclipse.sirius.tests.swtbot.std.STD049;
import org.eclipse.sirius.tests.swtbot.table.CellEditionTest;
import org.eclipse.sirius.tests.swtbot.table.DeleteLineWithDELShortcutTest;
import org.eclipse.sirius.tests.unit.api.dialect.ExportAsImageTest;
import org.eclipse.sirius.tests.unit.api.vsm.editor.DragAndDropNodeTest;
import org.eclipse.sirius.tests.unit.diagram.action.DeleteFromModelActionTests;
import org.eclipse.sirius.tests.unit.diagram.modeler.ecore.design.EntitiesDiagramFiltersTests;
import org.eclipse.sirius.tests.unit.diagram.modeler.ecore.design.EntitiesDiagramLayersTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * 
 * @author lredor
 */
public class STDSwtbotTestSuite extends TestCase {
    /**
     * Launches the test with the given arguments.
     * 
     * @param args
     *            Arguments of the testCase.
     */
    public static void main(final String[] args) {
        TestRunner.run(suite());
    }

    /**
     * Creates the {@link junit.framework.TestSuite TestSuite} for all the test.
     * 
     * @return The testsuite containing all the tests
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite("STD SWTBOT test suite");
        addPart1(suite);
//        addPart2(suite);
        return suite;
    }
    
    /**
     * Add the first part of the SWTbot tests to the specified suite.
     * 
     * @param suite
     *            the suite into which to add the tests.
     */    
    public static void addPart1(TestSuite suite) {
    	// STD 1.1
        suite.addTestSuite(STD001.class);
        // STD 1.2 missing
        // STD 2 missing
        // STD 3 missing
        // STD 4
        suite.addTestSuite(PopupMenuTest.class);
        // STD 5 scenario obsolete
        // STD 6
        suite.addTestSuite(STD006.class);
        // STD 6.1
        suite.addTestSuite(NavigateToNewRepresentationTest.class);
        // STD 6.2 missing
        // STD 7
        suite.addTestSuite(STD007.class);
        // STD 8
        suite.addTestSuite(STD008.class);
        // STD 9 incomplete, it's not opening other diagrams
        suite.addTestSuite(STD009.class);
        // STD 10 incomplete, it does not test resizing
        suite.addTestSuite(STD010.class);
        // STD 11
        suite.addTestSuite(STD011.class);
        // STD 12 missing
        // STD 13 
        // suite.addTestSuite(STD013.class); // bugged
        suite.addTestSuite(EdgeCreationTest.class); // covers STD 13 scenario
        // STD 14 scenario obsolete
        // STD 15
        suite.addTestSuite(DragAndDropNodeTest.class);
        suite.addTestSuite(DragNDropTest.class);
        // STD 16
        suite.addTestSuite(SequenceSwtBotTestSuite.class);
        // STD 17
        suite.addTestSuite(STD017.class);
        suite.addTestSuite(EntitiesDiagramFiltersTests.class);
        // STD 18
        suite.addTestSuite(STD018.class);
        suite.addTestSuite(EntitiesDiagramLayersTests.class);
        // STD 19 missing (test editeur VSM)
        // STD 20 missing (test editeur VSM)
        // STD 21
        suite.addTestSuite(EdgeCopyPasteFormatTest.class); // Uses a odesign sample in a plugins
        // STD 22 missing (test editeur VSM)
        // STD 23
        suite.addTestSuite(DndWorkspaceSupportTest.class);
        suite.addTestSuite(StraightenToTest.class);
        suite.addTestSuite(STD017.class);
        suite.addTestSuite(VSMFieldTest.class);
        suite.addTestSuite(InitializeSessionTest.class);
        // STD 24
        suite.addTestSuite(CascadingSiriusURITest.class);
        // STD 25
        suite.addTestSuite(DeleteFromModelActionTests.class);
        // STD 26 missing
        // STD 27 missing
        // STD 28 missing
        // STD 29
        suite.addTestSuite(ToolCreationPositionTest.class);
        // STD 30
        suite.addTestSuite(HideRevealDiagramElementsLabelsTest.class);
        // STD 31
        suite.addTestSuite(DeleteFromModelActionTests.class);
        // STD 32
        suite.addTestSuite(DirectEditWithInputLabelTest.class);
        // STD 33
        suite.addTestSuite(DeleteFromModelActionTests.class);
        // STD 34
        suite.addTestSuite(ChildrenPositionStabilityAfterParentResizeTest.class);
        // STD 35
        suite.addTestSuite(ArrangeAllTest.class);
        // STD 36
        suite.addTestSuite(ExportAsImageTest.class);
        // STD 37 (partial)
        suite.addTestSuite(DiagramPrintTest.class);
        // STD 38
        suite.addTestSuite(CellEditionTest.class);
        suite.addTestSuite(DeleteLineWithDELShortcutTest.class);
        // STD 39 missing
        // STD 40
        suite.addTestSuite(DiagramZoomTest.class);
        // STD 41 missing
    }
    
    /**
     * Add the second part of the SWTbot tests to the specified suite.
     * 
     * @param suite
     *            the suite into which to add the tests.
     */    
    public static void addPart2(TestSuite suite) {
        suite.addTestSuite(STD006.class);
        suite.addTestSuite(STD008.class);
        suite.addTestSuite(STD010.class);
    }

    /**
     * Creates the {@link junit.framework.TestSuite TestSuite} for all the
     * disabled test.
     * 
     * @return The test suite containing all the disabled tests.
     */
    public static Test disabledSuite() {
        final TestSuite suite = new TestSuite("STD Disabled SwtBot tests");

        suite.addTestSuite(STD013.class);
        suite.addTestSuite(STD017.class);
        suite.addTestSuite(STD019.class);
        suite.addTestSuite(STD022.class);
        suite.addTestSuite(STD025.class);
        suite.addTestSuite(STD028.class);
        suite.addTestSuite(STD030.class);
        suite.addTestSuite(STD043.class);
        suite.addTestSuite(STD044.class);
        suite.addTestSuite(STD048.class);
        suite.addTestSuite(STD049.class);

        return suite;
    }
}
