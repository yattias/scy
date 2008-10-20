package eu.scy.core.persistence;

import eu.scy.core.model.Project;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.okt.2008
 * Time: 05:48:18
 * To change this template use File | Settings | File Templates.
 */
public interface ProjectDAO extends SCYBaseDAO{


    List getAllProjects();

    void createProject(Project project);

    List <Project> findProjectsByName(String name);

    Project getProject(String projectId);
}
