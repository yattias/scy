package eu.scy.server.eportfolio.xml;

import com.thoughtworks.xstream.XStream;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.core.model.transfer.LearningGoal;
import eu.scy.core.model.transfer.LearningGoals;
import eu.scy.server.controllers.xml.MissionRuntimeEnabledXMLService;

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

        addGeneralLearninigGoal("I can write a correct hypothesis", learningGoals);
        addGeneralLearninigGoal("I can design, plan and perform experiments to test a hypothesis", learningGoals);
        addGeneralLearninigGoal("I can organize, visualize and interpret data", learningGoals);
        addGeneralLearninigGoal("I can construct a dynamic model", learningGoals);
        addGeneralLearninigGoal("I can collaborate with peers", learningGoals);
        addGeneralLearninigGoal("I can plan and execute my own learning process", learningGoals);
        addGeneralLearninigGoal("I can reflect on my current knowledge and learning goals", learningGoals);
        addGeneralLearninigGoal("I can hold a presentation for a large group", learningGoals);
        addGeneralLearninigGoal("I can write an individual report", learningGoals);
        addGeneralLearninigGoal("I can collaborate to write a report", learningGoals);
        addGeneralLearninigGoal("I can develop research questions", learningGoals);
        addGeneralLearninigGoal("I can justify conclusions", learningGoals);
        addGeneralLearninigGoal("I can identify a problem", learningGoals);
        addGeneralLearninigGoal("I can gather and analyse information", learningGoals);
        addGeneralLearninigGoal("I can make decisions", learningGoals);
        addGeneralLearninigGoal("I can generate solutions to a problem", learningGoals);
        addGeneralLearninigGoal("I can manage my work effectively", learningGoals);
        addGeneralLearninigGoal("I can document and discuss research results", learningGoals);

        addSpecificLearningGoal("I can elaborate on factors that influence the size of a population in a fresh water eco system" ,learningGoals);
        addSpecificLearningGoal("I can provide examples of nature management and changes of natural environments" ,learningGoals);
        addSpecificLearningGoal("I can outline the main components in a fresh water eco system" ,learningGoals);
        addSpecificLearningGoal("I can identify the important factors that might influence and cause changes in teh composition or structure of an ecological community" ,learningGoals);
        addSpecificLearningGoal("I can describe the photosynthesis and outline its main components" ,learningGoals);
        addSpecificLearningGoal("I can use knowledge about the relation between parameters to construct a (dynamic) model, and vice-versa make inferences about the relation between parameters from the behaviour of a model" ,learningGoals);
        addSpecificLearningGoal("I can examine an eco system and describe its main abiotic and biotic components" ,learningGoals);
        addSpecificLearningGoal("I can describe the importance of light in an ecosystem" ,learningGoals);
        addSpecificLearningGoal("I can plan examinations in cooperation with others where parameters are identified and varied" ,learningGoals);
        addSpecificLearningGoal("I can specify experiments where the intensity of light is central" ,learningGoals);
        addSpecificLearningGoal("I can explain and assess wheat might be done to reduce uncertainty and error sources in measurements and result" ,learningGoals);
        addSpecificLearningGoal("I can examine an eco system and describe how light is of importance for the components in the ecological community" ,learningGoals);


        return learningGoals;
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
