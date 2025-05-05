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
                        .warn(
                                "Invalid violation for {} fixer, constraint ID is: {}",
                                getConstraintId(),
                                singleViolation.getConstraint());
            }
            if (singleViolation.getLocation().getXpath().isEmpty()) {
                getLogger()
                        .warn("Could not fix {} violation {}: no XPath present.", getConstraintId(), singleViolation);
                continue;
            }
            if (fixSingleViolation(docToFix, singleViolation.getLocation().getXpath())) {
                report.addFixedViolation(singleViolation);
            }
        }
        return report;
    }
}
