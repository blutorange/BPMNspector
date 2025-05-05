package com.github.blutorange.bpmnspector_test.schematron.commonExec;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector_test.schematron.TestCase;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing Constraint EXT.013
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class Ext013Test extends TestCase {

    private static final String ERR_MSG =
            "According to constraint CARD.064 (see Sec. 3.1) body is a mandatory attribute "
                    + "of a FormalExpression. Therefore, the element must contain any Expression as a string content.";

    @Test
    public void testConstraintFailTimerEventTimeDateNoBody() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT013_failure_timerEvent_timeDate_noBody.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:intermediateCatchEvent/bpmn:timerEventDefinition/bpmn:timeDate",
                11);
    }

    @Test
    public void testConstraintFailTransformation() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT013_failure_transformation.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:task/bpmn:dataInputAssociation/bpmn:transformation",
                21);
    }

    @Test
    public void testConstraintSuccessTransformation() throws ValidationException {
        verifyValidResult(createFile("EXT013_success_transformation.bpmn"));
    }

    @Test
    public void testConstraintFailComplexBehaviorDefinition() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT013_failure_complexBehaviorDefinition.bpmn"), 1);
        assertViolation(
                result.getViolations().get(0),
                ERR_MSG,
                "/bpmn:definitions/bpmn:process/bpmn:task/bpmn:multiInstanceLoopCharacteristics/bpmn:complexBehaviorDefinition/bpmn:condition",
                15);
    }

    @Test
    public void testConstraintSuccessComplexBehaviorDefinition() throws ValidationException {
        verifyValidResult(createFile("EXT013_success_complexBehaviorDefinition.bpmn"));
    }

    @Test
    public void testConstraintFailCorrelation() throws ValidationException {
        ValidationResult result = verifyInvalidResult(createFile("EXT013_failure_correlation.bpmn"), 2);
        assertViolation(
                result.getViolations().get(1),
                ERR_MSG,
                "/bpmn:definitions/bpmn:correlationProperty/bpmn:correlationPropertyRetrievalExpression/bpmn:messagePath",
                9);
        assertViolation(result.getViolations().get(0), ERR_MSG, "(//bpmn:dataPath)[1]", 21);
    }

    @Test
    public void testConstraintSuccessCorrelation() throws ValidationException {
        verifyValidResult(createFile("EXT013_success_correlation.bpmn"));
    }

    @Override
    protected String getExtNumber() {
        return "013";
    }
}
