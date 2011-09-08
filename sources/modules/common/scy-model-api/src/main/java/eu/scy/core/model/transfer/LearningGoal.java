package eu.scy.core.model.transfer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.jan.2011
 * Time: 19:51:56
 * To change this template use File | Settings | File Templates.
 */
public class LearningGoal extends BaseXMLTransfer{

    private String goal;
    private Boolean use = Boolean.TRUE;
    private List<LearningGoalCriterium> learningGoalCriterias = new LinkedList<LearningGoalCriterium>();

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public Boolean getUse() {
        return use;
    }

    public void setUse(Boolean use) {
        this.use = use;
    }

    @Override
    public String toString() {
        return this.getGoal();
    }

    public List<LearningGoalCriterium> getLearningGoalCriterias() {
        return learningGoalCriterias;
    }

    public void setLearningGoalCriterias(List<LearningGoalCriterium> learningGoalCriterias) {
        this.learningGoalCriterias = learningGoalCriterias;
    }

    public void addLearningGoalCriterium(LearningGoalCriterium learningGoalCriterium) {
        if(getLearningGoalCriterias() == null) setLearningGoalCriterias(new LinkedList<LearningGoalCriterium>());
        getLearningGoalCriterias().add(learningGoalCriterium);
    }
}
