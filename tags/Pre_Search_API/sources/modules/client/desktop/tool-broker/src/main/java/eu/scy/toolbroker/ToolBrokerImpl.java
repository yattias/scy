package eu.scy.toolbroker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.utils.StringUtils;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.CompletingActionLogger;
import eu.scy.actionlogging.MultiActionLogger;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.api.IContextService;
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
import eu.scy.toolbrokerapi.ConnectionListener;
import eu.scy.toolbrokerapi.LoginFailedException;
import eu.scy.toolbrokerapi.ServerNotRespondingException;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.toolbrokerapi.ToolBrokerAPIRuntimeSetting;
import java.net.URI;

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

	private IContextService contextService;

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

	private URI missionSpecificationURI;

        private URI missionRuntimeURI;

        private List<ConnectionListener> connectionListeners;

	private Map<String, BlockingQueue<INotification>> collaborationAnswers;

	/**
	 * Creates an instance of the ToolBroker, logs in the user (creates the XMPP connection) and
	 * initializes the services. Beware of using this constructor on the UI thread, as it may take
	 * some time to finalizes the initialization.
	 * 
	 * @param username XMPP user name of the user.
	 * @param password XMPP password of the user.
	 * @throws LoginFailedException is thrown when the login fails.
	 */
	public ToolBrokerImpl(String username, String password) throws LoginFailedException {
		this(username, password, false);
	}

	/**
	 * Creates an instance of the ToolBroker, logs in the user (creates the XMPP connection) and
	 * initializes the services depending on parameter avoidServiceInitialization. If calling this
	 * constructor on the UI thread, avoid the service initialization.
	 * 
	 * @param username XMPP user name of the user.
	 * @param password XMPP password of the user.
	 * @param avoidServiceInitialization if true, services will not be initialized.
	 * @throws LoginFailedException is thrown when the login fails.
	 */
	public ToolBrokerImpl(String username, String password, boolean avoidServiceInitialization) throws LoginFailedException {
		getConnection(username, password);

		connectionListeners = new CopyOnWriteArrayList<ConnectionListener>();
		
		if (!avoidServiceInitialization) {
			initializeServices();
		}
	}
	
	/**
	 * Creates and instance of ToolBroker (for more details see {@link #ToolBrokerImpl(String, String)} and adds connection listener.
	 * 
	 * @param connectionListener the connection listener to be added
	 */
	public ToolBrokerImpl(String username, String password, ConnectionListener connectionListener) {
		this(username, password, false, connectionListener);
	}
	
	/**
	 * Creates and instance of ToolBroker (for more details see {@link #ToolBrokerImpl(String, String, boolean))} and adds connection listener.
	 * 
	 * @param connectionListener the connection listener to be added
	 */
	public ToolBrokerImpl(String username, String password, boolean avoidServiceInitialization, ConnectionListener connectionListener) {
		this(username, password, false);
		addConnectionListener(connectionListener);
	}

	/*
	 * (non-Javadoc)
	 * @see eu.scy.toolbrokerapi.ToolBrokerAPI#addConnectionListener(eu.scy.toolbrokerapi.ConnectionListener)
	 */
	@Override
	public void addConnectionListener(ConnectionListener connectionListener) {
		if (!connectionListeners.contains(connectionListener)) {
			connectionListeners.add(connectionListener);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see eu.scy.toolbrokerapi.ToolBrokerAPI#removeConnectionListener(eu.scy.toolbrokerapi.ConnectionListener)
	 */
	@Override
	public void removeConnectionListener(ConnectionListener connectionListener) {
		connectionListeners.remove(connectionListener);
	}

	/**
	 * Do not call this method. Use {@link #initializeServices()} instead.
	 */
	@Deprecated
	public void getReadyForUser() {
		initializeServices(defaultBeanConfigurationFile);
	}
	
	/**
	 * Initializes the TBI services with the default configuration.
	 */
	public void initializeServices() {
		initializeServices(defaultBeanConfigurationFile);
	}

	/**
	 * Initializes the TBI services with the configuration file from the parameter.
	 * 
	 * @param beanConfigurationFile String path to the spring configuration file of the services. 
	 */
	public void initializeServices(String beanConfigurationFile) {
		String beanConfigFile = beanConfigurationFile;
		if (StringUtils.isEmpty(beanConfigFile)) {
			beanConfigFile = defaultBeanConfigurationFile;
		}
		context = new ClassPathXmlApplicationContext(beanConfigFile);

		// RoOLO
		repository = (IRepository) context.getBean("repository");
		metaDataTypeManager = (IMetadataTypeManager) context.getBean("metadataTypeManager");
		extensionManager = (IExtensionManager) context.getBean("extensionManager");
		eloFactory = (IELOFactory) context.getBean("eloFactory");

		// ActionLogger
		actionLogger = (IActionLogger) context.getBean("actionlogger");
		IActionLogger internalLogger = ((CompletingActionLogger) actionLogger).getInternalLogger();
		List<IActionLogger> loggers = ((MultiActionLogger) internalLogger).getLoggers();
		for (IActionLogger logger : loggers) {
                    if (logger instanceof ActionLogger) {
                        ((ActionLogger) logger).init(xmppConnection);
                    }
                }
		
		
		// ContextService
		contextService = (IContextService) context.getBean("contextservice");
                contextService.setUsername(userName);
                
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

		// student planning service

		// studentPedagogicalPlanService = this.getStudentPlanService();
		setStudentPedagogicalPlanService((StudentPedagogicalPlanService) context.getBean("studentPedagogicalPlanService"));

		// NotificationReceiver
		notificationReceiver = (NotificationReceiver) context.getBean("notificationReceiver");
		notificationReceiver.init(xmppConnection);

		// collaboration
		collaborationAnswers = new HashMap<String, BlockingQueue<INotification>>();
		registerForNotifications(new INotifiable() {

			@Override
			public boolean processNotification(INotification notification) {
				if (notification.getToolId().equals("scylab") && notification.getFirstProperty("type") != null && notification.getFirstProperty("type").equals("collaboration_response")) {
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
                                return true;
			}
		});
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
	 * 
	 * @see eu.scy.toolbrokerapi.ToolBrokerAPI#getRepository()
	 */
	@Override
	public IRepository getRepository() {
		return repository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.scy.toolbrokerapi.ToolBrokerAPI#getMetaDataTypeManager()
	 */
	@Override
	public IMetadataTypeManager getMetaDataTypeManager() {
		return metaDataTypeManager;
	}

	/*
	 * (non-Javadoc)
	 * 
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

			/*
			 * The reconnection mechanism will try to reconnect periodically:
			 * (from JavaDoc) - For the first minute it will attempt to connect
			 * once every ten seconds. - For the next five minutes it will
			 * attempt to connect once a minute. - If that fails it will
			 * indefinitely try to connect once every five minutes.
			 */

			this.xmppConnection.addConnectionListener(new org.jivesoftware.smack.ConnectionListener() {

				@Override
				public void connectionClosed() {
					logger.debug("TBI connection was closed. Informing listeners...");
					for (Iterator<ConnectionListener> iterator = connectionListeners.iterator(); iterator.hasNext();) {
						iterator.next().connectionClosed();
					}
				}

				@Override
				public void connectionClosedOnError(Exception e) {
					logger.debug("TBI server error closed;");
					for (Iterator<ConnectionListener> iterator = connectionListeners.iterator(); iterator.hasNext();) {
						iterator.next().connectionClosedOnError(e);
					}
				}

				@Override
				public void reconnectingIn(int seconds) {
					logger.debug("TBI server reconnecting;");
					for (Iterator<ConnectionListener> iterator = connectionListeners.iterator(); iterator.hasNext();) {
						iterator.next().reconnectingIn(seconds);
					}
				}

				@Override
				public void reconnectionFailed(Exception e) {
					logger.debug("TBI server reconnecting failed");
					for (Iterator<ConnectionListener> iterator = connectionListeners.iterator(); iterator.hasNext();) {
						iterator.next().reconnectionFailed(e);
					}
				}

				@Override
				public void reconnectionSuccessful() {
					logger.debug("TBI server reconnecting success");
					for (Iterator<ConnectionListener> iterator = connectionListeners.iterator(); iterator.hasNext();) {
						iterator.next().reconnectionSuccessful();
					}
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
		logger.debug("==========XMPPName: " + xmppConnection.getUser());
		final IActionLogger log = getActionLogger();
		final IAction requestCollaborationAction = new Action();
		requestCollaborationAction.setUser(xmppConnection.getUser());
		requestCollaborationAction.setType("collaboration_request");
		requestCollaborationAction.addContext(ContextConstants.tool, "scylab");
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

	@Override
   public URI getMissionRuntimeURI()
   {
      return missionRuntimeURI;
   }

   @Override
   public void setMissionRuntimeURI(URI missionRuntimeURI)
   {
      logger.info("setMissionRuntimeURI: "+missionRuntimeURI.toString());
      this.missionRuntimeURI = missionRuntimeURI;
   }

   @Override
   public URI getMissionSpecificationURI() {
       return missionSpecificationURI;
   }

   @Override
   public void setMissionSpecificationURI(URI missionSpecificationURI) {
       this.missionSpecificationURI = missionSpecificationURI;
   }

   @Deprecated
   public String getMission() {
       return "ToolBrokerAPI.getMission() is deprecated";
   }

   @Deprecated
   @Override
   public void setMissionId(String missionId) {
       // do nothing, deprecated
   }

    @Override
    public String getLoginUserName() {
            return userName;
    }

    public void setStudentPedagogicalPlanService(StudentPedagogicalPlanService studentPedagogicalPlanService) {
            this.studentPedagogicalPlanService = studentPedagogicalPlanService;
    }

    @Override
    public StudentPedagogicalPlanService getStudentPedagogicalPlanService() {
            return studentPedagogicalPlanService;
    }

    @Override
    public IContextService getContextService() {
        return contextService;
    }
    
}
