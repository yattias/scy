// package eu.scy.collaborationservice.tuplespaceconnector;
package eu.scy.openfire.plugin;

import java.io.File;

import org.apache.log4j.Logger;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;

import eu.scy.openfire.plugin.botz.BotzConnection;
import eu.scy.openfire.plugin.botz.BotzPacketReceiver;

public class ScyOpenFirePlugin implements Plugin, PacketInterceptor {
    
    private static final Logger logger = Logger.getLogger(ScyOpenFirePlugin.class.getName());
    private InterceptorManager interceptorManager;
    private static IScyCommunicationAdapter communicationsAdapter;
    private static PluginManager pluginManager;
    private String serviceName;
    
    public ScyOpenFirePlugin() {
        interceptorManager = InterceptorManager.getInstance();
        
    }
    
    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        // register with interceptor manager
        logger.debug("Connections make you happy.");
        this.interceptorManager.addInterceptor(this);
        logger.debug("1");
        this.pluginManager = manager;
        logger.debug("2");
        this.communicationsAdapter = ScyCommunicationAdapter.getInstance();
        logger.debug("TSC: " + communicationsAdapter);
        
        logger.debug("creating bot");
        
        BotzPacketReceiver packetReceiver = new BotzPacketReceiver() {
            
            BotzConnection bot;
            
            public void initialize(BotzConnection bot) {
                this.bot = bot;
            }
            
            public void processIncoming(Packet packet) {
                try {
                    logger.debug("bot got a packet: " + packet);
                    interceptPacket(packet, null, true, false);
                } catch (PacketRejectedException e) {
                    e.printStackTrace();
                }
            }
            
            public void processIncomingRaw(String rawText) {};
            
            public void terminate() {};
        };
        
        final BotzConnection bot = new BotzConnection(packetReceiver);
        try {
            // Create user and login
            bot.login("SCY-Bot");
            Presence presence = new Presence();
            presence.setStatus("SCYBot is watching you");
            bot.sendPacket(presence);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.communicationsAdapter.addScyCommunicationListener(new IScyCommunicationListener() {
            
            @Override
            public void handleCommunicationEvent(ScyCommunicationEvent e) {
                Packet newMessage = new Message();
                // newMessage.setTo("SCYBOT");
                // newMessage.
                // newMessage.setProperty("groupId", "scy");
                // newMessage.setBody("callback happened" + e.getMessage() );
                //                
                // bot.sendPacket((Packet)newMessage);
                // bot.
                
                // componentManager.sendPacket(ScyOpenFirePlugin.this,
                // newMessage);
                
            }
        });
    }
    
    public void destroyPlugin() {
        // unregister with interceptor manager
        interceptorManager.removeInterceptor(this);
    }
    
    public String getName() {
        return "collaboration-service-plugin";
    }
    
    public static PluginManager getPluginManager() {
        return pluginManager;
    }
    
    public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed) throws PacketRejectedException {
        if (!processed && incoming) {
            logger.debug("packet intercepted");
            if (packet.toString().contains("<ping xmlns=\"urn:xmpp:ping\"/>")) {
                logger.debug("============= PING from user " + packet.getFrom() + " ============");
            } else if (packet.toString().contains("<presence type=\"unavailable\" ")) {
                logger.debug("============= UNAVAILABLE from user" + packet.getFrom() + " ============");
            } else {
                logger.debug("============== uninteresting packet =============");
                logger.debug(packet.toXML());
            }
        }
    }
    
}
