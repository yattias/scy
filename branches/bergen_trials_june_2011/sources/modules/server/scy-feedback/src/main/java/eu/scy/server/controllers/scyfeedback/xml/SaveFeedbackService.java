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
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.feb.2011
 * Time: 13:15:01
 * To change this template use File | Settings | File Templates.
 */
public class  SaveFeedbackService extends XMLStreamerController {

    private MissionELOService missionELOService;

    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        String feedbackURI = request.getParameter("feedbackURI");
        String xmlContentFromFlash = request.getParameter("xmlContent");

        logger.info("FeedbackURI " + feedbackURI);
        logger.info("XML FROM FLASH: " + xmlContentFromFlash);

        FeedbackTransfer feedbackTransfer = (FeedbackTransfer) getXmlTransferObjectService().getObject(xmlContentFromFlash);

        try {
            URI uri = new URI(feedbackURI);
            ScyElo scyFeedbackElo = ScyElo.loadLastVersionElo(uri, getMissionELOService());
            String originalXMLFromFeedbackEloInRoolo = scyFeedbackElo.getContent().getXml();

            logger.info("ORIGINAL XML FROM FEEDBACK ELO (FROM ROOLO): " + originalXMLFromFeedbackEloInRoolo);

            FeedbackEloTransfer feedbackEloTransfer = (FeedbackEloTransfer) getXmlTransferObjectService().getObject(originalXMLFromFeedbackEloInRoolo);
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

    public String stripTags(String text, List<String> ignoreList) {
        if (text == null) throw new IllegalArgumentException("StringUtil.stripTags called with null text");
        String patternStr;
        if ((ignoreList == null) || (ignoreList.size() == 0)) {
            patternStr = "<[\\s\\S]+?>";
        } else {
            patternStr = "<(?!\\/?(";
            for (int i = 0; i < ignoreList.size(); i++) {
                if (i > 0)
                    patternStr += "|";
                String tag = ignoreList.get(i);
                patternStr += "(?:" + tag + ")";
            }
            patternStr += ")\\b)[\\s\\S]+?>";
        }
        String replaceStr = "";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(text);
        String completedString = matcher.replaceAll(replaceStr);
        completedString = removeAllTagAttributes(completedString);
        //completedString = replaceWhitespaceProducedByIE60(completedString);
        return completedString;
    }

    private String removeAllTagAttributes(String inputString) {
        String patternString = "\\w+\\s*=\\s*\"[^\"]*\"";
        String replaceString = "";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(inputString);
        return matcher.replaceAll(replaceString);
    }


}
