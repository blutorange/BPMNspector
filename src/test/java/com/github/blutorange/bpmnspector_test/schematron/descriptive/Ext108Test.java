package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.108
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext108Test extends TestCase {

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("Fail.bpmn"), 2);
        assertViolation(
                result.getViolations().get(0),
                "A message flow must connect 'InteractionNodes' from different Pools",
                "/bpmn:definitions/bpmn:collaboration/bpmn:messageFlow",
                7);
        assertViolation(
                result.getViolations().get(1),
                "An End Event MUST NOT be a target for a message flow",
                "/bpmn:definitions/bpmn:collaboration/bpmn:messageFlow",
                7);
    }

    @Override
    protected String getExtNumber() {
        return "108";
    }
}
