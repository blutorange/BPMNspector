package com.github.blutorange.bpmnspector_test.xsdvalidation.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.github.blutorange.bpmnspector.api.BPMNspector;
import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationOption;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class NonWellFormedXmlTest {

    private static BPMNspector nspector;

    private static final List<ValidationOption> REF_ONLY = List.of(ValidationOption.REF);
    private static final List<ValidationOption> EXT_ONLY = List.of(ValidationOption.EXT);

    @BeforeAll
    public static void setUpBeforeClass() throws ValidationException {
        nspector = new BPMNspector();
    }

    @Test
    public void nonWellFormedBpmnFileImportedTest() throws ValidationException {
        var file = Paths.get("src/test/resources/non-wellformed/import_non_wellformed.bpmn");
        var affectedFile = "import_non_wellformed.bpmn";
        var affectedLine = 18;

        var result = nspector.inspectFile(file, REF_ONLY);
        assertAffectedFileAndLine(result, affectedFile, affectedLine);

        result = nspector.inspectFile(file, EXT_ONLY);
        assertAffectedFileAndLine(result, affectedFile, affectedLine);
    }

    @Test
    public void nonWellformedXsdFileImportedTest() throws ValidationException {
        var file = Paths.get("src/test/resources/non-wellformed/import_xsd_non_wellformed.bpmn");
        var affectedFile = "import_xsd_non_wellformed.bpmn";
        var affectedLine = 3;

        var result = nspector.inspectFile(file, EXT_ONLY);
        assertAffectedFileAndLine(result, affectedFile, affectedLine);
    }

    private void assertAffectedFileAndLine(ValidationResult result, String affectedFile, int affectedLine) {
        assertFalse(result.isValid());
        assertEquals(
                affectedLine,
                result.getViolations().get(0).getLocation().getLocation().getRow());
        assertEquals(
                affectedFile,
                result.getViolations()
                        .get(0)
                        .getLocation()
                        .getResource()
                        .getPath()
                        .orElseThrow()
                        .getFileName()
                        .toString());
    }
}
