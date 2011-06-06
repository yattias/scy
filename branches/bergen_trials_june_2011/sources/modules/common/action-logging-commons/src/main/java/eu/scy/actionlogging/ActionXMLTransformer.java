package eu.scy.actionlogging;

import java.util.Iterator;
import org.dom4j.Element;
import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultCDATA;
import org.dom4j.tree.DefaultElement;
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
            //actionElement.add(new DefaultAttribute("time", actionPojo.getTime()));
            actionElement.add(new DefaultAttribute("timemillis", String.valueOf(actionPojo.getTimeInMillis())));

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
            e = new DefaultElement("eloURI");
            if (actionPojo.getContext(ContextConstants.eloURI)==null) {
            	e.setText("n/a");
            } else {
            	e.setText(actionPojo.getContext(ContextConstants.eloURI));
            }
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
                propertyElement.add(new DefaultCDATA(actionPojo.getAttribute(key)));
                //propertyElement.add(new DefaultAttribute("value", actionPojo.getAttribute(key)));
                attributesElement.add(propertyElement);
            }
            actionElement.add(attributesElement);

            // creating the data element
            /*if (actionPojo.getData() != null) {
                Element dataElement = new DefaultElement("data");
                dataElement.add(new DefaultAttribute("type", actionPojo.getDataType()));
                dataElement.add(new DefaultCDATA(actionPojo.getData()));
                actionElement.add(dataElement);
            }*/

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
            //actionPojo.setTime(actionElement.attributeValue("time"));
            actionPojo.setTimeInMillis(Long.parseLong(actionElement.attributeValue("timemillis")));
            // creating the context information
            Element contextElement = actionElement.element("context");
            actionPojo.addContext(ContextConstants.tool, contextElement.elementText("tool"));
            actionPojo.addContext(ContextConstants.mission, contextElement.elementText("mission"));
            actionPojo.addContext(ContextConstants.session, contextElement.elementText("session"));
            actionPojo.addContext(ContextConstants.eloURI, contextElement.elementText("eloURI"));
            // creating the attribute list (key/value)
            Element attributesElement = actionElement.element("attributes");
            for (Iterator<Element> it = attributesElement.elementIterator("property"); it.hasNext();) {
                Element nextElement = it.next();
                String key = nextElement.attributeValue("name");
                //String value = nextElement.attributeValue("value");
                String value = nextElement.getStringValue();
                actionPojo.addAttribute(key, value);
            }
            // creating the data element
           /* Element dataElement = actionElement.element("data");
            if (dataElement != null) {
                actionPojo.setDataType(dataElement.attributeValue("type"));
                actionPojo.setData(dataElement.getStringValue());
            }*/
            return actionPojo;
        }
    }
}
