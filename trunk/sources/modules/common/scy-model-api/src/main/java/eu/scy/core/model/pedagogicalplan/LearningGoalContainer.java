package eu.scy.core.model.pedagogicalplan;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 12:33:15
 * To change this template use File | Settings | File Templates.
 */
public interface LearningGoalContainer extends BaseObject {

    public Set<LearningGoal> getLearningGoals();
    public void setLearningGoals(Set <LearningGoal> learningGoals);


}
