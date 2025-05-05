package com.github.blutorange.bpmnspector_test.schematron.artifacts;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Abstract test class for simplifying the testing of the Constraints EXT.008 and EXT.009
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public abstract class AbstractArtifactTest extends TestCase {

    @Test
    public void testConstraintAssociationFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("Fail_association.bpmn"), 1);
        assertViolation(result.getViolations().get(0));
    }

    @Test
    public void testConstraintGroupFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("Fail_group.bpmn"), 1);
        assertViolation(result.getViolations().get(0));
    }

    @Test
    public void testConstraintTextAnnotationFail() throws ValidationException {
        ValidationResultBuilder result = verifyInvalidResult(createFile("Fail_text_annotation.bpmn"), 1);
        assertViolation(result.getViolations().get(0));
    }

    protected void assertViolation(Violation v) {
        throw new UnsupportedOperationException("must be overridden by every child class!");
    }
}
