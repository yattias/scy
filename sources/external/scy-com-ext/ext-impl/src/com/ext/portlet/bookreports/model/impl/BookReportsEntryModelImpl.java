package com.ext.portlet.bookreports.model.impl;

import com.ext.portlet.bookreports.model.BookReportsEntry;
import com.ext.portlet.bookreports.model.BookReportsEntrySoap;

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
 * <a href="BookReportsEntryModelImpl.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is a model that represents the <code>BookReportsEntry</code> table
 * in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.bookreports.model.BookReportsEntry
 * @see com.ext.portlet.bookreports.model.BookReportsEntryModel
 * @see com.ext.portlet.bookreports.model.impl.BookReportsEntryImpl
 *
 */
public class BookReportsEntryModelImpl extends BaseModelImpl<BookReportsEntry> {
    public static final String TABLE_NAME = "BookReportsEntry";
    public static final Object[][] TABLE_COLUMNS = {
            { "uuid_", new Integer(Types.VARCHAR) },
            

            { "entryId", new Integer(Types.BIGINT) },
            

            { "groupId", new Integer(Types.BIGINT) },
            

            { "companyId", new Integer(Types.BIGINT) },
            

            { "userId", new Integer(Types.BIGINT) },
            

            { "userName", new Integer(Types.VARCHAR) },
            

            { "createDate", new Integer(Types.TIMESTAMP) },
            

            { "modifiedDate", new Integer(Types.TIMESTAMP) },
            

            { "name", new Integer(Types.VARCHAR) },
            

            { "title", new Integer(Types.VARCHAR) }
        };
    public static final String TABLE_SQL_CREATE = "create table BookReportsEntry (uuid_ VARCHAR(75) null,entryId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,name VARCHAR(75) null,title VARCHAR(75) null)";
    public static final String TABLE_SQL_DROP = "drop table BookReportsEntry";
    public static final String DATA_SOURCE = "liferayDataSource";
    public static final String SESSION_FACTORY = "liferaySessionFactory";
    public static final String TX_MANAGER = "liferayTransactionManager";
    public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
                "value.object.entity.cache.enabled.com.ext.portlet.bookreports.model.BookReportsEntry"),
            true);
    public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.portal.util.PropsUtil.get(
                "value.object.finder.cache.enabled.com.ext.portlet.bookreports.model.BookReportsEntry"),
            true);
    public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.portal.util.PropsUtil.get(
                "lock.expiration.time.com.ext.portlet.bookreports.model.BookReportsEntry"));
    private String _uuid;
    private String _originalUuid;
    private Long _entryId;
    private Long _groupId;
    private Long _originalGroupId;
    private Long _companyId;
    private Long _userId;
    private String _userName;
    private Date _createDate;
    private Date _modifiedDate;
    private String _name;
    private String _title;

    public BookReportsEntryModelImpl() {
    }

    public static BookReportsEntry toModel(BookReportsEntrySoap soapModel) {
        BookReportsEntry model = new BookReportsEntryImpl();

        model.setUuid(soapModel.getUuid());
        model.setEntryId(soapModel.getEntryId());
        model.setGroupId(soapModel.getGroupId());
        model.setCompanyId(soapModel.getCompanyId());
        model.setUserId(soapModel.getUserId());
        model.setUserName(soapModel.getUserName());
        model.setCreateDate(soapModel.getCreateDate());
        model.setModifiedDate(soapModel.getModifiedDate());
        model.setName(soapModel.getName());
        model.setTitle(soapModel.getTitle());

        return model;
    }

    public static List<BookReportsEntry> toModels(
        BookReportsEntrySoap[] soapModels) {
        List<BookReportsEntry> models = new ArrayList<BookReportsEntry>(soapModels.length);

        for (BookReportsEntrySoap soapModel : soapModels) {
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

    public String getUuid() {
        return GetterUtil.getString(_uuid);
    }

    public void setUuid(String uuid) {
        _uuid = uuid;

        if (_originalUuid == null) {
            _originalUuid = uuid;
        }
    }

    public String getOriginalUuid() {
        return GetterUtil.getString(_originalUuid);
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

        if (_originalGroupId == null) {
            _originalGroupId = groupId;
        }
    }

    public Long getOriginalGroupId() {
        return _originalGroupId;
    }

    public Long getCompanyId() {
        return _companyId;
    }

    public void setCompanyId(Long companyId) {
        _companyId = companyId;
    }

    public Long getUserId() {
        return _userId;
    }

    public void setUserId(Long userId) {
        _userId = userId;
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

    public String getName() {
        return GetterUtil.getString(_name);
    }

    public void setName(String name) {
        _name = name;
    }

    public String getTitle() {
        return GetterUtil.getString(_title);
    }

    public void setTitle(String title) {
        _title = title;
    }

    public BookReportsEntry toEscapedModel() {
        if (isEscapedModel()) {
            return (BookReportsEntry) this;
        } else {
            BookReportsEntry model = new BookReportsEntryImpl();

            model.setNew(isNew());
            model.setEscapedModel(true);

            model.setUuid(HtmlUtil.escape(getUuid()));
            model.setEntryId(getEntryId());
            model.setGroupId(getGroupId());
            model.setCompanyId(getCompanyId());
            model.setUserId(getUserId());
            model.setUserName(HtmlUtil.escape(getUserName()));
            model.setCreateDate(getCreateDate());
            model.setModifiedDate(getModifiedDate());
            model.setName(HtmlUtil.escape(getName()));
            model.setTitle(HtmlUtil.escape(getTitle()));

            model = (BookReportsEntry) Proxy.newProxyInstance(BookReportsEntry.class.getClassLoader(),
                    new Class[] { BookReportsEntry.class },
                    new ReadOnlyBeanHandler(model));

            return model;
        }
    }

    public Object clone() {
        BookReportsEntryImpl clone = new BookReportsEntryImpl();

        clone.setUuid(getUuid());
        clone.setEntryId(getEntryId());
        clone.setGroupId(getGroupId());
        clone.setCompanyId(getCompanyId());
        clone.setUserId(getUserId());
        clone.setUserName(getUserName());
        clone.setCreateDate(getCreateDate());
        clone.setModifiedDate(getModifiedDate());
        clone.setName(getName());
        clone.setTitle(getTitle());

        return clone;
    }

    public int compareTo(BookReportsEntry bookReportsEntry) {
        int value = 0;

        value = getTitle().toLowerCase()
                    .compareTo(bookReportsEntry.getTitle().toLowerCase());

        if (value != 0) {
            return value;
        }

        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        BookReportsEntry bookReportsEntry = null;

        try {
            bookReportsEntry = (BookReportsEntry) obj;
        } catch (ClassCastException cce) {
            return false;
        }

        Long pk = bookReportsEntry.getPrimaryKey();

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

        sb.append("{uuid=");
        sb.append(getUuid());
        sb.append(", entryId=");
        sb.append(getEntryId());
        sb.append(", groupId=");
        sb.append(getGroupId());
        sb.append(", companyId=");
        sb.append(getCompanyId());
        sb.append(", userId=");
        sb.append(getUserId());
        sb.append(", userName=");
        sb.append(getUserName());
        sb.append(", createDate=");
        sb.append(getCreateDate());
        sb.append(", modifiedDate=");
        sb.append(getModifiedDate());
        sb.append(", name=");
        sb.append(getName());
        sb.append(", title=");
        sb.append(getTitle());
        sb.append("}");

        return sb.toString();
    }

    public String toXmlString() {
        StringBuilder sb = new StringBuilder();

        sb.append("<model><model-name>");
        sb.append("com.ext.portlet.bookreports.model.BookReportsEntry");
        sb.append("</model-name>");

        sb.append(
            "<column><column-name>uuid</column-name><column-value><![CDATA[");
        sb.append(getUuid());
        sb.append("]]></column-value></column>");
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
            "<column><column-name>userId</column-name><column-value><![CDATA[");
        sb.append(getUserId());
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
            "<column><column-name>name</column-name><column-value><![CDATA[");
        sb.append(getName());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>title</column-name><column-value><![CDATA[");
        sb.append(getTitle());
        sb.append("]]></column-value></column>");

        sb.append("</model>");

        return sb.toString();
    }
}
