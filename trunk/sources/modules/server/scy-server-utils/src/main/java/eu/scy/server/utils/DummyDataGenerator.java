package eu.scy.server.utils;

import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.ScenarioService;
import eu.scy.core.UserService;
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
        template.setScenario(generateScenario("Exploration", "A new way of thinking...."));
        template.setMission(generateMission("C02 friendly house"));
        saveAndCreatePedagogicalPlan(template);

        PedagogicalPlanTemplate ecosystems = new PedagogicalPlanTemplateImpl();
        ecosystems.setName("Ecosystems");
        ecosystems.setDescription("I haven't got a clue about what this will be about - but sounds freakin cool!");
        ecosystems.setScenario(generateScenario("Practical Scruffy nuffy (PSN)", "Whole new way of thinking"));
        ecosystems.setMission(generateMission("Eco systems mission"));
        saveAndCreatePedagogicalPlan(ecosystems);

        PedagogicalPlanTemplate canteenCuisine = new PedagogicalPlanTemplateImpl();
        canteenCuisine.setName("Canteen Cuisine");
        canteenCuisine.setDescription("Canteen Cuisine: I haven't got a clue about what this will be about - but sounds freakin cool!");
        canteenCuisine.setScenario(generateScenario("Transitive Accumulative Mutations", "Based on Reinbergers classic theory"));
        canteenCuisine.setMission(generateMission("Canteen Cusine"));
        saveAndCreatePedagogicalPlan(canteenCuisine);

    }

    private Mission generateMission(String name) {
        Mission mission = new MissionImpl();
        mission.setName(name);
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

    private Scenario generateScenario(String name, String description) {
        Scenario scenario = new ScenarioImpl();
        scenario.setName(name);
        scenario.setDescription(description);

        LearningActivitySpace orientation = createLAS("Orientation", 10 , 10 );
        LearningActivitySpace conceptualisation = createLAS("Conceptualization", 500, 60);
        LearningActivitySpace experiment = createLAS("Experiment", conceptualisation.getXPos() - 200, conceptualisation.getYPos() + 200);
        LearningActivitySpace information= createLAS("Information", conceptualisation.getXPos() + 200, conceptualisation.getYPos() + 200);


        LearningActivitySpace reflection= createLAS("Reflection", 400, 200);
        LearningActivitySpace evaluation= createLAS("Evaluation", 400, 200);
        LearningActivitySpace design= createLAS("Design", 400, 200);
        LearningActivitySpace construction= createLAS("Construction", 400, 200);
        LearningActivitySpace reporting= createLAS("Reporting", 400, 200);


        scenario.setLearningActivitySpace(orientation);

        AnchorELO outputFromOrientationELO = createAnchorELO("Output from Orientation");
        outputFromOrientationELO.setXPos(orientation.getXPos() + 200);
        outputFromOrientationELO.setYPos(orientation.getYPos());
        Activity identifyGoalStates = addActivity(orientation, "Identify goal states", outputFromOrientationELO);
        outputFromOrientationELO.setInputTo(conceptualisation);

        AnchorELO outputFromConceptualisation = createAnchorELO("Output from Conceptualisation");
        outputFromConceptualisation.setXPos(conceptualisation.getXPos() - 200);
        outputFromConceptualisation.setYPos(conceptualisation.getYPos() + 100);
        Activity createSimulation =  addActivity(conceptualisation, "Create simulation", outputFromConceptualisation);
        outputFromConceptualisation.setInputTo(experiment);

        AnchorELO outputFromConceptualisation2 = createAnchorELO("Output from Conceptualisation 2");
        outputFromConceptualisation2.setXPos(conceptualisation.getXPos() + 200);
        outputFromConceptualisation2.setYPos(conceptualisation.getYPos() + 100);
        Activity doSomeInformationActivity = addActivity(conceptualisation, "Create some information", outputFromConceptualisation2);
        outputFromConceptualisation2.setInputTo(information);

        /*
        AnchorELO outputFromInformation = createAnchorELO("Output from Information");
        outputFromInformation.setXPos(information.getXPos() - 100);
        outputFromInformation.setYPos(information.getYPos());
        Activity doSomethingWithTheOutputFromInformation = addActivity(experiment, "Do something with information", outputFromInformation);
        outputFromInformation.setInputTo(experiment);
        */
        /*
        Activity conceptualizationActivity = createActivity("Conceptualization activity");
        conceptualisation.addActivity(conceptualizationActivity);
        addToolToActivity(conceptualizationActivity, "SCYMapper", "A concept mapping tool");

        AnchorELO conceptualizationELO = createAnchorELO("ConceptualisationELO");
        conceptualizationActivity.setAnchorELO(conceptualizationELO);
        conceptualizationELO.setInputTo(experiment);

       */









         /*


        Activity buildAModel = addActivity(conceptualization, "Build a model");
        AnchorELO elo2 = createAnchorELO("Simulation");
        buildAModel.setAnchorELO(elo2);
        elo2.setInputTo(experiment);
        addActivity(conceptualization, "Give and classify examples");


        addActivity(experiment, "Design an experimental procedure");
        addActivity(experiment, "Run experiment");
        addActivity(experiment, "Organize data");
        addActivity(experiment, "Interpret data");

        LearningActivitySpace information = createLAS("LAS Information");
        addActivity(information, "Browse resources for specific information");

        LearningActivitySpace reporting = createLAS("LAS Reporting");
        addActivity(reporting, "Summarise");
        addActivity(reporting, "Explain");
        addActivity(reporting, "Propose a decision");

        LearningActivitySpace design = createLAS("LAS Design");
        addActivity(design, "Design a virtual artefact");

        LearningActivitySpace construction = createLAS("LAS Construction");
        addActivity(construction, "Build a virtual artefact");

        LearningActivitySpace evaluation = createLAS("LAS Evaluation");
        addActivity(evaluation, "Evaluate ELO");
        addActivity(evaluation, "Evaluate resources");

        LearningActivitySpace refleaction = createLAS("LAS Reflection");
        addActivity(refleaction, "Identify differences between current knowledge and learning goals");


        //AnchorELO conceptMapELO = createAnchorELO("Concept map");
        //activity.setAnchorELO(conceptMapELO);
          */
        return scenario;


    }

    private Activity addActivity(LearningActivitySpace las, String activityName, AnchorELO anchorELO) {
        Activity act = createActivity(activityName);
        las.addActivity(act);
        act.setAnchorELO(anchorELO);
        return act;
    }

    private LearningActivitySpace createLAS(String name, int xPos, int yPos) {
        LearningActivitySpace las = new LearningActivitySpaceImpl();
        las.setName(name);
        las.setXPos(xPos);
        las.setYPos(yPos);
        return las;
    }

    private Activity createActivity(String name) {
        Activity activity = new ActivityImpl();
        activity.setName(name);
        return activity;
    }

    private LearningActivitySpaceToolConfigurationImpl addToolToActivity(Activity activity, String toolName, String toolDescription) {
        LearningActivitySpaceToolConfigurationImpl configuration = new SCYMapperConfiguration();
        Tool scyPlanner = new ToolImpl();
        scyPlanner.setName(toolName);
        scyPlanner.setDescription(toolDescription);
        configuration.setTool(scyPlanner);
        activity.addLearningActivitySpaceToolConfiguration(configuration);
        return configuration;
    }

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
