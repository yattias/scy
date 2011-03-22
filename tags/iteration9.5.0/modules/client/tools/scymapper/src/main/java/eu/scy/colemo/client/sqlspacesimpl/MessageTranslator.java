package eu.scy.colemo.client.sqlspacesimpl;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.Field;
import eu.scy.colemo.contributions.AddClass;
import eu.scy.colemo.contributions.AddLink;
import eu.scy.colemo.contributions.MoveClass;
import eu.scy.colemo.client.ApplicationController;
import eu.scy.colemo.client.ConceptNode;
import eu.scy.colemo.client.GraphicsDiagram;
import eu.scy.colemo.client.scyconnectionhandler.SCYConnectionHandler;
import eu.scy.colemo.server.uml.UmlLink;
import eu.scy.colemo.server.uml.UmlClass;
import eu.scy.communications.message.impl.ScyMessage;
import eu.scy.communications.message.IMessageTranslator;
import eu.scy.communications.message.IScyMessage;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.jan.2009
 * Time: 10:32:59
 * To change this template use File | Settings | File Templates.
 */
public class MessageTranslator implements IMessageTranslator {

    private static final long DEFAULT_EXPIRATION_TIME = 30 * 1000;
    private static final Logger log = Logger.getLogger(MessageTranslator.class.getName());

