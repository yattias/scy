package eu.scy.toolbroker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.logger.ActionLogger;
import eu.scy.awareness.IAwarenessService;
import eu.scy.client.common.datasync.DataSyncService;
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.client.notification.NotificationReceiver;
import eu.scy.common.configuration.Configuration;
import eu.scy.notification.api.INotifiable;
import eu.scy.notification.api.INotification;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;
import eu.scy.server.pedagogicalplan.StudentPedagogicalPlanService;
import eu.scy.sessionmanager.SessionManager;
import eu.scy.toolbrokerapi.LoginFailedException;
import eu.scy.toolbrokerapi.ServerNotRespondingException;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.toolbrokerapi.ToolBrokerAPIRuntimeSetting;

/**
 * This class implements the ToolBrokerAPI interface and provides all the
 * services. The main idea of this implementation is that all the services are
 * injected by Spring into this class and then can be accessed by all clients.
 * 
 * @author Giemza
 */
public class ToolBrokerImpl implements ToolBrokerAPI, ToolBrokerAPIRuntimeSetting {

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

    private StudentPedagogicalPlanService studentPedagogicalPlanService;
    
    private NotificationReceiver notificationReceiver;

    private String userName;

    private String password;

    private String mission;

    private Map<String, BlockingQueue<INotification>> collaborationAnswers;

    public ToolBrokerImpl(String username, String password) {
        this(username, password, defaultBeanConfigurationFile);
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
        awarenessService.setMUCConferenceExtension(Configuration.getInstance().getOpenFireConference() + "." + Configuration.getInstance().getOpenFireHost());
        logger.debug("Conference extension parameter: " + awarenessService.getMUCConferenceExtension());

        // DataSyncService
        dataSyncService = (IDataSyncService) context.getBean("dataSyncService");
        ((DataSyncService) dataSyncService).init(xmppConnection);

        // PedagogicalPlan
        pedagogicalPlanService = (PedagogicalPlanService) context.getBean("pedagogicalPlanService");
        
        //student planning service
        
        //studentPedagogicalPlanService = this.getStudentPlanService();
        setStudentPedagogicalPlanService((StudentPedagogicalPlanService) context.getBean("studentPedagogicalPlanService"));

        // NotificationReceiver
        notificationReceiver = (NotificationReceiver) context.getBean("notificationReceiver");
        notificationReceiver.init(xmppConnection);

        // collaboration
        collaborationAnswers = new HashMap<String, BlockingQueue<INotification>>();
        registerForNotifications(new INotifiable() {

            @Override
            public void processNotification(INotification notification) {
                if (notification.getToolId().equals("scylab") && notification.getFirstProperty("type")!=null&&notification.getFirstProperty("type").equals("collaboration_response")) {
                    String proposingUser = notification.getFirstProperty("proposing_user");
                    String proposedUser = notification.getFirstProperty("proposed_user");
                    String elo = notification.getFirstProperty("proposed_elo");
                    BlockingQueue<INotification> q = collaborationAnswers.remove(proposingUser + "#" + proposedUser + "#" + elo);
                    if (q != null) {
                        q.add(notification);
                    } else {
                        logger.warn("Received collaboration response that could not be mapped to a request, from user " + proposingUser + " to user " + proposedUser + ", elouri is " + elo);
                    }
                }
            }
        });
        
       
        
        
    }

//    public StudentPedagogicalPlanService getStudentPlanService() {
//
//		// service =
//		// getWithUrl("http://localhost:8080/server-external-components/remoting/studentPlan-httpinvoker");
////		if (studentPedagogicalPlanService == null)
//		Configuration configuration = Configuration.getInstance();
//		studentPedagogicalPlanService = getWithUrl(configuration.getStudentPlanningToolUrl());
//			//studentPedagogicalPlanService = getWithUrl("http://scy.collide.info:8080/extcomp/remoting/studentPlan-httpinvoker");
//
//		return studentPedagogicalPlanService;
//
//	}
//
//    private StudentPedagogicalPlanService getWithUrl(String url) {
//		HttpInvokerProxyFactoryBean fb = new HttpInvokerProxyFactoryBean();
//		fb.setServiceInterface(StudentPedagogicalPlanService.class);
//		fb.setServiceUrl(url);
//		fb.afterPropertiesSet();
//		return (StudentPedagogicalPlanService) fb.getObject();
//	}
    
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
    @Override
    public IRepository getRepository() {
        return repository;
    }

    /*
     * (non-Javadoc)
     * @see eu.scy.toolbrokerapi.ToolBrokerAPI#getMetaDataTypeManager()
     */
    @Override
    public IMetadataTypeManager getMetaDataTypeManager() {
        return metaDataTypeManager;
    }

    /*
     * (non-Javadoc)
     * @see eu.scy.toolbrokerapi.ToolBrokerAPI#getExtensionManager()
     */
    @Override
    public IExtensionManager getExtensionManager() {
        return extensionManager;
    }

