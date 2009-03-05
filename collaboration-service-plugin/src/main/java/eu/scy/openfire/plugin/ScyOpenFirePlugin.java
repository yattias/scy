// package eu.scy.collaborationservice.tuplespaceconnector;
package eu.scy.openfire.plugin;

import java.io.File;

import org.apache.log4j.Logger;
import org.jivesoftware.openfire.PacketRouter;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;

import eu.scy.commons.packet.extension.ScyObjectExtensionProvider;
import eu.scy.commons.packet.extension.ScyObjectPacketExtension;
import eu.scy.core.model.impl.ScyBaseObject;

public class ScyOpenFirePlugin implements Plugin, Component, PacketInterceptor {
    
    private static final Logger logger = Logger.getLogger(ScyOpenFirePlugin.class.getName());
    private InterceptorManager interceptorManager;
    private static IScyCommunicationAdapter communicationsAdapter;
    private static PluginManager pluginManager;
    private String serviceName;
    private XMPPServer xmppServer;
    private PacketRouter packetRouter;
    private JID componentJID;
    private ComponentManager componentManager;
    private boolean isComponentReady;
    
    public ScyOpenFirePlugin() {
        interceptorManager = InterceptorManager.getInstance();
        xmppServer = XMPPServer.getInstance();
        packetRouter = xmppServer.getPacketRouter();
        ProviderManager providerManager = ProviderManager.getInstance();
        providerManager.addExtensionProvider(ScyObjectPacketExtension.ELEMENT_NAME, ScyObjectPacketExtension.NAMESPACE, new ScyObjectExtensionProvider());
    }
    
    /**
     * Initializes the component.
     *
     * @param jid the jid of the component
     * @param componentManager instance of the componentManager
     * @throws ComponentException thrown if there are issues initializing this component
     */
    public void initialize(JID jid, ComponentManager componentManager) throws ComponentException {
        this.componentJID = jid;
    }

    
    
    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        // register with interceptor manager
        logger.debug("Connections make you happy.");
        this.interceptorManager.addInterceptor(this);
        this.pluginManager = manager;
        this.communicationsAdapter = ScyCommunicationAdapter.getInstance();
        logger.debug("TSC: " + communicationsAdapter);
        
        componentManager = ComponentManagerFactory.getComponentManager();
        
        logger.debug("register component name");
        
        try {
            componentManager.addComponent(getName(), this);
        } catch (ComponentException e1) {
            e1.printStackTrace();
        }

        
        //logger.debug("creating bot");
        
//        BotzPacketReceiver packetReceiver = new BotzPacketReceiver() {
//            
//            BotzConnection bot;
//            
//            public void initialize(BotzConnection bot) {
//                this.bot = bot;
//            }
//            
//            public void processIncoming(Packet packet) {
//                try {
//                    logger.debug("bot got a packet: " + packet);
//                    interceptPacket(packet, null, true, false);
//                } catch (PacketRejectedException e) {
//                    e.printStackTrace();
//                }
//            }
//            
//            public void processIncomingRaw(String rawText) {};
//            
//            public void terminate() {};
//        };
//        
//        final BotzConnection bot = new BotzConnection(packetReceiver);
//        try {
//            // Create user and login
//            logger.debug("scy bot loggin in");
//            bot.login("SCY-Bot");
//            Presence presence = new Presence();
//            presence.setStatus("SCYBot is watching you");
//            bot.sendPacket(presence);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        
        this.communicationsAdapter.addScyCommunicationListener(new IScyCommunicationListener() {
            
            @Override
            public void handleCommunicationEvent(ScyCommunicationEvent e) {
                logger.debug("---------- handleCommunicationEvent ----------");
                org.xmpp.packet.Message m = new org.xmpp.packet.Message();
                m.setFrom("scy-bot");
                m.setBody("scy bot is watching you; callback happened: " + e.getMessage());
                ScyBaseObject scyObject = new ScyBaseObject();
                PacketExtension scye = new ScyObjectPacketExtension();
                ((ScyObjectPacketExtension) scye).setScyBase(scyObject);
                m.addExtension((org.xmpp.packet.PacketExtension) scye);
                sendPacket(m);
                logger.debug("---------- sent ----------");
            }
        });
    }
    
    /**
     * Used to send a packet with this component
     *
     * @param packet the package to send
     */
    public void sendPacket(Packet packet) {
        try {
            componentManager.sendPacket(this, packet);
        }
        catch (Exception e) {
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
    
    /**
     * Returns JID for this component
     *
     * @return the jid for this component
     */
    public JID getComponentJID() {
        return componentJID;
    }
    
    public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed) throws PacketRejectedException {
        if (!processed && incoming) {
            logger.debug("packet intercepted");
//            if (packet.toString().contains("<ping xmlns=\"urn:xmpp:ping\"/>")) {
//                logger.debug("============= PING from user " + packet.getFrom() + " ============");
//            } else if (packet.toString().contains("<presence type=\"unavailable\" ")) {
//                logger.debug("============= UNAVAILABLE from user" + packet.getFrom() + " ============");
//            } else {
                logger.debug("============== uninteresting packet =============");
                logger.debug(packet.toXML());
                
                    logger.debug("============== instance of message =============");
                    logger.debug("extracting element");
                    org.jivesoftware.smack.packet.PacketExtension extension =  (org.jivesoftware.smack.packet.PacketExtension) packet.getExtension(ScyObjectPacketExtension.ELEMENT_NAME, ScyObjectPacketExtension.NAMESPACE);
                   // logger.debug("extension; " + extension.toString());
                    logger.debug("debug " + packet.toString());
                    if( extension instanceof ScyObjectPacketExtension) {
                        ScyObjectPacketExtension scyExt = (ScyObjectPacketExtension)extension;
                        logger.debug("============== scy message packet =============");
                        logger.debug("name " + scyExt.getName());
                        logger.debug("description " + scyExt.getDescription());
                        logger.debug("id " + scyExt.getId());
                }
                communicationsAdapter.sendCallBack("send something");
        }
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void processPacket(Packet arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void shutdown() {
        isComponentReady = false;
    }

    @Override
    public void start() {
        isComponentReady = true;
    }
}
