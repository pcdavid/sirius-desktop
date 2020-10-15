/*******************************************************************************
 * Copyright (c) 2020 THALES GLOBAL SERVICES and others.
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
package org.eclipse.sirius.ui.tools.api.dialogs;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.widgets.WidgetFactory;
import org.eclipse.sirius.common.ui.tools.api.util.EclipseUIUtil;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.provider.SiriusEditPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;

/**
 * A {@link FilteredItemsSelectionDialog} that will be used to present the analysis candidates where a representation
 * can be placed.
 * 
 * @author <a href="mailto:steve.monnier@obeo.fr">Steve Monnier</a>
 *
 */
public class AnalysisSelectorFilteredItemsSelectionDialog extends FilteredItemsSelectionDialog {

    /**
     * The id for the persistent settings for this dialog.
     */
    public static final String DIALOG_SETTINGS_ID = "MoveRepresentations"; //$NON-NLS-1$

    /**
     * Message to display when the help button or F1 are pressed.
     */
    protected String helpMessage = "On this dialog window is presented a list of all the other locations (.aird file) where you can move the selected representation."; //$NON-NLS-1$

    private final DAnalysis bestCandidate;

    private final Collection<DAnalysis> allAnalysis;

    private final List<DAnalysis> bestCandidates;

    private final ILabelProvider labelProvider;

    private Text messageText;

    private final String defaultHeader = "Select the location where the representation will be placed"; //$NON-NLS-1$

    /**
     * Default constructor.
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
     */
    public AnalysisSelectorFilteredItemsSelectionDialog(Shell shell, DAnalysis bestCandidate, Collection<DAnalysis> allAnalysis, List<DAnalysis> bestCandidates, ILabelProvider labelProvider) {
        super(shell);
        this.bestCandidate = bestCandidate;
        this.allAnalysis = allAnalysis;
        this.bestCandidates = bestCandidates;
        this.labelProvider = labelProvider;
        this.setHelpAvailable(true);
    }

    /**
     * Changes the header message.
     * 
     * @param text
     *            new message to display in the header
     */
    public void setHeaderMessageText(String text) {
        EclipseUIUtil.displaySyncExec(new Runnable() {
            public void run() {
                if (!PlatformUI.getWorkbench().isClosing() && messageText != null) {
                    if (text.isEmpty()) {
                        messageText.setText(defaultHeader);
                    } else {
                        messageText.setText(text);
                    }
                }
            }
        });
    }

    /**
     * This method was overridden with the goal to add a text at the top of the dialog.
     */
    @Override
    protected Control createContents(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        Composite composite = WidgetFactory.composite(0).layout(layout).layoutData(new GridData(GridData.FILL_BOTH)).create(parent);
        messageText = new Text(composite, SWT.READ_ONLY);
        messageText.setText(defaultHeader);
        final GridData anyElementData = new GridData();
        anyElementData.horizontalAlignment = GridData.FILL;
        anyElementData.horizontalIndent = 8;
        anyElementData.verticalIndent = 8;
        anyElementData.grabExcessHorizontalSpace = true;
        messageText.setLayoutData(anyElementData);
        applyDialogFont(composite);
        // initialize the dialog units
        initializeDialogUnits(composite);
        // create the dialog area and button bar
        dialogArea = createDialogArea(composite);
        buttonBar = createButtonBar(composite);
        parent.addHelpListener(new HelpListener() {

            @Override
            public void helpRequested(HelpEvent e) {
                Shell activeShell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
                String title = "Help: Move representation"; //$NON-NLS-1$
                MessageDialog dialog = new MessageDialog(activeShell, title, null, getHelpMessage(), MessageDialog.INFORMATION, new String[] { IDialogConstants.OK_LABEL }, 0);
                dialog.open();
            }
        });
        return parent;
    }

    protected String getHelpMessage() {
        return helpMessage;
    }


    @Override
    protected Control createExtendedContentArea(Composite parent) {
        return null;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        // create OK and Cancel button
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    @Override
    protected IDialogSettings getDialogSettings() {
        IDialogSettings settings = SiriusEditPlugin.getPlugin().getDialogSettings();
        settings = settings.getSection(DIALOG_SETTINGS_ID);
        if (settings == null) {
            settings = SiriusEditPlugin.getPlugin().getDialogSettings().addNewSection(DIALOG_SETTINGS_ID);
        }
        return settings;
    }

    @Override
    protected IStatus validateItem(Object item) {
        return Status.OK_STATUS;
    }

    @Override
    protected ItemsFilter createFilter() {
        return new ItemsFilter() {

            @Override
            public String getPattern() {
                String pattern = super.getPattern();
                // Allows empty pattern fills view
                if ((pattern == null) || (pattern.length() == 0)) {
                    return "*"; //$NON-NLS-1$
                }
                return pattern;
            }

            @Override
            public boolean matchItem(Object item) {
                boolean result = false;
                // perform a match with displayed text
                if (patternMatcher.matches(labelProvider.getText(item))) {
                    result = true;
                }
                // If doesn't match, perform a match with fileName
                if (!result && item instanceof DAnalysis) {
                    DAnalysis analysis = (DAnalysis) item;
                    Resource resource = analysis.eResource();
                    if ((resource != null) && (resource.getURI() != null)) {
                        URI fileUri = resource.getURI();
                        if (fileUri.lastSegment() != null) {
                            if (patternMatcher.matches(fileUri.lastSegment())) {
                                result = true;
                            }
                        }
                    }
                }
                return result;
            }

            @Override
            public boolean isConsistentItem(Object item) {
                return true;
            }
        };
    }

    @Override
    protected Comparator<Object> getItemsComparator() {
        return new Comparator<Object>() {

            @Override
            public int compare(Object o1, Object o2) {
                if (o1.equals(bestCandidate)) {
                    return -2;
                }
                return o2.equals(bestCandidate) ? +2 : labelProvider.getText(o1).compareTo(labelProvider.getText(o2));
            }

        };
    }

    @Override
    protected void fillContentProvider(AbstractContentProvider contentProvider, ItemsFilter itemsFilter, IProgressMonitor progressMonitor) throws CoreException {
        for (Object element : allAnalysis) {
            contentProvider.add(element, itemsFilter);
        }
    }

    @Override
    protected void storeDialog(IDialogSettings settings) {
        super.storeDialog(settings);
    }

    @Override
    protected void restoreDialog(IDialogSettings settings) {
        setSelectionHistory(new SelectionHistory() {

            @Override
            protected Object restoreItemFromMemento(IMemento memento) {
                return null;
            }

            @Override
            protected void storeItemToMemento(Object item, IMemento memento) {
                // we don't save any history here
            }
        });

        super.restoreDialog(settings);

        // but we load best candidates
        if (bestCandidates != null) {
            for (Object object : bestCandidates) {
                accessedHistoryItem(object);
            }
        }
    }

    @Override
    public String getElementName(Object item) {
        return labelProvider.getText(item);
    }

    @Override
    protected void setShellStyle(int newShellStyle) {
        super.setShellStyle(SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());
    }
}
