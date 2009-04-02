package eu.scy.colemo.client.scyconnectionhandler;

import eu.scy.colemo.client.ConnectionHandler;
import eu.scy.colemo.client.ApplicationController;
import eu.scy.colemo.client.sqlspacesimpl.MessageTranslator;
import eu.scy.colemo.client.sqlspacesimpl.ConnectionHandlerSqlSpaces;
import eu.scy.colemo.contributions.AddClass;
import eu.scy.colemo.contributions.MoveClass;
import eu.scy.colemo.server.uml.UmlLink;
import eu.scy.colemo.server.uml.UmlClass;

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
public class SCYConnectionHandler extends ConnectionHandlerSqlSpaces implements ConnectionHandler, ICollaborationServiceListener {


    private static final Logger log = Logger.getLogger(ConnectionHandlerSqlSpaces.class.getName());
    private ICollaborationService collaborationService;
    private ICollaborationSession collaborationSession;
    //FIXME: this needs to come from the client, generated
    public static final String SESSIONID = "SCYMapper-Session";

    private String sessionId = SESSIONID;
    public static final String USER_NAME = "ScyMappingForPeace";


    public void updateObject(Object object) {
        MessageTranslator mt = new MessageTranslator();
        mt.setSessionId(collaborationSession.getId());
        IScyMessage sendMe = null;
        try {
            if (object instanceof AddClass) {
                AddClass addClass = (AddClass) object;
                System.out.println("NAME: " + addClass.getName());
                sendMe = mt.getScyMessage(addClass);
                collaborationService.update(sendMe, sendMe.getId());
            } else if (object instanceof UmlClass) {
                log.info("UPDATING CONCEPT : " + object);
                sendMe = mt.getScyMessage(object);
                collaborationService.update(sendMe, sendMe.getId());
            } else if (object instanceof UmlLink) {
                log.info("UPDATING LINK : " + object);
                sendMe = mt.getScyMessage(object);
                collaborationService.update(sendMe, sendMe.getId());
            }
        } catch (CollaborationServiceException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Object object) {
        System.out.println("Sending object:" + object);

        MessageTranslator mt = new MessageTranslator();
        mt.setSessionId(collaborationSession.getId());
        IScyMessage sendMe = null;

        try {
            if (object instanceof AddClass) {
                AddClass addClass = (AddClass) object;
                System.out.println("NAME: " + addClass.getName());
                sendMe = mt.getScyMessage(addClass);
                collaborationService.create(sendMe);
            } else if (object instanceof MoveClass) {
                MoveClass mc = (MoveClass) object;
                mc.setId(mc.getUmlClass().getId());
                sendMe = mt.getScyMessage(mc);
                collaborationService.update(sendMe, mc.getId());
            } else if (object instanceof UmlLink) {
                UmlLink addLink = (UmlLink) object;
                sendMe = mt.getScyMessage(addLink);
                collaborationService.create(sendMe);
            }
        } catch (CollaborationServiceException e) {
            e.printStackTrace();
        }
    }

    public void initialize() throws Exception {
        joinSession();

    }

    public void joinSession() {
        setSessionId(SESSIONID);
        log.info("JOINING SESSION " + sessionId + " NOW!");
        ApplicationController.getDefaultInstance().getGraphicsDiagram().clearAll();


        try {
            initializeCollaborationService();
            MessageTranslator ot = new MessageTranslator();
            ArrayList<IScyMessage> messages = new ArrayList<IScyMessage>();
            collaborationService.joinSession(getSessionId(), USER_NAME, ApplicationController.TOOL_NAME);
            messages = collaborationService.synchronizeClientState(USER_NAME, ApplicationController.TOOL_NAME, getSessionId(), true);
            log.info("got ###: " + messages.size() + " messages");
            synchronizeDiagramElements(messages, ot);
        } catch (CollaborationServiceException e) {
            log.error("CollaborationService failure: " + e);
            e.printStackTrace();
        }


    }

    private void initializeCollaborationService() throws CollaborationServiceException {
        collaborationService = CollaborationServiceFactory.getCollaborationService(CollaborationServiceFactory.LOCAL_STYLE);
        collaborationSession = collaborationService.createSession(ApplicationController.TOOL_NAME, USER_NAME);
        collaborationService.addCollaborationListener(this);
    }

    private void synchronizeDiagramElements(ArrayList<IScyMessage> messages, MessageTranslator ot) {
        if (messages == null) {
            log.info("Messages are null");
            return;
        }
        ScyMessage scyMessage;
        for (int i = 0; i < messages.size(); i++) {
            scyMessage = (ScyMessage) messages.get(i);
            Object newNOde = ot.getObject(scyMessage);
            log.info("NODE: " + newNOde);
            processNode(newNOde);
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
        IScyMessage sm = e.getScyMessage();
        if (sm != null) {
            log.debug("UPDATING FROM SERVER: " + sm.getObjectType());
            MessageTranslator mt = new MessageTranslator();
            log.debug("CALL: Before add class");
            Object object = mt.getObject(sm);
            log.info("OBJECT IS: " + object);
            if (sm.getObjectType().contains("AddClass")) {
                processNode(object);
            } else if (sm.getObjectType().contains("UmlLink")) {
                processNode(object);
            } else if (sm.getObjectType().contains("MoveClass")) {
                log.debug("ADDING A MOVE CLASS!");
                processNode(object);
                log.debug("MOVE CLASS WAS ADDED!");
            } else if (sm.getObjectType().contains("UmlClass")) {
                log.debug("A concept has been updated - updating diagram");
                processNode(object);
            }
        }
    }

    public void sendMessage(String message) {
        System.out.println("SCYConnectionHandler.sendMessage()");
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
