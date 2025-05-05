package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.023
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext023Test extends TestCase {

    @Test
    public void testConstraintNoIncomingFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_no_incoming.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                "The target element of the sequence flow must reference the SequenceFlow definition using their incoming attribute.",
                "/bpmn:definitions/bpmn:process/bpmn:sequenceFlow",
                10);
    }

    @Test
    public void testConstraintNoOutgoingFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_no_outgoing.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                "The source element of the sequence flow must reference the SequenceFlow definition using their outgoing attribute.",
                "/bpmn:definitions/bpmn:process/bpmn:sequenceFlow",
                10);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "023";
    }
}
