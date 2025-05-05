package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.045
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext045Test extends TestCase {

    private static final String ERR_MSG = "If the operation defines a outMessage, the ItemDefinition of the DataOutput "
            + "of the ServiceTask and the outMessage of the operation must be equal.";

    @Test
    public void testConstraintFailDifferentItemDefNoStructureRef() throws ValidationException {
        ValidationResultBuilder result =
                verifyInvalidResult(createFile("EXT045_failure_differentItemDef_noStructureRef.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:serviceTask", 17);
    }

    @Test
    public void testConstraintFailDifferentItemDefUnequalStructureRef() throws ValidationException {
        ValidationResultBuilder result =
                verifyInvalidResult(createFile("EXT045_failure_differentItemDef_unequalStructureRef.bpmn"), 1);
        assertViolation(result.getViolations().get(0), ERR_MSG, "/bpmn:definitions/bpmn:process/bpmn:serviceTask", 17);
    }

    @Test
    public void testConstraintSuccessDifferentItemDefEqualStructureRef() throws ValidationException {
        verifyValidResult(createFile("EXT045_success_differentItemDef_equalStructureRef.bpmn"));
    }

    @Test
    public void testConstraintSuccessOperationNoOutMessage() throws ValidationException {
        verifyValidResult(createFile("EXT045_success_operationNoOutMessage.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "045";
    }
}
