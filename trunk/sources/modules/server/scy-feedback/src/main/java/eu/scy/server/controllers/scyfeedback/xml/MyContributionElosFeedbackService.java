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
 * Time: 07:26:38
 * To change this template use File | Settings | File Templates.
 */
public class MyContributionElosFeedbackService extends MissionRuntimeEnabledXMLService {

    @Override
    protected Object getObject(MissionRuntimeElo missionRuntimeElo, HttpServletRequest request, HttpServletResponse response) {
        logger.info("*** *** LOADING MY CONTRIBUTION ELOS FEEDBACK SERVICE!");
        return getMissionELOService().getFeedbackElosWhereIHaveContributed(missionRuntimeElo, getCurrentUserName(request));
    }
}
