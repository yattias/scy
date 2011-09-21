package eu.scy.server.assessment;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.StudentUserDetails;
import eu.scy.core.model.User;
import eu.scy.core.model.transfer.EloAnchorEloPair;
import eu.scy.core.model.transfer.Portfolio;
import eu.scy.core.model.transfer.TransferElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.PedagogicalPlanELOService;
import eu.scy.core.roolo.PortfolioELOService;
import eu.scy.core.roolo.RuntimeELOService;
import eu.scy.server.controllers.BaseController;
import eu.scy.server.util.TransferObjectMapService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 04.sep.2011
 * Time: 08:45:32
 * To change this template use File | Settings | File Templates.
 */
public class WebAssessmentIndex extends BaseController {

    private PortfolioELOService portfolioELOService;
    private RuntimeELOService runtimeELOService;
    private MissionELOService missionELOService;
    private XMLTransferObjectService xmlTransferObjectService;
    private PedagogicalPlanELOService pedagogicalPlanELOService;
    private TransferObjectMapService transferObjectMapService;



    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        URI missionRuntimeURI = getURI(request.getParameter("missionRuntimeURI"));
        MissionRuntimeElo missionRuntimeElo  = MissionRuntimeElo.loadLastVersionElo(missionRuntimeURI, getMissionELOService());
        Portfolio portfolio = getMissionELOService().getPortfolio(missionRuntimeElo, getCurrentUserName(request));
        List<EloAnchorEloPair> eloAnchorEloPairs = portfolio.getEloAnchorEloPairs();
        List<TransferElo> elos = new LinkedList<TransferElo>();
        for (int i = 0; i < eloAnchorEloPairs.size(); i++) {
            EloAnchorEloPair eloAnchorEloPair = eloAnchorEloPairs.get(i);
            elos.add(eloAnchorEloPair.getElo());

        }

        String userName = portfolio.getOwner();
        String portfolioOwner = "";
        if(userName != null) {
            User user = getUserService().getUser(userName);
            StudentUserDetails studentUserDetails = (StudentUserDetails) user.getUserDetails();
            if(studentUserDetails != null) {
                portfolioOwner = studentUserDetails.getFirstName() +  " " + studentUserDetails.getLastName();               
            }
        }

        modelAndView.addObject("elos", elos);
        modelAndView.addObject("missionRuntimeURI", getEncodedUri(missionRuntimeElo.getUri().toString()));
        modelAndView.addObject("portfolioOwner", portfolioOwner);

    }

    public PortfolioELOService getPortfolioELOService() {
        return portfolioELOService;
    }

    public void setPortfolioELOService(PortfolioELOService portfolioELOService) {
        this.portfolioELOService = portfolioELOService;
    }

    public RuntimeELOService getRuntimeELOService() {
        return runtimeELOService;
    }

    public void setRuntimeELOService(RuntimeELOService runtimeELOService) {
        this.runtimeELOService = runtimeELOService;
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

    public PedagogicalPlanELOService getPedagogicalPlanELOService() {
        return pedagogicalPlanELOService;
    }

    public void setPedagogicalPlanELOService(PedagogicalPlanELOService pedagogicalPlanELOService) {
        this.pedagogicalPlanELOService = pedagogicalPlanELOService;
    }

    public TransferObjectMapService getTransferObjectMapService() {
        return transferObjectMapService;
    }

    public void setTransferObjectMapService(TransferObjectMapService transferObjectMapService) {
        this.transferObjectMapService = transferObjectMapService;
    }
}
