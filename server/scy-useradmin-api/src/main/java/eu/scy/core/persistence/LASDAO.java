package eu.scy.core.persistence;

import eu.scy.core.model.pedagogicalplan.*;

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

    List<AnchorELO> getAnchorELOsProducedByLAS(LearningActivitySpace learningActivitySpace);

    List<LearningActivitySpace> getAllLearningActivitySpacesForScenario(Scenario scenario);

    LearningActivitySpace getLearningActivitySpaceByName(String lasName, Scenario participatesIn);
}
