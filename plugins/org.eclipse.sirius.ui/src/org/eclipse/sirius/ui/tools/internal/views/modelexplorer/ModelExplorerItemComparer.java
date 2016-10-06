/*******************************************************************************
 * Copyright (c) 2013 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ui.tools.internal.views.modelexplorer;

import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.sirius.ui.tools.api.views.common.item.ItemWrapper;
import org.eclipse.sirius.ui.tools.internal.views.common.item.RepresentationItemImpl;

/**
 * A {@link IElementComparer} to have {@link ItemWrapper} considered equals to
 * {@link ItemWrapper#getWrappedObject()}.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
public class ModelExplorerItemComparer implements IElementComparer {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object a, Object b) {
        boolean equals = false;
        Object realAObject = a;
        if (realAObject instanceof ItemWrapper && !(realAObject instanceof RepresentationItemImpl)) {
            ItemWrapper itemWrapper = (ItemWrapper) realAObject;
            realAObject = itemWrapper.getWrappedObject();
        }
        Object realBObject = b;
        if (realBObject instanceof ItemWrapper && !(realBObject instanceof RepresentationItemImpl)) {
            ItemWrapper itemWrapper = (ItemWrapper) realBObject;
            realBObject = itemWrapper.getWrappedObject();
        }
        equals = realAObject != null && realAObject.equals(realBObject);
        return equals;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode(Object element) {
        int hashCode = 0;
        Object realElementObject = element;
        if (realElementObject instanceof ItemWrapper && !(realElementObject instanceof RepresentationItemImpl)) {
            ItemWrapper itemWrapper = (ItemWrapper) realElementObject;
            realElementObject = itemWrapper.getWrappedObject();
        }
        if (realElementObject != null) {
            hashCode = realElementObject.hashCode();
        }
        return hashCode;
    }

}
