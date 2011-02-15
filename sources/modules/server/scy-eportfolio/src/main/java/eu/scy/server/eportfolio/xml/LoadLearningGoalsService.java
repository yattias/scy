package eu.scy.server.eportfolio.xml;

import com.thoughtworks.xstream.XStream;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.model.transfer.LearningGoal;
import eu.scy.core.model.transfer.LearningGoals;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.PedagogicalPlanELOService;
import eu.scy.server.controllers.xml.MissionRuntimeEnabledXMLService;
import eu.scy.server.controllers.xml.XMLStreamerController;
import eu.scy.server.url.UrlInspector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.jan.2011
 * Time: 05:33:49
 * To change this template use File | Settings | File Templates.
 */
public class LoadLearningGoalsService extends XMLStreamerController {

    private MissionELOService missionELOService;
    private UrlInspector urlInspector;
    private PedagogicalPlanELOService pedagogicalPlanELOService;

    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        try {
            logger.info("******************************************* Loading learning goals");
            String missionURI = request.getParameter("missionURI");
            URI uri = new URI(missionURI);
            MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(uri, getMissionELOService());
            MissionSpecificationElo missionSpecificationElo = getMissionELOService().getMissionSpecificationELOForRuntume(missionRuntimeElo);

            PedagogicalPlanTransfer pedagogicalPlanTransfer = getPedagogicalPlanELOService().getPedagogicalPlanForMission(missionSpecificationElo);

            LearningGoals learningGoals = new LearningGoals();

            if (missionSpecificationElo != null) {

                for (int i = 0; i < pedagogicalPlanTransfer.getAssessmentSetup().getGeneralLearningGoals().size(); i++) {
                    LearningGoal learningGoal = (LearningGoal) pedagogicalPlanTransfer.getAssessmentSetup().getGeneralLearningGoals().get(i);
                    addGeneralLearninigGoal(learningGoal.getGoal(), learningGoals);
                }
                for (int i = 0; i < pedagogicalPlanTransfer.getAssessmentSetup().getSpecificLearningGoals().size(); i++) {
                    LearningGoal learningGoal = (LearningGoal) pedagogicalPlanTransfer.getAssessmentSetup().getSpecificLearningGoals().get(i);
                    addSpecificLearningGoal(learningGoal.getGoal(), learningGoals);
                }
            } else {
                logger.info("missionSpecification not found!!");
            }
            /*
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

            addSpecificLearningGoal("I can elaborate on factors that influence the size of a population in a fresh water eco system", learningGoals);
            addSpecificLearningGoal("I can provide examples of nature management and changes of natural environments", learningGoals);
            addSpecificLearningGoal("I can outline the main components in a fresh water eco system", learningGoals);
            addSpecificLearningGoal("I can identify the important factors that might influence and cause changes in teh composition or structure of an ecological community", learningGoals);
            addSpecificLearningGoal("I can describe the photosynthesis and outline its main components", learningGoals);
            addSpecificLearningGoal("I can use knowledge about the relation between parameters to construct a (dynamic) model, and vice-versa make inferences about the relation between parameters from the behaviour of a model", learningGoals);
            addSpecificLearningGoal("I can examine an eco system and describe its main abiotic and biotic components", learningGoals);
            addSpecificLearningGoal("I can describe the importance of light in an ecosystem", learningGoals);
            addSpecificLearningGoal("I can plan examinations in cooperation with others where parameters are identified and varied", learningGoals);
            addSpecificLearningGoal("I can specify experiments where the intensity of light is central", learningGoals);
            addSpecificLearningGoal("I can explain and assess wheat might be done to reduce uncertainty and error sources in measurements and result", learningGoals);
            addSpecificLearningGoal("I can examine an eco system and describe how light is of importance for the components in the ecological community", learningGoals);
            */

            return learningGoals;
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        throw new RuntimeException("SOMETHING WENT WRONG WHEN LOADING LEARNING GOALS!");

    }


    private void addSpecificLearningGoal(String s, LearningGoals learningGoals) {
        if (s != null) {
            logger.info("ADDING SPECIFIC LEARNING GOAL: " + s);
            LearningGoal specificLearningGoal = new LearningGoal();
            specificLearningGoal.setGoal(s);
            learningGoals.getSpecificLearningGoals().add(specificLearningGoal);
        }

    }

    private void addGeneralLearninigGoal(String s, LearningGoals learningGoals) {
        if (s != null) {
            logger.info("ADDING GENERAL LEARNING GOAL: " + s);
            LearningGoal learningGoal = new LearningGoal();
            learningGoal.setGoal(s);
            learningGoals.getGeneralLearningGoals().add(learningGoal);
        }

    }


    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public UrlInspector getUrlInspector() {
        return urlInspector;
    }

    public void setUrlInspector(UrlInspector urlInspector) {
        this.urlInspector = urlInspector;
    }

    public PedagogicalPlanELOService getPedagogicalPlanELOService() {
        return pedagogicalPlanELOService;
    }

    public void setPedagogicalPlanELOService(PedagogicalPlanELOService pedagogicalPlanELOService) {
        this.pedagogicalPlanELOService = pedagogicalPlanELOService;
    }
}
