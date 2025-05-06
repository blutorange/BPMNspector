package com.github.blutorange.bpmnspector.schematron;

import com.github.blutorange.bpmnspector.api.Location;
import com.github.blutorange.bpmnspector.api.LocationCoordinate;
import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector.api.Warning;
import com.github.blutorange.bpmnspector.common.importer.BPMNProcess;
import com.github.blutorange.bpmnspector.common.importer.ProcessImporter;
import com.github.blutorange.bpmnspector.common.util.ConstantHelper;
import com.github.blutorange.bpmnspector.schematron.preprocessing.PreProcessor;
import com.github.blutorange.bpmnspector.validation.BpmnProcessValidator;
import com.github.blutorange.bpmnspector.validation.UnsortedValidationResult;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import com.helger.schematron.ISchematronResource;
import com.helger.schematron.pure.SchematronResourcePure;
import com.helger.schematron.pure.xpath.XPathConfig;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.jaxb.DiagnosticReference;
import com.helger.schematron.svrl.jaxb.Dir;
import com.helger.schematron.svrl.jaxb.Emph;
import com.helger.schematron.svrl.jaxb.FailedAssert;
import com.helger.schematron.svrl.jaxb.Span;
import com.helger.schematron.svrl.jaxb.Text;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPathFactoryConfigurationException;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.located.LocatedElement;
import org.jdom2.output.DOMOutputter;
import org.jdom2.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Does the validation process of the xsd and the schematron validation and returns the results of the validation
 *
 * @author Philipp Neugebauer
 * @author Matthias Geiger
 * @version 1.0
 */
