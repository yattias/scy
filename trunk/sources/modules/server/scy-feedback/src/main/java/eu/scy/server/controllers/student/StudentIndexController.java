package eu.scy.server.controllers.student;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.User;
import eu.scy.core.model.transfer.NewestElos;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.BaseController;
import eu.scy.core.model.transfer.Portfolio;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.des.2010
 * Time: 10:14:37
 * To change this template use File | Settings | File Templates.
 */
public class StudentIndexController extends BaseController {

    private MissionELOService missionELOService;
    private XMLTransferObjectService xmlTransferObjectService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        MissionRuntimeElo missionRuntimeElo = (MissionRuntimeElo) getScyElo();
        Portfolio portfolio = getMissionELOService().getPortfolio(missionRuntimeElo, getCurrentUserName(request));
        if (portfolio != null) {
            portfolio.unCdatify();
            modelAndView.addObject("portfolio", portfolio);
            modelAndView.addObject("portfolioStatus", portfolio.getPortfolioStatus());
            logger.info("SET PORTFOLIO: " + portfolio.getPortfolioStatus());
        } else {
            logger.info("PORTFOLIO IS NULL!!");
        }

        String user = missionRuntimeElo.getUserRunningMission();
        String locale = null;
        List<Locale> languages = missionRuntimeElo.getContent().getLanguages();
        if (!languages.isEmpty()) {
            Locale language = languages.get(0);
            locale = language.getLanguage();
        }
        String uri = missionRuntimeElo.getUri().toString();
        try {
            uri = URLEncoder.encode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            logger.error("Could not encode URL for mission ELO" + uri, e1);
        }
        String jnlpUrl = "scy-lab.jnlp?username=" + user + "&mission=" + uri + "&locale=" + locale;

        NewestElos myElosWithFeedback = getMissionELOService().getMyElosWithFeedback(missionRuntimeElo, getCurrentUserName(request));
        NewestElos elosWhereIHaveProvidedFeedback = getMissionELOService().getFeedbackElosWhereIHaveContributed(missionRuntimeElo, getCurrentUserName(request));

        MissionSpecificationElo missionSpecificationElo = getMissionELOService().getMissionSpecificationELOForRuntume(missionRuntimeElo);
        URI descriptionURI = missionSpecificationElo.getTypedContent().getMissionDescriptionUri();

        modelAndView.addObject("jnlpUrl", jnlpUrl);
        modelAndView.addObject("descriptionUrl", descriptionURI);
        modelAndView.addObject("missionSpecificationTransporter", getMissionELOService().getWebSafeTransporter(missionRuntimeElo));
        modelAndView.addObject("numberOfFeedbacksToMyElos", myElosWithFeedback.getElos().size());
        modelAndView.addObject("elosWhereIHaveProvidedFeedback", elosWhereIHaveProvidedFeedback.getElos().size());

        String xdescriptionURI = String.valueOf(missionSpecificationElo.getTypedContent().getMissionDescriptionUri());
        xdescriptionURI = localizeDescriptionURI(descriptionURI.toString(), request.getLocale().getCountry());

        String content = "";
        try {
            URL u = new URL(xdescriptionURI);
            URLConnection connection = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content = content + inputLine;
            }
            content = content.replaceAll("'", "");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        modelAndView.addObject("content", content);
    }

    private String localizeDescriptionURI(String descriptionURI, String locale) {
        final String contentString = "content/";
        String firstPart = descriptionURI.substring(0, descriptionURI.indexOf(contentString) + contentString.length());
        String lastPart = descriptionURI.substring(firstPart.length(), descriptionURI.length());
        lastPart = lastPart.substring(lastPart.indexOf("/"), lastPart.length());
        lastPart = locale + lastPart;
        return firstPart + lastPart;
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
