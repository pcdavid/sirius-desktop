/*******************************************************************************
 * Copyright (c) 2017, 2023 Obeo and others.
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.NoteEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.NoteAttachmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.TextEditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.sirius.business.api.query.IdentifiedElementQuery;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.description.filter.FilterDescription;
import org.eclipse.sirius.diagram.ui.edit.api.part.AbstractDiagramContainerEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeListEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.SiriusNoteEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.SiriusTextEditPart;
import org.eclipse.sirius.diagram.ui.provider.Messages;
import org.eclipse.sirius.diagram.ui.tools.internal.preferences.SiriusDiagramUiInternalPreferencesKeys;
import org.eclipse.sirius.ext.gmf.runtime.editparts.GraphicalHelper;
import org.eclipse.sirius.tests.swtbot.support.api.AbstractSiriusSwtBotGefTestCase;
import org.eclipse.sirius.tests.swtbot.support.api.business.UIResource;
import org.eclipse.sirius.tests.swtbot.support.api.condition.CheckSelectedCondition;
import org.eclipse.sirius.tests.swtbot.support.api.editor.SWTBotSiriusDiagramEditor;
import org.eclipse.sirius.tests.swtbot.support.utils.SWTBotUtils;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.eclipse.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefConnectionEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarDropDownButton;

/**
 * Verifies that Note (and Text) attachments, which are not handled directly/only by Sirius but inherited from GMF, work
 * correctly.
 */
@SuppressWarnings("restriction")
public class NoteAttachmentTest extends AbstractSiriusSwtBotGefTestCase {
    private static final String MODEL_FILE = "MyEcore.ecore";

    private static final String SECOND_MODEL_FILE = "My.ecore";

    private static final String SESSION_FILE = "representations.aird";

    private static final String ODESIGN_FILE = "My.odesign";

    private static final String DATA_UNIT_DIR = "data/unit/noteAttachments/Bugzilla-527391/";

    private static final String DATA_SECOND_UNIT_DIR = "data/unit/noteAttachments/bugzilla_582155/";

    @Override
    protected void onSetUpBeforeClosingWelcomePage() throws Exception {
        
    }

    /**
     * Checks that Note attachments can be selected.
     * 
     * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=527391"
     */
    public void test_NoteAttachments_can_be_selected() {
        copyFileToTestProject(Activator.PLUGIN_ID, DATA_UNIT_DIR, MODEL_FILE, SESSION_FILE);
        sessionAirdResource = new UIResource(designerProject, SESSION_FILE);
        localSession = designerPerspective.openSessionFromFile(sessionAirdResource, true);
        editor = (SWTBotSiriusDiagramEditor) openRepresentation(localSession.getOpenedSession(), "Entities", " package entities", DDiagram.class);
        List<NoteAttachmentEditPart> notes = editor.getConnectionsEditPart().stream().map(SWTBotGefConnectionEditPart::part).filter(NoteAttachmentEditPart.class::isInstance)
                .map(NoteAttachmentEditPart.class::cast).collect(Collectors.toList());
        assertEquals("The test diagram should contain 2 note attachments", 2, notes.size());
        Optional<NoteAttachmentEditPart> attachmentToNote = notes.stream().filter(n -> n.getTarget() instanceof NoteEditPart).findFirst();
        assertTrue("Could not find the note attachment edit part", attachmentToNote.isPresent());
        Optional<NoteAttachmentEditPart> attachmentToText = notes.stream().filter(n -> n.getSource() instanceof TextEditPart).findFirst();
        assertTrue("Could not find the text attachment edit part", attachmentToText.isPresent());
        // Make sure the diagram's background is selected first.
        editor.click(new Point(10, 10));
        bot.waitUntil(new DefaultCondition() {
            @Override
            public boolean test() throws Exception {
                ISelection sel = editor.getSelection();
                return (sel instanceof StructuredSelection) && ((StructuredSelection) sel).getFirstElement() == editor.getDiagramEditPart();
            }

            @Override
            public String getFailureMessage() {
                return "The diagram was not selected";
            }
        });
        // Select the note attachment
        editor.click(new Point(417, 111));
        bot.waitUntil(new CheckSelectedCondition(editor, attachmentToNote.get()));
        // Select the text attachment
        editor.click(new Point(433, 223));
        bot.waitUntil(new CheckSelectedCondition(editor, attachmentToText.get()));

    }

