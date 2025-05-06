package com.github.blutorange.bpmnspector.common.importer;

import com.github.blutorange.bpmnspector.api.Location;
import com.github.blutorange.bpmnspector.api.LocationCoordinate;
import com.github.blutorange.bpmnspector.api.Resource;
import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.api.Violation;
import com.github.blutorange.bpmnspector.api.Warning;
import com.github.blutorange.bpmnspector.common.util.ConstantHelper;
import com.github.blutorange.bpmnspector.validation.ValidationResultBuilder;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import org.apache.commons.io.IOUtils;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.located.LocatedElement;
import org.jdom2.located.LocatedJDOMFactory;
import org.jdom2.xpath.XPathHelper;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * @author Matthias Geiger
 * @version 1.0
 */
public class ProcessImporter {

    private final SAXBuilder builder;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ProcessImporter.class.getSimpleName());

    public ProcessImporter() {
        builder = new SAXBuilder();
        builder.setJDOMFactory(new LocatedJDOMFactory());
    }

    public BPMNProcess importProcessFromPath(Path path, ValidationResultBuilder result) throws ValidationException {
        return importProcessFromPath(path, result, true);
    }

    public BPMNProcess importProcessFromPath(Path path, ValidationResultBuilder result, boolean removeDI)
            throws ValidationException {
        if (Files.notExists(path) || !Files.isRegularFile(path)) {
            final var msg = "BPMNProcess cannot be created: Path " + path + " is invalid.";
            throw new ValidationException(msg);
        }
        final var resource = new Resource(path);
        return importProcessRecursively(resource, null, null, result, removeDI);
    }

    public BPMNProcess importProcessFromStreamSource(InputStream src, String resourceName) throws ValidationException {
        return importProcessFromStreamSource(src, resourceName, true);
    }

    public BPMNProcess importProcessFromStreamSource(InputStream src, String resourceName, boolean removeDI)
            throws ValidationException {
        try {
            final var resource = new Resource(resourceName);
            final var streamContent = IOUtils.toByteArray(src);
            final var processAsDoc = builder.build(new ByteArrayInputStream(streamContent), resource.getResourceName());

            if ("definitions".equals(processAsDoc.getRootElement().getName())
                    && ConstantHelper.BPMN_NAMESPACE_STRING.equals(
                            processAsDoc.getRootElement().getNamespaceURI())) {
                final var processNamespace = processAsDoc.getRootElement().getAttributeValue("targetNamespace");

                final var process = new BPMNProcess(processAsDoc, resourceName, processNamespace, null);

                if (removeDI) {
                    // remove BPMNDI information
                    processAsDoc.getRootElement().removeChildren("BPMNDiagram", getBPMNDINamespace());
                }

                return process;
            } else {
                // Invalid BPMN file
                return null;
            }
        } catch (JDOMException | IOException e) {
            throw new ValidationException(
                    "Creation of BPMNProcess for stream (" + resourceName + ") object failed.", e);
        }
    }

    private BPMNProcess importProcessRecursively(
            Resource resource,
            BPMNProcess parent,
            BPMNProcess rootProcess,
            ValidationResultBuilder result,
            boolean removeDI)
            throws ValidationException {
        result.addResource(resource);
        try (final var stream = openStreamToResource(resource)) {
            try {
                final var streamContent = IOUtils.toByteArray(stream);
                final var processAsDoc =
                        builder.build(new ByteArrayInputStream(streamContent), resource.getResourceName());
                if ("definitions".equals(processAsDoc.getRootElement().getName())
                        && ConstantHelper.BPMN_NAMESPACE_STRING.equals(
                                processAsDoc.getRootElement().getNamespaceURI())) {
                    final var processNamespace = processAsDoc.getRootElement().getAttributeValue("targetNamespace");
                    final var process =
                            new BPMNProcess(processAsDoc, resource.getResourceName(), processNamespace, parent);

                    if (removeDI) {
                        // remove BPMNDI information
                        processAsDoc.getRootElement().removeChildren("BPMNDiagram", getBPMNDINamespace());
                    }

                    resolveAndAddImports(process, Objects.requireNonNullElse(rootProcess, process), result, removeDI);
                    return process;
                } else {
                    // Invalid BPMN file
                    return null;
                }
            } catch (ValidationException e) {
                // Thrown if file is not well-formed or does not have claimed encoding error is already logged and
                // added to the validation result - but further processing is not
                // possible
                LOGGER.debug("caught: {}", String.valueOf(e));
                return null;
            } catch (JDOMException | IOException e) {
                throw new ValidationException(
                        "Creation of BPMNProcess for file " + resource.getResourceName() + " object failed.", e);
            }
        } catch (IOException e) {
            throw new ValidationException("Failure accessing BPMN process " + resource.getResourceName() + ".", e);
        }
    }

    private void resolveAndAddImports(
            BPMNProcess process, BPMNProcess rootProcess, ValidationResultBuilder result, boolean removeDI)
            throws ValidationException {

        final var importElements = process.getProcessAsDoc().getRootElement().getChildren("import", getBPMNNamespace());

        for (final var elem : importElements) {
            final var importType = elem.getAttributeValue("importType");

            // fail fast if import type is not supported
            if (!(ConstantHelper.BPMN_NAMESPACE_STRING.equals(importType)
                    || ConstantHelper.WSDL2_NAMESPACE.equals(importType)
                    || ConstantHelper.XSD_NAMESPACE.equals(importType))) {

                final var line = ((LocatedElement) elem).getLine();
                final var column = ((LocatedElement) elem).getColumn();
                final var xpath = XPathHelper.getAbsolutePath(elem);
                final var loc =
                        new Location(Paths.get(process.getBaseURI()), new LocationCoordinate(line, column), xpath);
                result.addWarning(new Warning(
                        "The import type '" + importType + "' is not supported. Import will be ignored.", loc));
                return;
            }

            final var location = elem.getAttributeValue("location");

            Resource resource = null;
            // determine whether an absolute URL or a file is used and create corresponding Resource
            try {

                var importUri = new URI(location);

                if (importUri.isAbsolute()
                        && importUri.getScheme().toLowerCase().startsWith("http")) {
                    // process as URL
                    final var asURL = importUri.toURL();
                    resource = new Resource(asURL);
                } else {
                    // process as file
                    final var decodedUrlString = URLDecoder.decode(importUri.toString(), StandardCharsets.UTF_8);
                    var importPath = Paths.get(decodedUrlString);
                    if (!importPath.isAbsolute()) {
                        // resolve relative path based on the baseURI from the process
                        importPath = Paths.get(process.getBaseURI())
                                .getParent()
                                .resolve(importPath)
                                .normalize()
                                .toAbsolutePath();
                    }

                    if (Files.notExists(importPath) || !Files.isRegularFile(importPath)) {
                        String msg = "Import could not be resolved: Path " + elem.getAttributeValue("location")
                                + " is invalid.";
                        Violation violation = createViolation(process, elem, msg);
                        result.addViolation(violation);
                    } else {
                        resource = new Resource(importPath);
                    }
                }
            } catch (URISyntaxException | MalformedURLException e) {
                var msg = "Import could not be resolved: Path " + location + " is invalid.";
                Violation violation = createViolation(process, elem, msg);
                result.addViolation(violation);
            }

            if (resource != null) {
                switch (importType) {
                    case ConstantHelper.BPMN_NAMESPACE_STRING:
                        if (!isFileAlreadyImported(resource.getResourceName(), rootProcess)) {
                            try {
                                final var importedProcess =
                                        importProcessRecursively(resource, process, rootProcess, result, removeDI);

                                if (importedProcess != null) {
                                    process.getChildren().add(importedProcess);
                                }
                            } catch (ValidationException e) {
                                result.addViolation(createViolation(process, elem, e.getMessage()));
                            }
                        }
                        break;
                    case ConstantHelper.WSDL2_NAMESPACE:
                        try (final var stream = openStreamToResource(resource)) {

                            result.addResource(resource);

                            process.getWsdls().add(builder.build(stream));
                        } catch (ValidationException e) {
                            // Creation of stream failed object could not be found
                            result.addViolation(createViolation(process, elem, e.getMessage()));
                        } catch (IOException | JDOMException e) {
                            throw new ValidationException(
                                    "WSDL validation of file " + resource.getResourceName() + " failed.", e);
                        }
                        break;
                    case ConstantHelper.XSD_NAMESPACE:
                        try (final var stream = openStreamToResource(resource)) {
                            result.addResource(resource);
                            final var schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                            schemaFactory.newSchema(new StreamSource(stream));
                        } catch (ValidationException e) {
                            // Creation of stream failed object could not be found
                            result.addViolation(createViolation(process, elem, e.getMessage()));
                        } catch (SAXException e) {
                            final var msg = "File " + resource.getResourceName() + " is not a valid XSD file.";
                            result.addViolation(createViolation(process, elem, msg));
                        } catch (IOException e) {
                            throw new ValidationException(
                                    "XSD file check for  " + resource.getResourceName() + " failed.", e);
                        }
                        break;
                }
            }
        }
    }

    private Namespace getBPMNNamespace() {
        return Namespace.getNamespace(ConstantHelper.BPMN_NAMESPACE_STRING);
    }

    private Namespace getBPMNDINamespace() {
        return Namespace.getNamespace(ConstantHelper.BPMNDI_NAMESPACE);
    }

    private Violation createViolation(BPMNProcess parent, Element importElement, String msg) {
        final var line = ((LocatedElement) importElement).getLine();
        final var column = ((LocatedElement) importElement).getColumn();
        final var xpath = XPathHelper.getAbsolutePath(importElement);

        final var location = new Location(Paths.get(parent.getBaseURI()), new LocationCoordinate(line, column), xpath);
        return new Violation(location, msg, "EXT.001");
    }

    private boolean isFileAlreadyImported(String baseURI, BPMNProcess process) {
        if (process.getBaseURI().equals(baseURI)) {
            return true;
        } else {
            for (final var child : process.getChildren()) {
                if (isFileAlreadyImported(baseURI, child)) {
                    return true;
                }
            }
        }
        return false;
    }

    private InputStream openStreamToResource(Resource resource) throws ValidationException, IOException {

        if (resource.getType() == Resource.ResourceType.URL) {
            LOGGER.debug("Trying to openStream to: {}", resource.getResourceName());
            try {
                return resource.getUrl().orElseThrow().openConnection().getInputStream();
            } catch (UnknownHostException e) {
                throw new ValidationException("Host " + e.getMessage() + " is unknown.", e);
            } catch (FileNotFoundException e) {
                throw new ValidationException("File cannot be resolved from URL: " + e.getMessage(), e);
            }
        } else if (resource.getType() == Resource.ResourceType.FILE) {
            return new FileInputStream(resource.getPath().orElseThrow().toFile());
        } else {
            throw new IllegalArgumentException(
                    "Import processing of resource type " + resource.getType() + " is not supported.");
        }
    }
}
