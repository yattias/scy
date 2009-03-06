package eu.scy.communications.packet.extension;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;



public class ScyObjectExtensionProvider implements PacketExtensionProvider {
    
    
    public ScyObjectExtensionProvider() {
    }
    
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        ScyObjectPacketExtension scyExt = new ScyObjectPacketExtension();
        boolean done = false;
        StringBuffer buffer = new StringBuffer();;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("id")) { 
                    scyExt.setId(parser.nextText());
                }
                else if (parser.getName().equals("description")) {                    
                    scyExt.setDescription(parser.nextText());
                }
                else if (parser.getName().equals("name")) {                    
                    scyExt.setName(parser.nextText());
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals(scyExt.getElementName())) {
                    done = true;
                }
            }
        }
        return scyExt;

    }
    
}
