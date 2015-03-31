/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.suite;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.Platform;
import org.eclipse.sirius.tests.SiriusTestsPlugin;
import org.eclipse.sirius.tests.support.api.TestsUtil;

/**
 * Sirius Gerrit Junit Tests.
 * 
 * @author fbarbin
 */
public class GerritJUnitSuite extends TestCase {

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
        TestSuite suite = new TestSuite("Sirius Gerrit JUnit Tests");
        List<Class<? extends TestCase>> allTestCases = TestsUtil.getAllTestCases(Platform.getBundle(SiriusTestsPlugin.PLUGIN_ID), "/org/eclipse/sirius/tests/unit/");

        for (Class<? extends TestCase> clazz : TestsUtil.getFilteredTestCases(allTestCases, false, null, null, true, null)) {
            suite.addTestSuite(clazz);
        }
        return suite;
    }
}
