package org.eclipse.sirius.properties.core.internal.validation.description.constraints;

import java.text.MessageFormat;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.sirius.properties.AbstractButtonDescription;
import org.eclipse.sirius.properties.AbstractCheckboxDescription;
import org.eclipse.sirius.properties.AbstractControlDescription;
import org.eclipse.sirius.properties.AbstractDynamicMappingForDescription;
import org.eclipse.sirius.properties.AbstractDynamicMappingIfDescription;
import org.eclipse.sirius.properties.AbstractHyperlinkDescription;
import org.eclipse.sirius.properties.AbstractRadioDescription;
import org.eclipse.sirius.properties.AbstractSelectDescription;
import org.eclipse.sirius.properties.AbstractTextAreaDescription;
import org.eclipse.sirius.properties.AbstractTextDescription;
import org.eclipse.sirius.properties.PropertiesPackage;
import org.eclipse.sirius.properties.util.PropertiesSwitch;
import org.eclipse.sirius.tools.internal.validation.AbstractConstraint;

@SuppressWarnings("restriction")
public class PropertiesConstraint extends AbstractConstraint {
    @Override
    public IStatus validate(IValidationContext ctx) {
        final EObject eObj = ctx.getTarget();
        final EMFEventType eventType = ctx.getEventType();
        //
        // In the case of batch mode.
        if (shouldValidate(eObj) && eventType == EMFEventType.NULL) {
            final IStatus status = ctx.createSuccessStatus();
            final PropertiesSwitchValidator propertiesSwitchValidator = new PropertiesSwitchValidator(ctx, status);
            IStatus result = propertiesSwitchValidator.doSwitch(eObj);
            return result;
        }
        return ctx.createSuccessStatus();
    }

    public class PropertiesSwitchValidator extends PropertiesSwitch<IStatus> {

        /**
         * The current status.
         */
        private IStatus currentStatus;

        /**
         * The validation context.
         */
        private final IValidationContext ctx;

        /**
         * Creates a new validator.
         * 
         * @param ctx
         *            the validation context (mandatory).
         * @param currentStatus
         *            the current status (optional).
         */
        public PropertiesSwitchValidator(final IValidationContext ctx, final IStatus currentStatus) {
            this.currentStatus = currentStatus;
            this.ctx = ctx;
            if (this.currentStatus == null) {
                this.currentStatus = ctx.createSuccessStatus();
            }
        }

        @Override
        public IStatus caseAbstractTextDescription(AbstractTextDescription object) {
            if (this.currentStatus.isOK()) {
                final IStatus superStatus = super.caseAbstractTextDescription(object);
                if (superStatus == null || superStatus.isOK()) {
                    this.currentStatus = checkInitialOperationPredicate(ctx, object, PropertiesPackage.Literals.ABSTRACT_TEXT_DESCRIPTION__INITIAL_OPERATION,
                            PropertiesPackage.Literals.ABSTRACT_TEXT_DESCRIPTION__EXTENDS);
                } else {
                    this.currentStatus = superStatus;
                }
            }
            return this.currentStatus;
        }

        @Override
        public IStatus caseAbstractTextAreaDescription(AbstractTextAreaDescription object) {
            if (this.currentStatus.isOK()) {
                final IStatus superStatus = super.caseAbstractTextAreaDescription(object);
                if (superStatus == null || superStatus.isOK()) {
                    this.currentStatus = checkInitialOperationPredicate(ctx, object, PropertiesPackage.Literals.ABSTRACT_TEXT_AREA_DESCRIPTION__INITIAL_OPERATION,
                            PropertiesPackage.Literals.ABSTRACT_TEXT_AREA_DESCRIPTION__EXTENDS);
                } else {
                    this.currentStatus = superStatus;
                }
            }
            return this.currentStatus;
        }

