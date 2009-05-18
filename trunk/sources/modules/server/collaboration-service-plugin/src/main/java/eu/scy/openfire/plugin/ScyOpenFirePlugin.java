// package eu.scy.collaborationservice.tuplespaceconnector;
package eu.scy.openfire.plugin;

import java.io.File;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.jivesoftware.openfire.PacketRouter;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmpp.packet.Packet;
import org.xmpp.packet.PacketExtension;
import org.xmpp.packet.Presence;

import eu.scy.communications.adapter.IScyCommunicationAdapter;
import eu.scy.communications.adapter.IScyCommunicationListener;
import eu.scy.communications.adapter.ScyCommunicationAdapterHelper;
import eu.scy.communications.adapter.ScyCommunicationEvent;
import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;
import eu.scy.communications.packet.extension.message.ScyMessagePacketExtension;
import eu.scy.openfire.plugin.botz.BotzConnection;

public class ScyOpenFirePlugin implements Plugin, PacketInterceptor, IScyCommunicationListener {
    
    private static final Logger logger = Logger.getLogger(ScyOpenFirePlugin.class.getName());
    private IScyCommunicationAdapter communicationsAdapter;
    private static PluginManager pluginManager;
    private String serviceName;
    private XMPPServer xmppServer;
    private PacketRouter packetRouter;
    private InterceptorManager interceptorManager;
    //private BotzConnection bot;
    
    public ScyOpenFirePlugin() {
        xmppServer = XMPPServer.getInstance();
        ProviderManager providerManager = ProviderManager.getInstance();
        this.interceptorManager = InterceptorManager.getInstance();
        
        providerManager.addExtensionProvider(ScyMessagePacketExtension.ELEMENT_NAME, ScyMessagePacketExtension.NAMESPACE, ScyMessagePacketExtension.class);
        
        communicationsAdapter = ScyCommunicationAdapterHelper.getInstance();
        this.communicationsAdapter.addScyCommunicationListener(this);
    }
    
    public void initializePlugin(PluginManager pluginManager, File pluginDirectory) {
        this.pluginManager = pluginManager;
        this.interceptorManager.addInterceptor(this);
        logger.debug("==== Communication is Le Key ====");
        
        //bot = new BotzConnection();
        try {
            // Create user and login
            //bot.login("scybot");
            //Presence presence = new Presence();
            //presence.setStatus("SCYBot is watching you");
            //bot.sendPacket(presence);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void destroyPlugin() {
        interceptorManager.removeInterceptor(this);
    }
    
    public String getName() {
        return "collaboration-service-plugin";
    }
    
    public static PluginManager getPluginManager() {
        return pluginManager;
    }
    
    public void processScyPacket(PacketExtension extension) {
        try {
            logger.debug("checking if scy packet");
            if (extension != null) {
                ScyMessagePacketExtension scyExt = new ScyMessagePacketExtension(extension.getElement());
                logger.debug("processing SCY Packet");
                // ScyObjectPacketExtension scyExt = (ScyObjectPacketExtension)
                // extension;
                logger.debug("============== scy message packet =============");
                logger.debug("name " + scyExt.getName());
                logger.debug("description " + scyExt.getDescription());
                logger.debug("id " + scyExt.getId());
//                communicationsAdapter.actionUponWrite(scyExt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @Override
    public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed) throws PacketRejectedException {
        // a packet asking for presence subsription from thomasd to test looks like this:
        // <presence to="test@wiki.intermedia.uio.no" type="subscribe" from="thomasd@wiki.intermedia.uio.no/imediamac10-3"/>
        if (!processed && incoming) {
            logger.debug("= packet incoming/unprocessed");
            logger.debug(packet.toXML());
//            if (!packet.getFrom().getNode().equals(bot.getUsername())) {
//                logger.debug("====== intercepting-packet ============");
//                logger.debug("====== packet ============");
//                logger.debug(packet.toXML());
//                PacketExtension extension = (PacketExtension) packet.getExtension(ScyMessagePacketExtension.ELEMENT_NAME, ScyMessagePacketExtension.NAMESPACE);
//                this.processScyPacket(extension);
//            } else {
//                logger.debug("Packet from bot");
//            }            
        } else if (processed && !incoming) {
            logger.debug("= packet outgoing/processed");
            logger.debug(packet.toXML());
        }
    }
    
    @Override
    public void handleCommunicationEvent(ScyCommunicationEvent e) {
        logger.debug("---------- handleCommunicationEvent ----------");
//        org.xmpp.packet.Message m = new org.xmpp.packet.Message();
//        m.setTo("biden@imediamac09.uio.no/Smack");
//        try {
//            m.setFrom(bot.getUsername() + "@" + bot.getHostName() + "/" + bot.getResource());
//        } catch (UnknownHostException e1) {
//            e1.printStackTrace();
//        }
//        m.setSubject("Message to set");
//        m.setBody("i is watching you; callback happened: " + e.getScyMessage());
//        IScyMessage scyObject = new ScyMessage();
//        scyObject.setId("666");
//        scyObject.setName("mr.component");
//        scyObject.setDescription("call back from mr. component");
//        PacketExtension scye = new ScyObjectPacketExtension();
//        ((ScyObjectPacketExtension) scye).setScyBase(scyObject);
//        m.addExtension(scye);
//        bot.sendPacket(m);
//        logger.debug("---------- sent ----------");
    }
    
}
