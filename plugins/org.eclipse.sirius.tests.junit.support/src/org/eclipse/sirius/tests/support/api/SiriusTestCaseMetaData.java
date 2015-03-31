/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/

package org.eclipse.sirius.tests.support.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allows meta-information on Sirius TestCase.
 * 
 * @author <a href="mailto:laurent.fasani@obeo.fr">Laurent Fasani</a>
 */
@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SiriusTestCaseMetaData {
    public enum Category {
        /**
         * Common test.
         */
        COMMON,
        /**
         * Diagram related test.
         */
        DIAGRAM,
        /**
         * Table related test.
         */
        TABLE,
        /**
         * Sequence related test.
         */
        SEQUENCE
    };

    /**
     * The category of test. <br>
     * It the dialect or else common.
     * 
     * @return
     */
    Category category();

    public enum PluginOrStandalone {
        /**
         * The test relies on eclipse platform.
         */
        PLUGIN,
        /**
         * This test is standalone.
         */
        STANDALONE
    };

    /**
     * Indicates if the test is a plugin or standalone.
     * 
     * @return
     */
    PluginOrStandalone pluginStandalone();

    /**
     * If true, the test case is ignored in suite.
     * 
     * @return
     */
    boolean disabled() default false;

    /**
     * If true, the test case is included in gerrit suite.
     * 
     * @return
     */
    boolean gerrit() default true;

    /**
     * If true, the test case is considered as long test.
     * 
     * @return
     */
    boolean longTest() default false;

    public enum ErrorTag {
        /**
         * The test has no error Tag.
         */
        NONE,
        /**
         * The test is unreliable.
         */
        UNRELIABLE,
        /**
         * This test ends in deadlock.
         */
        DEADLOCK,
        /**
         * This test ends in error.
         */
        ERROR
    };

    /**
     * The type of error tagged on this testCase.
     * 
     * @return
     */
    ErrorTag errorTag() default ErrorTag.NONE;
}
