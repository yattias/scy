package eu.scy.server.controllers.scyfeedback.xml;

import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.model.transfer.FeedbackEloTransfer;
import eu.scy.core.model.transfer.FeedbackReplyTransfer;
import eu.scy.core.model.transfer.FeedbackTransfer;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.xml.XMLStreamerController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.feb.2011
 * Time: 05:26:12
 * To change this template use File | Settings | File Templates.
 */
public class FeedbackEloService extends XMLStreamerController {

    private MissionELOService missionELOService;

    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {

        String fURI = request.getParameter("feedbackURI");
        if(fURI != null) {
            try {
                URI feedbackUri = new URI(fURI);
                ScyElo feedbackElo = ScyElo.loadLastVersionElo(feedbackUri, getMissionELOService());
                String feedbackRepresentation = feedbackElo.getContent().getXmlString();

                //need to do some cleanup to make this model compatible with the rest:

                feedbackRepresentation = feedbackRepresentation.replaceAll("<feedback>", "<feedbackelo>");
                feedbackRepresentation = feedbackRepresentation.replaceAll("</feedback>", "</feedbackelo>");

                logger.info("WASHED ELOSTRING: " + feedbackRepresentation);

                FeedbackEloTransfer feedbackEloTransfer = (FeedbackEloTransfer) getXmlTransferObjectService().getObject(feedbackRepresentation);
                feedbackEloTransfer.setUri(fURI);

                FeedbackTransfer feedbackTransfer = new FeedbackTransfer();
                feedbackTransfer.setCreatedBy("wouter");
                SimpleDateFormat format = new SimpleDateFormat("DD.MM.yyyy");
                feedbackTransfer.setCalendarDate(format.format(new Date()));
                feedbackTransfer.setComment("A COMMENT MAN!");

                FeedbackReplyTransfer reply = new FeedbackReplyTransfer();
                reply.setCreatedBy("adam");
                reply.setComment("This is totally bull!");
                reply.setComment("This is totally bull!");
                reply.setCalendarDate(format.format(new Date()));
                feedbackTransfer.addReply(reply);

                feedbackEloTransfer.addFeedback(feedbackTransfer);

                return feedbackEloTransfer;

            } catch (URISyntaxException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return null;


    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
