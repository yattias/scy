package eu.scy.server.controllers.xml;

import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.model.transfer.NewestElos;
import eu.scy.core.model.transfer.ServiceMessage;
import eu.scy.core.model.transfer.TransferElo;
import eu.scy.core.roolo.MissionELOService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.feb.2011
 * Time: 11:53:35
 * To change this template use File | Settings | File Templates.
 */
public class RetrieveSingleEloService extends XMLStreamerController{

    private MissionELOService missionELOService;

    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        String eloURI = request.getParameter("singleELO");
        String missionURI = request.getParameter("missionURI");

        try {
            URI eloUri = new URI(eloURI);

            ScyElo scyElo = ScyElo.loadLastVersionElo(eloUri, getMissionELOService());
            TransferElo transferElo = new TransferElo(scyElo);


            NewestElos newestElos = getMissionELOService().getNewestElosForFeedback(null, null);
            List transferElos = newestElos.getElos();
            for (int i = 0; i < transferElos.size(); i++) {
                TransferElo elo = (TransferElo) transferElos.get(i);
                if(elo.getUri().equals(elo.getUri())) {
                    String feedbackURK = elo.getFeedbackEloURI();
                    feedbackURK = URLDecoder.decode(feedbackURK, "UTF-8");
                    logger.info("FEEDBACK URK: " + feedbackURK);
                    if(feedbackURK != null) {
                        URI feedbackURI = new URI(feedbackURK);
                        ScyElo scyFeedbackElo = ScyElo.loadLastVersionElo(feedbackURI, getMissionELOService());
                        logger.info("LOADED ELO BASED ON URK: " + scyFeedbackElo);
                        transferElo.setFeedbackELO(scyFeedbackElo);
                    }
                }
            }

            return transferElo;
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        ServiceMessage sm = new ServiceMessage();
        sm.setMessage("This wasn't exactly how we envisioned it - unclearException occurred.");

        return sm;


    }


    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
