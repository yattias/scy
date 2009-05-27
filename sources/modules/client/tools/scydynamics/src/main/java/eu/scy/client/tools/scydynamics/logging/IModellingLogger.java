package eu.scy.client.tools.scydynamics.logging;

import java.awt.Component;

import colab.um.draw.JdFigure;

public interface IModellingLogger {

	public abstract void close();

	public abstract void logAddAction(JdFigure object, String modelString);

	public abstract void logDeleteAction(JdFigure object, String modelString);

	public abstract void logRenameAction(String id, String oldName,
			String newName);

	public abstract void logChangeSpecification(String id, String expression,
			String unit);

	public abstract void logSimpleAction(String type);

	public abstract void logInspectVariablesAction(String type,
			String selectedVariables);

	public abstract void logLoadAction(String modelString);

	public abstract void logActivateWindow(String window, String id,
			Component comp);

}