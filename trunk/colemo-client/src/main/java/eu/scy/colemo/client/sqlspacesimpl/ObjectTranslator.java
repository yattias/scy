package eu.scy.colemo.client.sqlspacesimpl;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.Field;
import eu.scy.colemo.contributions.AddClass;
import eu.scy.colemo.contributions.MoveClass;
import eu.scy.colemo.contributions.AddLink;
import eu.scy.colemo.client.ApplicationController;
import eu.scy.colemo.client.ConceptNode;
import eu.scy.colemo.server.uml.UmlLink;
import eu.scy.communications.message.ScyMessage;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.jan.2009
 * Time: 10:32:59
 * To change this template use File | Settings | File Templates.
 */
public class ObjectTranslator {

    private static final long DEFAULT_EXPIRATION_TIME = 30*1000;
    
    public Tuple translate(Object object) {
        return null;

    }

    public Tuple getTuple(AddClass addClass) {
        Field userNameField = new Field("henrik@enovate.no");
        Field toolName = new Field("Colemo");
        Field objectIdField = new Field(String.valueOf(addClass.hashCode()));
        Field objectType = new Field(addClass.getClass().getName());
        Field objectName = new Field(addClass.getName());
        Field objectDescription = new Field("Some description");

        return new Tuple(userNameField, toolName, objectIdField, objectType, objectName, objectDescription);
    }


    // TODO: refactor this one to handle a ScyMessage, not a Tuple
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
                    ConceptNode graphicsClass = ApplicationController.getDefaultInstance().getGraphicsDiagram().getConceptMapNode((String) objectIdField.getValue());
                    String pos = (String) descriptionField.getValue();
                    String[] posArray = pos.split(",");
                    String xPos = posArray[0];
                    String yPos = posArray[1];
                    graphicsClass.getUmlClass().setX(new Integer(xPos));
                    graphicsClass.getUmlClass().setY(new Integer(yPos));
                    System.out.println("tried to fetch umlclass with id: " + objectIdField.getValue() + ",  using umlclass " + graphicsClass);
                    return new MoveClass(graphicsClass.getUmlClass(), null, null);
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
            UmlLink link = new UmlLink((String)fromField.getValue(), (String) toField.getValue(), "Henrik");
            link.setId((String) objectIdField.getValue());
            return link;
        }
        System.out.println("** *** ** Object Translator returning null");
        return null;

    }

    public Tuple getTuple(MoveClass moveClass) {
        Field userNameField = new Field("henrik@enovate.no");
        Field toolName = new Field("Colemo");
        Field objectIdField = new Field(moveClass.getUmlClass().getId());
        Field objectType = new Field(moveClass.getClass().getName());
        Field objectName = new Field(moveClass.getUmlClass().getName());
        Field objectDescription = new Field("" + moveClass.getUmlClass().getX() + "," + moveClass.getUmlClass().getY());

        return new Tuple(userNameField, toolName, objectIdField, objectType, objectName, objectDescription);
    }

    public Tuple getTuple(UmlLink addLink) {
        Field userNameField = new Field("henrik@enovate.no");
        Field toolName = new Field("Colemo");
        Field objectIdField = new Field(addLink.getId());
        Field fromField = new Field(addLink.getFrom());
        Field toField = new Field(addLink.getTo());
        return new Tuple(userNameField, toolName, objectIdField, fromField, toField);
    }
    
    
    public ScyMessage getScyMessage(AddClass addClass) {
        return ScyMessage.createScyMessage("henrik@enovate.no", "Colemo", String.valueOf(addClass.hashCode()), addClass.getClass().getName(), addClass.getName(), "Some description", null, null, null, DEFAULT_EXPIRATION_TIME);
    }
    
    
    public ScyMessage getScyMessage(MoveClass moveClass) {
        return ScyMessage.createScyMessage("henrik@enovate.no", "Colemo", moveClass.getUmlClass().getId(), moveClass.getClass().getName(), moveClass.getUmlClass().getName(), "" + moveClass.getUmlClass().getX() + "," + moveClass.getUmlClass().getY(), null, null, null, DEFAULT_EXPIRATION_TIME);
    }
    
    public ScyMessage getScyMessage(UmlLink addLink) {
        return ScyMessage.createScyMessage("henrik@enovate.no", "Colemo", addLink.getId(), null, null, "Some description", addLink.getTo(), addLink.getFrom(), null, DEFAULT_EXPIRATION_TIME);
    }
}
