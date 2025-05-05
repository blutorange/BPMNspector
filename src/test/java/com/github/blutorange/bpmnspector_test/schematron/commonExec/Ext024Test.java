package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.024
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext024Test extends TestCase {

    private static final String ERR_MSG =
            "The optional attribute isImmediate must not be 'false' for executable processes.";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT024_failure.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "(//bpmn:sequenceFlow[@isImmediate='false'])[1]", 10);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT024_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "024";
    }
}
