package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.AssessmentCriteria;
import eu.scy.core.model.impl.pedagogicalplan.AssessmentCriteriaImpl;
import eu.scy.core.model.impl.pedagogicalplan.AssessmentImpl;
import eu.scy.core.model.impl.pedagogicalplan.AssessmentScoreDefinitionImpl;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.Assessment;
import eu.scy.core.model.pedagogicalplan.AssessmentScoreDefinition;
import eu.scy.core.persistence.AssessmentDAO;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.jan.2010
 * Time: 05:45:50
 * To change this template use File | Settings | File Templates.
 */
public class AssessmentDAOHibernate extends ScyBaseDAOHibernate implements AssessmentDAO {
    @Override
    public Assessment findAssessmentByName(String s) {
        return (Assessment) getSession().createQuery("from AssessmentImpl where name like :name")
                .setString("name", s)
                .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    public void addCriteria(Assessment assessment) {
        AssessmentCriteria assessmentCriteria = new AssessmentCriteriaImpl();
        save(assessmentCriteria);
        assessment.addCriteria(assessmentCriteria);
        save(assessment);
    }

    @Override
    public void addAssessment(AnchorELO anchorELO) {
        Assessment assessment = new AssessmentImpl();
        save(assessment);
        anchorELO.setAssessment(assessment);
        save(anchorELO);
    }

    @Override
    public void addScoreDefinition(Assessment assessment) {
        AssessmentScoreDefinition scoreDefinition = new AssessmentScoreDefinitionImpl();
        save(scoreDefinition);
        assessment.addAssessmentScoreDefinition(scoreDefinition);
        save(assessment);
    }
}
