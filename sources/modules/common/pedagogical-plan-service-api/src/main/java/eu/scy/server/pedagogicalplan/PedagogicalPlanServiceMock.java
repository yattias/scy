package eu.scy.server.pedagogicalplan;

import eu.scy.core.model.impl.pedagogicalplan.*;
import eu.scy.core.model.pedagogicalplan.*;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 10:17:27
 * To change this template use File | Settings | File Templates.
 */
public class PedagogicalPlanServiceMock implements PedagogicalPlanService {

    private List <PedagogicalPlanTemplate>pedagogicalPlanTemplates = new LinkedList<PedagogicalPlanTemplate>();

    public PedagogicalPlanServiceMock() {
        pedagogicalPlanTemplates.add(createPedagogicalPlanTemplate("Mission 1"));

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

    public List<PedagogicalPlan> getPedagogicalPlans() {
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

        LearningActivitySpace orientation = createLAS("LA(r)S Orientation");
        scenario.setLearningActivitySpace(orientation);
        Activity act1 = addActivity(orientation, "Identify goal states");
        AnchorELO anchorELO = createAnchorELO("A product you can be proud of!");
        act1.setAnchorELO(anchorELO);

        Activity act2 = addActivity(orientation, "Identify learning goals");
        AnchorELO anchorELO2 = createAnchorELO("A concept map concepting something really bad!");
        act2.setAnchorELO(anchorELO2);



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
