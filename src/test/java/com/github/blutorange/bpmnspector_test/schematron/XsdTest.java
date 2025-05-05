package com.github.blutorange.bpmnspector_test.schematron;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector.api.Violation;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing the XSD validation
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class XsdTest extends TestCase {

    @Test
    public void testXsdFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("xsdfail.bpmn"), 1);
        Violation v = result.getViolations().get(0);
        assertEquals(
                "xsdfail.bpmn",
                v.getLocation().getResource().getPath().get().getFileName().toString());
        assertEquals(6, v.getLocation().getLocation().getRow());
        assertTrue(v.getMessage().contains("cvc-complex-type.2.4.a:"));
        assertTrue(v.getMessage().contains("outgoing"));
        assertEquals("", v.getLocation().getXpath().orElse(""));
        assertEquals("XSD-Check", v.getConstraint());
    }

    @Override
    protected String getExtNumber() {
        return "xsd";
    }
}
