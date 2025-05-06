package com.github.blutorange.bpmnspector.api;

import java.util.List;

/**
 * Represents a report of the fixes applied to a BPMN diagram.
 *
 * @param <Value> the type of the value returned by the fixer
 */
public interface FixReport<Value> {
    /**
     * Gets all violations that have been fixed.
     *
     * @return a list of fixed violations
     */
    List<Violation> getFixedViolations();

    /**
     * Gets the value returned by the fixer.
     *
     * @return the value returned by the fixer
     */
    Value getValue();
}
