package com.github.blutorange.bpmnspector_test.schematron.descriptive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.blutorange.bpmnspector.api.Location;
import com.github.blutorange.bpmnspector.api.LocationCoordinate;
import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector.api.Warning;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.076
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext076Test extends TestCase {

    @Test
    public void testConstraintFail1() throws ValidationException {
        List<Warning> expectedWarnings = new ArrayList<>();
        expectedWarnings.add(new Warning(
                getErrorMessage(),
                new Location(
                        createFile("Fail_1.bpmn").toPath(),
                        new LocationCoordinate(5, 79),
                        "/bpmn:definitions/bpmn:process/bpmn:dataObjectReference")));

        ValidationResult result = validator.validate(createFile("Fail_1.bpmn"));

        assertTrue(result.isValid());
        assertEquals(expectedWarnings, result.getWarnings());
    }

    @Test
    public void testConstraintFail2() throws ValidationException {
        List<Warning> expectedWarnings = new ArrayList<>();
        expectedWarnings.add(new Warning(
                getErrorMessage(),
                new Location(
                        createFile("Fail_2.bpmn").toPath(),
                        new LocationCoordinate(5, 88),
                        "/bpmn:definitions/bpmn:process/bpmn:dataObjectReference")));

        ValidationResult result = validator.validate(createFile("Fail_2.bpmn"));

        assertTrue(result.isValid());
        assertEquals(expectedWarnings, result.getWarnings());
    }

    @Test
    public void testConstraintFail3() throws ValidationException {
        List<Warning> expectedWarnings = new ArrayList<>();
        expectedWarnings.add(new Warning(
                getErrorMessage(),
                new Location(
                        createFile("Fail_3.bpmn").toPath(),
                        new LocationCoordinate(5, 74),
                        "/bpmn:definitions/bpmn:process/bpmn:dataObjectReference")));

        ValidationResult result = validator.validate(createFile("Fail_3.bpmn"));

        assertTrue(result.isValid());
        assertEquals(expectedWarnings, result.getWarnings());
    }

    @Test
    public void testConstraintSuccess() throws ValidationException {
        assertValidValidationResultForFile("Success.bpmn");
    }

    @Override
    protected String getErrorMessage() {
        return "Naming Convention: name = Data Object Name [Data Object Reference State]";
    }

    @Override
    protected String getExtNumber() {
        return "076";
    }
}
