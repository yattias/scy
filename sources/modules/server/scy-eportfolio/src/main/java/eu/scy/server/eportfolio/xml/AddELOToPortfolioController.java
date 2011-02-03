package eu.scy.server.eportfolio.xml;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.model.transfer.Portfolio;
import eu.scy.server.eportfolio.xml.utilclasses.ServiceExceptionMessage;
import eu.scy.server.eportfolio.xml.utilclasses.ServiceStatusMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        String missionURI = request.getParameter("missionURI");
        String eloURI = request.getParameter("eloURI");

        if(missionURI == null || eloURI == null) {
            return new ServiceExceptionMessage("Either missionURI or eloURI is null - service needs both to work!");
        }

        ScyElo scyElo = ScyElo.loadLastVersionElo(missionRuntimeElo.getTypedContent().getEPortfolioEloUri(), getMissionELOService());
        if(scyElo != null) {
            String xml = scyElo.getContent().getXmlString();
            if(xml == null || xml.length() == 0) {
                scyElo.getContent().setXmlString(getXstream().toXML(new Portfolio()));
                scyElo.updateElo();
            }
        }

        logger.info("ADDING ELO: " + eloURI + " TO PORTFOLIO OF : " + missionURI);

        return new ServiceStatusMessage("OK");

    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
