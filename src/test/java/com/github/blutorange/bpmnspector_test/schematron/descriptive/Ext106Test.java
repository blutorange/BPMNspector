package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.106
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext106Test extends TestCase {

    @Test
    public void testConstraintEventFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_cancel_end_event.bpmn"), 1);
        assertViolation(result.getViolations().get(0), "/bpmn:definitions/bpmn:process/bpmn:endEvent", 7);
    }

    @Test
    public void testConstraintEventRefFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_sub_process.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0), "/bpmn:definitions/bpmn:process/bpmn:subProcess/bpmn:endEvent[2]", 22);
    }

    @Test
    public void testConstraintCancelEventSuccess() throws ValidationException {
        verifyValidResult(createFile("success_cancel_event.bpmn"));
    }

    @Test
    public void testConstraintCancelBoundaryEventSuccess() throws ValidationException {
        verifyValidResult(createFile("success_cancel_boundary_event.bpmn"));
    }

    @Override
    protected String getErrorMessage() {
        return "A cancel EndEvent is only allowed in a transaction sub-process";
    }

    @Override
    protected String getExtNumber() {
        return "106";
    }
}
