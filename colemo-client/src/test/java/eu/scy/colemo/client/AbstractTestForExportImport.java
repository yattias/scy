package eu.scy.colemo.client;

import junit.framework.TestCase;

import java.util.LinkedList;
import java.util.Collection;

import eu.scy.colemo.server.uml.UmlClass;
import eu.scy.colemo.server.uml.UmlLink;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 06.apr.2009
 * Time: 05:24:26
 * To change this template use File | Settings | File Templates.
 */
public class AbstractTestForExportImport extends TestCase {
    protected String xmlContents;
    protected Collection nodes = null;
    protected Collection links = null;

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

    
}
