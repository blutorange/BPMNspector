package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.087
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext087Test extends TestCase {

    private static final String ERR_MSG = "If an inputSet references an outputSet using the outputSetRefs element it "
            + "must be referenced by the outputSet using the inputSetRefs element and vice versa.";

    @Test
    public void testConstraintFailInvalidReferencing() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT087_failure_invalidReferencing.bpmn"), 4);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:ioSpecification/bpmn:inputSet[1]",
                8);
    }

    @Test
    public void testConstraintFailMissingInputSetRef() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT087_failure_missingInputSetRef.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:ioSpecification/bpmn:inputSet",
                7);
    }

    @Test
    public void testConstraintFailMissingOutputSetRef() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT087_failure_missingOutputSetRef.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:ioSpecification/bpmn:outputSet",
                10);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT087_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "087";
    }
}
