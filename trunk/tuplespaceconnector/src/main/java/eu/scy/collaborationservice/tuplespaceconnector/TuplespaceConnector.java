package eu.scy.collaborationservice.tuplespaceconnector;

import java.io.File;

import org.apache.log4j.Logger;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.xmpp.packet.Packet;


public class TuplespaceConnector implements Plugin, PacketInterceptor {
    
    private static final Logger logger = Logger.getLogger(TuplespaceConnector.class.getName());

    public void destroyPlugin() {
        // TODO Auto-generated method stub        
    }

    public void initializePlugin(PluginManager arg0, File arg1) {
        // TODO Auto-generated method stub
        logger.debug("You are connected to cheesy connector. Connections make you happy.");
    }

    public void interceptPacket(Packet arg0, Session arg1, boolean arg2, boolean arg3) throws PacketRejectedException {
        logger.debug("Packet?: gotcha! " + arg0.toXML());
        System.out.println("Packet?: gotcha! " + arg0.toXML());
    }

}
