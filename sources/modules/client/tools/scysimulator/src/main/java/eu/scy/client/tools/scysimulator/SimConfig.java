package eu.scy.client.tools.scysimulator;

import java.util.HashMap;
import java.util.Iterator;
import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;
import sqv.ModelVariable;

/**
 * This class holds the names and values of all input variables of a SimQuest
 * simulation. It can be constructed by using a DataCollector or an XML
 * representation.
 * @author Lars Bollen
 */
public class SimConfig {

    private Element element;
    private HashMap<String, String> variableValueMap;
    private String simulationName;
    private String simulationUri;

    public SimConfig(DataCollector datacollector) {
        variableValueMap = new HashMap();
        // building a hashmap of all variables and their values of a simulation
        ModelVariable var;
        for (Iterator<ModelVariable> variables = datacollector.getSimulationVariables().iterator(); variables.hasNext();) {
            var = variables.next();
            variableValueMap.put(var.getName(), var.getValueString());
        }
        simulationName = datacollector.getSimQuestViewer().getApplication().getTopic(0).getName();
        simulationUri = datacollector.getSimQuestViewer().getFile().toString();
        // System.out.println("SimConfig.simulationName: "+simulationName);
        // System.out.println("SimConfig.simulationUri: "+simulationUri);
    }

    public SimConfig(String xmlString) throws JDOMException {
        this(new JDomStringConversion().stringToXml(xmlString));
    }

    public SimConfig(Element xmlElem) throws JDOMException {
        variableValueMap = new HashMap();
        if (xmlElem.getName().equals("simconfig")) {
            this.element = xmlElem;
            this.simulationName = xmlElem.getAttributeValue("simulationname");
            this.simulationUri = xmlElem.getAttributeValue("simulationuri");
            Element xmlVariable;
            for (Iterator<Element> xmlVariables = xmlElem.getChildren().iterator(); xmlVariables.hasNext();) {
                xmlVariable = xmlVariables.next();
                variableValueMap.put(xmlVariable.getAttributeValue("name"), xmlVariable.getAttributeValue("value"));
            }
        } else {
            throw (new JDOMException(
                    "SimConfig expects <simconfig> as root element, but found <" + xmlElem.getName() + ">."));
        }
    }

    public String getSimulationName() {
        return simulationName;
    }

    public String getSimulationUri() {
        return simulationUri;
    }

    public Element toXML() {
        if (element == null) {
            element = new Element("simconfig");
            element.setAttribute("simulationname", simulationName);
            element.setAttribute("simulationuri", simulationUri);
            Element variableElement;
            String variableName;
            for (Iterator<String> variables = variableValueMap.keySet().iterator(); variables.hasNext();) {
                variableName = variables.next();
                variableElement = new Element("variable");
                variableElement.setAttribute("name", variableName);
                variableElement.setAttribute("value", variableValueMap.get(variableName));
                element.addContent(variableElement);
            }
        }
        return element;
    }

    public HashMap<String, String> getVariables() {
        return variableValueMap;
    }
}

