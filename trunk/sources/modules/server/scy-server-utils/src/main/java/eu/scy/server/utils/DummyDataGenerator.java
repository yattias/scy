package eu.scy.server.utils;

import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.ScenarioService;
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

    private void generatePedagogicalPlanTemplates() {
        PedagogicalPlanTemplate template = new PedagogicalPlanTemplateImpl();
        template.setName("Mission 1");
        template.setDescription("A pedagogical plan for people with white teeth");
        template.setScenario(generateScenario());
        if(getPedagogicalPlanPersistenceService().getPedagogicalPlanByName(template.getName()) != null)  {
            log.info("Did not find a default plan - creating one...");
            getPedagogicalPlanPersistenceService().save(template);
        } else {
            log.info("The default plan is already added - will not create any more!");
        }



    }

    private Scenario generateScenario() {
        Scenario scenario = new ScenarioImpl();
        scenario.setName("Scenario 1 - Exploration");

        LearningActivitySpace orientation = createLAS("LAS Orientation");
        scenario.setLearningActivitySpace(orientation);
        addActivity(orientation, "Identify goal states");
        addActivity(orientation, "Identify learning goals");

        LearningActivitySpace conceptualization = createLAS("LAS Conceptualization");
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

        //AnchorELO conceptualizationELO = createAnchorELO("ConceptualisationELO");
        //conceptualizationActivity.setAnchorELO(conceptualizationELO);
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

    @Override
    public void afterPropertiesSet() throws Exception {

        generatePedagogicalPlanTemplates();
    }
}
