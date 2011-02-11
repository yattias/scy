package eu.scy.server.controllers;

import eu.scy.actionlogging.SQLSpacesActionLogger;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.AssignedPedagogicalPlanService;
import eu.scy.core.GroupService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.UserService;
import eu.scy.core.model.User;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.scy.core.roolo.MissionELOService;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.springframework.web.servlet.ModelAndView;

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

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

        try {

            if(getEportfolioTupleSpace().isConnected()) {
                logger.info("CONNECTED TO THE EPORTFOLIO TUPLE SPACE");
                /*Tuple tuple = new Tuple(String.class, String.class, String.class, String.class);

                try {
                    Tuple [] tuples = getEportfolioTupleSpace().readAll(tuple);
                    logger.info("TUPLES:" + tuples.length);
                    for (int i = 0; i < tuples.length; i++) {
                        Tuple tuple1 = tuples[i];
                        Field[] fields = tuple1.getFields();
                        for (int j = 0; j < fields.length; j++) {
                            Field field = fields[j];
                            logger.info("----> " + field.toString());
                        }
                    }
                } catch (TupleSpaceException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }    */
            };
            

            String uriParam = request.getParameter("uri");
            logger.info("*** **** URI IS : " + uriParam);
            URI uri = new URI(uriParam);

            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(uri, getMissionELOService());
            URI descriptionURI = missionSpecificationElo.getTypedContent().getMissionDescriptionUri();
            modelAndView.addObject("descriptionUrl", descriptionURI);
            logger.info("DESCRIPTION: " + descriptionURI); // HAHAHA I laugh myself to death!

            try {
                URI pedagogicalPlanUri = missionSpecificationElo.getTypedContent().getPedagogicalPlanSettingsEloUri();
                logger.info("**** PEDAGOGICAL PLAN URI: " + pedagogicalPlanUri);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


            Integer globalScaffoldingLevel = getMissionELOService().getGlobalMissionScaffoldingLevel(missionSpecificationElo);
            List portfoliosReadyForAssessment = getMissionELOService().getPortfoliosThatAreReadyForAssessment(missionSpecificationElo);

            modelAndView.addObject("numberOfPortfoliosReadyForAssessment", portfoliosReadyForAssessment.size());
            modelAndView.addObject("missionSpecificationTransporter", getMissionELOService().getWebSafeTransporter(missionSpecificationElo));
            modelAndView.addObject("missionGlobalScaffoldingLevel", globalScaffoldingLevel);

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


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
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
}