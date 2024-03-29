package eu.scy.server.controllers;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.model.transfer.Portfolio;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.PedagogicalPlanELOService;
import eu.scy.core.roolo.RuntimeELOService;
import org.springframework.web.servlet.ModelAndView;
import roolo.search.ISearchResult;

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
        String action = request.getParameter("action");
        User user = getUserService().getUser(userName);
        PedagogicalPlanTransfer pedagogicalPlanTransfer = getPedagogicalPlanELOService().getPedagogicalPlanForMission(getMissionELOService().getMissionSpecificationElo(missionURI));


        if(action != null) {
            if(action.equals("clearPortfolios")) clearPortfolios(userName);
        }

        List <ISearchResult> runtimeEloSearchResult = getRuntimeELOService().getRuntimeElosForUser(userName);
        List portfolios = new LinkedList();
        if(runtimeEloSearchResult.size() > 0 ) {
            for (int i = 0; i < runtimeEloSearchResult.size(); i++) {
                MissionRuntimeElo runtimeElo = MissionRuntimeElo.loadLastVersionElo(runtimeEloSearchResult.get(i).getUri(), getMissionELOService());
                Portfolio portfolio = getMissionELOService().getPortfolio(runtimeElo, userName);
                portfolios.add(portfolio);
            }

        }



        modelAndView.addObject("portfolios", portfolios);
        modelAndView.addObject("user", user);
        modelAndView.addObject("pedagogicalPlan", pedagogicalPlanTransfer);
        modelAndView.addObject(ELO_URI, getEncodedUri(missionURI) );
    }

    private void clearPortfolios(String userName) {
        List <ISearchResult> runtimeEloSearchResult = getRuntimeELOService().getRuntimeElosForUser(userName);
        if(runtimeEloSearchResult.size() > 0 ) {
            for (int i = 0; i < runtimeEloSearchResult.size(); i++) {
                MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(runtimeEloSearchResult.get(i).getUri(), getMissionELOService());
                ScyElo portfolioElo = ScyElo.loadLastVersionElo(missionRuntimeElo.getTypedContent().getEPortfolioEloUri(), getMissionELOService());
                portfolioElo.getContent().setXmlString("");
                portfolioElo.updateElo();
            }
        }
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
