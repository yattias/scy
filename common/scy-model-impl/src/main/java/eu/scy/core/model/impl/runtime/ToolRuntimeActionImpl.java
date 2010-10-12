package eu.scy.core.model.impl.runtime;

import eu.scy.core.model.runtime.ToolRuntimeAction;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.apr.2010
 * Time: 06:39:10
 * To change this template use File | Settings | File Templates.
 */
@Entity
@DiscriminatorValue("toolruntimeaction")
public class ToolRuntimeActionImpl extends AbstractRuntimeActionImpl implements ToolRuntimeAction {

    private String tool;

    @Override
    public String getTool() {
        return tool;
    }

    @Override
    public void setTool(String tool) {
        this.tool = tool;
    }
}
