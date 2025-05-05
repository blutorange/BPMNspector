package com.github.blutorange.bpmnspector_test.schematron.full;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.029
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext029Test extends TestCase {

    private static final String ERR_MSG =
            "The value of the minimum attribute of the participant multiplicity must not be negative.";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT029_failure.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:collaboration/bpmn:participant[2]/bpmn:participantMultiplicity",
                8);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT029_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "029";
    }
}
