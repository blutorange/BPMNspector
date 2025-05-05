package com.github.blutorange.bpmnspector_test.refcheck.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.blutorange.bpmnspector.api.ValidationException;
import com.github.blutorange.bpmnspector.refcheck.BPMNElement;
import com.github.blutorange.bpmnspector.refcheck.Reference;
import com.github.blutorange.bpmnspector.refcheck.ReferenceLoader;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 *
 * @author Matthias Geiger
 * @version 1.0
 */
public class ReferenceLoaderTest {
    private static final ReferenceLoader referenceLoader = new ReferenceLoader();

    @Test
    public void loadAllReferences() throws ValidationException {
        referenceLoader.load(
                "/com/github/blutorange/bpmnspector/resources/references.xml",
                "/com/github/blutorange/bpmnspector/resources/references.xsd");
    }

    @Test
    public void loadNonExistentFile() {
        var e = assertThrows(
                ValidationException.class,
                () -> referenceLoader.load(
                        "NON_EXISTING", "/com/github/blutorange/bpmnspector/resources/references.xsd"));
        assertEquals("Problems occurred while traversing the file 'NON_EXISTING'", e.getMessage());
    }

    @Test
    public void useNonExistentXsd() {
        var e = assertThrows(
                ValidationException.class,
                () -> referenceLoader.load(
                        "/com/github/blutorange/bpmnspector/resources/references.xml", "NON_EXISTING"));
        assertEquals(
                "Problems occurred while trying to check the references XML file against the corresponding XSD file.",
                e.getMessage());
    }

    @Test
    public void testCorrectnessAssociation() throws ValidationException {
        Map<String, BPMNElement> refs = referenceLoader.load(
                "/com/github/blutorange/bpmnspector/resources/references.xml",
                "/com/github/blutorange/bpmnspector/resources/references.xsd");
        assertTrue(refs.containsKey("association"));
        List<Reference> referenceList = refs.get("association").getReferences();
        assertEquals(2, referenceList.size());
        assertEquals("sourceRef", referenceList.get(0).getName());
        assertTrue(referenceList.get(0).isAttribute());
        assertTrue(referenceList.get(0).isQname());
        assertEquals("targetRef", referenceList.get(1).getName());
        assertTrue(referenceList.get(1).isAttribute());
        assertTrue(referenceList.get(1).isQname());
    }

    @Test
    public void testCorrectnessDataObject() throws ValidationException {
        Map<String, BPMNElement> refs = referenceLoader.load(
                "/com/github/blutorange/bpmnspector/resources/references.xml",
                "/com/github/blutorange/bpmnspector/resources/references.xsd");
        assertTrue(refs.containsKey("dataObject"));
        List<Reference> referenceList = refs.get("dataObject").getReferences();
        assertEquals(2, referenceList.size());
        assertEquals("categoryValueRef", referenceList.get(0).getName());
        assertFalse(referenceList.get(0).isAttribute());
        assertTrue(referenceList.get(0).isQname());
        assertEquals("itemSubjectRef", referenceList.get(1).getName());
        assertTrue(referenceList.get(1).isAttribute());
        assertTrue(referenceList.get(1).isQname());
    }

    @Test
    public void testCorrectnessGateway() throws ValidationException {
        Map<String, BPMNElement> refs = referenceLoader.load(
                "/com/github/blutorange/bpmnspector/resources/references.xml",
                "/com/github/blutorange/bpmnspector/resources/references.xsd");
        assertTrue(refs.containsKey("gateway"));
        List<Reference> referenceList = refs.get("gateway").getReferences();
        assertEquals(3, referenceList.size());
        assertEquals("categoryValueRef", referenceList.get(0).getName());
        assertFalse(referenceList.get(0).isAttribute());
        assertTrue(referenceList.get(0).isQname());
        assertEquals("incoming", referenceList.get(1).getName());
        assertFalse(referenceList.get(1).isAttribute());
        assertTrue(referenceList.get(1).isQname());
        assertEquals("outgoing", referenceList.get(2).getName());
        assertFalse(referenceList.get(2).isAttribute());
        assertTrue(referenceList.get(2).isQname());
    }
}
