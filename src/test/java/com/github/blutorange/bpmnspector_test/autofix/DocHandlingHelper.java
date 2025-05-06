package com.github.blutorange.bpmnspector_test.autofix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.github.blutorange.bpmnspector.api.BPMNspector;
import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.common.importer.BPMNProcess;
import com.github.blutorange.bpmnspector.common.importer.ProcessImporter;
import com.github.blutorange.bpmnspector.validation.SimpleValidationResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class DocHandlingHelper {

    private static final String PATH_PREFIX = Paths.get(System.getProperty("user.dir"))
            .resolve("src/test/resources")
            .toString();

    public static void assertEqualBPMNProcess(BPMNProcess expected, BPMNProcess actual) {
        if (expected.getBaseURI().equals(actual.getBaseURI())
                && expected.getNamespace().equals(actual.getNamespace())) {
            assertEqualDocumentSerialization(expected.getProcessAsDoc(), actual.getProcessAsDoc());
            return;
        }

        fail("Checked BPMNProcesses " + expected + "and " + actual + " are not equal.");
    }

    public static void assertEqualDocumentSerialization(Document expected, Document actual) {
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());
        String expectedAsString = outputter.outputString(expected);
        String actualAsString = outputter.outputString(actual);
        assertEquals(expectedAsString, actualAsString);
    }

    public static BPMNProcess loadResource(String path) throws ValidationException {
        ProcessImporter importer = new ProcessImporter();

        var fullPath = Paths.get(PATH_PREFIX + File.separator + path);
        return importer.importProcessFromPath(fullPath, new SimpleValidationResult());
    }

    public static BPMNProcess loadResource(String path, boolean removeDI) throws ValidationException {
        ProcessImporter importer = new ProcessImporter();

        var fullPath = Paths.get(PATH_PREFIX + File.separator + path);
        return importer.importProcessFromPath(fullPath, new SimpleValidationResult(), removeDI);
    }

    public static Document loadResourceAsDoc(String path) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        return builder.build(new File(PATH_PREFIX + File.separator + path));
    }

    public static void assertValidBPMNspectorResult(Document docToCheck) throws ValidationException, IOException {
        var outputter = new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());

        var bos = new ByteArrayOutputStream();
        outputter.output(docToCheck, bos);

        var bis = new ByteArrayInputStream(bos.toByteArray());
        var inspector = new BPMNspector();
        var result = inspector.validate(bis, "name");
        assertTrue(result.isValid());
    }
}
