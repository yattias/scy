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

        Object returnObject = null;
        if(missionRuntimeElo == null) {
            String missionURI = request.getParameter("missionURI");
            URI uri = null;
            try {
                uri = new URI(missionURI);
            } catch (URISyntaxException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
                portfolio = (Portfolio) xstream.fromXML(portfolioString);
            }
        }
        if (portfolio == null) {
            portfolio = new Portfolio();
            portfolio.setMissionName(missionRuntimeElo.getTitle());
            portfolio.setOwner(getCurrentUser(request).getUserDetails().getUsername());
            portfolio.setPortfolioStatus("NOT_SUBMITTED");
            portfolio.setMissionRuntimeURI(missionRuntimeElo.getUri().toString());
            //try {
            //portfolio.setMissionRuntimeURI(URLEncoder.encode(missionRuntimeElo.getUri().toString(), "UTF-8"));
            //} catch (UnsupportedEncodingException e) {
            //    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            //}
        }


        List elos = getMissionELOService().findElosFor(missionRuntimeElo.getUri().toString(), getCurrentUserName(request));
        List searchResult = new LinkedList();
        /**
         for (int i = 0; i < elos.size(); i++) {
         IELO elo = (IELO) elos.get(i);
         ScyElo scyElo = ScyElo.loadLastVersionElo(elo.getUri(), getMissionELOService());
         TransferElo telo = new TransferElo(scyElo);
         portfolio.getElos().add(telo);


         }  */


        LearningGoal learningGoal = new LearningGoal();
        learningGoal.setGoal("A generally considered general goal");

        //elo.getGeneralLearningGoals().add(learningGoal);

        //portfolio.getElos().add(elo);

        returnObject = portfolio;
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
