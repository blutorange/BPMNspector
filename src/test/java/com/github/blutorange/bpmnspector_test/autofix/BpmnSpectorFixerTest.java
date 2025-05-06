package com.github.blutorange.bpmnspector_test.autofix;

import static com.github.blutorange.bpmnspector_test.autofix.DocHandlingHelper.assertEqualBPMNProcess;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.blutorange.bpmnspector.api.AutoFixOptions;
import com.github.blutorange.bpmnspector.api.BPMNspector;
import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.common.importer.BPMNProcess;
import com.github.blutorange.bpmnspector.common.importer.ProcessImporter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;

@SuppressWarnings("SameParameterValue")
final class BpmnSpectorFixerTest {
    @Test
    public void canAutoFixAfterValidation() throws ValidationException, IOException {
        // arrange
        var inspector = new BPMNspector();
        var data = readStringResource("/128/EXT128_failure_endEvent.bpmn");
        var result = inspector.validate(new ByteArrayInputStream(data.getBytes(UTF_8)), "input.bpmn");

        // act
        var report = result.autoFix(new AutoFixOptions().setPrettyPrint(true));

        // assert
        var input = new ByteArrayInputStream(report.getValue().getBytes(UTF_8));
        var fixedProcess = new ProcessImporter().importProcessFromStreamSource(input, "fixed.bpmn");
        assertEqualBPMNProcess(
                getManuallyFixedProcess("128/EXT128_failure_endEvent_manualFix.bpmn", "fixed.bpmn"), fixedProcess);
        assertEquals(1, report.getFixedViolations().size());
        assertEquals("EXT.128", report.getFixedViolations().get(0).getConstraint());
    }

    private BPMNProcess getManuallyFixedProcess(String resourceToLoad, String baseUriToUse) throws ValidationException {
        var loaded = DocHandlingHelper.loadResource(resourceToLoad);
        return new BPMNProcess(loaded.getProcessAsDoc(), baseUriToUse, loaded.getNamespace());
    }

    private String readStringResource(String path) throws IOException {
        try (var input = getClass().getResourceAsStream(path)) {
            assertNotNull(input, "Resource not found: " + path);
            return new String(input.readAllBytes(), UTF_8);
        }
    }
}
