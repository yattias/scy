package eu.scy.webapp.pages;

import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.impl.ScyBaseObject;
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

    private SCYGroup group;

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

    public SCYGroup getGroup() {
        return group;
    }

    public void setGroup(SCYGroup group) {
        this.group = group;
    }

    public Object onSuccess() {
        getGroupDAO().save(group);
        groupOverview.setModel((ScyBaseObject) group);
        return groupOverview;
    }
}
