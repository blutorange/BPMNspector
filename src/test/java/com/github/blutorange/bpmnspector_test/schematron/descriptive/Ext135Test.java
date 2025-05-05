package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.135
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext135Test extends TestCase {

    private static final String ERRORMESSAGE =
            "A Gateway MUST have either multiple incoming Sequence Flows or multiple outgoing Sequence Flows";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail.bpmn"), 2);
        assertViolation(result.getViolations().get(0), "(//bpmn:parallelGateway)[1]", 10);
        assertViolation(result.getViolations().get(1), "(//bpmn:parallelGateway)[2]", 20);
    }

    @Test
    public void testConstraintSubFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_no_connection.bpmn"), 1);
        assertViolation(result.getViolations().get(0), "(//bpmn:parallelGateway)[1]", 4);
    }

    @Test
    public void testConstraintEXSubFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_ex_no_connection.bpmn"), 1);
        assertViolation(result.getViolations().get(0), "(//bpmn:exclusiveGateway)[1]", 4);
    }

    @Test
    public void testConstraintBothMultipleSuccess() throws ValidationException {
        verifyValidResult(createFile("success_multiple_in_and_out.bpmn"));
    }

    @Test
    public void testConstraintOutMultipleSuccess() throws ValidationException {
        verifyValidResult(createFile("success_multiple_out.bpmn"));
    }

    @Override
    protected String getErrorMessage() {
        return ERRORMESSAGE;
    }

    @Override
    protected String getExtNumber() {
        return "135";
    }
}
