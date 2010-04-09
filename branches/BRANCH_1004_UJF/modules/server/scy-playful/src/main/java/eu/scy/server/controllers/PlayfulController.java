package eu.scy.server.controllers;

import eu.scy.core.ELORefService;
import eu.scy.core.PlayfulAssessmentService;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author bjoerge
 * @created 25.feb.2010 10:55:24
 */
public abstract class PlayfulController extends AbstractController {
	private PlayfulAssessmentService assessmentService;
	private ELORefService eloRefService;

	public void setPlayfulAssessmentService(PlayfulAssessmentService assessmentService) {
		this.assessmentService = assessmentService;
	}

	public PlayfulAssessmentService getPlayfulAssessmentService() {
		return assessmentService;
	}

	public void setELORefService(ELORefService eloRefService) {
		this.eloRefService = eloRefService;
	}

	public ELORefService getEloRefService() {
		return eloRefService;
	}
}