public class SchematronBPMNValidator implements BpmnProcessValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchematronBPMNValidator.class.getSimpleName());
    private static final Pattern XPATH_ELEM_NUMBER_REGEX = Pattern.compile("(.*)\\[(\\d*)]");
    private final ProcessImporter bpmnImporter;
    private final Ext002Checker ext002Checker;
    private final PreProcessor preProcessor;
    private final List<ISchematronResource> schemaToCheck;

    public SchematronBPMNValidator() throws ValidationException {
        preProcessor = new PreProcessor();
        bpmnImporter = new ProcessImporter();
        ext002Checker = new Ext002Checker();
        schemaToCheck = loadAndValidateSchematronFiles();
    }

    public ValidationResultBuilder validate(File xmlFile) throws ValidationException {
        final var validationResult = new UnsortedValidationResult();

        // Trying to import process
        final var process = bpmnImporter.importProcessFromPath(Paths.get(xmlFile.getPath()), validationResult);
        if (process != null) {
            validate(process, validationResult);
        }
        return validationResult;
    }

    public void validate(BPMNProcess process, ValidationResultBuilder validationResult) throws ValidationException {

        LOGGER.debug("Validating {}", process.getBaseURI());

        try {
            // EXT.002 checks whether there are ID duplicates - as ID
            // duplicates in a single file are already detected during XSD
            // validation this is only relevant if other processes are imported
            if (!process.getChildren().isEmpty()) {
                ext002Checker.checkConstraint002(process, validationResult);
            }

            final var documentToCheck = preProcessor.preProcess(process);
            final var domOutputter = new DOMOutputter();
            final var w3cDoc = domOutputter.output(documentToCheck);
            final var domSource = new DOMSource(w3cDoc);
            for (final var schematronFile : schemaToCheck) {
                final var schematronOutputType = schematronFile.applySchematronValidationToSVRL(domSource);
                if (schematronOutputType != null) {
                    schematronOutputType.getActivePatternAndFiredRuleAndFailedAssert().stream()
                            .filter(FailedAssert.class::isInstance)
                            .map(FailedAssert.class::cast)
                            .forEach(obj -> handleSchematronErrors(process, validationResult, obj));
                }
            }
        } catch (Exception e) {
            LOGGER.debug("exception during schematron validation", e);
            throw new ValidationException("Something went wrong during schematron validation!");
        }

        LOGGER.debug("Validating process successfully done, file is valid: {}", validationResult.isValid());
    }

    public List<ValidationResultBuilder> validateFiles(List<File> xmlFiles) throws ValidationException {
        final var validationResults = new ArrayList<ValidationResultBuilder>();
        for (final var xmlFile : xmlFiles) {
            validationResults.add(validate(xmlFile));
        }
        return validationResults;
    }

    private XPathConfig createXPathConfig() throws ValidationException {
        try {
            final var xpathFactory = javax.xml.xpath.XPathFactory.newInstance("http://java.sun.com/jaxp/xpath/dom");

            try {
                final var setProperty =
                        javax.xml.xpath.XPathFactory.class.getMethod("setProperty", String.class, String.class);
                setProperty.invoke(xpathFactory, "jdk.xml.xpathExprOpLimit", "600");
                setProperty.invoke(xpathFactory, "jdk.xml.xpathExprGrpLimit", "30");
            } catch (final NoSuchMethodException e) {
                // Available only since JDK 18
                // For earlier versions, you must set this globally for the JVM
                final var exprOpLimit = getNumericalSystemProperty("jdk.xml.xpathExprOpLimit", 100);
                final var exprGrpLimit = getNumericalSystemProperty("jdk.xml.xpathExprGrpLimit", 10);
                if (exprOpLimit < 600) {
                    throw new ValidationException(
                            "The system property 'jdk.xml.xpathExprOpLimit' must be at least 600");
                }
                if (exprGrpLimit < 30) {
                    throw new ValidationException(
                            "The system property 'jdk.xml.xpathExprGrpLimit' must be at least 30");
                }
            }
            xpathFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            return new XPathConfig(xpathFactory, null, null);
        } catch (final XPathFactoryConfigurationException | ReflectiveOperationException e) {
            throw new ValidationException("Could not create XPathFactory", e);
        }
    }

    private int getNumericalSystemProperty(String name, int defaultValue) {
        final var value = System.getProperty(name);
        try {
            return value != null && !value.isEmpty() ? Integer.parseInt(value) : defaultValue;
        } catch (final NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * tries to locate errors in the specific files
     *
     * @param baseProcess the file where the error must be located
     * @param validationResult the result of the validation to which to add newly found errors
     * @param failedAssert the error of the schematron validation
     */
    private void handleSchematronErrors(
            BPMNProcess baseProcess, ValidationResultBuilder validationResult, FailedAssert failedAssert) {
        String message = textToString(SVRLHelper.getText(failedAssert)).trim();
        var constraint = message.substring(0, message.indexOf('|'));
        var errorMessage = message.substring(message.indexOf('|') + 1);

        Location violationLocation = null;

        String location = failedAssert.getLocation();

        LOGGER.debug("XPath to evaluate: {}", location);
        XPathFactory fac = XPathFactory.instance();
        Element foundElem = fac.compile(location, Filters.element(), null, ConstantHelper.BPMN_NAMESPACE)
                .evaluateFirst(baseProcess.getProcessAsDoc());

        String logText;
        if (foundElem != null) {
            int line = ((LocatedElement) foundElem).getLine();
            int column = ((LocatedElement) foundElem).getColumn();
            String fileName = baseProcess.getBaseURI();
            violationLocation =
                    new Location(Paths.get(fileName).toAbsolutePath(), new LocationCoordinate(line, column), location);
            logText = String.format(
                    "violation of constraint %s found in %s at line %s.",
                    constraint,
                    violationLocation.getResource(),
                    violationLocation.getLocation().getRow());
        } else {
            try {
                var xpathId = "";
                if (failedAssert.getDiagnosticReferenceOrPropertyReferenceOrTextCount() > 0) {
                    final var firstId = SVRLHelper.getAllDiagnosticReferences(failedAssert).stream()
                            .map(DiagnosticReference::getContent)
                            .flatMap(Collection::stream)
                            .filter(Text.class::isInstance)
                            .map(Text.class::cast)
                            .map(SchematronBPMNValidator::textToString)
                            .findFirst()
                            .orElse("");
                    xpathId = firstId.trim();
                }
                LOGGER.debug("Trying to locate in files: {}", xpathId);
                violationLocation = searchForViolationFile(xpathId, baseProcess);
                logText = String.format(
                        "violation of constraint %s found in %s at line %s.",
                        constraint,
                        violationLocation.getResource(),
                        violationLocation.getLocation().getRow());
            } catch (ValidationException | StringIndexOutOfBoundsException e) {
                LOGGER.error("Line of affected Element could not be determined.", e);
                logText = String.format(
                        "Found violation of constraint %s but the correct location could not be determined.",
                        constraint);
            }
        }

        LOGGER.debug(logText);
        if ("EXT.076".equals(constraint)) {
            Warning warning = new Warning(errorMessage, violationLocation);
            validationResult.addWarning(warning);
        } else {
            Violation violation = new Violation(violationLocation, errorMessage, constraint);
            validationResult.addViolation(violation);
        }
    }

    private List<ISchematronResource> loadAndValidateSchematronFiles() throws ValidationException {
        final var schemasToCheck = new ArrayList<ISchematronResource>();

        final var xpathConfig = createXPathConfig();

        final var schematronSchemaDescriptive =
                SchematronResourcePure.fromClassPath("com/github/blutorange/bpmnspector/resources/EXT_descriptive.xml");
        schematronSchemaDescriptive.setXPathConfig(xpathConfig);
        if (!schematronSchemaDescriptive.isValidSchematron()) {
            LOGGER.debug("schematron file for Descriptive Conformance class is invalid");
            throw new ValidationException("Invalid Schematron file (EXT_descriptive.xml)!");
        } else {
            schemasToCheck.add(schematronSchemaDescriptive);
        }

        final var schematronSchemaAnalytic =
                SchematronResourcePure.fromClassPath("com/github/blutorange/bpmnspector/resources/EXT_analytic.xml");
        schematronSchemaAnalytic.setXPathConfig(xpathConfig);
        if (!schematronSchemaAnalytic.isValidSchematron()) {
            LOGGER.debug("schematron file for Analytic Conformance class is invalid");
            throw new ValidationException("Invalid Schematron file (EXT_analytic.xml)!");
        } else {
            schemasToCheck.add(schematronSchemaAnalytic);
        }

        final var schematronSchemaCommonExec =
                SchematronResourcePure.fromClassPath("com/github/blutorange/bpmnspector/resources/EXT_commonExec.xml");
        schematronSchemaCommonExec.setXPathConfig(xpathConfig);
        if (!schematronSchemaCommonExec.isValidSchematron()) {
            LOGGER.debug("schematron file for Common Executable Conformance class is invalid");
            throw new ValidationException("Invalid Schematron file (EXT_commonExec.xml)!");
        } else {
            schemasToCheck.add(schematronSchemaCommonExec);
        }

        final var schematronSchemaFull =
                SchematronResourcePure.fromClassPath("com/github/blutorange/bpmnspector/resources/EXT_full.xml");
        schematronSchemaFull.setXPathConfig(xpathConfig);
        if (!schematronSchemaFull.isValidSchematron()) {
            LOGGER.debug("schematron file for Full Conformance class is invalid");
            throw new ValidationException("Invalid Schematron file (EXT_full.xml)!");
        } else {
            schemasToCheck.add(schematronSchemaFull);
        }
        return schemasToCheck;
    }

    /**
     * searches for the file and line, where the violation occurred
     *
     * @param xpathExpression the expression, through which the file and line should be identified
     * @param baseProcess baseProcess used for validation
     * @return the violation Location
     * @throws ValidationException if no element can be found
     */
    private Location searchForViolationFile(String xpathExpression, BPMNProcess baseProcess)
            throws ValidationException {

        var namespacePrefix = xpathExpression.substring(0, xpathExpression.indexOf('_'));

        Optional<BPMNProcess> optional = baseProcess.findProcessByGeneratedPrefix(namespacePrefix);
        if (optional.isPresent()) {
            String fileName = optional.get().getBaseURI();

            var line = -1;
            var column = -1;

            // use ID with generated prefix for lookup
            var xpathObjectId = createIdBpmnExpression(xpathExpression);

            LOGGER.debug("Expression to evaluate: {}", xpathObjectId);
            XPathFactory fac = XPathFactory.instance();
            List<Element> elems = fac.compile(xpathObjectId, Filters.element(), null, ConstantHelper.BPMN_NAMESPACE)
                    .evaluate(optional.get().getProcessAsDoc());

            if (elems.size() == 1) {
                line = ((LocatedElement) elems.get(0)).getLine();
                column = ((LocatedElement) elems.get(0)).getColumn();
                // use ID without prefix  (=original ID) as Violation xPath
                xpathObjectId = createIdBpmnExpression(xpathExpression.substring(xpathExpression.indexOf('_') + 1));
            }

            if (line == -1 || column == -1) {
                throw new ValidationException("BPMN Element couldn't be found in file '" + fileName + "'!");
            }

            return new Location(
                    Paths.get(fileName).toAbsolutePath(), new LocationCoordinate(line, column), xpathObjectId);

        } else {
            // File not found
            throw new ValidationException("BPMN Element couldn't be found as no corresponding file could be found.");
        }
    }

    /**
     * creates a xpath expression for finding the id
     *
     * @param id the id, to which the expression should refer
     * @return the xpath expression, which refers the given id
     */
    private static String createIdBpmnExpression(String id) {
        return String.format("//bpmn:*[@id = '%s']", id);
    }

    private static String textToString(Text text) {
        if (text == null) {
            return "";
        }
        final var sb = new StringBuilder();
        for (final var content : text.getContent()) {
            if (content instanceof String) {
                sb.append(content);
            } else if (content instanceof Span) {
                sb.append(((Span) content).getContent());
            } else if (content instanceof Emph) {
                sb.append(((Emph) content).getContent());
            } else if (content instanceof Dir) {
                sb.append(((Dir) content).getContent());
            }
        }
        return sb.toString();
    }
}
