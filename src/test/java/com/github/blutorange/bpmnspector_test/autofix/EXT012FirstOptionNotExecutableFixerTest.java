package com.github.blutorange.bpmnspector_test.autofix;

import static org.junit.jupiter.api.Assertions.*;

import com.github.blutorange.bpmnspector.api.Location;
import com.github.blutorange.bpmnspector.api.LocationCoordinate;
import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector.autofix.EXT012FirstOptionNotExecutableFixer;
import com.github.blutorange.bpmnspector.autofix.FixReport;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EXT012FirstOptionNotExecutableFixerTest {
    private static Document docToFix;
    private static Document fixedVersionOfDoc;
    private static Violation testViolation;

    private List<Violation> violationList;

    private Document clonedDoc;

    @BeforeAll
    public static void loadDocToFix() throws JDOMException, IOException {
        docToFix = DocHandlingHelper.loadResourceAsDoc("012/EXT012_failure_sequenceFlow.bpmn");
        fixedVersionOfDoc = DocHandlingHelper.loadResourceAsDoc("012/EXT012_failure_sequenceFlow_manualFix.bpmn");
        testViolation = new Violation(
                new Location(
                        Paths.get(docToFix.getBaseURI().replace("file:/", "")),
                        new LocationCoordinate(11, 7),
                        "(//bpmn:sequenceFlow[bpmn:conditionExpression and ancestor::bpmn:process[@isExecutable='true']])[1]"),
                "msg",
                "EXT.012");
    }

    @BeforeEach
    public void setUp() {
        clonedDoc = docToFix.clone();
    }

    @Test
    public void emptyViolationListDoesNotChangeDocument() {
        violationList = Collections.emptyList();
        EXT012FirstOptionNotExecutableFixer fixer = new EXT012FirstOptionNotExecutableFixer();

        FixReport report = fixer.fixIssues(clonedDoc, violationList);

        DocHandlingHelper.assertEqualDocumentSerialization(docToFix, clonedDoc);
        assertFalse(report.violationsHaveBeenFixed());
    }

    @Test
    public void otherConstraintViolationShouldNotBeFixed() {
        Violation otherViolation = new Violation(
                new Location(Paths.get("empty"), LocationCoordinate.EMPTY), "Should not be used", "OTHER");
        violationList = Collections.singletonList(otherViolation);
        EXT012FirstOptionNotExecutableFixer fixer = new EXT012FirstOptionNotExecutableFixer();

        FixReport report = fixer.fixIssues(clonedDoc, violationList);

        DocHandlingHelper.assertEqualDocumentSerialization(docToFix, clonedDoc);
        assertFalse(report.violationsHaveBeenFixed());
    }

    @Test
    public void fixingAutoFixViolationChangesProcessExecutabilityToFalse() {
        violationList = Collections.singletonList(testViolation);
        EXT012FirstOptionNotExecutableFixer fixer = new EXT012FirstOptionNotExecutableFixer();

        FixReport report = fixer.fixIssues(clonedDoc, violationList);

        DocHandlingHelper.assertEqualDocumentSerialization(fixedVersionOfDoc, clonedDoc);
        assertTrue(report.violationsHaveBeenFixed());
        assertEquals(violationList, report.getFixedViolations());
    }
}
