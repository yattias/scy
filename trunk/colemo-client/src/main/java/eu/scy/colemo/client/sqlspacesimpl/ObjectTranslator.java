package eu.scy.colemo.client.sqlspacesimpl;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.Field;
import eu.scy.colemo.contributions.AddClass;
import eu.scy.colemo.contributions.Contribution;
import eu.scy.colemo.contributions.MoveClass;
import eu.scy.colemo.client.ApplicationController;
import eu.scy.colemo.client.GraphicsClass;
import eu.scy.colemo.server.uml.UmlClass;

import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.jan.2009
 * Time: 10:32:59
 * To change this template use File | Settings | File Templates.
 */
public class ObjectTranslator {

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

    public Object getObject(Tuple tuple) {
        Field [] fields = tuple.getFields();
        
        if(fields.length == 6) {
            Field userNameField = fields[0];
            Field toolName = fields[1];
            Field objectIdField = fields[2];
            Field typeField = fields[3];
            Field nameField = fields[4];
            Field descriptionField = fields[5];

            try {
                Class c = Class.forName((String) typeField.getValue() );
                if(c.getName().indexOf("AddClass")> 0) {
                    System.out.println("Creating new add class");
                    AddClass ac = new AddClass((String) nameField.getValue(), (String)typeField.getValue(), "HEI", null, null);
                    ac.setId((String) objectIdField.getValue());
                    return ac;
               } else if(c.getName().indexOf("MoveClass") > 0) {
                    System.out.println("Creating new MoveClass!");
                    GraphicsClass graphicsClass = ApplicationController.getDefaultInstance().getGraphicsDiagram().getConceptMapNode((String) objectIdField.getValue());
                    String pos = (String) descriptionField.getValue();
                    String [] posArray = pos.split(",");
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
        }
        System.out.println("returning null");
        return null;

    }

    public Tuple getTuple(MoveClass moveClass) {
        Field userNameField = new Field("henrik@enovate.no");
        Field toolName = new Field("Colemo");
        Field objectIdField = new Field(moveClass.getUmlClass().getId());
        Field objectType = new Field(moveClass.getClass().getName());
        Field objectName = new Field(moveClass.getUmlClass().getName());
        Field objectDescription = new Field("" + moveClass.getUmlClass().getX() + "," + moveClass.getUmlClass().getY());

        return new Tuple(userNameField, toolName, objectIdField, objectType, objectName , objectDescription);
    }
}
