package eu.scy.server.controllers.student;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.NewestElos;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.BaseController;
import eu.scy.core.model.transfer.Portfolio;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

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
        URI portfolioURI = missionRuntimeElo.getTypedContent().getEPortfolioEloUri();
        modelAndView.addObject("portfolioStatus", "Not Submitted");
        if (portfolioURI != null) {
            ScyElo scyElo = ScyElo.loadLastVersionElo(portfolioURI, getMissionELOService());
            String xmlContent = scyElo.getContent().getXmlString();
            if (xmlContent != null) {
                Portfolio portfolio = (Portfolio) getXmlTransferObjectService().getObject(xmlContent);
                if (portfolio != null) {
                    portfolio.unCdatify();
                    modelAndView.addObject("portfolio", portfolio);
                    if(portfolio.getPortfolioStatus() == null) portfolio.setPortfolioStatus("PORTFOLIO_NOT_SUBMITTED");
                    if(portfolio.getPortfolioStatus().equals("PORTFOLIO_SUBMITTED")) modelAndView.addObject("portfolioStatus", "Submitted");
                    if(portfolio.getPortfolioStatus().equals("PORTFOLIO_ASSESSED")) modelAndView.addObject("portfolioStatus", "<strong>Assessed</strong>");
                    else modelAndView.addObject("portfolioStatus", "Not Submitted");
                    logger.info("SET PORTFOLIO: " + portfolio.getPortfolioStatus());
                } else {
                    logger.info("PORTFOLIO IS NULL!!");
                }
            } else {
                logger.info("XML CONTENT IS NULL!");
            }

        } else {
            logger.info("NO PORTFOLIO URI!");
        }


        NewestElos myElosWithFeedback = getMissionELOService().getMyElosWithFeedback(missionRuntimeElo, getCurrentUserName(request));
        NewestElos elosWhereIHaveProvidedFeedback = getMissionELOService().getFeedbackElosWhereIHaveContributed(missionRuntimeElo, getCurrentUserName(request));

        MissionSpecificationElo missionSpecificationElo = getMissionELOService().getMissionSpecificationELOForRuntume(missionRuntimeElo);
        URI descriptionURI = missionSpecificationElo.getTypedContent().getMissionDescriptionUri();

        modelAndView.addObject("descriptionUrl", descriptionURI);
        modelAndView.addObject("missionSpecificationTransporter", getMissionELOService().getWebSafeTransporter(missionRuntimeElo));
        modelAndView.addObject("numberOfFeedbacksToMyElos", myElosWithFeedback.getElos().size());
        modelAndView.addObject("elosWhereIHaveProvidedFeedback", elosWhereIHaveProvidedFeedback.getElos().size());
        try {
            //modelAndView.addObject("jnlpRef", "/webapp/scy-lab.jnlp?username=" + getCurrentUserName(request) + "&mission=" + URLEncoder.encode(missionRuntimeElo.getUri().toString(), "UTF-8"));
            modelAndView.addObject("jnlpRef", "/webapp/scy-lab.jnlp?username=" + getCurrentUserName(request) + "&mission=" + URLEncoder.encode(missionRuntimeElo.getUri().toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


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
