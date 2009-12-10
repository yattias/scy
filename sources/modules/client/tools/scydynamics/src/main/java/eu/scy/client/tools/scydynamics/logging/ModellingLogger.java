package eu.scy.client.tools.scydynamics.logging;

import java.awt.Component;
import java.awt.IllegalComponentStateException;

import colab.um.draw.JdAux;
import colab.um.draw.JdConst;
import colab.um.draw.JdFigure;
import colab.um.draw.JdFlow;
import colab.um.draw.JdFlowCtr;
import colab.um.draw.JdRelation;
import colab.um.draw.JdStock;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;

public class ModellingLogger implements IModellingLogger {

	private IAction action;
	private IActionLogger logger;
	private String username;
	private String mission = "mission1";
	private String toolname = "scydynamics";
	
	public ModellingLogger(IActionLogger logger, String username) {
		this.logger = logger;
		this.username = username;
	}

	public void close() {
		// TODO
		//logSQLSpacesHandler.close();
	}
	
	/* (non-Javadoc)
	 * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logAddAction(colab.um.draw.JdFigure, java.lang.String)
	 */
	public void logAddAction(JdFigure object, String modelString) {
		switch (object.getType()) {
		case JdFigure.AUX:
			action = createBasicAction("add_auxiliary");
			action.addAttribute("id", object.getID());
			action.addAttribute("name", ((JdAux) object).getLabel());
			break;
		case JdFigure.CONSTANT:
			action = createBasicAction("add_constant");
			action.addAttribute("id", object.getID());
			action.addAttribute("name", ((JdConst) object).getLabel());
			break;
		case JdFigure.STOCK:
			action = createBasicAction("add_stock");
			action.addAttribute("id", object.getID());
			action.addAttribute("name", ((JdStock) object).getLabel());
			break;
		case JdFigure.RELATION:
			action = createBasicAction("add_relation");
			action.addAttribute("id", object.getID());
			action.addAttribute("name", ((JdRelation)object).getLabel());
			action.addAttribute("from", ((JdRelation) object).getFigure1()
					.getID());
			// if the "to" object of a relation is the center of a flow,
			// list the target of the parent (flow) here
			if (((JdRelation) object).getFigure2().getType() == JdFigure.FLOWCTR) {
				action.addAttribute("to", ((JdFlowCtr) ((JdRelation) object)
						.getFigure2()).getParent().getID());
			} else {
				action.addAttribute("to", ((JdRelation) object).getFigure2()
						.getID());
			}
			break;
		case JdFigure.FLOW:
			action = createBasicAction("add_flow");
			action.addAttribute("id", object.getID());
			action.addAttribute("name", ((JdFlow)object).getLabel());
			// the ends of a flow may be null
			if (((JdFlow) object).getFigure1() != null) {
				action.addAttribute("from", ((JdFlow) object).getFigure1()
						.getID());
			}
			if (((JdFlow) object).getFigure2() != null) {
				action.addAttribute("to", ((JdFlow) object).getFigure2()
						.getID());
			}
			break;
		case JdFigure.DATASET:
			action = createBasicAction("add_dataset");
			action.addAttribute("id", object.getID());
		}
		if (action != null) {
			action.addAttribute("model", modelString);
			logger.log(action);
		}
	}
	
