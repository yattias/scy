package eu.scy.colemo.client.scyconnectionhandler;

import eu.scy.colemo.client.ConnectionHandler;

import eu.scy.collaborationservice.ICollaborationService;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 27.feb.2009
 * Time: 13:41:28
 * CoLeMo controller that serves as a bridge between colemo and the SCY collaboration and awareness services.
 */
public class SCYConnectionHandler implements ConnectionHandler {
    private ICollaborationService ics;

    public void sendMessage(String message) {
        //ics.sendMessage(message);
    }

    public void sendObject(Object object) {
        //ics.sendObject(object);
        
    }

    public void initialize() throws Exception {
        //ics = CollaborationService.getInstance();
    }

    public void cleanUp() {
        //ics.cleanUp();
        
    }
}
