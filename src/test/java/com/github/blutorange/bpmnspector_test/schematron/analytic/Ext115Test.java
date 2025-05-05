package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.115
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext115Test extends TestCase {

    private static final String ERR_MSG = "Intermediate Events MUST be a target of at least a Sequence Flow.";

    @Test
    public void testConstraintFailNoIncoming() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT115_failure_noIncoming.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:intermediateThrowEvent",
                11);
    }

    @Test
    public void testConstraintFailNoIncomingCatch() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT115_failure_noIncomingCatch.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:intermediateCatchEvent",
                11);
    }

    @Test
    public void testConstraintSuccess_noStartEnd() throws ValidationException {
        verifyValidResult(createFile("EXT115_success_noStartEnd.bpmn"));
    }

    @Test
    public void testConstraintSuccess_StartEnd() throws ValidationException {
        verifyValidResult(createFile("EXT115_success_StartEnd.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "115";
    }
}
