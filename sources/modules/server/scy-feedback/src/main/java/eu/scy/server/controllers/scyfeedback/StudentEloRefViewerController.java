package eu.scy.server.controllers.scyfeedback;

import eu.scy.core.*;
import eu.scy.core.model.AssessmentCriteria;
import eu.scy.core.model.ELORef;
import eu.scy.core.model.ScyBase;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.ImageRefImpl;
import eu.scy.core.model.impl.playful.PlayfulAssessmentImpl;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.AssessmentCriteriaExperience;
import eu.scy.core.model.pedagogicalplan.AssignedPedagogicalPlan;
import eu.scy.core.model.playful.PlayfulAssessment;
import eu.scy.core.roolo.RooloAccessor;
import eu.scy.server.controllers.ui.OddEven;
import eu.scy.server.controllers.BaseFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URLDecoder;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.jun.2010
 * Time: 06:31:39
 */
public class StudentEloRefViewerController  extends BaseFormController {

    private FileService fileService;
    private ELORefService eloRefService;
    private PlayfulAssessmentService playfulAssessmentService;
    private UserService userService;
    private AssignedPedagogicalPlanService assignedPedagogicalPlanService;
    private AssessmentService assessmentService;
    private RooloAccessor rooloAccessor;

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        StudentFeedbackBean studentFeedbackBean = (StudentFeedbackBean) command;
        logger.info("SCORE: " + studentFeedbackBean.getScore() + " comment: " + studentFeedbackBean.getComment() + " user " + studentFeedbackBean.getUsername() + " action: " + studentFeedbackBean.getAction() + " modelId " + studentFeedbackBean.getModelId());

        ELORef eloRef = getEloRefService().getELORefById(studentFeedbackBean.getModelId());
        logger.info("ELORef is: " + eloRef);

        PlayfulAssessment playfulAssessment = new PlayfulAssessmentImpl();
        playfulAssessment.setComment(studentFeedbackBean.getComment());
        playfulAssessment.setDate(new Date(System.currentTimeMillis()));
        playfulAssessment.setReviewer(getCurrentUser(request));
        playfulAssessment.setELORef(getEloRefService().getELORefById(studentFeedbackBean.getModelId()));
        playfulAssessment.setScore(studentFeedbackBean.getScore());
        getPlayfulAssessmentService().save(playfulAssessment);


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("username", getCurrentUserName(request));
        modelAndView.addObject("username", getCurrentUserName(request));
        modelAndView.addObject("modelId", request.getParameter("modelId"));
        modelAndView.addObject("oddEven", new OddEven());

        prepareNextPage(request, response, modelAndView);

        return modelAndView;

    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = super.handleRequest(request, response);
        modelAndView.addObject("username", getCurrentUserName(request));
        modelAndView.addObject("oddEven", new OddEven());

        String uriParam = request.getParameter("uri");
        logger.info("LOADING URI: " + uriParam);
        uriParam = URLDecoder.decode(uriParam, "UTF-8");
        logger.info("DECODED:  "+ uriParam);
        if(uriParam != null) {
            URI uri = new URI(uriParam);
            modelAndView.addObject("elo", getRooloAccessor().getElo(uri));
        }



        //prepareNextPage(request, response, modelAndView);

