package eu.scy.communications.datasync.session;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessageHelper;



/**
 * CollaborationSession
 * 
 * @author thomasd
 */
public class DataSyncSession implements IDataSyncSession {
    
    public static final long DEFAULT_SESSION_EXPIRATION_TIME = 60*60*1000; // one hour

    private String id;
    private String persistenceId;
    private String toolId;
    private List<String> users = new ArrayList<String>();
    
    @Override
    public String getPersistenceId() {
        return persistenceId;
    }
    
    @Override
    public void setPersistenceId(String persistenceId) {
        this.persistenceId = persistenceId;
    }

    @Override
    public void setToolId(String toolName) {
        this.toolId = toolName;
    }
    
    @Override
    public String getToolId() {
        return this.toolId;
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
    }

    @Override
    public Timestamp getExpirationDate() {
        return null;
    }

    @Override
    public void renew() {
        
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    @Override
    public void addUser(String userName) {
        if(  userName != null &&  !this.users.contains(userName) )
            this.users.add(userName);
    }

    @Override
    public List<String> getUsers() {
        return this.users;
    }

    @Override
    public void removeUser(String userName) {
        if( userName != null && this.users.contains(userName) )
            this.users.remove(userName);
    }

}
