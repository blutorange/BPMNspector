package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.119
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext119Test extends TestCase {

    private static final String ERR_MSG_CATCH = "An intermediateCatchEvent must not be a source of a messageFlow.";
    private static final String ERR_MSG_THROW = "An intermediateThrowEvent must not be a target of a messageFlow.";

    @Test
    public void testConstraintFailCatchInOut() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT119_failure_catchInOut.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG_CATCH,
                "/bpmn:definitions/bpmn:process/bpmn:intermediateCatchEvent",
                10);
    }

    @Test
    public void testConstraintFailCatchOutgoing() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT119_failure_catchOutgoing.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG_CATCH,
                "/bpmn:definitions/bpmn:process/bpmn:intermediateCatchEvent",
                10);
    }

    @Test
    public void testConstraintFailThrowInOut() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT119_failure_throwInOut.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG_THROW,
                "/bpmn:definitions/bpmn:process/bpmn:intermediateThrowEvent",
                7);
    }

    @Test
    public void testConstraintFailThrowIncoming() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT119_failure_throwIncoming.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG_THROW,
                "/bpmn:definitions/bpmn:process/bpmn:intermediateThrowEvent",
                7);
    }

    @Test
    public void testConstraintSuccessCatchIncoming() throws ValidationException {
        verifyValidResult(createFile("EXT119_success_catchIncoming.bpmn"));
    }

    @Test
    public void testConstraintSuccessThrowOutgoing() throws ValidationException {
        verifyValidResult(createFile("EXT119_success_throwOutgoing.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "119";
    }
}
