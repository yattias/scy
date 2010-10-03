package eu.scy.server.controllers;

import eu.scy.core.PlayfulAssessmentService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 08.feb.2010
 * Time: 10:26:12
 * To change this template use File | Settings | File Templates.
 */
public class PlayfulIndexController extends AbstractController {
	private static Logger log = Logger.getLogger("PlayfulIndexController.class");
	private PlayfulAssessmentService assessmentService;

	public void setPlayfulAssessmentService(PlayfulAssessmentService assessmentService) {
		this.assessmentService = assessmentService;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
		ModelAndView modelAndView = new ModelAndView();

		log.info("################################### I'M FEELING PLAYFUL");

		log.info("################################### MY ASSESSMENTSERVICE IS : " + assessmentService);

		return modelAndView;
	}
}
