package eu.scy.server.eportfolio.xml.utilclasses;

import eu.scy.core.model.pedagogicalplan.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 06.okt.2010
 * Time: 05:48:10
 * To change this template use File | Settings | File Templates.
 */
public class CurrentMissionProgressOverview {

    private AssignedPedagogicalPlan currentAssignedPedagogicalPlan;
    private PedagogicalPlan pedagogicalPlan;
    private Scenario scenario;
    private List lasses = new LinkedList();
    private Mission mission;

    public AssignedPedagogicalPlan getCurrentAssignedPedagogicalPlan() {
        return currentAssignedPedagogicalPlan;
    }

    public void setCurrentAssignedPedagogicalPlan(AssignedPedagogicalPlan currentAssignedPedagogicalPlan) {
        this.currentAssignedPedagogicalPlan = currentAssignedPedagogicalPlan;
    }

    public void addLAS(LearningActivitySpace learningActivitySpace) {
        getLasses().add(learningActivitySpace);
    }

    public List getLasses() {
        return lasses;
    }

    public void setLasses(List lasses) {
        this.lasses = lasses;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public PedagogicalPlan getPedagogicalPlan() {
        return pedagogicalPlan;
    }

    public void setPedagogicalPlan(PedagogicalPlan pedagogicalPlan) {
        this.pedagogicalPlan = pedagogicalPlan;
    }
}
