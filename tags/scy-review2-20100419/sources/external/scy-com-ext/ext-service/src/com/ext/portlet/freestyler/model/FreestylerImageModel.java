package com.ext.portlet.freestyler.model;

import com.liferay.portal.model.BaseModel;

import java.util.Date;


/**
 * <a href="FreestylerImageModel.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the <code>FreestylerImage</code>
 * table in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.freestyler.model.FreestylerImage
 * @see com.ext.portlet.freestyler.model.impl.FreestylerImageImpl
 * @see com.ext.portlet.freestyler.model.impl.FreestylerImageModelImpl
 *
 */
public interface FreestylerImageModel extends BaseModel<FreestylerImage> {
    public long getPrimaryKey();

    public void setPrimaryKey(long pk);

    public long getImageId();

    public void setImageId(long imageId);

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

    public long getFreestylerId();

    public void setFreestylerId(long freestylerId);

    public long getFolderId();

    public void setFolderId(long folderId);

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public long getSmallImageId();

    public void setSmallImageId(long smallImageId);

    public long getLargeImageId();

    public void setLargeImageId(long largeImageId);

    public long getCustom1ImageId();

    public void setCustom1ImageId(long custom1ImageId);

    public long getCustom2ImageId();

    public void setCustom2ImageId(long custom2ImageId);

    public FreestylerImage toEscapedModel();
}
