package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.068
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext068Test extends TestCase {

    private static final String ERR_MSG = "If a multiInstance task is used in an executable process "
            + "loopDataInputReference must be resolvable to a DataInput defined in the InputOutputSpecification of the Task.";

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT068_failure.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:task/bpmn:multiInstanceLoopCharacteristics",
                18);
    }

    @Override
    protected String getExtNumber() {
        return "068";
    }
}
