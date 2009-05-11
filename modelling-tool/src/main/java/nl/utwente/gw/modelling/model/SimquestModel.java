package nl.utwente.gw.modelling.model;

import java.util.Date;

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
		header.addContent(new Element("description").setText("some description"));
		header.addContent(new Element("timestamp").setText(new Date().toString()));		
		this.addContent(header);	
	}

	private void createVariables() {
		Element variables = new Element("variables");
		// always add a variable for "time"
		addVariable(variables, "time", "time", "0.0", null);
		for (JdObject node: model.getNodes().values()) {
			if (node.getType() == JdFigure.CONSTANT) {
				addVariable(variables, node.getLabel(), "constant", node.getExpr(), null);
			} else if (node.getType() == JdFigure.AUX) {
				addVariable(variables, node.getLabel(), "variable", "0.0", null);		
			} else if (node.getType() == JdFigure.STOCK) {
				addVariable(variables, node.getLabel(), "state", node.getExpr(), node.getLabel()+"_dot");
				addVariable(variables, node.getLabel()+"_dot", "rate", "0.0", node.getLabel());
			}
		}		
		this.addContent(variables);
	}

	private void addVariable(Element variables, String name, String type, String value, String pair) {
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
		Element equation, variable, literal, operator;

		// handle the nodes
		for (JdObject node: model.getNodes().values()) {
			if (node.getType() == JdFigure.CONSTANT) {
				equation = new Element("equation");
				variable = new Element("variable").setText(node.getLabel());
				literal = new Element("literal").setText(node.getExpr());
				equation.addContent(variable);
				equation.addContent(literal);
				code.addContent(equation);
			} else if (node.getType() == JdFigure.AUX) {
				equation = new Element("equation");
				equation.addContent(new Element("variable").setText(node.getLabel()));
				parseEquation(equation, node.getExpr());
				code.addContent(equation);
			}
			// STOCKS don't have equations on their own, their change-over-time is processed
			// by the equations for the flows
		}

		// handle the flows
		for (JdObject node: model.getLinks()) {
			System.out.println("*** parsing link: "+node);
			if (node.getType() == JdFigure.RELATION) {
				System.out.println("   *** parsing relation: "+node);
				System.out.println("   *** pointing to: "+((JdRelation)node).getFigure2());
				if (((JdRelation)node).getFigure2().getType() == JdFigure.FLOWCTR) {
					System.out.println("      *** parsing flow: "+node);
					// okay, found a relation that points to a flow, go on
					JdAux aux = (JdAux)((JdRelation)node).getFigure1();
					JdFlowCtr flowctr = (JdFlowCtr)((JdRelation)node).getFigure2();
					JdFlow flow = (JdFlow)flowctr.parent;

					if (flow.getFigure1()!=null) {
						equation = new Element("equation");
						JdStock stockOut = (JdStock)flow.getFigure1();
						equation.addContent(new Element("variable").setText(stockOut.getLabel()+"_dot"));
						operator = new Element("operator").setAttribute("name", "*");
						operator.addContent(new Element("literal").setText("-1"));
						operator.addContent(new Element("variable").setText(aux.getLabel()));
						equation.addContent(operator);
						code.addContent(equation);
					}

					if (flow.getFigure2()!=null) {
						equation = new Element("equation");
						JdStock stockIn = (JdStock)flow.getFigure2();
						equation.addContent(new Element("variable").setText(stockIn.getLabel()+"_dot"));
						equation.addContent(new Element("variable").setText(aux.getLabel()));
						code.addContent(equation);
					}
				}
			}
		}

		computationalModel.addContent(code);
		this.addContent(computationalModel);
	}

	private void parseEquation(Element equation, String expr) {
		JEP parser = new JEP();
		parser.addStandardFunctions();
		parser.addStandardConstants();
		parser.setAllowUndeclared(true);
		//parser.setTraverse(true);
		try {
			parser.parseExpression(expr);
			//System.out.println(parser.getSymbolTable().toString());
			if (parser.getErrorInfo()!=null) {		
				throw new ParseException();
			}		
			traverse(equation, parser.getTopNode());
		} catch (ParseException ex){
			System.out.println(parser.getErrorInfo());
		}
	}

	private void traverse(Element element, Node node) {
		System.out.println(node.toString());
		String fragment = new String(node.toString());

		// handle this node
		if (fragment.startsWith("Function")) {
			Element operator = new Element("operator");
			fragment = fragment.substring(10);
			fragment = fragment.replaceAll("\"", "");
			operator.setAttribute("name", fragment);			
			// traverse children
			for (int i=0; i<node.jjtGetNumChildren(); i++) {
				traverse(operator, node.jjtGetChild(i));
			}
			element.addContent(operator);
		}
		else if (fragment.startsWith("Constant")) {
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

	private void createSimulation() {
		Element simulation = new Element("simulation");
		simulation.addContent(new Element("method").setText("euler"));
		simulation.addContent(new Element("startTime").setText(model.getStart()+""));
		simulation.addContent(new Element("finishTime").setText(model.getStop()+""));
		simulation.addContent(new Element("stepSize").setText(model.getStep()+""));
		this.addContent(simulation);
	}

}