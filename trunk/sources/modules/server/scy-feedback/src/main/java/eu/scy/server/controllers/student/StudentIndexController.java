package eu.scy.server.controllers.student;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.BaseController;
import eu.scy.server.controllers.xml.XMLTransferObjectService;
import eu.scy.server.controllers.xml.transfer.Portfolio;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

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
        if (portfolioURI != null) {
            ScyElo scyElo = ScyElo.loadLastVersionElo(portfolioURI, getMissionELOService());
            String xmlContent = scyElo.getContent().getXmlString();
            if (xmlContent != null) {
                Portfolio portfolio = (Portfolio) getXmlTransferObjectService().getObject(xmlContent);
                if (portfolio != null) {
                    modelAndView.addObject("portfolio", portfolio);
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
        modelAndView.addObject("missionSpecificationTransporter", getMissionELOService().getWebSafeTransporter(missionRuntimeElo));
        try {
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
