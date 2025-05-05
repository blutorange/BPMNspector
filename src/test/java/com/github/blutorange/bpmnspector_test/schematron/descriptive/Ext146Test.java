package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.146
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext146Test extends TestCase {

    private static final String ERRORMESSAGE =
            "Only messageEventDefinitions, escalationEventDefinitions, errorEventDefinitions, cancelEventDefinitions, compensationEventDefinitions, signalEventDefinitions and terminateEventDefinitions are allowed for end events";
    private static final String XPATHSTRING = "/bpmn:definitions/bpmn:process/bpmn:endEvent";

    @Test
    public void testConstraintLinkFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_link.bpmn"), 1);
        assertViolation(result.getViolations().get(0), 7);
    }

    @Test
    public void testConstraintTimerFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_timer.bpmn"), 1);
        assertViolation(result.getViolations().get(0), 7);
    }

    @Test
    public void testConstraintTimerRefFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_timer_ref.bpmn"), 1);
        assertViolation(result.getViolations().get(0), 8);
    }

    @Test
    public void testConstraintMultipleFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_multiple.bpmn"), 1);
        assertViolation(result.getViolations().get(0), 7);
    }

    @Test
    public void testConstraintConditionalFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_conditional.bpmn"), 1);
        assertViolation(result.getViolations().get(0), 7);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("success.bpmn"));
    }

    @Test
    public void testConstraintMultipleSuccess() throws ValidationException {
        verifyValidResult(createFile("success_multiple.bpmn"));
    }

    private void assertViolation(Violation v, int line) {
        assertViolation(v, XPATHSTRING, line);
    }

    @Override
    protected String getErrorMessage() {
        return ERRORMESSAGE;
    }

    @Override
    protected String getExtNumber() {
        return "146";
    }
}
