package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.BaseObject;
import eu.scy.core.model.pedagogicalplan.Mission;
import eu.scy.core.persistence.MissionDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.jan.2010
 * Time: 12:10:04
 * To change this template use File | Settings | File Templates.
 */
public class MissionServiceImpl extends BaseServiceImpl implements MissionService {

	private MissionDAO missionDAO;

	public MissionDAO getMissionDAO() {
		return (MissionDAO) getScyBaseDAO();
	}

	public void setMissionDAO(MissionDAO missionDAO) {
		this.missionDAO = missionDAO;
	}

	@Override
	@Transactional
	public void save(BaseObject baseObject) {
		getMissionDAO().save(baseObject);
	}

    @Override
    public Mission getMission(String parameter) {
        return getMissionDAO().getMission(parameter);
    }
}
