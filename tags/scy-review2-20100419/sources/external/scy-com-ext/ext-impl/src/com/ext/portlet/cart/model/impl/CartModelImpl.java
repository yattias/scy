package com.ext.portlet.cart.model.impl;

import com.ext.portlet.cart.model.Cart;
import com.ext.portlet.cart.model.CartSoap;

import com.liferay.portal.kernel.bean.ReadOnlyBeanHandler;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.model.impl.BaseModelImpl;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.model.impl.ExpandoBridgeImpl;

import java.io.Serializable;

import java.lang.reflect.Proxy;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <a href="CartModelImpl.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is a model that represents the <code>Cart</code> table
 * in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.cart.model.Cart
 * @see com.ext.portlet.cart.model.CartModel
 * @see com.ext.portlet.cart.model.impl.CartImpl
 *
 */
public class CartModelImpl extends BaseModelImpl<Cart> {
    public static final String TABLE_NAME = "Cart";
    public static final Object[][] TABLE_COLUMNS = {
            { "cartId", new Integer(Types.BIGINT) },
            

            { "groupId", new Integer(Types.BIGINT) },
            

            { "companyId", new Integer(Types.BIGINT) },
            

            { "userId", new Integer(Types.BIGINT) },
            

            { "title", new Integer(Types.VARCHAR) },
            

            { "createDate", new Integer(Types.TIMESTAMP) },
            

            { "modifiedDate", new Integer(Types.TIMESTAMP) },
            

            { "tagNames", new Integer(Types.VARCHAR) }
        };
    public static final String TABLE_SQL_CREATE = "create table Cart (cartId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,title VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,tagNames VARCHAR(75) null)";
    public static final String TABLE_SQL_DROP = "drop table Cart";
    public static final String DATA_SOURCE = "liferayDataSource";
    public static final String SESSION_FACTORY = "liferaySessionFactory";
    public static final String TX_MANAGER = "liferayTransactionManager";
    public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
                "value.object.entity.cache.enabled.com.ext.portlet.cart.model.Cart"),
            true);
    public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
                "value.object.finder.cache.enabled.com.ext.portlet.cart.model.Cart"),
            true);
    public static final boolean FINDER_CACHE_ENABLED_CART_CARTENTRIES = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
                "value.object.finder.cache.enabled.Cart_CartEntries"), true);
    public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
                "lock.expiration.time.com.ext.portlet.cart.model.Cart"));
    private long _cartId;
    private long _groupId;
    private long _companyId;
    private long _userId;
    private String _title;
    private Date _createDate;
    private Date _modifiedDate;
    private String _tagNames;
    private transient ExpandoBridge _expandoBridge;

    public CartModelImpl() {
    }

    public static Cart toModel(CartSoap soapModel) {
        Cart model = new CartImpl();

        model.setCartId(soapModel.getCartId());
        model.setGroupId(soapModel.getGroupId());
        model.setCompanyId(soapModel.getCompanyId());
        model.setUserId(soapModel.getUserId());
        model.setTitle(soapModel.getTitle());
        model.setCreateDate(soapModel.getCreateDate());
        model.setModifiedDate(soapModel.getModifiedDate());
        model.setTagNames(soapModel.getTagNames());

        return model;
    }

    public static List<Cart> toModels(CartSoap[] soapModels) {
        List<Cart> models = new ArrayList<Cart>(soapModels.length);

        for (CartSoap soapModel : soapModels) {
            models.add(toModel(soapModel));
        }

        return models;
    }

    public long getPrimaryKey() {
        return _cartId;
    }

    public void setPrimaryKey(long pk) {
        setCartId(pk);
    }

    public Serializable getPrimaryKeyObj() {
        return new Long(_cartId);
    }

    public long getCartId() {
        return _cartId;
    }

    public void setCartId(long cartId) {
        _cartId = cartId;
    }

    public long getGroupId() {
        return _groupId;
    }

    public void setGroupId(long groupId) {
        _groupId = groupId;
    }

    public long getCompanyId() {
        return _companyId;
    }

    public void setCompanyId(long companyId) {
        _companyId = companyId;
    }

    public long getUserId() {
        return _userId;
    }

    public void setUserId(long userId) {
        _userId = userId;
    }

    public String getTitle() {
        return GetterUtil.getString(_title);
    }

    public void setTitle(String title) {
        _title = title;
    }

    public Date getCreateDate() {
        return _createDate;
    }

    public void setCreateDate(Date createDate) {
        _createDate = createDate;
    }

    public Date getModifiedDate() {
        return _modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        _modifiedDate = modifiedDate;
    }

    public String getTagNames() {
        return GetterUtil.getString(_tagNames);
    }

    public void setTagNames(String tagNames) {
        _tagNames = tagNames;
    }

    public Cart toEscapedModel() {
        if (isEscapedModel()) {
            return (Cart) this;
        } else {
            Cart model = new CartImpl();

            model.setNew(isNew());
            model.setEscapedModel(true);

            model.setCartId(getCartId());
            model.setGroupId(getGroupId());
            model.setCompanyId(getCompanyId());
            model.setUserId(getUserId());
            model.setTitle(HtmlUtil.escape(getTitle()));
            model.setCreateDate(getCreateDate());
            model.setModifiedDate(getModifiedDate());
            model.setTagNames(HtmlUtil.escape(getTagNames()));

            model = (Cart) Proxy.newProxyInstance(Cart.class.getClassLoader(),
                    new Class[] { Cart.class }, new ReadOnlyBeanHandler(model));

            return model;
        }
    }

    public ExpandoBridge getExpandoBridge() {
        if (_expandoBridge == null) {
            _expandoBridge = new ExpandoBridgeImpl(Cart.class.getName(),
                    getPrimaryKey());
        }

        return _expandoBridge;
    }

    public Object clone() {
        CartImpl clone = new CartImpl();

        clone.setCartId(getCartId());
        clone.setGroupId(getGroupId());
        clone.setCompanyId(getCompanyId());
        clone.setUserId(getUserId());
        clone.setTitle(getTitle());
        clone.setCreateDate(getCreateDate());
        clone.setModifiedDate(getModifiedDate());
        clone.setTagNames(getTagNames());

        return clone;
    }

    public int compareTo(Cart cart) {
        long pk = cart.getPrimaryKey();

        if (getPrimaryKey() < pk) {
            return -1;
        } else if (getPrimaryKey() > pk) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        Cart cart = null;

        try {
            cart = (Cart) obj;
        } catch (ClassCastException cce) {
            return false;
        }

        long pk = cart.getPrimaryKey();

        if (getPrimaryKey() == pk) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (int) getPrimaryKey();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{cartId=");
        sb.append(getCartId());
        sb.append(", groupId=");
        sb.append(getGroupId());
        sb.append(", companyId=");
        sb.append(getCompanyId());
        sb.append(", userId=");
        sb.append(getUserId());
        sb.append(", title=");
        sb.append(getTitle());
        sb.append(", createDate=");
        sb.append(getCreateDate());
        sb.append(", modifiedDate=");
        sb.append(getModifiedDate());
        sb.append(", tagNames=");
        sb.append(getTagNames());
        sb.append("}");

        return sb.toString();
    }

    public String toXmlString() {
        StringBuilder sb = new StringBuilder();

        sb.append("<model><model-name>");
        sb.append("com.ext.portlet.cart.model.Cart");
        sb.append("</model-name>");

        sb.append(
            "<column><column-name>cartId</column-name><column-value><![CDATA[");
        sb.append(getCartId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>groupId</column-name><column-value><![CDATA[");
        sb.append(getGroupId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>companyId</column-name><column-value><![CDATA[");
        sb.append(getCompanyId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>userId</column-name><column-value><![CDATA[");
        sb.append(getUserId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>title</column-name><column-value><![CDATA[");
        sb.append(getTitle());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>createDate</column-name><column-value><![CDATA[");
        sb.append(getCreateDate());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>modifiedDate</column-name><column-value><![CDATA[");
        sb.append(getModifiedDate());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>tagNames</column-name><column-value><![CDATA[");
        sb.append(getTagNames());
        sb.append("]]></column-value></column>");

        sb.append("</model>");

        return sb.toString();
    }
}
