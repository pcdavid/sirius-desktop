/*******************************************************************************
 * Copyright (c) 2018 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.internal.migration;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.sirius.business.api.migration.AbstractRepresentationsFileMigrationParticipant;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.Messages;
import org.eclipse.sirius.viewpoint.SiriusPlugin;
import org.eclipse.sirius.viewpoint.ViewpointPackage;
import org.osgi.framework.Version;

/**
 * This migration participant will update IdentifiedElement.uid to be the same value than the xmiid.</br>
 * DRepresentationDescriptor.repPath is updated consequently to the DRepresentation.uid change
 * 
 * @author <a href="mailto:laurent.fasani@obeo.fr">Laurent Fasani</a>
 */
public class TechnicalUidMigrationParticipant extends AbstractRepresentationsFileMigrationParticipant {

    /**
     * The VP version for which this migration is added.
     */
    public static final Version MIGRATION_VERSION = new Version("14.1.0.201808080808"); //$NON-NLS-1$

    /**
     * From org.eclipse.emf.ecore.xmi.impl.XMIHandler.ID_ATTRIB
     */
    private static final String XMI_ID_ATTRIB = "xmi:id"; //$NON-NLS-1$

    private boolean migrationOccured;

    @Override
    public Version getMigrationVersion() {
        return MIGRATION_VERSION;
    }

    @Override
    public EStructuralFeature getAttribute(EClass eClass, String name, String loadedVersion) {
        if (Version.parseVersion(loadedVersion).compareTo(MIGRATION_VERSION) < 0) {
            if (XMI_ID_ATTRIB.equals(name) && ViewpointPackage.eINSTANCE.getIdentifiedElement().isSuperTypeOf(eClass) && !ViewpointPackage.eINSTANCE.getDRepresentation().isSuperTypeOf(eClass)) {
                // Transfer xmi:id value to IdentifiedElement:uid for all identified element object excepted for
                // DRepresentation as they already had this uid attribute which is used in
                // DRepresentationDescriptor.repPath
                // This avoid to migrate repPath which might point to another resource.
                migrationOccured = true;
                return ViewpointPackage.eINSTANCE.getIdentifiedElement_Uid();
            }
        }
        return super.getAttribute(eClass, name, loadedVersion);
    }

    @Override
    public void postLoad(XMLResource resource, String loadedVersion) {
        if (Version.parseVersion(loadedVersion).compareTo(MIGRATION_VERSION) < 0) {
            resource.getAllContents().forEachRemaining(eObject -> {
                if (eObject instanceof DRepresentation) {
                    // getAttribute() Xmi:id has been set as id during parsing and creation of the element.
                    // The uid was already present for DRepresentation instances and the chose behavior is to drop
                    // xmi:id for DRepresentation in favor of the uid (to avoid repPath migration), so we need to update
                    // the resource id <> EObject cache with the uid as id in replacement for the xmi:id.
                    // This is done in postLoad to keep the idRef resolution based on xmi:id works to retrieve the
                    // DDiagram from the GMF Diagram.element as the OPTION_DEFER_IDREF_RESOLUTION is set to true.
                    String uid = ((DRepresentation) eObject).getUid();
                    String cachedId = resource.getID(eObject);
                    if (!uid.equals(cachedId)) {
                        migrationOccured = true;
                        resource.setID(eObject, uid);
                    }
                }

            });
            if (migrationOccured) {
                SiriusPlugin.getDefault().info(Messages.TechnicalUidMigrationParticipant_message, null);
                migrationOccured = false;
            }
        }
        super.postLoad(resource, loadedVersion);
    }
}
