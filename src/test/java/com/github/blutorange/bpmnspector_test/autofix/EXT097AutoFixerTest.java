package com.github.blutorange.bpmnspector_test.autofix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.blutorange.bpmnspector.api.Location;
import com.github.blutorange.bpmnspector.api.LocationCoordinate;
import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector.autofix.EXT097AutoFixer;
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

public class EXT097AutoFixerTest {

    private static Document docToFix;
    private static Violation testViolation;
    private static EXT097AutoFixer fixer;

    private List<Violation> violationList;

    private Document clonedDoc;

    @BeforeAll
    public static void loadDocToFix() throws JDOMException, IOException {
        docToFix = DocHandlingHelper.loadResourceAsDoc("097/multiple_tasks_with_no_start.bpmn");
        testViolation = new Violation(
                new Location(
                        Paths.get(docToFix.getBaseURI().replace("file:/", "")),
                        new LocationCoordinate(16, 40),
                        "(//bpmn:endEvent)[1]"),
                "msg",
                "EXT.097");
        fixer = new EXT097AutoFixer();
    }

    @BeforeEach
    public void setUp() {
        clonedDoc = docToFix.clone();
    }

    @Test
    public void emptyViolationListDoesNotChangeDocument() {
        violationList = Collections.emptyList();

        FixReport report = fixer.fixIssues(clonedDoc, violationList);

        DocHandlingHelper.assertEqualDocumentSerialization(docToFix, clonedDoc);
        assertFalse(report.violationsHaveBeenFixed());
    }

    @Test
    public void otherConstraintViolationShouldNotBeFixed() {
        Violation otherViolation = new Violation(
                new Location(Paths.get("empty"), LocationCoordinate.EMPTY), "Should not be used", "OTHER");
        violationList = Collections.singletonList(otherViolation);

        FixReport report = fixer.fixIssues(clonedDoc, violationList);

        DocHandlingHelper.assertEqualDocumentSerialization(docToFix, clonedDoc);
        assertFalse(report.violationsHaveBeenFixed());
    }

    @Test
    public void fixAddsAdditionalStartEventParallelGatewayAndConnectionsIfMultipleElemsAreUnconnected()
            throws IOException, ValidationException {
        violationList = Collections.singletonList(testViolation);

        FixReport report = fixer.fixIssues(clonedDoc, violationList);

        assertTrue(report.violationsHaveBeenFixed());
        assertEquals(violationList, report.getFixedViolations());

        DocHandlingHelper.assertValidBPMNspectorResult(clonedDoc);
    }

    @Test
    public void fixAddsAdditionalStartEventIfSingleElemIsUnconnected()
            throws IOException, ValidationException, JDOMException {
        violationList = Collections.singletonList(testViolation);
        docToFix = DocHandlingHelper.loadResourceAsDoc("097/single_task_with_no_start.bpmn");

        FixReport report = fixer.fixIssues(clonedDoc, violationList);

        assertTrue(report.violationsHaveBeenFixed());
        assertEquals(violationList, report.getFixedViolations());

        DocHandlingHelper.assertValidBPMNspectorResult(clonedDoc);
    }
}
