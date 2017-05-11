package org.eclipse.sirius.tests.ui.properties.internal.vsm;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.sirius.ecore.extender.tool.api.ModelUtils;
import org.eclipse.sirius.properties.ControlDescription;
import org.eclipse.sirius.properties.DynamicMappingForDescription;
import org.eclipse.sirius.properties.DynamicMappingIfDescription;
import org.eclipse.sirius.properties.ViewExtensionDescription;
import org.eclipse.sirius.tests.support.api.SiriusDiagramTestCase;
import org.eclipse.sirius.viewpoint.description.Group;

public class PropertiesVSMValidationRulesTest extends SiriusDiagramTestCase {

    private static final String EXTENDS_ERROR = "The description must extend another description";

    private static final String INITIAL_OPERATION_ERROR = "The description must define at least one or more initial operations";

    private Group modelerToValidate;

    @Override
    protected void setUp() throws Exception {
        ResourceSet set = new ResourceSetImpl();
        modelerToValidate = (Group) ModelUtils.load(URI.createPlatformPluginURI("/org.eclipse.sirius.tests.ui.properties/data/vsm/validateCustomRules.odesign", true), set);
    }

    /**
     * Test VSM custom validation rules on text.
     */
    public void testValidationVSMForText() {
        Diagnostician diagnostician = new Diagnostician();
        EList<ControlDescription> controls = ((ViewExtensionDescription) modelerToValidate.getExtensions().get(0)).getCategories().get(0).getGroups().get(0).getControls();

        ControlDescription missingInitialOperation = controls.get(0);
        Diagnostic diagnostic = diagnostician.validate(missingInitialOperation);
        Diagnostic diag = diagnostic.getChildren().get(0);
        assertEquals(INITIAL_OPERATION_ERROR, "The description TextMissingInitialOperation must define at least one or more initial operations or extend another description", diag.getMessage());
        assertEquals(INITIAL_OPERATION_ERROR, Diagnostic.ERROR, diag.getSeverity());

        ControlDescription extendsAnotherControl = controls.get(1);
        diagnostic = diagnostician.validate(extendsAnotherControl);
        assertEquals(EXTENDS_ERROR, Diagnostic.OK, diagnostic.getSeverity());
    }

    /**
     * Test VSM custom validation rules on button.
     */
    public void testValidationVSMForButton() {
        Diagnostician diagnostician = new Diagnostician();
        EList<ControlDescription> controls = ((ViewExtensionDescription) modelerToValidate.getExtensions().get(0)).getCategories().get(0).getGroups().get(1).getControls();

        ControlDescription missingInitialOperation = controls.get(0);
        Diagnostic diagnostic = diagnostician.validate(missingInitialOperation);
        Diagnostic diag = diagnostic.getChildren().get(0);
        assertEquals(INITIAL_OPERATION_ERROR, "The description ButtonMissingInitialOperation must define at least one or more initial operations or extend another description", diag.getMessage());
        assertEquals(INITIAL_OPERATION_ERROR, Diagnostic.ERROR, diag.getSeverity());

        ControlDescription extendsAnotherControl = controls.get(1);
        diagnostic = diagnostician.validate(extendsAnotherControl);
        assertEquals(EXTENDS_ERROR, Diagnostic.OK, diagnostic.getSeverity());
    }

    /**
     * Test VSM custom validation rules on checkbox.
     */
    public void testValidationVSMForCheckbox() {
        Diagnostician diagnostician = new Diagnostician();
        EList<ControlDescription> controls = ((ViewExtensionDescription) modelerToValidate.getExtensions().get(0)).getCategories().get(0).getGroups().get(2).getControls();

        ControlDescription missingInitialOperation = controls.get(0);
        Diagnostic diagnostic = diagnostician.validate(missingInitialOperation);
        Diagnostic diag = diagnostic.getChildren().get(0);
        assertEquals(INITIAL_OPERATION_ERROR, "The description CheckboxMissingInitialOperation must define at least one or more initial operations or extend another description", diag.getMessage());
        assertEquals(INITIAL_OPERATION_ERROR, Diagnostic.ERROR, diag.getSeverity());

        ControlDescription extendsAnotherControl = controls.get(1);
        diagnostic = diagnostician.validate(extendsAnotherControl);
        assertEquals(EXTENDS_ERROR, Diagnostic.OK, diagnostic.getSeverity());
    }

