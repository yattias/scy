package eu.scy.collaborationservice.plugin;

import java.io.File;

import org.apache.log4j.Logger;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.xmpp.packet.Packet;

import eu.scy.collaborationservice.CollaborationService;


public class CollaborationServicePlugin implements Plugin, PacketInterceptor {

    private static final Logger logger = Logger.getLogger(CollaborationServicePlugin.class.getName());
    private InterceptorManager interceptorManager;
    private CollaborationService collaborationService;
    private static PluginManager pluginManager;
    

    public CollaborationServicePlugin() {
        interceptorManager = InterceptorManager.getInstance();
    }

    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        // register with interceptor manager
        logger.debug("Connections make you happy.");
        interceptorManager.addInterceptor(this);
        logger.debug("1");
        pluginManager = manager;
        logger.debug("2");
        collaborationService = CollaborationService.getInstance();
        logger.debug("TSC: " + collaborationService);
    }

    public void destroyPlugin() {
        // unregister with interceptor manager
        interceptorManager.removeInterceptor(this);
    }   
    
    public String getName() {
        return "CollaborationService";  
    }
    

    public static PluginManager getPluginManager() {
        return pluginManager;
    }

    public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed) throws PacketRejectedException {       
        if (!processed && incoming) {
            //logger.debug(packet.toXML());
            logger.debug("packet intercepted");
            String id;
            if (packet.toString().contains("<ping xmlns=\"urn:xmpp:ping\"/>")) {
                logger.debug("============= PING from user " + packet.getFrom() + " ============");
                //id = userPing(packet.getFrom().toString());
                //logger.debug("got id: " + id);
            }
            else if (packet.toString().contains("<presence type=\"unavailable\" ")) {
                logger.debug("============= UNAVAILABLE from user " + packet.getFrom() + " ============");     
                //id = userUnavailable(packet.getFrom().toString());
                //logger.debug("got id: " + id);
            } else {
                logger.debug("============== uninteresting packet =============");
                logger.debug(packet.toXML());
            }
        }
    }
    
//    public String userPing(String jabberUser) {
//        logger.debug("Attempting to write PING to CS");
//        String id = collaborationService.write(jabberUser, "ping");
//        logger.debug("ID returned: " + id);
//        return id;
//    }
//    
//    
//    public String userUnavailable(String jabberUser) {
//        logger.debug("Attempting to write UNAVAILABLE to CS");
//        String id = collaborationService.write(jabberUser, "unavailable");
//        logger.debug("ID returned: " + id);
//        return id;
//    }
    
}   