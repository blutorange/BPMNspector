package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.048
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext048Test extends TestCase {

    private static final String ERR_MSG =
            "An Item must be referenced which must be declared in referenced Message definition.";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT048_failure.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:sendTask", 14);
    }

    @Test
    public void testConstraintFailDifferentItemDefUnequalStructureRef() throws ValidationException {
        ValidationResult result =
                verifyInvalidResult(createFile("EXT048_failure_differentItemDef_unequalStructureRef.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:sendTask", 15);
    }

    @Test
    public void testConstraintSuccessDifferentItemDefEqualStructureRef() throws ValidationException {
        verifyValidResult(createFile("EXT048_success_differentItemDef_equalStructureRef.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "048";
    }
}
