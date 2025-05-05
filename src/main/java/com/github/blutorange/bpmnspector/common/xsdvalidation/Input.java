package com.github.blutorange.bpmnspector.common.xsdvalidation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * Customized LSInput in order to provide access to a resolved resource.
 *
 * <p>Needed for &lt;xs:include&gt;-resolution when generating a {@link javax.xml.validation.Schema} when using
 * getClass().getResourceAsStream("path/to/file.xsd")
 *
 * @author Matthias Geiger
 * @version 1.0
 * @see LSInput
 * @see LSResourceResolver
 * @see ResourceResolver
 */
public class Input implements LSInput {

    private String publicId;

    private String systemId;

    private BufferedInputStream inputStream;

    private static final Logger LOGGER = LoggerFactory.getLogger(Input.class.getSimpleName());

    /**
     * Constructor to generate a customized input source Input using an InputStream
     *
     * @param publicId public ID set by the ResourceResolver
     * @param sysId System ID set by the ResourceResolver
     * @param input InputStream to be used
     */
    public Input(String publicId, String sysId, InputStream input) {
        this.publicId = publicId;
        this.systemId = sysId;
        this.inputStream = new BufferedInputStream(input);
    }

    @Override
    public String getPublicId() {
        return publicId;
    }

    @Override
    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    @Override
    public String getBaseURI() {
        return null;
    }

    @Override
    public InputStream getByteStream() {
        return null;
    }

    @Override
    public boolean getCertifiedText() {
        return false;
    }

    @Override
    public Reader getCharacterStream() {
        return null;
    }

    @Override
    public String getEncoding() {
        return null;
    }

    @Override
    public String getStringData() {
        try {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.debug("Input stream couldn't be converted to String.", e);
            return null;
        }
    }

    @Override
    public void setBaseURI(String baseURI) {
        // not used
    }

    @Override
    public void setByteStream(InputStream byteStream) {
        // not used
    }

    @Override
    public void setCertifiedText(boolean certifiedText) {
        // not used
    }

    @Override
    public void setCharacterStream(Reader characterStream) {
        // not used
    }

    @Override
    public void setEncoding(String encoding) {
        // not used
    }

    @Override
    public void setStringData(String stringData) {
        // not used
    }

    @Override
    public String getSystemId() {
        return systemId;
    }

    @Override
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public BufferedInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(BufferedInputStream inputStream) {
        this.inputStream = inputStream;
    }
}
