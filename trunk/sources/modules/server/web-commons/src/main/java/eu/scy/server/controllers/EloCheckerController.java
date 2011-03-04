package eu.scy.server.controllers;

import eu.scy.server.controllers.formutils.EloUriForm;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.feb.2011
 * Time: 10:51:38
 * To change this template use File | Settings | File Templates.
 */
public class EloCheckerController extends SimpleFormController {

    public EloCheckerController() {
    }

    @Override
    protected ModelAndView onSubmit(Object command) throws Exception {
        logger.info("SUBMIT!");
        return super.onSubmit(command);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        /*EloUriForm eloUriForm = (EloUriForm) command;
        logger.info("RECEIVED AN ELOCHECK");
        if(eloUriForm != null) {
        logger.info("ELO URI: " +eloUriForm.getEloURI());    
        }
         */

        return new ModelAndView("/components/util/EloChecker");
    }
}
