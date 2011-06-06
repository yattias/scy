package eu.scy.server.controllers.scyfeedback;

import eu.scy.core.ELORefService;
import eu.scy.core.PlayfulAssessmentService;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.jul.2010
 * Time: 08:00:54
 * To change this template use File | Settings | File Templates.
 */
public class StoreELORefCommentController extends SimpleFormController {

    private ELORefService eloRefService;
    private PlayfulAssessmentService playfulAssessmentService;
    private UserService userService;

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        /*StudentFeedbackBean studentFeedbackBean = (StudentFeedbackBean) command;
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

                      */
        ModelAndView modelAndView = new ModelAndView();
        /*modelAndView.addObject("username", getCurrentUserName(request));
        modelAndView.addObject("username", getCurrentUserName(request));
        modelAndView.addObject("modelId", request.getParameter("modelId"));
        modelAndView.addObject("oddEven", new OddEven());*/
        modelAndView.setViewName("forward:studentEloRefViewer.html");

        return modelAndView;
    }

    public User getCurrentUser(HttpServletRequest request) {
        return getUserService().getUser(getCurrentUserName(request));
    }

    public String getCurrentUserName(HttpServletRequest request) {
       org.springframework.security.userdetails.User user = (org.springframework.security.userdetails.User) request.getSession().getAttribute("CURRENT_USER");
       return user.getUsername();
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
