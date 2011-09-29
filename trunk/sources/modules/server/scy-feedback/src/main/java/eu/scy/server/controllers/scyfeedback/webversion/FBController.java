package eu.scy.server.controllers.scyfeedback.webversion;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.core.model.User;
import eu.scy.core.model.transfer.TransferElo;
import eu.scy.core.roolo.FeedbackEloSearchFilter;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 04.jul.2011
 * Time: 06:59:53
 * To change this template use File | Settings | File Templates.
 */
public class FBController extends BaseController {

    private MissionELOService missionELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String uri = request.getParameter(ELO_URI);
        URI runtimeURI = getURI(uri);
        MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(runtimeURI, getMissionELOService());

        String criteria = request.getParameter("criteria");
        String anchorElo = request.getParameter("anchorElo");
        String user = request.getParameter("user");

        if(criteria == null) criteria = "NEWEST";

        FeedbackEloSearchFilter filter = getMissionELOService().createFeedbackEloSearchFilter();
        if(user != null && user.equalsIgnoreCase("MINE")) user = getCurrentUserName(request);
        filter.setCriteria(criteria);
        filter.setCategory(anchorElo);
        filter.setOwner(user);

        List<TransferElo> elos = getMissionELOService().getElosForFeedback(missionRuntimeElo, getCurrentUserName(request), filter);
        logger.info("ELOS: " + elos.size());

        if(user == null) user = "ALL";
        if(user.equals(getCurrentUserName(request))) user = "MINE";

        List<TransferElo> allElos = getMissionELOService().getElosForFeedback(missionRuntimeElo, getCurrentUserName(request), null);
        List<String> allUserNames = new LinkedList<String>();
        List<String> allEloNames = new LinkedList<String>();
        for (int i = 0; i < allElos.size(); i++) {
            TransferElo transferElo = allElos.get(i);
            if(!allUserNames.contains(transferElo.getCreatedBy())) {
                allUserNames.add(transferElo.getCreatedBy());
            }
            if(!allEloNames.contains(transferElo.getMyname())) {
                allEloNames.add(transferElo.getMyname());
            }
        }

        List <User> users = new LinkedList<User>();
        for (int i = 0; i < allUserNames.size(); i++) {
            String s = allUserNames.get(i);
            User uzz = getUserService().getUser(s);
            users.add(uzz);
        }



        modelAndView.addObject("elos", elos);
        modelAndView.addObject("anchorElos", getMissionELOService().getAnchorELOs(getMissionELOService().getMissionSpecificationELOForRuntume(missionRuntimeElo)));
        modelAndView.addObject(ELO_URI, getEncodedUri(runtimeURI.toString()));
        modelAndView.addObject("criteria", criteria);
        modelAndView.addObject("uzer", user);
        modelAndView.addObject("uzers", users);
        modelAndView.addObject("allEloNames", allEloNames);
        modelAndView.addObject("anchorElo", anchorElo);
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
