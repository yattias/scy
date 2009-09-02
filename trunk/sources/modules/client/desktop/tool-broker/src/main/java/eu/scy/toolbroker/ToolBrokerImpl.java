package eu.scy.toolbroker;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.logger.ActionLogger;
import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.AwarenessServiceFactory;
import eu.scy.awareness.IAwarenessService;
import eu.scy.collaborationservice.ICollaborationService;
import eu.scy.communications.datasync.properties.CommunicationProperties;
import eu.scy.datasync.client.IDataSyncService;
import eu.scy.notification.api.INotificationService;
import eu.scy.sessionmanager.SessionManager;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 * This class implements the ToolBrokerAPI interface and provides all the
 * services. The main idea of this implementation is that all the services are
 * injected by Spring into this class and then can be accessed by all clients.
 * 
 * @author Giemza
 */
public class ToolBrokerImpl<K extends IMetadataKey> implements ToolBrokerAPI<K> {
    
    private static final Logger logger = Logger.getLogger(ToolBrokerImpl.class.getName());
    
    private static final String beanConfigurationFile = "beans.xml";
    
    private ApplicationContext context;
    
    private IRepository repository;
    
    private IMetadataTypeManager metaDataTypeManager;
    
    private IExtensionManager extensionManager;
    
    private IActionLogger actionLogger;
    
    private INotificationService notificationService;
    
    private SessionManager sessionManager;

    private IAwarenessService awarenessService;

    private ICollaborationService collaborationService;

    private IDataSyncService dataSyncService;

    private CommunicationProperties communicationProps;

    private ConnectionConfiguration config;

    private XMPPConnection xmppConnection;

    private String userName;

    private String password;
    
    
    @SuppressWarnings("unchecked")
    public ToolBrokerImpl() {
        context = new ClassPathXmlApplicationContext(beanConfigurationFile);
        
        repository = (IRepository) context.getBean("repository");
        metaDataTypeManager = (IMetadataTypeManager) context.getBean("metadataTypeManager");
        extensionManager = (IExtensionManager) context.getBean("extensionManager");
        
        actionLogger = (IActionLogger) context.getBean("actionlogger");
        // FIXME: init action logger with XMPP connection with fixed credentials!
        ((ActionLogger) actionLogger).init(getConnection("obama", "obama"));
        
        notificationService = (INotificationService) context.getBean("notificationService");
        
        sessionManager = (SessionManager) context.getBean("sessionManager");
        awarenessService = (IAwarenessService) context.getBean("awarenessService");
        collaborationService = (ICollaborationService) context.getBean("collaborationService");
        dataSyncService = (IDataSyncService) context.getBean("dataSyncService");
    }
    
    /**
     * Sets the repository instance into the ToolBroker. Is mainly used for
     * Spring bean injection.
     * 
     * @param repository
     *            the repository instance to set
     */
    public void setRepository(IRepository repository) {
        this.repository = repository;
    }
    
    /**
     * @param metaDataTypeManager
     *            the metaDataTypeManager to set
     */
    public void setMetaDataTypeManager(IMetadataTypeManager metaDataTypeManager) {
        this.metaDataTypeManager = metaDataTypeManager;
    }
    
    /**
     * @param extensionManager
     *            the extensionManager to set
     */
    public void setExtensionManager(IExtensionManager extensionManager) {
        this.extensionManager = extensionManager;
    }
    
    /**
     * @param actionLogger
     *            the actionLogger to be set
     */
    public void setActionLogger(IActionLogger actionLogger) {
        this.actionLogger = actionLogger;
    }
    
    /**
     * @param notificationService
     *            the notificationService to be set
     */
    public void setNotificationService(INotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    
    /*
     * (non-Javadoc)
     * @see eu.scy.toolbrokerapi.ToolBrokerAPI#getRepository()
     */
    public IRepository getRepository() {
        return repository;
    }
    
    /*
     * (non-Javadoc)
     * @see eu.scy.toolbrokerapi.ToolBrokerAPI#getMetaDataTypeManager()
     */
    public IMetadataTypeManager getMetaDataTypeManager() {
        return metaDataTypeManager;
    }
    
    /*
     * (non-Javadoc)
     * @see eu.scy.toolbrokerapi.ToolBrokerAPI#getExtensionManager()
     */
    public IExtensionManager getExtensionManager() {
        return extensionManager;
    }
    
    public IActionLogger getActionLogger() {
        return actionLogger;
    }
    
    public INotificationService getNotificationService() {
        return notificationService;
    }
    
    @Override
    public SessionManager getUserSession(String username, String password) {
        try {
            sessionManager.login(username, password);
            return sessionManager;
        } catch (LoginException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IAwarenessService getAwarenessService() {
        
        try {
            awarenessService = AwarenessServiceFactory.getAwarenessService(AwarenessServiceFactory.XMPP_STYLE);
            return awarenessService;
        } catch (AwarenessServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        return null;
    }
    
    @Override
    public ICollaborationService getCollaborationService() {
        return collaborationService;
    }
    
    @Override
    public IDataSyncService getDataSyncService() {
        return dataSyncService;
    }

    @Override
    public XMPPConnection getConnection(String userName, String password) {
    	if(xmppConnection == null) {
	        communicationProps = new CommunicationProperties(); 
	        
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
    	}
        return xmppConnection;
    }
    
}
