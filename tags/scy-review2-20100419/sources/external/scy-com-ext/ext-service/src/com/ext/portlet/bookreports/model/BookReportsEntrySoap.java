package com.ext.portlet.bookreports.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <a href="BookReportsEntrySoap.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * <code>com.ext.portlet.bookreports.service.http.BookReportsEntryServiceSoap</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.bookreports.service.http.BookReportsEntryServiceSoap
 *
 */
public class BookReportsEntrySoap implements Serializable {
    private String _uuid;
    private Long _entryId;
    private Long _groupId;
    private Long _companyId;
    private Long _userId;
    private String _userName;
    private Date _createDate;
    private Date _modifiedDate;
    private String _name;
    private String _title;

    public BookReportsEntrySoap() {
    }

    public static BookReportsEntrySoap toSoapModel(BookReportsEntry model) {
        BookReportsEntrySoap soapModel = new BookReportsEntrySoap();

        soapModel.setUuid(model.getUuid());
        soapModel.setEntryId(model.getEntryId());
        soapModel.setGroupId(model.getGroupId());
        soapModel.setCompanyId(model.getCompanyId());
        soapModel.setUserId(model.getUserId());
        soapModel.setUserName(model.getUserName());
        soapModel.setCreateDate(model.getCreateDate());
        soapModel.setModifiedDate(model.getModifiedDate());
        soapModel.setName(model.getName());
        soapModel.setTitle(model.getTitle());

        return soapModel;
    }

    public static BookReportsEntrySoap[] toSoapModels(BookReportsEntry[] models) {
        BookReportsEntrySoap[] soapModels = new BookReportsEntrySoap[models.length];

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModel(models[i]);
        }

        return soapModels;
    }

    public static BookReportsEntrySoap[][] toSoapModels(
        BookReportsEntry[][] models) {
        BookReportsEntrySoap[][] soapModels = null;

        if (models.length > 0) {
            soapModels = new BookReportsEntrySoap[models.length][models[0].length];
        } else {
            soapModels = new BookReportsEntrySoap[0][0];
        }

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModels(models[i]);
        }

        return soapModels;
    }

    public static BookReportsEntrySoap[] toSoapModels(
        List<BookReportsEntry> models) {
        List<BookReportsEntrySoap> soapModels = new ArrayList<BookReportsEntrySoap>(models.size());

        for (BookReportsEntry model : models) {
            soapModels.add(toSoapModel(model));
        }

        return soapModels.toArray(new BookReportsEntrySoap[soapModels.size()]);
    }

    public Long getPrimaryKey() {
        return _entryId;
    }

    public void setPrimaryKey(Long pk) {
        setEntryId(pk);
    }

    public String getUuid() {
        return _uuid;
    }

    public void setUuid(String uuid) {
        _uuid = uuid;
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

    public Long getUserId() {
        return _userId;
    }

    public void setUserId(Long userId) {
        _userId = userId;
    }

    public String getUserName() {
        return _userName;
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
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }
}
