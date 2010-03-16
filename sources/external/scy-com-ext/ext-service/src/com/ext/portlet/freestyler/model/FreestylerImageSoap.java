package com.ext.portlet.freestyler.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <a href="FreestylerImageSoap.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * <code>com.ext.portlet.freestyler.service.http.FreestylerImageServiceSoap</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.freestyler.service.http.FreestylerImageServiceSoap
 *
 */
public class FreestylerImageSoap implements Serializable {
    private long _imageId;
    private long _groupId;
    private long _companyId;
    private long _userId;
    private Date _createDate;
    private Date _modifiedDate;
    private long _freestylerId;
    private long _folderId;
    private String _name;
    private String _description;
    private long _smallImageId;
    private long _largeImageId;
    private long _custom1ImageId;
    private long _custom2ImageId;

    public FreestylerImageSoap() {
    }

    public static FreestylerImageSoap toSoapModel(FreestylerImage model) {
        FreestylerImageSoap soapModel = new FreestylerImageSoap();

        soapModel.setImageId(model.getImageId());
        soapModel.setGroupId(model.getGroupId());
        soapModel.setCompanyId(model.getCompanyId());
        soapModel.setUserId(model.getUserId());
        soapModel.setCreateDate(model.getCreateDate());
        soapModel.setModifiedDate(model.getModifiedDate());
        soapModel.setFreestylerId(model.getFreestylerId());
        soapModel.setFolderId(model.getFolderId());
        soapModel.setName(model.getName());
        soapModel.setDescription(model.getDescription());
        soapModel.setSmallImageId(model.getSmallImageId());
        soapModel.setLargeImageId(model.getLargeImageId());
        soapModel.setCustom1ImageId(model.getCustom1ImageId());
        soapModel.setCustom2ImageId(model.getCustom2ImageId());

        return soapModel;
    }

    public static FreestylerImageSoap[] toSoapModels(FreestylerImage[] models) {
        FreestylerImageSoap[] soapModels = new FreestylerImageSoap[models.length];

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModel(models[i]);
        }

        return soapModels;
    }

    public static FreestylerImageSoap[][] toSoapModels(
        FreestylerImage[][] models) {
        FreestylerImageSoap[][] soapModels = null;

        if (models.length > 0) {
            soapModels = new FreestylerImageSoap[models.length][models[0].length];
        } else {
            soapModels = new FreestylerImageSoap[0][0];
        }

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModels(models[i]);
        }

        return soapModels;
    }

    public static FreestylerImageSoap[] toSoapModels(
        List<FreestylerImage> models) {
        List<FreestylerImageSoap> soapModels = new ArrayList<FreestylerImageSoap>(models.size());

        for (FreestylerImage model : models) {
            soapModels.add(toSoapModel(model));
        }

        return soapModels.toArray(new FreestylerImageSoap[soapModels.size()]);
    }

    public long getPrimaryKey() {
        return _imageId;
    }

    public void setPrimaryKey(long pk) {
        setImageId(pk);
    }

    public long getImageId() {
        return _imageId;
    }

    public void setImageId(long imageId) {
        _imageId = imageId;
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

    public long getFreestylerId() {
        return _freestylerId;
    }

    public void setFreestylerId(long freestylerId) {
        _freestylerId = freestylerId;
    }

    public long getFolderId() {
        return _folderId;
    }

    public void setFolderId(long folderId) {
        _folderId = folderId;
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

    public long getSmallImageId() {
        return _smallImageId;
    }

    public void setSmallImageId(long smallImageId) {
        _smallImageId = smallImageId;
    }

    public long getLargeImageId() {
        return _largeImageId;
    }

    public void setLargeImageId(long largeImageId) {
        _largeImageId = largeImageId;
    }

    public long getCustom1ImageId() {
        return _custom1ImageId;
    }

    public void setCustom1ImageId(long custom1ImageId) {
        _custom1ImageId = custom1ImageId;
    }

    public long getCustom2ImageId() {
        return _custom2ImageId;
    }

    public void setCustom2ImageId(long custom2ImageId) {
        _custom2ImageId = custom2ImageId;
    }
}
