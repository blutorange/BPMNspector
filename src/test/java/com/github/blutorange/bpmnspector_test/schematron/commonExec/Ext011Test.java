package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.011
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext011Test extends TestCase {

    private static final String ERR_MSG =
            "An escalationCode must be present if the escalation is used in an EndEvent or "
                    + "in an intermediate throw Event if the trigger is an Escalation.";

    @Test
    public void testConstraintFailEndEventNoRef() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT011_failure_endEventNoRef.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:endEvent", 7);
    }

    @Test
    public void testConstraintFailEndEventNoEscCode() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT011_failure_endEventNoEscCode.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:endEvent", 7);
    }

    @Test
    public void testConstraintFailIntermediateThrowNoEscCode() throws ValidationException {
        ValidationResultBuilder result =
                verifyInvalidResult(createFile("EXT011_failure_intermediateThrowNoEscCode.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:intermediateThrowEvent",
                10);
    }

    @Test
    public void testConstraintSuccessEndEvent() throws ValidationException {
        verifyValidResult(createFile("EXT011_success_endEvent.bpmn"));
    }

    @Test
    public void testConstraintSuccessIntermediateThrow() throws ValidationException {
        verifyValidResult(createFile("EXT011_success_intermediateThrow.bpmn"));
    }

    @Test
    public void testConstraintSuccessBoundaryNoEscCode() throws ValidationException {
        verifyValidResult(createFile("EXT011_success_boundaryNoEscCode.bpmn"));
    }

    @Test
    public void testConstraintSuccessNotExecutable() throws ValidationException {
        verifyValidResult(createFile("EXT011_success_notExecutable.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "011";
    }
}
