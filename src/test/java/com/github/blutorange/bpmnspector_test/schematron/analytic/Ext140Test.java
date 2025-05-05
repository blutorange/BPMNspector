package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.140
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext140Test extends TestCase {

    private static final String ERR_MSG =
            "Receive Tasks used in an Event Gateway configuration MUST NOT have any " + "attached Intermediate Events.";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT140_failure.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "(//bpmn:receiveTask[@id=//bpmn:sequenceFlow[@id=//bpmn:eventBasedGateway/bpmn:outgoing]/@targetRef])[1]",
                19);
    }

    @Override
    protected String getExtNumber() {
        return "140";
    }
}
