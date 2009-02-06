package eu.scy.collaborationservice.tuplespaceconnector;

import java.io.File;

import org.apache.log4j.Logger;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.xmpp.packet.Packet;


public class TuplespaceConnector implements Plugin, PacketInterceptor {

    private static final Logger logger = Logger.getLogger(TuplespaceConnector.class.getName());
    private InterceptorManager interceptorManager;                                       
    
    public TuplespaceConnector() {
        interceptorManager = InterceptorManager.getInstance();
    }


    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        // register with interceptor manager
        logger.debug("Connections make you happy.");
        interceptorManager.addInterceptor(this);
    }

    public void destroyPlugin() {
        // unregister with interceptor manager
        interceptorManager.removeInterceptor(this);
    }


    public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed) throws PacketRejectedException {
        if (processed) {
            logger.debug("packet processed.");
        } else {
            logger.debug("packet not processed.");
        }
        if (incoming) {
            logger.debug("packet incoming.");
        } else {
            logger.debug("packet outgoing.");
        }
        logger.debug("packet: " + packet.toXML());        
        logger.debug("============================================");        
    }
}   