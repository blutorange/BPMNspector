package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.092
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext092Test extends TestCase {

    private static final String ERR_MSG =
            "If a DataAssociation does not define a Transformation, only a single sourceRef is allowed.";

    @Test
    public void testConstraintFailDifferentStructureRef() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT092_failure_twoSourceRefs.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:task/bpmn:dataInputAssociation",
                13);
    }

    @Test
    public void testConstraintSuccessWithTransformation() throws ValidationException {
        verifyValidResult(createFile("EXT092_success_withTransformation.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "092";
    }
}
