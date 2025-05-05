package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.096
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext096Test extends TestCase {

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("Fail.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                "A Start Event must not have an incoming sequence flow",
                "(//bpmn:startEvent)[1]",
                4);
    }

    @Override
    protected String getExtNumber() {
        return "096";
    }
}
