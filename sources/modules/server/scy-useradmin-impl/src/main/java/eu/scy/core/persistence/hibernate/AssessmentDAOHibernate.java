package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.AssessmentCriteria;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.pedagogicalplan.AssessmentCriteriaExperienceImpl;
import eu.scy.core.model.impl.pedagogicalplan.AssessmentCriteriaImpl;
import eu.scy.core.model.impl.pedagogicalplan.AssessmentImpl;
import eu.scy.core.model.impl.pedagogicalplan.AssessmentScoreDefinitionImpl;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.Assessment;
import eu.scy.core.model.pedagogicalplan.AssessmentCriteriaExperience;
import eu.scy.core.model.pedagogicalplan.AssessmentScoreDefinition;
import eu.scy.core.persistence.AssessmentDAO;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.jan.2010
 * Time: 05:45:50
 * To change this template use File | Settings | File Templates.
 */
public class AssessmentDAOHibernate extends ScyBaseDAOHibernate implements AssessmentDAO {

    private static Logger log = Logger.getLogger("AssessmentDAOHibernate.class");

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

    @Override
    public AssessmentCriteria getAssessmentCriteria(String parameter) {
        return (AssessmentCriteria) getSession().createQuery("From AssessmentCriteriaImpl where id like :id")
                .setString("id", parameter)
                .uniqueResult();
    }

    @Override
    public void createOrUpdateAssessmentCriteriaExperience(User user, AssessmentCriteria criteria, String criteriaText, int score, String comment) {
        logger.info("CHECKING WHETHER EXPERIENCE ALREADY HAS BEEN STORED FOR " + user.getUserDetails().getUsername() + " " + criteria.getId());
        AssessmentCriteriaExperience assessmentCriteriaExperience = getAssessmentCriteriaExperience(user, criteria);
        if(assessmentCriteriaExperience == null) {
            logger.info("DID NOT FIND ANY EXPERIENCE FOR THIS COMBINATION - CREATING NEW:...");
            assessmentCriteriaExperience =  new AssessmentCriteriaExperienceImpl();
            assessmentCriteriaExperience.setUser(user);
            assessmentCriteriaExperience.setAssessmentCriteria(criteria);
            save(assessmentCriteriaExperience);

        } else {
            logger.info("EXPERIENCE ALREADY EXISTED - UPDATING");
        }

        assessmentCriteriaExperience.setScore(score);
        if(criteriaText != "") assessmentCriteriaExperience.setCriteriaText(criteriaText);
        if(comment != null) assessmentCriteriaExperience.setComment(comment);

        getHibernateTemplate().saveOrUpdate(assessmentCriteriaExperience);
    }

    @Override
    public AssessmentCriteriaExperience getAssessmentCriteriaExperience(User user, AssessmentCriteria criteria) {
        return (AssessmentCriteriaExperience) getSession().createQuery("from AssessmentCriteriaExperienceImpl where user = :user and assessmentCriteria = :criteria")
                .setEntity("user", user)
                .setEntity("criteria", criteria)
                .uniqueResult();

    }
}
