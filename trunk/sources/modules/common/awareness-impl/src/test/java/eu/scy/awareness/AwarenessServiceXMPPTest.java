package eu.scy.awareness;

import java.util.List;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.junit.Ignore;

import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessRosterEvent;
import eu.scy.awareness.event.IAwarenessRosterListener;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.communications.datasync.properties.CommunicationProperties;

public class AwarenessServiceXMPPTest {

    private static final Logger logger = Logger.getLogger(AwarenessServiceXMPPTest.class.getName());
    private CommunicationProperties communicationProps;
    private ConnectionConfiguration config;
    private XMPPConnection xmppConnection;
    private String userName;
    private String password;
    private IAwarenessService awarenessService;
    
    @Ignore
    public void setupService() {
        try {
           setAwarenessService(AwarenessServiceFactory.getAwarenessService(AwarenessServiceFactory.XMPP_STYLE));
        } catch (AwarenessServiceException e) {
            e.printStackTrace();
        }
        
        getAwarenessService().init(getConnection("obama", "obama"));
        
        // Quick trick to ensure that this application will be running for ever. To stop the
        // application you will need to kill the process
        
        initListeners();
            
        
    }

    private void initListeners() {
        getAwarenessService().addAwarenessRosterListener(new IAwarenessRosterListener() {            
            @Override
            public void handleAwarenessRosterEvent(IAwarenessRosterEvent e) {
                System.out.println("rosterevent " + e);
            }
        });
        getAwarenessService().addAwarenessMessageListener(new IAwarenessMessageListener() {
            
            @Override
            public void handleAwarenessMessageEvent(IAwarenessEvent e) {
                System.out.println("messageevent " + e);
            }
        });
        getAwarenessService().addAwarenessPresenceListener(new IAwarenessPresenceListener() {
            
            @Override
            public void handleAwarenessPresenceEvent(IAwarePresenceEvent e) {
                System.out.println("presence " + e);
            }
        });
    }

    public void startServer() {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    public static void main(String[] args) {
        AwarenessServiceXMPPTest awarenessServiceXMPPTest = new AwarenessServiceXMPPTest();
        awarenessServiceXMPPTest.setupService();
//        awarenessServiceXMPPTest.startServer();
        
       //test to get all the buddies
//       awarenessServiceXMPPTest.getAllBuddiesTest();
//       awarenessServiceXMPPTest.addBuddyTest("pelsoi"); 
//       awarenessServiceXMPPTest.removeBuddyTest("pelsoi");
//       awarenessServiceXMPPTest.getAllBuddiesTest();
       
       awarenessServiceXMPPTest.sendMessageTest("jeremyt@wiki.intermedia.uio.no", "u are bad"); 
       awarenessServiceXMPPTest.sendMessageTest("jeremyt@wiki.intermedia.uio.no", "eat my shorts"); 
        
        
       //start the server so it can get messages
       awarenessServiceXMPPTest.startServer();
       
    }
    
    @Ignore
    public void sendMessageTest(String user, String message) {
        try {
            getAwarenessService().sendMessage(user, message);
        } catch (AwarenessServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Ignore
    public void getAllBuddiesTest() {
        List<IAwarenessUser> buddies = null;
        try {
            buddies = getAwarenessService().getBuddies();
        } catch (AwarenessServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (IAwarenessUser iAwarenessUser : buddies) {
            System.out.println("buddies: " + iAwarenessUser.toString());
        }

    }
    
    @Ignore
    public void addBuddyTest(String username) {
        try {
            getAwarenessService().addBuddy(username);
        } catch (AwarenessServiceException e) {
            e.printStackTrace();
        }
    }
    
    @Ignore
    public void removeBuddyTest(String username) {
        try {
            getAwarenessService().removeBuddy(username);
        } catch (AwarenessServiceException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    public XMPPConnection getConnection(String userName, String password) {
        CommunicationProperties communicationProps = new CommunicationProperties(); 
        
        this.userName = userName;
        this.password = password;
        
        //make it a jid
        userName = userName + "@" + communicationProps.datasyncServerHost;

              
        config = new ConnectionConfiguration(communicationProps.datasyncServerHost, new Integer(communicationProps.datasyncServerPort).intValue());
        config.setCompressionEnabled(true);
        config.setReconnectionAllowed(true);
        this.xmppConnection = new XMPPConnection(config);
        this.xmppConnection.DEBUG_ENABLED = true;

        //xmpp.client.idle -1 in server to set no time
        try {            
            this.xmppConnection.connect();
            SmackConfiguration.setPacketReplyTimeout(100000);
            SmackConfiguration.setKeepAliveInterval(10000);
            logger.debug("successful connection to xmpp server " + config.getHost() + ":" + config.getPort());
        } catch (XMPPException e) {
            logger.error("Error during xmpp connect");
            e.printStackTrace();
        }
        
        
        try {
            this.xmppConnection.login(userName, password);
            logger.debug("xmpp login ok");
        } catch (XMPPException e1) {
            logger.error("xmpp login failed. bummer. " + e1);
            e1.printStackTrace();
        }        
        
        this.xmppConnection.addConnectionListener(new ConnectionListener() {
            
            @Override
            public void connectionClosed() {
                logger.debug("TBI closed connection");
                try {
                    xmppConnection.connect();
                    logger.debug("TBI reconnected");
                } catch (XMPPException e) {
                    e.printStackTrace();
                    logger.debug("TBI failed to reconnect");
                }
            }
            
            @Override
            public void connectionClosedOnError(Exception arg0) {
                logger.debug("TBI server error closed;");
            }
            
            @Override
            public void reconnectingIn(int arg0) {
                logger.debug("TBI server reconnecting;");
            }
            @Override
            public void reconnectionFailed(Exception arg0) {
                logger.debug("TBI server reconnecting failed");
            }
            @Override
            public void reconnectionSuccessful() {
                logger.debug("TBI server reconnecting success");
            }
        });
        
        
        return xmppConnection;
    }

    /**
     * @param awarenessService the awarenessService to set
     */
    public void setAwarenessService(IAwarenessService awarenessService) {
        this.awarenessService = awarenessService;
    }

    /**
     * @return the awarenessService
     */
    public IAwarenessService getAwarenessService() {
        return awarenessService;
    }
    
    
    
}
