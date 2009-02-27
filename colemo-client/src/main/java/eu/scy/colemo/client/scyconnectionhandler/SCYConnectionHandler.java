package eu.scy.colemo.client.scyconnectionhandler;

import eu.scy.colemo.client.ConnectionHandler;
import eu.scy.collaborationservice.CollaborationService;
import eu.scy.collaborationservice.ICollaborationService;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 27.feb.2009
 * Time: 13:41:28
 * To change this template use File | Settings | File Templates.
 */
public class SCYConnectionHandler implements ConnectionHandler {
    private ICollaborationService ics;

    public void sendMessage(String message) {

    }

    public void sendObject(Object object) {
        
    }

    public void initialize() throws Exception {
        ics = CollaborationService.getInstance();
    }

    public void cleanUp() {
        
    }
}
