package com.github.blutorange.bpmnspector_test.schematron.artifacts;

import com.github.blutorange.bpmnspector.api.Violation;

/**
 * Test class for testing Constraint EXT.009
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext009Test extends AbstractArtifactTest {

    @Override
    protected void assertViolation(Violation v) {
        assertViolation(v, "/bpmn:definitions/bpmn:collaboration/bpmn:messageFlow", 7);
    }

    @Override
    protected String getErrorMessage() {
        return "An Artifact MUST NOT be a source for a Message Flow";
    }

    @Override
    protected String getExtNumber() {
        return "009";
    }
}
