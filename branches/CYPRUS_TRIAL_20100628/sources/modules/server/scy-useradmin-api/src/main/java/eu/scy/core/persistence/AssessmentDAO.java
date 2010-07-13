package eu.scy.core.persistence;

import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.Assessment;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.jan.2010
 * Time: 05:45:08
 * To change this template use File | Settings | File Templates.
 */
public interface AssessmentDAO extends SCYBaseDAO{
    Assessment findAssessmentByName(String s);

    void addCriteria(Assessment assessment);

    void addAssessment(AnchorELO anchorELO);

    void addScoreDefinition(Assessment assessment);
}
