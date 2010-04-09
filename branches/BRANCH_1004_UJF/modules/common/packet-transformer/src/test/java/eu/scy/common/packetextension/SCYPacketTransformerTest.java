package eu.scy.common.packetextension;
import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.junit.BeforeClass;
import org.junit.Test;

import eu.scy.common.packetextension.SCYPacketTransformer;

public class SCYPacketTransformerTest {

	private static ActionTestPojo pojo;
	private static String pojoXml;

	@BeforeClass
	public static void setUp() {
		pojo = new ActionTestPojo();
		pojo.setActiontype1("login");
		pojo.setActiontype2("get focus");
		pojo.setTimestamp1(100);
		pojo.setTimestamp2(105);
		pojo.setToolName("TestTool");
		pojo.setToolVersion(4);
		pojo.setUsername("Obama");
		pojoXml = "<x xmlns=\"jabber:x:TestPacketTransformer\"><lom><username>Obama</username><action timestamp=\"100\"><type>login</type></action><action timestamp=\"105\"><type>get focus</type></action><tool><name>TestTool</name><version>4</version></tool></lom></x>";
	}

	@Test
	public void testPojoToXML() throws ParserConfigurationException,
			TransformerFactoryConfigurationError, TransformerException,
			IOException {

		SCYPacketTransformer t = new PacketTestTransformer();
		t.setObject(pojo);

		String xml = t.toXML();

		assertEquals("Generated XML is somehow malformed", pojoXml, xml);
	}
	
	
	
	@Test
	public void testXMLToPojo() throws ParserConfigurationException,
	TransformerFactoryConfigurationError, TransformerException,
	IOException {
		SCYPacketTransformer t = new PacketTestTransformer();
		t.mapXMLNodeToObject("/lom[0]/username", "Obama");
		t.mapXMLNodeToObject("/lom[0]/action[0]/type", "login");
		t.mapXMLNodeToObject("/lom[0]/action[1]/type", "get focus");
		t.mapXMLNodeToObject("/lom[0]/action[0]@timestamp", "100");
		t.mapXMLNodeToObject("/lom[0]/action[1]@timestamp", "105");
		t.mapXMLNodeToObject("/lom[0]/tool[0]/name", "TestTool");
		t.mapXMLNodeToObject("/lom[0]/tool[0]/version", "4");
		
		Object o = t.getObject();
		assertEquals("Generated Pojo is not correct", o, pojo);
	}
	
	
	
	

}
