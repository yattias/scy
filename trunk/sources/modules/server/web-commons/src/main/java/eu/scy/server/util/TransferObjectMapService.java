package eu.scy.server.util;

import eu.scy.core.model.transfer.*;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
        Map map = createMap(transfer);

        log.info("Getting " + id + " from base transfer: " + transfer.getClass().getName());
        Object returnObject = map.get(id);
        if(returnObject != null) {
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
        if(transfer instanceof PedagogicalPlanTransfer) {
            return getPedagogicalPlanMap((PedagogicalPlanTransfer)transfer);
        }
        return null;
    }

    private Map getPedagogicalPlanMap(PedagogicalPlanTransfer pedagogicalPlanTransfer) {
        Map returnMap = new HashMap();

        returnMap.put(pedagogicalPlanTransfer.getId(), pedagogicalPlanTransfer);
        AssessmentSetupTransfer assessmentSetupTransfer = pedagogicalPlanTransfer.getAssessmentSetup();
        if(assessmentSetupTransfer != null) register(assessmentSetupTransfer, returnMap);



        return returnMap;

    }

    private void register(AssessmentSetupTransfer assessmentSetupTransfer, Map map) {
        map.put(assessmentSetupTransfer.getId(), assessmentSetupTransfer);

        for (int i = 0; i < assessmentSetupTransfer.getGeneralLearningGoals().size(); i++) {
            LearningGoal learningObject = (LearningGoal) assessmentSetupTransfer.getGeneralLearningGoals().get(i);
            register(learningObject, map);
        }

        for (int i = 0; i < assessmentSetupTransfer.getSpecificLearningGoals().size(); i++) {
            LearningGoal learningGoal = (LearningGoal) assessmentSetupTransfer.getSpecificLearningGoals().get(i);
            register(learningGoal, map);
        }

        for (int i = 0; i < assessmentSetupTransfer.getReflectionQuestions().size(); i++) {
            ReflectionQuestion reflectionQuestion = (ReflectionQuestion) assessmentSetupTransfer.getReflectionQuestions().get(i);
            registerReflectionQuestion(reflectionQuestion, map);
            }

        for (int i = 0; i < assessmentSetupTransfer.getReflectionTabs().size(); i++) {
            Tab tab = (Tab) assessmentSetupTransfer.getReflectionTabs().get(i);
            registerReflectionTab(tab, map);
        }
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



}
