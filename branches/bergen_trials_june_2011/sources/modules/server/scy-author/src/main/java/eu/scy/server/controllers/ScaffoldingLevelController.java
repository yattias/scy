package eu.scy.server.controllers;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.roolo.MissionELOService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 13.jun.2011
 * Time: 06:43:33
 * To change this template use File | Settings | File Templates.
 */
public class ScaffoldingLevelController extends BaseController{

    private MissionELOService missionELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

        URI uri = getURI(request.getParameter(ELO_URI));
        MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(uri, getMissionELOService());
        Integer globalScaffoldingLevel = getMissionELOService().getGlobalMissionScaffoldingLevel(missionSpecificationElo);

        String action = request.getParameter("action");
        if (action != null) {
            if (action.equals("increaseScaffoldingLevel")) {
                increaseScaffoldingLevel(request, response, modelAndView, missionSpecificationElo);
            }
        }

        modelAndView.addObject("agentLevels", getScaffoldingLevels());
        modelAndView.addObject("scaffoldingLevel", globalScaffoldingLevel);
        modelAndView.addObject("rooloServices", getMissionELOService());
        modelAndView.addObject("missionSpecificationTransporter", getMissionELOService().getWebSafeTransporter(missionSpecificationElo));

    }

    private List getScaffoldingLevels() {
        List agentLevels = new LinkedList();
        agentLevels.add("Low");
        agentLevels.add("Medium");
        agentLevels.add("High");
        return agentLevels;
    }

    private void increaseScaffoldingLevel(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView, ScyElo elo) {
        logger.info("INCREASING SCAFFOLDING LEVEL for " + elo.getTitle());
        MissionSpecificationElo missionSpecificationElo = (MissionSpecificationElo) elo;
        logger.info("LEVEL: " + getMissionELOService().getGlobalMissionScaffoldingLevel(missionSpecificationElo));
        getMissionELOService().setGlobalMissionScaffoldingLevel((MissionSpecificationElo) elo, (4 + getMissionELOService().getGlobalMissionScaffoldingLevel(missionSpecificationElo)));
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
