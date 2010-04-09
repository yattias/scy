package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.ELORef;
import eu.scy.core.model.playful.PlayfulAssessment;
import eu.scy.core.persistence.PlayfulAssessmentDAO;

import java.util.List;

/**
 * @author bjoerge
 * @created 23.feb.2010 14:39:13
 */
public class PlayfulAssessmentDAOHibernate extends ScyBaseDAOHibernate implements PlayfulAssessmentDAO {

	@Override
	public List<PlayfulAssessment> getAssessments() {
		return getSession().createQuery("From PlayfulAssessmentImpl order by name")
				.list();
	}

	@Override
	public void createAssessment(PlayfulAssessment assessment) {
		getHibernateTemplate().save(assessment);
	}

	@Override
	public PlayfulAssessment getAssessmentById(String assessmentId) {
		return (PlayfulAssessment) getSession().createQuery("from PlayfulAssessmentImpl where id = :id").setString("id", assessmentId)
				.setMaxResults(1)
				.uniqueResult();
	}

	@Override
	public List<PlayfulAssessment> getAssessmentsForELORef(ELORef eloRef) {
		return getSession().createQuery("from PlayfulAssessmentImpl where ELORef = :eloRef")
				.setEntity("eloRef", eloRef)
				.list();
	}
}
