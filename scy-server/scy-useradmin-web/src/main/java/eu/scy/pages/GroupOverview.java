package eu.scy.pages;

import eu.scy.core.persistence.GroupDAO;
import eu.scy.core.model.Group;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry.commons.components.SlidingPanel;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.okt.2008
 * Time: 06:29:24
 * To change this template use File | Settings | File Templates.
 */
public class GroupOverview extends ScyModelPage {

    @Inject
    private GroupDAO groupDAO;

    public GroupDAO getGroupDAO() {
        return groupDAO;
    }

    public void setGroupDAO(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    public void loadModel() {
        groupDAO.getGroup(getModelId());
    }

    public Group getRootGroup() {
        return getGroupDAO().getRootGroup(getCurrentProject());
    }

    public String getSomeTitle() {
        return getRootGroup().getClass().getName() + " :.... " + "HENRIK" + getRootGroup().getName();
    }

}
