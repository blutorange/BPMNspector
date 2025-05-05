package com.github.blutorange.bpmnspector_test.schematron.full;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.030
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext030Test extends TestCase {

    private static final String ERR_MSG =
            "The value of maximum MUST be one or greater, AND MUST be equal or greater than the minimum value.";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT030_failure.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:collaboration/bpmn:participant[2]/bpmn:participantMultiplicity",
                8);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT030_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "030";
    }
}
