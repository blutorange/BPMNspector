package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.110
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext110Test extends TestCase {

    private static final String ERR_MSG_CANCEL_TRUE = "A boundaryEvent with cancelActivity='true' must have at least "
            + "one eventDefinition but does not allow link or terminate eventDefinitions.";

    private static final String ERR_MSG_CANCEL_FALSE = "A boundaryEvent with cancelActivity='false' must have at least "
            + "one eventDefinition but does not allow link, error, cancel, compensate or terminate eventDefinitions.";

    @Test
    public void testConstraintFailCancelNone() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT110_failure_cancel_none.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG_CANCEL_TRUE,
                "/bpmn:definitions/bpmn:process/bpmn:boundaryEvent",
                16);
    }

    @Test
    public void testConstraintFailCancelTerminate() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT110_failure_cancel_terminate.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG_CANCEL_TRUE,
                "/bpmn:definitions/bpmn:process/bpmn:boundaryEvent",
                16);
    }

    @Test
    public void testConstraintFailCancelLink() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT110_failure_cancel_link.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG_CANCEL_TRUE,
                "/bpmn:definitions/bpmn:process/bpmn:boundaryEvent",
                16);
    }

    @Test
    public void testConstraintFailNoCancelNone() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT110_failure_noCancel_none.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG_CANCEL_FALSE,
                "/bpmn:definitions/bpmn:process/bpmn:boundaryEvent",
                16);
    }

    @Test
    public void testConstraintFailNoCancelTerminate() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT110_failure_noCancel_terminate.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG_CANCEL_FALSE,
                "/bpmn:definitions/bpmn:process/bpmn:boundaryEvent",
                16);
    }

    @Test
    public void testConstraintFailNoCancelCancel() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT110_failure_noCancel_cancel.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG_CANCEL_FALSE,
                "/bpmn:definitions/bpmn:process/bpmn:boundaryEvent",
                16);
    }

    @Test
    public void testConstraintFailNoCancelCompensate() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT110_failure_noCancel_compensate.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG_CANCEL_FALSE,
                "/bpmn:definitions/bpmn:process/bpmn:boundaryEvent",
                16);
    }

    @Test
    public void testConstraintFailNoCancelError() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT110_failure_noCancel_error.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG_CANCEL_FALSE,
                "/bpmn:definitions/bpmn:process/bpmn:boundaryEvent",
                16);
    }

    @Test
    public void testConstraintSuccessCancelMessage() throws ValidationException {
        verifyValidResult(createFile("EXT110_success_cancel_message.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "110";
    }
}
