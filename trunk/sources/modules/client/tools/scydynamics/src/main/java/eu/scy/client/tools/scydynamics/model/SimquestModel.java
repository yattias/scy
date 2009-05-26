package eu.scy.client.tools.scydynamics.model;

import java.util.Date;
import java.util.Vector;

import org.jdom.Element;
import org.nfunk.jep.JEP;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

import colab.um.draw.JdAux;
import colab.um.draw.JdFigure;
import colab.um.draw.JdFlow;
import colab.um.draw.JdFlowCtr;
import colab.um.draw.JdObject;
import colab.um.draw.JdRelation;
import colab.um.draw.JdStock;

public class SimquestModel extends Element {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3296792249446085803L;
	private Model model;

	public SimquestModel(Model model) {
		super("model");
		this.model = model;
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
		header.addContent(new Element("creator").setText("lars"));
		header.addContent(new Element("description")
				.setText("some description"));
		header.addContent(new Element("timestamp").setText(new Date()
				.toString()));
		this.addContent(header);
	}

	private void createVariables() {
		Element variables = new Element("variables");
		// always add a variable for "time"
		addVariable(variables, "time", "time", "0.0", null);
		for (JdObject node : model.getNodes().values()) {
			if (node.getType() == JdFigure.CONSTANT) {
				addVariable(variables, node.getLabel(), "constant", node
						.getExpr(), null);
			} else if (node.getType() == JdFigure.AUX) {
				addVariable(variables, node.getLabel(), "variable", "0.0", null);
			} else if (node.getType() == JdFigure.STOCK) {
				addVariable(variables, node.getLabel(), "state",
						node.getExpr(), node.getLabel() + "_dot");
				addVariable(variables, node.getLabel() + "_dot", "rate", "0.0",
						node.getLabel());
			}
		}
		this.addContent(variables);
	}

	private void addVariable(Element variables, String name, String type,
			String value, String pair) {
		Element timevariable = new Element("modelVariable");
		timevariable.addContent(new Element("name").setText(name));
		timevariable.addContent(new Element("externalName").setText(name));
		timevariable.addContent(new Element("kind").setText(type));
		timevariable.addContent(new Element("type").setText("real"));
		timevariable.addContent(new Element("value").setText(value));
		if (pair != null) {
			timevariable.addContent(new Element("pair").setText(pair));
		}
		variables.addContent(timevariable);
	}

	private void createComputationalModel() {
		Element computationalModel = new Element("computationalModel");
		Element code = new Element("code");
		Element equation, variable, literal;

		// handle the nodes
		for (JdObject node : model.getNodes().values()) {
			if (node.getType() == JdFigure.CONSTANT) {
				equation = new Element("equation");
				variable = new Element("variable").setText(node.getLabel());
				literal = new Element("literal").setText(node.getExpr());
				equation.addContent(variable);
				equation.addContent(literal);
				code.addContent(equation);
			} else if (node.getType() == JdFigure.AUX) {
				equation = new Element("equation");
				equation.addContent(new Element("variable").setText(node
						.getLabel()));
				parseEquation(equation, node.getExpr());
				code.addContent(equation);
			} else if (node.getType() == JdFigure.STOCK) {
				Vector<JdFigure> incomingFigs = getIncomingFigs((JdStock) node);
				Vector<JdFigure> outgoingFigs = getOutgoingFigs((JdStock) node);
				equation = new Element("equation");
				equation.addContent(new Element("variable").setText(node
						.getLabel()
						+ "_dot"));

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

	private Vector<JdFigure> getOutgoingFigs(JdStock stock) {
		Vector<JdFigure> list = new Vector<JdFigure>();
		for (JdRelation rel : model.getRelations()) {
			if (rel.getFigure2().getType() == JdFigure.FLOWCTR) {
				if (((JdFlow) ((JdFlowCtr) rel.getFigure2()).getParent())
						.getFigure1() == stock) {
					list.add((JdAux) rel.getFigure1());
				}
			}
		}
		return list;
	}

	private Vector<JdFigure> getIncomingFigs(JdStock stock) {
		Vector<JdFigure> list = new Vector<JdFigure>();
		for (JdRelation rel : model.getRelations()) {
			if (rel.getFigure2().getType() == JdFigure.FLOWCTR) {
				if (((JdFlow) ((JdFlowCtr) rel.getFigure2()).getParent())
						.getFigure2() == stock) {
					list.add((JdFigure) rel.getFigure1());
				}
			}
		}
		return list;
	}

	private void parseEquation(Element equation, String expr) {
		JEP parser = new JEP();
		parser.addStandardFunctions();
		parser.addStandardConstants();
		parser.setAllowUndeclared(true);
		// parser.setTraverse(true);
		try {
			parser.parseExpression(expr);
			if (parser.getErrorInfo() != null) {
				throw new ParseException();
			}
			traverse(equation, parser.getTopNode());
		} catch (ParseException ex) {
			System.out.println(parser.getErrorInfo());
		}
	}

	private void traverse(Element element, Node node) {
		String fragment = new String(node.toString());
		// handle this node
		if (fragment.startsWith("Function")) {
			Element operator = new Element("operator");
			fragment = fragment.substring(10);
			fragment = fragment.replaceAll("\"", "");
			if (fragment.equals("UMinus"))
				fragment = "-.";
			operator.setAttribute("name", fragment);
			// traverse children
			for (int i = 0; i < node.jjtGetNumChildren(); i++) {
				traverse(operator, node.jjtGetChild(i));
			}
			element.addContent(operator);
		} else if (fragment.startsWith("Constant")) {
			element.addContent(new Element("literal").setText(fragment
					.substring(10)));
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

	private void createSimulation() {
		Element simulation = new Element("simulation");
		simulation.addContent(new Element("method").setText("euler"));
		simulation.addContent(new Element("startTime").setText(model.getStart()
				+ ""));
		simulation.addContent(new Element("finishTime").setText(model.getStop()
				+ ""));
		simulation.addContent(new Element("stepSize").setText(model.getStep()
				+ ""));
		this.addContent(simulation);
	}

}