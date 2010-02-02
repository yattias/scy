package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.LearningGoalImpl;
import eu.scy.core.model.pedagogicalplan.LearningGoal;
import eu.scy.core.model.pedagogicalplan.Mission;
import eu.scy.core.persistence.MissionDAO;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.jan.2010
 * Time: 10:48:35
 * To change this template use File | Settings | File Templates.
 */
public class MissionDAOHibernateTest extends AbstractDAOTest{

    private MissionDAO missionDAO;

    public MissionDAO getMissionDAO() {
        return missionDAO;
    }

    public void setMissionDAO(MissionDAO missionDAO) {
        this.missionDAO = missionDAO;
    }

    public void testSetup() {
        assertNotNull(getMissionDAO());
    }

    public void testAddLearningGoalToMission() {
        Mission mission  = createMission("test");
        LearningGoalImpl learningGoal = new LearningGoalImpl();
        mission.addLearningGoal(learningGoal);
        getMissionDAO().save(mission);
        assertNotNull(learningGoal.getId());
    }
}
