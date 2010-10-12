package eu.scy.server.utils;

import eu.scy.core.*;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.pedagogicalplan.*;
import eu.scy.core.model.pedagogicalplan.*;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
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
    private StudentPedagogicalPlanPersistenceService studentPedagogicalPlanPersistenceService;
    private UserService userService;
    private ToolService toolService;
    private LASService lasService;
    private MissionService missionService;
    private AgentService agentService;

    private ImportedXMLMission importedXMLMission;

    @Override
    public void afterPropertiesSet() throws Exception {
        //generatePedagogicalPlanTemplates();
        generatedMissionMapBasedOnXML();
    }
    


    private void generatedMissionMapBasedOnXML() {
        Scenario scenario = setupScenarioBasedOnXML();
        PedagogicalPlanTemplate template = new PedagogicalPlanTemplateImpl();
        template.setName("CO2 House");
        template.setDescription("A pedagogical plan");
        template.setScenario(scenario);
        template.setMission(generateMission("C02 friendly house"));
        saveAndCreatePedagogicalPlan(template);


    }

    private Scenario setupScenarioBasedOnXML() {
        Scenario scenario = new ScenarioImpl();
        scenario.setName("Exploration - M1");
        getScenarioService().save(scenario);


        List lases = getImportedXMLMission().getBasicMissionMap().getLasses();
        AnchorELO currentAnchorELO = null;
        LearningActivitySpace lastLAS = null;
        BasicLas lastBasicLas = null;
        Activity lastActivity = null;
        BasicMissionAnchor lastMissionAnchor = null;
        for (int lasCounter = 0; lasCounter < lases.size(); lasCounter++) {
            BasicLas basicLas = (BasicLas) lases.get(lasCounter);

            LearningActivitySpace learninigActivitySpace =  createLAS(basicLas.getId(), (int)basicLas.getxPosition(), (int) basicLas.getyPosition(), scenario);
            learninigActivitySpace.setParticipatesIn(scenario);
            getScenarioService().save(learninigActivitySpace);
            lastLAS = learninigActivitySpace;
            lastBasicLas = basicLas;
            if(lasCounter == 0)  {
                scenario.setLearningActivitySpace(learninigActivitySpace);
            } else {
                currentAnchorELO.setInputTo(learninigActivitySpace);
            }
            BasicMissionAnchor anchor = getImportedXMLMission().getAnchor(basicLas.getAnchorEloId());
            lastMissionAnchor = anchor;
            if(anchor != null) {
                AnchorELO outputFromLearningActivitySpace = createAnchorELO(anchor.getId(), "");
                if(anchor.getxPos() != null) {
                    outputFromLearningActivitySpace.setXPos(anchor.getxPos());
                }
                if(anchor.getyPos() != null) {
                    outputFromLearningActivitySpace.setYPos(anchor.getyPos());    
                }


                currentAnchorELO = outputFromLearningActivitySpace;
                Activity doTheStuff = addActivity(learninigActivitySpace, "Do the stuff - " + currentAnchorELO.getName(), outputFromLearningActivitySpace);
                lastActivity = doTheStuff;
            }
        }

        AnchorELO lastAnchorELO = createAnchorELO(lastMissionAnchor.getId(), "");
        lastActivity.setAnchorELO(lastAnchorELO);

        getMissionService().save(lastActivity);
        lastAnchorELO.setProducedBy(lastActivity);
        getMissionService().save(lastAnchorELO);

        return scenario;
    }


    private void generatePedagogicalPlanTemplates() {

        Tool scyMapper = createTool("SCYMapper", "A concept bla bla");
        Tool scySim = createTool ("SCYSim", "Simulating simulations is a recursive expression");

        Agent coolAgent = createAgent("CoolAgent", "A rather cool agent");
        Agent jamesBond = createAgent("James", "Bond");

        getAgentService().save(coolAgent);
        getAgentService().save(jamesBond);

        getToolService().save(scyMapper);
        getToolService().save(scySim);

        PedagogicalPlanTemplate template = new PedagogicalPlanTemplateImpl();
        template.setName("CO2 House - home of the wild!2");
        template.setDescription("A pedagogical plan for people with white teeth");
        template.setScenario(generateScenario("Exploration", "A new way of thinking...."));
        template.setMission(generateMission("C02 friendly house"));
        saveAndCreatePedagogicalPlan(template);

        PedagogicalPlanTemplate ecosystems = new PedagogicalPlanTemplateImpl();
        ecosystems.setName("Ecosystems2");
        ecosystems.setDescription("I haven't got a clue about what this will be about - but sounds freakin cool!");
        ecosystems.setScenario(generateScenario("Practical Scruffy nuffy (PSN)", "Whole new way of thinking"));
        ecosystems.setMission(generateMission("Eco systems mission"));
        saveAndCreatePedagogicalPlan(ecosystems);

        PedagogicalPlanTemplate canteenCuisine = new PedagogicalPlanTemplateImpl();
        canteenCuisine.setName("Canteen Cuisine3");
        canteenCuisine.setDescription("Canteen Cuisine: I haven't got a clue about what this will be about - but sounds freakin cool!");
        canteenCuisine.setScenario(generateScenario("Transitive Accumulative Mutations", "Based on Reinbergers classic theory"));
        canteenCuisine.setMission(generateMission("Canteen Cusine"));
        saveAndCreatePedagogicalPlan(canteenCuisine);



    }

    private Agent createAgent(String name, String description) {
        Agent agent = new AgentImpl();
        agent.setName(name);
        agent.setDescription(description);
        return agent;
    }

    private Mission generateMission(String name) {
        Mission mission = new MissionImpl();
        mission.setName(name);

        mission.addLearningGoal(createLearningGoal("Learning Goal for " + name + "1"));
        mission.addLearningGoal(createLearningGoal("Learning Goal for " + name + " 2"));

        getMissionService().save(mission);

        return mission;

    }

    private LearningGoal createLearningGoal(String name) {
        LearningGoal goal = new LearningGoalImpl();
        goal.setName(name);
        return goal;
    }

    private void saveAndCreatePedagogicalPlan(PedagogicalPlanTemplate template) {
        if (getPedagogicalPlanPersistenceService().getPedagogicalPlanByName(template.getName()) == null) {
            log.info("Did not find a default plan - creating one...");
            getPedagogicalPlanPersistenceService().save(template);
            PedagogicalPlan plan = getPedagogicalPlanPersistenceService().createPedagogicalPlan(template);
            assignStudentsToPlan(plan);

        } else {
            log.info("The default plan is already added - will not create any more!");
        }
    }

    private void assignStudentsToPlan(PedagogicalPlan plan) {
        assignStudent("Willy", "Wonka", "wiwoo" , "scy", plan);
        assignStudent("Dare", "Devil", "dade", "scy", plan);
        assignStudent("Donald", "Duck", "dodu", "scy", plan);
        assignStudent("Uber", "Dude", "ubdu", "scy", plan);
        assignStudent("Harry", "Klein", "hakl", "scy", plan);
        assignStudent("Hercule", "Poirot", "hepo", "scy", plan);
    }

    private User assignStudent(String firstName, String lastName, String username, String password, PedagogicalPlan pedagogicalPlan) {
        User student1 = addUserIfNotExists(firstName, lastName, username, password, "ROLE_STUDENT");
        if(student1 != null) {
            getStudentPedagogicalPlanPersistenceService().createStudentPlan(pedagogicalPlan, student1);
        }
        return student1;

    }


    private User addUserIfNotExists(String firstName, String lastName, String userName, String password, String role) {
        log.info("ADding user if not exists: " + firstName + " " + lastName + " " + userName + " shhhhh " + password);
        if (getUserService().getUser(userName) == null) {
            return getUserService().createUser(userName, password, role);
        }
        return null;
    }

    private Scenario generateScenario(String name, String description) {

        Agent agent1 = getAgentService().getAgentByName("CoolAgent");


        Scenario scenario = new ScenarioImpl();
        scenario.setName(name);
        scenario.setDescription(description);

        getScenarioService().save(scenario);

        LearningActivitySpace orientation = createLAS("Orientation", 10 , 10, scenario);
        LearningActivitySpace conceptualisation = createLAS("Conceptualization", 500, 60, scenario);
        LearningActivitySpace experiment = createLAS("Experiment", conceptualisation.getXPos() - 200, conceptualisation.getYPos() + 200, scenario);
        LearningActivitySpace information= createLAS("Information", conceptualisation.getXPos() + 200, conceptualisation.getYPos() + 200, scenario);

        if(agent1 != null ) {
            getAgentService().addAgentToLearningActivitySpace(orientation, agent1);
            getAgentService().addAgentToLearningActivitySpace(experiment, agent1);   
        }

        LearningActivitySpace reflection= createLAS("Reflection", 400, 200, scenario);
        LearningActivitySpace evaluation= createLAS("Evaluation", 400, 200, scenario);
        LearningActivitySpace design= createLAS("Design", 400, 200, scenario);
        LearningActivitySpace construction= createLAS("Construction", 400, 200, scenario);
        LearningActivitySpace reporting= createLAS("Reporting", 400, 200, scenario);

        Tool scyMapper = getToolService().findToolByName("SCYMapper");
        Tool scySim = getToolService().findToolByName("SCYSim");




        scenario.setLearningActivitySpace(orientation);


        AnchorELO outputFromOrientationELO = createAnchorELO("Output from Orientation", "We are the world");
        outputFromOrientationELO.setXPos(orientation.getXPos() + 200);
        outputFromOrientationELO.setYPos(orientation.getYPos());
        Activity identifyGoalStates = addActivity(orientation, "Identify goal states", outputFromOrientationELO);

        outputFromOrientationELO.setInputTo(conceptualisation);
        getLasService().addToolToActivity(scyMapper, identifyGoalStates);
        AnchorELO outputFromConceptualisation = createAnchorELO("Output from Conceptualisation", "Conceptualize me");
        outputFromConceptualisation.setXPos(conceptualisation.getXPos() - 200);
        outputFromConceptualisation.setYPos(conceptualisation.getYPos() + 100);
        Activity createSimulation =  addActivity(conceptualisation, "Create simulation", outputFromConceptualisation);
        getLasService().addToolToActivity(scySim, createSimulation);
        outputFromConceptualisation.setInputTo(experiment);

        AnchorELO outputFromConceptualisation2 = createAnchorELO("Output from Conceptualisation 2", "me too");
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
        anchorELO.setProducedBy(act);
        //getMissionService().save(anchorELO);
        return act;
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
        configuration.setActivity(activity);
        configuration.setLearningActivitySpace(activity.getLearningActivitySpace());
        activity.addLearningActivitySpaceToolConfiguration(configuration);
        return configuration;
    }

    private AnchorELO createAnchorELO(String name, String description) {
        log.info("CREATING ANCHOR ELO: " + name);
        AnchorELO elo = new AnchorELOImpl();
        elo.setDescription(description);
        elo.setName(name);
        elo.setMissionMapId(name);
        return elo;
    }

    private Tool createTool(String name, String description){
        Tool tool = new ToolImpl();
        tool.setName(name);
        tool.setDescription(description);
        return tool;
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

    public ToolService getToolService() {
        return toolService;
    }

    public void setToolService(ToolService toolService) {
        this.toolService = toolService;
    }

    public LASService getLasService() {
        return lasService;
    }

    public void setLasService(LASService lasService) {
        this.lasService = lasService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public StudentPedagogicalPlanPersistenceService getStudentPedagogicalPlanPersistenceService() {
        return studentPedagogicalPlanPersistenceService;
    }

    public void setStudentPedagogicalPlanPersistenceService(StudentPedagogicalPlanPersistenceService studentPedagogicalPlanPersistenceService) {
        this.studentPedagogicalPlanPersistenceService = studentPedagogicalPlanPersistenceService;
    }

    public MissionService getMissionService() {
        return missionService;
    }

    public void setMissionService(MissionService missionService) {
        this.missionService = missionService;
    }

    public AgentService getAgentService() {
        return agentService;
    }

    public void setAgentService(AgentService agentService) {
        this.agentService = agentService;
    }

    public ImportedXMLMission getImportedXMLMission() {
        return importedXMLMission;
    }

    public void setImportedXMLMission(ImportedXMLMission importedXMLMission) {
        this.importedXMLMission = importedXMLMission;
    }



}
