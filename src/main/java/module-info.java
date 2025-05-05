module com.github.blutorange.bpmnspector {
    requires com.helger.commons;
    requires com.helger.schematron;
    requires com.helger.schematron.pure;
    requires java.xml;
    requires org.apache.commons.io;
    requires org.jdom2;
    requires org.slf4j;

    opens com.github.blutorange.bpmnspector.resources;

    exports com.github.blutorange.bpmnspector.autofix to
            com.github.blutorange.bpmnspector_test;
    exports com.github.blutorange.bpmnspector.common.importer to
            com.github.blutorange.bpmnspector_test;
    exports com.github.blutorange.bpmnspector.common.util to
            com.github.blutorange.bpmnspector_test;
    exports com.github.blutorange.bpmnspector.common.xsdvalidation to
            com.github.blutorange.bpmnspector_test;
    exports com.github.blutorange.bpmnspector.refcheck to
            com.github.blutorange.bpmnspector_test;
    exports com.github.blutorange.bpmnspector.schematron to
            com.github.blutorange.bpmnspector_test;
    exports com.github.blutorange.bpmnspector.schematron.preprocessing to
            com.github.blutorange.bpmnspector_test;
    exports com.github.blutorange.bpmnspector.validation to
            com.github.blutorange.bpmnspector_test;
    exports com.github.blutorange.bpmnspector.api;
}
