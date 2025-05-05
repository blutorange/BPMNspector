package com.github.blutorange.bpmnspector_test.schematron.analytic;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.124
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext124Test extends TestCase {

    private static final String ERR_MSG =
            "A LinkEventDefinition in a Catch Event must have at least one source Element.";

    @Test
    public void testConstraintFailNoSource() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT124_failure_noSource.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "(//bpmn:linkEventDefinition[parent::bpmn:intermediateCatchEvent])[1]",
                9);
    }

    @Override
    protected String getExtNumber() {
        return "124";
    }
}
