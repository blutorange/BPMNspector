package com.github.blutorange.bpmnspector.api;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/** The result of a validation process with violations and warnings. */
public interface ValidationResult {
    /**
     * Fixes all possible issues found during the validation process; and outputs the fixed XML.
     *
     * @param options Options for the auto-fix process.
     * @param writer The writer to write the fixed XML to.
     */
    FixReport<Void> autoFix(AutoFixOptions options, Writer writer) throws IOException;

    /**
     * Fixes all possible issues found during the validation process; and outputs the fixed XML.
     *
     * @param options Options for the auto-fix process.
     * @param writer The writer to write the fixed XML to.
     */
    FixReport<Void> autoFix(AutoFixOptions options, OutputStream writer) throws IOException;

    /**
     * Fixes all possible issues found during the validation process; and returns the fixed XML.
     *
     * @param options Options for the auto-fix process.
     * @return The fixed XML as a string
     */
    FixReport<String> autoFix(AutoFixOptions options);

    /**
     * Returns a list of all files that have violations.
     *
     * @return a list of files with violations
     */
    default List<Path> getFilesWithViolations() {
        return getViolations().stream()
                .map(v -> v.getLocation().getResource())
                .filter(r -> (r.getType() == Resource.ResourceType.FILE
                        && r.getPath().isPresent()))
                .map(r -> r.getPath().get())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of all files that were found during the validation process.
     *
     * @return a list of files
     */
    default List<Path> getFoundFiles() {
        return getResources().stream()
                .filter(r -> (r.getType() == Resource.ResourceType.FILE)
                        && r.getPath().isPresent())
                .map(r -> r.getPath().get())
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of resources (files) that were validated.
     *
     * @return a list of resources
     */
    List<Resource> getResources();

    /**
     * Returns a list of all resources that have violations.
     *
     * @return a list of resources with violations
     */
    default List<Resource> getResourcesWithViolations() {
        return getViolations().stream()
                .map(v -> v.getLocation().getResource())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of all constraints that were violated during the validation process.
     *
     * @return a list of violated constraints
     */
    default List<String> getViolatedConstraints() {
        return getViolations().stream()
                .map(Violation::getConstraint)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of violations (errors) found during the validation process.
     *
     * @return a list of violations
     */
    List<Violation> getViolations();

    /**
     * Returns a list of warnings (advisories) found during the validation process.
     *
     * @return a list of warnings
     */
    List<Warning> getWarnings();

    /**
     * Checks whether any violations were found during the validation process.
     *
     * @return true if there are no violations, false otherwise
     */
    default boolean isValid() {
        return getViolations().isEmpty();
    }
}
