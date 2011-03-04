package eu.scy.server.controllers;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.model.transfer.Portfolio;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.PedagogicalPlanELOService;
import eu.scy.core.roolo.RuntimeELOService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.feb.2011
 * Time: 07:08:21
 * To change this template use File | Settings | File Templates.
 */
public class ManageAssignedStudentController extends BaseController{

    private UserService userService;
    private PedagogicalPlanELOService pedagogicalPlanELOService;
    private MissionELOService missionELOService;
    private RuntimeELOService runtimeELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String userName = request.getParameter("username");
        String missionURI = request.getParameter("eloURI");
        User user = getUserService().getUser(userName);
        PedagogicalPlanTransfer pedagogicalPlanTransfer = getPedagogicalPlanELOService().getPedagogicalPlanForMission(getMissionELOService().getMissionSpecificationElo(missionURI));
        List runtimeElos = getRuntimeELOService().getRuntimeElosForUser(userName);
        List portfolios = new LinkedList();


        if(runtimeElos.size() > 0 ) {
            for (int i = 0; i < runtimeElos.size(); i++) {
                MissionRuntimeElo runtimeElo = (MissionRuntimeElo) runtimeElos.get(i);
                Portfolio portfolio = getMissionELOService().getPortfolio(runtimeElo);
                portfolios.add(portfolio);
            }

        }

        modelAndView.addObject("portfolios", portfolios);
        modelAndView.addObject("user", user);
        modelAndView.addObject("pedagogicalPlan", pedagogicalPlanTransfer);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public PedagogicalPlanELOService getPedagogicalPlanELOService() {
        return pedagogicalPlanELOService;
    }

    public void setPedagogicalPlanELOService(PedagogicalPlanELOService pedagogicalPlanELOService) {
        this.pedagogicalPlanELOService = pedagogicalPlanELOService;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public RuntimeELOService getRuntimeELOService() {
        return runtimeELOService;
    }

    public void setRuntimeELOService(RuntimeELOService runtimeELOService) {
        this.runtimeELOService = runtimeELOService;
    }
}
