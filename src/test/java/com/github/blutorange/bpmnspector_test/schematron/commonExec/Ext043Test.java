package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.043
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext043Test extends TestCase {

    private static final String ERR_MSG = "If a ServiceTask references an operation, at most one OutputSet can be "
            + "defined in the ioSpecification.";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT043_failure.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:serviceTask", 16);
    }

    @Override
    protected String getExtNumber() {
        return "043";
    }
}
