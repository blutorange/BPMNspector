package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.002
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext002Test extends TestCase {

    private static final String ERRORMESSAGE = "Resources have id duplicates";
    private static final String XPATHSTRING =
            "/*[local-name() = 'definitions' and namespace-uri() = 'http://www.omg.org/spec/BPMN/20100524/MODEL']/@id";

    @Test
    public void testConstraintImport1Fail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_import.bpmn"), 10);
        assertViolation(result.getViolations().get(0), "fail_import.bpmn", 2);
        assertViolation(result.getViolations().get(1), "import.bpmn", 2);
    }

    @Test
    public void testConstraintImport2Fail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_import2.bpmn"), 10);
        assertViolation(result.getViolations().get(0), "import.bpmn", 2);
        assertViolation(result.getViolations().get(1), "import2.bpmn", 2);
    }

    @Test
    public void testConstraintImport3Fail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_import3.bpmn"), 20);
        assertViolation(result.getViolations().get(0), "fail_import3.bpmn", 2);
        assertViolation(result.getViolations().get(1), "fail_import2.bpmn", 2);
    }

    @Test
    public void testConstraintImportUrlFail() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("fail_import_URL.bpmn"), 10);
        assertViolation(result.getViolations().get(0), "fail_import_URL.bpmn", 2);
        assertURLViolation(
                result.getViolations().get(1), ERRORMESSAGE, "http://www.bpmnspector.org/import.bpmn", XPATHSTRING, 2);
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        verifyValidResult(createFile("success_import.bpmn"));
    }

    @Test
    public void testConstraintSuccessUrl() throws ValidationException {
        verifyValidResult(createFile("success_import_URL.bpmn"));
    }

    @Override
    protected void assertViolation(Violation v, String fileName, int line) {
        assertViolation(v, ERRORMESSAGE, fileName, XPATHSTRING, line);
    }

    @Override
    protected String getExtNumber() {
        return "002";
    }
}
