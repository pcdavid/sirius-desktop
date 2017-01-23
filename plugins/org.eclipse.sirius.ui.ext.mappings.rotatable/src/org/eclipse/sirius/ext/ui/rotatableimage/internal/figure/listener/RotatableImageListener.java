/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ext.ui.rotatableimage.internal.figure.listener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.AncestorListener;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.sirius.ext.ui.rotatableimage.internal.editPart.AbstractRotatableImageEditPart;

/**
 * Listener on rotatable image : refresh the image if figure moves or changes.
 * 
 * @author nlepine
 * @author hmarchadour
 * @author mbats
 */
public class RotatableImageListener implements FigureListener, PropertyChangeListener, AncestorListener {

	/**
	 * The edit part.
	 */
	private AbstractRotatableImageEditPart editpart;

	/**
	 * Constructor.
	 * 
	 * @param editpart
	 *            RotatableImageEditPart
	 */
	public RotatableImageListener(AbstractRotatableImageEditPart editpart) {
		this.editpart = editpart;
	}

	/**
	 * Notify the edit part that figure has changed.
	 */
	public void notifyEditPart() {
		editpart.figureHasChanged();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.draw2d.FigureListener#figureMoved(org.eclipse.draw2d.IFigure)
	 */
	@Override
	public void figureMoved(IFigure source) {
		notifyEditPart();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(org.eclipse.draw2d.IFigure)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		notifyEditPart();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.draw2d.AncestorListener#ancestorAdded(org.eclipse.draw2d.IFigure)
	 */
	@Override
	public void ancestorAdded(IFigure ancestor) {
		notifyEditPart();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.draw2d.AncestorListener#ancestorMoved(org.eclipse.draw2d.IFigure)
	 */
	@Override
	public void ancestorMoved(IFigure ancestor) {
		notifyEditPart();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.draw2d.AncestorListener#ancestorRemoved(org.eclipse.draw2d.IFigure)
	 */
	@Override
	public void ancestorRemoved(IFigure ancestor) {
		notifyEditPart();
	}
}
