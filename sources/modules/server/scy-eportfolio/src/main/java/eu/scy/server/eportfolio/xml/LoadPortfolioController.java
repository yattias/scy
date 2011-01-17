package eu.scy.server.eportfolio.xml;

import com.thoughtworks.xstream.XStream;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.xml.XMLStreamerController;
import eu.scy.server.controllers.xml.transfer.Portfolio;
import eu.scy.server.controllers.xml.transfer.PortfolioContainer;
import eu.scy.server.controllers.xml.transfer.TransferElo;
import eu.scy.server.url.UrlInspector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jan.2011
 * Time: 12:48:46
 * To change this template use File | Settings | File Templates.
 */
public class LoadPortfolioController extends XMLStreamerController {

    private UrlInspector urlInspector;
    private MissionELOService missionELOService;


    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {

        Object returnObject = null;

        try {
            String missionURI = request.getParameter("missionURI");
            logger.info("MIssionURI: " + missionURI);
            if(missionURI != null) {
                missionURI = URLDecoder.decode(missionURI, "UTF-8");

                ScyElo scyElo = (ScyElo) getUrlInspector().instpectRequest(request, httpServletResponse);
                MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadElo(new URI(missionURI), getMissionELOService());
                URI portfolioELO = missionRuntimeElo.getTypedContent().getEPortfolioEloUri();
                logger.info("DID I GET A PORTFOLIO NOW??: " + portfolioELO);
                if(portfolioELO != null) {
                    ScyElo porElo = ScyElo.loadLastVersionElo(portfolioELO, getMissionELOService());
                    logger.info("XML: " + porElo.getElo().getContent().getXmlString());

                }

                logger.info("MISSION: " + missionRuntimeElo.getTitle());

                Portfolio portfolio = new Portfolio();
                portfolio.setMissionName(missionRuntimeElo.getTitle());
                portfolio.setOwner(getCurrentUser(request).getUserDetails().getUsername());
                portfolio.setPortfolioStatus("NOT_SUBMITTED");
                portfolio.setAssessed(false);
                portfolio.setReflectionCollaboration("HUH DUDE; REFLECTION is for professors!");
                portfolio.setReflectionEffort("NO GOOD MAN!");
                portfolio.setReflectionInquiry("MUUU");

                returnObject = portfolio;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnObject;
    }

    @Override
    protected void addAliases(XStream xStream) {
        super.addAliases(xStream);    //To change body of overridden methods use File | Settings | File Templates.

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
