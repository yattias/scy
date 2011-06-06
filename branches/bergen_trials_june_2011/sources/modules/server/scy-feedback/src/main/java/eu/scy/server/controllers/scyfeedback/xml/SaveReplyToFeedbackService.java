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
import java.util.Enumeration;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.feb.2011
 * Time: 19:46:28
 * To change this template use File | Settings | File Templates.
 */
public class SaveReplyToFeedbackService extends XMLStreamerController{

    private MissionELOService missionELOService;

    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {

        try {
            String feedbackURI = request.getParameter("feedbackURI");
            String replyfeedbackID = request.getParameter("replyfeedbackID");
            String xmlContent = request.getParameter("xmlContent");

            logger.info("FEEDBACK URI: " + feedbackURI);
            logger.info("REPLY FEEDBACK ID: " + replyfeedbackID);
            logger.info("XML CONTENT: " + xmlContent);

            URI feedbackEloURI = new URI(feedbackURI);

            ScyElo feedbackElo = ScyElo.loadLastVersionElo(feedbackEloURI, getMissionELOService());
            String xmlContentFromRoolo = feedbackElo.getContent().getXmlString();
            logger.info("FOUND THIS CONTENT: " + xmlContent);
            logger.info("Ceating a feedback elo object");

            FeedbackEloTransfer feedbackEloTransfer = (FeedbackEloTransfer) getXmlTransferObjectService().getObject(xmlContentFromRoolo);
            List feedbacks = feedbackEloTransfer.getFeedbacks();
            for (int i = 0; i < feedbacks.size(); i++) {
                FeedbackTransfer feedbackTransfer = (FeedbackTransfer) feedbacks.get(i);
                if(feedbackTransfer.getId() != null && feedbackTransfer.getId().equals(replyfeedbackID)) {
                    logger.info("FOUND THE CORRECT FEEDBACK!!! SETTING REPLY");
                    FeedbackReplyTransfer replyTransfer = (FeedbackReplyTransfer) getXmlTransferObjectService().getObject(xmlContent);
                    feedbackTransfer.addReply(replyTransfer);
                }
            }

            logger.info("NOW MARCHALLING XML:");

            feedbackEloTransfer.setUri(feedbackURI);

            String newXML = getXmlTransferObjectService().getXStreamInstance().toXML(feedbackEloTransfer);

            feedbackElo.getContent().setXmlString(newXML);
            feedbackElo.updateElo();


            logger.info("FUCK YEAH! new xml(which is also returned to client): "  + newXML);

            return feedbackEloTransfer;

        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
