package com.github.blutorange.bpmnspector.autofix;

import static com.github.blutorange.bpmnspector.autofix.FixingStrategy.AUTO_FIX;
import static com.github.blutorange.bpmnspector.autofix.FixingStrategy.FIRST_OPTION;

import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector.common.importer.BPMNProcess;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jdom2.Document;

public class ConstraintFixer {
    private final BPMNProcess baseProcess;
    private final Map<Violation, FixingStrategy> foundViolations;
    private final FixerRepository fixerRepository;
    private final FixReportBuilder globalFixReport;

    private final Document documentClone;

    public ConstraintFixer(BPMNProcess processToFix, Map<Violation, FixingStrategy> foundViolations) {
        Objects.requireNonNull(processToFix, "BPMNProcess to fix must not be null");
        Objects.requireNonNull(foundViolations, "List of violations must not be null");

        this.baseProcess = processToFix;
        documentClone = baseProcess.getProcessAsDoc().clone();

        this.foundViolations = foundViolations;

        this.fixerRepository = new FixerRepository();
        this.globalFixReport = new FixReportBuilder();
    }

    public void fixAllPossibleIssues() {
        List<ViolationFixer> usedFixers = foundViolations.entrySet().stream()
                .map(e -> {
                    if (e.getValue() == FixingStrategy.IGNORE) {
                        return List.<ViolationFixer>of();
                    }
                    if (e.getValue() == AUTO_FIX) {
                        return fixerRepository.getAllFixersForConstraint(
                                e.getKey().getConstraint());
                    } else {
                        return fixerRepository
                                .getFixerForConstraintAndStrategy(e.getKey().getConstraint(), e.getValue())
                                .map(List::of)
                                .orElseGet(List::of);
                    }
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());

        for (final var fixer : usedFixers) {
            List<Violation> matchingViolations = foundViolations.entrySet().stream()
                    .filter(e -> e.getKey().getConstraint().equals(fixer.getConstraintId()))
                    .filter(e -> e.getValue() == AUTO_FIX
                            ? fixer.getSupportedStrategy() == FIRST_OPTION
                            : e.getValue().equals(fixer.getSupportedStrategy()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            final var report = fixer.fixIssues(documentClone, matchingViolations);
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

    public FixReportBuilder getGlobalFixReport() {
        return globalFixReport;
    }
}
