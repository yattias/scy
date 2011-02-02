package eu.scy.server.eportfolio.xml;

import com.thoughtworks.xstream.XStream;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.server.controllers.xml.transfer.LearningGoal;
import eu.scy.server.controllers.xml.transfer.LearningGoals;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.jan.2011
 * Time: 05:33:49
 * To change this template use File | Settings | File Templates.
 */
public class LoadLearningGoalsService extends MissionRuntimeEnabledXMLService {


    @Override
    protected Object getObject(MissionRuntimeElo missionRuntimeElo, HttpServletRequest request, HttpServletResponse response) {
        LearningGoals learningGoals = new LearningGoals();

        addGeneralLearninigGoal("To do that funky rumba so baad you will never forget it!", learningGoals);
        addGeneralLearninigGoal("To make sure no uncertainty exists", learningGoals);
        addGeneralLearninigGoal("Specify the generics correctly in short words", learningGoals);

        addSpecificLearningGoal("KickAss!" ,learningGoals);
        addSpecificLearningGoal("Understand the nature of the curious cat" ,learningGoals);


        return learningGoals;
    }

    @Override
    protected void addAliases(XStream xStream) {
        super.addAliases(xStream);    
        xStream.alias("learninggoals", LearningGoals.class);
        xStream.alias("goal", LearningGoal.class);
    }


    private void addSpecificLearningGoal(String s, LearningGoals learningGoals) {
        LearningGoal specificLearningGoal = new LearningGoal();
        specificLearningGoal.setGoal(s);
        learningGoals.getSpecificLearningGoals().add(specificLearningGoal);
    }

    private void addGeneralLearninigGoal(String s, LearningGoals learningGoals) {
        LearningGoal learningGoal = new LearningGoal();
        learningGoal.setGoal(s);
        learningGoals.getGeneralLearningGoals().add(learningGoal);
    }
}
