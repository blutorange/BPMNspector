package com.github.blutorange.bpmnspector.api;

import java.util.Objects;

/**
 * Represents a warning in a BPMN diagram. A warning is a message that indicates a potential issue or best practice
 * violation in the BPMN model.
 */
public final class Warning implements Comparable<Warning> {

    private final String message;
    private final Location location;

    /**
     * Constructs a new warning instance.
     *
     * @param message the warning message
     * @param location the location of the warning in the BPMN model
     * @throws NullPointerException if message or location is null
     */
    public Warning(String message, Location location) {
        this.message = Objects.requireNonNull(message);
        this.location = Objects.requireNonNull(location);
    }

    /**
     * Gets the warning message.
     *
     * @return the warning message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the location of the warning in the BPMN model.
     *
     * @return the location of the warning
     */
    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Warning{" + "message='" + message + '\'' + ", indicator=" + location + '}';
    }

    @Override
    public int compareTo(Warning o) {
        Objects.requireNonNull(o);

        if (getMessage().equals(o.getMessage())) {
            return getLocation().compareTo(o.getLocation());
        } else {
            return getMessage().compareTo(o.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var warning = (Warning) o;
        return Objects.equals(message, warning.message) && Objects.equals(location, warning.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, location);
    }
}
