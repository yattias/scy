package eu.scy.colemo.client.sqlspacesimpl;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import eu.scy.colemo.client.ApplicationController;
import eu.scy.colemo.client.ConnectionHandler;
import eu.scy.colemo.contributions.AddClass;
import eu.scy.colemo.contributions.MoveClass;
import eu.scy.colemo.contributions.BaseConceptMapNode;
import eu.scy.colemo.server.uml.UmlLink;
import eu.scy.colemo.server.uml.UmlClass;
import eu.scy.collaborationservice.CollaborationServiceException;
import eu.scy.collaborationservice.CollaborationServiceFactory;
import eu.scy.collaborationservice.ICollaborationService;
import eu.scy.collaborationservice.event.ICollaborationServiceEvent;
import eu.scy.collaborationservice.event.ICollaborationServiceListener;
import eu.scy.communications.message.impl.ScyMessage;
import eu.scy.communications.message.IScyMessage;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jan.2009
 * Time: 23:02:33
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionHandlerSqlSpaces implements ConnectionHandler, ICollaborationServiceListener {

    //private TupleSpace tupleSpace = null;

    public static String MESSAGE_TYPE = "MESSAGE_TYPE";

    //private Tuple conceptTemplate = new Tuple(String.class, String.class, String.class, String.class, String.class, String.class);
    //private Tuple linkTemplate = new Tuple(String.class, String.class, String.class, String.class, String.class);
    private static final Logger logger = Logger.getLogger(ConnectionHandlerSqlSpaces.class.getName());
    private ICollaborationService cs;
    //FIXME: this needs to come from the client, generated
    public static final String SESSIONID = "1";

    public void sendMessage(String message) {
//        try {
//            System.out.println("Sending message: " + message);
//            if (tupleSpace != null) {
//                Field typeField = new Field(MESSAGE_TYPE);
//                Field messageField = new Field(message);
//
//                Tuple t = new Tuple(typeField, messageField);
//                try {
//                    tupleSpace.write(t);
//                } catch (TupleSpaceException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            Tuple templateTuple = new Tuple(String.class, String.class, String.class, String.class, String.class);
//            Tuple returnTuple = tupleSpace.read(templateTuple);
//            if (returnTuple != null) {
//                System.out.println("return tuple" + returnTuple + returnTuple.getField(0) + " " + returnTuple.getField(1));
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
    }


    public void sendObject(Object object) {
        System.out.println("Sending object:" + object);

        ObjectTranslator translator = new ObjectTranslator();
        IScyMessage sendMe = null;

        if (object instanceof AddClass) {
            AddClass addClass = (AddClass) object;
            System.out.println("NAME: " + addClass.getName());
            sendMe = translator.getScyMessage(addClass);
        } else if (object instanceof MoveClass) {
            MoveClass mc = (MoveClass) object;
            mc.setId(mc.getUmlClass().getId());
            System.out.println("moving class with id:  " + mc.getId() + " to position: " + mc.getUmlClass().getX() + ", " + mc.getUmlClass().getY());
            sendMe = translator.getScyMessage(mc);
        } else if (object instanceof UmlLink) {
            UmlLink addLink = (UmlLink) object;
            System.out.println("Adding link");
            sendMe = translator.getScyMessage(addLink);
        }

        if (sendMe != null) {
            logger.debug("Sending ScyMessage: \n" + sendMe.toString());
            try {
                //cs.create(sendMe);
                throw new CollaborationServiceException("THOMAS AND TONY FIX THIS EXCEPTION");
            } catch (CollaborationServiceException e) {
                logger.error("Bummer. Create in CollaborationService failed somehow: \n" + e);
                e.printStackTrace();
            }
        }
    }

//    public void sendObject2(Object object) {
//        try {
//            System.out.println("Sending object:" + object);
//            ObjectTranslator translator = new ObjectTranslator();
//            Tuple sendMe = null;
//            if (object instanceof AddClass) {
//                AddClass addClass = (AddClass) object;
//                System.out.println("NAME: " + addClass.getName());
//                sendMe = translator.getTuple(addClass);
//            } else if (object instanceof MoveClass) {
//                MoveClass mc = (MoveClass) object;
//                mc.setId(mc.getUmlClass().getId());
//                System.out.println("moving class with id:  " + mc.getId() + " to position: " + mc.getUmlClass().getX() + ", " + mc.getUmlClass().getY());
//                sendMe = translator.getTuple(mc);
//            } else if (object instanceof UmlLink) {
//                UmlLink addLink = (UmlLink) object;
//                System.out.println("Adding link");
//                sendMe = translator.getTuple(addLink);
//            }
//
//            try {
//                if (sendMe != null) {
//                    tupleSpace.write(sendMe);
//                }
//
//            } catch (TupleSpaceException e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//    }


    public void initialize() throws Exception {

        logger.debug("initializing");
//        tupleSpace = new TupleSpace("scy.collide.info", 2525, "AIRFORCE_ONE");
//        //tupleSpace = new TupleSpace("localhost", 2525, "AIRFORCE_ONE");
//
//        //tupleSpace = new TupleSpace("129.240.212.15", 2525, "AIRFORCE_ONE");
//        Tuple tmp = new Tuple(String.class, String.class);
//        //Tuple colemoBus = new Tuple(String.class, String.class, String.class, String.class, String.class, String.class);
//        Callback cb = this;
//        int seqNo = tupleSpace.eventRegister(Command.WRITE, tmp, cb, false);
//        int conceptSeq= tupleSpace.eventRegister(Command.WRITE, conceptTemplate, cb, false);
//        int linkSeq = tupleSpace.eventRegister(Command.WRITE, linkTemplate, cb, false);
//        //int seqNo = tupleSpace.eventRegister(Command.WRITE, tmp, cb, false);

//        Tuple [] allConcepts = tupleSpace.readAll(conceptTemplate);
//        Tuple [] allLinks = tupleSpace.readAll(linkTemplate);
        ObjectTranslator ot = new ObjectTranslator();

        ArrayList<ScyMessage> messages = new ArrayList<ScyMessage>();
        try {
            cs = CollaborationServiceFactory.getCollaborationService(CollaborationServiceFactory.LOCAL_STYLE);
            cs.addCollaborationListener(this);
            //messages = cs.synchronizeClientState("Colemo", SESSIONID);
        } catch (CollaborationServiceException e) {
            logger.error("CollaborationService failure: " + e);
            e.printStackTrace();
        }

//        synchronizeDiagramElements(allConcepts, ot);
//        synchronizeDiagramElements(allLinks, ot);
        synchronizeDiagramElements(messages, ot);
    }

//    private void synchronizeDiagramElements(Tuple[] allTuples, ObjectTranslator ot) {
//        for (int i = 0; i < allTuples.length; i++) {
//            Tuple allTuple = allTuples[i];
//            Object newNOde = ot.getObject(allTuple);
//            addNewNode(newNOde);
//        }
//        //TODO: Find a better place for this
//        ApplicationController.getDefaultInstance().getColemoPanel().setBounds(0,0, 800, 700);
//    }

    private void synchronizeDiagramElements(ArrayList<ScyMessage> messages, ObjectTranslator ot) {
        ScyMessage scyMessage;
        for (int i = 0; i < messages.size(); i++) {
            scyMessage = messages.get(i);
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

//    public void call(Command command, int i, Tuple tuple, Tuple tuple1) {
//        System.out.println("i: " + i);
//        if (tuple != null && tuple.getFields().length == conceptTemplate.getFields().length) {
//            System.out.println("UPDATING CONCEPT FROM SERVER!");
//            ObjectTranslator ot = new ObjectTranslator();
//
//            System.out.println("CALL: Before add class");
//            BaseConceptMapNode node = (BaseConceptMapNode) ot.getObject(tuple);
//
//            addNewNode(node);
//        } else if(tuple != null && tuple.getFields().length == linkTemplate.getFields().length) {
//            System.out.println("UPDATING LINK FROM SERVER!");
//            ObjectTranslator ot = new ObjectTranslator();
//            UmlLink link = (UmlLink) ot.getObject(tuple);
//            addNewNode(link);
//        }
//    }

    private void addNewNode(Object node) {
        UmlClass umlClass;
        if (node instanceof AddClass) {
            System.out.println("CALL: After add class");
            umlClass = new UmlClass(((AddClass) node).getName(), ((AddClass) node).getType(), ((AddClass) node).getAuthor());
            umlClass.setId(((AddClass) node).getId());
            ApplicationController.getDefaultInstance().getColemoPanel().getGraphicsDiagram().getUmlDiagram().addDiagramData(umlClass);
            ApplicationController.getDefaultInstance().getColemoPanel().getGraphicsDiagram().addClass(umlClass);
            //ApplicationController.getDefaultInstance().getColemoPanel().getGraphicsDiagram().updateUmlDiagram(ApplicationController.getDefaultInstance().getColemoPanel().getGraphicsDiagram().getUmlDiagram());
        } else if (node instanceof MoveClass) {
            System.out.println("MOVE CLASS: " + ((MoveClass) node).getUmlClass().getName());
            MoveClass moveClass = (MoveClass) node;
            ApplicationController.getDefaultInstance().getColemoPanel().getGraphicsDiagram().updateClass(moveClass.getUmlClass());
        } else if (node instanceof UmlLink) {
            UmlLink link = (UmlLink) node;
            ApplicationController.getDefaultInstance().getColemoPanel().getGraphicsDiagram().addLink(link);
        }
    }


    public void handleCollaborationServiceEvent(ICollaborationServiceEvent e) {
        logger.debug("got CollaborationServiceEvent");
        IScyMessage sm = e.getScyMessage();
        if (sm != null) {
            logger.debug("UPDATING FROM SERVER!");
            ObjectTranslator ot = new ObjectTranslator();
            logger.debug("CALL: Before add class");
            Object object = ot.getObject(sm);
            if (object instanceof BaseConceptMapNode) {
                addNewNode((BaseConceptMapNode) object);
            }
            if (object instanceof UmlLink) {
                addNewNode((UmlLink) object);
            }
        }
    }
}
