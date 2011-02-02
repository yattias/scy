package eu.scy.server.controllers.createnewpedplansteps;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.*;
import eu.scy.core.model.impl.pedagogicalplan.ActivityImpl;
import eu.scy.core.model.impl.pedagogicalplan.AnchorELOImpl;
import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceImpl;
import eu.scy.core.model.impl.pedagogicalplan.ScenarioImpl;
import eu.scy.core.model.pedagogicalplan.*;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.okt.2010
 * Time: 06:15:35
 * To change this template use File | Settings | File Templates.
 */
public class SelectScenarioController extends BaseController {

    private ScenarioService scenarioService;
    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;
    private LASService lasService;
    private ActivityService activityService;
    private AnchorELOService anchorELOService;
    private MissionELOService missionELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String pedPlanId = request.getParameter("pedPlanId");

        String action = request.getParameter("action");
        logger.info("ACTION: " + action);
        if (action != null) {
            logger.info("ACTION " + action + " was found - creating copy!");
            addCopyOfSelecteScenario(request);
            modelAndView.setViewName("redirect:assignStudents.html?pedPlanId=" + pedPlanId);
        } else {
            modelAndView.addObject("transporters", getMissionELOService().getWebSafeTransporters(getMissionELOService().getMissionSpecifications()));
        }


    }

    private void addCopyOfSelecteScenario(HttpServletRequest request) {
        try {
            logger.info("CREATING NEW SCENARIO BASE ON EXISTING SCENARIO!");

            URI uri = new URI(request.getParameter("uri"));
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(uri, getMissionELOService());
            getMissionELOService().createMissionSpecification(missionSpecificationElo, getCurrentUserName(request));

            logger.info("CREATING COPY OF " + missionSpecificationElo.getTitle());
        } catch (Exception e) {
            e.printStackTrace();  
        }
    }

    private Scenario createCopyOfScenario(Scenario originalScenario, PedagogicalPlan pedagogicalPlan) {
        Scenario copy = new ScenarioImpl();
        copy.setName(originalScenario.getName() + " for " + pedagogicalPlan.getName());
        getScenarioService().save(copy);

        List lasses = getScenarioService().getLearningActivitySpaces(originalScenario);


        Map lasMap = new HashMap();

        if(lasses.size() >0 ) {
            for (int i = 0; i < lasses.size(); i++) {
                LearningActivitySpace learningActivitySpace = (LearningActivitySpace) lasses.get(i);
                LearningActivitySpace copyLas = copyLearningActivitySpace(learningActivitySpace, copy, pedagogicalPlan);
                lasMap.put(learningActivitySpace, copyLas);
                if(i == 0) {
                    copy.setLearningActivitySpace(copyLas);
                }
            }

            logger.info("================== DONE CREATING LASSES `===== WILL ADD ANCHOR ELOS!");

            getScenarioService().save(copy);


            connectAnchorElosAndLasses(originalScenario, copy, lasMap);


        }




        return copy;

    }

    private void connectAnchorElosAndLasses(Scenario originalScenario, Scenario copyScenario, Map lasMap) {
        List originalLasList = getScenarioService().getLearningActivitySpaces(originalScenario);
        List copyLasList = getScenarioService().getLearningActivitySpaces(copyScenario);

        for (int i = 0; i < originalLasList.size(); i++) {
            LearningActivitySpace originalLearningActivitySpace = (LearningActivitySpace) originalLasList.get(i);
            LearningActivitySpace copyLearningActivitySpace = (LearningActivitySpace) copyLasList.get(i);

            logger.info("Identifying anchor elos for learning activity space: " + originalLearningActivitySpace);
            AnchorELO original = findAnchorELoFor(originalLearningActivitySpace);
            AnchorELO copy = findAnchorELoFor(copyLearningActivitySpace);

            logger.info("FOUND ORIGINAL: " + original.getName() + " AND COPY: " + copy.getName());

            LearningActivitySpace inputTo = (LearningActivitySpace) lasMap.get(original.getInputTo());
            if(inputTo != null) copy.setInputTo(inputTo);
            getAnchorELOService().save(copy);

        }

    }

    private AnchorELO findAnchorELoFor(LearningActivitySpace learningActivitySpace) {
        List anchorElos = getLasService().getAnchorELOsProducedByLAS(learningActivitySpace);
        for (int i = 0; i < anchorElos.size(); i++) {
            AnchorELO anchorELO = (AnchorELO) anchorElos.get(i);
            LearningActivitySpace inputTo = anchorELO.getInputTo();
            String inputToName = "Nothing";
            if(inputTo != null) inputToName = inputTo.getName();
            logger.info("---> " + learningActivitySpace.getName() + " PROCUCED: " + anchorELO.getName() + " which inputs to " + inputToName);
        }

        if(anchorElos.size() > 0 ) return (AnchorELO) anchorElos.get(0);

        return null;
    }

    private LearningActivitySpace copyLearningActivitySpace(LearningActivitySpace first, Scenario newScenario, PedagogicalPlan pedagogicalPlan) {
        LearningActivitySpace newLas = new LearningActivitySpaceImpl();
        newLas.setName(first.getName() + " for " + pedagogicalPlan.getName());
        newLas.setDescription(first.getDescription());
        newLas.setParticipatesIn(newScenario);
        newLas.setXPos(first.getXPos());
        newLas.setYPos(first.getYPos());

        List activities = first.getActivities();
        for (int i = 0; i < activities.size(); i++) {
            Activity activity = (Activity) activities.get(i);
            copyAndAddToNewLAS(activity, newLas, pedagogicalPlan);
        }

        return newLas;
    }

    private AnchorELO copyAndAddToNewLAS(Activity original, LearningActivitySpace newLas, PedagogicalPlan pedagogicalPlan) {
        Activity newActivity = new ActivityImpl();
        newActivity.setName(original.getName() + " for " + pedagogicalPlan.getName() );
        newActivity.setDescription(original.getDescription());
        newActivity.setAutoaddToStudentPlan(original.getAutoaddToStudentPlan());
        newActivity.setEndDate(original.getEndDate());
        newActivity.setStartDate(original.getStartDate());
        newActivity.setStartTime(original.getStartTime());
        newActivity.setEndTime(original.getEndTime());
        newActivity.setExpectedDurationInMinutes(original.getExpectedDurationInMinutes());
        newActivity.setLearningActivitySpace(newLas);
        newActivity.setTeacherRoleType(original.getTeacherRoleType());


        AnchorELO originalAnchorELO = original.getAnchorELO();
        if(originalAnchorELO != null) {
            AnchorELO copy = new AnchorELOImpl();
            copy.setName(originalAnchorELO.getName() + " for " + pedagogicalPlan.getName());
            copy.setDescription(originalAnchorELO.getDescription());
            copy.setHumanReadableName(originalAnchorELO.getHumanReadableName());
            copy.setMissionMapId(originalAnchorELO.getMissionMapId());

            newActivity.setAnchorELO(copy);


            getAnchorELOService().save(copy);
            getActivityService().save(newActivity);

            return copy;

        }

        return null;


    }

    public ScenarioService getScenarioService() {
        return scenarioService;
    }

    public void setScenarioService(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }

    public LASService getLasService() {
        return lasService;
    }

    public void setLasService(LASService lasService) {
        this.lasService = lasService;
    }

    public ActivityService getActivityService() {
        return activityService;
    }

    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    public AnchorELOService getAnchorELOService() {
        return anchorELOService;
    }

    public void setAnchorELOService(AnchorELOService anchorELOService) {
        this.anchorELOService = anchorELOService;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
