package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.122
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext122Test extends TestCase {

    private static final String ERR_MSG =
            "A intermediateCatchEvent in normal flow must not contain a compensateEventDefinition.";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT122_failure.bpmn"), 2);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:intermediateCatchEvent",
                7);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT122_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "122";
    }
}
