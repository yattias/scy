package eu.scy.server.controllers.json;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import eu.scy.core.AssignedPedagogicalPlanService;
import eu.scy.core.LASService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.ScenarioService;
import eu.scy.core.model.StudentUserDetails;
import eu.scy.core.model.impl.SCYStudentUserDetails;
import eu.scy.core.model.impl.pedagogicalplan.AnchorELOImpl;
import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceImpl;
import eu.scy.core.model.pedagogicalplan.AssignedPedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.runtime.RuntimeService;
import eu.scy.server.controllers.json.util.LearningActivitySpaceAnchorEloConnectionUtil;
import eu.scy.server.controllers.json.util.UserLearningActivitySpaceConnectionUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 27.mai.2010
 * Time: 13:24:05
 * To change this template use File | Settings | File Templates.
 */
public class RuntimeUserInfoJSON extends AbstractController {

    private AssignedPedagogicalPlanService assignedPedagogicalPlanService;
    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;
    private RuntimeService runtimeService;
    private LASService lasService;
    private ScenarioService scenarioService;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        PedagogicalPlan pedagogicalPlan = getPedagogicalPlanPersistenceService().getPedagogicalPlan(httpServletRequest.getParameter("model"));
        Scenario scenario = getScenarioService().getScenario(httpServletRequest.getParameter("scenario"));

        if (pedagogicalPlan != null) {
            List model = new LinkedList();

            List assignedPedagogicalPlans = getAssignedPedagogicalPlanService().getAssignedPedagogicalPlans(pedagogicalPlan);
            for (int i = 0; i < assignedPedagogicalPlans.size(); i++) {
                AssignedPedagogicalPlan assignedPedagogicalPlan = (AssignedPedagogicalPlan) assignedPedagogicalPlans.get(i);
                assignedPedagogicalPlan.getUser();
                StudentUserDetails copy = new SCYStudentUserDetails();
                StudentUserDetails original = (StudentUserDetails) assignedPedagogicalPlan.getUser().getUserDetails();
                copy.setUsername(original.getUsername());
                copy.setFirstName(original.getFirstName());
                copy.setLastName(original.getLastName());
                model.add(copy);

                String lasName = getRuntimeService().getCurrentLAS(assignedPedagogicalPlan.getUser());
                if(lasName != null) {
                    UserLearningActivitySpaceConnectionUtil userLearningActivitySpaceConnectionUtil = new  UserLearningActivitySpaceConnectionUtil();
                    userLearningActivitySpaceConnectionUtil.setLasName(lasName);
                    userLearningActivitySpaceConnectionUtil.setUserName(assignedPedagogicalPlan.getUser().getUserDetails().getUsername());
                    LearningActivitySpace learningActivitySpace = getLasService().getLearningActivitySpaceByName(lasName, scenario);
                    userLearningActivitySpaceConnectionUtil.setLasId(learningActivitySpace.getId());
                    userLearningActivitySpaceConnectionUtil.setPedagogicalPlanId(assignedPedagogicalPlan.getId());
                    model.add(userLearningActivitySpaceConnectionUtil);


                }



            }

            XStream xstream = new XStream(new JettisonMappedXmlDriver());
            httpServletResponse.setContentType("text/json");

            xstream.setMode(XStream.NO_REFERENCES);
            xstream.alias("model", LinkedList.class);
            xstream.alias("LearningActivitySpace", LearningActivitySpaceImpl.class);
            xstream.alias("AnchorELO", AnchorELOImpl.class);
            xstream.alias("Connection", LearningActivitySpaceAnchorEloConnectionUtil.class);
            xstream.alias("UserLASConnection", UserLearningActivitySpaceConnectionUtil.class);
            xstream.alias("StudentUserDetails", SCYStudentUserDetails .class);
            xstream.toXML(model, httpServletResponse.getWriter());

        }

        return null;

    }

    public AssignedPedagogicalPlanService getAssignedPedagogicalPlanService() {
        return assignedPedagogicalPlanService;
    }

    public void setAssignedPedagogicalPlanService(AssignedPedagogicalPlanService assignedPedagogicalPlanService) {
        this.assignedPedagogicalPlanService = assignedPedagogicalPlanService;
    }

    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }

    public RuntimeService getRuntimeService() {
        return runtimeService;
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public LASService getLasService() {
        return lasService;
    }

    public void setLasService(LASService lasService) {
        this.lasService = lasService;
    }

    public ScenarioService getScenarioService() {
        return scenarioService;
    }

    public void setScenarioService(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }
}
