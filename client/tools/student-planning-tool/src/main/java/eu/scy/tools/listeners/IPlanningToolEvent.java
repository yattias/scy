package eu.scy.tools.listeners;

import eu.scy.awareness.IAwarenessUser;

/**
 * Interface for general student planning tool events
 * 
 * @author anthonyp
 *
 */
public interface IPlanningToolEvent {
    
	public static String ADD_BUDDY = "ADD_BUDDY";
	public static String ADD_ELO = "ADD_ELO";

	
	/**
	 * type of event
	 * 
	 * @param type
	 */
	public void setType(String type);
	
	/**
	 * get the type of event
	 * 
	 * @return
	 */
	public String getType();
	
   
}