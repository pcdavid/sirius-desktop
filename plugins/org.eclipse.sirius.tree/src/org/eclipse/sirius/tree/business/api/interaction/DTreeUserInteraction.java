/*******************************************************************************
 * Copyright (c) 2011 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tree.business.api.interaction;

import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.sirius.common.tools.DslCommonPlugin;
import org.eclipse.sirius.synchronizer.SemanticPartitionInvalidator;
import org.eclipse.sirius.tools.api.profiler.SiriusTasksKey;
import org.eclipse.sirius.tree.DTree;
import org.eclipse.sirius.tree.DTreeItem;
import org.eclipse.sirius.tree.business.api.query.TreeDescriptionQuery;
import org.eclipse.sirius.tree.business.internal.dialect.common.tree.DTreeRefresh;
import org.eclipse.sirius.tree.business.internal.dialect.common.viewpoint.GlobalContext;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

/**
 * This class is responsible for providing an entry point to all the user
 * interactions available on an opened DTree instance.
 * 
 * @author cbrun
 * 
 */
public class DTreeUserInteraction {

    private DTree tree;

    private GlobalContext ctx;

    /**
     * Creates a new DTreeUserInteraction.
     * 
     * @param tree
     *            the DTree
     * @param ctx
     *            the GlobalContext
     */
    public DTreeUserInteraction(DTree tree, GlobalContext ctx) {
        this.tree = tree;
        this.ctx = ctx;
    }

    /**
     * Refreshes the content of the DTree.
     * 
     * @param monitor
     *            a {@link IProgressMonitor} to use
     * 
     * @return this user interaction
     */
    public DTreeUserInteraction refreshContent(IProgressMonitor monitor) {
        try {
            monitor.beginTask("Refresh tree", 1);
            DslCommonPlugin.PROFILER.startWork(SiriusTasksKey.REFRESH_TREE_KEY);
            SemanticPartitionInvalidator invalidator = new SemanticPartitionInvalidator();
            DTreeRefresh refresher = new DTreeRefresh(tree, new TreeDescriptionQuery(tree.getDescription()).getAllDescendantMappings(), invalidator, ctx);
            refresher.refresh(new SubProgressMonitor(monitor, 1));
            DslCommonPlugin.PROFILER.stopWork(SiriusTasksKey.REFRESH_TREE_KEY);
        } finally {
            monitor.done();
        }
        return this;
    }

    /**
     * Expands all items of the DTree recursively.
     * 
     * @param monitor
     *            a progress monitor to make sure the user can cancel the
     *            operation.
     * @return this user interaction
     */
    public DTreeUserInteraction expandAll(IProgressMonitor monitor) {
        for (DTreeItem child : tree.getOwnedTreeItems()) {
            new DTreeItemUserInteraction(child, ctx).expandAll(monitor);
        }
        return this;
    }

    /**
     * Expand all root items of the DTree.
     * 
     * @param monitor
     *            a progress monitor to make sure the user can cancel the
     *            operation.
     * @return this user interaction
     */
    public DTreeUserInteraction expand(IProgressMonitor monitor) {
        Iterator<DTreeItem> it = Iterators.filter(Iterators.filter(tree.eAllContents(), DTreeItem.class), new Predicate<DTreeItem>() {

            public boolean apply(DTreeItem input) {
                return !input.isExpanded();
            }
        });
        while (it.hasNext()) {
            new DTreeItemUserInteraction(it.next(), ctx).expand(monitor);

        }
        return this;
    }
}
