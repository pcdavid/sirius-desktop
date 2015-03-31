/**
 * Copyright (c) 2010, 2015 THALES GLOBAL SERVICES
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Obeo - Initial API and implementation
 *      Ketan Padegaonkar and others - the various waitUntil() methods come
 *        from org.eclipse.swtbot.swt.finder.SWTBotFactory
 */
package org.eclipse.sirius.tests.support.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.Platform;
import org.eclipse.sirius.diagram.ui.tools.internal.editor.tabbar.Tabbar;
import org.eclipse.sirius.tests.support.api.SiriusTestCaseMetaData.Category;
import org.eclipse.sirius.tests.support.api.SiriusTestCaseMetaData.ErrorTag;
import org.eclipse.sirius.tests.support.api.SiriusTestCaseMetaData.PluginOrStandalone;
import org.eclipse.ui.PlatformUI;
import org.junit.Assert;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;
import org.osgi.framework.wiring.BundleWiring;

/**
 * Useful operation common to test cases.
 * 
 * @author mchauvin
 */
public final class TestsUtil {

    private static final String UI_WORKBENCH_JUNO_START = "3.103";

    private static final String UI_WORKBENCH_KEPLER_START = "3.105";

    private static final String UI_WORKBENCH_LUNA_START = "3.106";

    /**
     * Constructor.
     */
    protected TestsUtil() {
        // Nothing
    }

    /**
     * Wait the end of the asynchronous calls in UI Thread and ignore the
     * Exception. <B>Use this exclusively</B> in the setup method to ensure a
     * clean environment.
     */
    public static void emptyEventsFromUIThread() {
        boolean shouldRetry = false;
        do {
            try {
                TestsUtil.synchronizationWithUIThread();
                shouldRetry = false;
                // CHECKSTYLE:OFF
            } catch (final Exception e) {
                // CHECKSTYLE:ON
                shouldRetry = true;
            }
        } while (shouldRetry);
    }

    /**
     * Wait the end of the asynchronous calls waiting in UI thread.
     */
    public static void synchronizationWithUIThread() {
        while (PlatformUI.getWorkbench().getDisplay().readAndDispatch()) {
            // Do nothing, just wait
        }
    }

    /**
     * Tests whether the environment is configured to skip non-critical tests
     * which take a long time. This possibility to skip some tests should only
     * be used on developer machines to get faster feedback, and never on a
     * continuous integration server.
     * 
     * @return <code>true</code> if the environment is setup to skip long tests.
     */
    public static boolean shouldSkipLongTests() {
        return "true".equals(System.getProperty("org.eclipse.sirius.tests.skipLongTests"));
    }

    /**
     * Tests whether unreliable tests should be run. See
     * {@link #shouldSkipUnreliableTests()}. Can be used in tests as:
     * 
     * <pre>
     * Assume.assumeTrue(TestUtil.shouldRunUnreliableTests())
     * </pre>
     * 
     * at the beginning of such tests.
     * 
     * @return <code>true</code> iff unreliable tests should be run.
     */
    public static boolean shouldRunUnreliableTests() {
        return !TestsUtil.shouldSkipUnreliableTests();
    }

    /**
     * Tests whether the environment is configured to skip tests which are known
     * to be unreliable (i.e. they sometimes work, sometimes fail).
     * 
     * @return <code>true</code> if the environment is setup to skip unreliable
     *         tests.
     */
    public static boolean shouldSkipUnreliableTests() {
        return "true".equals(System.getProperty("org.eclipse.sirius.tests.skipUnreliableTests"));
    }

    /**
     * Tests whether long running tests should be run. See
     * {@link #shouldSkipLongTests()}. Can be used in tests as:
     * 
     * <pre>
     * Assume.assumeTrue(TestUtil.shouldRunLongTests())
     * </pre>
     * 
     * at the beginning of such tests.
     * 
     * @return <code>true</code> iff long running tests should be run.
     */
    public static boolean shouldRunLongTests() {
        return !TestsUtil.shouldSkipLongTests();
    }

    /**
     * Tells if the current platform corresponds to juno3 (i.e. Eclipse 3.8).
     * 
     * @return true if the current platform corresponds to juno3 (i.e. Eclipse
     *         3.8), false else
     */
    public static boolean isJuno3Platform() {
        boolean isJuno3Platform = false;
        String platformVersion = Platform.getBundle("org.eclipse.core.runtime").getHeaders().get("Bundle-Version");
        if (platformVersion.startsWith("3.8")) {
            isJuno3Platform = true;
        }
        return isJuno3Platform && !isJuno4Platform();
    }

