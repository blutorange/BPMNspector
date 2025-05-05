package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.112
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext112Test extends TestCase {

    private static final String ERR_MSG = "A boundary event must not be the target of a Sequence Flow.";

    @Test
    public void testConstraintFailIncomingSeqFlow() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT112_failure_incomingSeqFlow.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:boundaryEvent", 23);
    }

    @Override
    protected String getExtNumber() {
        return "112";
    }
}
