package com.ext.portlet.freestyler.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <a href="FreestylerEntrySoap.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * <code>com.ext.portlet.freestyler.service.http.FreestylerEntryServiceSoap</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.freestyler.service.http.FreestylerEntryServiceSoap
 *
 */
public class FreestylerEntrySoap implements Serializable {
    private long _freestylerId;
    private long _groupId;
    private long _companyId;
    private long _userId;
    private String _name;
    private String _description;
    private long _xmlFileId;
    private Date _createDate;
    private Date _modifiedDate;

    public FreestylerEntrySoap() {
    }

    public static FreestylerEntrySoap toSoapModel(FreestylerEntry model) {
        FreestylerEntrySoap soapModel = new FreestylerEntrySoap();

        soapModel.setFreestylerId(model.getFreestylerId());
        soapModel.setGroupId(model.getGroupId());
        soapModel.setCompanyId(model.getCompanyId());
        soapModel.setUserId(model.getUserId());
        soapModel.setName(model.getName());
        soapModel.setDescription(model.getDescription());
        soapModel.setXmlFileId(model.getXmlFileId());
        soapModel.setCreateDate(model.getCreateDate());
        soapModel.setModifiedDate(model.getModifiedDate());

        return soapModel;
    }

    public static FreestylerEntrySoap[] toSoapModels(FreestylerEntry[] models) {
        FreestylerEntrySoap[] soapModels = new FreestylerEntrySoap[models.length];

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModel(models[i]);
        }

        return soapModels;
    }

    public static FreestylerEntrySoap[][] toSoapModels(
        FreestylerEntry[][] models) {
        FreestylerEntrySoap[][] soapModels = null;

        if (models.length > 0) {
            soapModels = new FreestylerEntrySoap[models.length][models[0].length];
        } else {
            soapModels = new FreestylerEntrySoap[0][0];
        }

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModels(models[i]);
        }

        return soapModels;
    }

    public static FreestylerEntrySoap[] toSoapModels(
        List<FreestylerEntry> models) {
        List<FreestylerEntrySoap> soapModels = new ArrayList<FreestylerEntrySoap>(models.size());

        for (FreestylerEntry model : models) {
            soapModels.add(toSoapModel(model));
        }

        return soapModels.toArray(new FreestylerEntrySoap[soapModels.size()]);
    }

    public long getPrimaryKey() {
        return _freestylerId;
    }

    public void setPrimaryKey(long pk) {
        setFreestylerId(pk);
    }

    public long getFreestylerId() {
        return _freestylerId;
    }

    public void setFreestylerId(long freestylerId) {
        _freestylerId = freestylerId;
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
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getDescription() {
        return _description;
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
}
