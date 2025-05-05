package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.065
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext065Test extends TestCase {

    private static final String ERR_MSG =
            "An InputOutputBinding element must correctly bind one Input and one Output of"
                    + " the InputOutputSpecification to an Operation of a Service Interface.";

    @Test
    public void testConstraintFailCalledProcess() throws ValidationException {
        ValidationResultBuilder result =
                verifyInvalidResult(createFile("EXT065_failure_calledProcess_wrongInputData.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process", 19);
    }

    @Test
    public void testConstraintFailCalledGlobalTask() throws ValidationException {
        ValidationResultBuilder result =
                verifyInvalidResult(createFile("EXT065_failure_calledGlobalTask_wrongOutputData.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:globalTask", 14);
    }

    @Override
    protected String getExtNumber() {
        return "065";
    }
}
