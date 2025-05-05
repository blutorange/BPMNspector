package com.github.blutorange.bpmnspector.common.util;

import org.jdom2.Namespace;

/**
 * This class provides the constants used in the whole project
 *
 * @author Philipp Neugebauer
 * @version 1.0
 */
public class ConstantHelper {

    public static final String PI_NAMESPACE = "http://www.uniba.de/pi/bpmn-cons/validation";
    public static final String BPMN_NAMESPACE_STRING = "http://www.omg.org/spec/BPMN/20100524/MODEL";
    public static final String BPMNDI_NAMESPACE = "http://www.omg.org/spec/BPMN/20100524/DI";
    public static final String WSDL2_NAMESPACE = "http://www.w3.org/TR/wsdl20/";
    public static final String XSD_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
    public static final String XSI_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";

    public static final Namespace BPMN_NAMESPACE = Namespace.getNamespace("bpmn", BPMN_NAMESPACE_STRING);
}
