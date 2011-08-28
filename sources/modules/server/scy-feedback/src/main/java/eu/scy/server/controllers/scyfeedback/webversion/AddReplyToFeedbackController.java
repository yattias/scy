package eu.scy.server.controllers.scyfeedback.webversion;

import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.FeedbackEloTransfer;
import eu.scy.core.model.transfer.FeedbackReplyTransfer;
import eu.scy.core.model.transfer.FeedbackTransfer;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.aug.2011
 * Time: 21:30:48
 * To change this template use File | Settings | File Templates.
 */
public class AddReplyToFeedbackController extends BaseController {

    private MissionELOService missionELOService;
    private XMLTransferObjectService xmlTransferObjectService;

    private String commentContent;

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String feedbackId = request.getParameter("feedbackId");
        String feedbackELOUri = request.getParameter("feedbackEloURI");
        String reply = request.getParameter("reply");
        setCommentContent(reply);
        logger.info("FEEDBACK ID: " + feedbackId);
        logger.info("FEEDBACK ELO URI: " + feedbackELOUri);

        URI uri = getURI(feedbackELOUri);

        ScyElo feedbackElo = ScyElo.loadLastVersionElo(uri, getMissionELOService());
        String feedbackRepresentation = feedbackElo.getContent().getXmlString();
        FeedbackEloTransfer feedbackEloTransfer = (FeedbackEloTransfer) getXmlTransferObjectService().getObject(feedbackRepresentation);

        List<FeedbackTransfer> feedbackTransferList = feedbackEloTransfer.getFeedbacks();
        boolean feedbackAdded = false;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
        for (int i = 0; i < feedbackTransferList.size(); i++) {
            FeedbackTransfer feedbackTransfer = feedbackTransferList.get(i);
            if(feedbackTransfer.getId().equals(feedbackId)) {
                FeedbackReplyTransfer frt = new FeedbackReplyTransfer();
                
                frt.setComment(reply);
                frt.setCreatedBy(getCurrentUserName(request));
                frt.setCalendarTime(timeFormat.format(new Date()));
                frt.setCalendarDate(simpleDateFormat.format(new Date()));
                feedbackTransfer.addReply(frt);
                feedbackElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(feedbackEloTransfer));
                feedbackElo.updateElo();
                feedbackAdded = true;

            }
        }

        if(feedbackAdded ) logger.info("Feedback reply was given: " + reply);
        else logger.info("Did not add feedback reply.");

    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public XMLTransferObjectService getXmlTransferObjectService() {
        return xmlTransferObjectService;
    }

    public void setXmlTransferObjectService(XMLTransferObjectService xmlTransferObjectService) {
        this.xmlTransferObjectService = xmlTransferObjectService;
    }

    public String getFeedbackString(){
        String feedback = "";
        feedback += getCommentContent();
        return feedback;
    }


}
