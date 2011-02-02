package eu.scy.core.model.pedagogicalplan;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 11:18:20
 * To change this template use File | Settings | File Templates.
 */
public interface Scenario extends LearningGoalContainer{

    public LearningActivitySpace getLearningActivitySpace();
    public void setLearningActivitySpace(LearningActivitySpace learningActivitySpace);
}
