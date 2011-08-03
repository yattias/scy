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
import java.net.URISyntaxException;
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

        modelAndView.addObject("eloURI", getEncodedUri(request.getParameter(ELO_URI)));
        URI listUri = scyElo.getUri();
        modelAndView.addObject("listUri", listUri);
        TransferElo transferElo = getMissionELOService().getTransferElo(scyElo);
        try {
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

            //shownInteger++;

            feedbackEloTransfer.setShown(String.valueOf(shownInteger));
            feedbackElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(feedbackEloTransfer));
            feedbackElo.updateElo();
            
            modelAndView.addObject("feedbackElo", feedbackEloTransfer);
            

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

    



    /*public User getCreatedByUser(){
        User createdBy = getUserService().getUser(getMissionELOService().getTransferElo(getElo()).getCreatedBy());
        return createdBy;
    } */


    


}
