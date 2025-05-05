package com.github.blutorange.bpmnspector.api;

import com.github.blutorange.bpmnspector.common.importer.BPMNProcess;
import com.github.blutorange.bpmnspector.common.importer.ProcessImporter;
import com.github.blutorange.bpmnspector.common.util.FileUtils;
import com.github.blutorange.bpmnspector.refcheck.BPMNReferenceValidator;
import com.github.blutorange.bpmnspector.schematron.SchematronBPMNValidator;
import com.github.blutorange.bpmnspector.validation.UnsortedValidationResult;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for BPMNspector.
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public final class BPMNspector {

    private static final Logger LOGGER = LoggerFactory.getLogger(BPMNspector.class.getSimpleName());
    private final ProcessImporter bpmnImporter;
    private final SchematronBPMNValidator extValidator;
    private final BPMNReferenceValidator refValidator;

    public BPMNspector() throws ValidationException {
        extValidator = new SchematronBPMNValidator();
        refValidator = new BPMNReferenceValidator();
        bpmnImporter = new ProcessImporter();
    }

    /**
     * Validates a directory with BPMN files.
     *
     * @param directory the path to the BPMN file
     * @param validationOptions the validation options to use
     * @return the validation result
     * @throws ValidationException if an error occurs during validation
     */
    public List<ValidationResult> inspectDirectory(Path directory, List<ValidationOption> validationOptions)
            throws ValidationException {
        final var results = new ArrayList<ValidationResult>();

        final var relevantFiles = FileUtils.getAllBpmnFileFromDirectory(directory);
        for (final var path : relevantFiles) {
            results.add(inspectFile(path, validationOptions));
        }
        return results;
    }

    /**
     * Validates a BPMN file at the given path.
     *
     * @param file the path to the BPMN file
     * @param validationOptions the validation options to use
     * @return the validation result
     * @throws ValidationException if an error occurs during validation
     */
    public ValidationResult inspectFile(Path file, List<ValidationOption> validationOptions)
            throws ValidationException {

        ValidationResultBuilder result = new UnsortedValidationResult();

        // Trying to generate BPMNProcess structure - XSD validation is always
        // performed is included here
        BPMNProcess process = bpmnImporter.importProcessFromPath(file, result);

        if (process == null) {
            LOGGER.warn("Process could not parsed correctly. Further processing is skipped.");
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
        LOGGER.debug("Overall result for '{}': {}", file.getFileName().toString(), resultString);

        return result;
    }

    /**
     * Validates a BPMN file at the given path.
     *
     * @param file the path to the BPMN file
     * @return the validation result
     * @throws ValidationException if an error occurs during validation
     */
    public ValidationResult validate(String file) throws ValidationException {
        return validate(Paths.get(file));
    }

    /**
     * Validates a BPMN file at the given path.
     *
     * @param path the path to the BPMN file
     * @return the validation result
     * @throws ValidationException if an error occurs during validation
     */
    public ValidationResult validate(Path path) throws ValidationException {
        List<ValidationOption> options = new ArrayList<>();
        options.add(ValidationOption.EXT);
        options.add(ValidationOption.REF);
        return inspectFile(path, options);
    }

    /**
     * Validates a BPMN file at the given path.
     *
     * @param source The content of the BPMN file.
     * @param resourceName The name of the BPMN file.
     * @return the validation result
     * @throws ValidationException if an error occurs during validation
     */
    public ValidationResult validate(InputStream source, String resourceName) throws ValidationException {

        ValidationResultBuilder result = new UnsortedValidationResult();

        // Trying to generate BPMNProcess structure - XSD validation is always
        // performed is included here
        BPMNProcess process = bpmnImporter.importProcessFromStreamSource(source, resourceName, result);

        if (process == null) {
            LOGGER.warn("Process could not parsed correctly. Further processing is skipped.");
        } else {
            refValidator.validate(process, result);
            extValidator.validate(process, result);
        }
        var resultString = result.isValid() ? "valid" : "invalid";
        LOGGER.debug("Overall result for '{}': {}", resourceName, resultString);
        return result;
    }
}
