package eu.scy.core;

import eu.scy.core.model.ELORef;
import eu.scy.core.model.playful.PlayfulAssessment;

import java.util.List;

/**
 * @author bjoerge
 * @created 23.feb.2010 18:00:44
 */
public interface PlayfulAssessmentService extends BaseService {
	List<PlayfulAssessment> getAssesments();

	PlayfulAssessment getAssessmentById(String assessmentId);

	List<PlayfulAssessment> getAssesmentsForELORef(ELORef eloRef);

    public Integer getScoreForELORef(ELORef eloRef);

	void save(PlayfulAssessment playfulAssessment);
}
