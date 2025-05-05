module com.github.blutorange.bpmnspector_test {
    requires com.github.blutorange.bpmnspector;
    requires org.junit.jupiter.api;
    requires org.jdom2;

    opens com.github.blutorange.bpmnspector_test.schematron to
            org.junit.platform.commons;
    opens com.github.blutorange.bpmnspector_test.schematron.artifacts to
            org.junit.platform.commons;
    opens com.github.blutorange.bpmnspector_test.schematron.artifacts.sequence_flow to
            org.junit.platform.commons;
    opens com.github.blutorange.bpmnspector_test.schematron.analytic to
            org.junit.platform.commons;
    opens com.github.blutorange.bpmnspector_test.schematron.commonExec to
            org.junit.platform.commons;
    opens com.github.blutorange.bpmnspector_test.schematron.descriptive to
            org.junit.platform.commons;
    opens com.github.blutorange.bpmnspector_test.schematron.full to
            org.junit.platform.commons;
    opens com.github.blutorange.bpmnspector_test.autofix to
            org.junit.platform.commons;
    opens com.github.blutorange.bpmnspector_test.api.tests to
            org.junit.platform.commons;
    opens com.github.blutorange.bpmnspector_test.common.importer to
            org.junit.platform.commons;
    opens com.github.blutorange.bpmnspector_test.refcheck.tests to
            org.junit.platform.commons;
    opens com.github.blutorange.bpmnspector_test.xsdvalidation.tests to
            org.junit.platform.commons;
}
