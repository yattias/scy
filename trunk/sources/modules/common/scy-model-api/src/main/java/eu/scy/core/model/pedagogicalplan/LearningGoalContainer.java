package eu.scy.core.model.pedagogicalplan;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 12:33:15
 * To change this template use File | Settings | File Templates.
 */
public interface LearningGoalContainer extends BaseObject {

    public List<LearningGoal> getLearningGoals();
    public void setLearningGoals(List <LearningGoal> learningGoals);

    public void addLearningGoal(LearningGoal learningGoal);
    public void removeLearningGoal(LearningGoal learningGoal);


}
