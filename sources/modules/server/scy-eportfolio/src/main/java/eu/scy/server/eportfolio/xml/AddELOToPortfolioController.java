package eu.scy.server.eportfolio.xml;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.model.transfer.TransferElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.model.transfer.Portfolio;
import eu.scy.server.controllers.xml.MissionRuntimeEnabledXMLService;
import eu.scy.server.controllers.xml.ServiceStatusMessage;
import eu.scy.server.controllers.xml.ServiceExceptionMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jan.2011
 * Time: 14:31:38
 * To change this template use File | Settings | File Templates.
 */
public class AddELOToPortfolioController extends MissionRuntimeEnabledXMLService {

    private MissionELOService missionELOService;

    @Override
    protected Object getObject(MissionRuntimeElo missionRuntimeElo, HttpServletRequest request, HttpServletResponse response) {
        try {
            String missionURI = request.getParameter("missionURI");
            String eloURI = request.getParameter("eloURI");

            if(missionURI == null || eloURI == null) {
                return new ServiceExceptionMessage("Either missionURI or eloURI is null - service needs both to work!");
            }

            URI toBeAddedURI = new URI(eloURI);
            ScyElo toBeAdded = ScyElo.loadLastVersionElo(toBeAddedURI, getMissionELOService());

            URI missionRuntimeURI = new URI(missionURI);

            missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(missionRuntimeURI, getMissionELOService());
            
            ScyElo portfolioElo = ScyElo.loadLastVersionElo(missionRuntimeElo.getTypedContent().getEPortfolioEloUri(), getMissionELOService());
            if(portfolioElo != null) {
                String xml = portfolioElo.getContent().getXmlString();
                if(xml == null || xml.length() == 0) {
                    portfolioElo.getContent().setXmlString(getXstream().toXML(new Portfolio()));
                    portfolioElo.updateElo();
                }
            }

            Portfolio portfolio = (Portfolio) getXmlTransferObjectService().getObject(portfolioElo.getContent().getXmlString());
            portfolio.setAssessed(false);
            portfolio.setPortfolioStatus("PORTFOLIO_NOT_SUBMITTED");
            portfolio.setMissionName(missionRuntimeElo.getTitle());
            portfolio.setMissionRuntimeURI(String.valueOf(missionRuntimeURI));
            portfolio.setOwner(getCurrentUserName(request));
            TransferElo toBeAddedToPortfolio = new TransferElo(toBeAdded);
            portfolio.addElo(toBeAddedToPortfolio);


            portfolioElo.getContent().setXmlString(getXstream().toXML(portfolio));
            portfolioElo.updateElo();

            logger.info("ADDING ELO: " + eloURI + " TO PORTFOLIO OF : " + missionURI);


            return new ServiceStatusMessage("OK");
        } catch (URISyntaxException e) {
            logger.error(e.getMessage(), e);
            return new ServiceStatusMessage(e.getMessage());
        }
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
