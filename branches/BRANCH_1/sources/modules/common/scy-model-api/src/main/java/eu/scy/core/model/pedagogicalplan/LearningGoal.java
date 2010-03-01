package eu.scy.core.model.pedagogicalplan;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 11:22:57
 */

public interface LearningGoal extends BaseObject {

    Mission getLearningGoalContainer();

    void setLearningGoalContainer(Mission learningGoalContainer);
}
