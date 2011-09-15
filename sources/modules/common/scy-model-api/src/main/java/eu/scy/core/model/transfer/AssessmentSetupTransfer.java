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
    private List <TeacherQuestionToElo> teacherQuestionToElos = new LinkedList<TeacherQuestionToElo>();
    
    private Boolean useOnlyLearningGoals = true;
    private Boolean useScorableLearningGoals = false;
    private Boolean useLearningGoalsWithCriteria = false;



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

    public Boolean getUseOnlyLearningGoals() {
        return useOnlyLearningGoals;
    }

    public void setUseOnlyLearningGoals(Boolean useOnlyLearningGoals) {
        this.useOnlyLearningGoals = useOnlyLearningGoals;
        if(useOnlyLearningGoals) {
            this.useScorableLearningGoals = false;
            this.useLearningGoalsWithCriteria = false;
        }
    }

    public Boolean getUseScorableLearningGoals() {
        return useScorableLearningGoals;
    }

    public void setUseScorableLearningGoals(Boolean useScorableLearningGoals) {
        this.useScorableLearningGoals = useScorableLearningGoals;
        if(useScorableLearningGoals) {
            this.useOnlyLearningGoals = false;
            this.useLearningGoalsWithCriteria = false;
        }
    }

    public Boolean getUseLearningGoalsWithCriteria() {
        return useLearningGoalsWithCriteria;
    }

    public void setUseLearningGoalsWithCriteria(Boolean useLearningGoalsWithCriteria) {
        this.useLearningGoalsWithCriteria = useLearningGoalsWithCriteria;
        if(useLearningGoalsWithCriteria) {
            this.useOnlyLearningGoals = false;
            this.useScorableLearningGoals = false;
        }
    }

    public void addTeacherQuestionToElo(TeacherQuestionToElo teacherQuestionToElo) {
        getTeacherQuestionToElos().add(teacherQuestionToElo);
    }

    public List<TeacherQuestionToElo> getTeacherQuestionToElos() {
        if(teacherQuestionToElos == null) teacherQuestionToElos = new LinkedList<TeacherQuestionToElo>();
        return teacherQuestionToElos;
    }

    public void setTeacherQuestionToElos(List<TeacherQuestionToElo> teacherQuestionToElos) {
        this.teacherQuestionToElos = teacherQuestionToElos;
    }

    public List<TeacherQuestionToElo> getTeacherQuestionToElo(String uri) {
        List <TeacherQuestionToElo> returnList = new LinkedList<TeacherQuestionToElo>();
        for (int i = 0; i < getTeacherQuestionToElos().size(); i++) {
            TeacherQuestionToElo teacherQuestionToElo = getTeacherQuestionToElos().get(i);
            if(teacherQuestionToElo.getEloURI().equals(uri)) returnList.add(teacherQuestionToElo);
        }

        return returnList;
    }

}
