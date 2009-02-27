
package eu.scy.collaborationservice;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import eu.scy.core.model.impl.ScyBaseObject;


public class CollaborationService implements ICollaborationService {
    
    private final static Logger logger = Logger.getLogger(CollaborationService.class.getName());
    private ConnectionConfiguration config;
    private XMPPConnection xmppConnection;
    
    private CollaborationService() {        
    }
 
    public static CollaborationService createAwarenessService(String username, String password) {
        CollaborationService as = new CollaborationService();
        
        Properties props = new Properties();
        try {
            props.load(CollaborationService.class.getResourceAsStream("server.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = props.getProperty("collaborationservice.address");
        String port = props.getProperty("collaborationservice.port");
        String name = props.getProperty("collaborationservice.name");
        
        //as.config = new ConnectionConfiguration("wiki.intermedia.uio.no", 5222, "AwarenessService");
        as.config = new ConnectionConfiguration(address, new Integer(port).intValue(), name);
        as.xmppConnection = new XMPPConnection(as.config);
        try {
            as.xmppConnection.connect();
        } catch (XMPPException e) {
            logger.error("Error during connect");
            e.printStackTrace();
        }
        try {
            as.xmppConnection.login(username, password);
        } catch (XMPPException e) {
            logger.error("Error during login");
            e.printStackTrace();
            
        }
        return as;
    }
    

    public void closeCollaborationService() {
        xmppConnection.disconnect();
    }
    
    
    
    public void sendMessage(String recipient, String message) {
        Chat chat = xmppConnection.getChatManager().createChat(recipient, (MessageListener) this);
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            logger.error("Error during sendMessage");
            e.printStackTrace();
        }
    }

    @Override
    public void create(ScyBaseObject scyBaseObject) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void delete(ScyBaseObject scyBaseObject) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void read(String id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(ScyBaseObject scyBaseObject) {
        // TODO Auto-generated method stub
        
    }    
    
    
    
    
}
