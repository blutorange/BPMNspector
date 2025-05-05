package com.github.blutorange.bpmnspector_test.schematron;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector.api.Violation;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing the WSDL Validation
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class WsdlTest extends TestCase {

    @Test
    public void testConstraintImportedWsdlFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("wsdl-fail.bpmn"), 4);
        Violation v = result.getViolations().get(0);
        assertTrue(v.getMessage().contains("cvc-complex-type.2.4.c:"));
        assertTrue(v.getMessage().contains("xs:schema"));
        assertViolation(v, 22);
        v = result.getViolations().get(1);
        assertTrue(v.getMessage().contains("cvc-complex-type.2.4.a:"));
        assertTrue(v.getMessage().contains("DAMAGEDinterface"));
        assertViolation(v, 40);
        v = result.getViolations().get(2);
        assertTrue(v.getMessage().contains("cvc-complex-type.3.2.2:"));
        assertTrue(v.getMessage().contains("DAMinterface"));
        assertTrue(v.getMessage().contains("service"));
        assertViolation(v, 72);
        v = result.getViolations().get(3);
        assertTrue(v.getMessage().contains("cvc-complex-type.4:"));
        assertTrue(v.getMessage().contains("interface"));
        assertTrue(v.getMessage().contains("service"));
        assertViolation(v, 72);
    }

    @Test
    public void testConstraintImportedWsdlSuccess() throws ValidationException {
        verifyValidResult(createFile("wsdl-success.bpmn"));
    }

    private void assertViolation(Violation v, int line) {
        assertEquals("undef", v.getLocation().getXpath().orElse("undef"));
        assertEquals(
                "wsdl2primer-fail.wsdl",
                v.getLocation().getResource().getPath().get().getFileName().toString());
        assertEquals(line, v.getLocation().getLocation().getRow());
    }

    @Override
    protected String getExtNumber() {
        return "wsdl";
    }
}
