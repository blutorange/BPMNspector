package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.069b
 *
 * @version 1.0
 */
public class Ext069bTest extends TestCase {

    private static final String ERR_MSG =
            "Type of DataObject must be the scalar of the loopDataOutput type for SubProcess.";

    @Test
    public void testConstraintFailIsCollection() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT069b_failure_isCollection.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:subProcess/bpmn:multiInstanceLoopCharacteristics",
                14);
    }

    @Test
    public void testConstraintFailUnequalStructureRef() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT069b_failure_unequalStructureRef.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:subProcess/bpmn:multiInstanceLoopCharacteristics",
                14);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT069b_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "069b";
    }
}
