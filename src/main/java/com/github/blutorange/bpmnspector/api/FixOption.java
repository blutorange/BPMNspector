package com.github.blutorange.bpmnspector.api;

/** Enumeration with options what to auto-fix. */
public enum FixOption {
    /** No fixes should be performed (default). */
    NONE("No fixes should be performed (default)"),
    /** All fixable violations will be fixed automatically. */
    AUTO("all fixable violations will be fixed automatically"),
    /** Ask for each violation if it should be fixed. */
    INTERACTIVE("ask for each violation");

    private final String description;

    FixOption(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
