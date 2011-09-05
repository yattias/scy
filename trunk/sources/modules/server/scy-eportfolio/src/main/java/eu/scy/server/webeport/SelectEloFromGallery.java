package eu.scy.server.webeport;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.Portfolio;
import eu.scy.core.model.transfer.SearchResultTransfer;
import eu.scy.core.model.transfer.TransferElo;
import eu.scy.core.roolo.MissionELOService;
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
 * Time: 14:21:07
 * To change this template use File | Settings | File Templates.
 */
public class SelectEloFromGallery extends BaseController {

    private MissionELOService missionELOService;
    private XMLTransferObjectService xmlTransferObjectService;


    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

        URI anchorEloURI = getURI(request.getParameter("anchorEloURI"));
        URI missionRuntimeURI = getURI(request.getParameter("missionRuntimeURI"));
        ScyElo anchorElo = ScyElo.loadLastVersionElo(anchorEloURI, getMissionELOService());
        TransferElo anchorEloTransferElo = new TransferElo(anchorElo);
        MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(missionRuntimeURI, getMissionELOService());
        Portfolio portfolio = getMissionELOService().getPortfolio(missionRuntimeElo, getCurrentUserName(request));
        TransferElo elo = portfolio.getEloForAnchroElo(anchorEloTransferElo);

        if(elo != null) {
            modelAndView.setViewName("forward:editEloReflections.html");
        } else {
            String technicalFormat = anchorElo.getTechnicalFormat();
            String encodedAnchorEloURI = getEncodedUri(anchorElo.getUri().toString());

            List<SearchResultTransfer> searchResults = getMissionELOService().getSearchResultTransfers(getMissionELOService().getElosWithTechnicalType(technicalFormat, getCurrentUserName(request)), request.getLocale());
            modelAndView.addObject("elos", searchResults);
            modelAndView.addObject("encodedAnchorEloURI", encodedAnchorEloURI);
            modelAndView.addObject("missionRuntimeURI", getEncodedUri(missionRuntimeURI.toString()));
        }
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

}
