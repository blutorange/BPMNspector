package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.118
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext118Test extends TestCase {

    private static final String ERR_MSG =
            "For each source Link there must exist a corresponding target. There may be multiple sources for one target.";

    @Test
    public void testConstraintFailNoTarget() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("EXT118_failure_wrongTarget.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:intermediateThrowEvent[2]/bpmn:linkEventDefinition",
                19);
    }

    @Test
    public void testConstraintSuccessMultipleSources() throws ValidationException {
        verifyValidResult(createFile("EXT118_success_multipleSources.bpmn"));
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("EXT118_success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "118";
    }
}
