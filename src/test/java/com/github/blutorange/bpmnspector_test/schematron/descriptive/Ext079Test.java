package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.079
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext079Test extends TestCase {

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                "InputOutputSpecifications are not allowed in SubProcesses",
                "/bpmn:definitions/bpmn:process/bpmn:subProcess",
                7);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "079";
    }
}
