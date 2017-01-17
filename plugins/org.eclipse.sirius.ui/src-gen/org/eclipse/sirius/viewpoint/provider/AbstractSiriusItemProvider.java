package org.eclipse.sirius.viewpoint.provider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.sirius.viewpoint.description.tool.ChangeContext;
import org.eclipse.sirius.viewpoint.description.tool.InitialOperation;
import org.eclipse.sirius.viewpoint.description.tool.ToolFactory;
import org.eclipse.sirius.viewpoint.description.tool.ToolPackage;

/**
 * This class is used as supertype for all Sirius {@link IItemLabelProvider}. It
 * is used to initialize some Sirius model elements.
 * 
 * @author <a href="mailto:laurent.fasani@obeo.fr">Laurent Fasani</a> *
 */
public class AbstractSiriusItemProvider extends ItemProviderAdapter {

    public AbstractSiriusItemProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
     * The override allows to initialize tools initial operation with a
     * "Change Context" operation that set self as context.
     * 
     * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#createChildParameter(
     *      java.lang.Object, java.lang.Object)
     * 
     **/
    @Override
    protected CommandParameter createChildParameter(Object feature, Object child) {
        if (child instanceof EObject) {
            EObject obj = (EObject) child;
            for (EReference ref : obj.eClass().getEAllReferences()) {
                if (ref.isContainment() && ref.getEReferenceType() == ToolPackage.Literals.INITIAL_OPERATION) {
                    InitialOperation begin = ToolFactory.eINSTANCE.createInitialOperation();
                    ChangeContext noop = ToolFactory.eINSTANCE.createChangeContext();
                    noop.setBrowseExpression("var:self"); //$NON-NLS-1$
                    begin.setFirstModelOperations(noop);
                    obj.eSet(ref, begin);
                }
            }
        }
        return super.createChildParameter(feature, child);
    }
}
