package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.064
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext064Test extends TestCase {

    private static final String ERR_MSG =
            "At least one InputOutputBinding must be defined as the Callable Element is exposed as a Service.";

    @Test
    public void testConstraintFailCalledProcess() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT064_failure_calledProcess.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process[1]", 21);
    }

    @Test
    public void testConstraintFailCalledGlobalTask() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT064_failure_calledGlobalTask.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:globalTask", 54);
    }

    @Test
    public void testConstraintSuccessCalledProcess() throws ValidationException {
        verifyValidResult(createFile("EXT064_success_calledProcess.bpmn"));
    }

    @Test
    public void testConstraintSuccessCalledGlobalTask() throws ValidationException {
        verifyValidResult(createFile("EXT064_success_calledGlobalTask.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "064";
    }
}
