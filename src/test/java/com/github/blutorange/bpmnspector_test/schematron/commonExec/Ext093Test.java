package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.093
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext093Test extends TestCase {

    @Test
    public void testConstraintFailBoundaryEventMissingDataOutput() throws ValidationException {
        ValidationResultBuilder result =
                verifyInvalidResult(createFile("EXT093_failure_boundaryEvent_missingDataOutput.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                "If dataOutputs are used in a BoundaryEvent for each eventDefinition a DataOutput must be defined.",
                "/bpmn:definitions/bpmn:process/bpmn:boundaryEvent",
                26);
    }

    @Test
    public void testConstraintFailEndEventMissingDataInput() throws ValidationException {
        ValidationResultBuilder result =
                verifyInvalidResult(createFile("EXT093_failure_endEvent_missingDataInput.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                "If dataInputs are used in an EndEvent for each eventDefinition a DataInput must be defined.",
                "/bpmn:definitions/bpmn:process/bpmn:endEvent",
                20);
    }

    @Test
    public void testConstraintFailIntCatchMissingDataOutput() throws ValidationException {
        ValidationResultBuilder result =
                verifyInvalidResult(createFile("EXT093_failure_intCatch_missingDataOutput.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                "If dataOutputs are used in an IntermediateCatchEvent for each eventDefinition a DataOutput must be defined.",
                "/bpmn:definitions/bpmn:process/bpmn:intermediateCatchEvent",
                15);
    }

    @Test
    public void testConstraintFailIntThrowMissingDataInput() throws ValidationException {
        ValidationResultBuilder result =
                verifyInvalidResult(createFile("EXT093_failure_intThrow_missingDataInput.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                "If dataInputs are used in an intermediateThrowEvent for each eventDefinition a DataInput must be defined.",
                "/bpmn:definitions/bpmn:process/bpmn:intermediateThrowEvent",
                15);
    }

    @Test
    public void testConstraintFailStartEventMissingDataOutput() throws ValidationException {
        ValidationResultBuilder result =
                verifyInvalidResult(createFile("EXT093_failure_startEvent_missingDataOutput.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                "If dataOutputs are used in a StartEvent for each eventDefinition a DataOutput must be defined.",
                "/bpmn:definitions/bpmn:process/bpmn:startEvent",
                12);
    }

    @Test
    public void testConstraintSuccessBoundaryEvent() throws ValidationException {
        verifyValidResult(createFile("EXT093_success_boundaryEvent.bpmn"));
    }

    @Test
    public void testConstraintSuccessEndEvent() throws ValidationException {
        verifyValidResult(createFile("EXT093_success_endEvent.bpmn"));
    }

    @Test
    public void testConstraintSuccessIntCatch() throws ValidationException {
        verifyValidResult(createFile("EXT093_success_intCatch.bpmn"));
    }

    @Test
    public void testConstraintSuccessIntThrow() throws ValidationException {
        verifyValidResult(createFile("EXT093_success_intThrow.bpmn"));
    }

    @Test
    public void testConstraintSuccessStartEvent() throws ValidationException {
        verifyValidResult(createFile("EXT093_success_startEvent.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "093";
    }
}
