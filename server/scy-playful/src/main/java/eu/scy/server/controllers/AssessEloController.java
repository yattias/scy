package eu.scy.server.controllers;

import eu.scy.core.model.ELORef;
import eu.scy.core.model.playful.PlayfulAssessment;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 08.feb.2010
 * Time: 10:26:12
 * To change this template use File | Settings | File Templates.
 */
public class AssessEloController extends PlayfulController {
	private static Logger log = Logger.getLogger("EloRefsController.class");

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

		String eloId = httpServletRequest.getParameter("id");

		ModelAndView modelAndView = new ModelAndView();

		ELORef eloRef = getEloRefService().getELORefById(eloId);

		// TODO: Get the latest assessments only
		List<PlayfulAssessment> contributions = getPlayfulAssessmentService().getAssesments();

		List<PlayfulAssessment> assessments = getPlayfulAssessmentService().getAssesmentsForELORef(eloRef);

		log.info("Found " + assessments.size() + " assessments for the ELO " + eloRef.getTitle());

		modelAndView.addObject("eloRef", eloRef);

		//TODO: Replace this with a real screenshot of the ELO
		eloRef.setImage("/webapp/themes/scy/default/images/conceptmap.jpg");

		modelAndView.addObject("assessments", assessments);
		modelAndView.addObject("contributions", contributions);
		return modelAndView;
	}
}