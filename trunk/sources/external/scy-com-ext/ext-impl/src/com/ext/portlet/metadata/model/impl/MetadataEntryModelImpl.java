package com.ext.portlet.metadata.model.impl;

import com.ext.portlet.metadata.model.MetadataEntry;
import com.ext.portlet.metadata.model.MetadataEntrySoap;

import com.liferay.portal.kernel.bean.ReadOnlyBeanHandler;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.model.impl.BaseModelImpl;

import java.io.Serializable;

import java.lang.reflect.Proxy;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <a href="MetadataEntryModelImpl.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is a model that represents the <code>MetadataEntry</code> table
 * in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.metadata.model.MetadataEntry
 * @see com.ext.portlet.metadata.model.MetadataEntryModel
 * @see com.ext.portlet.metadata.model.impl.MetadataEntryImpl
 *
 */
public class MetadataEntryModelImpl extends BaseModelImpl<MetadataEntry> {
    public static final String TABLE_NAME = "MetadataEntry";
    public static final Object[][] TABLE_COLUMNS = {
            { "entryId", new Integer(Types.BIGINT) },
            

            { "groupId", new Integer(Types.BIGINT) },
            

            { "companyId", new Integer(Types.BIGINT) },
            

            { "createDate", new Integer(Types.TIMESTAMP) },
            

            { "modifiedDate", new Integer(Types.TIMESTAMP) },
            

            { "assertEntryId", new Integer(Types.BIGINT) },
            

            { "dc_contributor", new Integer(Types.VARCHAR) },
            

            { "dc_coverage", new Integer(Types.VARCHAR) },
            

            { "dc_creator", new Integer(Types.VARCHAR) },
            

            { "dc_date", new Integer(Types.VARCHAR) },
            

            { "dc_description", new Integer(Types.VARCHAR) },
            

            { "dc_format", new Integer(Types.VARCHAR) },
            

            { "dc_identifier", new Integer(Types.VARCHAR) },
            

            { "dc_language", new Integer(Types.VARCHAR) },
            

            { "dc_publisher", new Integer(Types.VARCHAR) },
            

            { "dc_relation", new Integer(Types.VARCHAR) },
            

            { "dc_rights", new Integer(Types.VARCHAR) },
            

            { "dc_source", new Integer(Types.VARCHAR) },
            

            { "dc_subject", new Integer(Types.VARCHAR) },
            

            { "dc_title", new Integer(Types.VARCHAR) },
            

            { "dc_type", new Integer(Types.VARCHAR) }
        };
    public static final String TABLE_SQL_CREATE = "create table MetadataEntry (entryId LONG not null primary key,groupId LONG,companyId LONG,createDate DATE null,modifiedDate DATE null,assertEntryId LONG,dc_contributor VARCHAR(75) null,dc_coverage VARCHAR(75) null,dc_creator VARCHAR(75) null,dc_date VARCHAR(75) null,dc_description VARCHAR(75) null,dc_format VARCHAR(75) null,dc_identifier VARCHAR(75) null,dc_language VARCHAR(75) null,dc_publisher VARCHAR(75) null,dc_relation VARCHAR(75) null,dc_rights VARCHAR(75) null,dc_source VARCHAR(75) null,dc_subject VARCHAR(75) null,dc_title VARCHAR(75) null,dc_type VARCHAR(75) null)";
    public static final String TABLE_SQL_DROP = "drop table MetadataEntry";
    public static final String DATA_SOURCE = "liferayDataSource";
    public static final String SESSION_FACTORY = "liferaySessionFactory";
    public static final String TX_MANAGER = "liferayTransactionManager";
    public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
                "value.object.entity.cache.enabled.com.ext.portlet.metadata.model.MetadataEntry"),
            true);
    public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
                "value.object.finder.cache.enabled.com.ext.portlet.metadata.model.MetadataEntry"),
            true);
    public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
                "lock.expiration.time.com.ext.portlet.metadata.model.MetadataEntry"));
    private Long _entryId;
    private Long _groupId;
    private Long _companyId;
    private Date _createDate;
    private Date _modifiedDate;
    private Long _assertEntryId;
    private Long _originalAssertEntryId;
    private String _dc_contributor;
    private String _dc_coverage;
    private String _dc_creator;
    private String _dc_date;
    private String _dc_description;
    private String _dc_format;
    private String _dc_identifier;
    private String _dc_language;
    private String _dc_publisher;
    private String _dc_relation;
    private String _dc_rights;
    private String _dc_source;
    private String _dc_subject;
    private String _dc_title;
    private String _dc_type;

    public MetadataEntryModelImpl() {
    }

    public static MetadataEntry toModel(MetadataEntrySoap soapModel) {
        MetadataEntry model = new MetadataEntryImpl();

        model.setEntryId(soapModel.getEntryId());
        model.setGroupId(soapModel.getGroupId());
        model.setCompanyId(soapModel.getCompanyId());
        model.setCreateDate(soapModel.getCreateDate());
        model.setModifiedDate(soapModel.getModifiedDate());
        model.setAssertEntryId(soapModel.getAssertEntryId());
        model.setDc_contributor(soapModel.getDc_contributor());
        model.setDc_coverage(soapModel.getDc_coverage());
        model.setDc_creator(soapModel.getDc_creator());
        model.setDc_date(soapModel.getDc_date());
        model.setDc_description(soapModel.getDc_description());
        model.setDc_format(soapModel.getDc_format());
        model.setDc_identifier(soapModel.getDc_identifier());
        model.setDc_language(soapModel.getDc_language());
        model.setDc_publisher(soapModel.getDc_publisher());
        model.setDc_relation(soapModel.getDc_relation());
        model.setDc_rights(soapModel.getDc_rights());
        model.setDc_source(soapModel.getDc_source());
        model.setDc_subject(soapModel.getDc_subject());
        model.setDc_title(soapModel.getDc_title());
        model.setDc_type(soapModel.getDc_type());

        return model;
    }

    public static List<MetadataEntry> toModels(MetadataEntrySoap[] soapModels) {
        List<MetadataEntry> models = new ArrayList<MetadataEntry>(soapModels.length);

        for (MetadataEntrySoap soapModel : soapModels) {
            models.add(toModel(soapModel));
        }

        return models;
    }

    public Long getPrimaryKey() {
        return _entryId;
    }

    public void setPrimaryKey(Long pk) {
        setEntryId(pk);
    }

    public Serializable getPrimaryKeyObj() {
        return _entryId;
    }

    public Long getEntryId() {
        return _entryId;
    }

    public void setEntryId(Long entryId) {
        _entryId = entryId;
    }

    public Long getGroupId() {
        return _groupId;
    }

    public void setGroupId(Long groupId) {
        _groupId = groupId;
    }

    public Long getCompanyId() {
        return _companyId;
    }

    public void setCompanyId(Long companyId) {
        _companyId = companyId;
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

    public Long getAssertEntryId() {
        return _assertEntryId;
    }

    public void setAssertEntryId(Long assertEntryId) {
        _assertEntryId = assertEntryId;

        if (_originalAssertEntryId == null) {
            _originalAssertEntryId = assertEntryId;
        }
    }

    public Long getOriginalAssertEntryId() {
        return _originalAssertEntryId;
    }

    public String getDc_contributor() {
        return GetterUtil.getString(_dc_contributor);
    }

    public void setDc_contributor(String dc_contributor) {
        _dc_contributor = dc_contributor;
    }

    public String getDc_coverage() {
        return GetterUtil.getString(_dc_coverage);
    }

    public void setDc_coverage(String dc_coverage) {
        _dc_coverage = dc_coverage;
    }

    public String getDc_creator() {
        return GetterUtil.getString(_dc_creator);
    }

    public void setDc_creator(String dc_creator) {
        _dc_creator = dc_creator;
    }

    public String getDc_date() {
        return GetterUtil.getString(_dc_date);
    }

    public void setDc_date(String dc_date) {
        _dc_date = dc_date;
    }

    public String getDc_description() {
        return GetterUtil.getString(_dc_description);
    }

    public void setDc_description(String dc_description) {
        _dc_description = dc_description;
    }

    public String getDc_format() {
        return GetterUtil.getString(_dc_format);
    }

    public void setDc_format(String dc_format) {
        _dc_format = dc_format;
    }

    public String getDc_identifier() {
        return GetterUtil.getString(_dc_identifier);
    }

    public void setDc_identifier(String dc_identifier) {
        _dc_identifier = dc_identifier;
    }

    public String getDc_language() {
        return GetterUtil.getString(_dc_language);
    }

    public void setDc_language(String dc_language) {
        _dc_language = dc_language;
    }

    public String getDc_publisher() {
        return GetterUtil.getString(_dc_publisher);
    }

    public void setDc_publisher(String dc_publisher) {
        _dc_publisher = dc_publisher;
    }

    public String getDc_relation() {
        return GetterUtil.getString(_dc_relation);
    }

    public void setDc_relation(String dc_relation) {
        _dc_relation = dc_relation;
    }

    public String getDc_rights() {
        return GetterUtil.getString(_dc_rights);
    }

    public void setDc_rights(String dc_rights) {
        _dc_rights = dc_rights;
    }

    public String getDc_source() {
        return GetterUtil.getString(_dc_source);
    }

    public void setDc_source(String dc_source) {
        _dc_source = dc_source;
    }

    public String getDc_subject() {
        return GetterUtil.getString(_dc_subject);
    }

    public void setDc_subject(String dc_subject) {
        _dc_subject = dc_subject;
    }

    public String getDc_title() {
        return GetterUtil.getString(_dc_title);
    }

    public void setDc_title(String dc_title) {
        _dc_title = dc_title;
    }

    public String getDc_type() {
        return GetterUtil.getString(_dc_type);
    }

    public void setDc_type(String dc_type) {
        _dc_type = dc_type;
    }

    public MetadataEntry toEscapedModel() {
        if (isEscapedModel()) {
            return (MetadataEntry) this;
        } else {
            MetadataEntry model = new MetadataEntryImpl();

            model.setNew(isNew());
            model.setEscapedModel(true);

            model.setEntryId(getEntryId());
            model.setGroupId(getGroupId());
            model.setCompanyId(getCompanyId());
            model.setCreateDate(getCreateDate());
            model.setModifiedDate(getModifiedDate());
            model.setAssertEntryId(getAssertEntryId());
            model.setDc_contributor(HtmlUtil.escape(getDc_contributor()));
            model.setDc_coverage(HtmlUtil.escape(getDc_coverage()));
            model.setDc_creator(HtmlUtil.escape(getDc_creator()));
            model.setDc_date(HtmlUtil.escape(getDc_date()));
            model.setDc_description(HtmlUtil.escape(getDc_description()));
            model.setDc_format(HtmlUtil.escape(getDc_format()));
            model.setDc_identifier(HtmlUtil.escape(getDc_identifier()));
            model.setDc_language(HtmlUtil.escape(getDc_language()));
            model.setDc_publisher(HtmlUtil.escape(getDc_publisher()));
            model.setDc_relation(HtmlUtil.escape(getDc_relation()));
            model.setDc_rights(HtmlUtil.escape(getDc_rights()));
            model.setDc_source(HtmlUtil.escape(getDc_source()));
            model.setDc_subject(HtmlUtil.escape(getDc_subject()));
            model.setDc_title(HtmlUtil.escape(getDc_title()));
            model.setDc_type(HtmlUtil.escape(getDc_type()));

            model = (MetadataEntry) Proxy.newProxyInstance(MetadataEntry.class.getClassLoader(),
                    new Class[] { MetadataEntry.class },
                    new ReadOnlyBeanHandler(model));

            return model;
        }
    }

    public Object clone() {
        MetadataEntryImpl clone = new MetadataEntryImpl();

        clone.setEntryId(getEntryId());
        clone.setGroupId(getGroupId());
        clone.setCompanyId(getCompanyId());
        clone.setCreateDate(getCreateDate());
        clone.setModifiedDate(getModifiedDate());
        clone.setAssertEntryId(getAssertEntryId());
        clone.setDc_contributor(getDc_contributor());
        clone.setDc_coverage(getDc_coverage());
        clone.setDc_creator(getDc_creator());
        clone.setDc_date(getDc_date());
        clone.setDc_description(getDc_description());
        clone.setDc_format(getDc_format());
        clone.setDc_identifier(getDc_identifier());
        clone.setDc_language(getDc_language());
        clone.setDc_publisher(getDc_publisher());
        clone.setDc_relation(getDc_relation());
        clone.setDc_rights(getDc_rights());
        clone.setDc_source(getDc_source());
        clone.setDc_subject(getDc_subject());
        clone.setDc_title(getDc_title());
        clone.setDc_type(getDc_type());

        return clone;
    }

    public int compareTo(MetadataEntry metadataEntry) {
        int value = 0;

        value = getDc_title().toLowerCase()
                    .compareTo(metadataEntry.getDc_title().toLowerCase());

        if (value != 0) {
            return value;
        }

        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        MetadataEntry metadataEntry = null;

        try {
            metadataEntry = (MetadataEntry) obj;
        } catch (ClassCastException cce) {
            return false;
        }

        Long pk = metadataEntry.getPrimaryKey();

        if (getPrimaryKey().equals(pk)) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return getPrimaryKey().hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{entryId=");
        sb.append(getEntryId());
        sb.append(", groupId=");
        sb.append(getGroupId());
        sb.append(", companyId=");
        sb.append(getCompanyId());
        sb.append(", createDate=");
        sb.append(getCreateDate());
        sb.append(", modifiedDate=");
        sb.append(getModifiedDate());
        sb.append(", assertEntryId=");
        sb.append(getAssertEntryId());
        sb.append(", dc_contributor=");
        sb.append(getDc_contributor());
        sb.append(", dc_coverage=");
        sb.append(getDc_coverage());
        sb.append(", dc_creator=");
        sb.append(getDc_creator());
        sb.append(", dc_date=");
        sb.append(getDc_date());
        sb.append(", dc_description=");
        sb.append(getDc_description());
        sb.append(", dc_format=");
        sb.append(getDc_format());
        sb.append(", dc_identifier=");
        sb.append(getDc_identifier());
        sb.append(", dc_language=");
        sb.append(getDc_language());
        sb.append(", dc_publisher=");
        sb.append(getDc_publisher());
        sb.append(", dc_relation=");
        sb.append(getDc_relation());
        sb.append(", dc_rights=");
        sb.append(getDc_rights());
        sb.append(", dc_source=");
        sb.append(getDc_source());
        sb.append(", dc_subject=");
        sb.append(getDc_subject());
        sb.append(", dc_title=");
        sb.append(getDc_title());
        sb.append(", dc_type=");
        sb.append(getDc_type());
        sb.append("}");

        return sb.toString();
    }

    public String toXmlString() {
        StringBuilder sb = new StringBuilder();

        sb.append("<model><model-name>");
        sb.append("com.ext.portlet.metadata.model.MetadataEntry");
        sb.append("</model-name>");

        sb.append(
            "<column><column-name>entryId</column-name><column-value><![CDATA[");
        sb.append(getEntryId());
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
            "<column><column-name>createDate</column-name><column-value><![CDATA[");
        sb.append(getCreateDate());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>modifiedDate</column-name><column-value><![CDATA[");
        sb.append(getModifiedDate());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>assertEntryId</column-name><column-value><![CDATA[");
        sb.append(getAssertEntryId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>dc_contributor</column-name><column-value><![CDATA[");
        sb.append(getDc_contributor());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>dc_coverage</column-name><column-value><![CDATA[");
        sb.append(getDc_coverage());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>dc_creator</column-name><column-value><![CDATA[");
        sb.append(getDc_creator());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>dc_date</column-name><column-value><![CDATA[");
        sb.append(getDc_date());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>dc_description</column-name><column-value><![CDATA[");
        sb.append(getDc_description());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>dc_format</column-name><column-value><![CDATA[");
        sb.append(getDc_format());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>dc_identifier</column-name><column-value><![CDATA[");
        sb.append(getDc_identifier());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>dc_language</column-name><column-value><![CDATA[");
        sb.append(getDc_language());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>dc_publisher</column-name><column-value><![CDATA[");
        sb.append(getDc_publisher());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>dc_relation</column-name><column-value><![CDATA[");
        sb.append(getDc_relation());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>dc_rights</column-name><column-value><![CDATA[");
        sb.append(getDc_rights());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>dc_source</column-name><column-value><![CDATA[");
        sb.append(getDc_source());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>dc_subject</column-name><column-value><![CDATA[");
        sb.append(getDc_subject());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>dc_title</column-name><column-value><![CDATA[");
        sb.append(getDc_title());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>dc_type</column-name><column-value><![CDATA[");
        sb.append(getDc_type());
        sb.append("]]></column-value></column>");

        sb.append("</model>");

        return sb.toString();
    }
}
