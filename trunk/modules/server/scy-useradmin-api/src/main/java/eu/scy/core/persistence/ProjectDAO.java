package eu.scy.core.persistence;

import eu.scy.core.model.SCYProject;
import eu.scy.core.model.SCYGroup;

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

    public void createProject(SCYProject project);

    public List <SCYProject> findProjectsByName(String name);

    public SCYProject getProject(String projectId);

    SCYGroup addGroupToProject(SCYProject project, SCYGroup group);


}
