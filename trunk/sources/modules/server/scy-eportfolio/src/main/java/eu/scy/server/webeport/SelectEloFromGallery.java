package eu.scy.server.webeport;

import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.SearchResultTransfer;
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

        URI uri = getURI(request.getParameter(ELO_URI));
        URI missionRuntimeURI = getURI(request.getParameter("missionRuntimeURI"));
        ScyElo anchorElo = ScyElo.loadLastVersionElo(uri, getMissionELOService());

        String technicalFormat = anchorElo.getTechnicalFormat();
        String encodedAnchorEloURI = getEncodedUri(anchorElo.getUri().toString());

        logger.info("TRYING TO LOAD ELOS WITH TECHNICAL FORMAT: " + technicalFormat);
        logger.info("LOADED MISSION URI: " + missionRuntimeURI.toString());

        List<SearchResultTransfer> searchResults = getMissionELOService().getSearchResultTransfers(getMissionELOService().getElosWithTechnicalType(technicalFormat, getCurrentUserName(request)), request.getLocale());
        modelAndView.addObject("elos", searchResults);
        modelAndView.addObject("encodedAnchorEloURI", encodedAnchorEloURI);
        modelAndView.addObject("missionRuntimeURI", getEncodedUri(missionRuntimeURI.toString()));

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
