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

        MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(uri, getMissionELOService());
        PedagogicalPlanTransfer pedagogicalPlanTransfer = null;
        

        try {
            URI pedagogicalPlanUri = missionSpecificationElo.getTypedContent().getPedagogicalPlanSettingsEloUri();
            logger.info("**** PEDAGOGICAL PLAN URI: " + pedagogicalPlanUri);

            ScyElo pedagogicalPlanELO = ScyElo.loadLastVersionElo(pedagogicalPlanUri, getMissionELOService());
            String pedagogicalPlanXML = pedagogicalPlanELO.getContent().getXmlString();
            if (pedagogicalPlanXML != null && pedagogicalPlanXML.length() > 0) {
                logger.info("Found existing pedagigical plan!");
                pedagogicalPlanTransfer = (PedagogicalPlanTransfer) getXmlTransferObjectService().getObject(pedagogicalPlanXML);
            } else {
                logger.info("Did not find pedagogical plan - creating one....");
                pedagogicalPlanTransfer = new PedagogicalPlanTransfer();
                pedagogicalPlanTransfer.setName(missionSpecificationElo.getTitle());
                pedagogicalPlanTransfer.setPedagogicalPlanURI(pedagogicalPlanUri.toString());

                pedagogicalPlanELO.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
                pedagogicalPlanELO.updateElo();
            }


        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        Integer globalScaffoldingLevel = getMissionELOService().getGlobalMissionScaffoldingLevel(missionSpecificationElo);
        //List portfoliosReadyForAssessment = getMissionELOService().getPortfoliosThatAreReadyForAssessment(missionSpecificationElo);
        //List assignedUsernames = getMissionELOService().getAssignedUserNamesFor(missionSpecificationElo);

        //modelAndView.addObject("numberOfStudentsAssigned" , assignedUsernames.size());
        //modelAndView.addObject("numberOfPortfoliosReadyForAssessment", portfoliosReadyForAssessment.size());
        modelAndView.addObject("missionSpecificationTransporter", getMissionELOService().getWebSafeTransporter(missionSpecificationElo));
        modelAndView.addObject("missionGlobalScaffoldingLevel", globalScaffoldingLevel);
        modelAndView.addObject("pedagogicalPlan", pedagogicalPlanTransfer);

        String action = request.getParameter("action");
        if (action != null) {
            if (action.equals("increaseScaffoldingLevel")) {
                increaseScaffoldingLevel(request, response, modelAndView, missionSpecificationElo);

            }

        }

        List agentLevels = new LinkedList();
        agentLevels.add("Low");
        agentLevels.add("Medium");
        agentLevels.add("High");
        modelAndView.addObject("agentLevels", agentLevels);

        modelAndView.addObject("scaffoldingLevel", globalScaffoldingLevel);
        modelAndView.addObject("rooloServices", getMissionELOService());


    }


    private void increaseScaffoldingLevel(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView, ScyElo elo) {
        logger.info("INCREASING SCAFFOLDING LEVEL for " + elo.getTitle());
        MissionSpecificationElo missionSpecificationElo = (MissionSpecificationElo) elo;
        logger.info("LEVEL: " + getMissionELOService().getGlobalMissionScaffoldingLevel(missionSpecificationElo));
        getMissionELOService().setGlobalMissionScaffoldingLevel((MissionSpecificationElo) elo, (4 + getMissionELOService().getGlobalMissionScaffoldingLevel(missionSpecificationElo)));
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