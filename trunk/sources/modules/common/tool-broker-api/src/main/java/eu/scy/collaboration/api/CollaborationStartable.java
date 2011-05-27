package eu.scy.collaboration.api;

import eu.scy.client.common.datasync.ISyncSession;

/**
 * A tool implementing this interface should be collaborative. 
 * Scy-Lab can now determine if collaboration is possible and if the collaboration should be initiated via Drag and Drop
 * the acceptDrop and canAcceptDrop methods from ScyTool interface should be implemented in a useful way by the tool.
 * @author Sven
 */
public interface CollaborationStartable {

    /**
     * This method is called when collaboration is initiated. 
     * A useful implementation would be creating an {@link ISyncSession} and register listeners on it (e.g. {@link ISyncSession#addCollaboratorStatusListener(eu.scy.client.common.datasync.CollaboratorStatusListener)}).
     * @param mucid The mucId for the ISyncSession
     */
    public void startCollaboration(String mucid);
}
