package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.138
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext138Test extends TestCase {

    private static final String ERR_MSG = "An eventBasedGateway may only be connected to a ReceiveTask or one of the "
            + "following intermediate Events: Message, Signal, Timer, Conditional, and Multiple (which can only include "
            + "the previous triggers)";

    @Test
    public void testConstraintFailInvalidEventType() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT138_failure_invalidEventType.bpmn"), 2);
        assertViolation(
                result.getViolations().get(1), ERR_MSG, "/bpmn:definitions/bpmn:process[2]/bpmn:sequenceFlow[7]", 47);
    }

    @Test
    public void testConstraintFailThrowEvent() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT138_failure_invalidThrowEvent.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:sequenceFlow[3]", 24);
    }

    @Test
    public void testConstraintFailInvalidTaskType() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT138_failure_invalidTaskType.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:sequenceFlow[3]", 24);
    }

    @Test
    public void testConstraintSuccessReceiveTask() throws ValidationException {
        verifyValidResult(createFile("EXT138_success_receiveTask.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "138";
    }
}