    /**
     * Tells if the current platform corresponds to Juno, Kepler, Luna, .. (i.e.
     * Eclipse 4.x).
     * 
     * @return true if the current platform corresponds to eclipse 4.x, false
     *         otherwise.
     */
    public static boolean isEclipse4xPlatform() {
        Version junoStart = Version.parseVersion(UI_WORKBENCH_JUNO_START);
        return checkUiWorkbenchVersion(junoStart, null);
    }

    /**
     * Tells if the current platform corresponds to Juno (i.e. Eclipse 4.x).
     * 
     * @return true if the current platform corresponds to Juno 4.x, false
     *         otherwise.
     */
    public static boolean isJuno4Platform() {
        Version junoStart = Version.parseVersion(UI_WORKBENCH_JUNO_START);
        Version keplerStart = Version.parseVersion(UI_WORKBENCH_KEPLER_START);
        return checkUiWorkbenchVersion(junoStart, keplerStart);
    }

    private static boolean checkUiWorkbenchVersion(Version versiontStart, Version versionEnd) {
        /*
         * Juno/Kepler Core Runtime plugins version are 3.8/3.9 and not 4.x. So
         * the "org.eclipse.ui.workbench" is used instead.
         */
        Bundle uiWorkbenchBundle = Platform.getBundle("org.eclipse.ui.workbench");
        if (uiWorkbenchBundle != null) {
            return uiWorkbenchBundle.getVersion().compareTo(versiontStart) >= 0 && (versionEnd == null || uiWorkbenchBundle.getVersion().compareTo(versionEnd) < 0);
        }
        return false;
    }

    /**
     * Tells if the current platform corresponds to Kepler.
     * 
     * @return true if the current platform corresponds to Kepler, false
     *         otherwise.
     */
    public static boolean isKeplerPlatform() {
        Version keplerStart = Version.parseVersion(UI_WORKBENCH_KEPLER_START);
        Version lunaStart = Version.parseVersion(UI_WORKBENCH_LUNA_START);
        return checkUiWorkbenchVersion(keplerStart, lunaStart);
    }

    /**
     * Tells if the current platform corresponds to Luna.
     * 
     * @return true if the current platform corresponds to Luna, false
     *         otherwise.
     */
    public static boolean isLunaPlatform() {
        Version keplerStart = Version.parseVersion(UI_WORKBENCH_LUNA_START);
        return checkUiWorkbenchVersion(keplerStart, null);
    }