	/* (non-Javadoc)
	 * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logDeleteAction(colab.um.draw.JdFigure, java.lang.String)
	 */
	public void logDeleteAction(JdFigure object, String modelString) {
		switch (object.getType()) {
		case JdFigure.AUX:
			action = createBasicAction("delete_auxiliary");
			action.addAttribute("id", object.getID());
			break;
		case JdFigure.CONSTANT:
			action = createBasicAction("delete_constant");
			action.addAttribute("id", object.getID());
			break;
		case JdFigure.STOCK:
			action = createBasicAction("delete_stock");
			action.addAttribute("id", object.getID());
			break;
		case JdFigure.RELATION:
			action = createBasicAction("delete_relation");
			action.addAttribute("id", object.getID());
			action.addAttribute("from", ((JdRelation) object).getFigure1()
					.getID());
			
			// if figure2 is null, then a link is deleted that has not really been drawn,
			// so, do nothing then
			if (((JdRelation) object).getFigure2() == null) {
				return;
			}
			
			// if the "to" object of a relation is the center of a flow,
			// list the target of the parent (flow) here
			if (((JdRelation) object).getFigure2().getType() == JdFigure.FLOWCTR) {
				action.addAttribute("to", ((JdFlowCtr) ((JdRelation) object)
						.getFigure2()).getParent().getID());
			} else {
				action.addAttribute("to", ((JdRelation) object).getFigure2()
						.getID());
			}
			break;
		case JdFigure.FLOW:
			action = createBasicAction("delete_flow");
			action.addAttribute("id", object.getID());
			// the ends of a flow may be null
			if (((JdFlow) object).getFigure1() != null) {
				action.addAttribute("from", ((JdFlow) object).getFigure1()
						.getID());
			}
			if (((JdFlow) object).getFigure2() != null) {
				action.addAttribute("to", ((JdFlow) object).getFigure2()
						.getID());
			}
			break;
		case JdFigure.DATASET:
			action = createBasicAction("delete_dataset");
			action.addAttribute("id", object.getID());
		}
		if (action != null) {
			action.addAttribute("model", modelString);
			logger.log(action);
		}
	}
	
	public IAction createBasicAction(String type) {
		IAction action = new Action();
		action.setType(type);
		action.setUser(username);
		action.addContext(ContextConstants.tool, toolname);
		action.addContext(ContextConstants.mission, mission);
		action.addContext(ContextConstants.session, "n/a");
		return action;
	}

	/* (non-Javadoc)
	 * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logRenameAction(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void logRenameAction(String id, String oldName, String newName) {
		action = createBasicAction("rename_element");
		action.addAttribute("id", id);
		action.addAttribute("old", oldName);
		action.addAttribute("new", newName);
		logger.log(action);
	}
	
	/* (non-Javadoc)
	 * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logChangeSpecification(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void logChangeSpecification(String id, String name, String expression, String unit, String modelString) {
		action = createBasicAction("change_specification");
		action.addAttribute("id", id);
		action.addAttribute("name", name);
		action.addAttribute("expression", expression);
		action.addAttribute("unit", unit);
		action.addAttribute("model", modelString);
		logger.log(action);
	}

	/* (non-Javadoc)
	 * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logSimpleAction(java.lang.String)
	 */
	public void logSimpleAction(String type) {	
		logger.log(createBasicAction(type));
	}
	
	/* (non-Javadoc)
	 * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logSimpleAction(java.lang.String)
	 */
	public void logSimpleAction(String type, String modelString) {
		action = createBasicAction(type);
		action.addAttribute("model", modelString);
		logger.log(action);
	}

	/* (non-Javadoc)
	 * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logInspectVariablesAction(java.lang.String, java.lang.String)
	 */
	public void logInspectVariablesAction(String type, String selectedVariables) {
		action = createBasicAction(type);
		action.addAttribute("variables", selectedVariables);
		logger.log(action);
	}

	/* (non-Javadoc)
	 * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logLoadAction(java.lang.String)
	 */
	public void logLoadAction(String modelString) {
		action = createBasicAction("load_model");
		action.addAttribute("model", modelString);
		logger.log(action);
	}

	/* (non-Javadoc)
	 * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logActivateWindow(java.lang.String, java.lang.String, java.awt.Component)
	 */
	public void logActivateWindow(String window, String id, Component comp) {
		action = createBasicAction("activate_window");
		action.addAttribute("window", window);
		if (id!=null) action.addAttribute("id", id);
		try {		
			action.addAttribute("x", comp.getLocationOnScreen().x+"");
			action.addAttribute("y", comp.getLocationOnScreen().y+"");
			action.addAttribute("w", comp.getWidth()+"");
			action.addAttribute("h", comp.getHeight()+"");				
		} catch (IllegalComponentStateException ex) {
			action.addAttribute("x", "0");
			action.addAttribute("y", "0");
			action.addAttribute("w", "0");
			action.addAttribute("h", "0");				
		}
		logger.log(action);
	}

}