package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.116
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext116Test extends TestCase {

    private static final String ERR_MSG = "Intermediate Events MUST be a source of at least a Sequence Flow.";

    @Test
    public void testConstraintFailNoOutgoing() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT116_failure_noOutgoing.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "(//bpmn:intermediateThrowEvent[not(bpmn:linkEventDefinition) and not(bpmn:outgoing)])[1]",
                11);
    }

    @Test
    public void testConstraintFailNoOutgoingCatch() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT116_failure_noOutgoingCatch.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0), ERR_MSG, "(//bpmn:intermediateCatchEvent[not(bpmn:outgoing)])[1]", 11);
    }

    @Override
    protected String getExtNumber() {
        return "116";
    }
}
