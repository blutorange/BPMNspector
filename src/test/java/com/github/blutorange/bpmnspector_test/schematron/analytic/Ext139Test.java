package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.139
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext139Test extends TestCase {

    private static final String ERR_MSG = "If Message Intermediate Events are used in the configuration, then Receive "
            + "Tasks MUST NOT be used in that configuration and vice versa.";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT139_failure.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:eventBasedGateway", 4);
    }

    @Override
    protected String getExtNumber() {
        return "139";
    }
}
