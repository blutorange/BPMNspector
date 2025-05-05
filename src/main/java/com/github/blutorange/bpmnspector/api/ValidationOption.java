package com.github.blutorange.bpmnspector.api;

public enum ValidationOption {
    REF("checks the correctness of references"),
    EXT("checks conformance to EXT rules");

    private final String description;

    ValidationOption(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
