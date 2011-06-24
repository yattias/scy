package eu.scy.server.controllers;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.PedagogicalPlanELOService;
import eu.scy.server.util.TransferObjectServiceCollection;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 25.mai.2011
 * Time: 20:23:59
 * To change this template use File | Settings | File Templates.
 */
public class MissionPlannerController extends BaseController{

    private MissionELOService missionELOService;
    private PedagogicalPlanELOService pedagogicalPlanELOService;
    private TransferObjectServiceCollection transferObjectServiceCollection;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        MissionSpecificationElo missionSpecificationElo = getMissionSpecificationElo(request);

        String action = request.getParameter("action");
        if(action != null) {
            if(action.equals("clearMissionPlanning")) clearMissionPlanning(missionSpecificationElo);
            else if(action.equals("initializeMissionPlanning")) initializeMissionPlanning(missionSpecificationElo);
            else if(action.equals("reinitializePedagogicalPlan")) {
                clearMissionPlanning(missionSpecificationElo);
                initializeMissionPlanning(missionSpecificationElo);
            }
        }

        modelAndView.addObject("pedagogicalPlan", getPedagogicalPlanELOService().getPedagogicalPlanForMission(missionSpecificationElo));
        modelAndView.addObject("transferObjectServiceCollection", getTransferObjectServiceCollection());
        modelAndView.addObject("eloURI", getEncodedUri(request.getParameter(ELO_URI)));
    }

    private void initializeMissionPlanning(MissionSpecificationElo missionSpecificationElo) {
        getPedagogicalPlanELOService().initializePedagogicalPlanWithLasses(missionSpecificationElo);
    }

    private void clearMissionPlanning(MissionSpecificationElo missionSpecificationElo) {
        try {
            getPedagogicalPlanELOService().clearMissionPlanning(missionSpecificationElo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private MissionSpecificationElo getMissionSpecificationElo(HttpServletRequest request) {
        String uriString = request.getParameter(ELO_URI);
        if(uriString != null) {
            try {
                URI uri = new URI(uriString);
                MissionSpecificationElo missionSpecificationElo= MissionSpecificationElo.loadLastVersionElo(uri, getMissionELOService());
                return missionSpecificationElo;
            } catch (URISyntaxException e) {
                logger.error(e.getMessage(), e);
            }
        }

        throw new RuntimeException("DId not manage to load mission specification! Maybe request does not contain the eloURI?");
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public PedagogicalPlanELOService getPedagogicalPlanELOService() {
        return pedagogicalPlanELOService;
    }

    public void setPedagogicalPlanELOService(PedagogicalPlanELOService pedagogicalPlanELOService) {
        this.pedagogicalPlanELOService = pedagogicalPlanELOService;
    }

    public TransferObjectServiceCollection getTransferObjectServiceCollection() {
        return transferObjectServiceCollection;
    }

    public void setTransferObjectServiceCollection(TransferObjectServiceCollection transferObjectServiceCollection) {
        this.transferObjectServiceCollection = transferObjectServiceCollection;
    }
}
