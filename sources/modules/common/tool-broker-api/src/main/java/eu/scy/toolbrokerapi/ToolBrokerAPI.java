package eu.scy.toolbrokerapi;

import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.api.IContextService;
import eu.scy.awareness.IAwarenessService;
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.common.scyelo.RooloServices;
import eu.scy.notification.api.INotifiable;
import eu.scy.sessionmanager.SessionManager;
import java.net.URI;

/**
 * This is the interface of the Tool-Broker-API. The Tool-Broker-API can return
 * references to all SCY broker services like the repository, the user
 * management etc. These services can then be used by the tools.
 * 
 * @author Giemza
 */
public interface ToolBrokerAPI extends RooloServices {

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
     * @return the context service
     */
    public IContextService getContextService();
    
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
     * @deprecated Use (@link getMissionRuntimeURI()) or (@getMissionSpecificationURI()) instead.
     * @return the mission
     */
    @Deprecated
    public String getMission();

    /**
     * Returns the current user's "personal" missionRuntimeURI
     * See also (@link getMissionSpecificationURI()).
     * @return the missionRuntimeURI
     */
    public URI getMissionRuntimeURI();

    /**
     * Returns the URI of the currently used mission specification.
     * @return the missionSpecificationURI
     */
    public URI getMissionSpecificationURI();

    /**
     * Returns the name, with which the user logged in.
     *
     * @return login user name
     */
    public String getLoginUserName();

    /**
     * Removes the connection listener.
     * 
     * @param connectionListener listener to be removed.
     */
	public void removeConnectionListener(ConnectionListener connectionListener);

	/**
	 * Adds the connection lister if not added yet.
	 * 
	 * @param connectionListener listener to be added.
	 */
	public void addConnectionListener(ConnectionListener connectionListener);

}
