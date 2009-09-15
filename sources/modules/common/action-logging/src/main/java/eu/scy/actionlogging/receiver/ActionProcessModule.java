package eu.scy.actionlogging.receiver;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import info.collide.sqlspaces.commons.util.XMLUtils;

import java.io.IOException;
import java.util.List;

import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionProcessModule;


public class ActionProcessModule implements IActionProcessModule {
    
    private TupleSpace ts;
    
    public ActionProcessModule(String host, int port) {
        try {
            ts = new TupleSpace(new User("Action Logging Module"), host, port, "actions");
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void create(IAction action) {
        try {
            writeAction(action);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }
    
    protected void writeAction(IAction action) throws TupleSpaceException {
        org.jdom.Element actionElement = null; /*action.getXML(); Please compile before comiting API changes*/
        Field idField;
        Field timeField;
        Field typeField;
        Field userField;
        Field toolField = null;
        Field missionField = null;
        Field xmlField;
        Tuple actionTuple;

        if (ts != null) {
            idField = new Field(actionElement.getAttributeValue("id"));
            timeField = new Field(actionElement.getAttributeValue("time"));
            typeField = new Field(actionElement.getAttributeValue("type"));
            userField = new Field(actionElement.getAttributeValue("user"));
            for (org.jdom.Element elem : ((List<org.jdom.Element>) actionElement.getChild("context").getChildren("property"))) {
                if (elem.getAttributeValue("name").equals("tool")) {
                    toolField = new Field(elem.getAttributeValue("value"));
                } else if (elem.getAttributeValue("name").equals("mission")) {
                    missionField = new Field(elem.getAttributeValue("value"));
                }
            }
            try {
                Document doc = XMLUtils.parseString(new XMLOutputter(Format.getPrettyFormat()).outputString(actionElement) + "");
                xmlField = new Field(doc);
                actionTuple = new Tuple(idField, timeField, typeField, userField, toolField, missionField, xmlField);
                ts.write(actionTuple);
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
