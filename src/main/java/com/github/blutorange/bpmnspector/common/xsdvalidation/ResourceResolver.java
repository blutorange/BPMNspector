package com.github.blutorange.bpmnspector.common.xsdvalidation;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * Class for representing a custom LSResourceResolver in order to enable resource resolution for BPMN XSD Validation.
 *
 * <p>Needed for &lt;xs:include&gt;-resolution when generating a {@link javax.xml.validation.Schema} when using
 * getClass().getResourceAsStream("path/to/file.xsd")
 *
 * @author Matthias Geiger
 * @version 1.0
 * @see LSResourceResolver
 */
public class ResourceResolver implements LSResourceResolver {
    @Override
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {

        var resourceAsStream = this.getClass().getResourceAsStream("/" + systemId);

        return new Input(publicId, systemId, resourceAsStream);
    }
}
