package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.061
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext061Test extends TestCase {

    private static final String ERR_MSG = "At least one Activity must be contained in an AdHocSubProcess.";

    @Test
    public void testConstraintFailEmptyAdHoc() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT061_fail_empty_AdHoc.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:adHocSubProcess", 7);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT061_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "061";
    }
}
