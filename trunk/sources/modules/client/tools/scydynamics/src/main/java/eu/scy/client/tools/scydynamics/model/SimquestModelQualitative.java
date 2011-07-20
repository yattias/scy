package eu.scy.client.tools.scydynamics.model;

import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Logger;

import org.jdom.Element;

import colab.um.draw.JdFigure;
import colab.um.draw.JdObject;
import colab.um.draw.JdStock;

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
	
	@Override
	protected void addVariable(Element variables, String name, String type, String value, String pair) {
		Element variable = new Element("modelVariable");
		variable.addContent(new Element("name").setText(name));
		variable.addContent(new Element("externalName").setText(name));
		variable.addContent(new Element("kind").setText(type));
		variable.addContent(new Element("type").setText("real"));
		String newValue;
		if (simulationValues.containsKey(name)) {
			newValue = getQualitativeValue(name, simulationValues.get(name).toString())+"";
		} else {
			newValue = getQualitativeValue(name, value)+"";
		}
		System.out.println("SimquestModelQualitative.addVariable: "+name+": "+value+" -> "+newValue);
		variable.addContent(new Element("value").setText(newValue));
		if (pair != null) {
			variable.addContent(new Element("pair").setText(pair));
		}
		variables.addContent(variable);
	}
	
	protected void createComputationalModel() {
		Element computationalModel = new Element("computationalModel");
		Element code = new Element("code");
		Element equation, variable, literal;

		// handle the nodes
		for (JdObject node : model.getNodes().values()) {
			if (node.getType() == JdFigure.CONSTANT) {
				equation = new Element("equation");
				variable = new Element("variable").setText(node.getLabel());
				String newValue;
				if (simulationValues.containsKey(node.getLabel())) {
					newValue = getQualitativeValue(node.getLabel(), simulationValues.get(node.getLabel()).toString())+"";
				} else {
					newValue = getQualitativeValue(node.getLabel(), node.getExpr())+"";
				}	
				System.out.println("SimquestModelQualitative.createComputationalModel: literal "+node.getLabel()+": "+node.getExpr()+" -> "+newValue);
				literal = new Element("literal").setText(newValue);
				equation.addContent(variable);
				equation.addContent(literal);
				code.addContent(equation);
			} else if (node.getType() == JdFigure.AUX) {
				equation = new Element("equation");
				equation.addContent(new Element("variable").setText(node.getLabel()));
				parseEquation(equation, node.getExpr());
				code.addContent(equation);
			} else if (node.getType() == JdFigure.STOCK) {
				Vector<JdFigure> incomingFigs = getIncomingFigs((JdStock) node);
				Vector<JdFigure> outgoingFigs = getOutgoingFigs((JdStock) node);
				equation = new Element("equation");
				equation.addContent(new Element("variable").setText(node.getLabel() + "_dot"));
				String expression = new String();
				for (JdFigure fig : incomingFigs) {
					// incoming auxs are added to the stock's _dot
					expression = expression + "+" + fig.getProperties().get("label");
				}
				for (JdFigure fig : outgoingFigs) {
					// outgoing auxs are substracted from the stocks's _dot
					expression = expression + "-" + fig.getProperties().get("label");
				}
				parseEquation(equation, expression);
				code.addContent(equation);
			}
		}

		computationalModel.addContent(code);
		this.addContent(computationalModel);
	}

	public double getQualitativeValue(String name, String valueString) {
		double value = Double.valueOf(valueString);
		if (modelEditor.getDomain() == null) {
			return getDefaultQualitativeValue(value);
		}
		String concept = modelEditor.getDomain().getConceptByTerm(name);
		Node node = modelEditor.getDomain().getNodeByConcept(concept);
		if (node == null) {
			return getDefaultQualitativeValue(value);
		} else {
			return getNodeQualitativeValue(value, node);
		}
	}

	private double getNodeQualitativeValue(double value, Node node) {
		try {
			return getValueFromPercent(value, node);
//			if (Double.valueOf(value)<0) {
//				Double negativeRange = Double.valueOf(node.getHighNegative());
//				Double doubleValue = Double.valueOf(value);
//				Double newValue = new Double(doubleValue*0.01*negativeRange);
//				return newValue;
//			} else {
//				Double positiveRange = Double.valueOf(node.getHighPositive());
//				Double doubleValue = Double.valueOf(value);
//				Double newValue = new Double(doubleValue*0.01*positiveRange);
//				return newValue;
//			}
		} catch (Exception ex) {
			LOGGER.info("caught a "+ex.getMessage());
			return getDefaultQualitativeValue(value);
		}	
	}
	
	private double getValueFromPercent(double percentValue, Node node) {
		if (percentValue<=0) {
			return node.getLowValue();
		} else if (percentValue>=100) {
			return node.getHighValue();
		} else {
			return node.getLowValue()+(node.getHighValue()-node.getLowValue())*percentValue*0.01;
		}
	}

	private double getDefaultQualitativeValue(double value) {
//		try {
//			switch (Integer.parseInt(value)) {
//				case ModelEditor.LARGE_NEGATIVE: return "-10.0";
//				case ModelEditor.SMALL_NEGATIVE: return "-1.0";
//				case ModelEditor.ZERO: return "0.0";
//				case ModelEditor.SMALL_POSITIVE: return "1.0";
//				case ModelEditor.LARGE_POSITIVE: return "10.0";
//				default: return "1.0";
//			}
//		} catch (Exception ex) {
//			return value;
//		}
		
		// actually doing nothing here...
		return value;
	}
	
	@Override
	protected void createSimulation() {
		Element simulation = new Element("simulation");
		if (modelEditor.getDomain() != null) {
			simulation.addContent(new Element("method").setText(modelEditor.getDomain().getSimulationSettings().getCalculationMethod()));
			simulation.addContent(new Element("startTime").setText(modelEditor.getDomain().getSimulationSettings().getStartTime()+""));
			simulation.addContent(new Element("finishTime").setText(modelEditor.getDomain().getSimulationSettings().getStopTime()+""));
			simulation.addContent(new Element("stepSize").setText(modelEditor.getDomain().getSimulationSettings().getStepSize()+""));
			System.out.println("SimquestModelQualtitative.createSimulation (domain settings): "+model.getMethod()+" / "+model.getStart()+"->"+model.getStop()+" | "+model.getStep());
		} else {
			simulation.addContent(new Element("method").setText("RungeKuttaFehlberg"));
			simulation.addContent(new Element("startTime").setText(model.getStart()+""));
			simulation.addContent(new Element("finishTime").setText(model.getStop()+""));
			simulation.addContent(new Element("stepSize").setText(model.getStep()+""));
			System.out.println("SimquestModelQualtitative.createSimulation (default): "+model.getMethod()+" / "+model.getStart()+"->"+model.getStop()+" | "+model.getStep());

		}
		this.addContent(simulation);
	}

}