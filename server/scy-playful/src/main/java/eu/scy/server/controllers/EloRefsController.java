package eu.scy.server.controllers;

import eu.scy.core.ELORefService;
import eu.scy.core.PlayfulAssessmentService;
import eu.scy.core.model.ELORef;
import eu.scy.core.model.impl.ELORefImpl;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

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
public class EloRefsController extends PlayfulController {
	private static Logger log = Logger.getLogger("EloRefsController.class");

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
		ModelAndView modelAndView = new ModelAndView();

		List<ELORef> eloRefs = getEloRefService().getELORefs();

		modelAndView.addObject("elorefs", eloRefs);
		return modelAndView;
	}
}