    @Override
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
        if (xmppConnection == null) {

            this.userName = userName;
            this.password = password;

            // make it a jid - no we don't!
//            if (!userName.contains("@")) {
//                userName = userName + "@" + Configuration.getInstance().getOpenFireHost();
//            }

            config = new ConnectionConfiguration(Configuration.getInstance().getOpenFireHost(), Configuration.getInstance().getOpenFirePort());
            config.setCompressionEnabled(true);
            config.setReconnectionAllowed(true);
            config.setSecurityMode(SecurityMode.disabled);
            this.xmppConnection = new XMPPConnection(config);
            XMPPConnection.DEBUG_ENABLED = false;

            try {
                this.xmppConnection.connect();
                logger.debug("Successful connection to xmpp server " + config.getHost() + ":" + config.getPort());
                SmackConfiguration.setPacketReplyTimeout(100000);
                SmackConfiguration.setKeepAliveInterval(10000);
            } catch (XMPPException e) {
                logger.error("Error during xmpp connect");
                e.printStackTrace();
                throw new ServerNotRespondingException(config.getHost(), config.getPort());
            }

            try {
            	logger.debug("User logging in: " + userName + " " + password);
                this.xmppConnection.login(userName, password);
                logger.debug("Login successful" + userName + " " + password);
            } catch (XMPPException e) {
                logger.error("Login failed  " + e);
                e.printStackTrace();
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

    @Override
    public void proposeCollaborationWith(String proposedUser, String elouri, String mucid) {
        logger.debug("TBI: proposeCollaborationWith: user: " + proposedUser + " eloid: " + elouri);
        // callback.receivedCollaborationResponse(elouri, proposedUser);
        final LinkedBlockingQueue<INotification> queue = new LinkedBlockingQueue<INotification>();
        collaborationAnswers.put(xmppConnection.getUser() + "#" + proposedUser + "#" + elouri, queue);
        logger.debug("==========XMPPName: "+xmppConnection.getUser());
        final IActionLogger log = getActionLogger();
        final IAction requestCollaborationAction = new Action();
        requestCollaborationAction.setUser(xmppConnection.getUser());
        requestCollaborationAction.setType("collaboration_request");
        requestCollaborationAction.addContext(ContextConstants.tool, "scylab");
        requestCollaborationAction.addContext(ContextConstants.mission, mission);
        // TODO replace with real session
        requestCollaborationAction.addContext(ContextConstants.session, "mysession");
        requestCollaborationAction.addAttribute("proposed_user", proposedUser);
        requestCollaborationAction.addAttribute("proposed_elo", elouri);
        if (mucid != null && !mucid.isEmpty()) {
            requestCollaborationAction.addAttribute("mucid", mucid);
        }
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                log.log(requestCollaborationAction);
                try {
                    INotification notif = queue.take();
                    boolean accepted = Boolean.parseBoolean(notif.getFirstProperty("accepted"));
                    if (accepted) {
                        String mucid = notif.getFirstProperty("mucid");
                        return;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    @Override
    public String answerCollaborationProposal(boolean accept, String proposingUser, String elouri) {
        LinkedBlockingQueue<INotification> queue = new LinkedBlockingQueue<INotification>();
        collaborationAnswers.put(proposingUser + "#" + xmppConnection.getUser() + "#" + elouri, queue);
        IActionLogger log = getActionLogger();
        IAction collaborationResponseAction = new Action();
        collaborationResponseAction.setUser(xmppConnection.getUser());
        collaborationResponseAction.setType("collaboration_response");
        collaborationResponseAction.addContext(ContextConstants.tool, "scylab");
        collaborationResponseAction.addContext(ContextConstants.mission, mission);
        // TODO replace with real session
        collaborationResponseAction.addContext(ContextConstants.session, "mysession");
        collaborationResponseAction.addAttribute("request_accepted", String.valueOf(accept));
        collaborationResponseAction.addAttribute("proposing_user", proposingUser);
        collaborationResponseAction.addAttribute("proposed_elo", elouri);
        log.log(collaborationResponseAction);
        if (accept) {
            try {
                INotification notif = queue.take();
                String mucid = notif.getFirstProperty("mucid");
                return mucid;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    @Override
    public String getMission() {
        return mission;
    }

    @Override
   public void setMissionId(String missionId)
   {
        mission = missionId;
    }

    @Override
    public String getLoginUserName() {
        return userName;
    }

  
	public void setStudentPedagogicalPlanService(
			StudentPedagogicalPlanService studentPedagogicalPlanService) {
		this.studentPedagogicalPlanService = studentPedagogicalPlanService;
	}

    @Override
	public StudentPedagogicalPlanService getStudentPedagogicalPlanService() {
		return studentPedagogicalPlanService;
	}
}
