package com.github.blutorange.bpmnspector_test.autofix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.blutorange.bpmnspector.api.Location;
import com.github.blutorange.bpmnspector.api.LocationCoordinate;
import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector.autofix.EXT150AutoFixer;
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

public class EXT150AutoFixerTest {

    private static Document docToFix;
    private static Violation testViolation;

    private List<Violation> violationList;

    private Document clonedDoc;

    @BeforeAll
    public static void loadDocToFix() throws JDOMException, IOException {
        docToFix = DocHandlingHelper.loadResourceAsDoc("105/fail_end_without_sub-events.bpmn");
        testViolation = new Violation(
                new Location(
                        Paths.get(docToFix.getBaseURI().replace("file:/", "")),
                        new LocationCoordinate(11, 7),
                        "(//bpmn:startEvent)[1]"),
                "msg",
                "EXT.150");
    }

    @BeforeEach
    public void setUp() {
        clonedDoc = docToFix.clone();
    }

    @Test
    public void emptyViolationListDoesNotChangeDocument() {
        violationList = Collections.emptyList();
        EXT150AutoFixer fixer = new EXT150AutoFixer();

        FixReportBuilder report = fixer.fixIssues(clonedDoc, violationList);

        DocHandlingHelper.assertEqualDocumentSerialization(docToFix, clonedDoc);
        assertFalse(report.violationsHaveBeenFixed());
    }

    @Test
    public void otherConstraintViolationShouldNotBeFixed() {
        Violation otherViolation = new Violation(
                new Location(Paths.get("empty"), LocationCoordinate.empty()), "Should not be used", "OTHER");
        violationList = Collections.singletonList(otherViolation);
        EXT150AutoFixer fixer = new EXT150AutoFixer();

        FixReportBuilder report = fixer.fixIssues(clonedDoc, violationList);

        DocHandlingHelper.assertEqualDocumentSerialization(docToFix, clonedDoc);
        assertFalse(report.violationsHaveBeenFixed());
    }

    @Test
    public void fixAddsParallelGatewayToExistingStartEventAndConnectsUnconnectedSubProcess()
            throws IOException, ValidationException, JDOMException {
        Document doc = DocHandlingHelper.loadResourceAsDoc("150/fail_normal_sequence_flow_missing_2.bpmn");
        testViolation = new Violation(
                new Location(
                        Paths.get(doc.getBaseURI().replace("file:/", "")),
                        LocationCoordinate.empty(),
                        "(//bpmn:subProcess[@isForCompensation = 'false' and @triggeredByEvent = 'false'] [parent::*/bpmn:startEvent])[1]"),
                "msg",
                "EXT.150");
        violationList = Collections.singletonList(testViolation);

        EXT150AutoFixer fixer = new EXT150AutoFixer();

        FixReportBuilder report = fixer.fixIssues(doc, violationList);

        assertTrue(report.violationsHaveBeenFixed());
        assertEquals(violationList, report.getFixedViolations());

        DocHandlingHelper.assertValidBPMNspectorResult(doc);
    }

    @Test
    public void fixAddsParallelGatewayToExistingStartEventAndConnectsToUnconnectedTask()
            throws IOException, ValidationException, JDOMException {
        Document doc = DocHandlingHelper.loadResourceAsDoc("150/fail_normal_sequence_flow_missing_1.bpmn");
        testViolation = new Violation(
                new Location(
                        Paths.get(doc.getBaseURI().replace("file:/", "")),
                        LocationCoordinate.empty(),
                        "(//bpmn:task[@isForCompensation = 'false'] [parent::*/bpmn:startEvent])[5]"),
                "msg",
                "EXT.150");
        violationList = Collections.singletonList(testViolation);

        EXT150AutoFixer fixer = new EXT150AutoFixer();

        FixReportBuilder report = fixer.fixIssues(doc, violationList);

        assertTrue(report.violationsHaveBeenFixed());
        assertEquals(violationList, report.getFixedViolations());

        DocHandlingHelper.assertValidBPMNspectorResult(doc);
    }

    @Test
    public void fixAddsParallelGatewayToExistingStartEventAndConnectsToUnconnectedTaskInSubProcess()
            throws IOException, ValidationException, JDOMException {
        Document doc = DocHandlingHelper.loadResourceAsDoc("150/fail_sequence_flow_in_sub_process_missing_1.bpmn");
        testViolation = new Violation(
                new Location(
                        Paths.get(doc.getBaseURI().replace("file:/", "")),
                        LocationCoordinate.empty(),
                        "(//bpmn:serviceTask[@isForCompensation = 'false'] [parent::*/bpmn:startEvent])[1]"),
                "msg",
                "EXT.150");
        violationList = Collections.singletonList(testViolation);

        EXT150AutoFixer fixer = new EXT150AutoFixer();

        FixReportBuilder report = fixer.fixIssues(doc, violationList);

        assertTrue(report.violationsHaveBeenFixed());
        assertEquals(violationList, report.getFixedViolations());

        DocHandlingHelper.assertValidBPMNspectorResult(doc);
    }
}
