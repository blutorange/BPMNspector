package com.github.blutorange.bpmnspector.api;

/** Options for auto-fixers. */
public final class AutoFixOptions {
    private boolean prettyPrint = false;

    /**
     * Returns whether the output should be pretty-printed.
     *
     * @return true if pretty-printing is enabled, false otherwise
     */
    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    /**
     * Sets whether the output should be pretty-printed.
     *
     * @param prettyPrint true to enable pretty-printing, false to disable it
     * @return This instance for method chaining.
     */
    public AutoFixOptions setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        return this;
    }
}