    /**
     * Test VSM custom validation rules on select.
     */
    public void testValidationVSMForSelect() {
        Diagnostician diagnostician = new Diagnostician();
        EList<ControlDescription> controls = ((ViewExtensionDescription) modelerToValidate.getExtensions().get(0)).getCategories().get(0).getGroups().get(3).getControls();

        ControlDescription missingInitialOperation = controls.get(0);
        Diagnostic diagnostic = diagnostician.validate(missingInitialOperation);
        Diagnostic diag = diagnostic.getChildren().get(0);
        assertEquals(INITIAL_OPERATION_ERROR, "The description SelectMissingInitialOperation must define at least one or more initial operations or extend another description", diag.getMessage());
        assertEquals(INITIAL_OPERATION_ERROR, Diagnostic.ERROR, diag.getSeverity());

        ControlDescription extendsAnotherControl = controls.get(1);
        diagnostic = diagnostician.validate(extendsAnotherControl);
        assertEquals(EXTENDS_ERROR, Diagnostic.OK, diagnostic.getSeverity());
    }

    /**
     * Test VSM custom validation rules on dynamic mapping for.
     */
    public void testValidationVSMForDynamicMappingFor() {
        Diagnostician diagnostician = new Diagnostician();
        EList<ControlDescription> controls = ((ViewExtensionDescription) modelerToValidate.getExtensions().get(0)).getCategories().get(0).getGroups().get(4).getControls();

        ControlDescription missingIfs = controls.get(0);
        Diagnostic diagnostic = diagnostician.validate(missingIfs);
        Diagnostic diag = diagnostic.getChildren().get(0);
        assertEquals(INITIAL_OPERATION_ERROR, "The dynamic mapping for ForMissingIfs must define at least one or more ifs or extend another description", diag.getMessage());
        assertEquals(INITIAL_OPERATION_ERROR, Diagnostic.ERROR, diag.getSeverity());

        ControlDescription extendsAnotherControl = controls.get(1);
        diagnostic = diagnostician.validate(extendsAnotherControl);
        assertEquals(EXTENDS_ERROR, Diagnostic.OK, diagnostic.getSeverity());

        ControlDescription missingIterator = controls.get(2);
        diagnostic = diagnostician.validate(missingIterator);
        diag = diagnostic.getChildren().get(0);
        assertEquals("The description must define at least one iterator", "The dynamic mapping for ForMissingIterator must define at least one iterator or extend another description",
                diag.getMessage());
        assertEquals("The description must define at least one iterator", Diagnostic.ERROR, diag.getSeverity());

        ControlDescription missingIterableExpression = controls.get(3);
        diagnostic = diagnostician.validate(missingIterableExpression);
        diag = diagnostic.getChildren().get(0);
        assertEquals("The description must define at least one iterable expression",
                "The dynamic mapping for ForMissingIterableExpression must define at least one iterable expression or extend another description",
                diag.getMessage());
        assertEquals("The description must define at least one iterable expression", Diagnostic.ERROR, diag.getSeverity());
    }

