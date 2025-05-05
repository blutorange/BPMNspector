package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.137
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext137Test extends TestCase {

    private static final String ERR_MSG =
            "The outgoing Sequence Flows of the Event Gateway MUST NOT have a conditionExpression.";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT137_failure.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:sequenceFlow[2]", 18);
    }

    @Override
    protected String getExtNumber() {
        return "137";
    }
}
