package eu.scy.server.controllers.scyfeedback.xml;

import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.model.transfer.FeedbackEloTransfer;
import eu.scy.core.model.transfer.FeedbackTransfer;
import eu.scy.core.model.transfer.ServiceMessage;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.xml.XMLStreamerController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.feb.2011
 * Time: 13:15:01
 * To change this template use File | Settings | File Templates.
 */
public class SaveFeedbackService extends XMLStreamerController {

    private MissionELOService missionELOService;

    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        String feedbackURI = request.getParameter("feedbackURI");
        String xmlContent = request.getParameter("xmlContent");

        logger.info("FeedbackURI " + feedbackURI);
        logger.info("XML: " + xmlContent);

        FeedbackTransfer feedbackTransfer = (FeedbackTransfer) getXmlTransferObjectService().getObject(xmlContent);

        try {
            URI uri = new URI(feedbackURI);
            ScyElo scyFeedbackElo = ScyElo.loadLastVersionElo(uri, getMissionELOService());
            String originalXML = scyFeedbackElo.getContent().getXml();

            logger.info("ORIGINAL XML: " + originalXML);
            int firstIndex = originalXML.indexOf("<feedbackelo>");
            int lastIndex = originalXML.indexOf("</content>");
            if(firstIndex >= 0 && lastIndex >= 0) {
                originalXML = originalXML.substring(firstIndex, lastIndex);
            }

            logger.info("CUT XML: " + originalXML);

            FeedbackEloTransfer feedbackEloTransfer = (FeedbackEloTransfer) getXmlTransferObjectService().getObject(originalXML);
            feedbackEloTransfer.addFeedback(feedbackTransfer);

            String newXML = getXmlTransferObjectService().getXStreamInstance().toXML(feedbackEloTransfer);

            scyFeedbackElo.getContent().setXmlString(newXML);
            scyFeedbackElo.updateElo();
            ScyElo updatedElo = ScyElo.loadLastVersionElo(uri, getMissionELOService());

            logger.info("CREATING FEEDBACK OBJECT FROM XML STRING: " + newXML);
            FeedbackEloTransfer updatedFeedbackEloTransfer = (FeedbackEloTransfer) getXmlTransferObjectService().getObject(newXML);
            return updatedFeedbackEloTransfer;

        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        ServiceMessage serviceMessage = new ServiceMessage();
        serviceMessage.setMessage("OOPPS");
        return serviceMessage;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
