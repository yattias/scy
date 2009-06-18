package eu.scy.datasync.impl.session;

import java.sql.Timestamp;
import java.util.ArrayList;

import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessage;
import eu.scy.datasync.api.session.IDataSyncSession;



/**
 * CollaborationSession
 * 
 * @author thomasd
 */
public class DataSyncSession implements IDataSyncSession {
    
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
    public ArrayList<ISyncMessage> getUsers() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public ISyncMessage convertToSyncMessage() {
        return SyncMessage.createSyncMessage(this.getId(), toolName, userName, null, this.getClass().getName(), this.getPersistenceId(), DataSyncSession.DEFAULT_SESSION_EXPIRATION_TIME);
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
