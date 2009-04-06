package eu.scy.colemo.client;

import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.apr.2009
 * Time: 06:35:53
 * To change this template use File | Settings | File Templates.
 */
public class ConceptMapLoaderTest extends AbstractTestForExportImport {


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
