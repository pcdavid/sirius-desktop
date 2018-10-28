/*******************************************************************************
 * Copyright (c) 2018 THALES GLOBAL SERVICES and others.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.swtbot;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellIsActive;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.AbstractBorderItemEditPart;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.DNodeList;
import org.eclipse.sirius.diagram.DNodeListElement;
import org.eclipse.sirius.diagram.HideFilter;
import org.eclipse.sirius.diagram.HideLabelFilter;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DDiagramEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DEdgeBeginNameEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNode3EditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNode4EditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeContainerEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeContainerViewNodeContainerCompartmentEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeListEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeListElementEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeNameEditPart;
import org.eclipse.sirius.tests.swtbot.support.api.condition.OperationDoneCondition;
import org.eclipse.sirius.tests.swtbot.support.api.editor.SWTBotSiriusDiagramEditor;
import org.eclipse.sirius.tests.swtbot.support.api.editor.SWTBotSiriusHelper;
import org.eclipse.sirius.tests.swtbot.support.utils.SWTBotUtils;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefConnectionEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarDropDownButton;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Tests that the show/hide mode behave as expected and that the menu to change the active mode is also working.
 * 
 * @author <a href="mailto:pierre.guilet@obeo.fr">Pierre Guilet</a>
 *
 */
public class EditModeTest extends AbstractModeTest {

    private void reconnectEdge(String source, String target, String newTargetName) {
        SWTBotGefEditPart sourceEditPartBot = getEditPart(source, AbstractBorderItemEditPart.class);
        SWTBotGefEditPart targetEditPartBot = getEditPart(target, AbstractBorderItemEditPart.class);
        SWTBotGefEditPart newtargetEditPartBot = getEditPart(newTargetName, AbstractBorderItemEditPart.class);
        SWTBotGefConnectionEditPart connectionEditPartBot = editor.getConnectionEditPart(sourceEditPartBot, targetEditPartBot).get(0);

        // Reconnect target of first connection
        PointList connection1Points = ((AbstractConnectionEditPart) connectionEditPartBot.part()).getConnectionFigure().getPoints().getCopy();
        Point from = connection1Points.getLastPoint();
        Point to = editor.getBounds(newtargetEditPartBot).getLocation();
        connectionEditPartBot.select();
        editor.drag(from, to);
    }

    private void testReconnect(Mode startingMode, Mode testedMode) {
        switch (startingMode) {
        case LAYOUTING:
            activateLayoutingModeUsingTabbar();
            break;
        case SHOWHIDE:
            activateShowHideModeUsingTabbar();
            break;
        case STANDARD:
            reconnectEdge("new EClass 3", "new EClass 4", "new EClass 5");
            // -> tool should have been applied
            assertEdgeReconnectionToolHasBeenApplied("new EClass 4", "eRef", true);
            editor.save();
            editor.setFocus();
            break;
        default:
            break;
        }

        switch (testedMode) {
        case LAYOUTING:
            activateLayoutingModeUsingTabbar();
            break;
        case SHOWHIDE:
            activateShowHideModeUsingTabbar();
            break;
        default:
            break;
        }

        // applying tool
        reconnectEdge("new EClass 5", "new EClass 4", "new EClass 3");
        SWTBotUtils.waitAllUiEvents();
        // -> tool should not have been applied
        assertEdgeReconnectionToolHasBeenApplied("new EClass 4", "eRef2", false);

        if (startingMode == Mode.STANDARD) {
            editor.close();
            editor = (SWTBotSiriusDiagramEditor) openRepresentation(localSession.getOpenedSession(), REPRESENTATION_DESCRIPTION_NAME, REPRESENTATION_INSTANCE_NAME, DDiagram.class);
            editor = SWTBotSiriusHelper.getSiriusDiagramEditor(editor.getTitle());
            reconnectEdge("new EClass 3", "new EClass 5", "EClass7");

            // -> tool should have been applied
            assertEdgeReconnectionToolHasBeenApplied("new EClass 4", "eRef", true);
        }
    }

