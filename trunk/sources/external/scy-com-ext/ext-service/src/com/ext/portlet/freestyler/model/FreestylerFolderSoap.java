package com.ext.portlet.freestyler.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <a href="FreestylerFolderSoap.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * <code>com.ext.portlet.freestyler.service.http.FreestylerFolderServiceSoap</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.freestyler.service.http.FreestylerFolderServiceSoap
 *
 */
public class FreestylerFolderSoap implements Serializable {
    private long _folderId;
    private long _groupId;
    private long _companyId;
    private long _userId;
    private Date _createDate;
    private Date _modifiedDate;
    private long _parentFolderId;
    private String _name;
    private String _description;

    public FreestylerFolderSoap() {
    }

    public static FreestylerFolderSoap toSoapModel(FreestylerFolder model) {
        FreestylerFolderSoap soapModel = new FreestylerFolderSoap();

        soapModel.setFolderId(model.getFolderId());
        soapModel.setGroupId(model.getGroupId());
        soapModel.setCompanyId(model.getCompanyId());
        soapModel.setUserId(model.getUserId());
        soapModel.setCreateDate(model.getCreateDate());
        soapModel.setModifiedDate(model.getModifiedDate());
        soapModel.setParentFolderId(model.getParentFolderId());
        soapModel.setName(model.getName());
        soapModel.setDescription(model.getDescription());

        return soapModel;
    }

    public static FreestylerFolderSoap[] toSoapModels(FreestylerFolder[] models) {
        FreestylerFolderSoap[] soapModels = new FreestylerFolderSoap[models.length];

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModel(models[i]);
        }

        return soapModels;
    }

    public static FreestylerFolderSoap[][] toSoapModels(
        FreestylerFolder[][] models) {
        FreestylerFolderSoap[][] soapModels = null;

        if (models.length > 0) {
            soapModels = new FreestylerFolderSoap[models.length][models[0].length];
        } else {
            soapModels = new FreestylerFolderSoap[0][0];
        }

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModels(models[i]);
        }

        return soapModels;
    }

    public static FreestylerFolderSoap[] toSoapModels(
        List<FreestylerFolder> models) {
        List<FreestylerFolderSoap> soapModels = new ArrayList<FreestylerFolderSoap>(models.size());

        for (FreestylerFolder model : models) {
            soapModels.add(toSoapModel(model));
        }

        return soapModels.toArray(new FreestylerFolderSoap[soapModels.size()]);
    }

    public long getPrimaryKey() {
        return _folderId;
    }

    public void setPrimaryKey(long pk) {
        setFolderId(pk);
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
}
