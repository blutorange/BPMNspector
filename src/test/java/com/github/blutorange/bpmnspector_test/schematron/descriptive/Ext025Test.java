package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.025
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext025Test extends TestCase {

    private static final String XPATHSTRING = "/bpmn:definitions/bpmn:process/bpmn:sequenceFlow[2]";

    @Test
    public void testConstraintNoIncomingFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("fail.bpmn"), 1);
        assertViolation(result.getViolations().get(0));
    }

    @Test
    public void testConstraintNoIncomingFail2() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("fail_2.bpmn"), 1);
        assertViolation(result.getViolations().get(0));
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("success.bpmn"));
    }

    @Test
    public void testConstraintSuccess2() throws ValidationException {
        verifyValidResult(createFile("success_2.bpmn"));
    }

    @Test
    public void testConstraintSuccessNoCondition() throws ValidationException {
        verifyValidResult(createFile("success_no_condition.bpmn"));
    }

    private void assertViolation(Violation v) {
        assertViolation(v, XPATHSTRING, 19);
    }

    @Override
    protected String getErrorMessage() {
        return "An Activity must not have only one outgoing conditional sequence flow if conditionExpression is present";
    }

    @Override
    protected String getExtNumber() {
        return "025";
    }
}
