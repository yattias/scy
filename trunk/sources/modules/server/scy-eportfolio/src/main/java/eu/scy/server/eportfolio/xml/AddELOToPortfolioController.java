package eu.scy.server.eportfolio.xml;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.model.transfer.TransferElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.model.transfer.Portfolio;
import eu.scy.server.controllers.xml.MissionRuntimeEnabledXMLService;
import eu.scy.server.controllers.xml.ServiceStatusMessage;
import eu.scy.server.controllers.xml.ServiceExceptionMessage;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jan.2011
 * Time: 14:31:38
 * To change this template use File | Settings | File Templates.
 */
public class AddELOToPortfolioController extends MissionRuntimeEnabledXMLService {

    private MissionELOService missionELOService;

    @Override
    protected Object getObject(MissionRuntimeElo missionRuntimeElo, HttpServletRequest request, HttpServletResponse response) {
            String missionURI = request.getParameter("missionURI");
            String eloURI = request.getParameter("eloURI");


            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("forward:/app/webeport/editEloReflections.html");
            return modelAndView;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
