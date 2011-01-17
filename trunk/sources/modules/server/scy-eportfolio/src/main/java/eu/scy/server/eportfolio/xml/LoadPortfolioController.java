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
import eu.scy.server.eportfolio.xml.utilclasses.LearningGoal;
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
public class LoadPortfolioController extends MissionRuntimeEnabledXMLService {

    private UrlInspector urlInspector;
    private MissionELOService missionELOService;


    @Override
    protected Object getObject(MissionRuntimeElo missionRuntimeElo, HttpServletRequest request, HttpServletResponse response) {

        Object returnObject = null;

        logger.info("MISSION: " + missionRuntimeElo.getTitle());

        Portfolio portfolio = new Portfolio();
        portfolio.setMissionName(missionRuntimeElo.getTitle());
        portfolio.setOwner(getCurrentUser(request).getUserDetails().getUsername());
        portfolio.setPortfolioStatus("NOT_SUBMITTED");
        portfolio.setAssessed(false);
        portfolio.setReflectionCollaboration("HUH DUDE; REFLECTION is for professors!");
        portfolio.setReflectionEffort("NO GOOD MAN!");
        portfolio.setReflectionInquiry("MUUU");
        portfolio.setReflectionMission("WOha reflect reflect");
        portfolio.setAssessmentPortfolioComment("A comment from me");
        portfolio.setAssessmentPortfolioRating("TOO GOOD TO BE TRUE (6/6)");

        TransferElo elo = new TransferElo(missionRuntimeElo);

        LearningGoal learningGoal = new LearningGoal();
        learningGoal.setGoal("A generally considered general goal");

        //elo.getGeneralLearningGoals().add(learningGoal);

        portfolio.getElos().add(elo);

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
