package eu.scy.core.roolo;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.Portfolio;

import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 04.aug.2011
 * Time: 08:37:17
 * To change this template use File | Settings | File Templates.
 */
public class PortfolioELOServiceImpl extends BaseELOServiceImpl implements PortfolioELOService{

    private XMLTransferObjectService xmlTransferObjectService;

    @Override
    public Portfolio getPortfolio(MissionRuntimeElo missionRuntimeElo, String username) {
        URI portfolioURI = missionRuntimeElo.getTypedContent().getEPortfolioEloUri();
        System.out.println("Loading portfolio: " + portfolioURI);
        Portfolio portfolio = null;
        if (portfolioURI != null) {
            ScyElo ePortfolioElo = ScyElo.loadLastVersionElo(portfolioURI, this);
            String portfolioString = ePortfolioElo.getContent().getXmlString();
            if (portfolioString != null) {
                return (Portfolio) getXmlTransferObjectService().getObject(portfolioString);
            }
        }

        if (portfolio == null) {
            portfolio = new Portfolio();
            portfolio.setMissionName(missionRuntimeElo.getTitle());
            portfolio.setOwner(username);
            portfolio.setPortfolioStatus("NOT_SUBMITTED");
            portfolio.setMissionRuntimeURI(missionRuntimeElo.getUri().toString());
        }

        return portfolio;

    }

    public XMLTransferObjectService getXmlTransferObjectService() {
        return xmlTransferObjectService;
    }

    public void setXmlTransferObjectService(XMLTransferObjectService xmlTransferObjectService) {
        this.xmlTransferObjectService = xmlTransferObjectService;
    }
}
