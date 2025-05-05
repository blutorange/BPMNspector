package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.101
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext101Test extends TestCase {

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail.bpmn"), 2);
        assertViolation(
                result.getViolations().get(0),
                "The source element of the sequence flow must reference the SequenceFlow definition using their outgoing attribute.",
                "/bpmn:definitions/bpmn:process/bpmn:sequenceFlow",
                9);
        assertViolation(
                result.getViolations().get(1),
                "A startEvent must have an outgoing sub element",
                "/bpmn:definitions/bpmn:process/bpmn:startEvent",
                4);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "101";
    }
}
