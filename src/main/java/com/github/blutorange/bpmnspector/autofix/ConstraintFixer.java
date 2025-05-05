package com.github.blutorange.bpmnspector.autofix;

import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector.common.importer.BPMNProcess;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jdom2.Document;

public class ConstraintFixer {
    private final BPMNProcess baseProcess;
    private final Map<Violation, FixingStrategy> foundViolations;
    private final FixerRepository fixerRepository;
    private final FixReport globalFixReport;

    private final Document documentClone;

    public ConstraintFixer(BPMNProcess processToFix, Map<Violation, FixingStrategy> foundViolations) {
        Objects.requireNonNull(processToFix, "BPMNProcess to fix must not be null");
        Objects.requireNonNull(foundViolations, "List of violations must not be null");

        this.baseProcess = processToFix;
        documentClone = baseProcess.getProcessAsDoc().clone();

        this.foundViolations = foundViolations;

        this.fixerRepository = new FixerRepository();
        this.globalFixReport = new FixReport();
    }

    public void fixAllPossibleIssues() {
        List<ViolationFixer> usedFixers = foundViolations.entrySet().stream()
                .filter(e -> fixerRepository
                        .getFixerForConstraintAndStrategy(e.getKey().getConstraint(), e.getValue())
                        .isPresent())
                .map(e -> fixerRepository
                        .getFixerForConstraintAndStrategy(e.getKey().getConstraint(), e.getValue())
                        .orElseThrow())
                .collect(Collectors.toList());

        Map<ViolationFixer, List<Violation>> fixerViolationMap = new HashMap<>();
        for (ViolationFixer fixer : usedFixers) {
            List<Violation> matchingViolations = foundViolations.entrySet().stream()
                    .filter(e -> e.getKey().getConstraint().equals(fixer.getConstraintId()))
                    .filter(e -> e.getValue().equals(fixer.getSupportedStrategy()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            fixerViolationMap.put(fixer, matchingViolations);
            FixReport report = fixer.fixIssues(documentClone, matchingViolations);
            report.getFixedViolations().forEach(globalFixReport::addFixedViolation);
        }
    }

    public BPMNProcess fixAllPossibleIssuesAndReturnProcess() {
        fixAllPossibleIssues();
        return getFixedProcess();
    }

    public BPMNProcess getFixedProcess() {
        return new BPMNProcess(documentClone, baseProcess.getBaseURI(), baseProcess.getNamespace());
    }

    public FixReport getGlobalFixReport() {
        return globalFixReport;
    }
}
