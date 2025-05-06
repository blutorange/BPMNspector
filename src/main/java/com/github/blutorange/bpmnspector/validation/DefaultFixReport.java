package com.github.blutorange.bpmnspector.validation;

import com.github.blutorange.bpmnspector.api.FixReport;
import com.github.blutorange.bpmnspector.api.Violation;
import java.util.List;

final class DefaultFixReport<Value> implements FixReport<Value> {
    private final List<Violation> fixedViolations;
    private final Value value;

    DefaultFixReport(Value value, List<Violation> fixedViolations) {
        this.value = value;
        this.fixedViolations = fixedViolations;
    }

    @Override
    public List<Violation> getFixedViolations() {
        return fixedViolations;
    }

    @Override
    public Value getValue() {
        return value;
    }
}
