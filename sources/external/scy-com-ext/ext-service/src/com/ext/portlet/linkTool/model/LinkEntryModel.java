package com.ext.portlet.linkTool.model;

import com.liferay.portal.model.BaseModel;

import java.util.Date;


/**
 * <a href="LinkEntryModel.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the <code>LinkEntry</code>
 * table in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.linkTool.model.LinkEntry
 * @see com.ext.portlet.linkTool.model.impl.LinkEntryImpl
 * @see com.ext.portlet.linkTool.model.impl.LinkEntryModelImpl
 *
 */
public interface LinkEntryModel extends BaseModel<LinkEntry> {
    public long getPrimaryKey();

    public void setPrimaryKey(long pk);

    public long getLinkId();

    public void setLinkId(long linkId);

    public long getGroupId();

    public void setGroupId(long groupId);

    public long getCompanyId();

    public void setCompanyId(long companyId);

    public long getUserId();

    public void setUserId(long userId);

    public Date getCreateDate();

    public void setCreateDate(Date createDate);

    public Date getModifiedDate();

    public void setModifiedDate(Date modifiedDate);

    public String getResourceId();

    public void setResourceId(String resourceId);

    public String getLinkedResourceId();

    public void setLinkedResourceId(String linkedResourceId);

    public String getLinkedResourceClassNameId();

    public void setLinkedResourceClassNameId(String linkedResourceClassNameId);

    public LinkEntry toEscapedModel();
}
