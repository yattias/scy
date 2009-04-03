package eu.scy.colemo.client;

import junit.framework.TestCase;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import eu.scy.colemo.server.uml.UmlClass;
import eu.scy.colemo.server.uml.UmlLink;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.apr.2009
 * Time: 06:35:53
 * To change this template use File | Settings | File Templates.
 */
public class ConceptMapLoaderTest extends TestCase {


    private String xmlContents;
    private Collection nodes = null;
    private Collection links = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        nodes = new LinkedList();
        links = new LinkedList();

        UmlClass umlClassA = new UmlClass("Concept A", "c", "Henrik");
        umlClassA.setX(200);
        umlClassA.setY(300);
        umlClassA.setId("1");

        UmlClass umlClassB = new UmlClass("Concept B", "c", "Henrik");
        umlClassB.setX(200);
        umlClassB.setY(300);
        umlClassB.setId("2");

        UmlLink link = new UmlLink(umlClassA.getId(), umlClassB.getId(), "Henrik");

        
        ConceptNode nodea = new ConceptNode(umlClassA);
        ConceptNode nodeb = new ConceptNode(umlClassB);

        nodes.add(nodea);
        nodes.add(nodeb);

        ConceptLink conceptLink = new ConceptLink(link);
        links.add(conceptLink);

        ConceptMapExporter.getDefaultInstance().initialize();
        xmlContents = ConceptMapExporter.getDefaultInstance().createXML(nodes.iterator(), links.iterator());
    }

    private Iterator getNodeIterator() {
        return nodes.iterator();
    }

    private Iterator getLinkIterator() {
        return links.iterator();
    }

    

    public void testCreateXMLContents() {
        assertNotNull(xmlContents);

    }


    public void testParseConcepts() {
        List concepts = ConceptMapLoader.getDefaultInstance().parseConcepts(xmlContents);
        assertEquals(concepts.size(), nodes.size());
    }

    public void testParseLinks() {
        List links = ConceptMapLoader.getDefaultInstance().parseLinks(xmlContents);
        assertEquals(links.size(), this.links.size());
    }






}
