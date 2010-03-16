package com.ext.portlet.freestyler.model.impl;

import com.ext.portlet.freestyler.model.FreestylerEntry;
import com.ext.portlet.freestyler.model.FreestylerEntrySoap;

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
 * <a href="FreestylerEntryModelImpl.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is a model that represents the <code>FreestylerEntry</code> table
 * in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.freestyler.model.FreestylerEntry
 * @see com.ext.portlet.freestyler.model.FreestylerEntryModel
 * @see com.ext.portlet.freestyler.model.impl.FreestylerEntryImpl
 *
 */
public class FreestylerEntryModelImpl extends BaseModelImpl<FreestylerEntry> {
    public static final String TABLE_NAME = "FreestylerEntry";
    public static final Object[][] TABLE_COLUMNS = {
            { "freestylerId", new Integer(Types.BIGINT) },
            

            { "groupId", new Integer(Types.BIGINT) },
            

            { "companyId", new Integer(Types.BIGINT) },
            

            { "userId", new Integer(Types.BIGINT) },
            

            { "name", new Integer(Types.VARCHAR) },
            

            { "description", new Integer(Types.VARCHAR) },
            

            { "xmlFileId", new Integer(Types.BIGINT) },
            

            { "createDate", new Integer(Types.TIMESTAMP) },
            

            { "modifiedDate", new Integer(Types.TIMESTAMP) }
        };
    public static final String TABLE_SQL_CREATE = "create table FreestylerEntry (freestylerId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,name VARCHAR(75) null,description VARCHAR(75) null,xmlFileId LONG,createDate DATE null,modifiedDate DATE null)";
    public static final String TABLE_SQL_DROP = "drop table FreestylerEntry";
    public static final String DATA_SOURCE = "liferayDataSource";
    public static final String SESSION_FACTORY = "liferaySessionFactory";
    public static final String TX_MANAGER = "liferayTransactionManager";
    public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
                "value.object.entity.cache.enabled.com.ext.portlet.freestyler.model.FreestylerEntry"),
            true);
    public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
                "value.object.finder.cache.enabled.com.ext.portlet.freestyler.model.FreestylerEntry"),
            true);
    public static final boolean FINDER_CACHE_ENABLED_FREESTYLERENTRY_FREESTYLERIMAGES =
        GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
                "value.object.finder.cache.enabled.FreestylerEntry_FreestylerImages"),
            true);
    public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
                "lock.expiration.time.com.ext.portlet.freestyler.model.FreestylerEntry"));
    private long _freestylerId;
    private long _originalFreestylerId;
    private boolean _setOriginalFreestylerId;
    private long _groupId;
    private long _companyId;
    private long _userId;
    private String _name;
    private String _description;
    private long _xmlFileId;
    private Date _createDate;
    private Date _modifiedDate;
    private transient ExpandoBridge _expandoBridge;

    public FreestylerEntryModelImpl() {
    }

    public static FreestylerEntry toModel(FreestylerEntrySoap soapModel) {
        FreestylerEntry model = new FreestylerEntryImpl();

        model.setFreestylerId(soapModel.getFreestylerId());
        model.setGroupId(soapModel.getGroupId());
        model.setCompanyId(soapModel.getCompanyId());
        model.setUserId(soapModel.getUserId());
        model.setName(soapModel.getName());
        model.setDescription(soapModel.getDescription());
        model.setXmlFileId(soapModel.getXmlFileId());
        model.setCreateDate(soapModel.getCreateDate());
        model.setModifiedDate(soapModel.getModifiedDate());

        return model;
    }

    public static List<FreestylerEntry> toModels(
        FreestylerEntrySoap[] soapModels) {
        List<FreestylerEntry> models = new ArrayList<FreestylerEntry>(soapModels.length);

        for (FreestylerEntrySoap soapModel : soapModels) {
            models.add(toModel(soapModel));
        }

        return models;
    }

    public long getPrimaryKey() {
        return _freestylerId;
    }

    public void setPrimaryKey(long pk) {
        setFreestylerId(pk);
    }

    public Serializable getPrimaryKeyObj() {
        return new Long(_freestylerId);
    }

    public long getFreestylerId() {
        return _freestylerId;
    }

    public void setFreestylerId(long freestylerId) {
        _freestylerId = freestylerId;

        if (!_setOriginalFreestylerId) {
            _setOriginalFreestylerId = true;

            _originalFreestylerId = freestylerId;
        }
    }

    public long getOriginalFreestylerId() {
        return _originalFreestylerId;
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

    public String getName() {
        return GetterUtil.getString(_name);
    }

    public void setName(String name) {
        _name = name;
    }

    public String getDescription() {
        return GetterUtil.getString(_description);
    }

    public void setDescription(String description) {
        _description = description;
    }

    public long getXmlFileId() {
        return _xmlFileId;
    }

    public void setXmlFileId(long xmlFileId) {
        _xmlFileId = xmlFileId;
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

    public FreestylerEntry toEscapedModel() {
        if (isEscapedModel()) {
            return (FreestylerEntry) this;
        } else {
            FreestylerEntry model = new FreestylerEntryImpl();

            model.setNew(isNew());
            model.setEscapedModel(true);

            model.setFreestylerId(getFreestylerId());
            model.setGroupId(getGroupId());
            model.setCompanyId(getCompanyId());
            model.setUserId(getUserId());
            model.setName(HtmlUtil.escape(getName()));
            model.setDescription(HtmlUtil.escape(getDescription()));
            model.setXmlFileId(getXmlFileId());
            model.setCreateDate(getCreateDate());
            model.setModifiedDate(getModifiedDate());

            model = (FreestylerEntry) Proxy.newProxyInstance(FreestylerEntry.class.getClassLoader(),
                    new Class[] { FreestylerEntry.class },
                    new ReadOnlyBeanHandler(model));

            return model;
        }
    }

    public ExpandoBridge getExpandoBridge() {
        if (_expandoBridge == null) {
            _expandoBridge = new ExpandoBridgeImpl(FreestylerEntry.class.getName(),
                    getPrimaryKey());
        }

        return _expandoBridge;
    }

    public Object clone() {
        FreestylerEntryImpl clone = new FreestylerEntryImpl();

        clone.setFreestylerId(getFreestylerId());
        clone.setGroupId(getGroupId());
        clone.setCompanyId(getCompanyId());
        clone.setUserId(getUserId());
        clone.setName(getName());
        clone.setDescription(getDescription());
        clone.setXmlFileId(getXmlFileId());
        clone.setCreateDate(getCreateDate());
        clone.setModifiedDate(getModifiedDate());

        return clone;
    }

    public int compareTo(FreestylerEntry freestylerEntry) {
        long pk = freestylerEntry.getPrimaryKey();

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

        FreestylerEntry freestylerEntry = null;

        try {
            freestylerEntry = (FreestylerEntry) obj;
        } catch (ClassCastException cce) {
            return false;
        }

        long pk = freestylerEntry.getPrimaryKey();

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

        sb.append("{freestylerId=");
        sb.append(getFreestylerId());
        sb.append(", groupId=");
        sb.append(getGroupId());
        sb.append(", companyId=");
        sb.append(getCompanyId());
        sb.append(", userId=");
        sb.append(getUserId());
        sb.append(", name=");
        sb.append(getName());
        sb.append(", description=");
        sb.append(getDescription());
        sb.append(", xmlFileId=");
        sb.append(getXmlFileId());
        sb.append(", createDate=");
        sb.append(getCreateDate());
        sb.append(", modifiedDate=");
        sb.append(getModifiedDate());
        sb.append("}");

        return sb.toString();
    }

    public String toXmlString() {
        StringBuilder sb = new StringBuilder();

        sb.append("<model><model-name>");
        sb.append("com.ext.portlet.freestyler.model.FreestylerEntry");
        sb.append("</model-name>");

        sb.append(
            "<column><column-name>freestylerId</column-name><column-value><![CDATA[");
        sb.append(getFreestylerId());
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
            "<column><column-name>name</column-name><column-value><![CDATA[");
        sb.append(getName());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>description</column-name><column-value><![CDATA[");
        sb.append(getDescription());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>xmlFileId</column-name><column-value><![CDATA[");
        sb.append(getXmlFileId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>createDate</column-name><column-value><![CDATA[");
        sb.append(getCreateDate());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>modifiedDate</column-name><column-value><![CDATA[");
        sb.append(getModifiedDate());
        sb.append("]]></column-value></column>");

        sb.append("</model>");

        return sb.toString();
    }
}
