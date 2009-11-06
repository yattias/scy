package eu.scy.actionlogging;

import info.collide.sqlspaces.commons.util.XMLUtils;

import java.util.Iterator;

import org.dom4j.Element;
import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultCDATA;
import org.dom4j.tree.DefaultElement;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.Context;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;

public class ActionXMLTransformer {

    private IAction actionPojo;

    private Element actionElement;

    public ActionXMLTransformer(IAction actionPojo) {
        this.actionPojo = actionPojo;
    }

    public ActionXMLTransformer(Element actionElement) {
        this.actionElement = actionElement;
    }

    public String getActionAsString() {
        return this.getActionAsElement().asXML();
    }

    public Element getActionAsElement() {
        if (actionElement != null) {
            return actionElement;
        } else {
            // creating the basics
            actionElement = new DefaultElement("action");
            actionElement.add(new DefaultAttribute("id", actionPojo.getId()));
            actionElement.add(new DefaultAttribute("user", actionPojo.getUser()));
            actionElement.add(new DefaultAttribute("type", actionPojo.getType()));
            actionElement.add(new DefaultAttribute("time", actionPojo.getTime()));

            // creating the context information
            Element contextElement = new DefaultElement("context");
            Element e = new DefaultElement("tool");
            e.setText(actionPojo.getContext(ContextConstants.tool));
            contextElement.add(e);
            e = new DefaultElement("mission");
            e.setText(actionPojo.getContext(ContextConstants.mission));
            contextElement.add(e);
            e = new DefaultElement("session");
            e.setText(actionPojo.getContext(ContextConstants.session));
            contextElement.add(e);
            actionElement.add(contextElement);

            // creating the attribute list (name/value)
            Element attributesElement = new DefaultElement("attributes");
            String key;
            Element propertyElement;
            for (Iterator<String> it = actionPojo.getAttributes().keySet().iterator(); it.hasNext();) {
                key = it.next();
                propertyElement = new DefaultElement("property");
                propertyElement.add(new DefaultAttribute("name", key));
                propertyElement.add(new DefaultAttribute("value", actionPojo.getAttribute(key)));
                attributesElement.add(propertyElement);
            }
            actionElement.add(attributesElement);

            // creating the data element
            if (actionPojo.getData() != null) {
                Element dataElement = new DefaultElement("data");
                dataElement.add(new DefaultAttribute("type", actionPojo.getDataType()));
                // dataElement.setText(new DefaultCDATA(actionPojo.getData()).toString());
                dataElement.add(new DefaultCDATA(actionPojo.getData()));
                actionElement.add(dataElement);
            }

            return actionElement;
        }
    }

    public IAction getActionAsPojo() {
        if (actionPojo != null) {
            return actionPojo;
        } else {
            // creating the basics
            actionPojo = new Action();
            actionPojo.setUser(actionElement.attributeValue("user"));
            actionPojo.setType(actionElement.attributeValue("type"));
            actionPojo.setTime(actionElement.attributeValue("time"));
            // creating the context information
            Element contextElement = actionElement.element("context");
            actionPojo.addContext(ContextConstants.tool, contextElement.elementText("tool"));
            actionPojo.addContext(ContextConstants.mission, contextElement.elementText("mission"));
            actionPojo.addContext(ContextConstants.session, contextElement.elementText("session"));
            // creating the attribute list (key/value)
            Element attributesElement = actionElement.element("attributes");
            for (Iterator<Element> it = attributesElement.elementIterator("property"); it.hasNext();) {
               Element nextElement = it.next();
                String key = nextElement.attributeValue("name");
                String value = nextElement.attributeValue("value");
                actionPojo.addAttribute(key, value);
            }
            // creating the data element
            Element dataElement = actionElement.element("data");
            if (dataElement != null) {
                actionPojo.setDataType(dataElement.attributeValue("type"));
                actionPojo.setData(dataElement.getStringValue());
            }
            return actionPojo;
        }
    }

    public static void main(String[] args) {
        IAction action = new Action();
        action.setUser("test");
        action.setType("testtype");
        action.setContext(new Context("testtool", "testmission", "testSession"));
        action.addAttribute("bla", "blub");
        action.addAttribute("super", "jan");
        action.addAttribute("chuck", "norris");
        action.setDataType("String");
        action.setData("The ActionXMLTransformer shall convert an IAction into a XMLElement and vice versa. It exits (twice) in the ScyDynamics Project and in the SCYSimulator. To prevent duplicate code and the sweet temptation of copy 'n paste, this should be moved to action-logging-commons");
        ActionXMLTransformer axt = new ActionXMLTransformer(action);
        Element actionAsElement = axt.getActionAsElement();
        String syso = actionAsElement.toString();
        System.out.println(syso);
        System.out.println("----");
        System.out.println("vice versa");
        System.out.println("----");
        ActionXMLTransformer axt2 = new ActionXMLTransformer(actionAsElement);
        IAction actionAsPojo = axt2.getActionAsPojo();
        System.out.println(actionAsPojo.toString());
    }

}
