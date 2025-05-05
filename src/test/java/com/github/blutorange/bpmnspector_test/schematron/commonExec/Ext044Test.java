package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.044
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext044Test extends TestCase {

    private static final String ERR_MSG = "The ItemDefinition of the DataInput of the ServiceTask and the inMessage "
            + "itemDefinition of the referenced Operation must be equal.";

    @Test
    public void testConstraintFailDifferentItemDefNoStructureRef() throws ValidationException {
        ValidationResult result =
                verifyInvalidResult(createFile("EXT044_failure_differentItemDef_noStructureRef.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:serviceTask", 17);
    }

    @Test
    public void testConstraintFailDifferentItemDefUnequalStructureRef() throws ValidationException {
        ValidationResult result =
                verifyInvalidResult(createFile("EXT044_failure_differentItemDef_unequalStructureRef.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:serviceTask", 17);
    }

    @Test
    public void testConstraintSuccessDifferentItemDefEqualStructureRef() throws ValidationException {
        verifyValidResult(createFile("EXT044_success_differentItemDef_equalStructureRef.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "044";
    }
}
