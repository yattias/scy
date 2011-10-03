package eu.scy.server.controllers;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.mission.impl.BasicMissionAnchor;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.PedagogicalPlanELOService;
import eu.scy.server.AnchorEloInfoWrapper;
import eu.scy.server.util.TransferObjectServiceCollection;
import org.springframework.web.servlet.ModelAndView;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 25.mai.2011
 * Time: 20:23:59
 * To change this template use File | Settings | File Templates.
 */
public class MissionPlannerController extends BaseController {

    private MissionELOService missionELOService;
    private PedagogicalPlanELOService pedagogicalPlanELOService;
    private TransferObjectServiceCollection transferObjectServiceCollection;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        MissionSpecificationElo missionSpecificationElo = getMissionSpecificationElo(request);
        if (missionSpecificationElo == null) {
            ScyElo scyElo = ScyElo.loadLastVersionElo(getURI(request.getParameter("anchorEloUri")), getMissionELOService());
            URI miUri = scyElo.getMissionSpecificationEloUri();
            missionSpecificationElo = MissionSpecificationElo.loadLastVersionElo(miUri, getMissionELOService());
        }

        String action = request.getParameter("action");
        if (action != null) {
            if (action.equals("clearMissionPlanning")) clearMissionPlanning(missionSpecificationElo);
            else if (action.equals("initializeMissionPlanning")) initializeMissionPlanning(missionSpecificationElo);
            else if (action.equals("reinitializePedagogicalPlan")) {
                clearMissionPlanning(missionSpecificationElo);
                initializeMissionPlanning(missionSpecificationElo);
            } else if (action.equals("deleteFeedback")) {
                getMissionELOService().deleteAllFeedbackFeedback();
            } else if (action.equals("setAnchorEloObligatory")) {
                setAnchorEloObligatory(request, missionSpecificationElo);
            }
        }

        //modelAndView.addObject("pedagogicalPlan", getPedagogicalPlanELOService().getPedagogicalPlanForMission(missionSpecificationElo));
        //modelAndView.addObject("transferObjectServiceCollection", getTransferObjectServiceCollection());
        modelAndView.addObject("eloURI", request.getParameter(ELO_URI));


        List<BasicMissionAnchor> anchorElos = getMissionELOService().getAnchorELOs(missionSpecificationElo);
        List anchorEloWrappers = new LinkedList();
        for (int i = 0; i < anchorElos.size(); i++) {
            BasicMissionAnchor basicMissionAnchor = anchorElos.get(i);
            ScyElo scyElo = anchorElos.get(i).getScyElo();
            AnchorEloInfoWrapper anchorEloInfoWrapper = new AnchorEloInfoWrapper();
            anchorEloInfoWrapper.setAnchorElo(scyElo);
            anchorEloInfoWrapper.setName(scyElo.getTitle());
            anchorEloInfoWrapper.setLasName(basicMissionAnchor.getLas().getTitle());
            if (scyElo.getObligatoryInPortfolio() == null) {
                anchorEloInfoWrapper.setObligatoryInPortfolio(false);
            } else {
                anchorEloInfoWrapper.setObligatoryInPortfolio(scyElo.getObligatoryInPortfolio());
            }

            anchorEloWrappers.add(anchorEloInfoWrapper);
        }

        modelAndView.addObject("anchorEloWrappers", anchorEloWrappers);

    }

    private void setAnchorEloObligatory(HttpServletRequest request, MissionSpecificationElo missionSpecificationElo) {
        URI uri = getURI(request.getParameter("anchorEloUri"));
        ScyElo scyElo = ScyElo.loadElo(uri, getMissionELOService());
        boolean selected = false;
        if(scyElo.getObligatoryInPortfolio() != null) {
            selected = scyElo.getObligatoryInPortfolio();    
        }



        IMetadata templateTrueMetadata = getMissionELOService().getELOFactory().createMetadata();
        IMetadataKey templateKey = getMissionELOService().getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.OBLIGATORY_IN_PORTFOLIO);

        Boolean newValue = new Boolean(!selected);

        templateTrueMetadata.getMetadataValueContainer(templateKey).setValue(newValue.toString());
        getMissionELOService().getRepository().addMetadata(uri, templateTrueMetadata);


        request.getSession().setAttribute(OBLIGATORY_ANCHOR_ELOS_SESSION_PARAMETER, null);

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
        if (uriString != null) {
            try {
                URI uri = new URI(uriString);
                MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadLastVersionElo(uri, getMissionELOService());
                return missionSpecificationElo;
            } catch (URISyntaxException e) {
                logger.error(e.getMessage(), e);
            }
        }

        return null;
        //throw new RuntimeException("DId not manage to load mission specification! Maybe request does not contain the eloURI?");
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
