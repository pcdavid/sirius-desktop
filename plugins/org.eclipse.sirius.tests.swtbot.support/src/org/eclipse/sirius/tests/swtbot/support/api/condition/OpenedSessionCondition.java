/**
 * Copyright (c) 2013, 2014 THALES GLOBAL SERVICES
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *      Obeo - Initial API and implementation
 */
package org.eclipse.sirius.tests.swtbot.support.api.condition;

import java.util.Collection;

import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.waits.ICondition;

/**
 * A condition to wait until the {@link SessionManager} return the expected
 * number of sessions.
 * 
 * @author <a href="mailto:maxime.porhel@obeo.fr">Maxime Porhel</a>
 */
public class OpenedSessionCondition extends DefaultCondition implements ICondition {

    private final int expectedNumber;

    private Collection<Session> currentSessions;

    /**
     * Construct a condition to wait until a session is closed.
     * 
     * @param expectedNumber
     *            the expected number of session
     */
    public OpenedSessionCondition(int expectedNumber) {
        this.expectedNumber = expectedNumber;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean test() throws Exception {
        this.currentSessions = SessionManager.INSTANCE.getSessions();
        return expectedNumber == currentSessions.size();
    }

    @Override
    public String getFailureMessage() {
        String message = "The expected number of session was not reached. Expected " + expectedNumber + " but was " + currentSessions.size() + "\nCurrent Opened sessions: ";
        for (Session session : currentSessions) {
            message += "\n" + session.getID();
        }
        return message;
    }

}
