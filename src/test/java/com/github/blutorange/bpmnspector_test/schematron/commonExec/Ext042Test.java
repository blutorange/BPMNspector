package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.042
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext042Test extends TestCase {

    private static final String ERR_MSG = "If a ServiceTask references an operation, exactly one InputSet must be "
            + "defined in the ioSpecification.";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT042_failure.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:serviceTask", 14);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT042_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "042";
    }
}
