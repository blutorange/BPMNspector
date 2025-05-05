package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.136
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext136Test extends TestCase {

    private static final String ERR_MSG = "An Event Gateway MUST have two or more outgoing Sequence Flows.";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT136_failure.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:eventBasedGateway", 4);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT136_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "136";
    }
}
