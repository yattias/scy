package eu.scy.scymapper.impl.configuration;

import eu.scy.core.model.pedagogicalplan.Tool;

/**
 * Created by IntelliJ IDEA.
 * User: Bjoerge
 * Date: 30.okt.2009
 * Time: 11:31:06
 * To change this template use File | Settings | File Templates.
 */
public class SCYMapperTool implements Tool {
    @Override
    public String getName() {
        return "SCYMapper";
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getDescription() {
        return "SCYMapper is a concept mapping tool";
    }

    @Override
    public void setDescription(String description) {
        
    }
}
