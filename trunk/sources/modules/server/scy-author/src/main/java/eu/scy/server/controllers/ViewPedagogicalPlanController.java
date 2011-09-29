package eu.scy.server.controllers;

import eu.scy.actionlogging.SQLSpacesActionLogger;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.*;
import eu.scy.core.model.User;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.roolo.MissionELOService;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.feb.2010
 * Time: 09:46:42
 */
public class ViewPedagogicalPlanController extends BaseController {

    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;
    private AssignedPedagogicalPlanService assignedPedagogicalPlanService;
    private UserService userService;
    private GroupService groupService = null;
    private MissionELOService missionELOService;
    private TupleSpace eportfolioTupleSpace;
    private XMLTransferObjectService xmlTransferObjectService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

        URI uri = getURI(request.getParameter(ELO_URI));
        if(uri == null) {
            uri = getURI((String) request.getAttribute(ELO_URI));
        }

        MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(uri, getMissionELOService());
        PedagogicalPlanTransfer pedagogicalPlanTransfer = null;
        

        try {
            URI pedagogicalPlanUri = missionSpecificationElo.getTypedContent().getPedagogicalPlanSettingsEloUri();

            ScyElo pedagogicalPlanELO = ScyElo.loadLastVersionElo(pedagogicalPlanUri, getMissionELOService());
            String pedagogicalPlanXML = pedagogicalPlanELO.getContent().getXmlString();
            if (pedagogicalPlanXML != null && pedagogicalPlanXML.length() > 0) {
                pedagogicalPlanTransfer = (PedagogicalPlanTransfer) getXmlTransferObjectService().getObject(pedagogicalPlanXML);
            } else {
                pedagogicalPlanTransfer = new PedagogicalPlanTransfer();
                pedagogicalPlanTransfer.setName(missionSpecificationElo.getTitle());
                pedagogicalPlanTransfer.setPedagogicalPlanURI(pedagogicalPlanUri.toString());

                pedagogicalPlanELO.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
                pedagogicalPlanELO.updateElo();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        modelAndView.addObject("missionSpecificationTransporter", getMissionELOService().getWebSafeTransporter(missionSpecificationElo));
        modelAndView.addObject("pedagogicalPlan", pedagogicalPlanTransfer);



    }



    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public AssignedPedagogicalPlanService getAssignedPedagogicalPlanService() {
        return assignedPedagogicalPlanService;
    }

    public void setAssignedPedagogicalPlanService(AssignedPedagogicalPlanService assignedPedagogicalPlanService) {
        this.assignedPedagogicalPlanService = assignedPedagogicalPlanService;
    }

    public GroupService getGroupService() {
        return groupService;
    }

    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    public String getCurrentUserName(HttpServletRequest request) {
        org.springframework.security.userdetails.User user = (org.springframework.security.userdetails.User) request.getSession().getAttribute("CURRENT_USER");
        logger.info("UserName: " + user.getUsername());
        return user.getUsername();
    }

    public User getCurrentUser(HttpServletRequest request) {
        return getUserService().getUser(getCurrentUserName(request));
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public TupleSpace getEportfolioTupleSpace() {
        return eportfolioTupleSpace;
    }

    public void setEportfolioTupleSpace(TupleSpace eportfolioTupleSpace) {
        this.eportfolioTupleSpace = eportfolioTupleSpace;
    }

    public XMLTransferObjectService getXmlTransferObjectService() {
        return xmlTransferObjectService;
    }

    public void setXmlTransferObjectService(XMLTransferObjectService xmlTransferObjectService) {
        this.xmlTransferObjectService = xmlTransferObjectService;
    }
}