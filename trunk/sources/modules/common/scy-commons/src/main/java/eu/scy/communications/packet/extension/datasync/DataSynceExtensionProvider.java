package eu.scy.communications.packet.extension.datasync;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;



/**
 * XMPP Packet extension provider for ScyMessages transported with an xmpp server
 * 
 * @author anthonyp
 *
 */
public class DataSynceExtensionProvider implements PacketExtensionProvider {
    
    
    public DataSynceExtensionProvider() {
        System.out.println("ScyMessageExtensionProvider.ScyMessageExtensionProvider()");
    }
    
    /**
     * Parses the xml of the extension which is an ScyMessage
     * 
     * @param parser
     * @return return the packet extension
     */
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        DataSyncPacketExtension ds = new DataSyncPacketExtension();
        
        try {
            boolean done = false;
            StringBuffer buffer = new StringBuffer();;
            while (!done) {
                int eventType = parser.next();
                if (eventType == XmlPullParser.START_TAG) {
                    if ( parser.getName() != null && parser.getName().equals(DataSyncPacketExtension.CONTENT)) {
                        String nt = parser.nextText();
                        System.out.println(nt);
                        ds.setContent(nt);
                        
                    }
                    else if (parser.getName() != null && parser.getName().equals(DataSyncPacketExtension.EVENT)) {
                        String nt = parser.nextText();
                        System.out.println(nt);
                        ds.setEvent(nt);
                    }
                    else if ( parser.getName() != null && parser.getName().equals(DataSyncPacketExtension.EXPIRATION)) {
                        String nt = parser.nextText();
                        System.out.println(nt);
                        ds.setExpiration(new Long(nt).longValue());
                    }
                    else if ( parser.getName() != null && parser.getName().equals(DataSyncPacketExtension.FROM)) {
                        String nt = parser.nextText();
                        System.out.println(nt);
                        ds.setFrom(nt);
                    }
                    else if ( parser.getName() != null && parser.getName().equals(DataSyncPacketExtension.PERSISTENCE_ID)) {
                        String nt = parser.nextText();
                        System.out.println(nt);
                        ds.setPersistenceId(nt);
                    }
                    else if ( parser.getName() != null && parser.getName().equals(DataSyncPacketExtension.TOOL_ID)) {
                        String nt = parser.nextText();
                        System.out.println(nt);
                        ds.setToolId(nt);
                    }
                    else if ( parser.getName() != null && parser.getName().equals(DataSyncPacketExtension.TOOL_SESSION_ID)) {
                        String nt = parser.nextText();
                        System.out.println(nt);
                        ds.setToolSessionId(nt);
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals(ds.getElementName())) {
                        done = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return (PacketExtension) ds;

    }
    
}
