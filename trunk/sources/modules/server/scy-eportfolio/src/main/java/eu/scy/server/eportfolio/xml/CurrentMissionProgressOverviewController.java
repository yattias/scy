package eu.scy.server.eportfolio.xml;

import com.thoughtworks.xstream.XStream;
import eu.scy.core.AssignedPedagogicalPlanService;
import eu.scy.core.LASService;
import eu.scy.core.model.FileData;
import eu.scy.core.model.FileRef;
import eu.scy.core.model.impl.FileDataImpl;
import eu.scy.core.model.impl.FileRefImpl;
import eu.scy.core.model.impl.pedagogicalplan.*;
import eu.scy.core.model.pedagogicalplan.*;
import eu.scy.server.controllers.xml.XMLStreamerController;
import eu.scy.server.eportfolio.xml.utilclasses.CurrentMissionProgressOverview;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 06.okt.2010
 * Time: 05:46:58
 * To change this template use File | Settings | File Templates.
 */
public class CurrentMissionProgressOverviewController extends XMLStreamerController{

    private AssignedPedagogicalPlanService assignedPedagogicalPlanService;
    private LASService lasService;

    /*@Override
    protected int getXStreamMode() {
        return XStream.NO_REFERENCES;
    } */

    @Override
    protected void addAliases(XStream xStream) {
        super.addAliases(xStream);    //To change body of overridden methods use File | Settings | File Templates.

    }

    @Override
    protected void omitFields(XStream xStream) {
        super.omitFields(xStream);   
        xStream.omitField(eu.scy.core.model.impl.pedagogicalplan.AssignedPedagogicalPlanImpl.class, "user");
        xStream.omitField(eu.scy.core.model.impl.pedagogicalplan.AssignedPedagogicalPlanImpl.class, "pedagogicalPlan");

        xStream.omitField(PedagogicalPlanImpl.class, "mission");
        xStream.omitField(PedagogicalPlanImpl.class, "scenario");

        xStream.omitField(LearningActivitySpaceImpl.class, "activities");
        xStream.omitField(LearningActivitySpace.class, "activities");
        omitGeneratedShit(xStream, ScenarioImpl.class);
        omitGeneratedShit(xStream, Scenario.class);
        omitGeneratedShit(xStream, Mission.class);
        omitGeneratedShit(xStream, MissionImpl.class);
        omitGeneratedShit(xStream, LearningActivitySpace.class);
        omitGeneratedShit(xStream, LearningActivitySpaceImpl.class);
        omitGeneratedShit(xStream, Assessment.class);
        omitGeneratedShit(xStream, AssessmentImpl.class);
        omitGeneratedShit(xStream, FileRef.class);
        omitGeneratedShit(xStream, FileRefImpl.class);
        omitGeneratedShit(xStream, PedagogicalPlanTemplate.class);
        omitGeneratedShit(xStream, PedagogicalPlanTemplateImpl.class);
        omitGeneratedShit(xStream, FileData.class);
        omitGeneratedShit(xStream, FileDataImpl.class);

        xStream.omitField(LearningActivitySpace.class, "participatesIn");
        xStream.omitField(LearningActivitySpaceImpl.class, "participatesIn");
        xStream.omitField(LearningActivitySpace.class, "assessment");
        xStream.omitField(LearningActivitySpaceImpl.class, "assessment");



    }


    private void omitGeneratedShit(XStream xStream, Class clazz) {
        xStream.omitField(clazz, "getIdentifierMethodClass");
        xStream.omitField(clazz, "setIdentifierMethodClass");
        xStream.omitField(clazz, "getIdentifierMethodName");
        xStream.omitField(clazz, "setIdentifierMethodName");
        xStream.omitField(clazz, "setIdentifierMethodParams");

    }

    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        CurrentMissionProgressOverview currentMissionProgressOverview = new CurrentMissionProgressOverview();
        currentMissionProgressOverview.setCurrentAssignedPedagogicalPlan(getAssignedPedagogicalPlanService().getCurrentAssignedPedagogicalPlan(getCurrentUser(request)));

        PedagogicalPlan pedagogicalPlan = getAssignedPedagogicalPlanService().getCurrentAssignedPedagogicalPlan(getCurrentUser(request)).getPedagogicalPlan();

        Scenario scenario = pedagogicalPlan.getScenario();
        List learningActivitySpaces = getLasService().getAllLearningActivitySpacesForScenario(scenario);
        for (int i = 0; i < learningActivitySpaces.size(); i++) {
            LearningActivitySpace learningActivitySpace = (LearningActivitySpace) learningActivitySpaces.get(i);
            currentMissionProgressOverview.addLAS(learningActivitySpace);
        }

        return currentMissionProgressOverview;
    }

    public AssignedPedagogicalPlanService getAssignedPedagogicalPlanService() {
        return assignedPedagogicalPlanService;
    }

    public void setAssignedPedagogicalPlanService(AssignedPedagogicalPlanService assignedPedagogicalPlanService) {
        this.assignedPedagogicalPlanService = assignedPedagogicalPlanService;
    }

    public LASService getLasService() {
        return lasService;
    }

    public void setLasService(LASService lasService) {
        this.lasService = lasService;
    }
}
