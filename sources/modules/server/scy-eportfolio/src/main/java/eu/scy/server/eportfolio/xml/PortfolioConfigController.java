package eu.scy.server.eportfolio.xml;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.model.transfer.*;
import eu.scy.server.controllers.xml.MissionRuntimeEnabledXMLService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.List;

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
        logger.info("GEtting portfolio config..." + missionRuntimeElo);

        MissionSpecificationElo missionSpecificationElo = null;

        String missionURI = request.getParameter("missionURI");
        if(missionURI != null) {
            URI uri = null;
            try {
                missionURI = URLDecoder.decode(missionURI, "utf-8");
                uri = new URI(missionURI);

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(uri, getMissionELOService());
            missionSpecificationElo = getMissionELOService().getMissionSpecificationELOForRuntume(missionRuntimeElo);
        }

        //missionSpecificationElo = getMissionELOService().getMissionSpecificationELOForRuntume(missionRuntimeElo);
        PedagogicalPlanTransfer pedagogicalPlanTransfer = getPedagogicalPlanTransfer(missionSpecificationElo);
        PortfolioConfig portfolioConfig = new PortfolioConfig();
        if (pedagogicalPlanTransfer != null) {
            List tabs = pedagogicalPlanTransfer.getAssessmentSetup().getReflectionTabs();
            for (int i = 0; i < tabs.size(); i++) {
                Tab tab = (Tab) tabs.get(i);
                portfolioConfig.addPortfolioReflectionTab(tab);
            }
        }


        EloReflection eloReflection = new EloReflection();
        eloReflection.setEloreflectioninquiryquestion("How helpful was building a concept map in identifying the main concepts related to reducing global CO2");
        portfolioConfig.setEloReflection(eloReflection);

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
