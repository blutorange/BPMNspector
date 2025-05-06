package com.github.blutorange.bpmnspector.autofix;

import com.github.blutorange.bpmnspector.api.Violation;
import java.util.ArrayList;
import java.util.List;

public final class FixReportBuilder {
    private final List<Violation> fixedViolations;

    public FixReportBuilder() {
        this.fixedViolations = new ArrayList<>();
    }

    public boolean violationsHaveBeenFixed() {
        return !fixedViolations.isEmpty();
    }

    public void addFixedViolation(Violation violation) {
        this.fixedViolations.add(violation);
    }

    public List<Violation> getFixedViolations() {
        return fixedViolations;
    }

    public static FixReportBuilder createUnchangedFixReport() {
        return new FixReportBuilder();
    }
}
