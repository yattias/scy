package eu.scy.core.model.transfer;

import java.util.AbstractList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 13.feb.2011
 * Time: 22:12:29
 * To change this template use File | Settings | File Templates.
 */
public class AssessmentSetupTransfer extends BaseXMLTransfer{

    private List generalLearningGoals = new LinkedList();
    private List specificLearningGoals = new LinkedList();
    private List reflectionQuestions = new LinkedList();
    private List reflectionTabs = new LinkedList();


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

    public List getReflectionQuestions() {
        return reflectionQuestions;
    }

    public void setReflectionQuestions(List reflectionQuestions) {
        this.reflectionQuestions = reflectionQuestions;
    }

    public void addReflectionQuestion(ReflectionQuestion reflectionQuestion) {
        try {
            getReflectionQuestions().add(reflectionQuestion);
        } catch (Exception e) {
            if(reflectionQuestions instanceof AbstractList) reflectionQuestions = new LinkedList();
            getReflectionQuestions().add(reflectionQuestion);
        }
    }

    public List<ReflectionQuestion> getReflectionQuestionsForAnchorElo(String anchorEloUri) {
        List <ReflectionQuestion> returnList = new LinkedList<ReflectionQuestion>();
        for (int i = 0; i < reflectionQuestions.size(); i++) {
            ReflectionQuestion reflectionQuestion = (ReflectionQuestion) reflectionQuestions.get(i);
            if(reflectionQuestion.getAnchorEloURI() != null && reflectionQuestion.getAnchorEloURI().equals(anchorEloUri)) returnList.add(reflectionQuestion);
        }
        return returnList;
    }

    public List getReflectionTabs() {
        return reflectionTabs;
    }

    public void setReflectionTabs(List reflectionTabs) {
        this.reflectionTabs = reflectionTabs;
    }

    public void addReflectionTab(Tab tab) {
        try {
            getReflectionTabs().add(tab);
        } catch (Exception e) {
            if(reflectionTabs instanceof AbstractList) reflectionTabs = new LinkedList();
            getReflectionTabs().add(tab);
        }
    }
}
