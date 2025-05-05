package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.028
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext028Test extends TestCase {

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("Fail.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                "A Sequence Flow must not cross the border of a Pool",
                "/bpmn:definitions/bpmn:process[1]/bpmn:sequenceFlow[1]",
                16);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("Success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "028";
    }
}
