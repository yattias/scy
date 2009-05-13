package eu.scy.colemo.client;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 06.apr.2009
 * Time: 05:23:52
 * To change this template use File | Settings | File Templates.
 */
public class ConceptMapExporterTest extends AbstractTestForExportImport {

    /**
     * regression test for SCY-119
     */
    public void testHasDoubleExportBugBeenResolvedSCY119() {
        String xml1 = ConceptMapExporter.getDefaultInstance().createXML(nodes.iterator(), links.iterator());
        String xml2 = ConceptMapExporter.getDefaultInstance().createXML(nodes.iterator(), links.iterator());

        assertEquals(xml1, xml2);

    }

}
