package eu.scy.server.services.impl;

import eu.scy.core.UserService;
import eu.scy.core.openfire.OpenFireServiceImpl;
import eu.scy.server.services.UserActivityService;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.springframework.beans.factory.InitializingBean;
import org.xmpp.packet.PacketExtension;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 13.apr.2010
 * Time: 14:56:57
 * To change this template use File | Settings | File Templates.
 */
public class UserActivityServiceImpl extends OpenFireServiceImpl implements UserActivityService, ConnectionListener, PacketListener, PacketFilter, InitializingBean {

    private static final Logger logger = Logger.getLogger(UserActivityServiceImpl.class);

    private final static String USER_SERVICE_LISTENER_STRING = "userServiceListener";
    private UserService userService;


    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void connectionClosed() {
        logger.info("CONNECTION CLOSED!");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        logger.warn("CONNECTION CLOSED ON ERROR");
    }

    @Override
    public void reconnectingIn(int i) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void reconnectionSuccessful() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void reconnectionFailed(Exception e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void processPacket(Packet packet) {
        logger.info("PACKET TO PROCESS: " + packet);
        Collection extensions = packet.getExtensions();
        for (Iterator iterator = extensions.iterator(); iterator.hasNext();) {
            PacketExtension packetExtension = (PacketExtension) iterator.next();
            logger.info("EXT: " + packetExtension.getElement().asXML());
        }
    }

    @Override
    public boolean accept(Packet packet) {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            if (getUserService().getUser(USER_SERVICE_LISTENER_STRING) == null) {
                getUserService().createUser(USER_SERVICE_LISTENER_STRING, USER_SERVICE_LISTENER_STRING, "ROLE_STUDENT");
            }
            XMPPConnection connection = getConnection(USER_SERVICE_LISTENER_STRING, USER_SERVICE_LISTENER_STRING);
            connection.addConnectionListener(this);
            connection.addPacketListener(this, this);
        } catch (XMPPException e) {
            e.printStackTrace();
        }

    }


}
