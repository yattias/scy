package eu.scy.common.smack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

import eu.scy.common.packetextension.SCYPacketTransformer;

public class SmacketExtensionProvider implements PacketExtensionProvider {

	private static Map<String, SCYPacketTransformer> transformers = new HashMap<String, SCYPacketTransformer>();

	public static void registerExtension(SCYPacketTransformer transformer) {
		transformers.put(transformer.getNamespace(), transformer);
	}

	@Override
	public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
		SCYPacketTransformer transformer = transformers.get(parser.getNamespace());
		transformer.resetParser();
		
		try {
			boolean done = false;
			
			String path = "";
			LinkedList<String> lastElementName = new LinkedList<String>();
			
			while (!done) {
				int eventType = parser.next();
				if (eventType == XmlPullParser.START_TAG) {
					if (parser.getName() != null) {
						path += "/" + parser.getName();
						lastElementName.add("/" + parser.getName());
						transformer.startNode(path);
						if(parser.getAttributeCount() > 0) {
							for (int i = 0; i < parser.getAttributeCount(); i++) {
								String attribKey = parser.getAttributeName(i);
								String attribValue = parser.getAttributeValue(i);
								transformer.mapXMLNodeToObject(path + "@" + attribKey, attribValue);
							}
						}
					}
				} else if (eventType == XmlPullParser.TEXT) {
					transformer.mapXMLNodeToObject(path, parser.getText());
				} else if (eventType == XmlPullParser.END_TAG) {
					transformer.endNode(path);
					path = path.substring(0, path.lastIndexOf(lastElementName.getLast()));
					lastElementName.removeLast();
					if (parser.getName().equals(transformer.getName())) {
						done = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		SmacketExtension extension = new SmacketExtension(transformer);
		return extension;
	}

}
