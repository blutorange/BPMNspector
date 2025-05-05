package com.github.blutorange.bpmnspector.api;

/** Enumeration with options for what to validate. */
public enum ValidationOption {
    /** Checks the correctness of references in the BPMN diagram. */
    REF("checks the correctness of references"),

    /** Checks the correctness of the BPMN model against the Schematron rules. */
    EXT("checks conformance to EXT rules");

    private final String description;

    ValidationOption(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
