// package eu.scy.collaborationservice.tuplespaceconnector;
package eu.scy.openfire.plugin;

import java.io.File;

import org.apache.log4j.Logger;
import org.jivesoftware.openfire.PacketRouter;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Session;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmpp.packet.JID;

import eu.scy.collaborationservice.adapter.IScyCommunicationAdapter;
import eu.scy.collaborationservice.adapter.IScyCommunicationListener;
import eu.scy.collaborationservice.adapter.ScyCommunicationAdapterHelper;
import eu.scy.collaborationservice.adapter.ScyCommunicationEvent;
import eu.scy.communications.packet.extension.message.ScyMessagePacketExtension;


public class ScyOpenFirePlugin implements Plugin, PacketInterceptor, IScyCommunicationListener {
    
    private static final Logger logger = Logger.getLogger(ScyOpenFirePlugin.class.getName());
    private static final String SUBJECT = "relayed presence info";
    private static final JID AGENTSMITH_JID = new JID("obama@scy.intermedia.uio.no");
    private static final JID SYSTEM_JID = new JID("obama@scy.intermedia.uio.no");

    private static PluginManager pluginManager;
    
    private IScyCommunicationAdapter communicationsAdapter;
    private String serviceName;
    private XMPPServer xmppServer;
    private PacketRouter packetRouter;
    private InterceptorManager interceptorManager;    
    

    
    public ScyOpenFirePlugin() {
        xmppServer = XMPPServer.getInstance();
        ProviderManager providerManager = ProviderManager.getInstance();
        providerManager.addExtensionProvider(ScyMessagePacketExtension.ELEMENT_NAME, ScyMessagePacketExtension.NAMESPACE, ScyMessagePacketExtension.class);        
        communicationsAdapter = ScyCommunicationAdapterHelper.getInstance();
        communicationsAdapter.addScyCommunicationListener(this);
        interceptorManager = InterceptorManager.getInstance();        
    }
    
    
    public void initializePlugin(PluginManager pluginManager, File pluginDirectory) {
        pluginManager = pluginManager;
//        interceptorManager.addInterceptor(this);
        packetRouter = XMPPServer.getInstance().getPacketRouter();        
        logger.debug("==== Communication is Le Key ====");
    }
    
    
    public void destroyPlugin() {
//        interceptorManager.removeInterceptor(this);
    }
    
    
    public String getName() {
        return "collaboration-service-plugin";
    }
    
    
    public static PluginManager getPluginManager() {
        return pluginManager;
    }
    
    
    public void processScyPacket(PacketExtension extension) {
//        try {
//            logger.debug("checking if scy packet");
//            if (extension != null) {
////                ScyMessagePacketExtension scyExt = new ScyMessagePacketExtension(extension.getElement());
//                logger.debug("processing SCY Packet");
//                logger.debug("============== scy message packet =============");
//                logger.debug("name " + scyExt.getName());
//                logger.debug("description " + scyExt.getDescription());
//                logger.debug("id " + scyExt.getId());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }        
    }
    
    
    public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed) throws PacketRejectedException {
        if (!processed && incoming) {
            logger.debug("--=([])=-- " + packet.toXML());
            if (packet instanceof Presence) {
//                logger.debug("Presence packet incoming and unprocessed");
                sendMessageToAgentSmith(packet.toString());
            } 
//            else {
//                logger.debug("----- non-presence packet intercepted -----");
//                logger.debug("--- " + packet.toXML());
//                logger.debug("-------------------------------------------");
//            }
        }
    }
    
    
    @Override
    public void handleCommunicationEvent(ScyCommunicationEvent e) {
        logger.debug("---------- handleCommunicationEvent ----------");
    }
    
    
    /**
     * Agent Smith wants to know all that is going on, presence-wise
     * 
     * @param body - original presence packet
     */
    private void sendMessageToAgentSmith(String body) {        
//        final Message message = new Message();
//        message.setTo(AGENTSMITH_JID);
//        message.setFrom(SYSTEM_JID);
//        message.setSubject(SUBJECT);
//        message.setBody(body);
//        TimerTask messageTask = new TimerTask() {
//           public void run() {
//               packetRouter.route(message);
//           }
//        };
//        TaskEngine.getInstance().schedule(messageTask, 5000);
    }


    @Override
    public void interceptPacket(Packet arg0) {
        // TODO Auto-generated method stub
        
    }
    
}
