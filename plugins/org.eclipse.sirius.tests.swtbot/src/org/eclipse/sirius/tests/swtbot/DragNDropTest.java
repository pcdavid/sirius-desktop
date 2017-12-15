/*******************************************************************************
 * Copyright (c) 2010, 2017 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.swtbot;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gmf.runtime.diagram.ui.editparts.CompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.ui.edit.api.part.AbstractDiagramContainerEditPart;
import org.eclipse.sirius.diagram.ui.edit.api.part.AbstractDiagramNodeEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeContainerEditPart;
import org.eclipse.sirius.tests.swtbot.sequence.condition.CheckNumberOfDescendants;
import org.eclipse.sirius.tests.swtbot.support.api.AbstractSiriusSwtBotGefTestCase;
import org.eclipse.sirius.tests.swtbot.support.api.business.UIDiagramRepresentation.ZoomLevel;
import org.eclipse.sirius.tests.swtbot.support.api.business.UILocalSession;
import org.eclipse.sirius.tests.swtbot.support.api.business.UIResource;
import org.eclipse.sirius.tests.swtbot.support.api.condition.DiagramWithChildrensCondition;
import org.eclipse.sirius.tests.swtbot.support.api.editor.SWTBotSiriusDiagramEditor;
import org.eclipse.sirius.tests.swtbot.support.utils.SWTBotUtils;
import org.eclipse.sirius.tests.swtbot.support.utils.dnd.DndUtil;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * Test class which checks drag&drop from Model Content to the diagram.
 * 
 * @author lchituc
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class DragNDropTest extends AbstractSiriusSwtBotGefTestCase {
    /**
     * The {@link SWTBotSiriusDiagramEditor} to use.
     */
    protected SWTBotSiriusDiagramEditor editor;

    private UIResource sessionAirdResource;

    private UILocalSession localSession;

    private static final String REPRESENTATION_INSTANCE_2BLANK = "new TC1041 representation 2 Blank";

    private static final String REPRESENTATION_INSTANCE_5BLANK = "new TC1041 representation 5 Blank";

    private static final String REPRESENTATION_INSTANCE_6BLANK = "new TC1041 representation 6 Blank";

    private static final String REPRESENTATION_NAME_2 = "TC1041 representation 2 Blank";

    private static final String REPRESENTATION_NAME_5 = "TC1041 representation 5 Blank";

    private static final String REPRESENTATION_NAME_6 = "TC1041 representation 6 Blank";

    private static final String MODEL = "tc1041.ecore";

    private static final String SESSION_FILE = "tc1041.aird";

    private static final String VSM_FILE = "tc1041.odesign";

    private static final String DATA_UNIT_DIR = "data/unit/dragAndDrop/tc-1041/";

    private static final String FILE_DIR = "/";

    private static final String CONTAINER_TO_DRAG_P1 = "P1";

    private static final String CONTAINER_TO_DRAG_P2 = "P2";

    private static final String CONTAINER_TO_DRAG_P3 = "P2.1";

    private static final String CLASS_TO_DRAG_C1 = "C1";

    private static final String ROOTPACKAGE_NAME = "Package";

    private UIResource ecoreEcoreResource;

    private SWTBotTreeItem semanticResourceNode;

    /**
     * True if SnapToGrid is activated on editor, false otherwise.
     */
    protected boolean snapToGrid;

    /**
     * Step used for the grid spacing.
     */
    protected static final int GRID_STEP = 20;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onSetUpBeforeClosingWelcomePage() throws Exception {
        copyFileToTestProject(Activator.PLUGIN_ID, DATA_UNIT_DIR, MODEL, SESSION_FILE, VSM_FILE);
    }

    /**
     * Open the diagram and gather the initial bounds of all the bordered nodes.
     */
    @Override
    protected void onSetUpAfterOpeningDesignerPerspective() throws Exception {
        sessionAirdResource = new UIResource(designerProject, FILE_DIR, SESSION_FILE);
        localSession = designerPerspective.openSessionFromFile(sessionAirdResource);

        ecoreEcoreResource = new UIResource(designerProject, MODEL);
        semanticResourceNode = localSession.getSemanticResourceNode(ecoreEcoreResource);
    }

    /**
     * Open "TC1041 representation 2 Blank" diagram.
     */
    private void openRepresentation2() {
        editor = (SWTBotSiriusDiagramEditor) openRepresentation(localSession.getOpenedSession(), REPRESENTATION_NAME_2, REPRESENTATION_INSTANCE_2BLANK, DDiagram.class);
        if (snapToGrid) {
            editor.setSnapToGrid(true, GRID_STEP, 2);
        }
    }

    /**
     * Open "TC1041 representation 5 Blank" diagram.
     */
    private void openRepresentation5() {
        editor = (SWTBotSiriusDiagramEditor) openRepresentation(localSession.getOpenedSession(), REPRESENTATION_NAME_5, REPRESENTATION_INSTANCE_5BLANK, DDiagram.class);
        if (snapToGrid) {
            editor.setSnapToGrid(true, GRID_STEP, 2);
        }
    }

    /**
     * Open "TC1041 representation 6 Blank" diagram.
     */
    private void openRepresentation6() {
        editor = (SWTBotSiriusDiagramEditor) openRepresentation(localSession.getOpenedSession(), REPRESENTATION_NAME_6, REPRESENTATION_INSTANCE_6BLANK, DDiagram.class);
        if (snapToGrid) {
            editor.setSnapToGrid(true, GRID_STEP, 2);
        }
    }

    /**
     * @throws Exception
     *             Test the drag&drop of P1(EPackage) from the Model Content
     *             view to the diagram. This test is done on a "TC1041
     *             representation 2 Blank" diagram.
     */
    @Test
    public void test_DnDPackageFromMC2DiagramBlank2() throws Exception {

        openRepresentation2();

        try {
            openErrorLogViewByAPI();
            SWTBot errorLogBot = bot.viewByPartName("Error Log").bot();
            int rowCount = errorLogBot.tree().rowCount();

            DndUtil util = new DndUtil(bot.getDisplay());

            // DnD P1(EPackage) from the Model Content view to the diagram
            final SWTBotTreeItem ecoreTreeItem = semanticResourceNode.expandNode(ROOTPACKAGE_NAME).getNode(CONTAINER_TO_DRAG_P1);
            util.dragAndDrop(ecoreTreeItem, editor.getCanvas());

            bot.waitUntil(new DiagramWithChildrensCondition(editor, 1));
            assertEquals("An error message was generated !", rowCount, errorLogBot.tree().rowCount());

            IGraphicalEditPart p1EditPart = (IGraphicalEditPart) editor.getEditPart(CONTAINER_TO_DRAG_P1, DNodeContainerEditPart.class).part();
            checkEditPartLocation(p1EditPart);
        } finally {
            closeErrorLogView();
        }
    }

    /**
     * @throws Exception
     *             Test the drag&drop of P2 from the Model Content view to P1 previously created. This test is done on a
     *             "TC1041 representation 2 Blank" diagram.
     */
    @Test
    public void test_DnDPackageFromMC2ContainerBlank2() throws Exception {

        test_DnDPackageFromMC2DiagramBlank2();

        try {
            openErrorLogViewByAPI();
            SWTBot errorLogBot = bot.viewByPartName("Error Log").bot();
            int rowCount = errorLogBot.tree().rowCount();

            DndUtil util = new DndUtil(bot.getDisplay());

            // Get the location of P1
            SWTBotGefEditPart p1Bot = editor.getEditPart(CONTAINER_TO_DRAG_P1).parent();
            Point p1Location = editor.getBounds(p1Bot).getLocation();

            // DnD P2(EPackage) from the Model Content view to P1
            final SWTBotTreeItem ecoreTreeItem1 = semanticResourceNode.expandNode(ROOTPACKAGE_NAME).getNode(CONTAINER_TO_DRAG_P2);
            util.dragAndDrop(ecoreTreeItem1, editor.getCanvas(), new org.eclipse.swt.graphics.Point(p1Location.x + 25, p1Location.y + 25));
            bot.waitUntil(new DiagramWithChildrensCondition(editor, 1));

            assertEquals("An error message was generated !", rowCount, errorLogBot.tree().rowCount());

            IGraphicalEditPart p2EditPart = (IGraphicalEditPart) editor.getEditPart(CONTAINER_TO_DRAG_P2, DNodeContainerEditPart.class).part();
            checkEditPartLocation(p2EditPart);
        } finally {
            closeErrorLogView();
        }
    }

    /**
     * @throws Exception
     *             Test the drag&drop of P2 from the Model Content view to P1 previously created. This test is done on a
     *             "TC1041 representation 2 Blank" diagram.
     */
    @Test
    public void test_DnDPackageFromMC2ContainerBlank2_zoom200() throws Exception {
        test_DnDPackageFromMC2DiagramBlank2();

        try {

            // TODO Reactivate once #521937 is fixed.
            // editor.zoom(ZoomLevel.ZOOM_200);

            openErrorLogViewByAPI();
            SWTBot errorLogBot = bot.viewByPartName("Error Log").bot();
            int rowCount = errorLogBot.tree().rowCount();

            DndUtil util = new DndUtil(bot.getDisplay());

            // Get the location of P1
            SWTBotGefEditPart p1Bot = editor.getEditPart(CONTAINER_TO_DRAG_P1).parent();

            editor.reveal(p1Bot.part());
            Point targetLocation = editor.getBounds(p1Bot).getCenter();

            // DnD P2(EPackage) from the Model Content view to P1
            final SWTBotTreeItem ecoreTreeItem1 = semanticResourceNode.expandNode(ROOTPACKAGE_NAME).getNode(CONTAINER_TO_DRAG_P2);
            util.dragAndDrop(ecoreTreeItem1, editor.getCanvas(), targetLocation);
            bot.waitUntil(new DiagramWithChildrensCondition(editor, 1));
            bot.waitUntil(new CheckNumberOfDescendants(p1Bot, AbstractDiagramNodeEditPart.class, 1));

            // Get the location of P2
            SWTBotGefEditPart p2Bot = editor.getEditPart(CONTAINER_TO_DRAG_P2).parent();
            Point p2Location = editor.getBounds(p2Bot).getTopLeft();

            if (!snapToGrid) {
                assertEquals(targetLocation.x, p2Location.x, 2);
                assertEquals(targetLocation.y, p2Location.y, 2);
            }

            assertEquals("An error message was generated !", rowCount, errorLogBot.tree().rowCount());

            IGraphicalEditPart p2EditPart = (IGraphicalEditPart) editor.getEditPart(CONTAINER_TO_DRAG_P2, DNodeContainerEditPart.class).part();
            checkEditPartLocation(p2EditPart);
        } finally {
            closeErrorLogView();
            editor.zoom(ZoomLevel.ZOOM_100);
            editor.click(10, 10);
        }
    }

    /**
     * @throws Exception
     *             Test the drag&drop of P2 from P1 to the diagram. This test is done on a "TC1041 representation 2
     *             Blank" diagram.
     */
    @Test
    public void test_DnDNodeFromContainer2DiagramBlank2() throws Exception {

        test_DnDPackageFromMC2ContainerBlank2();

        List<SWTBotGefEditPart> allEditParts = editor.mainEditPart().children();

        assertEquals("Bad number of elements!", 1, allEditParts.size());

        try {
            openErrorLogViewByAPI();
            SWTBot errorLogBot = bot.viewByPartName("Error Log").bot();
            int rowCount = errorLogBot.tree().rowCount();

            // DnD P2 from P1 to the diagram
            // I had to move P2.1 because in the previous Dnd it was moved out
            // of P1 but not to POINT_ON_DIAGRAM coordinates. As a matter of
            // fact there is a layout manager bug.

            // Get the location of P2
            SWTBotGefEditPart p2Bot = editor.getEditPart(CONTAINER_TO_DRAG_P2).parent();

            // Get the location of P1
            SWTBotGefEditPart p1Bot = editor.getEditPart(CONTAINER_TO_DRAG_P1).parent();
            Point targetLocation = editor.getBounds(p1Bot).getTopRight().getTranslated(10, 0);

            editor.drag(p2Bot, targetLocation);
            allEditParts = editor.mainEditPart().children();

            assertEquals("An error message was generated !", rowCount, errorLogBot.tree().rowCount());
            assertEquals("Bad number of elements!", 2, allEditParts.size());

            IGraphicalEditPart p2EditPart = (IGraphicalEditPart) editor.getEditPart(CONTAINER_TO_DRAG_P2, DNodeContainerEditPart.class).part();
            // TODO remove this condition once #521802 is fixed.
            if (!snapToGrid) {
                checkEditPartLocation(p2EditPart);
            }

        } finally {
            closeErrorLogView();
        }
    }

    /**
     * @throws Exception
     *             Test the drag&drop of P2 from the diagram to P1. This test is done on a "TC1041 representation 2
     *             Blank" diagram.
     */
    @Test
    public void test_DnDContainerFromDiagram2ContainerBlank2() throws Exception {

        test_DnDNodeFromContainer2DiagramBlank2();

        try {
            openErrorLogViewByAPI();
            SWTBot errorLogBot = bot.viewByPartName("Error Log").bot();
            int rowCount = errorLogBot.tree().rowCount();

            SWTBotGefEditPart sourceSwtBotPart = editor.getEditPart(CONTAINER_TO_DRAG_P2, AbstractDiagramContainerEditPart.class);
            SWTBotGefEditPart targetSwtBotPart = editor.getEditPart(CONTAINER_TO_DRAG_P1, AbstractDiagramContainerEditPart.class);

            final IGraphicalEditPart targetPart = (IGraphicalEditPart) targetSwtBotPart.part();
            Point targetCenter = targetPart.getFigure().getBounds().getCenter();

            // DnD P2 from the diagram to P1
            editor.drag(sourceSwtBotPart, targetCenter);

            assertEquals("An error message was generated !", rowCount, errorLogBot.tree().rowCount());

            IGraphicalEditPart p2EditPart = (IGraphicalEditPart) editor.getEditPart(CONTAINER_TO_DRAG_P2, DNodeContainerEditPart.class).part();
            checkEditPartLocation(p2EditPart);

        } finally {
            closeErrorLogView();
        }
    }

    /**
     * @throws Exception
     *             Test the drag&drop of P2 from the diagram to P1. This test is done using a "TC1041 representation 2
     *             Blank" diagram.
     */
    @Test
    public void test_DnDContainerFromContainer2ContainerBlank2() throws Exception {

        test_DnDContainerFromDiagram2ContainerBlank2();

        try {
            openErrorLogViewByAPI();
            SWTBot errorLogBot = bot.viewByPartName("Error Log").bot();
            int rowCount = errorLogBot.tree().rowCount();

            DndUtil util = new DndUtil(bot.getDisplay());

            // Get the location of P1
            SWTBotGefEditPart p1Bot = editor.getEditPart(CONTAINER_TO_DRAG_P1).parent();
            Point targetLocation = editor.getBounds(p1Bot).getLeft().getTranslated(-200, 0);

            // DnD P2.1(EPackage) from the Model Content view to the diagram
            final SWTBotTreeItem ecoreTreeItem = semanticResourceNode.expandNode(ROOTPACKAGE_NAME).expandNode(CONTAINER_TO_DRAG_P1).expandNode(CONTAINER_TO_DRAG_P2).getNode(CONTAINER_TO_DRAG_P3);
            util.dragAndDrop(ecoreTreeItem, editor.getCanvas(), new org.eclipse.swt.graphics.Point(targetLocation.x, targetLocation.y));

            bot.waitUntil(new DiagramWithChildrensCondition(editor, 2));
            assertEquals("An error message was generated !", rowCount, errorLogBot.tree().rowCount());

            IGraphicalEditPart p3EditPart = (IGraphicalEditPart) editor.getEditPart(CONTAINER_TO_DRAG_P3, DNodeContainerEditPart.class).part();
            checkEditPartLocation(p3EditPart);

            // Get the location of P2
            SWTBotGefEditPart p2Bot = editor.getEditPart(CONTAINER_TO_DRAG_P2).parent();
            Point p2Location = editor.getBounds(p2Bot).getCenter();

            SWTBotGefEditPart targetSwtBotPart = editor.getEditPart(CONTAINER_TO_DRAG_P3, AbstractDiagramContainerEditPart.class);
            final IGraphicalEditPart targetPart = (IGraphicalEditPart) targetSwtBotPart.part();
            Point targetCenter = targetPart.getFigure().getBounds().getCenter();

            // DnD P2 from P1 to P2.1
            editor.drag(p2Location, targetCenter);

            assertEquals("An error message was generated !", rowCount, errorLogBot.tree().rowCount());

            IGraphicalEditPart p2EditPart = (IGraphicalEditPart) editor.getEditPart(CONTAINER_TO_DRAG_P2, DNodeContainerEditPart.class).part();
            checkEditPartLocation(p2EditPart);

        } finally {
            closeErrorLogView();
        }
    }

    /**
     * @throws Exception
     *             Test the drag&drop of P2 from the diagram to P1. This test is done using a "TC1041 representation 2
     *             Blank" diagram.
     */
    @Test
    public void test_DnDContainerFromContainer2ContainerBlank2_zoom200() throws Exception {

        test_DnDContainerFromDiagram2ContainerBlank2();

        try {
            editor.click(10, 10);
            // TODO Reactivate once #521937 is fixed.
            // editor.zoom(ZoomLevel.ZOOM_200);
            openErrorLogViewByAPI();
            SWTBot errorLogBot = bot.viewByPartName("Error Log").bot();
            int rowCount = errorLogBot.tree().rowCount();

            DndUtil util = new DndUtil(bot.getDisplay());

            editor.reveal(CONTAINER_TO_DRAG_P1);

            // Get the location of P1
            SWTBotGefEditPart p1Bot = editor.getEditPart(CONTAINER_TO_DRAG_P1).parent();
            Point targetLocation = editor.getBounds(p1Bot).getLeft().getTranslated(-200, 0);

            // DnD P2.1(EPackage) from the Model Content view to the diagram
            final SWTBotTreeItem ecoreTreeItem = semanticResourceNode.expandNode(ROOTPACKAGE_NAME).expandNode(CONTAINER_TO_DRAG_P1).expandNode(CONTAINER_TO_DRAG_P2).getNode(CONTAINER_TO_DRAG_P3);
            util.dragAndDrop(ecoreTreeItem, editor.getCanvas(), targetLocation);
            bot.waitUntil(new CheckNumberOfDescendants(p1Bot, AbstractDiagramNodeEditPart.class, 1));

            bot.waitUntil(new DiagramWithChildrensCondition(editor, 2));
            assertEquals("An error message was generated !", rowCount, errorLogBot.tree().rowCount());

            IGraphicalEditPart p3EditPart = (IGraphicalEditPart) editor.getEditPart(CONTAINER_TO_DRAG_P3, DNodeContainerEditPart.class).part();
            checkEditPartLocation(p3EditPart);

            SWTBotGefEditPart p21Bot = editor.getEditPart(CONTAINER_TO_DRAG_P3, AbstractDiagramContainerEditPart.class);
            final IGraphicalEditPart targetPart = (IGraphicalEditPart) p21Bot.part();
            editor.reveal(targetPart);

            // Get the location of P2
            SWTBotGefEditPart p2Bot = editor.getEditPart(CONTAINER_TO_DRAG_P2).parent();
            Point p2Location = editor.getBounds(p2Bot).getCenter();

            Point targetCenter = editor.getBounds(p21Bot).getCenter();

            // DnD P2 from P1 to P2.1
            editor.drag(p2Location, targetCenter);

            // Get the location of P2
            p2Bot = editor.getEditPart(CONTAINER_TO_DRAG_P2).parent();
            p2Location = editor.getBounds(p2Bot).getCenter();
            bot.waitUntil(new CheckNumberOfDescendants(p1Bot, AbstractDiagramNodeEditPart.class, 0));
            bot.waitUntil(new CheckNumberOfDescendants(p21Bot, AbstractDiagramNodeEditPart.class, 1));

            // If the snap is activated, the location will not be the expected one since the view will be snapped on the
            // grid.
            // The snap will be check in checkEditPartLocation method.
            if (!snapToGrid) {
                assertEquals(targetCenter.x, p2Location.x, 4);
                assertEquals(targetCenter.y, p2Location.y, 4);
            }

            assertEquals("An error message was generated !", rowCount, errorLogBot.tree().rowCount());

            IGraphicalEditPart p2EditPart = (IGraphicalEditPart) editor.getEditPart(CONTAINER_TO_DRAG_P2, DNodeContainerEditPart.class).part();
            checkEditPartLocation(p2EditPart);

        } finally {
            closeErrorLogView();
            editor.zoom(ZoomLevel.ZOOM_100);
            editor.click(10, 10);
        }
    }

    /**
     * @throws Exception
     *             Asserts that the DnD of an EClass (C1) from the Model Content view to the diagram is not allowed
     *             using "TC1041 representation 2 Blank" diagram.
     */
    @Test
    public void test_DnDEClassFromMC2DiagramBlank2() throws Exception {

        openRepresentation2();

        try {
            openErrorLogViewByAPI();
            SWTBot errorLogBot = bot.viewByPartName("Error Log").bot();
            int rowCount = errorLogBot.tree().rowCount();

            DndUtil util = new DndUtil(bot.getDisplay());

            // DnD C1(EClass) from the Model Content view to P2. This
            // move shall not be allowed. Asserts that no error message is
            // generated
            final SWTBotTreeItem ecoreTreeItem = semanticResourceNode.expandNode(ROOTPACKAGE_NAME).expandNode(CONTAINER_TO_DRAG_P1).getNode(CLASS_TO_DRAG_C1);
            util.dragAndDrop(ecoreTreeItem, editor.getCanvas());

            // Asserts that C1 graphical element was not created on the diagram
            // and no error
            // message was generated
            List<SWTBotGefEditPart> allEditParts = editor.mainEditPart().children();

            assertEquals("Bad number of elements!", 0, allEditParts.size());
            assertEquals("An error message was generated !", rowCount, errorLogBot.tree().rowCount());
        } finally {
            closeErrorLogView();
        }

    }

    /**
     * @throws Exception
     *             Asserts that the DnD of an EClass(C1) from the Model Content view inside a container (P1) is not
     *             allowed using "TC1041 representation 2 Blank" diagram.
     */
    @Test
    public void test_DnDEClassMC2ContainerBlank2() throws Exception {

        test_DnDPackageFromMC2DiagramBlank2();

        try {
            openErrorLogViewByAPI();
            SWTBot errorLogBot = bot.viewByPartName("Error Log").bot();
            int rowCount = errorLogBot.tree().rowCount();

            DndUtil util = new DndUtil(bot.getDisplay());

            // Get the location of P1
            SWTBotGefEditPart p1Bot = editor.getEditPart(CONTAINER_TO_DRAG_P1).parent();
            Point p1Location = editor.getBounds(p1Bot).getLocation();

            // DnD C1(EClass) from the Model Content view to P2. This
            // move shall not be allowed. Asserts that no error message is
            // generated
            final SWTBotTreeItem ecoreTreeItem = semanticResourceNode.expandNode(ROOTPACKAGE_NAME).expandNode(CONTAINER_TO_DRAG_P1).getNode(CLASS_TO_DRAG_C1);
            util.dragAndDrop(ecoreTreeItem, editor.getCanvas(), new org.eclipse.swt.graphics.Point(p1Location.x + 25, p1Location.y + 25));

            // Asserts that C1 was not created as child of P1 and no error
            // message was generated
            SWTBotGefEditPart targetSwtBotPart = editor.getEditPart(CONTAINER_TO_DRAG_P1, AbstractDiagramContainerEditPart.class);
            Iterable<AbstractDiagramNodeEditPart> filter = Iterables.filter(((CompartmentEditPart) targetSwtBotPart.part().getChildren().get(1)).getChildren(), AbstractDiagramNodeEditPart.class);

            assertEquals("Bad number of elements", 0, Sets.newLinkedHashSet(filter).size());
            assertEquals("An error message was generated !", rowCount, errorLogBot.tree().rowCount());

        } finally {
            closeErrorLogView();
        }
    }

    /**
     * @throws Exception
     *             Test the drag&drop of P1(EPackage) from the Model Content view to the diagram. This test is done
     *             using a "TC1041 representation 5 Blank" diagram.
     */
    @Test
    public void test_DnDPackageFromMC2DiagramBlank5() throws Exception {

        openRepresentation5();

        try {
            openErrorLogViewByAPI();
            SWTBot errorLogBot = bot.viewByPartName("Error Log").bot();
            int rowCount = errorLogBot.tree().rowCount();

            DndUtil util = new DndUtil(bot.getDisplay());

            // DnD P1(EPackage) from the Model Content view to the diagram
            final SWTBotTreeItem ecoreTreeItem = semanticResourceNode.expandNode(ROOTPACKAGE_NAME).getNode(CONTAINER_TO_DRAG_P1);
            util.dragAndDrop(ecoreTreeItem, editor.getCanvas());

            bot.waitUntil(new DiagramWithChildrensCondition(editor, 1));
            assertEquals("An error message was generated !", rowCount, errorLogBot.tree().rowCount());

            IGraphicalEditPart p1EditPart = (IGraphicalEditPart) editor.getEditPart(CONTAINER_TO_DRAG_P1, DNodeContainerEditPart.class).part();
            checkEditPartLocation(p1EditPart);
        } finally {
            closeErrorLogView();
        }
    }

    /**
     * @throws Exception
     *             Test the drag&drop of C1 from the Model Content view to P1 previously created. This test is done
     *             using a "TC1041 representation 5 Blank" diagram.
     */
    @Test
    public void test_DnDClassFromMC2ContainerBlank5() throws Exception {

        test_DnDPackageFromMC2DiagramBlank5();

        try {
            openErrorLogViewByAPI();
            SWTBot errorLogBot = bot.viewByPartName("Error Log").bot();
            int rowCount = errorLogBot.tree().rowCount();

            DndUtil util = new DndUtil(bot.getDisplay());

            // Get the location of P1
            SWTBotGefEditPart p1Bot = editor.getEditPart(CONTAINER_TO_DRAG_P1).parent();
            Point p1Location = editor.getBounds(p1Bot).getLocation();

            // DnD P2(EPackage) from the Model Content view to P1
            final SWTBotTreeItem ecoreTreeItem = semanticResourceNode.expandNode(ROOTPACKAGE_NAME).expandNode(CONTAINER_TO_DRAG_P1).getNode(CLASS_TO_DRAG_C1);
            util.dragAndDrop(ecoreTreeItem, editor.getCanvas(), new org.eclipse.swt.graphics.Point(p1Location.x + 25, p1Location.y + 25));
            bot.waitUntil(new DiagramWithChildrensCondition(editor, 1));

            assertEquals("An error message was generated !", rowCount, errorLogBot.tree().rowCount());

            IGraphicalEditPart c1EditPart = (IGraphicalEditPart) editor.getEditPart(CLASS_TO_DRAG_C1, DNodeContainerEditPart.class).part();
            checkEditPartLocation(c1EditPart);
        } finally {
            closeErrorLogView();
        }
    }

    /**
     * @throws Exception
     *             Test the drag&drop of C1 from P1 to P2 . This test is done using a "TC1041 representation 5 Blank"
     *             diagram.
     */
    @Test
    public void test_DnDClassFromContainer2ContainerBlank5() throws Exception {

        test_DnDClassFromMC2ContainerBlank5();

        try {
            openErrorLogViewByAPI();
            SWTBot errorLogBot = bot.viewByPartName("Error Log").bot();
            int rowCount = errorLogBot.tree().rowCount();

            DndUtil util = new DndUtil(bot.getDisplay());

            // Get the location of P1
            SWTBotGefEditPart p1Bot = editor.getEditPart(CONTAINER_TO_DRAG_P1).parent();
            Point targetLocation = editor.getBounds(p1Bot).getLeft().getTranslated(-200, 0);

            // DnD P2(EPackage) from the Model Content view to the diagram
            final SWTBotTreeItem ecoreTreeItem = semanticResourceNode.expandNode(ROOTPACKAGE_NAME).getNode(CONTAINER_TO_DRAG_P2);
            util.dragAndDrop(ecoreTreeItem, editor.getCanvas(), new org.eclipse.swt.graphics.Point(targetLocation.x, targetLocation.y));
            bot.waitUntil(new DiagramWithChildrensCondition(editor, 2));
            assertEquals("An error message was generated !", rowCount, errorLogBot.tree().rowCount());

            IGraphicalEditPart p2EditPart = (IGraphicalEditPart) editor.getEditPart(CONTAINER_TO_DRAG_P2, DNodeContainerEditPart.class).part();
            checkEditPartLocation(p2EditPart);

            // Get the location of C1
            SWTBotGefEditPart c1Bot = editor.getEditPart(CLASS_TO_DRAG_C1).parent();
            Point c1Location = editor.getBounds(c1Bot).getLocation();

            // Get the center of P2 (the target)
            SWTBotGefEditPart targetSwtBotPart = editor.getEditPart(CONTAINER_TO_DRAG_P2, AbstractDiagramContainerEditPart.class);
            final IGraphicalEditPart targetPart = (IGraphicalEditPart) targetSwtBotPart.part();
            Point targetCenter = targetPart.getFigure().getBounds().getCenter();

            // DnD C1 from P1 to P2
            editor.drag(c1Location, targetCenter);

            assertEquals("An error message was generated !", rowCount, errorLogBot.tree().rowCount());

            IGraphicalEditPart c1EditPart = (IGraphicalEditPart) editor.getEditPart(CLASS_TO_DRAG_C1, DNodeContainerEditPart.class).part();
            checkEditPartLocation(c1EditPart);

        } finally {
            closeErrorLogView();
        }
    }

    /**
     * @throws Exception
     *             Test the drag&drop of C1(EClass) from the Model Content view to the diagram. This move should not be
     *             allowed and no error message should be generated.
     */
    @Test
    public void test_DnDClassFromMC2DiagramBlank5() throws Exception {

        openRepresentation5();

        try {
            openErrorLogViewByAPI();
            SWTBot errorLogBot = bot.viewByPartName("Error Log").bot();
            int rowCount = errorLogBot.tree().rowCount();

            DndUtil util = new DndUtil(bot.getDisplay());

            // DnD P1(EPackage) from the Model Content view to the diagram
            final SWTBotTreeItem ecoreTreeItem = semanticResourceNode.expandNode(ROOTPACKAGE_NAME).expandNode(CONTAINER_TO_DRAG_P1).getNode(CLASS_TO_DRAG_C1);
            util.dragAndDrop(ecoreTreeItem, editor.getCanvas());

            // Asserts that C1 graphical element was not created on the diagram
            // and no error
            // message was generated
            List<SWTBotGefEditPart> allEditParts = editor.mainEditPart().children();

            assertEquals("Bad number of elements!", 0, allEditParts.size());
            assertEquals("An error message was generated !", rowCount, errorLogBot.tree().rowCount());
        } finally {
            closeErrorLogView();
        }
    }

    /**
     * Test the drag&drop of C1(EClass) from the Model Content view to the diagram. This move should be allowed (as it
     * is in a transient layer enabled by default) and no error message should be generated.
     * 
     * @throws Exception
     *             In case of problem
     */
    @Test
    public void test_DnDClassFromMC2DiagramBlank6FromATransientLayerTool() throws Exception {

        openRepresentation6();

        try {
            openErrorLogViewByAPI();
            SWTBot errorLogBot = bot.viewByPartName("Error Log").bot();
            int rowCount = errorLogBot.tree().rowCount();

            DndUtil util = new DndUtil(bot.getDisplay());

            // DnD P1(EPackage) from the Model Content view to the diagram
            final SWTBotTreeItem ecoreTreeItem = semanticResourceNode.expandNode(ROOTPACKAGE_NAME).expandNode(CONTAINER_TO_DRAG_P1).getNode(CLASS_TO_DRAG_C1);
            util.dragAndDrop(ecoreTreeItem, editor.getCanvas());

            // Asserts that C1 graphical element was created on the diagram and
            // no error message was generated
            List<SWTBotGefEditPart> allEditParts = editor.mainEditPart().children();

            assertEquals("Bad number of elements!", 1, allEditParts.size());
            assertEquals("An error message was generated !", rowCount, errorLogBot.tree().rowCount());
        } finally {
            closeErrorLogView();
        }
    }

    /**
     * @throws Exception
     *             Test the drag&drop of P2 from the Model Content view to P1 previously created. This move should not
     *             be allowed and no error message should be generated.
     */
    @Test
    public void test_DnDPackageFromMC2ContainerBlank5() throws Exception {

        test_DnDPackageFromMC2DiagramBlank5();

        try {
            openErrorLogViewByAPI();
            SWTBot errorLogBot = bot.viewByPartName("Error Log").bot();
            int rowCount = errorLogBot.tree().rowCount();

            DndUtil util = new DndUtil(bot.getDisplay());

            // Get the location of P1
            SWTBotGefEditPart p1Bot = editor.getEditPart(CONTAINER_TO_DRAG_P1).parent();
            Point p1Location = editor.getBounds(p1Bot).getLocation();

            // DnD P2(EPackage) from the Model Content view to P1
            final SWTBotTreeItem ecoreTreeItem = semanticResourceNode.expandNode(ROOTPACKAGE_NAME).getNode(CONTAINER_TO_DRAG_P2);
            util.dragAndDrop(ecoreTreeItem, editor.getCanvas(), new org.eclipse.swt.graphics.Point(p1Location.x + 25, p1Location.y + 25));

            // Asserts that P2 was not created as child of P1 and no error
            // message was generated
            SWTBotGefEditPart targetSwtBotPart = editor.getEditPart(CONTAINER_TO_DRAG_P1, AbstractDiagramContainerEditPart.class);
            Iterable<AbstractDiagramNodeEditPart> filter = Iterables.filter(((CompartmentEditPart) targetSwtBotPart.part().getChildren().get(1)).getChildren(), AbstractDiagramNodeEditPart.class);

            assertEquals("Bad number of elements", 0, Sets.newLinkedHashSet(filter).size());
            assertEquals("An error message was generated !", rowCount, errorLogBot.tree().rowCount());
        } finally {
            closeErrorLogView();
        }
    }

    /**
     * @throws Exception
     *             Close the Error log view
     */
    protected void closeErrorLogView() throws Exception {
        SWTBotView errorView = bot.viewByPartName("Error Log");
        if (errorView.isActive()) {
            errorView.close();
        }
    }

    @Override
    @After
    public void tearDown() throws Exception {
        if (editor != null) {
            editor.close();
        }
        SWTBotUtils.waitAllUiEvents();
        localSession.close(false);
        super.tearDown();
    }

    /**
     * Check if EditPart is on Diagram.
     * 
     * @param editPart
     *            the edit part to check
     */
    protected void checkEditPartLocation(IGraphicalEditPart editPart) {
        assertNotNull("No container edit part found with this name", editPart);
    }
}
