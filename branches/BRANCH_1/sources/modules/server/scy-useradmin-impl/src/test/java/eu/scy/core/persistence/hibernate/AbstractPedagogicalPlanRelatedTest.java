package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.*;
import eu.scy.core.model.pedagogicalplan.*;
import eu.scy.core.persistence.LASDAO;
import eu.scy.core.persistence.PedagogicalPlanPersistenceDAO;
import eu.scy.core.persistence.ScenarioDAO;
import eu.scy.core.persistence.ToolDAO;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jan.2010
 * Time: 12:12:47
 * To change this template use File | Settings | File Templates.
 */
public class AbstractPedagogicalPlanRelatedTest extends AbstractDAOTest {

    protected PedagogicalPlanPersistenceDAO pedagogicalPlanPersistenceDAO = null;
    protected ScenarioDAO scenarioDAO;
    protected LASDAO lasdao;
    protected ToolDAO toolDAO;

    public PedagogicalPlanPersistenceDAO getPedagogicalPlanPersistenceDAO() {
        return pedagogicalPlanPersistenceDAO;
    }

    public void setPedagogicalPlanPersistenceDAO(PedagogicalPlanPersistenceDAO pedagogicalPlanPersistenceDAO) {
        this.pedagogicalPlanPersistenceDAO = pedagogicalPlanPersistenceDAO;
    }

    public ScenarioDAO getScenarioDAO() {
        return scenarioDAO;
    }

    public void setScenarioDAO(ScenarioDAO scenarioDAO) {
        this.scenarioDAO = scenarioDAO;
    }

    public LASDAO getLasdao() {
        return lasdao;
    }

    public void setLasdao(LASDAO lasdao) {
        this.lasdao = lasdao;
    }

    public ToolDAO getToolDAO() {
        return toolDAO;
    }

    public void setToolDAO(ToolDAO toolDAO) {
        this.toolDAO = toolDAO;
    }

    protected PedagogicalPlanTemplateImpl createPedagogicalPlanTemplate(String name) {
        PedagogicalPlanTemplate template = new PedagogicalPlanTemplateImpl();
        template.setName(name);
        template.setDescription("A pedagogical plan for people with white teeth");
        template.setScenario(generateScenario("Exploration", "A new way of thinking...."));
        template.setMission(generateMission("C02 friendly house"));
        saveAndCreatePedagogicalPlan(template);
        return (PedagogicalPlanTemplateImpl) template;
    }

    private void saveAndCreatePedagogicalPlan(PedagogicalPlanTemplate template) {
        getPedagogicalPlanPersistenceDAO().save(template);
        getPedagogicalPlanPersistenceDAO().createPedagogicalPlan(template);
    }


    protected PedagogicalPlan createPedagogicalPlan(PedagogicalPlanTemplate template) {
        PedagogicalPlan plan = getPedagogicalPlanPersistenceDAO().createPedagogicalPlan(template);
        //template.addPedagogicalPlan(plan);
        return plan;
    }

    @Test
    public void testSetup() {
        assertNotNull(getPedagogicalPlanPersistenceDAO());
    }

    private Scenario generateScenario(String name, String description) {
        Scenario scenario = new ScenarioImpl();
        scenario.setName(name);
        scenario.setDescription(description);

        getScenarioDAO().save(scenario);

        LearningActivitySpace orientation = createLAS("Orientation", 10, 10, scenario);
        LearningActivitySpace conceptualisation = createLAS("Conceptualization", 500, 60, scenario);
        LearningActivitySpace experiment = createLAS("Experiment", conceptualisation.getXPos() - 200, conceptualisation.getYPos() + 200, scenario);
        LearningActivitySpace information = createLAS("Information", conceptualisation.getXPos() + 200, conceptualisation.getYPos() + 200, scenario);


        LearningActivitySpace reflection = createLAS("Reflection", 400, 200, scenario);
        LearningActivitySpace evaluation = createLAS("Evaluation", 400, 200, scenario);
        LearningActivitySpace design = createLAS("Design", 400, 200, scenario);
        LearningActivitySpace construction = createLAS("Construction", 400, 200, scenario);
        LearningActivitySpace reporting = createLAS("Reporting", 400, 200, scenario);

        Tool scyMapper = getToolDAO().findToolByName("SCYMapper");
        Tool scySim = getToolDAO().findToolByName("SCYSim");


        scenario.setLearningActivitySpace(orientation);


        AnchorELO outputFromOrientationELO = createAnchorELO("Output from Orientation");
        outputFromOrientationELO.setXPos(orientation.getXPos() + 200);
        outputFromOrientationELO.setYPos(orientation.getYPos());
        Activity identifyGoalStates = addActivity(orientation, "Identify goal states", outputFromOrientationELO);

        outputFromOrientationELO.setInputTo(conceptualisation);
        getLasdao().addToolToActivity(scyMapper, identifyGoalStates);
        AnchorELO outputFromConceptualisation = createAnchorELO("Output from Conceptualisation");
        outputFromConceptualisation.setXPos(conceptualisation.getXPos() - 200);
        outputFromConceptualisation.setYPos(conceptualisation.getYPos() + 100);
        Activity createSimulation = addActivity(conceptualisation, "Create simulation", outputFromConceptualisation);
        getLasdao().addToolToActivity(scySim, createSimulation);
        outputFromConceptualisation.setInputTo(experiment);

        AnchorELO outputFromConceptualisation2 = createAnchorELO("Output from Conceptualisation 2");
        outputFromConceptualisation2.setXPos(conceptualisation.getXPos() + 200);
        outputFromConceptualisation2.setYPos(conceptualisation.getYPos() + 100);
        Activity doSomeInformationActivity = addActivity(conceptualisation, "Create some information", outputFromConceptualisation2);
        outputFromConceptualisation2.setInputTo(information);

        return scenario;


    }

    private Mission generateMission(String name) {
        Mission mission = new MissionImpl();
        mission.setName(name);
        getScenarioDAO().save(mission);
        return mission;

    }

    private Activity addActivity(LearningActivitySpace las, String activityName, AnchorELO anchorELO) {
        Activity act = createActivity(activityName);
        las.addActivity(act);
        act.setAnchorELO(anchorELO);
        return act;
    }

    private Activity createActivity(String name) {
        Activity activity = new ActivityImpl();
        activity.setName(name);
        return activity;
    }

    private AnchorELO createAnchorELO(String name) {
        AnchorELO elo = new AnchorELOImpl();
        elo.setName(name);
        return elo;
    }

    private Tool createTool(String name, String description) {
        Tool tool = new ToolImpl();
        tool.setName(name);
        tool.setDescription(description);
        return tool;
    }

    private LearningActivitySpace createLAS(String name, int xPos, int yPos, Scenario scenario) {
        LearningActivitySpace las = new LearningActivitySpaceImpl();
        las.setName(name);
        las.setXPos(xPos);
        las.setYPos(yPos);
        las.setParticipatesIn(scenario);
        return las;
    }


}
