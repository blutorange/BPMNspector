package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.046
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext051Test extends TestCase {

    private static final String ERR_MSG =
            "If a ReceiveTask references a message, at most one DataOutput must be defined "
                    + "in the ioSpecification.";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT051_failure.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:receiveTask", 9);
    }

    @Override
    protected String getExtNumber() {
        return "051";
    }
}
