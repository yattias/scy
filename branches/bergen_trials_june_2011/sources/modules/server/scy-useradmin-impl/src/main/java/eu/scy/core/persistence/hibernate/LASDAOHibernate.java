package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.LearningActivitySpaceToolConfigurationImpl;
import eu.scy.core.model.pedagogicalplan.*;
import eu.scy.core.persistence.LASDAO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.jan.2010
 * Time: 11:31:44
 * To change this template use File | Settings | File Templates.
 */
public class LASDAOHibernate extends ScyBaseDAOHibernate implements LASDAO {

    @Override
    public void addToolToLAS(Tool tool, LearningActivitySpace las) {
        LearningActivitySpaceToolConfigurationImpl configuration = new LearningActivitySpaceToolConfigurationImpl();
        configuration.setTool(tool);
        configuration.setLearningActivitySpace(las);
        save(configuration);
    }

    @Override
    public void addToolToActivity(Tool tool, Activity activity) {
        LearningActivitySpaceToolConfigurationImpl configuration = new LearningActivitySpaceToolConfigurationImpl();
        configuration.setTool(tool);
        configuration.setLearningActivitySpace(activity.getLearningActivitySpace());
        configuration.setActivity(activity);
        save(configuration);

    }

    @Override
    public List<LearningActivitySpaceToolConfiguration> getToolConfigurations(LearningActivitySpace learningActivitySpace) {
        return getSession().createQuery("from LearningActivitySpaceToolConfiguration where learningActivitySpace = :las")
                .setEntity("las", learningActivitySpace)
                .list();
    }

    @Override
    public List<AnchorELO> getAnchorELOsProducedByLAS(LearningActivitySpace learningActivitySpace) {
        return getSession().createQuery("select distinct(activity.anchorELO) from ActivityImpl as activity where activity.learningActivitySpace = :las")
                .setEntity("las", learningActivitySpace)
                .list();
    }

    @Override
    public List<LearningActivitySpace> getAllLearningActivitySpacesForScenario(Scenario scenario) {
        return getSession().createQuery("from LearningActivitySpaceImpl where participatesIn = :scenario")
                .setEntity("scenario", scenario)
                .list();
    }

    @Override
    public LearningActivitySpace getLearningActivitySpaceByName(String lasName, Scenario participatesIn) {
        return (LearningActivitySpace) getSession().createQuery("from LearningActivitySpaceImpl where name like :name and participatesIn = :participatesIn")
                .setString("name", lasName)
                .setEntity("participatesIn", participatesIn)
                .setMaxResults(1)
                .uniqueResult();
    }
}
