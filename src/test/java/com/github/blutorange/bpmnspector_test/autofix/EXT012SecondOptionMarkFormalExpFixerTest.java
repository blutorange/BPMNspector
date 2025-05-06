package com.github.blutorange.bpmnspector_test.autofix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.blutorange.bpmnspector.api.Location;
import com.github.blutorange.bpmnspector.api.LocationCoordinate;
import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector.autofix.EXT012SecondOptionMarkFormalExpFixer;
import com.github.blutorange.bpmnspector.autofix.FixReportBuilder;
import com.github.blutorange.bpmnspector.autofix.ViolationFixer;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EXT012SecondOptionMarkFormalExpFixerTest {
    private static Document docToFix;
    private static Document fixedVersionOfDoc;
    private static Violation testViolation;

    private List<Violation> violationList;

    private Document clonedDoc;
    private ViolationFixer fixer;

    @BeforeAll
    public static void loadDocToFix() throws JDOMException, IOException {
        docToFix = DocHandlingHelper.loadResourceAsDoc("012/EXT012_failure_sequenceFlow.bpmn");
        fixedVersionOfDoc = DocHandlingHelper.loadResourceAsDoc("012/EXT012_success_sequenceFlow.bpmn");
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
        fixer = new EXT012SecondOptionMarkFormalExpFixer();
        clonedDoc = docToFix.clone();
    }

    @Test
    public void emptyViolationListDoesNotChangeDocument() {
        violationList = Collections.emptyList();

        FixReportBuilder report = fixer.fixIssues(clonedDoc, violationList);

        DocHandlingHelper.assertEqualDocumentSerialization(docToFix, clonedDoc);
        assertFalse(report.violationsHaveBeenFixed());
    }

    @Test
    public void otherConstraintViolationShouldNotBeFixed() {
        Violation otherViolation = new Violation(
                new Location(Paths.get("empty"), LocationCoordinate.empty()), "Should not be used", "OTHER");
        violationList = Collections.singletonList(otherViolation);

        FixReportBuilder report = fixer.fixIssues(clonedDoc, violationList);

        DocHandlingHelper.assertEqualDocumentSerialization(docToFix, clonedDoc);
        assertFalse(report.violationsHaveBeenFixed());
    }

    @Test
    public void fixingSequenceFlowAddsXsiTypeAttributeToConditionExpression() throws JDOMException, IOException {
        Document docToFix = DocHandlingHelper.loadResourceAsDoc("012/EXT012_failure_sequenceFlow.bpmn");
        Document fixedVersionOfDoc = DocHandlingHelper.loadResourceAsDoc("012/EXT012_success_sequenceFlow.bpmn");
        Violation testViolation = new Violation(
                new Location(
                        Paths.get(docToFix.getBaseURI().replace("file:/", "")),
                        new LocationCoordinate(11, 7),
                        "(//bpmn:sequenceFlow[bpmn:conditionExpression and ancestor::bpmn:process[@isExecutable='true']])[1]"),
                "msg",
                "EXT.012");

        runFixAndAssertCorrectness(docToFix, fixedVersionOfDoc, testViolation);
    }

    @Test
    public void fixingAdHocSubProcessAddsXsiTypeAttributeToCompletionCondition() throws JDOMException, IOException {
        Document docToFix = DocHandlingHelper.loadResourceAsDoc("012/EXT012_failure_adHocSubProcess.bpmn");
        Document fixedVersionOfDoc = DocHandlingHelper.loadResourceAsDoc("012/EXT012_success_adHocSubProcess.bpmn");
        Violation testViolation = new Violation(
                new Location(
                        Paths.get(docToFix.getBaseURI().replace("file:/", "")),
                        new LocationCoordinate(7, 168),
                        "(//bpmn:adHocSubProcess[bpmn:completionCondition and ancestor::bpmn:process[@isExecutable='true']])[1]"),
                "msg",
                "EXT.012");

        runFixAndAssertCorrectness(docToFix, fixedVersionOfDoc, testViolation);
    }

    @Test
    public void fixingAssignmentAddsXsiTypeAttributeToFromAndTo() throws JDOMException, IOException {
        Document docToFix = DocHandlingHelper.loadResourceAsDoc("012/EXT012_failure_assignment.bpmn");
        Document fixedVersionOfDoc = DocHandlingHelper.loadResourceAsDoc("012/EXT012_success_assignment.bpmn");
        Violation testViolation = new Violation(
                new Location(
                        Paths.get(docToFix.getBaseURI().replace("file:/", "")),
                        new LocationCoordinate(7, 168),
                        "(//bpmn:assignment[ancestor::bpmn:process[@isExecutable='true']])[1]"),
                "msg",
                "EXT.012");

        runFixAndAssertCorrectness(docToFix, fixedVersionOfDoc, testViolation);
    }

    @Test
    public void fixingComplexGatewayAddsXsiTypeAttributeToActivationCondition() throws JDOMException, IOException {
        Document docToFix = DocHandlingHelper.loadResourceAsDoc("012/EXT012_failure_complexGateway.bpmn");
        Document fixedVersionOfDoc = DocHandlingHelper.loadResourceAsDoc("012/EXT012_success_complexGateway.bpmn");
        Violation testViolation = new Violation(
                new Location(
                        Paths.get(docToFix.getBaseURI().replace("file:/", "")),
                        new LocationCoordinate(7, 168),
                        "(//bpmn:complexGateway[bpmn:activationCondition and ancestor::bpmn:process[@isExecutable='true']])[1]"),
                "msg",
                "EXT.012");

        runFixAndAssertCorrectness(docToFix, fixedVersionOfDoc, testViolation);
    }

    @Test
    public void fixingMultiInstanceLoopCharacteristicsAddsXsiTypeAttributeToLoopCardinality()
            throws JDOMException, IOException {
        Document docToFix = DocHandlingHelper.loadResourceAsDoc(
                "012/EXT012_failure_multiInstanceLoopCharacteristics_invalidCardinality.bpmn");
        Document fixedVersionOfDoc =
                DocHandlingHelper.loadResourceAsDoc("012/EXT012_success_multiInstanceLoopCharacteristics.bpmn");
        Violation testViolation = new Violation(
                new Location(
                        Paths.get(docToFix.getBaseURI().replace("file:/", "")),
                        new LocationCoordinate(7, 168),
                        "(//bpmn:multiInstanceLoopCharacteristics[bpmn:loopCardinality and ancestor::bpmn:process[@isExecutable='true']])[1]"),
                "msg",
                "EXT.012");

        runFixAndAssertCorrectness(docToFix, fixedVersionOfDoc, testViolation);
    }

    @Test
    public void fixingMultiInstanceLoopCharacteristicsAddsXsiTypeAttributeToCompletionCondition()
            throws JDOMException, IOException {
        Document docToFix = DocHandlingHelper.loadResourceAsDoc(
                "012/EXT012_failure_multiInstanceLoopCharacteristics_invalidCompletionCondition.bpmn");
        Document fixedVersionOfDoc =
                DocHandlingHelper.loadResourceAsDoc("012/EXT012_success_multiInstanceLoopCharacteristics.bpmn");
        Violation testViolation = new Violation(
                new Location(
                        Paths.get(docToFix.getBaseURI().replace("file:/", "")),
                        new LocationCoordinate(7, 168),
                        "(//bpmn:multiInstanceLoopCharacteristics[bpmn:completionCondition and ancestor::bpmn:process[@isExecutable='true']])[1]"),
                "msg",
                "EXT.012");

        runFixAndAssertCorrectness(docToFix, fixedVersionOfDoc, testViolation);
    }

    @Test
    public void fixingStandardLoopCharacteristicsAddsXsiTypeAttributeToLoopCondition()
            throws JDOMException, IOException {
        Document docToFix = DocHandlingHelper.loadResourceAsDoc("012/EXT012_failure_standardLoopCharacteristics.bpmn");
        Document fixedVersionOfDoc =
                DocHandlingHelper.loadResourceAsDoc("012/EXT012_success_standardLoopCharacteristics.bpmn");
        Violation testViolation = new Violation(
                new Location(
                        Paths.get(docToFix.getBaseURI().replace("file:/", "")),
                        new LocationCoordinate(7, 168),
                        "(//bpmn:standardLoopCharacteristics[bpmn:loopCondition and ancestor::bpmn:process[@isExecutable='true']])[1]"),
                "msg",
                "EXT.012");

        runFixAndAssertCorrectness(docToFix, fixedVersionOfDoc, testViolation);
    }

    @Test
    public void fixingTimerEventDefinitionAddsXsiTypeAttributeToTimeCycle() throws JDOMException, IOException {
        Document docToFix = DocHandlingHelper.loadResourceAsDoc("012/EXT012_failure_timerEvent_timeCycle.bpmn");
        Document fixedVersionOfDoc =
                DocHandlingHelper.loadResourceAsDoc("012/EXT012_success_timerEvent_timeCycle.bpmn");
        Violation testViolation = new Violation(
                new Location(
                        Paths.get(docToFix.getBaseURI().replace("file:/", "")),
                        new LocationCoordinate(7, 168),
                        "(//bpmn:timerEventDefinition[(bpmn:timeDate or bpmn:timeDuration or bpmn:timeCycle) and ancestor::bpmn:process[@isExecutable='true']])[1]"),
                "msg",
                "EXT.012");

        runFixAndAssertCorrectness(docToFix, fixedVersionOfDoc, testViolation);
    }

    @Test
    public void fixingTimerEventDefinitionAddsXsiTypeAttributeToTimeDuration() throws JDOMException, IOException {
        Document docToFix = DocHandlingHelper.loadResourceAsDoc("012/EXT012_failure_timerEvent_timeDuration.bpmn");
        Document fixedVersionOfDoc =
                DocHandlingHelper.loadResourceAsDoc("012/EXT012_success_timerEvent_timeDuration.bpmn");
        Violation testViolation = new Violation(
                new Location(
                        Paths.get(docToFix.getBaseURI().replace("file:/", "")),
                        new LocationCoordinate(7, 168),
                        "(//bpmn:timerEventDefinition[(bpmn:timeDate or bpmn:timeDuration or bpmn:timeCycle) and ancestor::bpmn:process[@isExecutable='true']])[1]"),
                "msg",
                "EXT.012");

        runFixAndAssertCorrectness(docToFix, fixedVersionOfDoc, testViolation);
    }

    @Test
    public void fixingTimerEventDefinitionAddsXsiTypeAttributeToTimeDate() throws JDOMException, IOException {
        Document docToFix = DocHandlingHelper.loadResourceAsDoc("012/EXT012_failure_timerEvent_timeDate.bpmn");
        Document fixedVersionOfDoc = DocHandlingHelper.loadResourceAsDoc("012/EXT012_success_timerEvent_timeDate.bpmn");
        Violation testViolation = new Violation(
                new Location(
                        Paths.get(docToFix.getBaseURI().replace("file:/", "")),
                        new LocationCoordinate(7, 168),
                        "(//bpmn:timerEventDefinition[(bpmn:timeDate or bpmn:timeDuration or bpmn:timeCycle) and ancestor::bpmn:process[@isExecutable='true']])[1]"),
                "msg",
                "EXT.012");

        runFixAndAssertCorrectness(docToFix, fixedVersionOfDoc, testViolation);
    }

    private void runFixAndAssertCorrectness(Document docToFix, Document fixedVersionOfDoc, Violation testViolation) {
        violationList = Collections.singletonList(testViolation);
        FixReportBuilder report = fixer.fixIssues(docToFix, violationList);

        DocHandlingHelper.assertEqualDocumentSerialization(fixedVersionOfDoc, docToFix);
        assertTrue(report.violationsHaveBeenFixed());
        assertEquals(violationList, report.getFixedViolations());
    }
}