    private String sessionId = null;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }/* (non-Javadoc)
     * @see eu.scy.colemo.client.sqlspacesimpl.IMessageTranslator#translate(java.lang.Object)
     */

    public Tuple translate(Object object) {
        return null;

    }


    public Object getObject(Tuple tuple) {
        Field[] fields = tuple.getFields();

        if (fields.length == 6) {
            Field userNameField = fields[0];
            Field toolName = fields[1];
            Field objectIdField = fields[2];
            Field typeField = fields[3];
            Field nameField = fields[4];
            Field descriptionField = fields[5];

            try {
                Class c = Class.forName((String) typeField.getValue());
                if (c.getName().indexOf("AddClass") > 0) {
                    System.out.println("Creating new add class");
                    AddClass ac = new AddClass((String) nameField.getValue(), (String) typeField.getValue(), "HEI", null, null);
                    ac.setId((String) objectIdField.getValue());
                    return ac;
                } else if (c.getName().indexOf("MoveClass") > 0) {
                    System.out.println("Creating new MoveClass!");
                    String pos = (String) descriptionField.getValue();
                    String[] posArray = pos.split(",");
                    Integer xPos = new Integer(posArray[0]);
                    Integer yPos = new Integer(posArray[1]);

                    GraphicsDiagram diagram = ApplicationController.getDefaultInstance().getGraphicsDiagram();
                    ConceptNode node = diagram.getNodeByClassId((String) objectIdField.getValue());

                    System.out.println("xpos = " + xPos);
                    System.out.println("Y = " + yPos);
                    node.setLocation(xPos, yPos);

                    UmlClass cls = node.getModel();
                    cls.setX(xPos);
                    cls.setY(yPos);

                    return new MoveClass(cls, null, null);
                }
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else if (fields.length == 5) {
            Field userNameField = fields[0];
            Field toolName = fields[1];
            Field objectIdField = fields[2];
            Field fromField = fields[3];
            Field toField = fields[4];
            UmlLink link = new UmlLink((String) fromField.getValue(), (String) toField.getValue(), "Henrik");
            link.setId((String) objectIdField.getValue());
            return link;
        }
        System.out.println("** *** ** Object Translator returning null");
        return null;
//
    }


    /* (non-Javadoc)
     * @see eu.scy.colemo.client.sqlspacesimpl.IMessageTranslator#getObject(eu.scy.communications.message.IScyMessage)
     */
    public Object getObject(IScyMessage scyMessage) {
        log.info("Creating object from SCYMessage: " + scyMessage.getObjectType());
        if (scyMessage.getObjectType().indexOf("AddClass") > 0) {
            log.debug("Creating new add class");
            AddClass ac = new AddClass(scyMessage.getName(), scyMessage.getObjectType(), "HEI", null, null);
            ac.setId(scyMessage.getId());
            return ac;
        } else if (scyMessage.getObjectType().indexOf("MoveClass") > 0) {
            log.debug("Creating new MoveClass!");
            String pos = scyMessage.getDescription();
            String[] posArray = pos.split(",");
            Integer xPos = new Integer(posArray[0]);
            Integer yPos = new Integer(posArray[1]);

            GraphicsDiagram diagram = ApplicationController.getDefaultInstance().getGraphicsDiagram();
            ConceptNode node = diagram.getNodeByClassId(scyMessage.getId());
            if (node != null) {
                log.debug("xpos = " + xPos);
                log.debug("Y = " + yPos);
                node.setLocation(xPos, yPos);

                UmlClass cls = node.getModel();
                cls.setX(xPos);
                cls.setY(yPos);

                return new MoveClass(cls, null, null);
            } else {
                log.info("NODE WAS NULL - need to create one!");
                UmlClass concept = new UmlClass(scyMessage.getName(), "c", "Henrik");
                concept.setX(xPos);
                concept.setY(yPos);
                concept.setId(scyMessage.getId());

                //ConceptNode newNode = new ConceptNode(concept);
                diagram.addClass(concept);



            }

        } else if (scyMessage.getObjectType().indexOf("UmlLink") > 0) {
            UmlLink link = new UmlLink(scyMessage.getFrom(), scyMessage.getTo(), "Henrik");
            link.setId(scyMessage.getId());
            link.setName(scyMessage.getName());
            log.info("SET NAME ON LINK: *************************** " + link.getName());
            return link;
        } else if (scyMessage.getObjectType().indexOf("UmlClass") > 0) {
            log.info("******************************SCY MESSAGES SAYS NAME: " + scyMessage.getName());
            UmlClass concept = new UmlClass(scyMessage.getName(), "c", "Henrik");
            concept.setId(scyMessage.getId());
            return concept;
        }

        //log.info("Could not translate scyMessage: " + scyMessage);

        return null;
    }


    public Tuple getTuple(AddClass addClass) {
        Field userNameField = new Field(SCYConnectionHandler.USER_NAME);
        Field toolName = new Field(ApplicationController.TOOL_NAME);
        Field objectIdField = new Field(String.valueOf(addClass.hashCode()));
        Field objectType = new Field(addClass.getClass().getName());
        Field objectName = new Field(addClass.getName());
        Field objectDescription = new Field("Some description");

        return new Tuple(userNameField, toolName, objectIdField, objectType, objectName, objectDescription);
    }

    //
    public Tuple getTuple(MoveClass moveClass) {
        Field userNameField = new Field(SCYConnectionHandler.USER_NAME);
        Field toolName = new Field(ApplicationController.TOOL_NAME);
        Field objectIdField = new Field(moveClass.getUmlClass().getId());
        Field objectType = new Field(moveClass.getClass().getName());
        Field objectName = new Field(moveClass.getUmlClass().getName());
        Field objectDescription = new Field("" + moveClass.getUmlClass().getX() + "," + moveClass.getUmlClass().getY());

        return new Tuple(userNameField, toolName, objectIdField, objectType, objectName, objectDescription);
    }

    //
    public Tuple getTuple(UmlLink addLink) {

        Field userNameField = new Field(SCYConnectionHandler.USER_NAME);
        Field toolName = new Field(ApplicationController.TOOL_NAME);
        Field objectIdField = new Field(addLink.getId());
        Field fromField = new Field(addLink.getFrom());
        Field toField = new Field(addLink.getTo());
        System.out.println("toField = " + toField);
        System.out.println("fromField = " + fromField);
        return new Tuple(userNameField, toolName, objectIdField, fromField, toField);
    }


    /* (non-Javadoc)
     * @see eu.scy.colemo.client.sqlspacesimpl.IMessageTranslator#getScyMessage(eu.scy.colemo.contributions.AddClass)
     */
    public IScyMessage getScyMessage(Object object) {
        log.debug("Creating scy message of object: " + object);
        IScyMessage message = null;
        if (object instanceof AddClass) {
            AddClass addClass = (AddClass) object;
            message = ScyMessage.createScyMessage(SCYConnectionHandler.USER_NAME, ApplicationController.TOOL_NAME, String.valueOf(addClass.hashCode()), addClass.getClass().getName(), addClass.getName(), "Some description", null, null, getSessionId(), 0, getSessionId());
        } else if (object instanceof MoveClass) {
            MoveClass moveClass = (MoveClass) object;
            message = ScyMessage.createScyMessage(SCYConnectionHandler.USER_NAME, ApplicationController.TOOL_NAME, moveClass.getUmlClass().getId(), moveClass.getClass().getName(), moveClass.getUmlClass().getName(), "" + moveClass.getUmlClass().getX() + "," + moveClass.getUmlClass().getY(), null, null, getSessionId(), 0, getSessionId());
        } else if (object instanceof AddLink) {
            AddLink addLink = (AddLink) object;
            message = ScyMessage.createScyMessage(SCYConnectionHandler.USER_NAME, ApplicationController.TOOL_NAME, addLink.getId(), addLink.getClass().getName(), addLink.getClass().getName(), "Some description", addLink.getTo(), addLink.getFrom(), getSessionId(), 0, getSessionId());
        } else if (object instanceof UmlLink) {
            UmlLink umlLink = (UmlLink) object;
            log.debug("************* ******************** *************** Creating a message for a link:" + umlLink.getName());
            message = ScyMessage.createScyMessage(SCYConnectionHandler.USER_NAME, ApplicationController.TOOL_NAME, umlLink.getId(), umlLink.getClass().getName(), umlLink.getName(), "Some description", umlLink.getTo(), umlLink.getFrom(), getSessionId(), 0, getSessionId());
        } else if (object instanceof UmlClass) {
            UmlClass umlClass = (UmlClass) object;
            message = ScyMessage.createScyMessage(SCYConnectionHandler.USER_NAME, ApplicationController.TOOL_NAME, umlClass.getId(), umlClass.getClass().getName(), umlClass.getName(), "Some description", null, null, getSessionId(), 0, getSessionId());
        } else {
            log.warn("Did not manage to create scy message from object: " + object);
        }
        return message;
    }


}