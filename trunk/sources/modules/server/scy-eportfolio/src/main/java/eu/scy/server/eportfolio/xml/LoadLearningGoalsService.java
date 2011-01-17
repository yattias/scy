package eu.scy.server.eportfolio.xml;

import com.thoughtworks.xstream.XStream;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.server.controllers.xml.XMLStreamerController;
import eu.scy.server.controllers.xml.transfer.Portfolio;
import eu.scy.server.controllers.xml.transfer.PortfolioContainer;
import eu.scy.server.controllers.xml.transfer.TransferElo;
import eu.scy.server.eportfolio.xml.utilclasses.LearningGoal;
import eu.scy.server.eportfolio.xml.utilclasses.LearningGoals;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;

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

        addGeneralLearninigGoal("A goal", learningGoals);
        addGeneralLearninigGoal("Yet a goal", learningGoals);

        addSpecificLearningGoal("Speci" ,learningGoals);
        addSpecificLearningGoal("Spe ble bla" ,learningGoals);


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
