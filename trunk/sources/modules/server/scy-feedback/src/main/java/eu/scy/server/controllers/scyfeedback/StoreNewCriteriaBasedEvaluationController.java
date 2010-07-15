package eu.scy.server.controllers.scyfeedback;

import eu.scy.core.AssessmentService;
import eu.scy.core.UserService;
import eu.scy.core.model.AssessmentCriteria;
import eu.scy.core.model.User;
import eu.scy.server.controllers.BaseFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jul.2010
 * Time: 06:32:17
 * To change this template use File | Settings | File Templates.
 */
public class StoreNewCriteriaBasedEvaluationController extends BaseFormController {

    private UserService userService;
    private AssessmentService assessmentService;

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ModelAndView modelAndView = new ModelAndView();

        CriteriaBasedEvaluationBean bean = (CriteriaBasedEvaluationBean) command;
        logger.info("CRITERIA TEXT: " + bean.getCriteriaText());
        logger.info("ANCHOR ELO:" + bean.getModelId());
        logger.info("evaluationCriteriaId" + request.getParameter("evaluationCriteriaId"));

        AssessmentCriteria criteria = getAssessmentService().getAssessmentCriteria(request.getParameter("evaluationCriteriaId"));
        User user = getCurrentUser(request);

        getAssessmentService().createOrUpdateAssessmentCriteriaExperience(user, criteria, bean.getCriteriaText(), Integer.getInteger(bean.getScore()));




        modelAndView.setViewName("forward:studentEloRefViewer.html");
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

    public AssessmentService getAssessmentService() {
        return assessmentService;
    }

    public void setAssessmentService(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }
}
