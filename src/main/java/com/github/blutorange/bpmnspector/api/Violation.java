package com.github.blutorange.bpmnspector.api;

import java.util.Objects;

/** A violation of a constraint in a BPMN diagram. */
public final class Violation implements Comparable<Violation> {

    // what
    private final String constraint;
    private final String message;

    // where
    private final Location location;

    /**
     * Creates a new violation.
     *
     * @param location the location of the violation
     * @param message the message describing the violation
     * @param constraint the constraint that was violated
     */
    public Violation(Location location, String message, String constraint) {
        this.location = Objects.requireNonNull(location);
        this.message = Objects.requireNonNull(message);
        this.constraint = Objects.requireNonNull(constraint);
    }

    /**
     * The constraint that was violated.
     *
     * @return the constraint
     */
    public String getConstraint() {
        return constraint;
    }

    /**
     * The message describing the violation.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * The location of the violation in the BPMN diagram.
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Violation{" + "constraint='"
                + constraint + '\'' + ", message='"
                + message + '\'' + ", indicator="
                + location + '}';
    }

    @Override
    public int compareTo(Violation o) {
        Objects.requireNonNull(o);

        if (getConstraint().equals(o.getConstraint())) {
            if (getMessage().equals(o.getMessage())) {
                return getLocation().compareTo(o.getLocation());
            } else {
                return getMessage().compareTo(o.getMessage());
            }
        } else {
            return getConstraint().compareTo(o.getConstraint());
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
        var violation = (Violation) o;
        return Objects.equals(constraint, violation.constraint)
                && Objects.equals(message, violation.message)
                && Objects.equals(location, violation.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constraint, message, location);
    }
}
