package eu.scy.server.controllers.scyfeedback;

import eu.scy.core.ELORefService;
import eu.scy.core.FileService;
import eu.scy.core.PlayfulAssessmentService;
import eu.scy.core.UserService;
import eu.scy.core.model.ELORef;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.playful.PlayfulAssessmentImpl;
import eu.scy.core.model.playful.PlayfulAssessment;
import eu.scy.server.common.OddEven;
import eu.scy.server.controllers.*;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.jun.2010
 * Time: 06:31:39
 * To change this template use File | Settings | File Templates.
 */
public class StudentEloRefViewerController  extends BaseFormController {

    private FileService fileService;
    private ELORefService eloRefService;
    private PlayfulAssessmentService playfulAssessmentService;
    private UserService userService;

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        logger.info("ON SUBMIT!!");
        logger.info("ON SUBMIT!!");
        logger.info("ON SUBMIT!!");
        logger.info("ON SUBMIT!!");

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

        prepareNextPage(request, response, modelAndView);

        return modelAndView;

    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = super.handleRequest(request, response);
        modelAndView.addObject("username", getCurrentUserName(request));
        modelAndView.addObject("oddEven", new OddEven());


        prepareNextPage(request, response, modelAndView);

        return modelAndView;
    }

    private void prepareNextPage(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        setModel(getUrlInspector().instpectRequest(request,response));

        logger.info("MODEL  IS: " + getModel());

        ELORefDataTransporter transporter = new ELORefDataTransporter();
        transporter.setEloRef((ELORef) getModel());
        ELORef eloRef = (ELORef) getModel();
        eloRef.setViewings(eloRef.getViewings() + 1);
        getEloRefService().save(eloRef);
        transporter.setFiles(getFileService().getFilesForELORef(eloRef));
        transporter.setTotalScore(getPlayfulAssessmentService().getScoreForELORef(eloRef));

        transporter.setAssessments(getPlayfulAssessmentService().getAssesmentsForELORef(eloRef));
        logger.info("I found " + getPlayfulAssessmentService().getAssesmentsForELORef(eloRef).size() + " ASSESSMENTS!!");

        modelAndView.addObject("transporter", transporter);
        String encodedModel = eloRef.getClass().getName() + "_" + eloRef.getId();
        modelAndView.addObject("encodedModel", encodedModel);
        modelAndView.addObject("modelId", eloRef.getId());
        
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


}
