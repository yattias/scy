package eu.scy.collaborationservice.session;

import java.sql.Timestamp;
import java.util.ArrayList;

import eu.scy.communications.message.IScyMessage;



/**
 * Collaboration Session
 * 
 * @author thomasd
 */
public class CollaborationSession implements ICollaborationSession {

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

}
