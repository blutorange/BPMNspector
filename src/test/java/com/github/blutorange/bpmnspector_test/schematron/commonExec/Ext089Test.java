package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.089
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext089Test extends TestCase {

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT089_failure.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                "An optionalOutputRef must be listed as dataOutputRef.",
                "(//bpmn:outputSet[bpmn:optionalOutputRefs])[1]",
                11);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT089_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "089";
    }
}