    /**
     * Checks that a Note attachment can be created between a Node and a Note.
     * 
     * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=581740"
     */
    public void test_NoteAttachmentCreation_betweenNodeAndNote() {
        copyFileToTestProject(Activator.PLUGIN_ID, DATA_UNIT_DIR, MODEL_FILE, SESSION_FILE);
        sessionAirdResource = new UIResource(designerProject, SESSION_FILE);
        localSession = designerPerspective.openSessionFromFile(sessionAirdResource, true);
        editor = (SWTBotSiriusDiagramEditor) openRepresentation(localSession.getOpenedSession(), "Entities", " package entities", DDiagram.class);
        // Create the note attachment
        createNoteAttachment("NewEClass2", DNodeListEditPart.class, "A note", SiriusNoteEditPart.class);

        // Check that a note attachment has been created.
        checkNoteAttachmentEditPartFrom("NewEClass2", 1);
    }

    /**
     * Checks that a Note attachment can be created between a Node and a Text.
     * 
     * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=581740"
     */
    public void test_NoteAttachmentCreation_betweenNodeAndText() {
        copyFileToTestProject(Activator.PLUGIN_ID, DATA_UNIT_DIR, MODEL_FILE, SESSION_FILE);
        sessionAirdResource = new UIResource(designerProject, SESSION_FILE);
        localSession = designerPerspective.openSessionFromFile(sessionAirdResource, true);
        editor = (SWTBotSiriusDiagramEditor) openRepresentation(localSession.getOpenedSession(), "Entities", " package entities", DDiagram.class);
        // Create the note attachment
        createNoteAttachment("NewEClass2", DNodeListEditPart.class, "A text", SiriusTextEditPart.class);

        // Check that a note attachment has been created.
        checkNoteAttachmentEditPartFrom("NewEClass2", 1);
    }

    /**
     * Checks that a Note attachment can not be created between a Node and another Node.
     * 
     * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=581740"
     */
    public void test_NoteAttachmentCreation_betweenNodeAndNode() {
        copyFileToTestProject(Activator.PLUGIN_ID, DATA_UNIT_DIR, MODEL_FILE, SESSION_FILE);
        sessionAirdResource = new UIResource(designerProject, SESSION_FILE);
        localSession = designerPerspective.openSessionFromFile(sessionAirdResource, true);
        editor = (SWTBotSiriusDiagramEditor) openRepresentation(localSession.getOpenedSession(), "Entities", " package entities", DDiagram.class);
        // Create the note attachment
        createNoteAttachment("NewEClass2", DNodeListEditPart.class, "A", DNodeListEditPart.class);

        // Check that a note attachment has been created.
        checkNoteAttachmentEditPartFrom("NewEClass2", 0);
    }

    /**
     * Checks that Note attachments are deleted on all cases of indirect "drag'n'drop" (node, edge, another note
     * attachment). Before this fix, the behavior was not the same for edge and node.
     * 
     * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=581751"
     */
    public void test_NoteAttachments_deletion() {
        copyFileToTestProject(Activator.PLUGIN_ID, DATA_UNIT_DIR, MODEL_FILE, SESSION_FILE);
        sessionAirdResource = new UIResource(designerProject, SESSION_FILE);
        localSession = designerPerspective.openSessionFromFile(sessionAirdResource, true);
        editor = (SWTBotSiriusDiagramEditor) openRepresentation(localSession.getOpenedSession(), "Entities", "ghostCase package entities", DDiagram.class);
        List<String> namesOfNotesToCheck = Arrays.asList("NoteOnNode", "NoteOnEdge", "NoteOnNoteAttachment");
        for (String nameOfNoteToCheck : namesOfNotesToCheck) {
            checkNoteAttachmentEditPartFrom(nameOfNoteToCheck, 1);
        }
        // Drag'n'drop packageA into packageB
        SWTBotGefEditPart sourceSwtBotPart = editor.getEditPart("packageA", AbstractDiagramContainerEditPart.class);
        SWTBotGefEditPart targetSwtBotPart = editor.getEditPart("packageB", AbstractDiagramContainerEditPart.class);
        Point targetCenter = ((IGraphicalEditPart) targetSwtBotPart.part()).getFigure().getBounds().getCenter();
        editor.drag(sourceSwtBotPart, targetCenter);
        // Check that a note attachments have been removed.
        for (String nameOfNoteToCheck : namesOfNotesToCheck) {
            checkNoteAttachmentEditPartFrom(nameOfNoteToCheck, 0);
        }
    }

