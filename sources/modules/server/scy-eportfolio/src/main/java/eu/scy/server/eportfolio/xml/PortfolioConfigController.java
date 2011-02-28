package eu.scy.server.eportfolio.xml;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.core.model.transfer.EloReflection;
import eu.scy.core.model.transfer.PortfolioConfig;
import eu.scy.core.model.transfer.PortfolioEffortScale;
import eu.scy.core.model.transfer.Tab;
import eu.scy.server.controllers.xml.MissionRuntimeEnabledXMLService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 31.jan.2011
 * Time: 11:10:25
 * To change this template use File | Settings | File Templates.
 */
public class PortfolioConfigController extends MissionRuntimeEnabledXMLService {
    @Override
    protected Object getObject(MissionRuntimeElo missionRuntimeElo, HttpServletRequest request, HttpServletResponse response) {
        PortfolioConfig portfolioConfig = new PortfolioConfig();

        EloReflection eloReflection = new EloReflection();
        eloReflection.setEloreflectioninquiryquestion("How helpful was building a concept map in identifying the main concepts related to reducing global CO2");
        portfolioConfig.setEloReflection(eloReflection);

        portfolioConfig.addPortfolioReflectionTab(createTab("Reflection on Mission", "The most important things I learned about CO2 neural houses are...", "text"));
        portfolioConfig.addPortfolioReflectionTab(createTab("Reflection on Collaboration", "Did your group agreed about how to work before you started and were there any problems during the work?", "text"));
        portfolioConfig.addPortfolioReflectionTab(createTab("Reflection on Mission", "The most important things I learned about CO2 neural houses are...", "text"));
        portfolioConfig.addPortfolioReflectionTab(createTab("Reflection on Inquiry", "Did you keep the hypotheses that you created early in the inquiry or did you change it?", "text"));
        portfolioConfig.addPortfolioReflectionTab(createTab("Reflection on Effort", "Are you satisfied with your own work on this Mission?", "slider"));

        return portfolioConfig;
    }

    private Tab createTab(String title, String question, String type) {
        Tab returnTab = new Tab();
        returnTab.setTitle(title);
        returnTab.setQuestion(question);
        returnTab.setType(type);

        return returnTab;
    }


    private PortfolioEffortScale createPortfolioEffortScale(String score, String text, String url) {
        PortfolioEffortScale portfolioEffortScale = new PortfolioEffortScale();
        portfolioEffortScale.setScore(score);
        portfolioEffortScale.setText(text);
        portfolioEffortScale.setUrl(url);
        return portfolioEffortScale;
    }

}