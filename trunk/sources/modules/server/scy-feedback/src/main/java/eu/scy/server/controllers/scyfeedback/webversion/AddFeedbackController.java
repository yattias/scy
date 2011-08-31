package eu.scy.server.controllers.scyfeedback.webversion;

import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.FeedbackEloTransfer;
import eu.scy.core.model.transfer.FeedbackTransfer;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.actionlogging.ActionLoggerService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.aug.2011
 * Time: 11:20:00
 * To change this template use File | Settings | File Templates.
 */
public class AddFeedbackController extends BaseController {

    private MissionELOService missionELOService;
    private XMLTransferObjectService xmlTransferObjectService;
    private ActionLoggerService actionLoggerService;


    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        logger.info("Adding feedback");
        String feedbackString = request.getParameter("feedbacktext");
        String feedbackEloURI = request.getParameter("feedbackEloURI");
        String score = request.getParameter("score");
        URI feedbackURI = getURI(feedbackEloURI);
        modelAndView.addObject("feedbacktext", feedbackString);

        ScyElo feedbackElo = ScyElo.loadLastVersionElo(feedbackURI, getMissionELOService());
        String feedbackRepresentation = feedbackElo.getContent().getXmlString();
        FeedbackEloTransfer feedbackEloTransfer = (FeedbackEloTransfer) getXmlTransferObjectService().getObject(feedbackRepresentation);


        URI parentEloURI = feedbackElo.getFeedbackOnEloUri();


        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");

        FeedbackTransfer feedbackTransfer = new FeedbackTransfer();
        feedbackTransfer.setComment(feedbackString);
        feedbackTransfer.setCreatedBy(getCurrentUserName(request));
        feedbackTransfer.setEvalu(score);
        feedbackTransfer.setCalendarDate(simpleDateFormat.format(now));
        feedbackTransfer.setCalendarTime(timeFormat.format(now));
        
        feedbackEloTransfer.addFeedback(feedbackTransfer);
        feedbackElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(feedbackEloTransfer));
        feedbackElo.updateElo();

        getActionLoggerService().logAction("feedback_given", feedbackEloTransfer.getCreatedBy(), "feedback", parentEloURI.toString());

        modelAndView.addObject("username", getCurrentUser(request));
        modelAndView.addObject("feedbackItem", feedbackTransfer);

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

    public ActionLoggerService getActionLoggerService() {
        return actionLoggerService;
    }

    public void setActionLoggerService(ActionLoggerService actionLoggerService) {
        this.actionLoggerService = actionLoggerService;
    }
}
