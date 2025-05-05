package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.088
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext088Test extends TestCase {

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                "A DataOutput must be referenced by at least one OutputSet",
                "(//bpmn:dataOutput)[1]",
                5);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "088";
    }
}
