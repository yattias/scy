package eu.scy.core.persistence;

import eu.scy.core.model.Group;
import eu.scy.core.model.Project;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.okt.2008
 * Time: 06:31:26
 * To change this template use File | Settings | File Templates.
 */
public interface GroupDAO extends SCYBaseDAO{
    
    public Group createGroup(Project project, String name, Group parent);

    public Group getGroup(String id);

    public Group getRootGroup(Project project);
}
