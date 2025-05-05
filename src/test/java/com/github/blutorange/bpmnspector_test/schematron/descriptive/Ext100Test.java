package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.100
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext100Test extends TestCase {

    @Test
    public void testConstraintEventFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_event.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0), "/bpmn:definitions/bpmn:process/bpmn:subProcess/bpmn:startEvent", 10);
    }

    @Test
    public void testConstraintTransactionEventFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_event_transaction.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0), "/bpmn:definitions/bpmn:process/bpmn:transaction/bpmn:startEvent", 10);
    }

    @Test
    public void testConstraintEventRefFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_event_ref.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0), "/bpmn:definitions/bpmn:process/bpmn:subProcess/bpmn:startEvent", 11);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("success.bpmn"));
    }

    @Test
    public void testConstraintEventSubProcessSuccess() throws ValidationException {
        verifyValidResult(createFile("success_event_sub.bpmn"));
    }

    @Override
    protected String getErrorMessage() {
        return "No EventDefinition is allowed for Start Events in Sub-Process definitions";
    }

    @Override
    protected String getExtNumber() {
        return "100";
    }
}
