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
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.logger.Action;

public class FileActionLogger implements IModellingLogger {

	private ActionLogFileHandler logfilehandler;
	private String filename;
	private IAction action;
	private String user;
	private String mission = "mission1";
	private String toolname = "scydynamics";

	public FileActionLogger(String user) {
		this.user = user;
		logfilehandler = new ActionLogFileHandler(getFileName());
		System.out.println("ActionLogger. creating logfile at "+getFileName());
	}
	
	private String getFileName() {
		if (this.filename == null) {
			filename = new String(System.getProperties().get("user.home").toString());
			filename = filename.concat(System.getProperties().get("file.separator").toString());
			filename = filename.concat("model-log-"+System.currentTimeMillis()+".xml");
		}
		return filename;
	}
	
	/* (non-Javadoc)
	 * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#close()
	 */
	public void close() {
		logfilehandler.close();
	}
	
	/* (non-Javadoc)
	 * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logAddAction(colab.um.draw.JdFigure, java.lang.String)
	 */
	public void logAddAction(JdFigure object, String modelString) {
		switch (object.getType()) {
		case JdFigure.AUX:
			action = createBasicAction("add_auxiliary");
			action.addAttribute("id", object.getID());
			action.addAttribute("name", ((JdAux)object).getLabel());
			break;
		case JdFigure.CONSTANT:
			action = createBasicAction("add_constant");			
			action.addAttribute("id", object.getID());
			action.addAttribute("name", ((JdConst)object).getLabel());
			break;
		case JdFigure.STOCK:
			action = createBasicAction("add_stock");
			action.addAttribute("id", object.getID());
			action.addAttribute("name", ((JdStock)object).getLabel());
			break;
		case JdFigure.RELATION:
			action = createBasicAction("add_relation");
			action.addAttribute("id", object.getID());
			action.addAttribute("from", ((JdRelation)object).getFigure1().getID());
			// if the "to" object of a relation is the center of a flow,
			// list the target of the parent (flow) here
			if (((JdRelation) object).getFigure2().getType()==JdFigure.FLOWCTR) {
				action.addAttribute("to", ((JdFlowCtr)((JdRelation)object).getFigure2()).getParent().getID());
			} else {
				action.addAttribute("to", ((JdRelation)object).getFigure2().getID());
			}
			break;
		case JdFigure.FLOW:
			action = createBasicAction("add_flow");
			action.addAttribute("id", object.getID());
			// the ends of a flow may be null
			if (((JdFlow) object).getFigure1()!=null) {
				action.addAttribute("from", ((JdFlow)object).getFigure1().getID());
			}
			if (((JdFlow) object).getFigure2()!=null) {
				action.addAttribute("to", ((JdFlow)object).getFigure2().getID());
			}
			break;
		}
		action.addAttribute("model", "", modelString);
		logfilehandler.writeAction(action.getXML());
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
			break;
		case JdFigure.FLOW:
			action = createBasicAction("delete_flow");
			action.addAttribute("id", object.getID());
			break;
		}
		action.addAttribute("model", "", modelString);
		logfilehandler.writeAction(action.getXML());
	}
	
	public IAction createBasicAction(String type) {
		IAction action = new Action(type, user);
		action.addContext("tool", toolname);
		action.addContext("mission", mission);
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
		logfilehandler.writeAction(action.getXML());
	}
	
	/* (non-Javadoc)
	 * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logChangeSpecification(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void logChangeSpecification(String id, String expression, String unit) {
		action = createBasicAction("change_specification");
		action.addAttribute("id", id);
		action.addAttribute("expression", expression);
		action.addAttribute("unit", unit);
		logfilehandler.writeAction(action.getXML());
	}

	/* (non-Javadoc)
	 * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logSimpleAction(java.lang.String)
	 */
	public void logSimpleAction(String type) {
		logfilehandler.writeAction(createBasicAction(type).getXML());
	}

	/* (non-Javadoc)
	 * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logInspectVariablesAction(java.lang.String, java.lang.String)
	 */
	public void logInspectVariablesAction(String type, String selectedVariables) {
		action = createBasicAction(type);
		action.addAttribute("variables", selectedVariables);
		logfilehandler.writeAction(action.getXML());
	}

	/* (non-Javadoc)
	 * @see eu.scy.client.tools.scydynamics.logging.IModellingLogger#logLoadAction(java.lang.String)
	 */
	public void logLoadAction(String modelString) {
		action = createBasicAction("load_model");
		action.addAttribute("model", "", modelString);
		logfilehandler.writeAction(action.getXML());	
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
		logfilehandler.writeAction(action.getXML());
	}
	
}