    private void testDragAndDrop(Mode startingMode, Mode testedMode) {

        switch (startingMode) {
        case LAYOUTING:
            activateLayoutingModeUsingTabbar();
            break;
        case SHOWHIDE:
            activateShowHideModeUsingTabbar();
            break;
        case STANDARD:
            editor.drag(getEditPart("new EClass 5").parent(), ((GraphicalEditPart) getEditPart("new EPackage 2").part()).getFigure().getBounds().getLocation());
            SWTBotUtils.waitAllUiEvents();
            // -> tool should have been applied
            assertDragAndDropToolHasBeenApplied("new EClass 5", "new EPackage 3", true);
            SWTBotGefEditPart rootEditPart = editor.rootEditPart();
            editor.click(rootEditPart);
            bot.waitUntil(new ICondition() {

                @Override
                public boolean test() throws Exception {
                    ISelection selection = editor.getSelection();
                    return ((StructuredSelection) selection).getFirstElement() instanceof DDiagramEditPart;
                }

                @Override
                public String getFailureMessage() {
                    return "diagram was never selected";
                }

                @Override
                public void init(SWTBot bot) {

                }
            });
            editor.save();
            editor.setFocus();
            break;
        default:
            break;
        }

        switch (testedMode) {
        case LAYOUTING:
            activateLayoutingModeUsingTabbar();
            break;
        case SHOWHIDE:
            activateShowHideModeUsingTabbar();
            break;
        default:
            break;
        }

        // applying tool
        SWTBotUtils.waitAllUiEvents();
        editor.drag(getEditPart("new EClass 3").parent(), ((GraphicalEditPart) getEditPart("new EPackage 2").part()).getFigure().getBounds().getLocation());
        SWTBotUtils.waitAllUiEvents();
        // -> tool should not have been applied
        assertDragAndDropToolHasBeenApplied("new EClass 3", "new Package 1", false);

        if (startingMode == Mode.STANDARD) {
            // reopening diagram and applying tool
            editor = (SWTBotSiriusDiagramEditor) openRepresentation(localSession.getOpenedSession(), REPRESENTATION_DESCRIPTION_NAME, REPRESENTATION_INSTANCE_NAME, DDiagram.class);
            editor = SWTBotSiriusHelper.getSiriusDiagramEditor(editor.getTitle());

            editor.drag(getEditPart("new EClass 3").parent(), ((GraphicalEditPart) getEditPart("new EPackage 2").part()).getFigure().getBounds().getLocation());
            SWTBotUtils.waitAllUiEvents();
            // -> tool should have been applied
            assertDragAndDropToolHasBeenApplied("new EClass 3", "new Package 1", true);
        }
    }

    private void testDirectEdit(Mode startingMode, Mode testedMode) {
        switch (startingMode) {
        case LAYOUTING:
            activateLayoutingModeUsingTabbar();
            break;
        case SHOWHIDE:
            activateShowHideModeUsingTabbar();
            break;
        case STANDARD:

            // applying tool
            editor.directEditType("newValue", getEditPart("new EClass 3"));
            SWTBotUtils.waitAllUiEvents();
            // -> tool should have been applied
            assertDirectEditToolHasBeenApplied("new EClass 3", true);
            editor.save();
            editor.setFocus();
            break;
        default:
            break;
        }

        switch (testedMode) {
        case LAYOUTING:
            activateLayoutingModeUsingTabbar();
            break;
        case SHOWHIDE:
            activateShowHideModeUsingTabbar();
            break;
        default:
            break;
        }

        // applying tool
        editor.directEditType("newValue2", getEditPart("new EClass 4"));
        SWTBotUtils.waitAllUiEvents();
        // -> tool should not have been applied
        assertDirectEditToolHasBeenApplied("new EClass 4", false);

        if (startingMode == Mode.STANDARD) {
            // reopening diagram and applying tool
            editor.close();
            editor = (SWTBotSiriusDiagramEditor) openRepresentation(localSession.getOpenedSession(), REPRESENTATION_DESCRIPTION_NAME, REPRESENTATION_INSTANCE_NAME, DDiagram.class);
            editor = SWTBotSiriusHelper.getSiriusDiagramEditor(editor.getTitle());
            editor.directEditType("newValue3", getEditPart("new EClass 4"));
            SWTBotUtils.waitAllUiEvents();
            // -> tool should have been applied
            assertDirectEditToolHasBeenApplied("new EClass 4", true);
        }
    }

    private enum Mode {
        STANDARD, LAYOUTING, SHOWHIDE
    }

    /**
     * Ensures that activating the Layouting mode when standard mode is activated forbids edge DnD tools applying.
     */
    public void testLayoutingModeOnDragAndDropFromStandardMode() {
        testDragAndDrop(Mode.STANDARD, Mode.LAYOUTING);
    }

