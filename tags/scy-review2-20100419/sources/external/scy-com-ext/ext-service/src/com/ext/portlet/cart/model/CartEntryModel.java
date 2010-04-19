package com.ext.portlet.cart.model;

import com.liferay.portal.model.BaseModel;

import java.util.Date;


/**
 * <a href="CartEntryModel.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the <code>CartEntry</code>
 * table in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.cart.model.CartEntry
 * @see com.ext.portlet.cart.model.impl.CartEntryImpl
 * @see com.ext.portlet.cart.model.impl.CartEntryModelImpl
 *
 */
public interface CartEntryModel extends BaseModel<CartEntry> {
    public long getPrimaryKey();

    public void setPrimaryKey(long pk);

    public long getCartEntryId();

    public void setCartEntryId(long cartEntryId);

    public long getGroupId();

    public void setGroupId(long groupId);

    public long getCompanyId();

    public void setCompanyId(long companyId);

    public long getUserId();

    public void setUserId(long userId);

    public long getResourceId();

    public void setResourceId(long resourceId);

    public String getUserName();

    public void setUserName(String userName);

    public Date getCreateDate();

    public void setCreateDate(Date createDate);

    public Date getModifiedDate();

    public void setModifiedDate(Date modifiedDate);

    public String getTagNames();

    public void setTagNames(String tagNames);

    public String getResourceType();

    public void setResourceType(String resourceType);

    public CartEntry toEscapedModel();
}
