package com.github.blutorange.bpmnspector_test.schematron;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector.api.Warning;
import com.github.blutorange.bpmnspector.schematron.SchematronBPMNValidator;
import com.github.blutorange.bpmnspector.validation.UnsortedValidationResult;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;

/**
 * Abstract test class for all tests of the BPMN Validator to simplify testing and reduce redundancy
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class TestCase {

    protected static SchematronBPMNValidator validator;

    @BeforeAll
    public static void setUp() throws ValidationException {
        validator = new SchematronBPMNValidator();
    }

    protected static String getTestFilePath() {
        return Paths.get(System.getProperty("user.dir"))
                .resolve("src/test/resources")
                .toString();
    }

    protected File createFile(String fileName) {
        var path = String.format(
                "%s%s%s%s%s", getTestFilePath(), File.separator, getExtNumber(), File.separator, fileName);
        return new File(path);
    }

    protected ValidationResultBuilder validate(File f) throws ValidationException {
        return validator.validate(f);
    }

    private ValidationResultBuilder validValidationResultForFile(String filename) {
        ValidationResultBuilder result = new UnsortedValidationResult();
        result.addFile(createFile(filename).toPath());
        return result;
    }

    protected void assertValidValidationResultForFile(String filename) throws ValidationException {
        ValidationResultBuilder expected = validValidationResultForFile(filename);
        ValidationResultBuilder result = validator.validate(createFile(filename));

        assertEquals(expected, result);
    }

    protected ValidationResultBuilder createValidationResultWithWarnings(String filename, List<Warning> warningList) {
        ValidationResultBuilder result = validValidationResultForFile(filename);
        warningList.forEach(result::addWarning);
        return result;
    }

    protected void verifyValidResult(File f) throws ValidationException {
        ValidationResultBuilder result = validate(f);
        assertTrue(result.isValid());
        assertTrue(result.getViolations().isEmpty());
    }

    protected ValidationResultBuilder verifyInvalidResult(File f, int violationsCount) throws ValidationException {
        ValidationResultBuilder result = validate(f);
        assertFalse(result.isValid());
        assertEquals(violationsCount, result.getViolations().size());
        return result;
    }

    protected void assertViolation(Violation v, String message, String fileName, String xpath, int line) {
        assertViolation(v, message, xpath, line);
        assertEquals(
                fileName,
                v.getLocation()
                        .getResource()
                        .getPath()
                        .orElseThrow()
                        .getFileName()
                        .toString());
    }

    protected void assertURLViolation(Violation v, String message, String url, String xpath, int line) {
        assertViolation(v, message, xpath, line);
        assertEquals(url, v.getLocation().getResource().getResourceName());
    }

    protected void assertViolation(Violation v, String message, String xpath, int line) {
        assertEquals(message.replaceAll("\\s+", " "), v.getMessage().replaceAll("\\s+", " "));
        assertEquals(xpath, v.getLocation().getXpath());
        assertEquals(line, v.getLocation().getLocation().getRow());
    }

    protected void assertViolation(Violation v, String xpath, int line) {
        assertViolation(v, getErrorMessage(), xpath, line);
    }

    protected String getErrorMessage() {
        throw new UnsupportedOperationException("must be overridden by every child class!");
    }

    protected String getExtNumber() {
        throw new UnsupportedOperationException("must be overridden by every child class!");
    }
}
