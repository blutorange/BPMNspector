package com.github.blutorange.bpmnspector.api;

import com.github.blutorange.bpmnspector.common.importer.BPMNProcess;
import com.github.blutorange.bpmnspector.common.importer.ProcessImporter;
import com.github.blutorange.bpmnspector.common.util.FileUtils;
import com.github.blutorange.bpmnspector.refcheck.BPMNReferenceValidator;
import com.github.blutorange.bpmnspector.schematron.SchematronBPMNValidator;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matthias Geiger
 * @version 1.0
 */
public class BPMNspector implements Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(BPMNspector.class.getSimpleName());

    private final SchematronBPMNValidator extValidator;
    private final BPMNReferenceValidator refValidator;
    private final ProcessImporter bpmnImporter;

    public BPMNspector() throws ValidationException {
        extValidator = new SchematronBPMNValidator();
        refValidator = new BPMNReferenceValidator();
        bpmnImporter = new ProcessImporter();
    }

    public List<ValidationResult> inspectDirectory(Path directory, List<ValidationOption> validationOptions)
            throws ValidationException {
        List<ValidationResult> results = new ArrayList<>();

        List<Path> relevantFiles = FileUtils.getAllBpmnFileFromDirectory(directory);
        for (Path path : relevantFiles) {
            results.add(inspectFile(path, validationOptions));
        }
        return results;
    }

    public ValidationResult inspectFile(Path file, List<ValidationOption> validationOptions)
            throws ValidationException {

        ValidationResult result = new UnsortedValidationResult();

        // Trying to generate BPMNProcess structure - XSD validation is always
        // performed is included here
        BPMNProcess process = bpmnImporter.importProcessFromPath(file, result);

        if (process == null) {
            LOGGER.info("Process could not parsed correctly. Further processing is skipped.");
        } else {
            if (validationOptions.contains(ValidationOption.REF)) {
                refValidator.validate(process, result);
            }
            if (validationOptions.contains(ValidationOption.EXT)) {
                extValidator.validate(process, result);
            }
        }

        var resultString = result.isValid() ? "valid" : "invalid";
        resultString += result.getWarnings().isEmpty() ? "" : " with warnings";
        LOGGER.info("Overall result for '{}': {}", file.getFileName().toString(), resultString);

        return result;
    }

    public ValidationResult validate(Path path) throws ValidationException {
        List<ValidationOption> options = new ArrayList<>();
        options.add(ValidationOption.EXT);
        options.add(ValidationOption.REF);
        return inspectFile(path, options);
    }

    public ValidationResult validate(InputStream source, String resourceName) throws ValidationException {

        ValidationResult result = new UnsortedValidationResult();

        // Trying to generate BPMNProcess structure - XSD validation is always
        // performed is included here
        BPMNProcess process = bpmnImporter.importProcessFromStreamSource(source, resourceName, result);

        if (process == null) {
            LOGGER.info("Process could not parsed correctly. Further processing is skipped.");
        } else {
            refValidator.validate(process, result);
            extValidator.validate(process, result);
        }
        var resultString = result.isValid() ? "valid" : "invalid";
        LOGGER.info("Overall result for '{}': {}", resourceName, resultString);
        return result;
    }
}
