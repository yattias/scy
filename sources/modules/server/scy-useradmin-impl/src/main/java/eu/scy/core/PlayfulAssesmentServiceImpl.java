package eu.scy.core;

import eu.scy.core.model.playful.PlayfulAssessment;
import eu.scy.core.persistence.PlayfulAssessmentDAO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author bjoerge
 * @created 23.feb.2010 14:40:28
 */
public class PlayfulAssesmentServiceImpl extends BaseServiceImpl implements PlayfulAssessmentService {

	private static Logger log = Logger.getLogger("PlayfulAssessmentService.class");

	private PlayfulAssessmentDAO playfulAssessmentDAO;

	public List getAssessments() {
		return getPlayfulAssessmentDAO().getAssessments();
	}

	@Transactional
	public void createAssessment(PlayfulAssessment assessment) {
		getPlayfulAssessmentDAO().createAssessment(assessment);
	}

	@Override
	public List<PlayfulAssessment> getAssesments() {
		return getPlayfulAssessmentDAO().getAssessments();
	}

	@Override
	public void save(PlayfulAssessment scenario) {
		getPlayfulAssessmentDAO().save(scenario);
	}

	public PlayfulAssessmentDAO getPlayfulAssessmentDAO() {
		return (PlayfulAssessmentDAO) getScyBaseDAO();
	}

	public void setPlayfulAssessmentDAO(PlayfulAssessmentDAO playfulAssessmentDAO) {
		this.playfulAssessmentDAO = playfulAssessmentDAO;
	}
}