    /**
     * Ensures that activating the Layouting mode when standard mode is activated forbids edge direct edit tools
     * applying.
     */
    public void testLayoutingModeOnDirectEditFromStandardMode() {
        testDirectEdit(Mode.STANDARD, Mode.LAYOUTING);
    }

    /**
     * Ensures that activating the Layouting mode when standard mode is activated forbids edge reconnection tools
     * applying.
     */
    public void testLayoutingModeOnEdgeReconnectionFromStandardMode() {
        testReconnect(Mode.STANDARD, Mode.LAYOUTING);
    }

    /**
     * Ensures that activating the Layouting mode when show/hide mode is activated forbids edge DnD tools applying.
     */
    public void testLayoutingModeOnDragAndDropFromShowHideMode() {
        testDragAndDrop(Mode.SHOWHIDE, Mode.LAYOUTING);
    }

    /**
     * Ensures that activating the Layouting mode when show/hide mode is activated forbids edge direct edit tools
     * applying.
     */
    public void testLayoutingModeOnDirectEditFromShowHideMode() {
        testDirectEdit(Mode.SHOWHIDE, Mode.LAYOUTING);
    }

    /**
     * Ensures that activating the Layouting mode when show/hide mode is activated forbids edge reconnection tools
     * applying.
     */
    public void testLayoutingModeOnEdgeReconnectionFromShowHideMode() {
        testReconnect(Mode.SHOWHIDE, Mode.LAYOUTING);
    }

    /**
     * Ensures that activating the showing mode when standard mode is activated forbids edge DnD tools applying.
     */
    public void testShowHideOnDragAndDropFromStandardMode() {
        testDragAndDrop(Mode.STANDARD, Mode.SHOWHIDE);
    }

    /**
     * Ensures that activating the showing mode when standard mode is activated forbids edge direct edit tools applying.
     */
    public void testShowHideOnDirectEditFromStandardMode() {
        testDirectEdit(Mode.STANDARD, Mode.SHOWHIDE);
    }

    /**
     * Ensures that activating the showing mode when standard mode is activated forbids edge reconnection tools
     * applying.
     */
    public void testShowHideOnEdgeReconnectionFromStandardMode() {
        testReconnect(Mode.STANDARD, Mode.SHOWHIDE);
    }

    /**
     * Ensures that activating the show/hide mode when Layouting mode is activated forbids edge DnD tools applying.
     */
    public void testShowHideOnDragAndDropFromLayoutingMode() {
        testDragAndDrop(Mode.LAYOUTING, Mode.SHOWHIDE);
    }

    /**
     * Ensures that activating the show/hide mode when Layouting mode is activated forbids edge direct edit tools
     * applying.
     */
    public void testShowHideOnDirectEditFromSLayoutingMode() {
        testDirectEdit(Mode.LAYOUTING, Mode.SHOWHIDE);
    }

    /**
     * Ensures that activating the show/hide mode when Layouting mode is activated forbids edge reconnection tools
     * applying.
     */
    public void testShowHideOnEdgeReconnectionFromLayoutingMode() {
        testReconnect(Mode.LAYOUTING, Mode.SHOWHIDE);
    }

    /**
     * Verify that a double click on a visible node label hides it and vice versa.
     */
    public void testShowHideDoubleClickOnNodeLabel() {
        SWTBotGefEditPart swtBotDNodeEditPart = getEditPart("new EClass 4", DNode4EditPart.class);
        EditPart part = swtBotDNodeEditPart.part();
        DNode element = (DNode) ((Node) part.getModel()).getElement();
        assertFalse("The node should not have its label filtered.", element.getGraphicalFilters().stream().anyMatch(HideLabelFilter.class::isInstance));

        activateShowHideModeUsingTabbar();
        SWTBotGefEditPart swtBotEditPart = getEditPart("new EClass 4", DNodeNameEditPart.class);
        hideShow(element, swtBotEditPart, true);
    }

