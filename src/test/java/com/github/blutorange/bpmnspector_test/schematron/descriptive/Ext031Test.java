package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.031
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext031Test extends TestCase {

    private static final String ERRORMESSAGE = "A message flow must connect 'InteractionNodes' from different Pools";
    private static final String XPATHSTRING = "/bpmn:definitions/bpmn:collaboration/bpmn:messageFlow";

    @Test
    public void testConstraintCircleFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("Fail_circle.bpmn"), 1);
        assertFirstViolation(result.getViolations().get(0));
    }

    @Test
    public void testConstraintFromPoolFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("Fail_message_flow_from_pool.bpmn"), 2);
        assertFirstViolation(result.getViolations().get(0));
        assertTargetViolation(result.getViolations().get(1));
    }

    @Test
    public void testConstraintToPoolFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("Fail_message_flow_to_pool.bpmn"), 2);
        assertFirstViolation(result.getViolations().get(0));
        assertSourceViolation(result.getViolations().get(1));
    }

    @Test
    public void testConstraintSamePoolFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("Fail_message_flow_in_same_pool.bpmn"), 3);
        assertFirstViolation(result.getViolations().get(0));
        assertSourceViolation(result.getViolations().get(1));
        assertTargetViolation(result.getViolations().get(2));
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("Success.bpmn"));
    }

    private void assertFirstViolation(Violation v) {
        assertViolation(v, ERRORMESSAGE, XPATHSTRING, 7);
    }

    private void assertSourceViolation(Violation v) {
        assertViolation(
                v,
                "A Start Event MUST NOT be a source for a message flow",
                "/bpmn:definitions/bpmn:collaboration/bpmn:messageFlow",
                7);
    }

    private void assertTargetViolation(Violation v) {
        assertViolation(
                v,
                "An End Event MUST NOT be a target for a message flow",
                "/bpmn:definitions/bpmn:collaboration/bpmn:messageFlow",
                7);
    }

    @Override
    protected String getExtNumber() {
        return "031";
    }
}
