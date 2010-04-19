package com.ext.portlet.freestyler.model.impl;

import com.ext.portlet.freestyler.model.FreestylerFolder;
import com.ext.portlet.freestyler.model.FreestylerFolderSoap;

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
 * <a href="FreestylerFolderModelImpl.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is a model that represents the <code>FreestylerFolder</code> table
 * in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.freestyler.model.FreestylerFolder
 * @see com.ext.portlet.freestyler.model.FreestylerFolderModel
 * @see com.ext.portlet.freestyler.model.impl.FreestylerFolderImpl
 *
 */
public class FreestylerFolderModelImpl extends BaseModelImpl<FreestylerFolder> {
    public static final String TABLE_NAME = "FreestylerFolder";
    public static final Object[][] TABLE_COLUMNS = {
            { "folderId", new Integer(Types.BIGINT) },
            

            { "groupId", new Integer(Types.BIGINT) },
            

            { "companyId", new Integer(Types.BIGINT) },
            

            { "userId", new Integer(Types.BIGINT) },
            

            { "createDate", new Integer(Types.TIMESTAMP) },
            

            { "modifiedDate", new Integer(Types.TIMESTAMP) },
            

            { "parentFolderId", new Integer(Types.BIGINT) },
            

            { "name", new Integer(Types.VARCHAR) },
            

            { "description", new Integer(Types.VARCHAR) }
        };
    public static final String TABLE_SQL_CREATE = "create table FreestylerFolder (folderId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,createDate DATE null,modifiedDate DATE null,parentFolderId LONG,name VARCHAR(75) null,description VARCHAR(75) null)";
    public static final String TABLE_SQL_DROP = "drop table FreestylerFolder";
    public static final String DATA_SOURCE = "liferayDataSource";
    public static final String SESSION_FACTORY = "liferaySessionFactory";
    public static final String TX_MANAGER = "liferayTransactionManager";
    public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
                "value.object.entity.cache.enabled.com.ext.portlet.freestyler.model.FreestylerFolder"),
            true);
    public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
                "value.object.finder.cache.enabled.com.ext.portlet.freestyler.model.FreestylerFolder"),
            true);
    public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
                "lock.expiration.time.com.ext.portlet.freestyler.model.FreestylerFolder"));
    private long _folderId;
    private long _groupId;
    private long _originalGroupId;
    private boolean _setOriginalGroupId;
    private long _companyId;
    private long _userId;
    private Date _createDate;
    private Date _modifiedDate;
    private long _parentFolderId;
    private long _originalParentFolderId;
    private boolean _setOriginalParentFolderId;
    private String _name;
    private String _originalName;
    private String _description;
    private transient ExpandoBridge _expandoBridge;

    public FreestylerFolderModelImpl() {
    }

    public static FreestylerFolder toModel(FreestylerFolderSoap soapModel) {
        FreestylerFolder model = new FreestylerFolderImpl();

        model.setFolderId(soapModel.getFolderId());
        model.setGroupId(soapModel.getGroupId());
        model.setCompanyId(soapModel.getCompanyId());
        model.setUserId(soapModel.getUserId());
        model.setCreateDate(soapModel.getCreateDate());
        model.setModifiedDate(soapModel.getModifiedDate());
        model.setParentFolderId(soapModel.getParentFolderId());
        model.setName(soapModel.getName());
        model.setDescription(soapModel.getDescription());

        return model;
    }

    public static List<FreestylerFolder> toModels(
        FreestylerFolderSoap[] soapModels) {
        List<FreestylerFolder> models = new ArrayList<FreestylerFolder>(soapModels.length);

        for (FreestylerFolderSoap soapModel : soapModels) {
            models.add(toModel(soapModel));
        }

        return models;
    }

    public long getPrimaryKey() {
        return _folderId;
    }

    public void setPrimaryKey(long pk) {
        setFolderId(pk);
    }

    public Serializable getPrimaryKeyObj() {
        return new Long(_folderId);
    }

    public long getFolderId() {
        return _folderId;
    }

    public void setFolderId(long folderId) {
        _folderId = folderId;
    }

    public long getGroupId() {
        return _groupId;
    }

    public void setGroupId(long groupId) {
        _groupId = groupId;

        if (!_setOriginalGroupId) {
            _setOriginalGroupId = true;

            _originalGroupId = groupId;
        }
    }

    public long getOriginalGroupId() {
        return _originalGroupId;
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

    public long getParentFolderId() {
        return _parentFolderId;
    }

    public void setParentFolderId(long parentFolderId) {
        _parentFolderId = parentFolderId;

        if (!_setOriginalParentFolderId) {
            _setOriginalParentFolderId = true;

            _originalParentFolderId = parentFolderId;
        }
    }

    public long getOriginalParentFolderId() {
        return _originalParentFolderId;
    }

    public String getName() {
        return GetterUtil.getString(_name);
    }

    public void setName(String name) {
        _name = name;

        if (_originalName == null) {
            _originalName = name;
        }
    }

    public String getOriginalName() {
        return GetterUtil.getString(_originalName);
    }

    public String getDescription() {
        return GetterUtil.getString(_description);
    }

    public void setDescription(String description) {
        _description = description;
    }

    public FreestylerFolder toEscapedModel() {
        if (isEscapedModel()) {
            return (FreestylerFolder) this;
        } else {
            FreestylerFolder model = new FreestylerFolderImpl();

            model.setNew(isNew());
            model.setEscapedModel(true);

            model.setFolderId(getFolderId());
            model.setGroupId(getGroupId());
            model.setCompanyId(getCompanyId());
            model.setUserId(getUserId());
            model.setCreateDate(getCreateDate());
            model.setModifiedDate(getModifiedDate());
            model.setParentFolderId(getParentFolderId());
            model.setName(HtmlUtil.escape(getName()));
            model.setDescription(HtmlUtil.escape(getDescription()));

            model = (FreestylerFolder) Proxy.newProxyInstance(FreestylerFolder.class.getClassLoader(),
                    new Class[] { FreestylerFolder.class },
                    new ReadOnlyBeanHandler(model));

            return model;
        }
    }

    public ExpandoBridge getExpandoBridge() {
        if (_expandoBridge == null) {
            _expandoBridge = new ExpandoBridgeImpl(FreestylerFolder.class.getName(),
                    getPrimaryKey());
        }

        return _expandoBridge;
    }

    public Object clone() {
        FreestylerFolderImpl clone = new FreestylerFolderImpl();

        clone.setFolderId(getFolderId());
        clone.setGroupId(getGroupId());
        clone.setCompanyId(getCompanyId());
        clone.setUserId(getUserId());
        clone.setCreateDate(getCreateDate());
        clone.setModifiedDate(getModifiedDate());
        clone.setParentFolderId(getParentFolderId());
        clone.setName(getName());
        clone.setDescription(getDescription());

        return clone;
    }

    public int compareTo(FreestylerFolder freestylerFolder) {
        int value = 0;

        if (getFolderId() < freestylerFolder.getFolderId()) {
            value = -1;
        } else if (getFolderId() > freestylerFolder.getFolderId()) {
            value = 1;
        } else {
            value = 0;
        }

        if (value != 0) {
            return value;
        }

        value = getName().toLowerCase()
                    .compareTo(freestylerFolder.getName().toLowerCase());

        if (value != 0) {
            return value;
        }

        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        FreestylerFolder freestylerFolder = null;

        try {
            freestylerFolder = (FreestylerFolder) obj;
        } catch (ClassCastException cce) {
            return false;
        }

        long pk = freestylerFolder.getPrimaryKey();

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

        sb.append("{folderId=");
        sb.append(getFolderId());
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
        sb.append(", parentFolderId=");
        sb.append(getParentFolderId());
        sb.append(", name=");
        sb.append(getName());
        sb.append(", description=");
        sb.append(getDescription());
        sb.append("}");

        return sb.toString();
    }

    public String toXmlString() {
        StringBuilder sb = new StringBuilder();

        sb.append("<model><model-name>");
        sb.append("com.ext.portlet.freestyler.model.FreestylerFolder");
        sb.append("</model-name>");

        sb.append(
            "<column><column-name>folderId</column-name><column-value><![CDATA[");
        sb.append(getFolderId());
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
            "<column><column-name>parentFolderId</column-name><column-value><![CDATA[");
        sb.append(getParentFolderId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>name</column-name><column-value><![CDATA[");
        sb.append(getName());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>description</column-name><column-value><![CDATA[");
        sb.append(getDescription());
        sb.append("]]></column-value></column>");

        sb.append("</model>");

        return sb.toString();
    }
}
