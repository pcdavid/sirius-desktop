/*******************************************************************************
 * Copyright (c) 2008-2020 THALES GLOBAL SERVICES and others.
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
package org.eclipse.sirius.ui.business.api.session.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.sirius.business.api.query.DRepresentationQuery;
import org.eclipse.sirius.business.api.query.EObjectQuery;
import org.eclipse.sirius.business.api.session.danalysis.DAnalysisSelector;
import org.eclipse.sirius.common.ui.tools.api.util.EclipseUIUtil;
import org.eclipse.sirius.ui.tools.api.dialogs.AnalysisSelectorFilteredItemsSelectionDialog;
import org.eclipse.sirius.ui.tools.api.views.ViewHelper;
import org.eclipse.sirius.ui.tools.internal.views.common.SessionLabelProvider;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.ViewpointPackage;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * A dialog which select smartly analysis.
 *
 * @author mchauvin
 */
public class SmartDialogAnalysisSelector implements DAnalysisSelector {
    
    /**
     * The dialog that displays the analysis where the representation will be placed.
     */
    protected AnalysisSelectorFilteredItemsSelectionDialog dialog;

    @Override
    public DAnalysis selectSmartlyAnalysisForAddedResource(final Resource resource, final Collection<DAnalysis> allAnalysis) {
        return selectSmartlyAnalysis(allAnalysis, null);
    }

    @Override
    public DAnalysis selectSmartlyAnalysisForAddedRepresentation(final DRepresentation representation, final Collection<DAnalysis> allAnalysis) {
        DAnalysis selectSmartlyAnalysis = selectSmartlyAnalysis(allAnalysis, representation.getName());
        if (selectSmartlyAnalysis == null) {
            DRepresentationDescriptor representationDescriptor = new DRepresentationQuery(representation).getRepresentationDescriptor();
            selectSmartlyAnalysis = (DAnalysis) new EObjectQuery(representationDescriptor).getFirstAncestorOfType(ViewpointPackage.eINSTANCE.getDAnalysis()).get();
        }
        return selectSmartlyAnalysis;
    }

    /**
     * Asks the user to select the analysis.
     *
     * @param allAnalysis
     *            all available analysis
     * @return selected analysis
     */
    private DAnalysis selectSmartlyAnalysis(final Collection<DAnalysis> allAnalysis, String representationName) {

        final ILabelProvider labelProvider = getLocationLabelProvider();

        RunnableWithResult<Object> runnable = new RunnableWithResult.Impl<Object>() {

            @Override
            public void run() {
                
                dialog = createAnalysisSelectorDialog(Display.getDefault().getActiveShell(), allAnalysis.iterator().next(), allAnalysis, new ArrayList<>(allAnalysis), labelProvider);
                dialog.setDetailsLabelProvider(labelProvider);
                dialog.setListLabelProvider(labelProvider);
                dialog.setSeparatorLabel("Other Fragments"); //$NON-NLS-1$
                if (representationName != null && !representationName.isEmpty()) {
                    dialog.setTitle("Location selection for '" + representationName + "'"); //$NON-NLS-1$ //$NON-NLS-2$
                    dialog.setMessage("Select a location where the representation '" + representationName + "' will be placed."); //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    dialog.setTitle("Location selection"); //$NON-NLS-1$
                    dialog.setMessage("Select a location"); //$NON-NLS-1$
                }

                if (allAnalysis.iterator().next() != null) {
                    dialog.setInitialElementSelections(Collections.singletonList(allAnalysis.iterator().next()));
                }

                if (dialog.open() == Window.OK) {
                    if (dialog.getFirstResult() != null) {
                        setResult(dialog.getFirstResult());
                    }
                } else {
                    // Box closed by cancel, ESC key ...
                    setResult(null);
                }
            }
        };
        /* synch execution as the user need to choose before we can get further */
        EclipseUIUtil.displaySyncExec(runnable);
        if (runnable.getResult() instanceof DAnalysis) {
            return (DAnalysis) runnable.getResult();
        }
        return null;
    }

    /**
     * Initializes the dialog that will be used for selecting the targeted {@link DAnalysis}.
     * 
     * @param shell
     *            shell to parent the dialog on
     * @param bestCandidate
     *            the best candidate that will be selected by default
     * @param allAnalysis
     *            all the analysis available
     * @param bestCandidates
     *            list of best candidates
     * @param labelProvider
     *            the label provider that will be used to present the candidates
     * @return the dialog that will be used for selecting the targeted {@link DAnalysis}
     */
    protected AnalysisSelectorFilteredItemsSelectionDialog createAnalysisSelectorDialog(Shell shell, DAnalysis bestCandidate, Collection<DAnalysis> allAnalysis, List<DAnalysis> bestCandidates,
            ILabelProvider labelProvider) {
        return new AnalysisSelectorFilteredItemsSelectionDialog(Display.getDefault().getActiveShell(), allAnalysis.iterator().next(), allAnalysis, new ArrayList<>(allAnalysis), labelProvider);
    }

    protected ILabelProvider getLocationLabelProvider() {
        return new SessionLabelProvider(ViewHelper.INSTANCE.createAdapterFactory()) {
            @Override
            public String getText(final Object object) {
                String result;
                if (object instanceof DAnalysis) {
                    DAnalysis dAnalysis = (DAnalysis) object;
                    if (dAnalysis.eResource().getURI().isPlatformResource()) {
                        result = "Local (in " + dAnalysis.eResource().getURI().path().replace("/resource/", "/") + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                    } else {
                        result = "in " + dAnalysis.eResource().getURI().path(); //$NON-NLS-1$
                    }
                } else {
                    result = super.getText(object);
                }
                return result;
            }

        };
    }

}
