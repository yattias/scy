package eu.scy.communications.message;


public interface IScyMessage {
    
    /**
     * Returns the user name
     * 
     * @return
     */
    public String getUserName();



    /**
     * sets the user name
     * 
     * @param userName
     */
    public void setUserName(String userName);



    /**
     * Gets the tool name
     * 
     * @return
     */
    public String getToolName();
    
    /**
     * Sets the tool name
     * 
     * @param toolName
     */
    public void setToolName(String toolName);
    
    /**
     * Gets the type of object this message represents
     * 
     * @return
     */
    public String getObjectType();
    
    /**
     * Set the object type
     * 
     * @param objectType
     */
    public void setObjectType(String objectType);
    
    /**
     * Get the to
     * 
     * @return
     */
    public String getTo();
    
    /**
     * set the to
     * 
     * @param to
     */
    public void setTo(String to);
    
    /**
     * Get the from
     * 
     * @return
     */
    public String getFrom();
    
    /**
     * sets the from
     * 
     * @param from
     */
    public void setFrom(String from);
    
    /**
     * Get the message purpose
     * 
     * @return
     */
    public String getMessagePurpose();
    
    /**
     * set message purpose
     * 
     * @param messagePurpose
     */
    public void setMessagePurpose(String messagePurpose);
    
    /**
     * Get the expiration
     * 
     * @return
     */
    public long getExpiraton();
    
    /**
     * Sets the expiration of this message
     * 
     * @param expiraton
     */
    public void setExpiraton(long expiraton);
    
    /**
     * Sets the session this message is associated with
     * 
     * @param session
     */
    public void setSession(String session);

    /**
     * Gets the session this message is associated with
     * 
     * @return
     */
    public String getSession();
    
}