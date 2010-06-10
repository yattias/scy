/**
 * 
 */
package eu.scy.common.message;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import eu.scy.common.datasync.ISyncObject;

/**
 * @author giemza
 *
 */
public class SyncMessage {
	
	public static final String PATH = "syncmessage";
	
	public enum Type {
		request, command, answer
	}

	public enum Event {
		create, kill, queryall, queryone
	}
	
	public enum Response {
		success, failure
	}

	private static final Logger logger = Logger.getLogger(SyncMessage.class.getName());    

    private Type type;
    private Response response;
    private Event event;
    
    private String toolId;
    private String userId;
    
    private String sessionId;
    
    private String message;
    
    private List<ISyncObject> syncobjects;
    
    public SyncMessage(Type type) {
    	this();
    	this.type = type;
    	
    	logger.info("SyncMessage created with type " + type);
    }
    
    /**
     * Default constructor of SyncCommand.
     * Should only be used by DataSyncCommandPacketTransformer!
     */
    public SyncMessage() {
    	syncobjects = new ArrayList<ISyncObject>();
    }

    /**
     * @return the type
     */
    public Type getType() {
		return type;
	}       
    
	/**
	 * @return the toolId
	 */
	public String getToolId() {
		return toolId;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @return the event
	 */
	public Event getEvent() {
		return event;
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the response
	 */
	public Response getResponse() {
		return response;
	}
	
	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * @param toolId the toolId to set
	 */
	public void setToolId(String toolId) {
		this.toolId = toolId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(Event event) {
		this.event = event;
	}
	
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(Response response) {
		this.response = response;
	}
	
	/**
	 * @param syncobjects
	 */
	public void setSyncobjects(List<ISyncObject> syncobjects) {
		this.syncobjects = syncobjects;
	}
	
	/**
	 * @return the list of syncobjects
	 */
	public List<ISyncObject> getSyncObjects() {
		return syncobjects;
	}
}