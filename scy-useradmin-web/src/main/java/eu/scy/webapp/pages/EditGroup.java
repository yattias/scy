package eu.scy.webapp.pages;

import eu.scy.core.model.Group;
import eu.scy.core.persistence.GroupDAO;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.InjectPage;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.jan.2009
 * Time: 05:31:51
 * To change this template use File | Settings | File Templates.
 */
public class EditGroup extends ScyModelPage{

    private Group group;

     @Inject
    private GroupDAO groupDAO;

    @InjectPage
    private GroupOverview groupOverview;

    public GroupDAO getGroupDAO() {
        return groupDAO;
    }

    public void setGroupDAO(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }


    public void loadModel() {
        Group group = getGroupDAO().getGroup(getModelId());
        setGroup(group);
    }

    public Object onSuccess() {
        getGroupDAO().save(group);
        groupOverview.setModelId(group.getId());
        groupOverview.loadModel();
        return groupOverview;
    }
}
