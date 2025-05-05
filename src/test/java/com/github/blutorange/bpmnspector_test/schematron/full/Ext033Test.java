package com.github.blutorange.bpmnspector_test.schematron.full;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.033
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext033Test extends TestCase {

    private static final String ERR_MSG = "If a collaboration references a Choreography the value of the attribute "
            + "isClosed must be equal for both elements.";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT033_failure.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "(//bpmn:collaboration[bpmn:choreographyRef])[1]", 29);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT033_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "033";
    }
}
