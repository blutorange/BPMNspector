package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.149
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext149Test extends TestCase {

    private static final String ERR_MSG = "Only messageEventDefinitions, escalationEventDefinitions, "
            + "compensationEventDefinitions, linkEventDefinitions and signalEventDefinitions are allowed for "
            + "intermediate throw events.";

    @Test
    public void testConstraintFailCancelThrow() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT149_failure_cancelThrow.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:intermediateThrowEvent",
                7);
    }

    @Test
    public void testConstraintFailConditionalThrow() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT149_failure_conditionalThrow.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:intermediateThrowEvent",
                7);
    }

    @Test
    public void testConstraintFailErrorThrow() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT149_failure_errorThrow.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:intermediateThrowEvent",
                7);
    }

    @Test
    public void testConstraintFailTerminateThrow() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT149_failure_terminateThrow.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:intermediateThrowEvent",
                7);
    }

    @Test
    public void testConstraintFailTimerThrow() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT149_failure_timerThrow.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:intermediateThrowEvent",
                7);
    }

    @Override
    protected String getExtNumber() {
        return "149";
    }
}
