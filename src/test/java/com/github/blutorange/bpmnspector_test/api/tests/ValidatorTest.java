package com.github.blutorange.bpmnspector_test.api.tests;

import com.github.blutorange.bpmnspector.api.BPMNspector;
import com.github.blutorange.bpmnspector.api.ValidationException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

/** @author Matthias Geiger */
public class ValidatorTest {

    private final Path path = Paths.get(System.getProperty("user.dir"))
            .resolve("src")
            .resolve("test")
            .resolve("resources")
            .resolve("test-1-gruppe-c.bpmn");
    private final BPMNspector validator;

    public ValidatorTest() throws ValidationException {
        validator = new BPMNspector();
    }

    @Test
    public void testValidateMethodPath() throws ValidationException {
        validator.validate(path);
    }

    @Test
    public void testValidateMethodString() throws ValidationException {
        validator.validate(path.toString());
    }
}
