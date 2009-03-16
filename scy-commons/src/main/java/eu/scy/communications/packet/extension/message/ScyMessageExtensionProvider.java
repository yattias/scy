package eu.scy.communications.packet.extension.message;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * XMPP Packet extension provider for ScyMessages transported with an xmpp
 * server
 * 
 * @author anthonyp
 */
public class ScyMessageExtensionProvider implements PacketExtensionProvider {
    
    public ScyMessageExtensionProvider() {
        System.out.println("ScyMessageExtensionProvider.ScyMessageExtensionProvider()");
    }
    
    /**
     * Parses the xml of the extension which is an ScyMessage
     * 
     * @param parser
     * @return return the packet extension
     */
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        ScyMessagePacketExtension scyExt = new ScyMessagePacketExtension();
        
        try {
            boolean done = false;
            StringBuffer buffer = new StringBuffer();
            
            while (!done) {
                int eventType = parser.next();
                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName() != null && parser.getName().equals("id")) {
                        String nt = parser.nextText();
                        System.out.println(nt);
                        scyExt.setId(nt);
                        
                    } else if (parser.getName() != null && parser.getName().equals("name")) {
                        String nt = parser.nextText();
                        System.out.println(nt);
                        scyExt.setName(nt);
                    } else if (parser.getName() != null && parser.getName().equals("description")) {
                        String nt = parser.nextText();
                        System.out.println(nt);
                        scyExt.setDescription(nt);
                    } else if (parser.getName() != null && parser.getName().equals("userName")) {
                        String nt = parser.nextText();
                        System.out.println(nt);
                        scyExt.setUserName(nt);
                    } else if (parser.getName() != null && parser.getName().equals("toolName")) {
                        String nt = parser.nextText();
                        System.out.println(nt);
                        scyExt.setToolName(nt);
                    } else if (parser.getName() != null && parser.getName().equals("to")) {
                        String nt = parser.nextText();
                        System.out.println(nt);
                        scyExt.setTo(nt);
                    } else if (parser.getName() != null && parser.getName().equals("from")) {
                        String nt = parser.nextText();
                        System.out.println(nt);
                        scyExt.setFrom(nt);
                    } else if (parser.getName() != null && parser.getName().equals("session")) {
                        String nt = parser.nextText();
                        System.out.println(nt);
                        scyExt.setSession(nt);
                    } else if (parser.getName() != null && parser.getName().equals("objectType")) {
                        String nt = parser.nextText();
                        System.out.println(nt);
                        scyExt.setObjectType(nt);
                    } else if (parser.getName() != null && parser.getName().equals("messagePurpose")) {
                        String nt = parser.nextText();
                        System.out.println(nt);
                        scyExt.setMessagePurpose(nt);
                    } else if (parser.getName() != null && parser.getName().equals("expiraton")) {
                        String nt = parser.nextText();
                        System.out.println(nt);
                        scyExt.setExpiraton(new Long(nt).longValue());
                    } 
                } else if (eventType == XmlPullParser.END_TAG) {
                    System.out.println("name; + " + scyExt.getName() + " " + scyExt.getElementName());
                    if (parser.getName().equals(scyExt.getElementName())) {
                        done = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return (PacketExtension) scyExt;
        
    }
    
}
