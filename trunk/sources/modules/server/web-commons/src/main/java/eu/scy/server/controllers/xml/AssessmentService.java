package eu.scy.server.controllers.xml;

import com.thoughtworks.xstream.XStream;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.roolo.MissionELOService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
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
        List portfolios = null;
        try {
            String eloURI = request.getParameter("missionURI");
            logger.info("THIS IS THE URI: " + eloURI);
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(new URI(eloURI), getMissionELOService() );

            portfolios = getMissionELOService().getPortfoliosThatAreReadyForAssessment(missionSpecificationElo);
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
