package com.github.blutorange.bpmnspector.autofix;

import com.github.blutorange.bpmnspector.api.Violation;
import java.util.List;
import org.jdom2.Document;
import org.slf4j.Logger;

public interface ViolationFixer {

    String getConstraintId();

    FixingStrategy getSupportedStrategy();

    String getDescription();

    Logger getLogger();

    boolean fixSingleViolation(Document processAsDoc, String xPath);

    default FixReport fixIssues(Document docToFix, List<Violation> violationList) {
        if (violationList.isEmpty()) {
            return FixReport.createUnchangedFixReport();
        }

        FixReport report = new FixReport();
        for (Violation singleViolation : violationList) {
            if (!getConstraintId().equals(singleViolation.getConstraint())) {
                getLogger()
                        .warn("Invalid violation for " + getConstraintId() + " fixer, constraint ID is: "
                                + singleViolation.getConstraint());
            }
            if (!singleViolation.getLocation().getXpath().isPresent()) {
                getLogger()
                        .warn("Could not fix " + getConstraintId() + " violation " + singleViolation
                                + ": no XPath present.");
                continue;
            }
            if (fixSingleViolation(
                    docToFix, singleViolation.getLocation().getXpath().get())) {
                report.addFixedViolation(singleViolation);
            }
        }
        return report;
    }
}
