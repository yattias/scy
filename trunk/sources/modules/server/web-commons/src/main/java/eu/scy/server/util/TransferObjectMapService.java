package eu.scy.server.util;

import eu.scy.core.model.transfer.*;


import java.util.*;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.feb.2011
 * Time: 06:07:23
 * To change this template use File | Settings | File Templates.
 */
public class TransferObjectMapService {

    private static Logger log = Logger.getLogger("TransferObjectMapService.class");

    public Object getObjectWithId(BaseXMLTransfer transfer, String id) {
        if (transfer == null) log.info("TRANSFER IS NULLL!");
        Map map = createMap(transfer);

        log.info("Getting " + id + " from base transfer: " + transfer.getClass().getName());
        Object returnObject = map.get(id);
        if (returnObject != null) {
            log.info("Found " + returnObject);
            return returnObject;
        }

        log.info("DID NOT FIND ANY RETURN OBJECTS IN MAP. Following values are available:");
        Set keys = map.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            Object key = it.next();
            Object value = map.get(key);
            log.info("KEY: " + key + " value: " + value);
        }

        throw new RuntimeException("Did not find correct object (with id: " + id + ") in map!");
    }

    public Map createMap(BaseXMLTransfer transfer) {
        if (transfer instanceof PedagogicalPlanTransfer) {
            return getPedagogicalPlanMap((PedagogicalPlanTransfer) transfer);
        }
        return null;
    }


    private Map getPedagogicalPlanMap(PedagogicalPlanTransfer pedagogicalPlanTransfer) {
        Map returnMap = new HashMap();

        returnMap.put(pedagogicalPlanTransfer.getId(), pedagogicalPlanTransfer);
        AssessmentSetupTransfer assessmentSetupTransfer = pedagogicalPlanTransfer.getAssessmentSetup();
        MissionPlanTransfer missionPlanTransfer = pedagogicalPlanTransfer.getMissionPlan();
        TechnicalInfo technicalInfo = pedagogicalPlanTransfer.getTechnicalInfo();

        if (assessmentSetupTransfer != null) register(assessmentSetupTransfer, returnMap);
        if (missionPlanTransfer != null) register(missionPlanTransfer, returnMap);
        if (technicalInfo != null) register(technicalInfo, returnMap);


        return returnMap;

    }

    private void register(TechnicalInfo technicalInfo, Map map) {
        map.put(technicalInfo.getId(), technicalInfo);
        for (int i = 0; i < technicalInfo.getJnlpProperties().size(); i++) {
            PropertyTransfer propertyTransfer = (PropertyTransfer) technicalInfo.getJnlpProperties().get(i);
            register(propertyTransfer, map);
        }
    }

    private void register(PropertyTransfer propertyTransfer, Map map) {
        map.put(propertyTransfer.getId(), propertyTransfer);
    }

    private void register(MissionPlanTransfer missionPlanTransfer, Map map) {
        map.put(missionPlanTransfer.getId(), missionPlanTransfer);
        for (int i = 0; i < missionPlanTransfer.getLasTransfers().size(); i++) {
            LasTransfer lasTransfer = missionPlanTransfer.getLasTransfers().get(i);
            register(lasTransfer, map);
        }
    }

    private void register(LasTransfer lasTransfer, Map map) {
        map.put(lasTransfer.getId(), lasTransfer);
        map.put(lasTransfer.getAnchorElo().getId(), lasTransfer.getAnchorElo());
    }

    private void register(AssessmentSetupTransfer assessmentSetupTransfer, Map map) {
        map.put(assessmentSetupTransfer.getId(), assessmentSetupTransfer);

        for (int i = 0; i < assessmentSetupTransfer.getGeneralLearningGoals().size(); i++) {
            LearningGoal learningObject = (LearningGoal) assessmentSetupTransfer.getGeneralLearningGoals().get(i);
            register(learningObject, map);
            if (learningObject.getLearningGoalCriterias() != null) {
                for (int j = 0; j < learningObject.getLearningGoalCriterias().size(); j++) {
                    LearningGoalCriterium criterium = learningObject.getLearningGoalCriterias().get(j);
                    register(criterium, map);

                }
            }

        }

        for (int i = 0; i < assessmentSetupTransfer.getSpecificLearningGoals().size(); i++) {
            LearningGoal learningGoal = (LearningGoal) assessmentSetupTransfer.getSpecificLearningGoals().get(i);
            register(learningGoal, map);
             if (learningGoal.getLearningGoalCriterias() != null) {
                for (int j = 0; j < learningGoal.getLearningGoalCriterias().size(); j++) {
                    LearningGoalCriterium criterium = learningGoal.getLearningGoalCriterias().get(j);
                    register(criterium, map);

                }
            }
        }

        for (int i = 0; i < assessmentSetupTransfer.getReflectionQuestions().size(); i++) {
            ReflectionQuestion reflectionQuestion = (ReflectionQuestion) assessmentSetupTransfer.getReflectionQuestions().get(i);
            registerReflectionQuestion(reflectionQuestion, map);
        }

        for (int i = 0; i < assessmentSetupTransfer.getReflectionTabs().size(); i++) {
            Tab tab = (Tab) assessmentSetupTransfer.getReflectionTabs().get(i);
            registerReflectionTab(tab, map);
        }

        for (int i = 0; i < assessmentSetupTransfer.getTeacherQuestionToElos().size(); i++) {
            TeacherQuestionToElo teacherQuestionToElo = assessmentSetupTransfer.getTeacherQuestionToElos().get(i);
            registerTeacherQuestion(map, teacherQuestionToElo);
        }

        for (int i = 0; i < assessmentSetupTransfer.getRubrics().size(); i++) {
            Rubric rubric = (Rubric) assessmentSetupTransfer.getRubrics().get(i);
            registerRubric(map, rubric);
        }
    }

    private void registerRubric(Map map, Rubric rubric) {
        map.put(rubric.getId(), rubric);
        List<RubricCategory> rubricCategoryList = rubric.getRubricCategories();
        for (int i = 0; i < rubricCategoryList.size(); i++) {
            RubricCategory rubricCategory = rubricCategoryList.get(i);
            map.put(rubricCategory.getId(), rubricCategory);
            for (int j = 0; j < rubricCategory.getRubricAssessmentCriterias().size(); j++) {
                RubricAssessmentCriteria rubricAssessmentCriteria = rubricCategory.getRubricAssessmentCriterias().get(j);
                map.put(rubricAssessmentCriteria.getId(), rubricAssessmentCriteria);
            }
        }
    }

    private void registerTeacherQuestion(Map map, TeacherQuestionToElo teacherQuestionToElo) {
        map.put(teacherQuestionToElo.getId(), teacherQuestionToElo);

    }

    private void registerReflectionTab(Tab tab, Map map) {
        map.put(tab.getId(), tab);
    }

    private void register(LearningGoal learningGoal, Map map) {
        map.put(learningGoal.getId(), learningGoal);
    }

    private void registerReflectionQuestion(ReflectionQuestion reflectionQuestion, Map map) {
        map.put(reflectionQuestion.getId(), reflectionQuestion);
    }

    private void register(LearningGoalCriterium learningGoalCriterium, Map map) {
        map.put(learningGoalCriterium.getId(), learningGoalCriterium);
    }


}
