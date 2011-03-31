package eu.scy.awareness.event;

import eu.scy.awareness.IAwarenessUser;

/**
 * Interface for general awareness events
 * 
 * @author anthonyp
 *
 */
public interface IAwarenessEvent {

    /**
     * gets the room id
     *
     * @return
     */
    public String getRoomId();

    /**
     * sets the room id
     *
     */
    public void setRoomId(String roomId);

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

    /**
     * Gets the message associated with this event
     *
     * @return
     */
    public String getMessage();

    /**
     * Sets the user
     * @return
     */
    public void setIAwarenessUser(IAwarenessUser user);

    /**
     * Returns the timestanp
     *
     * @return timestamp
     */
    public long getTimestamp();

    /**
     * Sets the timestamp
     *
     * @param timestamp
     */
    public void setTimestamp(long timestamp);
}
