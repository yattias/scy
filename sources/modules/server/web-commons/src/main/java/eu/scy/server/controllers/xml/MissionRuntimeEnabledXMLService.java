package eu.scy.server.controllers.xml;

import com.thoughtworks.xstream.XStream;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.roolo.MissionELOService;
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


    protected Object getDeserializedObject(String xml) {
        return xstream.fromXML(xml);
    }

    @Override
    protected final Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        try {
            String missionURI = request.getParameter("missionURI");
            logger.info("MIssionURI: " + missionURI + " from service: " + getClass().getName());
            if (missionURI != null) {
                missionURI = URLDecoder.decode(missionURI, "UTF-8");

                ScyElo scyElo = (ScyElo) getUrlInspector().instpectRequest(request, httpServletResponse);
                if (scyElo != null && scyElo.getTechnicalFormat().equals("scy/missionruntime")) {
                    MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadElo(new URI(missionURI), getMissionELOService());
                    return getObject(missionRuntimeElo, request, httpServletResponse);
                } else {
                    return getObject(null, request, httpServletResponse);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ServiceExceptionMessage("This service requires that you set missionURI parameter in order to load");
    }

    @Override
    protected void addAliases(XStream xStream) {
        super.addAliases(xStream);

    }

    protected PedagogicalPlanTransfer getPedagogicalPlanTransfer(MissionSpecificationElo missionSpecificationElo) {
        URI pedagogicalPlanUri = missionSpecificationElo.getTypedContent().getPedagogicalPlanSettingsEloUri();
        logger.info("**** PEDAGOGICAL PLAN URI: " + pedagogicalPlanUri + " from service " + getClass().getName());

        ScyElo pedagogicalPlanELO = ScyElo.loadLastVersionElo(pedagogicalPlanUri, getMissionELOService());
        String pedagogicalPlanXML = pedagogicalPlanELO.getContent().getXmlString();
        return (PedagogicalPlanTransfer) getXmlTransferObjectService().getObject(pedagogicalPlanXML);
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
