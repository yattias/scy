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
import eu.scy.communications.packet.extension.object.ScyObjectPacketExtension;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.openfire.plugin.botz.BotzConnection;

public class ScyOpenFirePlugin implements Plugin, PacketInterceptor, IScyCommunicationListener {
    
    private static final Logger logger = Logger.getLogger(ScyOpenFirePlugin.class.getName());
    private static IScyCommunicationAdapter communicationsAdapter;
    private static PluginManager pluginManager;
    private String serviceName;
    private XMPPServer xmppServer;
    private PacketRouter packetRouter;
    private InterceptorManager interceptorManager;
    private BotzConnection bot;
    
    public ScyOpenFirePlugin() {
        xmppServer = XMPPServer.getInstance();
        ProviderManager providerManager = ProviderManager.getInstance();
        this.interceptorManager = InterceptorManager.getInstance();
        
        providerManager.addExtensionProvider(ScyObjectPacketExtension.ELEMENT_NAME, ScyObjectPacketExtension.NAMESPACE, ScyObjectPacketExtension.class);
        
        this.communicationsAdapter = ScyCommunicationAdapterHelper.getInstance();
        this.communicationsAdapter.addScyCommunicationListener(this);
    }
    
    public void initializePlugin(PluginManager pluginManager, File pluginDirectory) {
        this.pluginManager = pluginManager;
        this.interceptorManager.addInterceptor(this);
        System.out.println("communication is the key");
        
        bot = new BotzConnection();
        try {
            // Create user and login
            logger.debug("scy bot loggin in");
            bot.login("scybot");
            Presence presence = new Presence();
            presence.setStatus("SCYBot is watching you");
            bot.sendPacket(presence);
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
            System.out.println("checking if scy packet");
            if (extension != null) {
                ScyObjectPacketExtension scyExt = new ScyObjectPacketExtension(extension.getElement());
                System.out.println("processing SCY Packet");
                // ScyObjectPacketExtension scyExt = (ScyObjectPacketExtension)
                // extension;
                System.out.println("============== scy message packet =============");
                System.out.println("name " + scyExt.getName());
                System.out.println("description " + scyExt.getDescription());
                System.out.println("id " + scyExt.getId());
//                communicationsAdapter.sendCallBack("send something");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @Override
    public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed) throws PacketRejectedException {
        if (!processed && incoming) {
            if (!packet.getFrom().getNode().equals(bot.getUsername())) {
                System.out.println("====== intercepting-packet ============");
                System.out.println("====== packet ============");
                System.out.println(packet.toXML());
                PacketExtension extension = (PacketExtension) packet.getExtension(ScyObjectPacketExtension.ELEMENT_NAME, ScyObjectPacketExtension.NAMESPACE);
                this.processScyPacket(extension);
            } else {
                System.out.println("Packet from bot");
            }
            
        }
        
    }
    
    @Override
    public void handleCommunicationEvent(ScyCommunicationEvent e) {
        System.out.println("---------- handleCommunicationEvent ----------");
        org.xmpp.packet.Message m = new org.xmpp.packet.Message();
        m.setTo("biden@imediamac09.uio.no/Smack");
        try {
            m.setFrom(bot.getUsername() + "@" + bot.getHostName() + "/" + bot.getResource());
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
        m.setSubject("Message to set");
//        m.setBody("i is watching you; callback happened: " + e.getMessage());
        ScyBaseObject scyObject = new ScyBaseObject();
        scyObject.setId("666");
        scyObject.setName("mr.component");
        scyObject.setDescription("call back from mr. component");
        PacketExtension scye = new ScyObjectPacketExtension();
        ((ScyObjectPacketExtension) scye).setScyBase(scyObject);
        m.addExtension(scye);
        bot.sendPacket(m);
        System.out.println("---------- sent ----------");
    }
    
}
