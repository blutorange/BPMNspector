package com.github.blutorange.bpmnspector_test.api.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.blutorange.bpmnspector.api.BPMNspector;
import com.github.blutorange.bpmnspector.api.Location;
import com.github.blutorange.bpmnspector.api.LocationCoordinate;
import com.github.blutorange.bpmnspector.api.SimpleValidationResult;
import com.github.blutorange.bpmnspector.api.UnsortedValidationResult;
import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector.api.Validator;
import com.github.blutorange.bpmnspector.api.Warning;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;

/** @author Matthias Geiger */
public class ValidationResultTest {

    private final Path path = Paths.get(System.getProperty("user.dir"))
            .resolve("src")
            .resolve("test")
            .resolve("resources")
            .resolve("036")
            .resolve("fail_call_choreography.bpmn");
    private final Validator validator;

    public ValidationResultTest() throws ValidationException {
        validator = new BPMNspector();
    }

    @Test
    public void testEmptyUnsortedValidationResult() {
        testEmptyValidationResult(new UnsortedValidationResult());
    }

    @Test
    public void testEmptySimpleValidationResult() {
        testEmptyValidationResult(new UnsortedValidationResult());
    }

    private void testEmptyValidationResult(ValidationResult result) {
        assertTrue(result.isValid());
        assertEquals(0, result.getViolations().size());
        assertEquals(0, result.getViolatedConstraints().size());
        assertEquals(0, result.getWarnings().size());
        assertEquals(0, result.getFoundFiles().size());
        assertEquals(0, result.getFilesWithViolations().size());
    }

    @Test
    public void testUnsortedValidationResult() throws ValidationException {
        ValidationResult result = validator.validate(path);
        testValidationResult(result);

        assertEquals("EXT.021", result.getViolations().get(2).getConstraint());
    }

    @Test
    public void testSimpleValidationResult() throws ValidationException {
        ValidationResult result = validator.validate(path);

        ValidationResult sorted = new SimpleValidationResult();
        sorted.addFile(result.getFoundFiles().get(0));

        result.getViolations().forEach(sorted::addViolation);

        assertEquals(result.getViolations().get(2), sorted.getViolations().get(0));
        testValidationResult(sorted);
    }

    private void testValidationResult(ValidationResult result) {
        assertFalse(result.isValid());

        assertEquals(1, result.getFoundFiles().size());
        assertEquals(result.getFoundFiles().get(0), path);

        assertEquals(1, result.getFilesWithViolations().size());
        assertEquals(path, result.getFilesWithViolations().get(0));

        assertEquals(5, result.getViolations().size());

        List<String> expectedConstraints = new LinkedList<>();
        expectedConstraints.add("EXT.021");
        expectedConstraints.add("EXT.022");
        expectedConstraints.add("EXT.036");
        expectedConstraints.add("REF_TYPE");

        assertEquals(result.getViolatedConstraints(), expectedConstraints);
    }

    @Test
    public void testSimpleValidationResultWithWarning() {
        ValidationResult result = new SimpleValidationResult();

        var warn1Msg = "1-sample warning";
        var warn2Msg = "2-sample warning";
        result.addWarning(new Warning(warn2Msg, new Location(Paths.get("dummy path"), LocationCoordinate.EMPTY)));
        result.addWarning(new Warning(warn1Msg, new Location(Paths.get("dummy path"), LocationCoordinate.EMPTY)));

        assertTrue(result.isValid());
        assertEquals(2, result.getWarnings().size());
        assertEquals(warn1Msg, result.getWarnings().get(0).getMessage());
        assertEquals(
                LocationCoordinate.EMPTY,
                result.getWarnings().get(0).getLocation().getLocation());
        assertEquals(warn2Msg, result.getWarnings().get(1).getMessage());
    }
}
