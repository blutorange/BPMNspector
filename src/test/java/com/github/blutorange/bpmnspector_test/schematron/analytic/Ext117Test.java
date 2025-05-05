package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.117
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext117Test extends TestCase {

    private static final String ERR_MSG =
            "A Link Intermediate Event MUST NOT be both a target and a source of a Sequence Flow.";

    @Test
    public void testConstraintFailThrowOutgoing() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT117_failure_throwOutgoing.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "(//bpmn:intermediateThrowEvent[bpmn:linkEventDefinition and bpmn:outgoing])[1]",
                11);
    }

    @Test
    public void testConstraintFailCatchIncoming() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT117_failure_catchIncoming.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "(//bpmn:intermediateCatchEvent[bpmn:linkEventDefinition and bpmn:incoming])[1]",
                19);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT117_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "117";
    }
}
