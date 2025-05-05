package com.github.blutorange.bpmnspector.api;

import com.github.blutorange.bpmnspector.common.importer.BPMNProcess;

public interface BpmnProcessValidator {
    void validate(BPMNProcess process, ValidationResult validationResult) throws ValidationException;
}
