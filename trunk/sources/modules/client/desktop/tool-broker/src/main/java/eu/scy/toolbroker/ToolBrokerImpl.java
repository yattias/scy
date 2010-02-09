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
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.logger.ActionLogger;
import eu.scy.awareness.IAwarenessService;
import eu.scy.client.common.datasync.DataSyncService;
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.client.notification.NotificationReceiver;
import eu.scy.common.configuration.Configuration;
import eu.scy.notification.api.INotifiable;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;
import eu.scy.sessionmanager.SessionManager;
import eu.scy.toolbrokerapi.LoginFailedException;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 * This class implements the ToolBrokerAPI interface and provides all the
 * services. The main idea of this implementation is that all the services are
 * injected by Spring into this class and then can be accessed by all clients.
 * 
 * @author Giemza
 */
public class ToolBrokerImpl implements ToolBrokerAPI {
    
    private static final Logger logger = Logger.getLogger(ToolBrokerImpl.class.getName());
    
    private static final String defaultBeanConfigurationFile = "beans.xml";
    
    private ApplicationContext context;
    
    private IRepository repository;
    
    private IMetadataTypeManager metaDataTypeManager;
    
    private IExtensionManager extensionManager;

    private IELOFactory eloFactory;

    private IActionLogger actionLogger;
    
    private SessionManager sessionManager;

    private IAwarenessService awarenessService;

    private IDataSyncService dataSyncService;

    private ConnectionConfiguration config;

    private XMPPConnection xmppConnection;

    private PedagogicalPlanService pedagogicalPlanService;
    
    private NotificationReceiver notificationReceiver;

    private String userName;

    private String password;
    

    public ToolBrokerImpl(String username, String password) {
       this(username,password,defaultBeanConfigurationFile);
    }

    @SuppressWarnings("unchecked")
    public ToolBrokerImpl(String username, String password, String beanConfigurationFile) throws LoginFailedException {
    	
    	getConnection(username, password);
    	
        context = new ClassPathXmlApplicationContext(beanConfigurationFile);
        
        // RoOLO
        repository = (IRepository) context.getBean("repository");
        metaDataTypeManager = (IMetadataTypeManager) context.getBean("metadataTypeManager");
        extensionManager = (IExtensionManager) context.getBean("extensionManager");
        eloFactory = (IELOFactory) context.getBean("eloFactory");
        
        // ActionLogger
        actionLogger = (IActionLogger) context.getBean("actionlogger");
        ((ActionLogger) actionLogger).init(xmppConnection);
        
        // SessionManager (Up-to-date?)
        sessionManager = (SessionManager) context.getBean("sessionManager");
        
        // AwarenessService
        awarenessService = (IAwarenessService) context.getBean("awarenessService");
        awarenessService.init(xmppConnection);
        awarenessService.setMUCConferenceExtension(Configuration.getInstance().getOpenFireConference()+"."+Configuration.getInstance().getOpenFireHost());
        logger.debug("Conference extension parameter: " + awarenessService.getMUCConferenceExtension());
        
        // DataSyncService
        dataSyncService = (IDataSyncService) context.getBean("dataSyncService");
        ((DataSyncService)dataSyncService).init(xmppConnection);
        
        // PedagogicalPlan
        pedagogicalPlanService = (PedagogicalPlanService) context.getBean("pedagogicalPlanService");
        
        // NotificationReceiver
        notificationReceiver = (NotificationReceiver) context.getBean("notificationReceiver");
        notificationReceiver.init(xmppConnection);
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
		return this.awarenessService;
	}
    
    @Override
    public IDataSyncService getDataSyncService() {
        return dataSyncService;
    }

    private XMPPConnection getConnection(String userName, String password) {
    	if(xmppConnection == null) {
	        
	        this.userName = userName;
	        this.password = password;
	        
	        //make it a jid
	        if(!userName.contains("@")) {
	        	userName = userName + "@" + Configuration.getInstance().getOpenFireHost();
	        }
	        
	        config = new ConnectionConfiguration(Configuration.getInstance().getOpenFireHost(), Configuration.getInstance().getOpenFirePort());
	        config.setCompressionEnabled(true);
	        config.setReconnectionAllowed(true);
	        this.xmppConnection = new XMPPConnection(config);
	        this.xmppConnection.DEBUG_ENABLED = true;
	
	        try {            
	            this.xmppConnection.connect();
	            SmackConfiguration.setPacketReplyTimeout(100000);
	            SmackConfiguration.setKeepAliveInterval(10000);
                logger.debug("User logging in: " + userName + " " + password);
	            logger.debug("successful connection to xmpp server " + config.getHost() + ":" + config.getPort());
	        } catch (XMPPException e) {
	            logger.error("Error during xmpp connect");
	            e.printStackTrace();
	        }
	        
	        
	        try {
	            this.xmppConnection.login(userName, password);
	            logger.debug("xmpp login ok " + userName + " " + password);
	        } catch (XMPPException e1) {
	            logger.error("xmpp login failed. bummer. " + e1);
	            e1.printStackTrace();
               throw new LoginFailedException(userName);
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
    
    @Override
    public PedagogicalPlanService getPedagogicalPlanService() {
        return pedagogicalPlanService;
    }

	@Override
	public void registerForNotifications(INotifiable notifiable) {
		notificationReceiver.addNotifiable(notifiable);
	}

	@Override
	public IELOFactory getELOFactory() {
		return eloFactory;
	}

}