        @Override
        public IStatus caseAbstractButtonDescription(AbstractButtonDescription object) {
            if (this.currentStatus.isOK()) {
                final IStatus superStatus = super.caseAbstractButtonDescription(object);
                if (superStatus == null || superStatus.isOK()) {
                    this.currentStatus = checkInitialOperationPredicate(ctx, object, PropertiesPackage.Literals.ABSTRACT_BUTTON_DESCRIPTION__INITIAL_OPERATION,
                            PropertiesPackage.Literals.ABSTRACT_BUTTON_DESCRIPTION__EXTENDS);
                } else {
                    this.currentStatus = superStatus;
                }
            }
            return this.currentStatus;
        }

        @Override
        public IStatus caseAbstractRadioDescription(AbstractRadioDescription object) {
            if (this.currentStatus.isOK()) {
                final IStatus superStatus = super.caseAbstractRadioDescription(object);
                if (superStatus == null || superStatus.isOK()) {
                    this.currentStatus = checkInitialOperationPredicate(ctx, object, PropertiesPackage.Literals.ABSTRACT_RADIO_DESCRIPTION__INITIAL_OPERATION,
                            PropertiesPackage.Literals.ABSTRACT_RADIO_DESCRIPTION__EXTENDS);
                } else {
                    this.currentStatus = superStatus;
                }
            }
            return this.currentStatus;
        }

        @Override
        public IStatus caseAbstractHyperlinkDescription(AbstractHyperlinkDescription object) {
            if (this.currentStatus.isOK()) {
                final IStatus superStatus = super.caseAbstractHyperlinkDescription(object);
                if (superStatus == null || superStatus.isOK()) {
                    this.currentStatus = checkInitialOperationPredicate(ctx, object, PropertiesPackage.Literals.ABSTRACT_HYPERLINK_DESCRIPTION__INITIAL_OPERATION,
                            PropertiesPackage.Literals.ABSTRACT_HYPERLINK_DESCRIPTION__EXTENDS);
                } else {
                    this.currentStatus = superStatus;
                }
            }
            return this.currentStatus;
        }

        @Override
        public IStatus caseAbstractCheckboxDescription(AbstractCheckboxDescription object) {
            if (this.currentStatus.isOK()) {
                final IStatus superStatus = super.caseAbstractCheckboxDescription(object);
                if (superStatus == null || superStatus.isOK()) {
                    this.currentStatus = checkInitialOperationPredicate(ctx, object, PropertiesPackage.Literals.ABSTRACT_CHECKBOX_DESCRIPTION__INITIAL_OPERATION,
                            PropertiesPackage.Literals.ABSTRACT_CHECKBOX_DESCRIPTION__EXTENDS);
                } else {
                    this.currentStatus = superStatus;
                }
            }
            return this.currentStatus;
        }

        @Override
        public IStatus caseAbstractSelectDescription(AbstractSelectDescription object) {
            if (this.currentStatus.isOK()) {
                final IStatus superStatus = super.caseAbstractSelectDescription(object);
                if (superStatus == null || superStatus.isOK()) {
                    this.currentStatus = checkInitialOperationPredicate(ctx, object, PropertiesPackage.Literals.ABSTRACT_SELECT_DESCRIPTION__INITIAL_OPERATION,
                            PropertiesPackage.Literals.ABSTRACT_SELECT_DESCRIPTION__EXTENDS);
                } else {
                    this.currentStatus = superStatus;
                }
            }
            return this.currentStatus;
        }

        private IStatus checkInitialOperationPredicate(IValidationContext context, AbstractControlDescription eObject, EStructuralFeature initialOperationFeature, EStructuralFeature extendsFeature) {
            // Check that the button defines an initial operation or extends another button
            if (isBlank(eObject, initialOperationFeature) && isBlank(eObject, extendsFeature)) {
                return context.createFailureStatus(MessageFormat.format(Messages.PropertiesConstraint_InitialOperationPredicateFailure, eObject.getName()));
            }
            return context.createSuccessStatus();
        }

