package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.151
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext151Test extends TestCase {

    private static final String ERROR_MESSAGE =
            "If end events are used, all flow nodes must have an outgoing sequence flow";

    @Test
    public void testConstraintNormalSequenceFlowFail1() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_normal_sequence_flow_missing_1.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERROR_MESSAGE,
                "(//bpmn:subProcess[@isForCompensation = 'false' and @triggeredByEvent = 'false'] [parent::*/bpmn:endEvent])[1]",
                10);
    }

    @Test
    public void testConstraintNormalSequenceFlowFail2() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_normal_sequence_flow_missing_2.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERROR_MESSAGE,
                "(//bpmn:task[@isForCompensation = 'false'] [parent::*/bpmn:endEvent])[4]",
                55);
    }

    @Test
    public void testConstraintSequenceFlowInSubProcessFail1() throws ValidationException {
        ValidationResult result =
                verifyInvalidResult(createFile("fail_sequence_flow_in_sub_process_missing_1.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERROR_MESSAGE,
                "(//bpmn:task[@isForCompensation = 'false'] [parent::*/bpmn:endEvent])[1]",
                17);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("success.bpmn"));
    }

    @Test
    public void testConstraintSuccess2() throws ValidationException {
        verifyValidResult(createFile("success_2.bpmn"));
    }

    @Test
    public void testConstraintLinkEventSuccess() throws ValidationException {
        verifyValidResult(createFile("success_linkevent.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "151";
    }
}
