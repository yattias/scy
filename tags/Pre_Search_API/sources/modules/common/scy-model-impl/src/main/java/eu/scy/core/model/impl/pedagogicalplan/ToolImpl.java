package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.Tool;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.nov.2009
 * Time: 09:33:49
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "tool")
public class ToolImpl extends BaseObjectImpl  implements Tool {

    private String toolId;

    @Override
    public String getToolId() {
        return toolId;
    }

    @Override
    public void setToolId(String toolId) {
        this.toolId = toolId;
    }
}
