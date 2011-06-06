package eu.scy.server.controllers.scyfeedback.xml;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.core.model.transfer.FeedbackEloTransfer;
import eu.scy.server.controllers.xml.MissionRuntimeEnabledXMLService;
import eu.scy.server.controllers.xml.XMLStreamerController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.feb.2011
 * Time: 07:25:21
 * To change this template use File | Settings | File Templates.
 */
public class MyElosFeedbackService extends MissionRuntimeEnabledXMLService {

    @Override
    protected Object getObject(MissionRuntimeElo missionRuntimeElo, HttpServletRequest request, HttpServletResponse response) {
        logger.info("*** *** LOADING MY ELOS FEEDBACK SERVICE!");
        return getMissionELOService().getMyElosWithFeedback(missionRuntimeElo, getCurrentUserName(request));
    }
}
