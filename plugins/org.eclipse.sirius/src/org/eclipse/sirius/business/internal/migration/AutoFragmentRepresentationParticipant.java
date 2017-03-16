package org.eclipse.sirius.business.internal.migration;

import org.eclipse.sirius.business.api.migration.AbstractRepresentationsFileMigrationParticipant;
import org.eclipse.sirius.business.internal.session.danalysis.DAnalysisSessionServicesImpl;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.DView;
import org.osgi.framework.Version;

public class AutoFragmentRepresentationParticipant extends AbstractRepresentationsFileMigrationParticipant {

    /**
     * The VP version for which this migration is added.
     */
    public static final Version MIGRATION_VERSION = new Version("12.0.0.201703161200"); //$NON-NLS-1$

    @Override
    public Version getMigrationVersion() {
        return MIGRATION_VERSION;
    }

    @Override
    protected void postLoad(DAnalysis dAnalysis, Version loadedVersion) {
        super.postLoad(dAnalysis, loadedVersion);

        for (DView view : dAnalysis.getOwnedViews()) {
            for (DRepresentationDescriptor repDesc : view.getOwnedRepresentationDescriptors()) {
                DAnalysisSessionServicesImpl.extract(repDesc.getRepresentation(), dAnalysis.eResource());
            }
        }
    }

}
