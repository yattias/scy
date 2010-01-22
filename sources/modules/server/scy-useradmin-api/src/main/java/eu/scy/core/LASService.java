package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpaceToolConfiguration;
import eu.scy.core.model.pedagogicalplan.Tool;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.jan.2010
 * Time: 11:28:23
 * To change this template use File | Settings | File Templates.
 */
public interface LASService extends BaseService{

    public void addToolToLAS(Tool tool, LearningActivitySpace las);


    /**
     * adds the tool directly to the activity and also the containing LAS
     * @param tool
     */
    public void addToolToActivity(Tool tool, Activity activity);

    public List<LearningActivitySpaceToolConfiguration> getToolConfigurations(LearningActivitySpace learningActivitySpace );

}