    /**
     * Checks that Note is deleted on all cases of indirect "deletion" (node, edge, another note attachment). Before this
     * fix, the behavior was not the same for edge and node.
     * 
     * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=581752"
     */
    public void test_Note_deletion() {
        copyFileToTestProject(Activator.PLUGIN_ID, DATA_UNIT_DIR, MODEL_FILE, SESSION_FILE);
        sessionAirdResource = new UIResource(designerProject, SESSION_FILE);
        localSession = designerPerspective.openSessionFromFile(sessionAirdResource, true);
        editor = (SWTBotSiriusDiagramEditor) openRepresentation(localSession.getOpenedSession(), "Entities", "ghostCase package entities", DDiagram.class);
        List<String> namesOfNotesToCheck = Arrays.asList("NoteOnNode", "NoteOnEdge", "NoteOnNoteAttachment");
        changeDiagramUIPreference(SiriusDiagramUiInternalPreferencesKeys.PREF_REMOVE_HIDE_NOTE_WHEN_ANNOTED_ELEMENT_HIDDEN_OR_REMOVE.name(), true);
        for (String nameOfNoteToCheck : namesOfNotesToCheck) {
            checkNoteAttachmentEditPartFrom(nameOfNoteToCheck, 1);
        }
        // Select packageA
        editor.select(editor.getEditPart("packageA", AbstractDiagramContainerEditPart.class));
        bot.waitUntil(new CheckSelectedCondition(editor, "packageA", AbstractDiagramContainerEditPart.class));
        // Delete it
        deleteSelectedElement();
        SWTBotUtils.waitAllUiEvents();

        // Check that the note has been removed.
        for (String nameOfNoteToCheck : namesOfNotesToCheck) {
            try {
                editor.getEditPart(nameOfNoteToCheck, SiriusNoteEditPart.class);
                fail("The Note \"" + nameOfNoteToCheck + "\" should be deleted during the deletion of \"packageA\".");
            } catch (WidgetNotFoundException e) {
                // This is the expected behavior
            }
        }
    }

    /**
     * Checks that a text is hidden when the node it annotates is hidden and the property remove/hide annotations on
     * remove/hide node is active
     * 
     * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=582155"
     */
    public void testAnnotationTextBehaviorOnFilters() {
        copyFileToTestProject(Activator.PLUGIN_ID, DATA_SECOND_UNIT_DIR, SECOND_MODEL_FILE, SESSION_FILE, ODESIGN_FILE);
        sessionAirdResource = new UIResource(designerProject, SESSION_FILE);
        localSession = designerPerspective.openSessionFromFile(sessionAirdResource, true);
        editor = (SWTBotSiriusDiagramEditor) openRepresentation(localSession.getOpenedSession(), "myDiagram", "new myDiagram", DDiagram.class);
        DRepresentation dRepresentation = editor.getDRepresentation();
        assertTrue(dRepresentation instanceof DDiagram);
        DDiagram dDiagram = (DDiagram) dRepresentation;
        List<FilterDescription> filters = dDiagram.getDescription().getFilters();
        SWTBotToolbarDropDownButton filtersButton = editor.bot().toolbarDropDownButtonWithTooltip("Filters");
        List<? extends SWTBotMenu> menuItems = filtersButton.menuItems(WidgetMatcherFactory.widgetOfType(MenuItem.class));
        assertEquals(filters.size(), menuItems.size());
        for (int i = 0; i < menuItems.size(); i++) {
            SWTBotMenu menuItem = menuItems.get(i);
            assertEquals(new IdentifiedElementQuery(filters.get(i)).getLabel(), menuItem.getText());
        }
        SWTBotMenu menuItem = menuItems.get(0);
        assertEquals("myFilter", menuItem.getText());
        assertTrue(menuItem.isEnabled());
        
        SWTBotGefEditPart textEditPart = editor.getEditPart("Text", SiriusTextEditPart.class);
       
        // Change default filters selection
        menuItems.get(0).click();
    }

    private void createNoteAttachment(String sourceName, Class<? extends EditPart> expectedSourceClass, String targetName, Class<? extends EditPart> expectedTargetClass) {
        SWTBotGefEditPart source = editor.getEditPart(sourceName, expectedSourceClass);
        SWTBotGefEditPart target = editor.getEditPart(targetName, expectedTargetClass);

        Point sourcePoint = GraphicalHelper.getAbsoluteBoundsIn100Percent((IGraphicalEditPart) source.part()).getCenter();
        Point targetPoint = GraphicalHelper.getAbsoluteBoundsIn100Percent((IGraphicalEditPart) target.part()).getCenter();

        editor.activateTool("Note Attachment");
        editor.click(sourcePoint);
        editor.click(targetPoint);
    }

    private void checkNoteAttachmentEditPartFrom(String sourceName, int expectedNumberOfNoteAttachment) {
        SWTBotGefEditPart sourcePart = editor.getEditPart(sourceName).parent();
        assertEquals("Bad number of note attachment from " + sourceName, expectedNumberOfNoteAttachment, sourcePart.sourceConnections().size());
        for (int i = 0; i < expectedNumberOfNoteAttachment; i++) {
            SWTBotGefConnectionEditPart swtBotConnectionEditPart = sourcePart.sourceConnections().get(i);
            assertTrue("The connection from " + sourceName + ", at index " + i + " is not a note attachment.", swtBotConnectionEditPart.part() instanceof NoteAttachmentEditPart);
        }
    }
}
