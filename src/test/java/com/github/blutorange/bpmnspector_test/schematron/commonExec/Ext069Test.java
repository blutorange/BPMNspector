package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.069
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext069Test extends TestCase {

    private static final String ERR_MSG = "Type of DataOutput must be the scalar of the loopDataOutput type.";

    @Test
    public void testConstraintFailIsCollection() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT069_failure_isCollection.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:task/bpmn:multiInstanceLoopCharacteristics",
                19);
    }

    @Test
    public void testConstraintFailUnequalStructureRef() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT069_failure_unequalStructureRef.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:task/bpmn:multiInstanceLoopCharacteristics",
                19);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT069_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "069";
    }
}
