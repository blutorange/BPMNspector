package com.github.blutorange.bpmnspector_test.schematron;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import java.io.File;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing the resolution of paths
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class PathTest extends TestCase {

    @Test
    public void testConstraintSuccess1() throws ValidationException {
        var f = new File(getTestFilePath() + File.separator + "path"
                + File.separator + "folder" + File.separator
                + "success_import.bpmn");
        verifyValidResult(f);
    }

    @Test
    public void testConstraintSuccess2() throws ValidationException {
        verifyValidResult(createFile("success_import.bpmn"));
    }

    @Test
    public void testInvalidUrlImports() throws ValidationException {
        ValidationResult result = validate(createFile("import_URL.bpmn"));
        assertEquals(2, result.getViolations().size());
    }

    @Override
    protected String getExtNumber() {
        return "path";
    }
}
