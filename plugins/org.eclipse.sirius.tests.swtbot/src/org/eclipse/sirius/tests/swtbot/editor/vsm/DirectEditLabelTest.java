/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.swtbot.editor.vsm;

import org.eclipse.sirius.tests.swtbot.Activator;
import org.eclipse.sirius.tests.swtbot.support.api.AbstractSiriusSwtBotGefTestCase;
import org.eclipse.sirius.tests.swtbot.support.api.editor.SWTBotVSMEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

/**
 * Test class for inputlabelExpression of directEditTool (see Bugzilla 431991).
 * 
 * @author jmallet
 */
public class DirectEditLabelTest extends AbstractSiriusSwtBotGefTestCase {

    private static final String VSM = "emptyName.odesign";

    private static final String ODESIGN = "platform:/resource/DesignerTestProject/" + VSM;

    private static final String DATA_UNIT_DIR = "data/unit/vsmValidation/emptyNameVariable/";

    @Override
    protected void onSetUpBeforeClosingWelcomePage() throws Exception {
        copyFileToTestProject(Activator.PLUGIN_ID, DATA_UNIT_DIR, VSM);
    }

    /**
     * Test that direct edition on label of the VSM tree element modifies the
     * label.
     */
    public void testDirectEditLabel() {
        // Open VSM
        SWTBotVSMEditor odesignEditor = openViewpointSpecificationModel(VSM);
        bot.editorByTitle(VSM).setFocus();
        SWTBotTree tree = odesignEditor.bot().tree();
        checkDirectEditLabel(odesignEditor, tree);
    }

    /**
     * Check that direct edit on VSM tree element works without any error.
     * 
     * @param odesignEditor
     *            the odesign editor
     * @param tree
     *            the tree to edit
     */
    private void checkDirectEditLabel(final SWTBotVSMEditor odesignEditor, SWTBotTree tree) {
        final SWTBotTreeItem treeItem = tree.getTreeItem(ODESIGN).getNode("Group").getNode("emptyName")
                .expandNode("diagTest", "Default", "node2").select();
        treeItem.pressShortcut(Keystrokes.F2);
        odesignEditor.bot().activeShell();
        odesignEditor.bot().text().typeText("newLabel\n");
        treeItem.expand();
        odesignEditor.bot().waitUntil(new ICondition() {

            @Override
            public boolean test() throws Exception {
                return "newLabel".equals(treeItem.getText());
            }

            @Override
            public void init(SWTBot bot) {
            }

            @Override
            public String getFailureMessage() {
                return "The treeItem must display \"newLabel\" after direct edit with F2";
            }
        });
    }
}
