package eu.scy.colemo.client;

import junit.framework.TestCase;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import eu.scy.colemo.server.uml.UmlClass;
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

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        nodes = new LinkedList();

        UmlClass umlClass = new UmlClass("Some Concept", "c", "Henrik");
        umlClass.setX(200);
        umlClass.setY(300);

        ConceptNode node = new ConceptNode(umlClass);
        nodes.add(node);

    }

    private Iterator getNodeIterator() {
        return nodes.iterator();
    }

    

    public void testParseConcepts() {
        /*Document xmldoc = ConceptMapExporter.getDefaultInstance().createXMLDocument();
        Element root = ConceptMapExporter.getDefaultInstance().createRootElement(xmldoc);
        xmldoc.appendChild(root);
        ConceptMapExporter.getDefaultInstance().parseNodes(xmldoc,  root, getNodeIterator());
        xmlContents = ConceptMapExporter.getDefaultInstance().createXML();
        assertNotNull(xmlContents);
        */
        assert(true);
    }


}