    /**
     * Make a double click on the diagram element and verifies it is hidden. And do a double click again and verifies it
     * is shown again.
     * 
     * @param element
     *            element to double click
     * @param swtBotEditPart
     *            the corresponding part.
     */
    private void hideShow(DDiagramElement element, SWTBotGefEditPart swtBotEditPart, boolean isLabelHidden) {
        OperationDoneCondition done = new OperationDoneCondition();
        swtBotEditPart.doubleClick();
        SWTBotUtils.waitAllUiEvents();
        bot.waitUntil(done);
        SWTBotUtils.waitAllUiEvents();

        if (isLabelHidden) {
            assertTrue("The node should have its label filtered.", element.getGraphicalFilters().stream().anyMatch(HideLabelFilter.class::isInstance));
        } else {
            assertTrue("The node should be filtered.", element.getGraphicalFilters().stream().anyMatch(HideFilter.class::isInstance));

        }
        done = new OperationDoneCondition();
        swtBotEditPart.doubleClick();
        SWTBotUtils.waitAllUiEvents();
        bot.waitUntil(done);
        SWTBotUtils.waitAllUiEvents();

        if (isLabelHidden) {
            assertFalse("The node should not have its label filtered.", element.getGraphicalFilters().stream().anyMatch(HideLabelFilter.class::isInstance));
        } else {
            assertFalse("The node should not be filtered.", element.getGraphicalFilters().stream().anyMatch(HideFilter.class::isInstance));
        }
    }

    /**
     * Verify that a double click on a visible bordered node hides it and vice versa.
     */
    public void testShowHideDoubleClickOnBorderedNode() {
        SWTBotGefEditPart swtBotEditPart = getEditPart("new EClass 4", DNode4EditPart.class);
        EditPart part = swtBotEditPart.part();
        DNode element = (DNode) ((Node) part.getModel()).getElement();
        assertFalse("The node should not have its label filtered.", element.getGraphicalFilters().stream().anyMatch(HideFilter.class::isInstance));

        activateShowHideModeUsingTabbar();
        hideShow(element, swtBotEditPart, false);
    }

    /**
     * Verify that a double click on a visible container hides it and vice versa.
     */
    public void testShowHideDoubleClickOnContainer() {
        SWTBotGefEditPart swtBotEditPart = getEditPart("new EPackage 2", DNodeContainerEditPart.class);
        EditPart part = swtBotEditPart.part();
        DNodeContainer element = (DNodeContainer) ((Node) part.getModel()).getElement();
        assertFalse("The container should not have its label filtered.", element.getGraphicalFilters().stream().anyMatch(HideFilter.class::isInstance));

        activateShowHideModeUsingTabbar();

        hideShow(element, swtBotEditPart, false);
    }

    /**
     * Verify that a double click on a visible edge hides it and vice versa.
     */
    // public void testShowHideDoubleClickOnEdge() {
    // SWTBotGefEditPart swtBotDNodeEditPart = getEdgePart("EPackage4");
    // EditPart part = swtBotDNodeEditPart.part();
    // DEdge element = (DEdge) ((Edge) part.getModel()).getElement();
    // assertFalse("The edge should not have its label filtered.",
    // element.getGraphicalFilters().stream().anyMatch(HideFilter.class::isInstance));
    //
    // activateShowHideModeUsingTabbar();
    // editor.reveal(part);
    // SWTBotUtils.waitAllUiEvents();
    // swtBotDNodeEditPart.select();
    // edgedoubleclick((DEdgeEditPart) part);
    //
    // SWTBotUtils.waitAllUiEvents();
    //
    // assertTrue("The edge should be filtered.",
    // element.getGraphicalFilters().stream().anyMatch(HideFilter.class::isInstance));
    //
    // swtBotDNodeEditPart.doubleClick();
    // SWTBotUtils.waitAllUiEvents();
    //
    // assertFalse("The edge should not be filtered.",
    // element.getGraphicalFilters().stream().anyMatch(HideFilter.class::isInstance));
    //
    // }

    // /**
    // * @param part
    // *
    // */
    // private void edgedoubleclick(DEdgeEditPart part) {
    // PointList newPoints = ((ViewEdgeFigure) part.getFigure()).getPoints().getCopy();
    // Point firstPoint = newPoints.getFirstPoint();
    // editor.getSWTBotGefViewer().doubleClick(firstPoint.x + 5, firstPoint.y); // TODO Auto-generated method stub
    //
    // }

