package eu.scy.core;

import eu.scy.core.model.PedagogicalPlanGroup;
import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.SCYProject;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import java.util.List;

public interface GroupService extends BaseService{
    public SCYGroup createGroup(SCYProject project, String name, SCYGroup parent);

    public SCYGroup getGroup(String id);

    public SCYGroup getRootGroup(SCYProject project);

    public List<SCYGroup> getGroupsForProject(SCYProject project);

    public List<PedagogicalPlanGroup> getPedagogicalPlanGroups(PedagogicalPlan plan);

    public Long getPedagogicalPlanGroupsCount(PedagogicalPlan plan);
}