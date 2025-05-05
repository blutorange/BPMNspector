package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.086
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext086Test extends TestCase {

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT086_failure.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                "A whileExecutingInputRef must be listed as dataInputRef.",
                "/bpmn:definitions/bpmn:process/bpmn:ioSpecification/bpmn:inputSet[1]",
                7);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT086_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "086";
    }
}
