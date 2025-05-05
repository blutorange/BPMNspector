package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.058
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext058Test extends TestCase {

    private static final String ERR_MSG = "An Event Sub-Process MUST have exactly one Start Event.";

    @Test
    public void testConstraintFailTwoStartEvents() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT058_fail_twoStartEvents.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "(//bpmn:subProcess[@triggeredByEvent='true'])[1]", 27);
    }

    @Test
    public void testConstraintFailNoStartEvent() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT058_fail_noStartEvent.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "(//bpmn:subProcess[@triggeredByEvent='true'])[1]", 27);
    }

    @Override
    protected String getExtNumber() {
        return "058";
    }
}
