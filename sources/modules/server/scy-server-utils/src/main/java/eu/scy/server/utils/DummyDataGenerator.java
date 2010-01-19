package eu.scy.server.utils;

import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.ScenarioService;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.pedagogicalplan.*;
import eu.scy.core.model.pedagogicalplan.*;
import org.springframework.beans.factory.InitializingBean;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.nov.2009
 * Time: 22:59:44
 * To change this template use File | Settings | File Templates.
 */
public class DummyDataGenerator implements InitializingBean {

    private static Logger log = Logger.getLogger("DummyDataGenerator.class");

    private ScenarioService scenarioService;
    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;
    private UserService userService;


    private void generatePedagogicalPlanTemplates() {
        PedagogicalPlanTemplate template = new PedagogicalPlanTemplateImpl();
        template.setName("CO2 House - home of the wild!");
        template.setDescription("A pedagogical plan for people with white teeth");
        template.setScenario(generateScenario());
        template.setMission(generateMission());
        saveAndCreatePedagogicalPlan(template);

        PedagogicalPlanTemplate ecosystems = new PedagogicalPlanTemplateImpl();
        ecosystems.setName("Ecosystems");
        ecosystems.setDescription("I haven't got a clue about what this will be about - but sounds freakin cool!");
        ecosystems.setScenario(generateScenario());
        saveAndCreatePedagogicalPlan(ecosystems);

        PedagogicalPlanTemplate canteenCuisine = new PedagogicalPlanTemplateImpl();
        canteenCuisine.setName("Canteen Cuisine");
        canteenCuisine.setDescription("Canteen Cuisine: I haven't got a clue about what this will be about - but sounds freakin cool!");
        canteenCuisine.setScenario(generateScenario());
        saveAndCreatePedagogicalPlan(canteenCuisine);

    }

    private Mission generateMission() {
        Mission mission = new MissionImpl();
        mission.setName("The first mission");
        return mission;

    }

    private void saveAndCreatePedagogicalPlan(PedagogicalPlanTemplate template) {
        if (getPedagogicalPlanPersistenceService().getPedagogicalPlanByName(template.getName()) == null) {
            log.info("Did not find a default plan - creating one...");
            getPedagogicalPlanPersistenceService().save(template);
            getPedagogicalPlanPersistenceService().createPedagogicalPlan(template);
        } else {
            log.info("The default plan is already added - will not create any more!");
        }
    }

    private Scenario generateScenario() {
        Scenario scenario = new ScenarioImpl();
        scenario.setName("Scenario 1 - Exploration");
        scenario.setDescription("Exploring the facts of life and other important stuff");

        LearningActivitySpace orientation = createLAS("LAS Orientation");
        LearningActivitySpace conceptualization = createLAS("LAS Conceptualization");
        scenario.setLearningActivitySpace(orientation);
        Activity activity1 = addActivity(orientation, "Identify goal states");
        Activity activity2 = addActivity(orientation, "Identify learning goals");

        AnchorELO elo1 = createAnchorELO("Concept map");
        activity1.setAnchorELO(elo1);
        elo1.setInputTo(conceptualization);




        addActivity(conceptualization, "Build a model");
        addActivity(conceptualization, "Give and classify examples");

        LearningActivitySpace experiment = createLAS("LAS Experiment");
        addActivity(experiment, "Design an experimental procedure");
        addActivity(experiment, "Run experiment");
        addActivity(experiment, "Organize data");
        addActivity(experiment, "Interpret data");
/*
        LearningActivitySpace information = createLAS("LAS Information");
        addActivity(information, "Browse resources for specific information");

        LearningActivitySpace reporting = createLAS("LAS Reporting");
        addActivity(reporting, "Summarise");
        addActivity(reporting, "Explain");
        addActivity(reporting, "Propose a decision");

        LearningActivitySpace design= createLAS("LAS Design");
        addActivity(design, "Design a virtual artefact");

        LearningActivitySpace construction= createLAS("LAS Construction");
        addActivity(construction, "Build a virtual artefact");
        
        LearningActivitySpace evaluation= createLAS("LAS Evaluation");
        addActivity(evaluation, "Evaluate ELO");
        addActivity(evaluation, "Evaluate resources");

        LearningActivitySpace refleaction= createLAS("LAS Reflection");
        addActivity(refleaction, "Identify differences between current knowledge and learning goals");
  */

        /*AnchorELO elo1 = createAnchorELO("Concept map");
        activity.setAnchorELO(elo1);

        LearningActivitySpace conceptualisation = createLAS("Conceptualization");
        elo1.setInputTo(conceptualisation);

        Activity conceptualizationActivity = createActivity("Conceptualization activity");
        conceptualisation.addActivity(conceptualizationActivity);
        addToolToActivity(conceptualizationActivity, "SCYMapper", "A concept mapping tool");

        AnchorELO conceptualizationELO = createAnchorELO("ConceptualisationELO");
        conceptualizationActivity.setAnchorELO(conceptualizationELO);
        */
        //getScenarioService().createScenario(scenario);
        return scenario;


    }

    private Activity addActivity(LearningActivitySpace las, String activityName) {
        Activity act = createActivity("Concept map on global warming");
        las.addActivity(act);
        return act;
    }

    private LearningActivitySpace createLAS(String name) {
        LearningActivitySpace las = new LearningActivitySpaceImpl();
        las.setName(name);
        return las;
    }

    private Activity createActivity(String name) {
        Activity activity = new ActivityImpl();
        activity.setName(name);
        return activity;
    }

    // TODO check this because it did not compile
//    private LearningActivitySpaceToolConfigurationImpl addToolToActivity(Activity activity, String toolName, String toolDescription) {
//        LearningActivitySpaceToolConfigurationImpl configuration = new SCYMapperConfiguration();
//        Tool scyPlanner = new ToolImpl();
//        scyPlanner.setName(toolName);
//        scyPlanner.setDescription(toolDescription);
//        configuration.setTool(scyPlanner);
//        activity.addLearningActivitySpaceToolConfiguration(configuration);
//        return configuration;
//    }

    private AnchorELO createAnchorELO(String name) {
        AnchorELO elo = new AnchorELOImpl();
        elo.setName(name);
        return elo;
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

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
         log.info("============================================================================");
         log.info("============================================================================");
         log.info("============================================================================");
         log.info("============================================================================");
         log.info("============================================================================");
        generatePedagogicalPlanTemplates();
        //generateDummyUsers();
    }


}
