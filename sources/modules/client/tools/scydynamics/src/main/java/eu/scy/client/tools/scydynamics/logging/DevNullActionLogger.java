package eu.scy.client.tools.scydynamics.logging;

import java.awt.Component;

import colab.um.draw.JdFigure;

public class DevNullActionLogger implements IModellingLogger {

	@Override
	public void close() {}

	@Override
	public void logActivateWindow(String window, String id, Component comp) {}

	@Override
	public void logAddAction(JdFigure object, String modelString) {}

	@Override
	public void logChangeSpecification(String id, String expression, String unit) {}

	@Override
	public void logDeleteAction(JdFigure object, String modelString) {}

	@Override
	public void logInspectVariablesAction(String type, String selectedVariables) {}

	@Override
	public void logLoadAction(String modelString) {}

	@Override
	public void logRenameAction(String id, String oldName, String newName) {}

	@Override
	public void logSimpleAction(String type) {}

}
