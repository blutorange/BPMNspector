package com.github.blutorange.bpmnspector_test.schematron.full;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.027
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext027Test extends TestCase {

    private static final String ERR_MSG = "A choreography or a GlobalConversation must not reference a choreography.";

    @Test
    public void testConstraintFailChoreography() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT027_failure_choreography.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:choreography[1]", 3);
    }

    @Test
    public void testConstraintFailGlobalConversation() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT027_failure_globalConversation.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:globalConversation", 14);
    }

    @Test
    public void testConstraintSuccessChorNoRef() throws ValidationException {
        verifyValidResult(createFile("EXT027_success_chorNoRef.bpmn"));
    }

    @Test
    public void testConstraintSuccessGlobalConversation() throws ValidationException {
        verifyValidResult(createFile("EXT027_success_globalConversation.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "027";
    }
}
