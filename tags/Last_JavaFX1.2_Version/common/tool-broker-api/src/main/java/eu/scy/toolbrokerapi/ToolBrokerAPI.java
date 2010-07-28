package eu.scy.toolbrokerapi;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.awareness.IAwarenessService;
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.notification.api.INotifiable;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;
import eu.scy.server.pedagogicalplan.StudentPedagogicalPlanService;
import eu.scy.sessionmanager.SessionManager;

/**
 * This is the interface of the Tool-Broker-API. The Tool-Broker-API can return
 * references to all SCY broker services like the repository, the user
 * management etc. These services can then be used by the tools.
 * 
 * @author Giemza
 */
public interface ToolBrokerAPI {

    /**
     * This method returns the repository instance to be used by clients of the
     * ToolsBrokerAPI.
     * 
     * @return repository the repository instace
     */
    public IRepository getRepository();

    /**
     * @return the metaDataTypeManager
     */
    public IMetadataTypeManager getMetaDataTypeManager();

    /**
     * @return the extensionManager
     */
    public IExtensionManager getExtensionManager();

    public IELOFactory getELOFactory();

    /**
     * The session manager for handling user authentication and authorisation
     * @param username
     * @param password
     * @return
     */
    public SessionManager getUserSession(String username, String password);

    /**
     * @return the action logger
     */
    public IActionLogger getActionLogger();

    /**
     * Registers the {@link INotifiable} for notifications
     */
    public void registerForNotifications(INotifiable notifiable);

    /**
     * @return the awareness service
     */
    public IAwarenessService getAwarenessService();

    /**
     * @return the data sync service
     */
    public IDataSyncService getDataSyncService();

    /**
     * 
     * Returns the pedagogical plan service
     * 
     * @return the pedagogical plan service
     */
    public PedagogicalPlanService getPedagogicalPlanService();
    
    /**
     * 
     * Returns the student pedagogical plan service
     * 
     * @return the student pedagogical plan service
     */
    public StudentPedagogicalPlanService getStudentPedagogicalPlanService();

    /**
     * Sends a message via the collaboration agent to another user to invite him for a
     * collaboration.
     * 
     * @param proposedUser
     *            the complete clientjid (user@server) of the invited user
     * @param elouri
     *            the URI of the ELO
     * @param mucid
     *            the optional id of the already created muc, can be null or empty, if there is no muc, yet
     */
    public void proposeCollaborationWith(String proposedUser, String elouri, String mucid);

    /**
     * Sends the answer to a previous collaboration proposal.
     * 
     * @param accept
     *            true, if the user wants to collaborate, false otherwise
     * @param proposingUser
     *            the user that sent the collaboration invitation. This should be the same username,
     *            that was delivered in the notification call to the tool.
     * @param elouri
     *            the uri of the elo. This should be the same elouri, that was delivered in the
     *            notification call to the tool.
     * @return the mucid if the user accepted the invitation, null otherwise
     */
    public String answerCollaborationProposal(boolean accept, String proposingUser, String elouri);

    /**
     * Returns the name of the mission the user is currently working on.
     *
     * @return the mission
     */
    public String getMission();

    /**
     * Returns the name, with which the user logged in.
     *
     * @return login user name
     */
    public String getLoginUserName();

}
