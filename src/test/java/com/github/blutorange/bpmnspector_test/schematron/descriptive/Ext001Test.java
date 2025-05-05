package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.001
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext001Test extends TestCase {

    @Test
    public void testConstraintFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("Fail.bpmn"), 1);
        assertViolation(result.getViolations().get(0));
    }

    @Test
    public void testConstraintFail2() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("Fail2.bpmn"), 1);
        assertViolation(result.getViolations().get(0));
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("Success.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "001";
    }

    private void assertViolation(Violation v) {
        assertTrue(v.getMessage().contains("Import could not be resolved: "));
        assertTrue(v.getMessage().contains("nofile.bpmn"));
        assertEquals(3, v.getLocation().getLocation().getRow());
        assertEquals("EXT.001", v.getConstraint());
    }
}
