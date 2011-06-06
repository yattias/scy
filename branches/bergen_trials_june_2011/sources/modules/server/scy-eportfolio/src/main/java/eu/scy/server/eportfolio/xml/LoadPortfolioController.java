package eu.scy.server.eportfolio.xml;

import com.thoughtworks.xstream.XStream;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.model.transfer.LearningGoal;
import eu.scy.core.model.transfer.Portfolio;
import eu.scy.server.controllers.xml.MissionRuntimeEnabledXMLService;
import eu.scy.server.url.UrlInspector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jan.2011
 * Time: 12:48:46
 * To change this template use File | Settings | File Templates.
 */
public class LoadPortfolioController extends MissionRuntimeEnabledXMLService {

    private UrlInspector urlInspector;
    private MissionELOService missionELOService;


    @Override
    protected Object getObject(MissionRuntimeElo missionRuntimeElo, HttpServletRequest request, HttpServletResponse response) {

        if(missionRuntimeElo == null) {
            String missionURI = request.getParameter("missionURI");
            URI uri = null;
            try {
                uri = new URI(missionURI);
            } catch (URISyntaxException e) {
                e.printStackTrace();  
            }

            missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(uri, getMissionELOService());

        }

        logger.info("MISSION: " + missionRuntimeElo.getTitle());


        URI portfolioURI = missionRuntimeElo.getTypedContent().getEPortfolioEloUri();
        Portfolio portfolio = null;
        if (portfolioURI != null) {
            ScyElo ePortfolioElo = ScyElo.loadLastVersionElo(portfolioURI, getMissionELOService());
            String portfolioString = ePortfolioElo.getContent().getXmlString();
            logger.info("PORTFOLIO: " + portfolioString);
            if (portfolioString != null) {
                logger.info("Now returning the freakin portfolio object");
                return getXmlTransferObjectService().getObject(portfolioString);
                //return xstream.fromXML(portfolioString);
            }
        }

        if (portfolio == null) {
            portfolio = new Portfolio();
            portfolio.setMissionName(missionRuntimeElo.getTitle());
            portfolio.setOwner(getCurrentUser(request).getUserDetails().getUsername());
            portfolio.setPortfolioStatus("NOT_SUBMITTED");
            portfolio.setMissionRuntimeURI(missionRuntimeElo.getUri().toString());
            logger.info("Portfolio: " + portfolio.getMissionName() + " " + portfolio.getMissionRuntimeURI());
        }

        //logger.info("NO SHITTY WAY! THIS IS NOT GOING TO HAPPEN!");

        return portfolio;

    }



    public UrlInspector getUrlInspector() {
        return urlInspector;
    }

    public void setUrlInspector(UrlInspector urlInspector) {
        this.urlInspector = urlInspector;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
