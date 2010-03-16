package com.ext.portlet.cart.model;

import com.liferay.portal.model.BaseModel;

import java.util.Date;


/**
 * <a href="CartModel.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the <code>Cart</code>
 * table in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.cart.model.Cart
 * @see com.ext.portlet.cart.model.impl.CartImpl
 * @see com.ext.portlet.cart.model.impl.CartModelImpl
 *
 */
public interface CartModel extends BaseModel<Cart> {
    public long getPrimaryKey();

    public void setPrimaryKey(long pk);

    public long getCartId();

    public void setCartId(long cartId);

    public long getGroupId();

    public void setGroupId(long groupId);

    public long getCompanyId();

    public void setCompanyId(long companyId);

    public long getUserId();

    public void setUserId(long userId);

    public String getTitle();

    public void setTitle(String title);

    public Date getCreateDate();

    public void setCreateDate(Date createDate);

    public Date getModifiedDate();

    public void setModifiedDate(Date modifiedDate);

    public String getTagNames();

    public void setTagNames(String tagNames);

    public Cart toEscapedModel();
}
