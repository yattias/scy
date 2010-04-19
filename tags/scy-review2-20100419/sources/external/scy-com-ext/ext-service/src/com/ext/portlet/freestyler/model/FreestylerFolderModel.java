package com.ext.portlet.freestyler.model;

import com.liferay.portal.model.BaseModel;

import java.util.Date;


/**
 * <a href="FreestylerFolderModel.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the <code>FreestylerFolder</code>
 * table in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.freestyler.model.FreestylerFolder
 * @see com.ext.portlet.freestyler.model.impl.FreestylerFolderImpl
 * @see com.ext.portlet.freestyler.model.impl.FreestylerFolderModelImpl
 *
 */
public interface FreestylerFolderModel extends BaseModel<FreestylerFolder> {
    public long getPrimaryKey();

    public void setPrimaryKey(long pk);

    public long getFolderId();

    public void setFolderId(long folderId);

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

    public long getParentFolderId();

    public void setParentFolderId(long parentFolderId);

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public FreestylerFolder toEscapedModel();
}
