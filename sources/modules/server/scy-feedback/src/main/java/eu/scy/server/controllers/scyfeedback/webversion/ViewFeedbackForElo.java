package eu.scy.server.controllers.scyfeedback.webversion;

import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.BaseController;
import eu.scy.server.controllers.scyfeedback.xml.FeedbackEloService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
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
        System.out.println("################# listUri: " + listUri);
        modelAndView.addObject("listUri", listUri);
        modelAndView.addObject("elo", getMissionELOService().getTransferElo(scyElo));

        

        //getMissionELOService().getTransferElo(scyElo).getF

        
        //String feedbackUri = getMissionELOService().getTransferElo(scyElo).getFeedbackEloURI();

        //modelAndView.addObject("createdByUser", getCreatedByUser());

        



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
