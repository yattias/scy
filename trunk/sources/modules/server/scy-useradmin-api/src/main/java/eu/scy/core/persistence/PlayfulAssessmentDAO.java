package eu.scy.core.persistence;

import eu.scy.core.model.ELORef;
import eu.scy.core.model.playful.PlayfulAssessment;

import java.util.List;

/**
 * @author bjoerge
 * @created 23.feb.2010 18:08:01
 */
public interface PlayfulAssessmentDAO extends SCYBaseDAO {
	List<PlayfulAssessment> getAssessments();

	void createAssessment(PlayfulAssessment assessment);

	PlayfulAssessment getAssessmentById(String assessmentId);

	List<PlayfulAssessment> getAssessmentsForELORef(ELORef eloRef);

    Integer getScoreForELORef(ELORef eloRef);
}
