package com.github.blutorange.bpmnspector_test.autofix;

import com.github.blutorange.bpmnspector.api.Location;
import com.github.blutorange.bpmnspector.api.LocationCoordinate;
import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector.autofix.ConstraintFixer;
import com.github.blutorange.bpmnspector.autofix.FixingStrategy;
import com.github.blutorange.bpmnspector.common.importer.BPMNProcess;
import java.nio.file.Paths;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class ConstraintFixerTest {
    private ConstraintFixer fixer;

    @Test
    public void fixingCorrectProcessDoesNotChangeProcess() throws ValidationException {
        BPMNProcess correctProcess = getBpmnProcessForCorrectProcess();
        fixer = new ConstraintFixer(correctProcess, Collections.emptyMap());

        DocHandlingHelper.assertEqualBPMNProcess(correctProcess, fixer.fixAllPossibleIssuesAndReturnProcess());
    }

    @Test
    public void fixingEXT128FailureMarksProcessAsNotExecutable() throws ValidationException {
        var invalidProcess = DocHandlingHelper.loadResource("128/EXT128_failure_endEvent.bpmn");
        var singleViolation = new Violation(
                new Location(
                        Paths.get(invalidProcess.getBaseURI()),
                        new LocationCoordinate(11, 7),
                        "(//bpmn:messageEventDefinition[ancestor::bpmn:process[@isExecutable='true']])[1]"),
                "msg",
                "EXT.128");

        fixer = new ConstraintFixer(
                invalidProcess, Collections.singletonMap(singleViolation, FixingStrategy.FIRST_OPTION));
        fixer.fixAllPossibleIssues();

        DocHandlingHelper.assertEqualBPMNProcess(
                getManuallyFixedProcess("128/EXT128_failure_endEvent_manualFix.bpmn", invalidProcess.getBaseURI()),
                fixer.getFixedProcess());
    }

    private BPMNProcess getManuallyFixedProcess(String resourceToLoad, String baseUriToUse) throws ValidationException {
        BPMNProcess loaded = DocHandlingHelper.loadResource(resourceToLoad);
        return new BPMNProcess(loaded.getProcessAsDoc(), baseUriToUse, loaded.getNamespace());
    }

    private BPMNProcess getBpmnProcessForCorrectProcess() throws ValidationException {
        return DocHandlingHelper.loadResource("/001/Success.bpmn");
    }
}
