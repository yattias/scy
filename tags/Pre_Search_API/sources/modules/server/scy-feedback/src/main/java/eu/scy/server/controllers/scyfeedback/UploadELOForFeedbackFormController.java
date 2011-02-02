package eu.scy.server.controllers.scyfeedback;

import eu.scy.core.*;
import eu.scy.core.model.ELORef;
import eu.scy.core.model.FileRef;
import eu.scy.core.model.ImageRef;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.ELORefImpl;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.AssignedPedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.runtime.RuntimeService;
import eu.scy.server.controllers.BaseController;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.jun.2010
 * Time: 12:05:14
 * To change this template use File | Settings | File Templates.
 */
public class UploadELOForFeedbackFormController extends SimpleFormController {

    private RuntimeService runtimeService;
    private UserService userService;
    private AssignedPedagogicalPlanService assignedPedagogicalPlanService;
    private ELORefService eloRefService;
    private MissionService missionService;
    private FileService fileService;
    private AnchorELOService anchorELOService;
    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;

    protected ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors) throws Exception {

        UploadELOBean eloBean = (UploadELOBean) command;

        logger.info("command object: " + command);
        logger.info("Uploaded file: " + eloBean.getFile());
        if (eloBean.getFile() != null) {
            logger.info("Uploaded file: " + eloBean.getFile().getOriginalFilename());


        }


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:ScyFeedbackIndex.html");
        

        String username = eloBean.getUsername();
        String action = eloBean.getAction();

        if (action != null) {
            if (action.equals("addNewEloRef")) addNewEloRef(request, eloBean);
        }

        if (username != null) {
            User user = getUserService().getUser(username);

            logger.info("User is : " + user);

            String currentElo = getRuntimeService().getCurrentELO(user);
            String tool = getRuntimeService().getCurrentTool(user);
            String las = getRuntimeService().getCurrentLAS(user);

            List assignedPedagogicalPlans = getAssignedPedagogicalPlanService().getAssignedPedagogicalPlans(user);

            List missions = new LinkedList();
            for (int i = 0; i < assignedPedagogicalPlans.size(); i++) {
                AssignedPedagogicalPlan assignedPedagogicalPlan = (AssignedPedagogicalPlan) assignedPedagogicalPlans.get(i);
                missions.add(assignedPedagogicalPlan.getPedagogicalPlan().getMission());
            }

            modelAndView.addObject("missions", missions);

            if (missions != null && missions.size() > 0) modelAndView.addObject("firstMission", missions.get(0));

            List statuses = new LinkedList();
            statuses.add("Under construction");
            statuses.add("Finished");


            modelAndView.addObject("statuses", statuses);
            modelAndView.addObject("currentELO", currentElo);
            modelAndView.addObject("currentTool", tool);
            modelAndView.addObject("currentLas", las);


            modelAndView.addObject("currentUser", user);

        }

        return modelAndView;

    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = super.handleRequest(request, response);
        logger.warn("NEW REQUEST!!");
        // System.out.println("yyyyyytea!");
        String username = request.getParameter("username");

        if (username != null) {
            User user = getUserService().getUser(username);

            logger.info("User is : " + user);

            String currentElo = getRuntimeService().getCurrentELO(user);
            String tool = getRuntimeService().getCurrentTool(user);
            String las = getRuntimeService().getCurrentLAS(user);

            List assignedPedagogicalPlans = getAssignedPedagogicalPlanService().getAssignedPedagogicalPlans(user);
            List elosToBeAssessed = new LinkedList();

            List missions = new LinkedList();
            for (int i = 0; i < assignedPedagogicalPlans.size(); i++) {
                AssignedPedagogicalPlan assignedPedagogicalPlan = (AssignedPedagogicalPlan) assignedPedagogicalPlans.get(i);
                missions.add(assignedPedagogicalPlan.getPedagogicalPlan().getMission());
                if(assignedPedagogicalPlan.getPedagogicalPlan().getLimitNumberOfELOsAvailableForPeerAssessment()) {
                    List anchorElos = getPedagogicalPlanPersistenceService().getAnchorELOs(assignedPedagogicalPlan.getPedagogicalPlan());
                    for (int j = 0; j < anchorElos.size(); j++) {
                        AnchorELO anchorELO = (AnchorELO) anchorElos.get(j);
                        elosToBeAssessed.add(anchorELO);
                    }
                }
            }

            modelAndView.addObject("missions", missions);
            modelAndView.addObject("elosToBeAssessed", elosToBeAssessed);

            if (missions != null && missions.size() > 0) modelAndView.addObject("firstMission", missions.get(0));

            List statuses = new LinkedList();
            statuses.add("Under construction");
            statuses.add("Finished");


            modelAndView.addObject("statuses", statuses);
            
            modelAndView.addObject("currentELO", currentElo);
            modelAndView.addObject("currentTool", tool);
            modelAndView.addObject("currentLas", las);


            modelAndView.addObject("currentUser", user);

        }

        return modelAndView;
    }

    @Override
    protected void onBindOnNewForm(HttpServletRequest request, Object command, BindException errors) throws Exception {
        super.onBindOnNewForm(request, command, errors);    //To change body of overridden methods use File | Settings | File Templates.
        logger.info("BINDING NEW FORM!!!!");
    }

    private void addNewEloRef(HttpServletRequest request, UploadELOBean eloBean) {
        logger.info("ADDING NEW ELO REF!!");

        User user = getUserService().getUser(request.getParameter("username"));

        AnchorELO elo = getAnchorELOService().getAnchorELO(request.getParameter("anchorEloId"));

        ELORef eloRef = new ELORefImpl();
        eloRef.setAuthor(user);
        eloRef.setTool(request.getParameter("tool"));
        eloRef.setName(request.getParameter("name"));
        eloRef.setTitle(elo.getHumanReadableName());
        eloRef.setType(elo.getHumanReadableName());
        eloRef.setMission(getMissionService().getMission(request.getParameter("mission")));
        eloRef.setDate(new Date());
        eloRef.setComment(request.getParameter("comment"));
        eloRef.setAnchorELO(elo);

        getEloRefService().save(eloRef);


        MultipartFile file = eloBean.getFile();
        File tmpFile = null;
        try {
            tmpFile = new File(file.getOriginalFilename());
            tmpFile.deleteOnExit();
            file.transferTo(tmpFile);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        if (file.getContentType().contains("image")) {
            FileRef fileRef = getFileService().saveFile(tmpFile);
            getFileService().addFileToELORef(fileRef, eloRef);
        } else {
            FileRef fileRef = getFileService().saveFile(tmpFile);
            getFileService().addFileToELORef(fileRef, eloRef);
        }
    }

    public RuntimeService getRuntimeService() {
        return runtimeService;
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
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

    public ELORefService getEloRefService() {
        return eloRefService;
    }

    public void setEloRefService(ELORefService eloRefService) {
        this.eloRefService = eloRefService;
    }

    public MissionService getMissionService() {
        return missionService;
    }

    public void setMissionService(MissionService missionService) {
        this.missionService = missionService;
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    public AnchorELOService getAnchorELOService() {
        return anchorELOService;
    }

    public void setAnchorELOService(AnchorELOService anchorELOService) {
        this.anchorELOService = anchorELOService;
    }

    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }
}
