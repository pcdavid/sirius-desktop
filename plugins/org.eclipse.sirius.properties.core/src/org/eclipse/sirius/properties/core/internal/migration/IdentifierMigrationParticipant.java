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
package org.eclipse.sirius.properties.core.internal.migration;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sirius.business.api.migration.AbstractVSMMigrationParticipant;
import org.eclipse.sirius.properties.PropertiesPackage;
import org.osgi.framework.Version;

/**
 * This migration participant is responsible of the renaming of identifier into
 * label.
 * 
 * @author sbegaudeau
 */
public class IdentifierMigrationParticipant extends AbstractVSMMigrationParticipant {
    /**
     * The migration version.
     */
    private static final Version MIGRATION_VERSION = new Version("12.0.0.201702021139"); //$NON-NLS-1$

    /**
     * The name of the identifier EAttribute.
     */
    private static final String IDENTIFIER = "identifier"; //$NON-NLS-1$

    /**
     * The new name of the identifier EAttribute.
     */
    private static final String LABEL = "label"; //$NON-NLS-1$

    @Override
    public Version getMigrationVersion() {
        return MIGRATION_VERSION;
    }

    @Override
    public EStructuralFeature getAttribute(EClass eClass, String name, String loadedVersion) {
        if (IDENTIFIER.equals(name) && this.shouldMigrate(eClass) && eClass.getEStructuralFeature(LABEL) instanceof EAttribute) {
            return eClass.getEStructuralFeature(LABEL);
        }
        return super.getAttribute(eClass, name, loadedVersion);
    }

    /**
     * Indicates if the given eClass should be migrated.
     * 
     * @param eClass
     *            The EClass
     * @return <code>true</code> if it should be migrated, <code>false</code>
     *         otherwise
     */
    private boolean shouldMigrate(EClass eClass) {
        EClass[] eClassesToMigrate = new EClass[] { PropertiesPackage.Literals.VIEW_EXTENSION_DESCRIPTION, PropertiesPackage.Literals.CATEGORY, PropertiesPackage.Literals.ABSTRACT_PAGE_DESCRIPTION,
                PropertiesPackage.Literals.ABSTRACT_GROUP_DESCRIPTION, PropertiesPackage.Literals.ABSTRACT_CONTROL_DESCRIPTION, PropertiesPackage.Literals.ABSTRACT_DYNAMIC_MAPPING_IF_DESCRIPTION, };

        for (EClass eClassToMigrate : eClassesToMigrate) {
            if (eClassToMigrate.isInstance(eClass)) {
                return true;
            }
        }
        return false;
    }
}
