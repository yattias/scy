package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.pedagogicalplan.Activity;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.persistence.AnchorELODAO;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 21:34:30
 * To change this template use File | Settings | File Templates.
 */
public class AnchorELODAOHibernate extends ScyBaseDAOHibernate implements AnchorELODAO {
    @Override
    public AnchorELO getAnchorELO(String anchorEloId) {
        return (AnchorELO) getSession().createQuery("from AnchorELOImpl where id like :id")
                .setString("id", anchorEloId)
                .uniqueResult();
    }

    @Override
    public List<AnchorELO> getAllAnchorELOsForScenario(Scenario scenario) {
        List anchorElos = new LinkedList();
        List lasses =  getSession().createQuery("from LearningActivitySpaceImpl as las where las.participatesIn = :scenario")
                        .setEntity("scenario", scenario)
                        .list();

        for (int i = 0; i < lasses.size(); i++) {
            LearningActivitySpace learningActivitySpace = (LearningActivitySpace) lasses.get(i);
            List activities = learningActivitySpace.getActivities();
            for (int j = 0; j < activities.size(); j++) {
                Activity activity = (Activity) activities.get(j);
                anchorElos.add(activity.getAnchorELO());
            }
        }

        return anchorElos;

    }
}
