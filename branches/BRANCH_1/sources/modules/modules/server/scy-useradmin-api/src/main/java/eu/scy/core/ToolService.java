package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.Tool;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.jan.2010
 * Time: 11:07:39
 * To change this template use File | Settings | File Templates.
 */
public interface ToolService extends BaseService{

    public Tool findToolByName(String name);

}
