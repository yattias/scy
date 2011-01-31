package eu.scy.server.eportfolio.xml;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.server.controllers.xml.transfer.PortfolioConfig;
import eu.scy.server.controllers.xml.transfer.PortfolioEffortScale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 31.jan.2011
 * Time: 11:10:25
 * To change this template use File | Settings | File Templates.
 */
public class PortfolioConfigController extends MissionRuntimeEnabledXMLService{
    @Override
    protected Object getObject(MissionRuntimeElo missionRuntimeElo, HttpServletRequest request, HttpServletResponse response) {
        PortfolioConfig portfolioConfig = new PortfolioConfig();
        portfolioConfig.setReflectionOnMissionQuestion("The most important thing I learned about funk is that it....");
        portfolioConfig.setReflectionOnCollaborationQuestion("Did your group agree to funk togehta immediately or after da rythm shook you?");
        portfolioConfig.setReflectionOnInquiryQuestion("Did you keep on groovin'?");
        portfolioConfig.addPortfolioEffortScalePoint(createPortfolioEffortScale("1", "BAD", "boo"));
        portfolioConfig.addPortfolioEffortScalePoint(createPortfolioEffortScale("2", "GOOD", "boo"));
        portfolioConfig.addPortfolioEffortScalePoint(createPortfolioEffortScale("3", "GROOVY", "boo"));
        portfolioConfig.addPortfolioEffortScalePoint(createPortfolioEffortScale("4", "FREAKYGROOVE", "boo"));

        return portfolioConfig;
    }


    private PortfolioEffortScale createPortfolioEffortScale(String score, String text, String url) {
        PortfolioEffortScale portfolioEffortScale = new PortfolioEffortScale();
        portfolioEffortScale.setScore(score);
        portfolioEffortScale.setText(text);
        portfolioEffortScale.setUrl(url);
        return portfolioEffortScale;
    }

}
