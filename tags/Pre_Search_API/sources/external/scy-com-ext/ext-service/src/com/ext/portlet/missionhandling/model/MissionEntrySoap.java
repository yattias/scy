package com.ext.portlet.missionhandling.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <a href="MissionEntrySoap.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class is used by
 * <code>com.ext.portlet.missionhandling.service.http.MissionEntryServiceSoap</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.missionhandling.service.http.MissionEntryServiceSoap
 *
 */
public class MissionEntrySoap implements Serializable {
    private long _missionEntryId;
    private long _companyId;
    private long _groupId;
    private long _organizationId;
    private Date _createDate;
    private Date _modifiedDate;
    private Date _endDate;
    private boolean _active;

    public MissionEntrySoap() {
    }

    public static MissionEntrySoap toSoapModel(MissionEntry model) {
        MissionEntrySoap soapModel = new MissionEntrySoap();

        soapModel.setMissionEntryId(model.getMissionEntryId());
        soapModel.setCompanyId(model.getCompanyId());
        soapModel.setGroupId(model.getGroupId());
        soapModel.setOrganizationId(model.getOrganizationId());
        soapModel.setCreateDate(model.getCreateDate());
        soapModel.setModifiedDate(model.getModifiedDate());
        soapModel.setEndDate(model.getEndDate());
        soapModel.setActive(model.getActive());

        return soapModel;
    }

    public static MissionEntrySoap[] toSoapModels(MissionEntry[] models) {
        MissionEntrySoap[] soapModels = new MissionEntrySoap[models.length];

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModel(models[i]);
        }

        return soapModels;
    }

    public static MissionEntrySoap[][] toSoapModels(MissionEntry[][] models) {
        MissionEntrySoap[][] soapModels = null;

        if (models.length > 0) {
            soapModels = new MissionEntrySoap[models.length][models[0].length];
        } else {
            soapModels = new MissionEntrySoap[0][0];
        }

        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModels(models[i]);
        }

        return soapModels;
    }

    public static MissionEntrySoap[] toSoapModels(List<MissionEntry> models) {
        List<MissionEntrySoap> soapModels = new ArrayList<MissionEntrySoap>(models.size());

        for (MissionEntry model : models) {
            soapModels.add(toSoapModel(model));
        }

        return soapModels.toArray(new MissionEntrySoap[soapModels.size()]);
    }

    public long getPrimaryKey() {
        return _missionEntryId;
    }

    public void setPrimaryKey(long pk) {
        setMissionEntryId(pk);
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
}
