package eu.scy.core.persistence;

import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.SCYProject;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.okt.2008
 * Time: 06:31:26
 * To change this template use File | Settings | File Templates.
 */
public interface GroupDAO extends SCYBaseDAO{
    
    public SCYGroup createGroup(SCYProject project, String name, SCYGroup parent);

    public SCYGroup getGroup(String id);

    public SCYGroup getRootGroup(SCYProject project);

    public List <SCYGroup> getGroupsForProject(SCYProject project);

    //public List <User> getUsers(SCYGroup group);
}
