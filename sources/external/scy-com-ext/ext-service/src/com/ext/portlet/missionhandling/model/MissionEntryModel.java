package com.ext.portlet.missionhandling.model;

import com.liferay.portal.model.BaseModel;

import java.util.Date;


/**
 * <a href="MissionEntryModel.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the <code>MissionEntry</code>
 * table in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.missionhandling.model.MissionEntry
 * @see com.ext.portlet.missionhandling.model.impl.MissionEntryImpl
 * @see com.ext.portlet.missionhandling.model.impl.MissionEntryModelImpl
 *
 */
public interface MissionEntryModel extends BaseModel<MissionEntry> {
    public long getPrimaryKey();

    public void setPrimaryKey(long pk);

    public long getMissionEntryId();

    public void setMissionEntryId(long missionEntryId);

    public long getCompanyId();

    public void setCompanyId(long companyId);

    public long getGroupId();

    public void setGroupId(long groupId);

    public long getOrganizationId();

    public void setOrganizationId(long organizationId);

    public Date getCreateDate();

    public void setCreateDate(Date createDate);

    public Date getModifiedDate();

    public void setModifiedDate(Date modifiedDate);

    public Date getEndDate();

    public void setEndDate(Date endDate);

    public boolean getActive();

    public boolean isActive();

    public void setActive(boolean active);

    public MissionEntry toEscapedModel();
}