        @Override
        public IStatus caseAbstractDynamicMappingForDescription(AbstractDynamicMappingForDescription object) {
            if (this.currentStatus.isOK()) {
                final IStatus superStatus = super.caseAbstractDynamicMappingForDescription(object);
                if (superStatus == null || superStatus.isOK()) {
                    this.currentStatus = checkDynamicMappingForPredicate(ctx, object);
                } else {
                    this.currentStatus = superStatus;
                }
            }
            return this.currentStatus;
        }

        private IStatus checkDynamicMappingForPredicate(IValidationContext context, AbstractDynamicMappingForDescription eObject) {
            IStatus status = context.createSuccessStatus();
            if (isBlank(eObject, PropertiesPackage.Literals.ABSTRACT_DYNAMIC_MAPPING_FOR_DESCRIPTION__EXTENDS)) {
                if (isBlank(eObject, PropertiesPackage.Literals.ABSTRACT_DYNAMIC_MAPPING_FOR_DESCRIPTION__IFS)) {
                    // Check that the dynamic mapping for defines an if or extends another for
                    status = context.createFailureStatus(MessageFormat.format(Messages.PropertiesConstraint_IfsPredicateFailure, eObject.getName()));
                } else if (isBlank(eObject, PropertiesPackage.Literals.ABSTRACT_DYNAMIC_MAPPING_FOR_DESCRIPTION__ITERATOR)) {
                    // Check that the dynamic mapping for defines an iterator or extends another for
                    status = context.createFailureStatus(MessageFormat.format(Messages.PropertiesConstraint_IteratorPredicateFailure, eObject.getName()));
                } else if (isBlank(eObject, PropertiesPackage.Literals.ABSTRACT_DYNAMIC_MAPPING_FOR_DESCRIPTION__ITERABLE_EXPRESSION)) {
                    // Check that the dynamic mapping for defines an iterator or extends another for
                    status = context.createFailureStatus(MessageFormat.format(Messages.PropertiesConstraint_IterableExpressionPredicateFailure, eObject.getName()));
                }
            }
            return status;
        }

        @Override
        public IStatus caseAbstractDynamicMappingIfDescription(AbstractDynamicMappingIfDescription object) {
            if (this.currentStatus.isOK()) {
                final IStatus superStatus = super.caseAbstractDynamicMappingIfDescription(object);
                if (superStatus == null || superStatus.isOK()) {
                    this.currentStatus = checkDynamicMappingIfPredicate(ctx, object);
                } else {
                    this.currentStatus = superStatus;
                }
            }
            return this.currentStatus;
        }

        private IStatus checkDynamicMappingIfPredicate(IValidationContext context, AbstractDynamicMappingIfDescription eObject) {
            IStatus status = context.createSuccessStatus();
            if (isBlank(eObject, PropertiesPackage.Literals.ABSTRACT_DYNAMIC_MAPPING_IF_DESCRIPTION__EXTENDS)) {
                if (isBlank(eObject, PropertiesPackage.Literals.ABSTRACT_DYNAMIC_MAPPING_IF_DESCRIPTION__WIDGET)) {
                    // Check that the dynamic mapping if defines a widget or extends another if
                    status = context.createFailureStatus(MessageFormat.format(Messages.PropertiesConstraint_WidgetPredicateFailure, eObject.getName()));
                } else if (isBlank(eObject, PropertiesPackage.Literals.ABSTRACT_DYNAMIC_MAPPING_IF_DESCRIPTION__PREDICATE_EXPRESSION)) {
                    // Check that the dynamic mapping if defines a predicate expression or extends another if
                    status = context.createFailureStatus(MessageFormat.format(Messages.PropertiesConstraint_PredicateExpressionPredicateFailure, eObject.getName()));
                }
            }
            return status;
        }

        private boolean isBlank(EObject eObject, EStructuralFeature feature) {
            return !eObject.eIsSet(feature);
        }
    }
}
