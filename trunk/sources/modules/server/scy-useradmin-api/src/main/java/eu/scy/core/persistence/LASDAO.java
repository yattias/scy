package eu.scy.core.persistence;

import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpaceToolConfiguration;
import eu.scy.core.model.pedagogicalplan.Tool;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.jan.2010
 * Time: 11:29:14
 * To change this template use File | Settings | File Templates.
 */
public interface LASDAO extends SCYBaseDAO{

    public void addToolToLAS(Tool tool, LearningActivitySpace las);


    void addToolToActivity(Tool tool, Activity activity);

    List<LearningActivitySpaceToolConfiguration> getToolConfigurations(LearningActivitySpace learningActivitySpace);
}
