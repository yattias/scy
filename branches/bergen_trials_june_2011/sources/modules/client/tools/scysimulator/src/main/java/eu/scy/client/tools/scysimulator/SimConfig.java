package eu.scy.client.tools.scysimulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
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

    private final static Logger debugLogger = Logger.getLogger(SimConfig.class.getName());
    private Element element;
    private HashMap<String, String> variableValueMap;
    private String simulationName;
    private String simulationUri;
    private ArrayList<String> relevantVariables;
    private MODE mode = MODE.collect_data;

     public enum MODE { explore_only, explore_simple_data, collect_simple_data, collect_data; }

//    public SimConfig(String simulationUri, boolean showDataCollector) {
//	this.simulationUri = simulationUri;
//	variableValueMap = new HashMap();
//	this.showDataCollector = showDataCollector;
//	relevantVariables = new ArrayList<String>();
//    }

     public SimConfig(String simulationUri, MODE mode) {
	 this.simulationUri = simulationUri;
	 this.mode = mode;
	 this.simulationName = "n/a";
	 relevantVariables = new ArrayList<String>();
	 variableValueMap = new HashMap();
     }

    public SimConfig(DataCollector datacollector) {
        // building a hashmap of all variables and their values of a simulation
	variableValueMap = new HashMap();
        ModelVariable var;
        for (Iterator<ModelVariable> variables = datacollector.getSimulationVariables().iterator(); variables.hasNext();) {
            var = variables.next();
            variableValueMap.put(var.getName(), var.getValueString());
        }
	// building a list of all variables that have been marked as being relevant (to record a dataset)
	relevantVariables = new ArrayList<String>();
	for (ModelVariable modelVar: datacollector.getSelectedVariables()) {
	    relevantVariables.add(modelVar.getName());
	}
        simulationName = datacollector.getSimQuestViewer().getApplication().getTopic(0).getName();
        simulationUri = datacollector.getSimQuestViewer().getFile().toString();
	mode = datacollector.getMode();
    }

    public SimConfig(String xmlString) throws JDOMException {
        this(new JDomStringConversion().stringToXml(xmlString));
    }

    public SimConfig(Element xmlElem) throws JDOMException {
	variableValueMap = new HashMap();
	relevantVariables = new ArrayList<String>();

        if (xmlElem.getName().equals("simconfig")) {
            this.element = xmlElem;
            this.simulationName = xmlElem.getAttributeValue("simulationname");
            this.simulationUri = xmlElem.getAttributeValue("simulationuri");
	    String modeString = xmlElem.getAttributeValue("mode", "not_found");
	    this.mode = getModeFromString(modeString);
            Element xmlVariable;
            for (Iterator<Element> xmlVariables = xmlElem.getChildren("variable").iterator(); xmlVariables.hasNext();) {
                xmlVariable = xmlVariables.next();
                variableValueMap.put(xmlVariable.getAttributeValue("name"), xmlVariable.getAttributeValue("value"));
            }
	    for (Iterator<Element> xmlVariables = xmlElem.getChildren("relevantVariable").iterator(); xmlVariables.hasNext();) {
                xmlVariable = xmlVariables.next();
                relevantVariables.add(xmlVariable.getAttributeValue("name"));
            }
        } else {
            throw (new JDOMException(
                    "SimConfig expects <simconfig> as root element, but found <" + xmlElem.getName() + ">."));
        }
    }

    public static MODE getModeFromString(String modeString) {
	if (modeString.equals("explore_only")) {
		return MODE.explore_only;
	    } else if (modeString.equals("explore_simple_data")) {
		return MODE.explore_simple_data;
	    } else if (modeString.equals("collect_simple_data")) {
		return MODE.collect_simple_data;
	    } else if (modeString.equals("collect_data")) {
		return MODE.collect_data;
	    } else if (modeString.equals("not_found")) {
		debugLogger.severe("mode not found, set to default 'collect_data'");
		return MODE.collect_data;
	    } else {
		debugLogger.severe("unknown mode, set to default 'collect_data'");
		return MODE.collect_data;
	    }
    }

    public MODE getMode() {
	return this.mode;
    }

    public void setMode(MODE newMode) {
	this.mode = newMode;
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
	    element.setAttribute("mode", this.mode.toString());
	    Element variableElement;
            String variableName;
            for (Iterator<String> variables = variableValueMap.keySet().iterator(); variables.hasNext();) {
                variableName = variables.next();
                variableElement = new Element("variable");
                variableElement.setAttribute("name", variableName);
                variableElement.setAttribute("value", variableValueMap.get(variableName));
                element.addContent(variableElement);
            }
	    for (String varName: relevantVariables) {
                variableElement = new Element("relevantVariable");
                variableElement.setAttribute("name", varName);
                element.addContent(variableElement);
            }
        }
        return element;
    }

    public HashMap<String, String> getVariables() {
        return variableValueMap;
    }

    public ArrayList<String> getRelevantVariables() {
	return relevantVariables;
    }
}

