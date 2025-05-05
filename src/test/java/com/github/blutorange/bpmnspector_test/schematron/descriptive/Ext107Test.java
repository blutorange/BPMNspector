package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.107
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext107Test extends TestCase {

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("fail.bpmn"), 2);
        assertViolation(
                result.getViolations().get(0),
                "The target element of the sequence flow must reference the SequenceFlow definition using their incoming attribute.",
                "/bpmn:definitions/bpmn:process/bpmn:sequenceFlow",
                9);
        assertViolation(
                result.getViolations().get(1),
                "An End Event MUST have at least one incoming Sequence Flow",
                "/bpmn:definitions/bpmn:process/bpmn:endEvent",
                4);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "107";
    }
}
