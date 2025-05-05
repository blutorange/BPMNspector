package com.github.blutorange.bpmnspector.validation;

import com.github.blutorange.bpmnspector.api.Resource;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector.api.Warning;
import java.nio.file.Path;

/** The result of a validation process with violations and warnings, plus methods to add data. */
public interface ValidationResultBuilder extends ValidationResult {
    void addWarning(Warning warning);

    void addViolation(Violation violation);

    void addFile(Path s);

    void addResource(Resource resource);
}
