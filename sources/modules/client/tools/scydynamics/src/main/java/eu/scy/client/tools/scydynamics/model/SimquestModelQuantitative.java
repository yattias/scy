package eu.scy.client.tools.scydynamics.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Logger;

import org.jdom.Element;
import org.nfunk.jep.JEP;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

import colab.um.draw.JdFigure;
import colab.um.draw.JdFlow;
import colab.um.draw.JdFlowCtr;
import colab.um.draw.JdObject;
import colab.um.draw.JdRelation;
import colab.um.draw.JdStock;
import eu.scy.client.tools.scydynamics.editor.ModelEditor;

@SuppressWarnings("serial")
public class SimquestModelQuantitative extends Element {

	private final static Logger LOGGER = Logger.getLogger(SimquestModelQuantitative.class.getName());

	protected Model model;
	protected ModelEditor modelEditor;
	protected HashMap<String, Double> simulationValues;

	public SimquestModelQuantitative(ModelEditor modelEditor) {
		this(modelEditor, new HashMap<String, Double>());
	}

	public SimquestModelQuantitative(ModelEditor modelEditor, HashMap<String, Double> simulationValues) {
		super("model");
		this.modelEditor = modelEditor;
		this.model = modelEditor.getModel();
		this.simulationValues = simulationValues;
		update();
	}

	public void update() {
		createHeader();
		createVariables();
		createComputationalModel();
		createSimulation();
	}

	private void createHeader() {
		Element header = new Element("header");
		header.addContent(new Element("name").setText("name of model"));
		header.addContent(new Element("format").setText("version1.0"));
		header.addContent(new Element("creator").setText(System.getProperty("user.name")));
		header.addContent(new Element("description").setText("some description"));
		header.addContent(new Element("timestamp").setText(new Date().toString()));
		this.addContent(header);
	}

	private void createVariables() {
		Element variables = new Element("variables");
		// always add a variable for "time"
		addVariable(variables, "time", "time", "0.0", null);
		for (JdObject node : model.getNodes().values()) {
			if (node.getType() == JdFigure.CONSTANT) {
				addVariable(variables, node.getLabel(), "constant", node.getExpr(), null);
			} else if (node.getType() == JdFigure.AUX) {
				addVariable(variables, node.getLabel(), "variable", "0.0", null);
			} else if (node.getType() == JdFigure.STOCK) {
				addVariable(variables, node.getLabel(), "state", node.getExpr(), node.getLabel() + "_dot");
				addVariable(variables, node.getLabel() + "_dot", "rate", "0.0", node.getLabel());
			}
		}
		this.addContent(variables);
	}

	protected void addVariable(Element variables, String name, String type, String value, String pair) {
		Element variable = new Element("modelVariable");
		variable.addContent(new Element("name").setText(name));
		variable.addContent(new Element("externalName").setText(name));
		variable.addContent(new Element("kind").setText(type));
		variable.addContent(new Element("type").setText("real"));
		if (simulationValues.containsKey(name)) {
			variable.addContent(new Element("value").setText(simulationValues.get(name).toString()));
		} else {
			variable.addContent(new Element("value").setText(value));
		}
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
				if (simulationValues.containsKey(node.getLabel())) {
					literal = new Element("literal").setText(simulationValues.get(node.getLabel()).toString());
				} else {
					literal = new Element("literal").setText(node.getExpr());
				}
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

	protected Vector<JdFigure> getOutgoingFigs(JdStock stock) {
		Vector<JdFigure> list = new Vector<JdFigure>();
		for (JdRelation rel : model.getRelations()) {
			if (rel.getFigure2().getType() == JdFigure.FLOWCTR) {
				if (((JdFlow) ((JdFlowCtr) rel.getFigure2()).getParent()).getFigure1() == stock) {
					list.add((JdFigure) rel.getFigure1());
				}
			}
		}
		return list;
	}

	protected Vector<JdFigure> getIncomingFigs(JdStock stock) {
		Vector<JdFigure> list = new Vector<JdFigure>();
		for (JdRelation rel : model.getRelations()) {
			if (rel.getFigure2().getType() == JdFigure.FLOWCTR) {
				if (((JdFlow) ((JdFlowCtr) rel.getFigure2()).getParent()).getFigure2() == stock) {
					list.add((JdFigure) rel.getFigure1());
				}
			}
		}
		return list;
	}

	protected void parseEquation(Element equation, String expr) {
		JEP parser = new JEP();
		parser.addStandardFunctions();
		parser.addStandardConstants();
		parser.setAllowUndeclared(true);
		parser.addFunction("min", new MinFunction());
		parser.addFunction("max", new MaxFunction());
		parser.addFunction("sign", new SignFunction());
		// parser.setTraverse(true);
		try {
			parser.parseExpression(expr);
			if (parser.getErrorInfo() != null) {
				throw new ParseException();
			}
			traverse(equation, parser.getTopNode());
		} catch (ParseException ex) {
			LOGGER.info(ex.getMessage());
			LOGGER.info(parser.getErrorInfo());
		}
	}

	private void traverse(Element element, Node node) throws ParseException {
		String fragment = new String(node.toString());
		// LOGGER.info(fragment);
		// handle this node
		if (fragment.startsWith("Function")) {
			Element operator = null;
			fragment = fragment.substring(10);
			fragment = fragment.replaceAll("\"", "");
			fragment = fragment.toLowerCase();

			// is this fragment an "operator" (+, -, /,..)
			// or a "function" (sin, cos,...)?
			// get the mapping to simquest terminology

			if (FormulaMapper.getInstance().getOperatorMap().get(fragment) != null) {
				operator = new Element("operator");
				operator.setAttribute("name", FormulaMapper.getInstance().getOperatorMap().get(fragment));
			} else if (FormulaMapper.getInstance().getFunctionMap().get(fragment) != null) {
				operator = new Element("function");
				operator.setAttribute("name", FormulaMapper.getInstance().getFunctionMap().get(fragment));
			}

			if (operator == null) {
				throw new ParseException("Could not identify operator or function '" + fragment + "'.");
			}

			// traverse children
			for (int i = 0; i < node.jjtGetNumChildren(); i++) {
				traverse(operator, node.jjtGetChild(i));
			}
			element.addContent(operator);
		} else if (fragment.startsWith("Constant")) {
			element.addContent(new Element("literal").setText(fragment.substring(10)));
			// a constant has no children to traverse
		} else if (fragment.startsWith("Variable")) {
			Element variable = new Element("variable");
			fragment = fragment.substring(10);
			fragment = fragment.replaceAll("\"", "");
			variable.setText(fragment);
			element.addContent(variable);
			// a variable has no children to traverse
		}
	}

	protected void createSimulation() {
		Element simulation = new Element("simulation");
		simulation.addContent(new Element("method").setText(model.getMethod()));
		simulation.addContent(new Element("startTime").setText(model.getStart() + ""));
		simulation.addContent(new Element("finishTime").setText(model.getStop() + ""));
		simulation.addContent(new Element("stepSize").setText(model.getStep() + ""));
		System.out.println("SimquestModelQuantitative.createSimulation (user settings): "+model.getMethod()+" / "+model.getStart()+"->"+model.getStop()+" | "+model.getStep());
		this.addContent(simulation);
	}

}