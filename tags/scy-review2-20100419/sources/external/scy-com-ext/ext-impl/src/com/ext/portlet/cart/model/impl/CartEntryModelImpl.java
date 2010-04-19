package com.ext.portlet.cart.model.impl;

import com.ext.portlet.cart.model.CartEntry;
import com.ext.portlet.cart.model.CartEntrySoap;

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
 * <a href="CartEntryModelImpl.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is a model that represents the <code>CartEntry</code> table
 * in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.cart.model.CartEntry
 * @see com.ext.portlet.cart.model.CartEntryModel
 * @see com.ext.portlet.cart.model.impl.CartEntryImpl
 *
 */
public class CartEntryModelImpl extends BaseModelImpl<CartEntry> {
    public static final String TABLE_NAME = "CartEntry";
    public static final Object[][] TABLE_COLUMNS = {
            { "cartEntryId", new Integer(Types.BIGINT) },
            

            { "groupId", new Integer(Types.BIGINT) },
            

            { "companyId", new Integer(Types.BIGINT) },
            

            { "userId", new Integer(Types.BIGINT) },
            

            { "resourceId", new Integer(Types.BIGINT) },
            

            { "userName", new Integer(Types.VARCHAR) },
            

            { "createDate", new Integer(Types.TIMESTAMP) },
            

            { "modifiedDate", new Integer(Types.TIMESTAMP) },
            

            { "tagNames", new Integer(Types.VARCHAR) },
            

            { "resourceType", new Integer(Types.VARCHAR) }
        };
    public static final String TABLE_SQL_CREATE = "create table CartEntry (cartEntryId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,resourceId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,tagNames VARCHAR(75) null,resourceType VARCHAR(75) null)";
    public static final String TABLE_SQL_DROP = "drop table CartEntry";
    public static final String DATA_SOURCE = "liferayDataSource";
    public static final String SESSION_FACTORY = "liferaySessionFactory";
    public static final String TX_MANAGER = "liferayTransactionManager";
    public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
                "value.object.entity.cache.enabled.com.ext.portlet.cart.model.CartEntry"),
            true);
    public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
                "value.object.finder.cache.enabled.com.ext.portlet.cart.model.CartEntry"),
            true);
    public static final boolean FINDER_CACHE_ENABLED_CART_CARTENTRIES = com.ext.portlet.cart.model.impl.CartModelImpl.FINDER_CACHE_ENABLED_CART_CARTENTRIES;
    public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
                "lock.expiration.time.com.ext.portlet.cart.model.CartEntry"));
    private long _cartEntryId;
    private long _groupId;
    private long _companyId;
    private long _userId;
    private long _resourceId;
    private String _userName;
    private Date _createDate;
    private Date _modifiedDate;
    private String _tagNames;
    private String _resourceType;
    private transient ExpandoBridge _expandoBridge;

    public CartEntryModelImpl() {
    }

    public static CartEntry toModel(CartEntrySoap soapModel) {
        CartEntry model = new CartEntryImpl();

        model.setCartEntryId(soapModel.getCartEntryId());
        model.setGroupId(soapModel.getGroupId());
        model.setCompanyId(soapModel.getCompanyId());
        model.setUserId(soapModel.getUserId());
        model.setResourceId(soapModel.getResourceId());
        model.setUserName(soapModel.getUserName());
        model.setCreateDate(soapModel.getCreateDate());
        model.setModifiedDate(soapModel.getModifiedDate());
        model.setTagNames(soapModel.getTagNames());
        model.setResourceType(soapModel.getResourceType());

        return model;
    }

    public static List<CartEntry> toModels(CartEntrySoap[] soapModels) {
        List<CartEntry> models = new ArrayList<CartEntry>(soapModels.length);

        for (CartEntrySoap soapModel : soapModels) {
            models.add(toModel(soapModel));
        }

        return models;
    }

    public long getPrimaryKey() {
        return _cartEntryId;
    }

    public void setPrimaryKey(long pk) {
        setCartEntryId(pk);
    }

    public Serializable getPrimaryKeyObj() {
        return new Long(_cartEntryId);
    }

    public long getCartEntryId() {
        return _cartEntryId;
    }

    public void setCartEntryId(long cartEntryId) {
        _cartEntryId = cartEntryId;
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

    public long getResourceId() {
        return _resourceId;
    }

    public void setResourceId(long resourceId) {
        _resourceId = resourceId;
    }

    public String getUserName() {
        return GetterUtil.getString(_userName);
    }

    public void setUserName(String userName) {
        _userName = userName;
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

    public String getResourceType() {
        return GetterUtil.getString(_resourceType);
    }

    public void setResourceType(String resourceType) {
        _resourceType = resourceType;
    }

    public CartEntry toEscapedModel() {
        if (isEscapedModel()) {
            return (CartEntry) this;
        } else {
            CartEntry model = new CartEntryImpl();

            model.setNew(isNew());
            model.setEscapedModel(true);

            model.setCartEntryId(getCartEntryId());
            model.setGroupId(getGroupId());
            model.setCompanyId(getCompanyId());
            model.setUserId(getUserId());
            model.setResourceId(getResourceId());
            model.setUserName(HtmlUtil.escape(getUserName()));
            model.setCreateDate(getCreateDate());
            model.setModifiedDate(getModifiedDate());
            model.setTagNames(HtmlUtil.escape(getTagNames()));
            model.setResourceType(HtmlUtil.escape(getResourceType()));

            model = (CartEntry) Proxy.newProxyInstance(CartEntry.class.getClassLoader(),
                    new Class[] { CartEntry.class },
                    new ReadOnlyBeanHandler(model));

            return model;
        }
    }

    public ExpandoBridge getExpandoBridge() {
        if (_expandoBridge == null) {
            _expandoBridge = new ExpandoBridgeImpl(CartEntry.class.getName(),
                    getPrimaryKey());
        }

        return _expandoBridge;
    }

    public Object clone() {
        CartEntryImpl clone = new CartEntryImpl();

        clone.setCartEntryId(getCartEntryId());
        clone.setGroupId(getGroupId());
        clone.setCompanyId(getCompanyId());
        clone.setUserId(getUserId());
        clone.setResourceId(getResourceId());
        clone.setUserName(getUserName());
        clone.setCreateDate(getCreateDate());
        clone.setModifiedDate(getModifiedDate());
        clone.setTagNames(getTagNames());
        clone.setResourceType(getResourceType());

        return clone;
    }

    public int compareTo(CartEntry cartEntry) {
        int value = 0;

        if (getCartEntryId() < cartEntry.getCartEntryId()) {
            value = -1;
        } else if (getCartEntryId() > cartEntry.getCartEntryId()) {
            value = 1;
        } else {
            value = 0;
        }

        if (value != 0) {
            return value;
        }

        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        CartEntry cartEntry = null;

        try {
            cartEntry = (CartEntry) obj;
        } catch (ClassCastException cce) {
            return false;
        }

        long pk = cartEntry.getPrimaryKey();

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

        sb.append("{cartEntryId=");
        sb.append(getCartEntryId());
        sb.append(", groupId=");
        sb.append(getGroupId());
        sb.append(", companyId=");
        sb.append(getCompanyId());
        sb.append(", userId=");
        sb.append(getUserId());
        sb.append(", resourceId=");
        sb.append(getResourceId());
        sb.append(", userName=");
        sb.append(getUserName());
        sb.append(", createDate=");
        sb.append(getCreateDate());
        sb.append(", modifiedDate=");
        sb.append(getModifiedDate());
        sb.append(", tagNames=");
        sb.append(getTagNames());
        sb.append(", resourceType=");
        sb.append(getResourceType());
        sb.append("}");

        return sb.toString();
    }

    public String toXmlString() {
        StringBuilder sb = new StringBuilder();

        sb.append("<model><model-name>");
        sb.append("com.ext.portlet.cart.model.CartEntry");
        sb.append("</model-name>");

        sb.append(
            "<column><column-name>cartEntryId</column-name><column-value><![CDATA[");
        sb.append(getCartEntryId());
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
            "<column><column-name>resourceId</column-name><column-value><![CDATA[");
        sb.append(getResourceId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>userName</column-name><column-value><![CDATA[");
        sb.append(getUserName());
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
        sb.append(
            "<column><column-name>resourceType</column-name><column-value><![CDATA[");
        sb.append(getResourceType());
        sb.append("]]></column-value></column>");

        sb.append("</model>");

        return sb.toString();
    }
}
