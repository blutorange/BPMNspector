package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.143
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext143Test extends TestCase {

    private static final String ERR_MSG =
            "A compensateBoundaryEvent must be connected with an Association to a Compensation Activity.";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT143_failure.bpmn"), 2);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:boundaryEvent", 5);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT143_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "143";
    }
}
