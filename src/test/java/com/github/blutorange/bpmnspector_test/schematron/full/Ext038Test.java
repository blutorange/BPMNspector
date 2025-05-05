package com.github.blutorange.bpmnspector_test.schematron.full;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.038
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext038Test extends TestCase {

    private static final String ERR_MSG =
            "The value of the completionQuantity attribute of an activity MUST NOT be less than 1.";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT038_failure.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:task", 7);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT038_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "038";
    }
}
