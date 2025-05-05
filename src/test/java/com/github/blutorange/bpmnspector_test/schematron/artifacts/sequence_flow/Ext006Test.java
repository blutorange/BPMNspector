package com.github.blutorange.bpmnspector_test.schematron.artifacts.sequence_flow;

import com.github.blutorange.bpmnspector.api.Violation;

/**
 * Test class for testing Constraint EXT.006
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class Ext006Test extends AbstractArtifactSequenceFlowTest {

    private static final String ERRORMESSAGEONE = "An Artifact MUST NOT be a target for a Sequence Flow";
    private static final String ERRORMESSAGETWO =
            "For a Process: Of the types of FlowNode, only Activities, Gateways, and Events can be the target. However, Activities that are Event SubProcesses are not allowed to be a target";
    private static final String ERRORMESSAGETHREE =
            "The target element of the sequence flow must reference the SequenceFlow definition using their incoming attribute.";
    private static final String XPATHSTRING = "/bpmn:definitions/bpmn:process/bpmn:sequenceFlow";

    @Override
    protected void assertFirstViolation(Violation v, String fileName) {
        assertViolation(v, ERRORMESSAGEONE, fileName, XPATHSTRING, 7);
    }

    @Override
    protected void assertSecondViolation(Violation v, String fileName, int line) {
        switch (fileName) {
            case "Fail_group.bpmn":
                assertViolation(v, ERRORMESSAGETWO, fileName, "/bpmn:definitions/bpmn:process/bpmn:group", line);
                break;
            case "Fail_association.bpmn":
                assertViolation(v, ERRORMESSAGETWO, fileName, "/bpmn:definitions/bpmn:process/bpmn:association", line);
                break;
            case "Fail_text_annotation.bpmn":
                assertViolation(
                        v, ERRORMESSAGETWO, fileName, "/bpmn:definitions/bpmn:process/bpmn:textAnnotation", line);
                break;
        }
    }

    @Override
    protected void assertThirdViolation(Violation v, String fileName) {
        assertViolation(v, ERRORMESSAGETHREE, fileName, XPATHSTRING, 7);
    }

    @Override
    protected String getExtNumber() {
        return "006";
    }
}
