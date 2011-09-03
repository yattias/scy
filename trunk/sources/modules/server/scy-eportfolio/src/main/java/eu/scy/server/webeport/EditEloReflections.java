package eu.scy.server.webeport;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.LearningGoal;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.model.transfer.TransferElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.PedagogicalPlanELOService;
import eu.scy.core.roolo.RuntimeELOService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;
import roolo.search.ISearchResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 31.aug.2011
 * Time: 21:25:38
 * To change this template use File | Settings | File Templates.
 */
public class EditEloReflections extends BaseController {

    private MissionELOService missionELOService;
    private XMLTransferObjectService xmlTransferObjectService;
    private RuntimeELOService runtimeELOService;
    private PedagogicalPlanELOService pedagogicalPlanELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        URI eloURI = getURI(request.getParameter(ELO_URI));
        ScyElo elo = ScyElo.loadLastVersionElo(eloURI, getMissionELOService());
        TransferElo transferElo = new TransferElo(elo);

        logger.info("QUERY: " + request.getQueryString());
        logger.info("MISSIONURI: " + request.getParameter("missionRuntimeURI"));

        URI missionRuntimeURI = getURI(request.getParameter("missionRuntimeURI"));


        URI anchorEloURI = getURI(request.getParameter("anchorEloURI"));
        ScyElo anchorElo = ScyElo.loadLastVersionElo(anchorEloURI, getMissionELOService());
        TransferElo anchorEloTransfer = new TransferElo(anchorElo);

        modelAndView.addObject("elo", transferElo);
        modelAndView.addObject("anchorElo", anchorEloTransfer);

        List<ISearchResult> runtimeElos = getRuntimeELOService().getRuntimeElosForUser(getCurrentUserName(request));
        PedagogicalPlanTransfer pedagogicalPlanTransfer = null;
        if(runtimeElos.size() >0 ) {
            ISearchResult searchResult = runtimeElos.get(0);
            pedagogicalPlanTransfer = getPedagogicalPlanELOService().getPedagogicalPlanForMissionRuntimeElo(searchResult.getUri().toString());
        }

        List <LearningGoal> generalLearningGooals = pedagogicalPlanTransfer.getAssessmentSetup().getGeneralLearningGoals();
        List <LearningGoal> specificLearningGooals = pedagogicalPlanTransfer.getAssessmentSetup().getSpecificLearningGoals();

        modelAndView.addObject("generalLearningGoals", generalLearningGooals);
        modelAndView.addObject("specificLearningGoals", specificLearningGooals);
        modelAndView.addObject("missionRuntimeURI", getEncodedUri(missionRuntimeURI.toString()));
        modelAndView.addObject(ELO_URI, getEncodedUri(eloURI.toString()));
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public XMLTransferObjectService getXmlTransferObjectService() {
        return xmlTransferObjectService;
    }

    public void setXmlTransferObjectService(XMLTransferObjectService xmlTransferObjectService) {
        this.xmlTransferObjectService = xmlTransferObjectService;
    }

    public RuntimeELOService getRuntimeELOService() {
        return runtimeELOService;
    }

    public void setRuntimeELOService(RuntimeELOService runtimeELOService) {
        this.runtimeELOService = runtimeELOService;
    }

    public PedagogicalPlanELOService getPedagogicalPlanELOService() {
        return pedagogicalPlanELOService;
    }

    public void setPedagogicalPlanELOService(PedagogicalPlanELOService pedagogicalPlanELOService) {
        this.pedagogicalPlanELOService = pedagogicalPlanELOService;
    }
}
