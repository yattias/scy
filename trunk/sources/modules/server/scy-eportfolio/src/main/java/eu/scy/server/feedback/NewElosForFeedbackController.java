package eu.scy.server.feedback;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.server.controllers.xml.MissionRuntimeEnabledXMLService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 08.feb.2011
 * Time: 14:08:39
 * To change this template use File | Settings | File Templates.
 */
public class NewElosForFeedbackController extends MissionRuntimeEnabledXMLService {

    @Override
    protected Object getObject(MissionRuntimeElo missionRuntimeElo, HttpServletRequest request, HttpServletResponse response) {
        logger.info("*** *** LOADING NEW ELOS FOR FEEDBACK!");
        return null;//getMissionELOService().getNewestElosForFeedback(missionRuntimeElo, getCurrentUserName(request));        
    }

}
