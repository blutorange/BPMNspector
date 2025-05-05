package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import static java.lang.String.format;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.056
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext056Test extends TestCase {

    private static final String ERROR_MESSAGE_TARGET =
            "For a Process: Of the types of FlowNode, only Activities, Gateways, and Events can be the target. However, Activities that are Event SubProcesses are not allowed to be a target";
    private static final String ERROR_MESSAGE_CHOREOGRAPHY = "A SubProcess must not contain Choreography Activities";
    private static final String ERROR_MESSAGE_SOURCE =
            "For a Process: Of the types of FlowNode, only Activities, Gateways, and Events can be the source. However, Activities that are Event SubProcesses are not allowed to be a source";
    private static final String XPATH_STRING_TARGET = "/bpmn:definitions/bpmn:process/bpmn:%s/bpmn:%s";
    private static final String XPATH_STRING_SOURCE = "/bpmn:definitions/bpmn:process/bpmn:%s/bpmn:%s";

    @Test
    public void testConstraintCallChoreographyFail() throws ValidationException {
        assertTests("fail_call_choreography.bpmn", "subProcess", "callChoreography");
    }

    @Test
    public void testConstraintChoreographyTaskFail() throws ValidationException {
        assertTests("fail_choreography_task.bpmn", "subProcess", "choreographyTask");
    }

    @Test
    public void testConstraintChoreographyTaskTransactionFail() throws ValidationException {
        assertTests("fail_choreography_task_transaction.bpmn", "transaction", "choreographyTask");
    }

    @Test
    public void testConstraintSubChoreographyFail() throws ValidationException {
        assertTests("fail_sub_choreography.bpmn", "subProcess", "subChoreography");
    }

    private void assertTests(String fileName, String processType, String choreographyType) throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile(fileName), 3);
        assertViolation(
                result.getViolations().get(0),
                ERROR_MESSAGE_SOURCE,
                format(XPATH_STRING_SOURCE, processType, choreographyType),
                11);
        assertViolation(
                result.getViolations().get(1),
                ERROR_MESSAGE_TARGET,
                format(XPATH_STRING_TARGET, processType, choreographyType),
                11);
        assertViolation(
                result.getViolations().get(2),
                ERROR_MESSAGE_CHOREOGRAPHY,
                format("/bpmn:definitions/bpmn:process/bpmn:%s", processType),
                4);
    }

    @Override
    protected String getExtNumber() {
        return "056";
    }
}
