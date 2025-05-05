package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.123
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext123Test extends TestCase {

    private static final String ERR_MSG = "For executable Processes (isExecutable = true), if the trigger is "
            + "Conditional, then a FormalExpression MUST be entered.";

    @Test
    public void testConstraintFailEmptyCondition() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT123_failure_emptyCondition.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:intermediateCatchEvent/bpmn:conditionalEventDefinition",
                7);
    }

    @Test
    public void testConstraintFailNoFormalCondition() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT123_failure_noFormalCondition.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:intermediateCatchEvent/bpmn:conditionalEventDefinition",
                7);
    }

    @Test
    public void testConstraintSuccessFormalCondition() throws ValidationException {
        verifyValidResult(createFile("EXT123_success_formalCondition.bpmn"));
    }

    @Test
    public void testConstraintSuccessNotExecutable() throws ValidationException {
        verifyValidResult(createFile("EXT123_success_notExecutable.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "123";
    }
}
