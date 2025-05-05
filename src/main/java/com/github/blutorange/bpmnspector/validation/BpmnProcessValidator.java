package com.github.blutorange.bpmnspector.validation;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.common.importer.BPMNProcess;

/** Validator for BPMN diagrams. */
public interface BpmnProcessValidator {
    /**
     * Validates the given BPMN process and adds any violations to the provided validation result.
     *
     * @param process the BPMN process to validate
     * @param validationResult the validation result to which violations will be added
     * @throws ValidationException if an error occurs during validation
     */
    void validate(BPMNProcess process, ValidationResultBuilder validationResult) throws ValidationException;
}
