package com.ext.portlet.linkTool.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <a href="LinkEntrySoap.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * <code>com.ext.portlet.linkTool.service.http.LinkEntryServiceSoap</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.linkTool.service.http.LinkEntryServiceSoap
 *
 */
public class LinkEntrySoap implements Serializable {
    private long _linkId;
    private long _groupId;
    private long _companyId;
    private long _userId;
    private Date _createDate;
    private Date _modifiedDate;
    private String _resourceId;
    private String _linkedResourceId;
    private String _linkedResourceClassNameId;

    public LinkEntrySoap() {
    }

    public static LinkEntrySoap toSoapModel(LinkEntry model) {
        LinkEntrySoap soapModel = new LinkEntrySoap();

        soapModel.setLinkId(model.getLinkId());
        soapModel.setGroupId(model.getGroupId());
        soapModel.setCompanyId(model.getCompanyId());
        soapModel.setUserId(model.getUserId());
        soapModel.setCreateDate(model.getCreateDate());
        soapModel.setModifiedDate(model.getModifiedDate());
        soapModel.setResourceId(model.getResourceId());
        soapModel.setLinkedResourceId(model.getLinkedResourceId());
        soapModel.setLinkedResourceClassNameId(model.getLinkedResourceClassNameId());

        return soapModel;
    }

    public static LinkEntrySoap[] toSoapModels(LinkEntry[] models) {
        LinkEntrySoap[] soapModels = new LinkEntrySoap[models.length];

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModel(models[i]);
        }

        return soapModels;
    }

    public static LinkEntrySoap[][] toSoapModels(LinkEntry[][] models) {
        LinkEntrySoap[][] soapModels = null;

        if (models.length > 0) {
            soapModels = new LinkEntrySoap[models.length][models[0].length];
        } else {
            soapModels = new LinkEntrySoap[0][0];
        }

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModels(models[i]);
        }

        return soapModels;
    }

    public static LinkEntrySoap[] toSoapModels(List<LinkEntry> models) {
        List<LinkEntrySoap> soapModels = new ArrayList<LinkEntrySoap>(models.size());

        for (LinkEntry model : models) {
            soapModels.add(toSoapModel(model));
        }

        return soapModels.toArray(new LinkEntrySoap[soapModels.size()]);
    }

    public long getPrimaryKey() {
        return _linkId;
    }

    public void setPrimaryKey(long pk) {
        setLinkId(pk);
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
        return _resourceId;
    }

    public void setResourceId(String resourceId) {
        _resourceId = resourceId;
    }

    public String getLinkedResourceId() {
        return _linkedResourceId;
    }

    public void setLinkedResourceId(String linkedResourceId) {
        _linkedResourceId = linkedResourceId;
    }

    public String getLinkedResourceClassNameId() {
        return _linkedResourceClassNameId;
    }

    public void setLinkedResourceClassNameId(String linkedResourceClassNameId) {
        _linkedResourceClassNameId = linkedResourceClassNameId;
    }
}
