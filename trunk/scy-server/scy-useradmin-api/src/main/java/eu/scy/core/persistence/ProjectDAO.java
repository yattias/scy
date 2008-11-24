package eu.scy.core.persistence;

import eu.scy.core.model.Project;
import eu.scy.core.model.Group;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.okt.2008
 * Time: 05:48:18
 * To change this template use File | Settings | File Templates.
 */
public interface ProjectDAO extends SCYBaseDAO{


    public List getAllProjects();

    public void createProject(Project project);

    public List <Project> findProjectsByName(String name);

    public Project getProject(String projectId);

    Project addGroupToProject(Project project, Group group);


}
