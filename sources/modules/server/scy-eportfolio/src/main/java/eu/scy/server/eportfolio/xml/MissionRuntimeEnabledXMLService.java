package eu.scy.server.eportfolio.xml;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.xml.XMLStreamerController;
import eu.scy.server.url.UrlInspector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.jan.2011
 * Time: 05:34:59
 * To change this template use File | Settings | File Templates.
 */
public abstract class MissionRuntimeEnabledXMLService extends XMLStreamerController {

    private MissionELOService missionELOService;
    private UrlInspector urlInspector;


    @Override
    protected final Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        try {
            String missionURI = request.getParameter("missionURI");
            logger.info("MIssionURI: " + missionURI);
            if (missionURI != null) {
                missionURI = URLDecoder.decode(missionURI, "UTF-8");

                ScyElo scyElo = (ScyElo) getUrlInspector().instpectRequest(request, httpServletResponse);
                MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadElo(new URI(missionURI), getMissionELOService());
                return getObject(missionRuntimeElo, request, httpServletResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected abstract Object getObject(MissionRuntimeElo missionRuntimeElo, HttpServletRequest request, HttpServletResponse response);

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public UrlInspector getUrlInspector() {
        return urlInspector;
    }

    public void setUrlInspector(UrlInspector urlInspector) {
        this.urlInspector = urlInspector;
    }
}
