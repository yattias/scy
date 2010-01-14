package eu.scy.server.pedagogicalplan;

import eu.scy.core.model.auth.SessionInfo;
import eu.scy.core.model.impl.pedagogicalplan.*;
import eu.scy.core.model.pedagogicalplan.*;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 10:17:27
 * To change this template use File | Settings | File Templates.
 */
public class PedagogicalPlanServiceMock implements PedagogicalPlanService {

    private List <PedagogicalPlanTemplate>pedagogicalPlanTemplates = new LinkedList<PedagogicalPlanTemplate>();

    private List <Tool> tools = new ArrayList<Tool>();

    public PedagogicalPlanServiceMock() {

        tools.add(createTool("SCYmapper", "A mappingTool!"));
        tools.add(createTool("SCYSim", "A simtool"));
        tools.add(createTool("Nutpad", "A padder"));
        tools.add(createTool("SCYWriter", "A text pad"));
        tools.add(createTool("SCYOverallTool", "A general tool"));
        
        pedagogicalPlanTemplates.add(createPedagogicalPlanTemplate("Mission 1"));
    }

    private Tool createTool(String name, String description) {
        Tool tool = new ToolImpl();
        tool.setName(name);
        tool.setDescription(description);
        return tool;
    }

    @Override
    public SessionInfo login(String username, String password) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Tool> getTools() {
        return tools;
    }

    public List<Scenario> getScenarios() {
        System.out.println("GETTING SCENARIOS!");
        List scenarios = new LinkedList();
        for (int i = 0; i < pedagogicalPlanTemplates.size(); i++) {
            PedagogicalPlanTemplate pedagogicalPlanTemplate = pedagogicalPlanTemplates.get(i);
            System.out.println("CHECKING: " + pedagogicalPlanTemplate.getName());
            Scenario scenario = pedagogicalPlanTemplate.getScenario();
            if(!scenarios.contains(scenario))  {
                scenarios.add(scenario);
            }
        }
        return scenarios;
    }

    public List<PedagogicalPlanTemplate> getPedagogicalPlanTemplates() {
        return Collections.EMPTY_LIST;
    }


    private PedagogicalPlanTemplate createPedagogicalPlanTemplate(String name) {
        PedagogicalPlanTemplate template = new PedagogicalPlanTemplateImpl();
        template.setName(name);
        template.setScenario(createExplorationScenario());

        PedagogicalPlan plan = createPedagogicalPlan(template);
        plan.setScenario(createExplorationScenario());

        return template;
    }

    private Scenario createExplorationScenario() {
        Scenario scenario = new ScenarioImpl();
        scenario.setName("Exploration");
        scenario.setDescription("Exploration scenario that will make you shiver!");

        LearningActivitySpace orientation = createLAS("LAS Orientation");
        scenario.setLearningActivitySpace(orientation);
        Activity act1 = addActivity(orientation, "Identify goal states");
        createToolConfigurationForActivity(act1, 0);
        createToolConfigurationForActivity(act1, 2);
        AnchorELO anchorELO = createAnchorELO("A product you can be proud of!");
        act1.setAnchorELO(anchorELO);
        orientation.addAnchorELO(anchorELO);

        orientation.setAssessment(createAssessment("An assessment"));

        Activity act2 = addActivity(orientation, "Identify learning goals");
        createToolConfigurationForActivity(act2, 2);
        createToolConfigurationForActivity(act2, 1);
        AnchorELO anchorELO2 = createAnchorELO("A concept map concepting something really bad!");
        act2.setAnchorELO(anchorELO2);
        LearningActivitySpaceToolConfiguration conf = new SCYMapperConfiguration();

        act2.addLearningActivitySpaceToolConfiguration(conf);



        LearningActivitySpace conceptualization = createLAS("LAS Conceptualization");
        addActivity(conceptualization, "Build a model");
        addActivity(conceptualization, "Give and classify examples");

        anchorELO.setInputTo(conceptualization);

        LearningActivitySpace experiment = createLAS("LAS Experiment");
        addActivity(experiment, "Design an experimental procedure");
        addActivity(experiment, "Run experiment");
        addActivity(experiment, "Organize data");
        addActivity(experiment, "Interpret data");


        return scenario;
    }

    private Assessment createAssessment(String name) {
        Assessment assessment = new AssessmentImpl();
        assessment.setAssessmentStrategy(new PeerToPeerAssessmentStrategyImpl());
        return assessment;
    }

    private void createToolConfigurationForActivity(Activity activity, int toolIndex) {
        LearningActivitySpaceToolConfiguration config = new LearningActivitySpaceToolConfigurationImpl();
        config.setTool(tools.get(toolIndex));
        activity.addLearningActivitySpaceToolConfiguration(config);
    }

    private Activity addActivity(LearningActivitySpace las, String activityName) {
        Activity act = createActivity(activityName);
        
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

    private AnchorELO createAnchorELO(String name) {
        AnchorELO anchorElo = new AnchorELOImpl();
        anchorElo.setName(name);
        return anchorElo;
    }

    private PedagogicalPlan createPedagogicalPlan(PedagogicalPlanTemplate template) {
        PedagogicalPlan plan = new PedagogicalPlanImpl();
        template.addPedagogicalPlan(plan);
        plan.setScenario(template.getScenario());

        return plan;
    }
}
