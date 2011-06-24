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
        if (fURI != null) {
            try {
                URI feedbackUri = new URI(fURI);
                ScyElo feedbackElo = ScyElo.loadLastVersionElo(feedbackUri, getMissionELOService());
                String feedbackRepresentation = feedbackElo.getContent().getXmlString();

                //need to do some cleanup to make this model compatible with the rest:
                if (feedbackRepresentation.trim().startsWith("<feedback>")) {
                    feedbackRepresentation = feedbackRepresentation.replaceFirst("<feedback>", "<feedbackelo>");
                    int lastIndex = feedbackRepresentation.lastIndexOf("</feedback");
                    feedbackRepresentation = feedbackRepresentation.substring(0, lastIndex);
                    feedbackRepresentation += "</feedbackelo>";
                }


                int feedbacksIndex = feedbackRepresentation.indexOf("<feedbacks");
                if (feedbacksIndex > 0) {
                    String end = feedbackRepresentation.substring(feedbacksIndex, feedbackRepresentation.length());
                    String start = feedbackRepresentation.substring(0, feedbacksIndex);
                    feedbackRepresentation = start + end;
                }

                FeedbackEloTransfer feedbackEloTransfer = (FeedbackEloTransfer) getXmlTransferObjectService().getObject(feedbackRepresentation);
                feedbackEloTransfer.setUri(fURI);
                feedbackEloTransfer = updateNumberOfViews(feedbackEloTransfer, feedbackElo);

                return feedbackEloTransfer;

            } catch (URISyntaxException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return null;


    }

    private FeedbackEloTransfer updateNumberOfViews(FeedbackEloTransfer feedbackEloTransfer, ScyElo feedbackElo) {
        logger.info("**************************************************************** UPDATING NUMBER OF VIEWS for " + feedbackEloTransfer.getComment());

        logger.info("SHOWN: " + feedbackEloTransfer.getShown());

        if (feedbackEloTransfer.getShown() != null) {
            try {
                Integer shown =Integer.parseInt(feedbackEloTransfer.getShown());
                shown ++;
                feedbackEloTransfer.setShown(String.valueOf(shown));
            } catch (NumberFormatException e) {
                logger.warn("shown was invalid, setting to 1");
                feedbackEloTransfer.setShown("1");
            }
        } else {
            feedbackEloTransfer.setShown("1");
        }

        feedbackElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(feedbackEloTransfer));
        feedbackElo.updateElo();

        logger.info("New SHOWN: " + feedbackEloTransfer.getShown());

        return feedbackEloTransfer;

    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
