package eu.scy.colemo.client.sqlspacesimpl;

import eu.scy.colemo.client.ConnectionHandler;
import eu.scy.colemo.client.ApplicationController;
import eu.scy.colemo.contributions.Contribution;
import eu.scy.colemo.contributions.AddClass;
import eu.scy.colemo.contributions.MoveClass;
import eu.scy.colemo.contributions.BaseConceptMapNode;
import eu.scy.colemo.server.uml.UmlClass;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Callback;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jan.2009
 * Time: 23:02:33
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionHandlerSqlSpaces implements ConnectionHandler, Callback {

    private TupleSpace tupleSpace = null;

    public static String MESSAGE_TYPE = "MESSAGE_TYPE";

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


    public void sendObject(Object object) {
        try {
            System.out.println("Sending object:" + object);
            eu.scy.colemo.client.sqlspacesimpl.ObjectTranslator translator = new eu.scy.colemo.client.sqlspacesimpl.ObjectTranslator();
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


    public void initialize() throws Exception {
        System.out.println("initializing");
        tupleSpace = new TupleSpace("localhost", 2525, "AIRFORCE_ONE");
        //tupleSpace = new TupleSpace("129.240.212.15", 2525, "AIRFORCE_ONE");
        Tuple tmp = new Tuple(String.class, String.class);
        Tuple colemoBus = new Tuple(String.class, String.class, String.class, String.class, String.class, String.class);
        Callback cb = this;
        int seqNo = tupleSpace.eventRegister(Command.WRITE, tmp, cb, false);
        int colemoSeq = tupleSpace.eventRegister(Command.WRITE, colemoBus, cb, false);
        //int seqNo = tupleSpace.eventRegister(Command.WRITE, tmp, cb, false);

    }

    public void call(Command command, int i, Tuple tuple, Tuple tuple1) {
        System.out.println("i: " + i);
        if (tuple != null && tuple.getFields().length == 6) {
            eu.scy.colemo.client.sqlspacesimpl.ObjectTranslator ot = new eu.scy.colemo.client.sqlspacesimpl.ObjectTranslator();

            UmlClass umlClass = null;

            System.out.println("CALL: Before add class");
            BaseConceptMapNode node = (BaseConceptMapNode) ot.getObject(tuple);

            if (node instanceof AddClass) {
                System.out.println("CALL: After add class");
                System.out.println("CALL: name: " + ((AddClass) node).getName() + " ID : " + node.getId());
                System.out.println(((AddClass) node).getType());
                System.out.println(((AddClass) node).getAuthor());
                umlClass = new UmlClass(((AddClass) node).getName(), ((AddClass) node).getType(), ((AddClass) node).getAuthor());
                umlClass.setId(node.getId());
                ApplicationController.getDefaultInstance().getColemoPanel().getGraphicsDiagram().getUmlDiagram().addDiagramData(umlClass);
                ApplicationController.getDefaultInstance().getColemoPanel().getGraphicsDiagram().addClass(umlClass);
                //ApplicationController.getDefaultInstance().getColemoPanel().getGraphicsDiagram().updateUmlDiagram(ApplicationController.getDefaultInstance().getColemoPanel().getGraphicsDiagram().getUmlDiagram());
            } else if (node instanceof MoveClass) {
                System.out.println("MOVE CLASS: " + ((MoveClass) node).getUmlClass().getName());
                MoveClass moveClass = (MoveClass) node;
                ApplicationController.getDefaultInstance().getColemoPanel().getGraphicsDiagram().updateClass(moveClass.getUmlClass());
            }
        }
    }
}
