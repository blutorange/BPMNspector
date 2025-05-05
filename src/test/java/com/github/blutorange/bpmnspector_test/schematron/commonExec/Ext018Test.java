package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.018
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext018Test extends TestCase {

    private static final String ERR_MSG = "A diverging Gateway must not have more than one incoming Sequence Flow.";

    @Test
    public void testConstraintFailExclusiveGateway() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT018_failure_exclusiveGateway.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:exclusiveGateway[2]", 13);
    }

    @Test
    public void testConstraintFailParallelGateway() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT018_failure_parallelGateway.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:parallelGateway[2]", 13);
    }

    @Override
    protected String getExtNumber() {
        return "018";
    }
}
