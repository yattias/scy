package eu.scy.client.tools.scydynamics.logging;

import java.awt.Component;

import colab.um.draw.JdFigure;
//import eu.scy.actionlogging.logger.Action;

public class DevNullActionLogger implements IModellingLogger {
	
//	private Action action;

	@Override
	public void close() {}

	@Override
	public void logActivateWindow(String window, String id, Component comp) {}

	@Override
	public void logAddAction(JdFigure object, String modelString) {
//		action = new Action("add_object", "lars");
//		action.addContext("tool", "SCYDynamics");
//		action.addContext("mission", "SCY mission 1");
//		action.addAttribute("model", "", modelString);
//		System.out.println(action.getXMLString());
	}

	@Override
	public void logChangeSpecification(String id, String expression, String unit) {
//		action = new Action("change_specification", "lars");
//		action.addContext("tool", "SCYDynamics");
//		action.addContext("mission", "SCY mission 1");
//		action.addAttribute("id", id);
//		action.addAttribute("expression", expression);
//		action.addAttribute("unit", unit);
//		System.out.println(action.getXMLString());
	}

	@Override
	public void logDeleteAction(JdFigure object, String modelString) {}

	@Override
	public void logInspectVariablesAction(String type, String selectedVariables) {}

	@Override
	public void logLoadAction(String modelString) {}

	@Override
	public void logRenameAction(String id, String oldName, String newName) {}

	@Override
	public void logSimpleAction(String type) {
//		action = new Action(type, "lars");
//		action.addContext("tool", "SCYDynamics");
//		action.addContext("mission", "SCY mission 1");
//		System.out.println(action.getXMLString());
	}

}
