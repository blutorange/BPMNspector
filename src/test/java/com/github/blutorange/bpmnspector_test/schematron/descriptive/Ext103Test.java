package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.103
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext103Test extends TestCase {

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("Fail.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                "If a Start Event is target of a MessageFlow definition, at least one messageEventDefinition must be present",
                "(//bpmn:startEvent[@id = //bpmn:messageFlow/@targetRef])[1]",
                13);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("Success.bpmn"));
    }

    @Test
    public void testConstraintRefSuccess() throws ValidationException {
        verifyValidResult(createFile("Success_ref.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "103";
    }
}
