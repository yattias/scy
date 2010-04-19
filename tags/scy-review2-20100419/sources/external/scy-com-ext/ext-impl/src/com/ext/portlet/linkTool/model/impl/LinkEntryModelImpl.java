package com.ext.portlet.linkTool.model.impl;

import com.ext.portlet.linkTool.model.LinkEntry;
import com.ext.portlet.linkTool.model.LinkEntrySoap;

import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.bean.ReadOnlyBeanHandler;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.util.PortalUtil;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.model.impl.ExpandoBridgeImpl;

import java.io.Serializable;

import java.lang.reflect.Proxy;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <a href="LinkEntryModelImpl.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the LinkEntry table in the
 * database.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       LinkEntryImpl
 * @see       com.ext.portlet.linkTool.model.LinkEntry
 * @see       com.ext.portlet.linkTool.model.LinkEntryModel
 * @generated
 */
public class LinkEntryModelImpl extends BaseModelImpl<LinkEntry> {
    public static final String TABLE_NAME = "LinkEntry";
    public static final Object[][] TABLE_COLUMNS = {
            { "linkId", new Integer(Types.BIGINT) },
            

            { "groupId", new Integer(Types.BIGINT) },
            

            { "companyId", new Integer(Types.BIGINT) },
            

            { "userId", new Integer(Types.BIGINT) },
            

            { "createDate", new Integer(Types.TIMESTAMP) },
            

            { "modifiedDate", new Integer(Types.TIMESTAMP) },
            

            { "resourceId", new Integer(Types.VARCHAR) },
            

            { "linkedResourceId", new Integer(Types.VARCHAR) },
            

            { "linkedResourceClassNameId", new Integer(Types.VARCHAR) }
        };
    public static final String TABLE_SQL_CREATE = "create table LinkEntry (linkId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,createDate DATE null,modifiedDate DATE null,resourceId VARCHAR(75) null,linkedResourceId VARCHAR(75) null,linkedResourceClassNameId VARCHAR(75) null)";
    public static final String TABLE_SQL_DROP = "drop table LinkEntry";
    public static final String DATA_SOURCE = "liferayDataSource";
    public static final String SESSION_FACTORY = "liferaySessionFactory";
    public static final String TX_MANAGER = "liferayTransactionManager";
    public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
                "value.object.entity.cache.enabled.com.ext.portlet.linkTool.model.LinkEntry"),
            true);
    public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
                "value.object.finder.cache.enabled.com.ext.portlet.linkTool.model.LinkEntry"),
            true);
    public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
                "lock.expiration.time.com.ext.portlet.linkTool.model.LinkEntry"));
    private long _linkId;
    private long _groupId;
    private long _companyId;
    private long _userId;
    private String _userUuid;
    private Date _createDate;
    private Date _modifiedDate;
    private String _resourceId;
    private String _linkedResourceId;
    private String _linkedResourceClassNameId;
    private transient ExpandoBridge _expandoBridge;

    public LinkEntryModelImpl() {
    }

    public static LinkEntry toModel(LinkEntrySoap soapModel) {
        LinkEntry model = new LinkEntryImpl();

        model.setLinkId(soapModel.getLinkId());
        model.setGroupId(soapModel.getGroupId());
        model.setCompanyId(soapModel.getCompanyId());
        model.setUserId(soapModel.getUserId());
        model.setCreateDate(soapModel.getCreateDate());
        model.setModifiedDate(soapModel.getModifiedDate());
        model.setResourceId(soapModel.getResourceId());
        model.setLinkedResourceId(soapModel.getLinkedResourceId());
        model.setLinkedResourceClassNameId(soapModel.getLinkedResourceClassNameId());

        return model;
    }

    public static List<LinkEntry> toModels(LinkEntrySoap[] soapModels) {
        List<LinkEntry> models = new ArrayList<LinkEntry>(soapModels.length);

        for (LinkEntrySoap soapModel : soapModels) {
            models.add(toModel(soapModel));
        }

        return models;
    }

    public long getPrimaryKey() {
        return _linkId;
    }

    public void setPrimaryKey(long pk) {
        setLinkId(pk);
    }

    public Serializable getPrimaryKeyObj() {
        return new Long(_linkId);
    }

    public long getLinkId() {
        return _linkId;
    }

    public void setLinkId(long linkId) {
        _linkId = linkId;
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

    public String getUserUuid() throws SystemException {
        return PortalUtil.getUserValue(getUserId(), "uuid", _userUuid);
    }

    public void setUserUuid(String userUuid) {
        _userUuid = userUuid;
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

    public String getResourceId() {
        return GetterUtil.getString(_resourceId);
    }

    public void setResourceId(String resourceId) {
        _resourceId = resourceId;
    }

    public String getLinkedResourceId() {
        return GetterUtil.getString(_linkedResourceId);
    }

    public void setLinkedResourceId(String linkedResourceId) {
        _linkedResourceId = linkedResourceId;
    }

    public String getLinkedResourceClassNameId() {
        return GetterUtil.getString(_linkedResourceClassNameId);
    }

    public void setLinkedResourceClassNameId(String linkedResourceClassNameId) {
        _linkedResourceClassNameId = linkedResourceClassNameId;
    }

    public LinkEntry toEscapedModel() {
        if (isEscapedModel()) {
            return (LinkEntry) this;
        } else {
            LinkEntry model = new LinkEntryImpl();

            model.setNew(isNew());
            model.setEscapedModel(true);

            model.setLinkId(getLinkId());
            model.setGroupId(getGroupId());
            model.setCompanyId(getCompanyId());
            model.setUserId(getUserId());
            model.setCreateDate(getCreateDate());
            model.setModifiedDate(getModifiedDate());
            model.setResourceId(HtmlUtil.escape(getResourceId()));
            model.setLinkedResourceId(HtmlUtil.escape(getLinkedResourceId()));
            model.setLinkedResourceClassNameId(HtmlUtil.escape(
                    getLinkedResourceClassNameId()));

            model = (LinkEntry) Proxy.newProxyInstance(LinkEntry.class.getClassLoader(),
                    new Class[] { LinkEntry.class },
                    new ReadOnlyBeanHandler(model));

            return model;
        }
    }

    public ExpandoBridge getExpandoBridge() {
        if (_expandoBridge == null) {
            _expandoBridge = new ExpandoBridgeImpl(LinkEntry.class.getName(),
                    getPrimaryKey());
        }

        return _expandoBridge;
    }

    public Object clone() {
        LinkEntryImpl clone = new LinkEntryImpl();

        clone.setLinkId(getLinkId());
        clone.setGroupId(getGroupId());
        clone.setCompanyId(getCompanyId());
        clone.setUserId(getUserId());
        clone.setCreateDate(getCreateDate());
        clone.setModifiedDate(getModifiedDate());
        clone.setResourceId(getResourceId());
        clone.setLinkedResourceId(getLinkedResourceId());
        clone.setLinkedResourceClassNameId(getLinkedResourceClassNameId());

        return clone;
    }

    public int compareTo(LinkEntry linkEntry) {
        int value = 0;

        value = getResourceId().compareTo(linkEntry.getResourceId());

        if (value != 0) {
            return value;
        }

        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        LinkEntry linkEntry = null;

        try {
            linkEntry = (LinkEntry) obj;
        } catch (ClassCastException cce) {
            return false;
        }

        long pk = linkEntry.getPrimaryKey();

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

        sb.append("{linkId=");
        sb.append(getLinkId());
        sb.append(", groupId=");
        sb.append(getGroupId());
        sb.append(", companyId=");
        sb.append(getCompanyId());
        sb.append(", userId=");
        sb.append(getUserId());
        sb.append(", createDate=");
        sb.append(getCreateDate());
        sb.append(", modifiedDate=");
        sb.append(getModifiedDate());
        sb.append(", resourceId=");
        sb.append(getResourceId());
        sb.append(", linkedResourceId=");
        sb.append(getLinkedResourceId());
        sb.append(", linkedResourceClassNameId=");
        sb.append(getLinkedResourceClassNameId());
        sb.append("}");

        return sb.toString();
    }

    public String toXmlString() {
        StringBuilder sb = new StringBuilder();

        sb.append("<model><model-name>");
        sb.append("com.ext.portlet.linkTool.model.LinkEntry");
        sb.append("</model-name>");

        sb.append(
            "<column><column-name>linkId</column-name><column-value><![CDATA[");
        sb.append(getLinkId());
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
            "<column><column-name>createDate</column-name><column-value><![CDATA[");
        sb.append(getCreateDate());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>modifiedDate</column-name><column-value><![CDATA[");
        sb.append(getModifiedDate());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>resourceId</column-name><column-value><![CDATA[");
        sb.append(getResourceId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>linkedResourceId</column-name><column-value><![CDATA[");
        sb.append(getLinkedResourceId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>linkedResourceClassNameId</column-name><column-value><![CDATA[");
        sb.append(getLinkedResourceClassNameId());
        sb.append("]]></column-value></column>");

        sb.append("</model>");

        return sb.toString();
    }
}
