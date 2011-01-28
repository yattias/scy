package eu.scy.server.controllers.xml;

import com.thoughtworks.xstream.XStream;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.xml.transfer.Portfolio;
import eu.scy.server.controllers.xml.transfer.PortfolioContainer;
import eu.scy.server.controllers.xml.transfer.TransferElo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.jan.2011
 * Time: 19:54:38
 * To change this template use File | Settings | File Templates.
 */
public class AssessmentService extends XMLStreamerController{

    private MissionELOService missionELOService;

    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        LinkedList portfolios = new LinkedList();
        try {
            String eloURI = request.getParameter("missionURI");
            logger.info("THIS IS THE URI: " + eloURI);
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(new URI(eloURI), getMissionELOService() );

            List runtimeElos = getMissionELOService().getRuntimeElos(missionSpecificationElo);
            for (int i = 0; i < runtimeElos.size(); i++) {
                ScyElo shittyElo = (ScyElo) runtimeElos.get(i);
                MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(shittyElo.getUri(),  getMissionELOService());
                URI portfolioURI = missionRuntimeElo.getTypedContent().getEPortfolioEloUri();
                if(portfolioURI != null) {
                    ScyElo scyElo = ScyElo.loadLastVersionElo(portfolioURI, getMissionELOService());
                    String xml = scyElo.getContent().getXmlString();
                    if(xml != null) {
                        Portfolio portfolio = (Portfolio) getXmlTransferObjectService().getObject(xml);
                        portfolios.add(portfolio);
                    }
                }
            }

/*            portfolios = new LinkedList();

            Portfolio portfolio = new Portfolio();
            portfolio.setMissionName("Freaky Styley");
            portfolio.setReflectionCollaboration("Woha wee");
            portfolio.setReflectionEffort("2");
            portfolio.setReflectionInquiry("REF INQ");
            portfolio.setReflectionMission("REFL MIS");
            portfolio.setOwner("digi");

            TransferElo transferElo = new TransferElo();
            transferElo.setAssessed(Boolean.FALSE);
            transferElo.setAssessmentComment("really awesome");
            transferElo.setGrade("NG");
            transferElo.setCatname("a cat on da mat");
            transferElo.setReflectionComment("Reflect me baby - do it hard!");

            portfolio.addElo(transferElo);
            portfolios.add(portfolio);
            */
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return portfolios;
    }

    @Override
    protected void addAliases(XStream xStream) {
        super.addAliases(xStream);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
