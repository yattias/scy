package eu.scy.server.controllers;

import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.ScenarioService;
import eu.scy.core.model.impl.pedagogicalplan.*;
import eu.scy.core.model.pedagogicalplan.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 23:16:06
 * To change this template use File | Settings | File Templates.
 */
public class ViewCreateNewPedagogicalPlanController extends BaseController {

    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;
    private ScenarioService scenarioService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        logger.info("CREATING NEW PEDAGOGICAL PLAN!");
        PedagogicalPlanTemplate template = new PedagogicalPlanTemplateImpl();
        Scenario scenario = createScenario("Scenario 1");
        template.setName("New pedagogical plan");
        template.setDescription("A pedagogical plan");
        getPedagogicalPlanPersistenceService().save(template);
        PedagogicalPlan plan = getPedagogicalPlanPersistenceService().createPedagogicalPlan(template);
        setModel(plan);
    }

    private Scenario createScenario(String name) {
        Scenario scenario = new ScenarioImpl();
        scenario.setName(name);
        getScenarioService().save(scenario);

        LearningActivitySpace learninigActivitySpace = createLAS("LAS1,", 100, 100, scenario);
        learninigActivitySpace.setParticipatesIn(scenario);
        getScenarioService().save(learninigActivitySpace);


        return scenario;
    }


    private LearningActivitySpace createLAS(String name, int xPos, int yPos, Scenario scenario) {
        LearningActivitySpace las = new LearningActivitySpaceImpl();
        las.setName(name);
        las.setXPos(xPos);
        las.setYPos(yPos);
        las.setParticipatesIn(scenario);

        Assessment assessment = new AssessmentImpl();
        assessment.setName("Assessment for " + name);
        las.setAssessment(assessment);

        AssessmentStrategy strategy = new PeerToPeerAssessmentStrategyImpl();
        assessment.setAssessmentStrategy(strategy);

        return las;
    }


    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }

    public ScenarioService getScenarioService() {
        return scenarioService;
    }

    public void setScenarioService(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }
}
