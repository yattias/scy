package eu.scy.collaborationservice.session;

import java.sql.Timestamp;
import java.util.ArrayList;

import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;



/**
 * CollaborationSession
 * 
 * @author thomasd
 */
public class CollaborationSession implements ICollaborationSession {
    
    public static final long DEFAULT_SESSION_EXPIRATION_TIME = 60*60*1000; // one hour

    private String id;
    private String persistenceId;
    private String toolName;
    private String userName;

    
    @Override
    public String getPersistenceId() {
        return persistenceId;
    }
    
    @Override
    public void setPersistenceId(String persistenceId) {
        this.persistenceId = persistenceId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void setTool(String toolName) {
        this.toolName = toolName;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void expire() {
        // TODO Auto-generated method stub        
    }

    @Override
    public Timestamp getExpirationDate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<IScyMessage> getUsers() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public IScyMessage convertToScyMessage() {
        return ScyMessage.createScyMessage(userName, toolName, String.valueOf(this.hashCode()), this.getClass().getName(), "some name", "some description", null, null, null, CollaborationSession.DEFAULT_SESSION_EXPIRATION_TIME, this.getId());
    }

    @Override
    public void renew() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setUser(String arg0) {
        // TODO Auto-generated method stub
        
    }

}
