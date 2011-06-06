package eu.scy.client.tools.scydynamics.logging;

import java.awt.Component;

import colab.um.draw.JdFigure;

public interface IModellingLogger {

    public abstract void close();

    public abstract void setEloUri(String newUri);

    public abstract void logAddAction(JdFigure object, String modelString);

    public abstract void logDeleteAction(JdFigure object, String modelString);

    public abstract void logRenameAction(String id, String oldName, String newName);

    public abstract void logChangeSpecification(String id, String name, String expression, String unit, String modelString);

    public abstract void logSimpleAction(String type);

    public abstract void logSimpleAction(String type, String modelString);

    public abstract void logInspectVariablesAction(String type, String selectedVariables);

    //public abstract void logLoadAction(String modelString);
    
    public abstract void logActivateWindow(String window, String id, Component comp);

    public void logModelRan(String modelString, String injectedValues);
}
