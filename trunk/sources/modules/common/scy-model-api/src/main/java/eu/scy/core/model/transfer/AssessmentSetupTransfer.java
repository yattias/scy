package eu.scy.core.model.transfer;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 13.feb.2011
 * Time: 22:12:29
 * To change this template use File | Settings | File Templates.
 */
public class AssessmentSetupTransfer extends BaseXMLTransfer{

    private List generalLearningGoals;
    private List specificLearningGoals;

    public List getGeneralLearningGoals() {
        return generalLearningGoals;
    }

    public void setGeneralLearningGoals(List generalLearningGoals) {
        this.generalLearningGoals = generalLearningGoals;
    }

    public void addGeneralLearningGoal(LearningGoal learningGoal) {
        getGeneralLearningGoals().add(learningGoal);

    }

    public List getSpecificLearningGoals() {
        return specificLearningGoals;
    }

    public void setSpecificLearningGoals(List specificLearningGoals) {
        this.specificLearningGoals = specificLearningGoals;
    }

    public void addSpecificLearningGoal(LearningGoal specificLearningGoal) {
        getSpecificLearningGoals().add(specificLearningGoal);
    }
}
