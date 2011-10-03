package eu.scy.core.model.transfer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
    private List <TeacherQuestionToMission> teacherQuestionsToMission = new LinkedList<TeacherQuestionToMission>();
    private List rubrics = new LinkedList();
    
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
            String reflectionQuestionAnchorEloURI = reflectionQuestion.getAnchorEloURI();
            try {
                String decodedReflectionQuestionURI = URLDecoder.decode(reflectionQuestionAnchorEloURI, "utf-8");
                String decodedAnchorEloURI = URLDecoder.decode(anchorEloUri, "utf-8");

                    if(decodedReflectionQuestionURI.equals(decodedAnchorEloURI)) {
                    returnList.add(reflectionQuestion);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Returning " + returnList.size() + " reflection questions");
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

    public List getRubrics() {
        if(rubrics == null) rubrics = new LinkedList();
        return rubrics;
    }

    public void setRubrics(List rubrics) {
        this.rubrics = rubrics;
    }

    public void addRubric(Rubric rubric) {
        getRubrics().add(rubric);
    }

    public List <RubricForElo> getRubricsForElo(String uri) {
        List returnList = new LinkedList<RubricForElo>();
        for (int i = 0; i < getRubrics().size(); i++) {
            Rubric rubric = (Rubric) rubrics.get(i);
            if(rubric instanceof RubricForElo) {
                RubricForElo rubricForElo = (RubricForElo) rubric;
                if(rubricForElo.getAnchorElo().equals(uri)) returnList.add(rubricForElo);

            }
        }

        return returnList;
    }

    public List getRubricForMission() {
        List <RubricForMission> returnList = new LinkedList<RubricForMission>();
        for (int i = 0; i < getRubrics().size(); i++) {
            Object o = getRubrics().get(i);
            if(o instanceof RubricForMission) returnList.add((RubricForMission) o);
        }

        return returnList;
    }

    public Rubric getRubric(String rubricId) {
        for (int i = 0; i < getRubrics().size(); i++) {
            Rubric rubric = (Rubric) getRubrics().get(i);
            if(rubric.getId().equals(rubricId)) return rubric;
        }

        return null;
    }

    public RubricCategory getRubricCategory(String categoryId) {
        for (int i = 0; i < getRubrics().size(); i++) {
            Rubric rubric = (Rubric) getRubrics().get(i);
            for (int j = 0; j < rubric.getRubricCategories().size(); j++) {
                RubricCategory rubricCategory = rubric.getRubricCategories().get(j);
                if(rubricCategory.getId().equals(categoryId)) return rubricCategory;
            }
        }
        return null;
    }

    public List<TeacherQuestionToMission> getTeacherQuestionsToMission() {
        if(teacherQuestionsToMission == null) teacherQuestionsToMission = new LinkedList<TeacherQuestionToMission>();
        return teacherQuestionsToMission;
    }

    public void setTeacherQuestionsToMission(List<TeacherQuestionToMission> teacherQuestionsToMission) {
        this.teacherQuestionsToMission = teacherQuestionsToMission;
    }

    public void addTeacherQuestiontoMission(TeacherQuestionToMission teacherQuestionToMission) {
        getTeacherQuestionsToMission().add(teacherQuestionToMission);
    }

    public void removeLearningGoal(String learningGoalId) {
        LearningGoal learningGoalToBeRemoved = null;
        for (int i = 0; i < generalLearningGoals.size(); i++) {
            LearningGoal o = (LearningGoal) generalLearningGoals.get(i);
            if(o.getId().equals(learningGoalId)) learningGoalToBeRemoved = o;
        }
        if(learningGoalToBeRemoved != null) {
            generalLearningGoals.remove(learningGoalToBeRemoved);
            return;
        }

        for (int i = 0; i < specificLearningGoals.size(); i++) {
            LearningGoal learningGoal = (LearningGoal) specificLearningGoals.get(i);
            if(learningGoal.getId().equals(learningGoalId)) learningGoalToBeRemoved = learningGoal;
        }
        if(learningGoalToBeRemoved != null) {
            specificLearningGoals.remove(learningGoalToBeRemoved);
        }
    }

    public void removeCriteria(String criteriaId) {
        LearningGoalCriterium criteriaToBeRemoved = null;
        for (int i = 0; i < generalLearningGoals.size(); i++) {
            LearningGoal o = (LearningGoal) generalLearningGoals.get(i);
            for (int j = 0; j < o.getLearningGoalCriterias().size(); j++) {
                LearningGoalCriterium learningGoalCriterium = o.getLearningGoalCriterias().get(j);
                if(learningGoalCriterium.getId().equals(criteriaId)) criteriaToBeRemoved = learningGoalCriterium;
            }
            if(criteriaToBeRemoved != null) {
                o.getLearningGoalCriterias().remove(criteriaToBeRemoved);
                return;
            }
        }
        for (int i = 0; i < specificLearningGoals.size(); i++) {
            LearningGoal o = (LearningGoal) specificLearningGoals.get(i);
            for (int j = 0; j < o.getLearningGoalCriterias().size(); j++) {
                LearningGoalCriterium learningGoalCriterium = o.getLearningGoalCriterias().get(j);
                if(learningGoalCriterium.getId().equals(criteriaId)) criteriaToBeRemoved = learningGoalCriterium;
            }
            if(criteriaToBeRemoved != null) {
                o.getLearningGoalCriterias().remove(criteriaToBeRemoved);
                return;
            }
        }

    }
}
