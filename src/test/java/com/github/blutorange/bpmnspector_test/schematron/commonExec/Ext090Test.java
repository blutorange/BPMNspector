package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.090
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext090Test extends TestCase {

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT090_failure.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                "A whileExecutingOutputRef must be listed as dataOutputRef.",
                "/bpmn:definitions/bpmn:process/bpmn:ioSpecification/bpmn:outputSet[1]",
                11);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT090_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "090";
    }
}
