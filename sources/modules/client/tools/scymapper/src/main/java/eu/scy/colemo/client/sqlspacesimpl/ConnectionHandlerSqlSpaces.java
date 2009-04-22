package eu.scy.colemo.client.sqlspacesimpl;

import javax.swing.SwingUtilities;

import eu.scy.colemo.client.ConnectionHandler;
import eu.scy.colemo.client.ApplicationController;
import eu.scy.colemo.contributions.AddClass;
import eu.scy.colemo.contributions.MoveClass;
import eu.scy.colemo.contributions.BaseConceptMapNode;
import eu.scy.colemo.server.uml.UmlClass;
import eu.scy.colemo.server.uml.UmlLink;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Callback;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jan.2009
 * Time: 23:02:33
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionHandlerSqlSpaces implements ConnectionHandler, Callback {

    private TupleSpace tupleSpace = null;

    protected static String MESSAGE_TYPE = "MESSAGE_TYPE";

    private Tuple conceptTemplate = new Tuple(String.class, String.class, String.class, String.class, String.class, String.class);
    private Tuple linkTemplate = new Tuple(String.class, String.class, String.class, String.class, String.class);

    private static final Logger log = Logger.getLogger(ConnectionHandlerSqlSpaces.class.getName());

    public void sendMessage(String message) {
        try {
            System.out.println("Sending message: " + message);
            if (tupleSpace != null) {
                Field typeField = new Field(MESSAGE_TYPE);
                Field messageField = new Field(message);

                Tuple t = new Tuple(typeField, messageField);
                try {
                    tupleSpace.write(t);
                } catch (TupleSpaceException e) {
                    e.printStackTrace();
                }
            }


            Tuple templateTuple = new Tuple(String.class, String.class, String.class, String.class, String.class);
            Tuple returnTuple = tupleSpace.read(templateTuple);
            if (returnTuple != null) {
                System.out.println("return tuple" + returnTuple + returnTuple.getField(0) + " " + returnTuple.getField(1));
            }

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public void updateObject(Object object) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void sendObject(Object object) {
        try {
            log.info("===================> Sending object:" + object);
            MessageTranslator translator = new MessageTranslator();
            Tuple sendMe = null;
            if (object instanceof AddClass) {
                AddClass addClass = (AddClass) object;
                System.out.println("NAME: " + addClass.getName());
                sendMe = translator.getTuple(addClass);
            } else if (object instanceof MoveClass) {
                MoveClass mc = (MoveClass) object;
                mc.setId(mc.getUmlClass().getId());
                System.out.println("moving class with id:  " + mc.getId() + " to position: " + mc.getUmlClass().getX() + ", " + mc.getUmlClass().getY());
                sendMe = translator.getTuple(mc);
            } else if (object instanceof UmlLink) {
                UmlLink addLink = (UmlLink) object;
                System.out.println("Adding link");
                sendMe = translator.getTuple(addLink);
            }

            try {
                if (sendMe != null) {
                    tupleSpace.write(sendMe);
                }

            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void deleteObject(Object object) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void initialize() throws Exception {

        System.out.println("initializing");
        tupleSpace = new TupleSpace("scy.collide.info", 2525, "AIRFORCE_ONE");
        //tupleSpace = new TupleSpace("localhost", 2525, "AIRFORCE_ONE");

        //tupleSpace = new TupleSpace("129.240.212.15", 2525, "AIRFORCE_ONE");
        Tuple tmp = new Tuple(String.class, String.class);
        //Tuple colemoBus = new Tuple(String.class, String.class, String.class, String.class, String.class, String.class);
        Callback cb = this;
        int seqNo = tupleSpace.eventRegister(Command.WRITE, tmp, cb, false);
        int conceptSeq= tupleSpace.eventRegister(Command.WRITE, conceptTemplate, cb, false);
        int linkSeq = tupleSpace.eventRegister(Command.WRITE, linkTemplate, cb, false);
        //int seqNo = tupleSpace.eventRegister(Command.WRITE, tmp, cb, false);

        Tuple [] allConcepts = tupleSpace.readAll(conceptTemplate);
        Tuple [] allLinks = tupleSpace.readAll(linkTemplate);
        MessageTranslator ot = new MessageTranslator();
        synchronizeDiagramElements(allConcepts, ot);
        synchronizeDiagramElements(allLinks, ot);
    }

    private void synchronizeDiagramElements(Tuple[] allTuples, MessageTranslator ot) {
        for (int i = 0; i < allTuples.length; i++) {
            Tuple allTuple = allTuples[i];
            log.info("Synchronizing: -------------------------------------------------------------------------------------------");
            Object newNOde = ot.getObject(allTuple);
            log.info("------------------------------------------------------------------------------------------- Synchronizing: " + newNOde);
            processNodeOnUIThread(newNOde);
        }
    }

    public void cleanUp() {
        try {
            tupleSpace.takeAll(conceptTemplate);
        } catch (TupleSpaceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void joinSession(String sessionId) {
        System.out.println("NOT IMPLEMENTED!!");
    }

    public void call(Command command, int i, Tuple tuple, Tuple tuple1) {
        if (tuple != null && tuple.getFields().length == conceptTemplate.getFields().length) {
            log.debug("UPDATING CONCEPT FROM SERVER!");
            MessageTranslator ot = new MessageTranslator();

            UmlClass umlClass = null;

            log.debug("CALL: Before add class");
            BaseConceptMapNode node = (BaseConceptMapNode) ot.getObject(tuple);

            processNodeOnUIThread(node);
        } else if(tuple != null && tuple.getFields().length == linkTemplate.getFields().length) {
            System.out.println("UPDATING LINK FROM SERVER!");
            MessageTranslator ot = new MessageTranslator();
            UmlLink link = (UmlLink) ot.getObject(tuple);
            processNodeOnUIThread(link);
        }
    }
    
    protected void processNodeOnUIThread(final Object node) {
    	SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				processNode(node);
			}
        });
    }

    protected void processNode(Object node) {
        if(node == null) {
            log.error("NODE IS NULL!!");
            return;
        }
        try {
            UmlClass umlClass;
            if (node instanceof AddClass) {
                umlClass = new UmlClass(((AddClass) node).getName(), ((AddClass) node).getType(), ((AddClass) node).getAuthor());
                umlClass.setId(((AddClass)node).getId());
                ApplicationController.getDefaultInstance().getColemoPanel().getGraphicsDiagram().getUmlDiagram().addDiagramData(umlClass);
                ApplicationController.getDefaultInstance().getColemoPanel().getGraphicsDiagram().addClass(umlClass);
                log.info("Added UML class: " + umlClass.getName());
            } else if (node instanceof MoveClass) {
                log.debug("MOVE CLASS: " + ((MoveClass) node).getUmlClass().getName());
                MoveClass moveClass = (MoveClass) node;
                ApplicationController.getDefaultInstance().getColemoPanel().getGraphicsDiagram().updateClass(moveClass.getUmlClass());
            } else if(node instanceof UmlLink) {
                UmlLink link = (UmlLink) node;
                log.info("*** **** *** UPDATING LINK!!! RETURNED FROM SERVER! - LINK NAME: " + link.getName());
                ApplicationController.getDefaultInstance().getColemoPanel().getGraphicsDiagram().addLink(link);

            } else if(node instanceof UmlClass) {
                UmlClass concept = (UmlClass) node;
                ApplicationController.getDefaultInstance().getColemoPanel().getGraphicsDiagram().updateConcept(concept);

            } else {
                log.warn("DO NOT KNOW HOW TO HANDLE OBJECTS OF TYPE " + node.getClass().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            log.info("EVERYTHING IS DONE!");
        }
    }
}
