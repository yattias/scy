package eu.scy.scymapper.impl;

import eu.scy.scymapper.api.IConceptMap;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * User: Bjoerge Naess
 * Date: 01.okt.2009
 * Time: 16:15:43
 * <conceptmap name="something">
 * 		<nodes>
 * 			<node id="22" x="12" y="44" height="33" width="100" label="I'm a concept">
 *	 			<style opaque="false" fgcolor="#ffffff" bgcolor="#000000">
 * 				</style>
 * 				<shape class="">
 *
 * 				<shape>
 * 			</node>
 * 		</nodes>
 *
	private INodeShape shape = new DefaultNodeShape();
	private Dimension size = new Dimension(150, 50);
	private Point location = new Point(20, 20);
	private String label = "New concept";
    private INodeStyle style = new DefaultNodeStyle();
    private boolean labelHidden = false;
	private transient boolean selected = false;
 * </nodes>
 */
public class ConceptMapSerializer {
	public static String serialize(IConceptMap cmap) throws ParserConfigurationException {
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		Document doc = docBuilder.newDocument();

		return "<error></error>";
	}
}
