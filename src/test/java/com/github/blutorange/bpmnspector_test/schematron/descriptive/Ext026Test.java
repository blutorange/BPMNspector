package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.026
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext026Test extends TestCase {

    @Test
    public void testConstraintActivityFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_activity.bpmn"), 1);
        assertViolation(result.getViolations().get(0), "(//bpmn:task[@default])[1]");
    }

    @Test
    public void testConstraintGatewayFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_gateway.bpmn"), 1);
        assertViolation(result.getViolations().get(0), "(//bpmn:exclusiveGateway[@default])[1]");
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("success.bpmn"));
    }

    private void assertViolation(Violation v, String xpath) {
        assertViolation(v, xpath, 11);
    }

    @Override
    protected String getErrorMessage() {
        return "If an activity or gateway references a sequenceFlow as default flow - the referenced sequence flow must reference the activity/the gateway as sourceRef";
    }

    @Override
    protected String getExtNumber() {
        return "026";
    }
}
