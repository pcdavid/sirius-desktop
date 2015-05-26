/*******************************************************************************
 * Copyright (c) 2009, 2014 THALES GLOBAL SERVICES and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ui.business.internal.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.sirius.business.api.preferences.SiriusPreferencesKeys;
import org.eclipse.sirius.ui.api.SiriusUIPlugin;
import org.eclipse.sirius.ui.business.api.preferences.SiriusUIPreferencesKeys;
import org.eclipse.sirius.viewpoint.SiriusPlugin;

/**
 * Initializes the preferences.
 * 
 * @author mchauvin
 */
public class GenericPreferencesInitializer extends AbstractPreferenceInitializer {

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences() {
        final IPreferenceStore uiPreferenceStore = SiriusUIPlugin.getPlugin().getPreferenceStore();
        final IEclipsePreferences defaultCorePreferences = DefaultScope.INSTANCE.getNode(SiriusPlugin.ID);

        uiPreferenceStore.setDefault(SiriusUIPreferencesKeys.PREF_REFRESH_ON_REPRESENTATION_OPENING.name(), getValue("_Pref_RefreshOnRepresentationOpening"));
        uiPreferenceStore.setDefault(SiriusUIPreferencesKeys.PREF_RELOAD_ON_LAST_EDITOR_CLOSE.name(), getValue("_Pref_ReloadOnLastEditorClose"));
        uiPreferenceStore.setDefault(SiriusUIPreferencesKeys.PREF_SAVE_WHEN_NO_EDITOR.name(), getValue("_Pref_SaveWhenNoEditor"));
        uiPreferenceStore.setDefault(SiriusUIPreferencesKeys.PREF_REACT_TO_PERMISSION_ISSUES_BY_GRAPHICAL_DISPLAY.name(), getValue("_Pref_ReactToPermissionIssuesByDisplayingPopup"));
        uiPreferenceStore.setDefault(SiriusUIPreferencesKeys.PREF_DISPLAY_PERMISSION_ISSUES_THROUGH_DIALOG.name(), getValue("_Pref_DisplayPermissionIssuesThroughDialog"));

        final boolean autoRefreshDefValue = getValue("_Pref_AutoRefresh");
        defaultCorePreferences.putBoolean(SiriusPreferencesKeys.PREF_AUTO_REFRESH.name(), autoRefreshDefValue);
        uiPreferenceStore.setDefault(SiriusPreferencesKeys.PREF_AUTO_REFRESH.name(), autoRefreshDefValue);

        final boolean emptyAirdFragOnControl = getValue("_Pref_EmptyAirdFragmentOnControl");
        defaultCorePreferences.putBoolean(SiriusPreferencesKeys.PREF_EMPTY_AIRD_FRAGMENT_ON_CONTROL.name(), emptyAirdFragOnControl);
        uiPreferenceStore.setDefault(SiriusPreferencesKeys.PREF_EMPTY_AIRD_FRAGMENT_ON_CONTROL.name(), emptyAirdFragOnControl);
    }

    private boolean getValue(final String key) {
        final String value = SiriusUIPlugin.INSTANCE.getString(key);
        return Boolean.valueOf(value);
    }
}