    /**
     * Copied and adapted from
     * org.eclipse.swtbot.swt.finder.SWTBotFactory.waitUntil(ICondition, long,
     * long)
     * 
     * Waits until the condition has been meet, or the timeout is reached. The
     * interval is the delay between evaluating the condition after it has
     * failed.
     * 
     * @param condition
     *            the condition to be evaluated.
     * @param timeout
     *            the timeout.
     * @param interval
     *            The delay time.
     */
    public static void waitUntil(ICondition condition, long timeout, long interval) {
        Assert.assertTrue("interval value is negative", interval >= 0); //$NON-NLS-1$
        Assert.assertTrue("timeout value is negative", timeout >= 0); //$NON-NLS-1$
        long limit = System.currentTimeMillis() + timeout;
        while (true) {
            try {
                if (condition.test()) {
                    return;
                }
                // CHECKSTYLE:OFF
            } catch (Throwable e) {
                // dO nothing
            }
            // CHECKSTYLE:ON

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Assert.fail(e.getMessage());
            }
            if (System.currentTimeMillis() > limit) {
                Assert.fail("Timeout after: " + timeout + " ms.: " + condition.getFailureMessage());
            }
        }
    }

    /**
     * Copied and adapted from
     * org.eclipse.swtbot.swt.finder.SWTBotFactory.waitUntil(ICondition, long,
     * long)
     * 
     * Waits until the condition has been meet, or the timeout is reached. The
     * interval is the delay between evaluating the condition after it has
     * failed.
     * 
     * Interval : 500
     * 
     * @param condition
     *            the condition to be evaluated.
     * @param timeout
     *            the timeout.
     */
    public static void waitUntil(ICondition condition, long timeout) {
        TestsUtil.waitUntil(condition, timeout, 500);
    }

    /**
     * Copied and adapted from
     * org.eclipse.swtbot.swt.finder.SWTBotFactory.waitUntil(ICondition, long,
     * long)
     * 
     * Waits until the condition has been meet, or the timeout is reached. The
     * interval is the delay between evaluating the condition after it has
     * failed.
     * 
     * Timeout: 5000 Interval : 500
     * 
     * @param condition
     *            the condition to be evaluated.
     */
    public static void waitUntil(ICondition condition) {
        TestsUtil.waitUntil(condition, 5000, 500);
    }

    /**
     * Indicates if the tabbar can be dynamic (ie: is contextual and can receive
     * contributions). Some issues were detected on Juno and Kepler.
     * 
     * @See {@link Tabbar#canBeDynamic()}
     * @return true if the tabbar is dynamic.
     */
    public static boolean isDynamicTabbar() {
        return Tabbar.canBeDynamic();
    }

    /**
     * Search testCases of a bundle in a given package.
     * 
     * @param bundle
     *            the bundle
     * @param packagePath
     *            packagePath starting with "/"
     * @return found testCases in package of bundle
     */
    public static List<Class<? extends TestCase>> getAllTestCases(Bundle bundle, String packagePath) {
        BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
        Collection<String> resources = bundleWiring.listResources(packagePath, "*.class", BundleWiring.LISTRESOURCES_LOCAL | BundleWiring.LISTRESOURCES_RECURSE);
        List<Class<? extends TestCase>> classes = new ArrayList<Class<? extends TestCase>>();
        ClassLoader cl = bundleWiring.getClassLoader();
        for (String s : resources) {
            String str = s.replace('/', '.').substring(0, s.length() - 6);
            try {
                Class<?> clazz = Class.forName(str, false, cl);
                if (TestCase.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(SiriusTestCaseMetaData.class)) {
                    classes.add((Class<? extends TestCase>) clazz);
                }
            } catch (ClassNotFoundException cnfe) {
            } catch (NoClassDefFoundError ncdfe) {
            }
        }

        Collections.sort(classes, new Comparator<Class<? extends TestCase>>() {
            @Override
            public int compare(Class<? extends TestCase> c1, Class<? extends TestCase> c2) {
                return c1.getName().compareTo(c2.getName());
            }
        });
        return classes;
    }

    /**
     * Get the filtered list of TestCases according to criteria.<br>
     * The filter is based on {@link SiriusTestCaseMetaData}. The test is filtered
     * if this annotation is not found.<br>
     * All filter are added; the more filter the smaller the returned list is.
     * 
     * @param testCasesToFilter
     *            testCases to filter
     * @param disabled
     *            if not null, filter with disabled testCases
     * @param category
     *            if not null, filter according to category
     * @param pluginOrStandalone
     *            if not null, filter according to pluginOrStandalone
     * @param gerrit
     *            if not null, filter with gerrit testCases
     * @param errorTag
     *            if not null, filter according to errorTag
     * @return filtered testCases
     */
    public static List<Class<? extends TestCase>> getFilteredTestCases(List<Class<? extends TestCase>> testCasesToFilter, Boolean disabled, Category category, PluginOrStandalone pluginOrStandalone,
            Boolean gerrit, ErrorTag errorTag) {
        List<Class<? extends TestCase>> filteredTestCases = new ArrayList<Class<? extends TestCase>>();

        boolean keepTest = false;
        for (Class<? extends TestCase> clazz : testCasesToFilter) {
            keepTest = false;
            SiriusTestCaseMetaData annotation = clazz.getAnnotation(SiriusTestCaseMetaData.class);
            if (annotation != null) {
                if (disabled != null) {
                    keepTest = annotation.disabled() == disabled;
                    if (!keepTest) {
                        continue;
                    }
                }
                if (category != null) {
                    keepTest = annotation.category().equals(category);
                    if (!keepTest) {
                        continue;
                    }
                }
                if (pluginOrStandalone != null) {
                    keepTest = annotation.pluginStandalone().equals(pluginOrStandalone);
                    if (!keepTest) {
                        continue;
                    }
                }
                if (gerrit != null) {
                    keepTest = annotation.gerrit() == gerrit;
                    if (!keepTest) {
                        continue;
                    }
                }
                if (errorTag != null) {
                    keepTest = annotation.errorTag().equals(errorTag);
                    if (!keepTest) {
                        continue;
                    }
                }
                if (keepTest) {
                    filteredTestCases.add(clazz);
                }
            }
        }
        return filteredTestCases;
    }
}
