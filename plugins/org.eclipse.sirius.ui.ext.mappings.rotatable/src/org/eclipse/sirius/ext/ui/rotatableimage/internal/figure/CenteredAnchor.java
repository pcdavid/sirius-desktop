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
package org.eclipse.sirius.ext.ui.rotatableimage.internal.figure;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

/**
 * Centered anchor.
 * 
 * @author hmarchadour
 * @author mbats
 */
public class CenteredAnchor extends AbstractConnectionAnchor {

	/**
	 * Constructor.
	 * 
	 * @param owner
	 *            the figure that this anchor is associated with
	 */
	public CenteredAnchor(IFigure owner) {
		super(owner);
	}

	@Override
	public Point getLocation(Point reference) {
		return super.getReferencePoint();
	}
}
