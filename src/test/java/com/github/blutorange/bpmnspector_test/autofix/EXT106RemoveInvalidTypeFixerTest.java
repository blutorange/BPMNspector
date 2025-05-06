package com.github.blutorange.bpmnspector_test.autofix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.blutorange.bpmnspector.api.Location;
import com.github.blutorange.bpmnspector.api.LocationCoordinate;
import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector.autofix.EXT106RemoveInvalidTypeFixer;
import com.github.blutorange.bpmnspector.autofix.FixReportBuilder;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EXT106RemoveInvalidTypeFixerTest {

    private static Document defaultDocToFix;

    private List<Violation> violationList;

    private Document clonedDoc;

    @BeforeAll
    public static void loadDocToFix() throws JDOMException, IOException {
        defaultDocToFix = DocHandlingHelper.loadResourceAsDoc("106/fail_sub_process.bpmn");
    }

    @BeforeEach
    public void setUp() {
        clonedDoc = defaultDocToFix.clone();
    }

    @Test
    public void emptyViolationListDoesNotChangeDocument() {
        violationList = Collections.emptyList();
        EXT106RemoveInvalidTypeFixer fixer = new EXT106RemoveInvalidTypeFixer();

        FixReportBuilder report = fixer.fixIssues(clonedDoc, violationList);

        DocHandlingHelper.assertEqualDocumentSerialization(defaultDocToFix, clonedDoc);
        assertFalse(report.violationsHaveBeenFixed());
    }

    @Test
    public void otherConstraintViolationShouldNotBeFixed() {
        Violation otherViolation = new Violation(
                new Location(Paths.get("empty"), LocationCoordinate.empty()), "Should not be used", "OTHER");
        violationList = Collections.singletonList(otherViolation);
        EXT106RemoveInvalidTypeFixer fixer = new EXT106RemoveInvalidTypeFixer();

        FixReportBuilder report = fixer.fixIssues(clonedDoc, violationList);

        DocHandlingHelper.assertEqualDocumentSerialization(defaultDocToFix, clonedDoc);
        assertFalse(report.violationsHaveBeenFixed());
    }

    @Test
    public void fixingEXT106RemovesInvalidCancelEventDefInSubProcess()
            throws IOException, ValidationException, JDOMException {

        Document invalidCancelInSubProcess = DocHandlingHelper.loadResourceAsDoc("106/fail_sub_process.bpmn");
        Document fixedCancelInSubProcess = DocHandlingHelper.loadResourceAsDoc("106/fail_sub_process_manualFix.bpmn");

        assertCorrectnessOfFix(fixedCancelInSubProcess, invalidCancelInSubProcess);
    }

    @Test
    public void fixingEXT106RemovesInvalidCancelEventDefProcess()
            throws IOException, ValidationException, JDOMException {

        Document invalidCancelInProcess = DocHandlingHelper.loadResourceAsDoc("106/fail_cancel_end_event.bpmn");
        Document fixedCancelInProcess = DocHandlingHelper.loadResourceAsDoc("106/fail_cancel_end_event_manualFix.bpmn");

        assertCorrectnessOfFix(fixedCancelInProcess, invalidCancelInProcess);
    }

    private void assertCorrectnessOfFix(Document expectedResult, Document docToFix)
            throws IOException, ValidationException {
        violationList = Collections.singletonList(createTestViolation(docToFix));

        EXT106RemoveInvalidTypeFixer fixer = new EXT106RemoveInvalidTypeFixer();

        FixReportBuilder report = fixer.fixIssues(docToFix, violationList);

        DocHandlingHelper.assertEqualDocumentSerialization(expectedResult, docToFix);
        assertTrue(report.violationsHaveBeenFixed());
        assertEquals(violationList, report.getFixedViolations());

        DocHandlingHelper.assertValidBPMNspectorResult(docToFix);
    }

    private Violation createTestViolation(Document docToFix) {
        return new Violation(
                new Location(
                        Paths.get(docToFix.getBaseURI().replace("file:/", "")),
                        new LocationCoordinate(7, 40),
                        "(//bpmn:endEvent[./bpmn:cancelEventDefinition])[1]"),
                "msg",
                "EXT.106");
    }
}
