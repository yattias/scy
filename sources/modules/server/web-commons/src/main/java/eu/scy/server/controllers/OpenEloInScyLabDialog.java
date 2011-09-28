package eu.scy.server.controllers;

import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.model.transfer.TransferElo;
import eu.scy.core.roolo.MissionELOService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 01.sep.2011
 * Time: 11:59:35
 * To change this template use File | Settings | File Templates.
 */
public class OpenEloInScyLabDialog extends BaseController {

    private MissionELOService missionELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        ScyElo elo = ScyElo.loadLastVersionElo(getURI(request.getParameter(ELO_URI)), getMissionELOService());

        String snippetURL = "/webapp/scy-lab.jnlp?singleEloUri=" + getEncodedUri(request.getParameter(ELO_URI));
        modelAndView.addObject("snippetURL" , snippetURL);


        TransferElo transferElo = new TransferElo(elo);
        modelAndView.addObject("elo", transferElo);
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
