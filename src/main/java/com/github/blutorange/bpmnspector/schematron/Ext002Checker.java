package com.github.blutorange.bpmnspector.schematron;

import com.github.blutorange.bpmnspector.api.Location;
import com.github.blutorange.bpmnspector.api.LocationCoordinate;
import com.github.blutorange.bpmnspector.api.Resource;
import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector.common.importer.BPMNProcess;
import com.github.blutorange.bpmnspector.common.util.ConstantHelper;
import com.github.blutorange.bpmnspector.common.util.ResourceUtils;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom2.Attribute;
import org.jdom2.filter.Filters;
import org.jdom2.located.LocatedElement;
import org.jdom2.xpath.XPathFactory;
import org.jdom2.xpath.XPathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for the check of the EXT.002 constraint
 *
 * @author Philipp Neugebauer
 * @author Matthias Geiger
 * @version 1.0
 */
class Ext002Checker {

    private static final Logger LOGGER = LoggerFactory.getLogger(Ext002Checker.class.getSimpleName());
    private static final String CONSTRAINT_NUMBER = "EXT.002";

    private final XPathFactory xPathFactory = XPathFactory.instance();

    /**
     * checks, if there are violations of the EXT.002 constraint
     *
     * @param process the process to be checked
     * @param validationResult the current validation result of validating process for adding found violations
     */
    public void checkConstraint002(BPMNProcess process, ValidationResultBuilder validationResult) {

        Map<String, Map<String, Attribute>> nspIdMap = new HashMap<>();

        ArrayList<BPMNProcess> allChildren = new ArrayList<>();
        process.getAllProcessesRecursively(allChildren);

        // Add IDs of all processes
        for (BPMNProcess child : allChildren) {
            Map<String, Attribute> idsOfChild = getAllIdAttributesInProcess(child);
            if (nspIdMap.containsKey(child.getNamespace())) {
                // duplicate found create Violation:
                idsOfChild.keySet().stream()
                        .filter(key -> nspIdMap.get(child.getNamespace()).containsKey(key))
                        .forEach(key -> createViolation(
                                nspIdMap.get(child.getNamespace()).get(key), idsOfChild.get(key), validationResult));
            } else {
                nspIdMap.put(child.getNamespace(), idsOfChild);
            }
        }
    }

    private void createViolation(
            Attribute firstAttrib, Attribute secondAttrib, ValidationResultBuilder validationResult) {
        try {
            Resource firstResource = ResourceUtils.determineAndCreateResourceFromString(
                    firstAttrib.getDocument().getBaseURI(), null);

            int file1Line = ((LocatedElement) firstAttrib.getParent()).getLine();
            int file1Column = ((LocatedElement) secondAttrib.getParent()).getColumn();
            String file1XPath = XPathHelper.getAbsolutePath(firstAttrib);

            Resource secondResource = ResourceUtils.determineAndCreateResourceFromString(
                    secondAttrib.getDocument().getBaseURI(), null);
            int file2Line = ((LocatedElement) secondAttrib.getParent()).getLine();
            int file2Column = ((LocatedElement) secondAttrib.getParent()).getColumn();
            String file2XPath = XPathHelper.getAbsolutePath(secondAttrib);

            Location location = new Location(firstResource, new LocationCoordinate(file1Line, file1Column), file1XPath);
            Violation violation = new Violation(location, "Resources have id duplicates", CONSTRAINT_NUMBER);
            validationResult.addViolation(violation);

            Location location2 =
                    new Location(secondResource, new LocationCoordinate(file2Line, file2Column), file2XPath);
            Violation violation2 = new Violation(location2, "Resources have id duplicates", CONSTRAINT_NUMBER);
            validationResult.addViolation(violation2);

            LOGGER.debug("violation of constraint {} found.", CONSTRAINT_NUMBER);
        } catch (ValidationException e) {
            throw new IllegalArgumentException("Invalid baseURI detected.", e);
        }
    }

    private Map<String, Attribute> getAllIdAttributesInProcess(BPMNProcess process) {
        Map<String, Attribute> map = new HashMap<>();
        var expStr = "//bpmn:*/@id";
        org.jdom2.xpath.XPathExpression<Attribute> exp =
                xPathFactory.compile(expStr, Filters.attribute(), null, ConstantHelper.BPMN_NAMESPACE);
        List<Attribute> attributes = exp.evaluate(process.getProcessAsDoc());
        attributes.forEach(x -> map.put(x.getValue(), x));
        return map;
    }
}