    /**
     * Verify that a double click on a visible edge labels hide it and vice versa.
     */
    public void testShowHideDoubleClickOnEdgeLabel() {
        SWTBotGefEditPart swtBotEditPart = getEditPart("beginLabelTest", DEdgeBeginNameEditPart.class);
        EditPart part = swtBotEditPart.part();
        DEdge element = (DEdge) ((Node) part.getModel()).getElement();
        assertFalse("The edge should not have its label filtered.", element.getGraphicalFilters().stream().anyMatch(HideLabelFilter.class::isInstance));

        activateShowHideModeUsingTabbar();
        SWTBotUtils.waitAllUiEvents();
        editor.reveal(part);
        hideShow(element, swtBotEditPart, true);
    }

    /**
     * Verify that a double click on a visible list container hides it and vice versa.
     */
    public void testShowHideDoubleClickOnListContainer() {
        switchLayer("L2");
        switchLayer("L3");
        SWTBotUtils.waitAllUiEvents();

        SWTBotGefEditPart swtBotEditPart = getEditPart("new Package 1", DNodeListEditPart.class);
        EditPart part = swtBotEditPart.part();
        DNodeList element = (DNodeList) ((Node) part.getModel()).getElement();
        assertFalse("The node should not have its label filtered.", element.getGraphicalFilters().stream().anyMatch(HideFilter.class::isInstance));

        activateShowHideModeUsingTabbar();
        hideShow(element, swtBotEditPart, false);
    }

    /**
     * Verify that a double click on a visible list node hides it and vice versa.
     */
    public void testShowHideDoubleClickOnListNode() {
        switchLayer("L2");
        switchLayer("L3");
        SWTBotUtils.waitAllUiEvents();

        SWTBotGefEditPart swtBotEditPart = getEditPart("new EClass 3", DNodeListElementEditPart.class);
        EditPart part = swtBotEditPart.part();
        DNodeListElement element = (DNodeListElement) ((Node) part.getModel()).getElement();
        assertFalse("The node should not have its label filtered.", element.getGraphicalFilters().stream().anyMatch(HideFilter.class::isInstance));

        activateShowHideModeUsingTabbar();
        hideShow(element, swtBotEditPart, false);
    }

    /**
     * Verify that a double click on a visible node hides it and vice versa.
     */
    public void testShowHideDoubleClickOnFilteredNode() {
        switchLayer("L2");
        switchLayer("L4");
        SWTBotGefEditPart swtBotEditPart = getEditPart("new EClass 4", DNode3EditPart.class);
        EditPart part = swtBotEditPart.part();
        DNode element = (DNode) ((Node) part.getModel()).getElement();
        assertFalse("The node should not have its label filtered.", element.getGraphicalFilters().stream().anyMatch(HideFilter.class::isInstance));

        activateShowHideModeUsingTabbar();
        hideShow(element, swtBotEditPart, false);
    }

    /**
     * Verify that a double click on a visible compartment container hides it and vice versa.
     */
    public void testShowHideDoubleClickOnCompartmentContainer() {
        switchLayer("L2");
        switchLayer("L5");

        SWTBotUtils.waitAllUiEvents();

        List<SWTBotGefEditPart> editParts = getAvailableCompartmentEditParts();

        SWTBotGefEditPart swtBotEditPart = editParts.get(0);
        EditPart part = swtBotEditPart.part();
        DNodeContainer element = (DNodeContainer) ((Node) part.getModel()).getElement();
        assertFalse("The compartment should not be filtered.", element.getGraphicalFilters().stream().anyMatch(HideFilter.class::isInstance));

        activateShowHideModeUsingTabbar();
        hideShow(element, swtBotEditPart, false);
    }

    /**
     * When an element specified in a layer is reusing a mapping and if a user make a double click on it when it is
     * invisible because the layer is not activated, then the user should be asked to activate all layers attached to
     * it. The current implementation is not capable of knowing the layer to activate among all related ones.
     * 
     */
    public void testShowHideDoubleClickOnReusedMapping() {
        switchLayer("L2");
        switchLayer("L7");
        SWTBotUtils.waitAllUiEvents();
        SWTBotGefEditPart editPart = getEditPart("new EClass 4", DNodeEditPart.class);

        DNode element = (DNode) ((Node) editPart.part().getModel()).getElement();

        activateShowHideModeUsingTabbar();
        switchLayer("L7");
        SWTBotUtils.waitAllUiEvents();
        assertFalse("The node should not be visible.", element.isVisible());
        editor.reveal(editPart.part());
        editPart.doubleClick();
        bot.waitUntil(shellIsActive("Filter/layer update confirmation"));
        bot.activeShell().bot().button("OK").click();
        SWTBotUtils.waitAllUiEvents();
        assertTrue("The node should be visible.", element.isVisible());

    }

