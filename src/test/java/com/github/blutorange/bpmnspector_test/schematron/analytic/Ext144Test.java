package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.144
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext144Test extends TestCase {

    private static final String ERR_MSG = "The associated Activity must be a Task or a Sub-Process which is marked for "
            + "compensation (i.e., isForCompensation=true)";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT144_failure.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:boundaryEvent", 5);
    }

    @Override
    protected String getExtNumber() {
        return "144";
    }
}
