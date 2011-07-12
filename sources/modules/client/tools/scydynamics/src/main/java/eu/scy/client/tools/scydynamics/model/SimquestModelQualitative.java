package eu.scy.client.tools.scydynamics.model;

import java.util.HashMap;
import java.util.logging.Logger;

import org.jdom.Element;

import eu.scy.client.tools.scydynamics.domain.Concept;
import eu.scy.client.tools.scydynamics.domain.Node;
import eu.scy.client.tools.scydynamics.editor.ModelEditor;

@SuppressWarnings("serial")
public class SimquestModelQualitative extends SimquestModelQuantitative {

	private final static Logger LOGGER = Logger.getLogger(SimquestModelQualitative.class.getName());

	public SimquestModelQualitative(ModelEditor modelEditor) {
		this(modelEditor, new HashMap<String, Double>());
	}

	public SimquestModelQualitative(ModelEditor modelEditor, HashMap<String, Double> simulationValues) {
		super(modelEditor, simulationValues);
	}

//	@Override
//	protected void addVariable(Element variables, String name, String type, String value, String pair) {
//		Element variable = new Element("modelVariable");
//		variable.addContent(new Element("name").setText(name));
//		variable.addContent(new Element("externalName").setText(name));
//		variable.addContent(new Element("kind").setText(type));
//		variable.addContent(new Element("type").setText("real"));
//		String variableValue;
//		if (simulationValues.containsKey(name)) {
//			variableValue = simulationValues.get(name).toString();
//			variable.addContent(new Element("value").setText(variableValue));
//		} else {
//			variableValue = getQualitativeValue(name, value);
//			variable.addContent(new Element("value").setText(variableValue));
//		}
//		LOGGER.info("setting value of "+name+" to "+variableValue);
//		if (pair != null) {
//			variable.addContent(new Element("pair").setText(pair));
//		}
//		variables.addContent(variable);
//	}

	public static String getQualitativeValue(String name, String value, ModelEditor modelEditor) {
		String concept = modelEditor.getDomain().getConceptByTerm(name);
		Node node = modelEditor.getDomain().getNodeByConcept(concept);
		LOGGER.info("concept: "+concept+" node: "+node);
		if (node == null) {
			return getDefaultQualitativeValue(value);
		} else {
			return getNodeQualitativeValue(value, node);
		}
	}

	private static String getNodeQualitativeValue(String value, Node node) {
		String returnValue = null;
		try {
			switch (Integer.parseInt(value)) {
			case ModelEditor.LARGE_NEGATIVE: returnValue = node.getHighNegative(); break;
			case ModelEditor.SMALL_NEGATIVE: returnValue = node.getLowNegative(); break;
			case ModelEditor.ZERO: returnValue = node.getHighNegative(); break;
			case ModelEditor.SMALL_POSITIVE: returnValue = node.getLowPositive(); break;
			case ModelEditor.LARGE_POSITIVE: returnValue = node.getHighPositive(); break;
			default: returnValue = value;
			}
			if (returnValue == null) {
				return getDefaultQualitativeValue(value);
			} else {
				return returnValue;
			}
		} catch (Exception ex) {
			return value;
		}
	}

	private static String getDefaultQualitativeValue(String value) {
		try {
			switch (Integer.parseInt(value)) {
				case ModelEditor.LARGE_NEGATIVE: return "-10.0";
				case ModelEditor.SMALL_NEGATIVE: return "-1.0";
				case ModelEditor.ZERO: return "0.0";
				case ModelEditor.SMALL_POSITIVE: return "1.0";
				case ModelEditor.LARGE_POSITIVE: return "10.0";
				default: return "1.0";
			}
		} catch (Exception ex) {
			return value;
		}
	}

}