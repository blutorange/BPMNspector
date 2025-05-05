package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.046
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext046Test extends TestCase {

    private static final String ERR_MSG =
            "If a SendTask references a message, at most one InputSet must be defined in " + "the ioSpecification.";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT046_failure.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:sendTask", 14);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT046_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "046";
    }
}
