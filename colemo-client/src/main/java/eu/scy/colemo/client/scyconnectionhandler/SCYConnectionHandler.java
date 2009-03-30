package eu.scy.colemo.client.scyconnectionhandler;

import eu.scy.colemo.client.ConnectionHandler;
import eu.scy.colemo.client.ApplicationController;
import eu.scy.colemo.client.sqlspacesimpl.MessageTranslator;
import eu.scy.colemo.client.sqlspacesimpl.ConnectionHandlerSqlSpaces;
import eu.scy.colemo.contributions.AddClass;
import eu.scy.colemo.contributions.MoveClass;
import eu.scy.colemo.contributions.BaseConceptMapNode;
import eu.scy.colemo.server.uml.UmlLink;

import eu.scy.collaborationservice.ICollaborationService;
import eu.scy.collaborationservice.CollaborationServiceException;
import eu.scy.collaborationservice.CollaborationServiceFactory;
import eu.scy.collaborationservice.session.ICollaborationSession;
import eu.scy.collaborationservice.event.ICollaborationServiceEvent;
import eu.scy.collaborationservice.event.ICollaborationServiceListener;
import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Bj�rge N�ss
 * Date: 27.feb.2009
 * Time: 13:41:28
 * CoLeMo controller that serves as a bridge between colemo and the SCY collaboration and awareness services.
 */
public class SCYConnectionHandler  extends ConnectionHandlerSqlSpaces implements ConnectionHandler, ICollaborationServiceListener {


    private static final Logger log = Logger.getLogger(ConnectionHandlerSqlSpaces.class.getName());
    private ICollaborationService collaborationService;
    private ICollaborationSession collaborationSession;
    //FIXME: this needs to come from the client, generated
    //public static final String SESSIONID = "1";

    //private String sessionId = SESSIONID;





    public void sendObject(Object object) {
        System.out.println("Sending object:" + object);

        MessageTranslator mt = new MessageTranslator();
        mt.setSessionId(collaborationSession.getId());
        IScyMessage sendMe = null;

        if (object instanceof AddClass) {
            AddClass addClass = (AddClass) object;
            System.out.println("NAME: " + addClass.getName());
            sendMe = mt.getScyMessage(addClass);
        } else if (object instanceof MoveClass) {
            MoveClass mc = (MoveClass) object;
            mc.setId(mc.getUmlClass().getId());
            System.out.println("moving class with id:  " + mc.getId() + " to position: " + mc.getUmlClass().getX() + ", " + mc.getUmlClass().getY());
            sendMe = mt.getScyMessage(mc);
        } else if (object instanceof UmlLink) {
            UmlLink addLink = (UmlLink) object;
            System.out.println("Adding link");
            sendMe = mt.getScyMessage(addLink);
        }

        if (sendMe != null) {
            log.debug("Sending ScyMessage: \n" + sendMe.toString());
            try {
                collaborationService.create(sendMe);
            } catch (CollaborationServiceException e) {
                log.error("Bummer. Create in CollaborationService failed somehow: \n" + e);
                e.printStackTrace();
            }
        }
    }

    public void initialize() throws Exception {
        joinSession(null);
   }

    public void joinSession(String sessionId) {
        ApplicationController.getDefaultInstance().getGraphicsDiagram().clearAll();
        log.info("initializing session: " + sessionId);
        MessageTranslator ot = new MessageTranslator();

        ArrayList<IScyMessage> messages = new ArrayList<IScyMessage>();
        try {
            collaborationService = CollaborationServiceFactory.getCollaborationService(CollaborationServiceFactory.LOCAL_STYLE);
            collaborationService.addCollaborationListener(this);
            if (sessionId == null) {
                collaborationSession = collaborationService.createSession("SCYMapper", "Henrik");                
            } else {
                collaborationSession = collaborationService.joinSession(sessionId, "Henrik", "SCYMapper"); 
            }
            log.debug("joined session: " + collaborationSession.getId());
            messages = collaborationService.synchronizeClientState("Henrik","SCYMapper", collaborationSession.getId(), true);
            log.debug("got ###: " + messages.size() + " messages");
        } catch (CollaborationServiceException e) {
            log.error("CollaborationService failure: " + e);
            e.printStackTrace();
        }

        synchronizeDiagramElements(messages, ot);
    }

    private void synchronizeDiagramElements(ArrayList<IScyMessage> messages, MessageTranslator ot) {
        if(messages == null) {
            log.info("Messages are null");
            return;
        }
        ScyMessage scyMessage;
        for (int i = 0; i < messages.size(); i++) {
            scyMessage = (ScyMessage) messages.get(i);
            Object newNOde = ot.getObject(scyMessage);
            addNewNode(newNOde);
        }
        //TODO: Find a better place for this
        ApplicationController.getDefaultInstance().getColemoPanel().setBounds(0, 0, 800, 700);
    }

                                                                                                                                                                            
    public void cleanUp() {
        //TODO: implement this in collaborationservice
//        try {
//            tupleSpace.takeAll(conceptTemplate);
//        } catch (TupleSpaceException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
    }

    /**
     * Handles collaboration service events
     */
    public void handleCollaborationServiceEvent(ICollaborationServiceEvent e) {
        log.debug("got CollaborationServiceEvent");
        IScyMessage sm = e.getScyMessage();
        if (sm != null) {
            log.debug("UPDATING FROM SERVER!");
            MessageTranslator mt = new MessageTranslator();
            log.debug("CALL: Before add class");
            Object object = mt.getObject(sm);
            if ( sm.getObjectType().contains("AddClass")) {
                addNewNode((BaseConceptMapNode) object);
            } else if (sm.getObjectType().contains("UmlLink")) {
                addNewNode((UmlLink) object);
            }
        }
    }

    public void sendMessage(String message) {
        System.out.println("SCYConnectionHandler.sendMessage()");
    }
}
