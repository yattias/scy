package com.ext.portlet.missionhandling.model.impl;

import com.ext.portlet.missionhandling.model.MissionEntry;
import com.ext.portlet.missionhandling.model.MissionEntrySoap;

import com.liferay.portal.kernel.bean.ReadOnlyBeanHandler;
import com.liferay.portal.kernel.util.GetterUtil;
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
 * <a href="MissionEntryModelImpl.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is a model that represents the <code>MissionEntry</code> table
 * in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.missionhandling.model.MissionEntry
 * @see com.ext.portlet.missionhandling.model.MissionEntryModel
 * @see com.ext.portlet.missionhandling.model.impl.MissionEntryImpl
 *
 */
public class MissionEntryModelImpl extends BaseModelImpl<MissionEntry> {
    public static final String TABLE_NAME = "MissionEntry";
    public static final Object[][] TABLE_COLUMNS = {
            { "missionEntryId", new Integer(Types.BIGINT) },
            

            { "companyId", new Integer(Types.BIGINT) },
            

            { "groupId", new Integer(Types.BIGINT) },
            

            { "organizationId", new Integer(Types.BIGINT) },
            

            { "createDate", new Integer(Types.TIMESTAMP) },
            

            { "modifiedDate", new Integer(Types.TIMESTAMP) },
            

            { "endDate", new Integer(Types.TIMESTAMP) },
            

            { "active_", new Integer(Types.BOOLEAN) }
        };
    public static final String TABLE_SQL_CREATE = "create table MissionEntry (missionEntryId LONG not null primary key,companyId LONG,groupId LONG,organizationId LONG,createDate DATE null,modifiedDate DATE null,endDate DATE null,active_ BOOLEAN)";
    public static final String TABLE_SQL_DROP = "drop table MissionEntry";
    public static final String DATA_SOURCE = "liferayDataSource";
    public static final String SESSION_FACTORY = "liferaySessionFactory";
    public static final String TX_MANAGER = "liferayTransactionManager";
    public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
                "value.object.entity.cache.enabled.com.ext.portlet.missionhandling.model.MissionEntry"),
            true);
    public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
                "value.object.finder.cache.enabled.com.ext.portlet.missionhandling.model.MissionEntry"),
            true);
    public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
                "lock.expiration.time.com.ext.portlet.missionhandling.model.MissionEntry"));
    private long _missionEntryId;
    private long _companyId;
    private long _groupId;
    private long _organizationId;
    private Date _createDate;
    private Date _modifiedDate;
    private Date _endDate;
    private boolean _active;
    private transient ExpandoBridge _expandoBridge;

    public MissionEntryModelImpl() {
    }

    public static MissionEntry toModel(MissionEntrySoap soapModel) {
        MissionEntry model = new MissionEntryImpl();

        model.setMissionEntryId(soapModel.getMissionEntryId());
        model.setCompanyId(soapModel.getCompanyId());
        model.setGroupId(soapModel.getGroupId());
        model.setOrganizationId(soapModel.getOrganizationId());
        model.setCreateDate(soapModel.getCreateDate());
        model.setModifiedDate(soapModel.getModifiedDate());
        model.setEndDate(soapModel.getEndDate());
        model.setActive(soapModel.getActive());

        return model;
    }

    public static List<MissionEntry> toModels(MissionEntrySoap[] soapModels) {
        List<MissionEntry> models = new ArrayList<MissionEntry>(soapModels.length);

        for (MissionEntrySoap soapModel : soapModels) {
            models.add(toModel(soapModel));
        }

        return models;
    }

    public long getPrimaryKey() {
        return _missionEntryId;
    }

    public void setPrimaryKey(long pk) {
        setMissionEntryId(pk);
    }

    public Serializable getPrimaryKeyObj() {
        return new Long(_missionEntryId);
    }

    public long getMissionEntryId() {
        return _missionEntryId;
    }

    public void setMissionEntryId(long missionEntryId) {
        _missionEntryId = missionEntryId;
    }

    public long getCompanyId() {
        return _companyId;
    }

    public void setCompanyId(long companyId) {
        _companyId = companyId;
    }

    public long getGroupId() {
        return _groupId;
    }

    public void setGroupId(long groupId) {
        _groupId = groupId;
    }

    public long getOrganizationId() {
        return _organizationId;
    }

    public void setOrganizationId(long organizationId) {
        _organizationId = organizationId;
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

    public Date getEndDate() {
        return _endDate;
    }

    public void setEndDate(Date endDate) {
        _endDate = endDate;
    }

    public boolean getActive() {
        return _active;
    }

    public boolean isActive() {
        return _active;
    }

    public void setActive(boolean active) {
        _active = active;
    }

    public MissionEntry toEscapedModel() {
        if (isEscapedModel()) {
            return (MissionEntry) this;
        } else {
            MissionEntry model = new MissionEntryImpl();

            model.setNew(isNew());
            model.setEscapedModel(true);

            model.setMissionEntryId(getMissionEntryId());
            model.setCompanyId(getCompanyId());
            model.setGroupId(getGroupId());
            model.setOrganizationId(getOrganizationId());
            model.setCreateDate(getCreateDate());
            model.setModifiedDate(getModifiedDate());
            model.setEndDate(getEndDate());
            model.setActive(getActive());

            model = (MissionEntry) Proxy.newProxyInstance(MissionEntry.class.getClassLoader(),
                    new Class[] { MissionEntry.class },
                    new ReadOnlyBeanHandler(model));

            return model;
        }
    }

    public ExpandoBridge getExpandoBridge() {
        if (_expandoBridge == null) {
            _expandoBridge = new ExpandoBridgeImpl(MissionEntry.class.getName(),
                    getPrimaryKey());
        }

        return _expandoBridge;
    }

    public Object clone() {
        MissionEntryImpl clone = new MissionEntryImpl();

        clone.setMissionEntryId(getMissionEntryId());
        clone.setCompanyId(getCompanyId());
        clone.setGroupId(getGroupId());
        clone.setOrganizationId(getOrganizationId());
        clone.setCreateDate(getCreateDate());
        clone.setModifiedDate(getModifiedDate());
        clone.setEndDate(getEndDate());
        clone.setActive(getActive());

        return clone;
    }

    public int compareTo(MissionEntry missionEntry) {
        int value = 0;

        if (getMissionEntryId() < missionEntry.getMissionEntryId()) {
            value = -1;
        } else if (getMissionEntryId() > missionEntry.getMissionEntryId()) {
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

        MissionEntry missionEntry = null;

        try {
            missionEntry = (MissionEntry) obj;
        } catch (ClassCastException cce) {
            return false;
        }

        long pk = missionEntry.getPrimaryKey();

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

        sb.append("{missionEntryId=");
        sb.append(getMissionEntryId());
        sb.append(", companyId=");
        sb.append(getCompanyId());
        sb.append(", groupId=");
        sb.append(getGroupId());
        sb.append(", organizationId=");
        sb.append(getOrganizationId());
        sb.append(", createDate=");
        sb.append(getCreateDate());
        sb.append(", modifiedDate=");
        sb.append(getModifiedDate());
        sb.append(", endDate=");
        sb.append(getEndDate());
        sb.append(", active=");
        sb.append(getActive());
        sb.append("}");

        return sb.toString();
    }

    public String toXmlString() {
        StringBuilder sb = new StringBuilder();

        sb.append("<model><model-name>");
        sb.append("com.ext.portlet.missionhandling.model.MissionEntry");
        sb.append("</model-name>");

        sb.append(
            "<column><column-name>missionEntryId</column-name><column-value><![CDATA[");
        sb.append(getMissionEntryId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>companyId</column-name><column-value><![CDATA[");
        sb.append(getCompanyId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>groupId</column-name><column-value><![CDATA[");
        sb.append(getGroupId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>organizationId</column-name><column-value><![CDATA[");
        sb.append(getOrganizationId());
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
            "<column><column-name>endDate</column-name><column-value><![CDATA[");
        sb.append(getEndDate());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>active</column-name><column-value><![CDATA[");
        sb.append(getActive());
        sb.append("]]></column-value></column>");

        sb.append("</model>");

        return sb.toString();
    }
}
