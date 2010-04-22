package eu.scy.core.model.runtime;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.apr.2010
 * Time: 06:38:48
 * To change this template use File | Settings | File Templates.
 */
public interface ToolRuntimeAction extends AbstractRuntimeAction{
    String getTool();

    void setTool(String tool);
}
