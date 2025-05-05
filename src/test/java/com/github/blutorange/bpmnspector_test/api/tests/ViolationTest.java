package com.github.blutorange.bpmnspector_test.api.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.blutorange.bpmnspector.api.Location;
import com.github.blutorange.bpmnspector.api.LocationCoordinate;
import com.github.blutorange.bpmnspector.api.Violation;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

/** @author Matthias Geiger */
public class ViolationTest {

    private final Location dummyLocationEmpty = new Location(Paths.get("dummy file"), LocationCoordinate.EMPTY);
    private final Location dummyLocation_1_1 = new Location(Paths.get("dummy file"), new LocationCoordinate(1, 1));
    private final Location dummyLocation_1_2 = new Location(Paths.get("dummy file"), new LocationCoordinate(1, 2));

    private final Violation violation1 = new Violation(dummyLocation_1_1, "msg1", "#1");
    private final Violation violation1_clone = new Violation(dummyLocation_1_1, "msg1", "#1");
    private final Violation violation1_loc1_2 = new Violation(dummyLocation_1_2, "msg1", "#1");
    private final Violation violation2 = new Violation(dummyLocation_1_1, "msg2", "#2");

    @Test
    public void testInvalidUsageOfNullLocation() {
        assertThrows(NullPointerException.class, () -> new Violation(null, "dummy msg", "dummy constraint"));
    }

    @Test
    public void testInvalidUsageOfNullMessage() {
        assertThrows(NullPointerException.class, () -> new Violation(dummyLocationEmpty, null, "dummy constraint"));
    }

    @Test
    public void testInvalidUsageOfNullConstraint() {
        assertThrows(NullPointerException.class, () -> new Violation(dummyLocationEmpty, "dummy msg", null));
    }

    @Test
    public void compareEqualViolations() {
        assertEquals(0, violation1.compareTo(violation1_clone));
    }

    @Test
    public void compareViolation1_loc1_2() {
        assertEquals(-1, violation1.compareTo(violation1_loc1_2));
        assertEquals(1, violation1_loc1_2.compareTo(violation1));
    }

    @Test
    public void compareViolation1_2() {
        assertEquals(-1, violation1.compareTo(violation2));
        assertEquals(1, violation2.compareTo(violation1));
    }
}
