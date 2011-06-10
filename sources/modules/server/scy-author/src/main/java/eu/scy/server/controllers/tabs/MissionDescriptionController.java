package eu.scy.server.controllers.tabs;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.model.User;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.jun.2011
 * Time: 09:12:17
 * To change this template use File | Settings | File Templates.
 */
public class MissionDescriptionController extends BaseController {

    private MissionELOService missionELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {


        MissionSpecificationElo missionSpecificationElo = getMissionSpecification(request);

        String descriptionURI = "/webapp/useradmin/LoadExternalPage.html?url=" + missionSpecificationElo.getTypedContent().getMissionDescriptionUri();
        descriptionURI = localizeDescriptionURI(descriptionURI, getCurrentUser(request));
        modelAndView.addObject("descriptionUrl", descriptionURI);

    }

    private String localizeDescriptionURI(String descriptionURI, User currentUser) {
        final String contentString = "content/";
        String firstPart = descriptionURI.substring(0, descriptionURI.indexOf(contentString) + contentString.length());
        String lastPart = descriptionURI.substring(firstPart.length(), descriptionURI.length());
        lastPart = lastPart.substring(lastPart.indexOf("/"), lastPart.length());
        lastPart = currentUser.getUserDetails().getLocale() + lastPart;
        return firstPart + lastPart;
    }


    private MissionSpecificationElo getMissionSpecification(HttpServletRequest request) {
        URI missionUri = getURI(request.getParameter(ELO_URI));
        if (missionUri != null) {
            return MissionSpecificationElo.loadLastVersionElo(missionUri, getMissionELOService());
        }
        logger.error("DID NOT GET THE MISSION SPECIFICATION URI!");
        return null;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
