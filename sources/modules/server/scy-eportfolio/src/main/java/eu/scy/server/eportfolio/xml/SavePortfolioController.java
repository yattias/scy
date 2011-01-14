package eu.scy.server.eportfolio.xml;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jan.2011
 * Time: 14:40:46
 * To change this template use File | Settings | File Templates.
 */
public class SavePortfolioController extends BaseController {

    private MissionELOService missionELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String missionURI = request.getParameter("missionURI");
        String xmlContent = request.getParameter("xmlContent");

        logger.info("THE MISSION :" + missionURI + " will be updated with: " + xmlContent);


        try {
            logger.info("MIssionURI: " + missionURI);
            if (missionURI != null) {
                missionURI = URLDecoder.decode(missionURI, "UTF-8");

                ScyElo scyElo = (ScyElo) getUrlInspector().instpectRequest(request, response);
                MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadElo(new URI(missionURI), getMissionELOService());
                URI portfolioELO = missionRuntimeElo.getTypedContent().getEPortfolioEloUri();
                logger.info("DID I GET A PORTFOLIO NOW??: " + portfolioELO);

                ScyElo portfolioElo = ScyElo.loadLastVersionElo(portfolioELO, getMissionELOService());
                portfolioElo.getContent().setXmlString(xmlContent);
                portfolioElo.updateElo();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
