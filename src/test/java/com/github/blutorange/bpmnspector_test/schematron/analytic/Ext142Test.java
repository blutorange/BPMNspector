package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.142
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext142Test extends TestCase {

    private static final String ERR_MSG = "When an EventBasedGateway is used to instantiate a process instance no "
            + "Incoming Sequence Flow is allowed.";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT142_failure.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0), ERR_MSG, "(//bpmn:eventBasedGateway[@instantiate='true'])[1]", 4);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT142_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "142";
    }
}
