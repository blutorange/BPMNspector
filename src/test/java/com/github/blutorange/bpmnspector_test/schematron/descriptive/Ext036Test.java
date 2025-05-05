package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.036
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext036Test extends TestCase {

    private static final String ERROR_MESSAGE_SOURCE =
            "For a Process: Of the types of FlowNode, only Activities, Gateways, and Events can be the source. However, Activities that are Event SubProcesses are not allowed to be a source";
    private static final String ERROR_MESSAGE_TARGET =
            "For a Process: Of the types of FlowNode, only Activities, Gateways, and Events can be the target. However, Activities that are Event SubProcesses are not allowed to be a target";

    @Test
    public void testConstraintCallChoreographyFail() throws ValidationException {
        assertTests("fail_call_choreography.bpmn", "callChoreography");
    }

    @Test
    public void testConstraintChoreographyTaskFail() throws ValidationException {
        assertTests("fail_choreography_task.bpmn", "choreographyTask");
    }

    @Test
    public void testConstraintSubChoreographyFail() throws ValidationException {
        assertTests("fail_sub_choreography.bpmn", "subChoreography");
    }

    private void assertTests(String fileName, String type) throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile(fileName), 3);
        assertViolation(
                result.getViolations().get(0), ERROR_MESSAGE_SOURCE, "/bpmn:definitions/bpmn:process/bpmn:" + type, 10);
        assertViolation(
                result.getViolations().get(1), ERROR_MESSAGE_TARGET, "/bpmn:definitions/bpmn:process/bpmn:" + type, 10);
        assertViolation(
                result.getViolations().get(2),
                "A Process must not contain Choreography Activities",
                "/bpmn:definitions/bpmn:process",
                3);
    }

    @Override
    protected String getExtNumber() {
        return "036";
    }
}