    /**
     * Test VSM custom validation rules on dynamic mapping if.
     */
    public void testValidationVSMForDynamicMappingIf() {
        Diagnostician diagnostician = new Diagnostician();
        EList<DynamicMappingIfDescription> ifs = ((DynamicMappingForDescription) ((ViewExtensionDescription) modelerToValidate.getExtensions().get(0)).getCategories().get(0).getGroups().get(5)
                .getControls().get(0)).getIfs();

        DynamicMappingIfDescription missingWidget = ifs.get(0);
        Diagnostic diagnostic = diagnostician.validate(missingWidget);
        Diagnostic diag = diagnostic.getChildren().get(0);
        assertEquals("The description must define at least one widget", "The dynamic mapping if IfMissingWidget must define at least one widget or extend another description", diag.getMessage());
        assertEquals("The description must define at least one widget", Diagnostic.ERROR, diag.getSeverity());

        DynamicMappingIfDescription extendsAnotherControl = ifs.get(1);
        diagnostic = diagnostician.validate(extendsAnotherControl);
        assertEquals(EXTENDS_ERROR, Diagnostic.OK, diagnostic.getSeverity());

        DynamicMappingIfDescription missingPredicateExpression = ifs.get(2);
        diagnostic = diagnostician.validate(missingPredicateExpression);
        diag = diagnostic.getChildren().get(0);
        assertEquals("The description must define at least one predicate expression",
                "The dynamic mapping if IfMissingPredicateExpression must define at least one predicate expression or extend another description", diag.getMessage());
        assertEquals("The description must define at least one predicate expression", Diagnostic.ERROR, diag.getSeverity());
    }

    /**
     * Test VSM custom validation rules on text area.
     */
    public void testValidationVSMForTextArea() {
        Diagnostician diagnostician = new Diagnostician();
        EList<ControlDescription> controls = ((ViewExtensionDescription) modelerToValidate.getExtensions().get(0)).getCategories().get(0).getGroups().get(6).getControls();

        ControlDescription missingInitialOperation = controls.get(0);
        Diagnostic diagnostic = diagnostician.validate(missingInitialOperation);
        Diagnostic diag = diagnostic.getChildren().get(0);
        assertEquals(INITIAL_OPERATION_ERROR, "The description TextAreaMissingInitialOperation must define at least one or more initial operations or extend another description", diag.getMessage());
        assertEquals(INITIAL_OPERATION_ERROR, Diagnostic.ERROR, diag.getSeverity());

        ControlDescription extendsAnotherControl = controls.get(1);
        diagnostic = diagnostician.validate(extendsAnotherControl);
        assertEquals(EXTENDS_ERROR, Diagnostic.OK, diagnostic.getSeverity());
    }

    /**
     * Test VSM custom validation rules on radio.
     */
    public void testValidationVSMForRadio() {
        Diagnostician diagnostician = new Diagnostician();
        EList<ControlDescription> controls = ((ViewExtensionDescription) modelerToValidate.getExtensions().get(0)).getCategories().get(0).getGroups().get(7).getControls();

        ControlDescription missingInitialOperation = controls.get(0);
        Diagnostic diagnostic = diagnostician.validate(missingInitialOperation);
        Diagnostic diag = diagnostic.getChildren().get(0);
        assertEquals(INITIAL_OPERATION_ERROR, "The description RadioMissingInitialOperation must define at least one or more initial operations or extend another description", diag.getMessage());
        assertEquals(INITIAL_OPERATION_ERROR, Diagnostic.ERROR, diag.getSeverity());

        ControlDescription extendsAnotherControl = controls.get(1);
        diagnostic = diagnostician.validate(extendsAnotherControl);
        assertEquals(EXTENDS_ERROR, Diagnostic.OK, diagnostic.getSeverity());
    }

    /**
     * Test VSM custom validation rules on hyperlink.
     */
    public void testValidationVSMForHyperlink() {
        Diagnostician diagnostician = new Diagnostician();
        EList<ControlDescription> controls = ((ViewExtensionDescription) modelerToValidate.getExtensions().get(0)).getCategories().get(0).getGroups().get(8).getControls();

        ControlDescription missingInitialOperation = controls.get(0);
        Diagnostic diagnostic = diagnostician.validate(missingInitialOperation);
        Diagnostic diag = diagnostic.getChildren().get(0);
        assertEquals(INITIAL_OPERATION_ERROR, "The description HyperlinkMissingInitialOperation must define at least one or more initial operations or extend another description", diag.getMessage());
        assertEquals(INITIAL_OPERATION_ERROR, Diagnostic.ERROR, diag.getSeverity());

        ControlDescription extendsAnotherControl = controls.get(1);
        diagnostic = diagnostician.validate(extendsAnotherControl);
        assertEquals(EXTENDS_ERROR, Diagnostic.OK, diagnostic.getSeverity());
    }
}
