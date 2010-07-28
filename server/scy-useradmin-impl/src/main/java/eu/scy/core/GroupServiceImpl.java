package eu.scy.core;

import eu.scy.core.model.PedagogicalPlanGroup;
import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.SCYProject;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.persistence.GroupDAO;
import java.util.List;
import java.util.logging.Logger;

public class GroupServiceImpl extends BaseServiceImpl implements GroupService {

    private static Logger log = Logger.getLogger("GroupServiceImpl.class");

    private GroupDAO groupDAO;

    public GroupDAO getGroupDAO() {
        return groupDAO;
    }

    public void setGroupDAO(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    @Override
    public SCYGroup createGroup(SCYProject project, String name, SCYGroup parent) {
        return getGroupDAO().createGroup(project, name, parent);
    }

    @Override
    public SCYGroup getGroup(String id) {
        return getGroupDAO().getGroup(id);
    }

    @Override
    public SCYGroup getRootGroup(SCYProject project) {
        return getGroupDAO().getRootGroup(project);
    }

    @Override
    public List<SCYGroup> getGroupsForProject(SCYProject project) {
        return getGroupDAO().getGroupsForProject(project);
    }

    @Override
    public List<PedagogicalPlanGroup> getPedagogicalPlanGroups(PedagogicalPlan plan) {
        return getGroupDAO().getPedagogicalPlanGroups(plan);
    }

    public Long getPedagogicalPlanGroupsCount(PedagogicalPlan plan) {
        return getGroupDAO().getPedagogicalPlanGroupsCount(plan);
    }
}