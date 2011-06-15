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
                    if(learningGoal.getUse() != null && learningGoal.getUse()) addGeneralLearninigGoal(learningGoal.getGoal(), learningGoals);
                }
                for (int i = 0; i < pedagogicalPlanTransfer.getAssessmentSetup().getSpecificLearningGoals().size(); i++) {
                    LearningGoal learningGoal = (LearningGoal) pedagogicalPlanTransfer.getAssessmentSetup().getSpecificLearningGoals().get(i);
                    if(learningGoal.getUse() != null && learningGoal.getUse()) addSpecificLearningGoal(learningGoal.getGoal(), learningGoals);
                }
            } else {
                logger.info("missionSpecification not found!!");
            }
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