    /**
     * Verify that a double click on an invisible container filtered by a filter asks the user to remove this filter and
     * make it visible if yes is chosen.
     */
    public void testShowHideDoubleClickOnFilteredContainer() {
        SWTBotToolbarDropDownButton toolbarDropDownButton = editor.bot().toolbarDropDownButton(3);
        ICondition done = new OperationDoneCondition();
        toolbarDropDownButton.menuItem("f1").click();
        SWTBotUtils.waitAllUiEvents();
        bot.waitUntil(done);

        try {
            getEditPart("new EPackage 2", DNodeContainerEditPart.class);
            fail("Part should be not visible at all.");
        } catch (WidgetNotFoundException | AssertionError | TimeoutException e) {
        }

        activateShowHideModeUsingTabbar();
        SWTBotGefEditPart swtBotDNodeEditPart = getEditPart("new EPackage 2", DNodeContainerEditPart.class);
        EditPart part = swtBotDNodeEditPart.part();
        DNodeContainer element = (DNodeContainer) ((Node) part.getModel()).getElement();
        assertFalse("The container should not be visible", element.isVisible());

        swtBotDNodeEditPart.doubleClick();

        bot.waitUntil(shellIsActive("Filter/layer update confirmation"));
        bot.activeShell().bot().button("OK").click();
        SWTBotUtils.waitAllUiEvents();

        assertTrue("The container should be visible", element.isVisible());
    }

    /**
     * Verify that a double click on an invisible container that belongs to a layer that is not activated asks the user
     * to activate it. And that activating the layer make the element visible.
     */
    public void testShowHideDoubleClickOnUnshownContainer() {
        switchLayer("L2");
        SWTBotUtils.waitAllUiEvents();

        activateShowHideModeUsingTabbar();

        SWTBotGefEditPart swtBotDNodeEditPart = getEditPart("new EPackage 2", DNodeContainerEditPart.class);
        EditPart part = swtBotDNodeEditPart.part();
        DNodeContainer element = (DNodeContainer) ((Node) part.getModel()).getElement();
        assertFalse("The container should not be visible", element.isVisible());

        swtBotDNodeEditPart.doubleClick();

        bot.waitUntil(shellIsActive("Filter/layer update confirmation"));
        bot.activeShell().bot().button("OK").click();
        SWTBotUtils.waitAllUiEvents();

        assertTrue("The container should be visible", element.isVisible());
    }

    private List<SWTBotGefEditPart> getAvailableCompartmentEditParts() {
        bot.waitUntil(new ICondition() {

            @Override
            public boolean test() throws Exception {
                List<SWTBotGefEditPart> editParts = getCompartmentEditParts();
                return editParts.size() > 0;
            }

            @Override
            public String getFailureMessage() {
                return "No compartment parts found.";
            }

            @Override
            public void init(SWTBot bot) {
            }

        });
        return getCompartmentEditParts();
    }

    private List<SWTBotGefEditPart> getCompartmentEditParts() {
        List<SWTBotGefEditPart> editParts = editor.getSWTBotGefViewer().editParts(new Matcher<EditPart>() {

            @Override
            public void describeTo(Description description) {
            }

            @Override
            public boolean matches(Object item) {
                EditPart editPart = (EditPart) item;
                return editPart instanceof DNodeContainerViewNodeContainerCompartmentEditPart;
            }

            @Override
            public void describeMismatch(Object item, Description mismatchDescription) {
            }

            @Override
            public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
            }
        });
        return editParts;
    }

    private SWTBotGefEditPart getEditPart(String partName, Class classType) {
        bot.waitUntil(new ICondition() {

            @Override
            public boolean test() throws Exception {
                try {
                    editor.getEditPart(partName, classType);
                } catch (WidgetNotFoundException e) {
                    return false;
                }
                return true;
            }

            @Override
            public String getFailureMessage() {
                return partName + " not found.";
            }

            @Override
            public void init(SWTBot bot) {
            }
        });
        SWTBotGefEditPart swtBotDNodeEditPart = editor.getEditPart(partName, classType);
        return swtBotDNodeEditPart;
    }

    private SWTBotGefEditPart getEditPart(String partName) {
        return getEditPart(partName, GraphicalEditPart.class);
    }
}
