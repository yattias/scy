package eu.scy.tools.listeners;

import eu.scy.awareness.IAwarenessUser;

public interface IBuddyEvent extends IPlanningToolEvent {

	/**
     * Gets the user
     * @return
     */
   public IAwarenessUser getUser();
   
   /**
    * sets the user
    * 
    * @param user
    */
   public void setUser(IAwarenessUser user);
   
}
