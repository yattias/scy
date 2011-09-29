package eu.scy.server.controllers.scyfeedback.webversion;

import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.FeedbackEloTransfer;
import eu.scy.core.model.transfer.FeedbackTransfer;
import eu.scy.core.model.transfer.TransferElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.BaseController;
import eu.scy.server.controllers.scyfeedback.xml.FeedbackEloService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 04.jul.2011
 * Time: 08:29:01
 * To change this template use File | Settings | File Templates.
 */
public class ViewFeedbackForElo extends BaseController {
    private ScyElo elo;
    private String originatingPage;

    private static final String ORIGINATING_PAGE = "originatingPage";

    public ScyElo getElo() {
        return elo;
    }

    public void setElo(ScyElo elo) {
        this.elo = elo;
    }

    private MissionELOService missionELOService;
    private XMLTransferObjectService xmlTransferObjectService;

    public XMLTransferObjectService getXmlTransferObjectService() {
        return xmlTransferObjectService;
    }

    public void setXmlTransferObjectService(XMLTransferObjectService xmlTransferObjectService) {
        this.xmlTransferObjectService = xmlTransferObjectService;
    }

    public FeedbackEloService getFeedbackEloService() {
        return feedbackEloService;
    }

    public void setFeedbackEloService(FeedbackEloService feedbackEloService) {
        this.feedbackEloService = feedbackEloService;
    }

    private FeedbackEloService feedbackEloService;



    

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        URI uri = getURI(request.getParameter(ELO_URI));
        ScyElo scyElo = ScyElo.loadLastVersionElo(uri, getMissionELOService());
        setScyElo(scyElo);
        setOriginatingPage(request.getParameter(ORIGINATING_PAGE));
        modelAndView.addObject("eloURI", getEncodedUri(request.getParameter(ELO_URI)));
        String action = request.getParameter("action");
        if(action == null) action = "give";

        String missionRuntimeURI = getEncodedUri(request.getParameter("missionRuntimeURI"));

        TransferElo transferElo = getMissionELOService().getTransferElo(scyElo);
        try {
            if(transferElo == null) {
                modelAndView.setViewName("forward:fbIndex.html");
                return;
            }
            String fbURI = transferElo.getFeedbackEloURI();
            fbURI = URLDecoder.decode(fbURI, "UTF-8");
            URI feedbackURI = new URI(fbURI);



            ScyElo feedbackElo = ScyElo.loadLastVersionElo(feedbackURI, getMissionELOService());
            String feedbackRepresentation = feedbackElo.getContent().getXmlString();
            FeedbackEloTransfer feedbackEloTransfer = (FeedbackEloTransfer) getXmlTransferObjectService().getObject(feedbackRepresentation);
            String shown = feedbackEloTransfer.getShown();
            Integer shownInteger = 0;
            if(shown != null) {
                shownInteger = new Integer(shown);
            }

            shownInteger++;

            List<FeedbackTransfer> feedbackTransfers = feedbackEloTransfer.getFeedbacks();
            Integer totalScore = 0;
            for (int i = 0; i < feedbackTransfers.size(); i++) {
                FeedbackTransfer feedbackTransfer = feedbackTransfers.get(i);
                String sc = feedbackTransfer.getEvalu();
                Integer score = 0;
                try {
                    score = Integer.parseInt(sc);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                totalScore += score;



            }


            feedbackEloTransfer.setShown(String.valueOf(shownInteger));
            feedbackEloTransfer.setScore(String.valueOf(totalScore));
            feedbackElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(feedbackEloTransfer));
            feedbackEloTransfer.setUri(fbURI);
            feedbackElo.updateElo();

            
            modelAndView.addObject("feedbackElo", feedbackEloTransfer);
            modelAndView.addObject("action", action);
            modelAndView.addObject(ELO_URI, getEncodedUri(request.getParameter(ELO_URI)));
            modelAndView.addObject("missionRuntimeURI", missionRuntimeURI);


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        modelAndView.addObject("transferElo", transferElo);
    }



    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    

    public List getFeedbackForElo(){
       List feedback = getMissionELOService().getFeedback();       
       return feedback;
    }

    public String getOriginatingPage() {
        return originatingPage;
    }

    public void setOriginatingPage(String originatingPage) {
        this.originatingPage = originatingPage;
    }

    /*public User getCreatedByUser(){
        User createdBy = getUserService().getUser(getMissionELOService().getTransferElo(getElo()).getCreatedBy());
        return createdBy;
    } */


    


}