        return modelAndView;
    }

    private void prepareNextPage(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        setModel((ScyBase) getUrlInspector().instpectRequest(request,response));

        logger.info("MODEL  IS: " + getModel());

        if(request.getParameter("action") != null) {
            if(request.getParameter("action").equals("addAssessment")) addAssessment(request, modelAndView);
            else if(request.getParameter("action").equals("delete") ) deleteELO(getModel(), modelAndView);
        }

        ELORefDataTransporter transporter = new ELORefDataTransporter();
        ELORef eloRef = (ELORef) getModel();

        if(eloRef == null) {
            if(request.getParameter("eloRefId") != null) {
                eloRef = getEloRefService().getELORefById(request.getParameter("eloRefId"));
                setModel(eloRef);
            }

        } else {
            eloRef.setViewings(eloRef.getViewings() + 1);
        }
        transporter.setEloRef((ELORef) getModel());

        getEloRefService().save(eloRef);
        transporter.setFiles(getFileService().getFilesForELORef(eloRef));
        transporter.setTotalScore(getPlayfulAssessmentService().getScoreForELORef(eloRef));




        if(eloRef.getAuthor() != null || getCurrentUser(request).getUserDetails().hasGrantedAuthority("ROLE_TEACHER") ) {
            if(eloRef.getAuthor() != null && eloRef.getAuthor().getUserDetails().getUsername().equals(getCurrentUser(request).getUserDetails().getUsername())) {
                logger.info("This user can delete elo - setting permissions");
                modelAndView.addObject("CAN_DELETE_MODEL", true);
            } else {
                modelAndView.addObject("CAN_DELETE_MODEL", false);
            }

            //Override if teacher:
            if(getCurrentUser(request).getUserDetails().hasGrantedAuthority("ROLE_TEACHER")) {
                modelAndView.addObject("CAN_DELETE_MODEL", true);
            }
        }



        transporter.setAssessments(getPlayfulAssessmentService().getAssesmentsForELORef(eloRef));
        logger.info("I found " + getPlayfulAssessmentService().getAssesmentsForELORef(eloRef).size() + " ASSESSMENTS!!");

        AssignedPedagogicalPlan assignedPedagogicalPlan = getAssignedPedagogicalPlanService().getCurrentAssignedPedagogicalPlan(getCurrentUser(request));
        if(assignedPedagogicalPlan != null)  {
            transporter.setUseCriteriaBasedAssessment(assignedPedagogicalPlan.getUseCriteriaBasedAssessment());
            transporter.setScoreImageId(((ImageRefImpl)assignedPedagogicalPlan.getPedagogicalPlan().getAssessmentScoreIcon()).getId());
        }

        AnchorELO elo = eloRef.getAnchorELO();
        if(elo != null) {
            if(elo.getAssessment() != null) {
                transporter.setEvaluationCriteria(elo.getAssessment().getAssessmentCriterias());
                List criteriaExperienceHolders = new LinkedList();
                List criterias = elo.getAssessment().getAssessmentCriterias();
                for (int i = 0; i < criterias.size(); i++) {
                    AssessmentCriteria assessmentCriteria = (AssessmentCriteria) criterias.get(i);
                    CriteriaAndExperienceHolder criteriaAndExperienceHolder = new CriteriaAndExperienceHolder();
                    criteriaAndExperienceHolder.setCriteria(assessmentCriteria);
                    AssessmentCriteriaExperience experience = getAssessmentService().getAssessmentCriteriaExperience(getCurrentUser(request), assessmentCriteria);
                    criteriaAndExperienceHolder.setAssessmentCriteriaExperience(experience);
                    logger.info("ADDED holder.  " + criteriaAndExperienceHolder.getCriteriaText());
                    criteriaExperienceHolders.add(criteriaAndExperienceHolder);
                }

                transporter.setAssessmentScoreDefinitions(elo.getAssessment().getAssessmentScoreDefinitions());
                transporter.setCriteriaAndExperienceHolders(criteriaExperienceHolders);
            }
        }


        modelAndView.addObject("transporter", transporter);
        String encodedModel = eloRef.getClass().getName() + "_" + eloRef.getId();
        modelAndView.addObject("encodedModel", encodedModel);
        modelAndView.addObject("modelId", eloRef.getId());
        
    }

    private void deleteELO(ScyBase model, ModelAndView modelAndView) {
        getEloRefService().delete((ELORef) model);
        modelAndView.setViewName("forward:ScyFeedbackIndex.html");
    }

    private void addAssessment(HttpServletRequest request, ModelAndView modelAndView) {
        logger.info("Adding asssessment!");
        
    }


    public User getCurrentUser(HttpServletRequest request) {
        return getUserService().getUser(getCurrentUserName(request));
    }

    public String getCurrentUserName(HttpServletRequest request) {
       org.springframework.security.userdetails.User user = (org.springframework.security.userdetails.User) request.getSession().getAttribute("CURRENT_USER");
       return user.getUsername();
   }



    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    public ELORefService getEloRefService() {
        return eloRefService;
    }

    public void setEloRefService(ELORefService eloRefService) {
        this.eloRefService = eloRefService;
    }

    public PlayfulAssessmentService getPlayfulAssessmentService() {
        return playfulAssessmentService;
    }

    public void setPlayfulAssessmentService(PlayfulAssessmentService playfulAssessmentService) {
        this.playfulAssessmentService = playfulAssessmentService;
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

    public AssessmentService getAssessmentService() {
        return assessmentService;
    }

    public void setAssessmentService(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    public RooloAccessor getRooloAccessor() {
        return rooloAccessor;
    }

    public void setRooloAccessor(RooloAccessor rooloAccessor) {
        this.rooloAccessor = rooloAccessor;
    }
}
