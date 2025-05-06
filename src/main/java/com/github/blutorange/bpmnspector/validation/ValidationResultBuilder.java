package com.github.blutorange.bpmnspector.validation;

import static java.util.stream.Collectors.toMap;

import com.github.blutorange.bpmnspector.api.AutoFixOptions;
import com.github.blutorange.bpmnspector.api.FixReport;
import com.github.blutorange.bpmnspector.api.Resource;
import com.github.blutorange.bpmnspector.api.ValidationResult;
import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector.api.Warning;
import com.github.blutorange.bpmnspector.autofix.ConstraintFixer;
import com.github.blutorange.bpmnspector.autofix.FixingStrategy;
import com.github.blutorange.bpmnspector.common.importer.BPMNProcess;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Map;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/** The result of a validation process with violations and warnings, plus methods to add data. */
public abstract class ValidationResultBuilder implements ValidationResult {
    private BPMNProcess process;

    public abstract void addFile(Path s);

    public abstract void addResource(Resource resource);

    public abstract void addViolation(Violation violation);

    public abstract void addWarning(Warning warning);

    @Override
    public FixReport<String> autoFix(AutoFixOptions options) {
        if (process == null) {
            throw new IllegalStateException("Cannot auto-fix, process was never read or set");
        }
        final var fixer = doFix();
        final var outputter = createXmlOutputter(options);
        final var xml = outputter.outputString(fixer.getFixedProcess().getProcessAsDoc());
        return new DefaultFixReport<>(xml, fixer.getGlobalFixReport().getFixedViolations());
    }

    @Override
    public FixReport<Void> autoFix(AutoFixOptions options, OutputStream output) throws IOException {
        if (process == null) {
            throw new IllegalStateException("Cannot auto-fix, process was never read or set");
        }
        final var fixer = doFix();
        final var outputter = createXmlOutputter(options);
        outputter.output(fixer.getFixedProcess().getProcessAsDoc(), output);
        return new DefaultFixReport<>(null, fixer.getGlobalFixReport().getFixedViolations());
    }

    @Override
    public FixReport<Void> autoFix(AutoFixOptions options, Writer writer) throws IOException {
        if (process == null) {
            throw new IllegalStateException("Cannot auto-fix, process was never read or set");
        }
        final var fixer = doFix();
        final var outputter = createXmlOutputter(options);
        outputter.output(fixer.getFixedProcess().getProcessAsDoc(), writer);
        return new DefaultFixReport<>(null, fixer.getGlobalFixReport().getFixedViolations());
    }

    public final void setBpmnProcess(BPMNProcess process) {
        this.process = process;
    }

    private XMLOutputter createXmlOutputter(AutoFixOptions options) {
        final var outputter = new XMLOutputter();
        if (options.isPrettyPrint()) {
            outputter.setFormat(Format.getPrettyFormat());
        } else {
            outputter.setFormat(Format.getCompactFormat());
        }
        return outputter;
    }

    private ConstraintFixer doFix() {
        final var fixes = getViolations().stream()
                .map(v -> Map.entry(v, FixingStrategy.AUTO_FIX))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        final var fixer = new ConstraintFixer(process, fixes);
        fixer.fixAllPossibleIssues();
        return fixer;
    }
}
