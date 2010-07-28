package eu.scy.server.controllers.scyfeedback;

import eu.scy.core.ELORefService;
import eu.scy.core.PlayfulAssessmentService;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.playful.PlayfulAssessmentImpl;
import eu.scy.core.model.playful.PlayfulAssessment;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;


/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.jun.2010
 * Time: 08:39:44
 * To change this template use File | Settings | File Templates.
 */
public class StudentFeedbackFormController extends SimpleFormController {


    private UserService userService;
    private ELORefService eloRefService;
    private PlayfulAssessmentService playfulAssessmentService;

    protected ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors) throws Exception {

        StudentFeedbackBean studentFeedbackBean = (StudentFeedbackBean) command;
        logger.info("SCORE: " + studentFeedbackBean.getScore() + " comment: " + studentFeedbackBean.getComment() + " user " + studentFeedbackBean.getUsername() + " action: " + studentFeedbackBean.getAction() + " modelId " + studentFeedbackBean.getModelId());

        PlayfulAssessment playfulAssessment = new PlayfulAssessmentImpl();
        playfulAssessment.setComment(studentFeedbackBean.getComment());
        playfulAssessment.setDate(new Date(System.currentTimeMillis()));
        playfulAssessment.setReviewer(getCurrentUser(request));
        playfulAssessment.setELORef(getEloRefService().getELORefById(studentFeedbackBean.getModelId()));
        playfulAssessment.setScore(studentFeedbackBean.getScore());
        getPlayfulAssessmentService().save(playfulAssessment);




        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("username", getCurrentUserName(request));

        return modelAndView;

    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = super.handleRequest(request, response);
        modelAndView.addObject("username", getCurrentUserName(request));
        modelAndView.addObject("modelId", request.getParameter("modelId"));
        
        return modelAndView;
    }

     public String getCurrentUserName(HttpServletRequest request) {
        org.springframework.security.userdetails.User user = (org.springframework.security.userdetails.User) request.getSession().getAttribute("CURRENT_USER");
         logger.info("UserName: " + user.getUsername());
        return user.getUsername();
    }

    public User getCurrentUser(HttpServletRequest request) {
        return getUserService().getUser(getCurrentUserName(request));
    }


    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
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
}
