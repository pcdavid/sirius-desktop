package org.eclipse.sirius.diagram.ui.graphical.edit.policies;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DecorationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.decorator.DecoratorService;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.Decoration;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoration;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecorator;
import org.eclipse.sirius.diagram.ui.tools.internal.providers.decorators.SiriusDecoration;

public class SiriusDecorationEditPolicy extends DecorationEditPolicy {
    public class SiriusDecoratorTarget extends DecoratorTarget {

        /**
         * {@inheritDoc}
         */
        public IDecoration addDecoration(IFigure figure, Locator locator, boolean isVolatile) {

            Decoration decoration = new SiriusDecoration((GraphicalEditPart) getAdapter(GraphicalEditPart.class));
            decoration.add(figure);
            decoration.setSize(figure.getSize());

            GraphicalEditPart ownerEditPart = (GraphicalEditPart) getAdapter(GraphicalEditPart.class);
            decoration.setOwnerFigure(ownerEditPart.getFigure());
            decoration.setLocator(locator);

            // Register this figure with it's owner editpart so mouse events
            // will be propagated to it's host.
            ownerEditPart.getViewer().getVisualPartMap().put(decoration, ownerEditPart);

            IFigure pane = getLayer(isVolatile ? DiagramRootEditPart.DECORATION_UNPRINTABLE_LAYER : DiagramRootEditPart.DECORATION_PRINTABLE_LAYER);

            pane.add(decoration);
            return decoration;
        }

    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {
        if (decorators == null) {
            decorators = new HashMap();
            DecoratorService.getInstance().createDecorators(new SiriusDecoratorTarget());
        }
        for (Iterator iter = decorators.values().iterator(); iter.hasNext(); ) {
            IDecorator decorator = (IDecorator) iter.next();
            decorator.refresh();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void activate() {
        if (decorators == null) {
            decorators = new HashMap();
            DecoratorService.getInstance().createDecorators(new SiriusDecoratorTarget());
        }
        if (decorators != null) {
            for (Iterator iter = decorators.values().iterator(); iter.hasNext(); ) {
                IDecorator decorator = (IDecorator) iter.next();
                decorator.activate();
            }
        }
    }

}
