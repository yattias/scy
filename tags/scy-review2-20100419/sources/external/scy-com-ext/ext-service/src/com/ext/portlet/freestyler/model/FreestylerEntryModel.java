package com.ext.portlet.freestyler.model;

import com.liferay.portal.model.BaseModel;

import java.util.Date;


/**
 * <a href="FreestylerEntryModel.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the <code>FreestylerEntry</code>
 * table in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.freestyler.model.FreestylerEntry
 * @see com.ext.portlet.freestyler.model.impl.FreestylerEntryImpl
 * @see com.ext.portlet.freestyler.model.impl.FreestylerEntryModelImpl
 *
 */
public interface FreestylerEntryModel extends BaseModel<FreestylerEntry> {
    public long getPrimaryKey();

    public void setPrimaryKey(long pk);

    public long getFreestylerId();

    public void setFreestylerId(long freestylerId);

    public long getGroupId();

    public void setGroupId(long groupId);

    public long getCompanyId();

    public void setCompanyId(long companyId);

    public long getUserId();

    public void setUserId(long userId);

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public long getXmlFileId();

    public void setXmlFileId(long xmlFileId);

    public Date getCreateDate();

    public void setCreateDate(Date createDate);

    public Date getModifiedDate();

    public void setModifiedDate(Date modifiedDate);

    public FreestylerEntry toEscapedModel();
}
