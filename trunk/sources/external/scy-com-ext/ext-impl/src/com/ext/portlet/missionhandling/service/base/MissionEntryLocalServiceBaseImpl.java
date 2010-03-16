package com.ext.portlet.missionhandling.service.base;

import com.ext.portlet.missionhandling.model.MissionEntry;
import com.ext.portlet.missionhandling.service.MissionEntryLocalService;
import com.ext.portlet.missionhandling.service.MissionEntryService;
import com.ext.portlet.missionhandling.service.persistence.MissionEntryPersistence;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.BeanReference;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.util.PortalUtil;

import java.util.List;


public abstract class MissionEntryLocalServiceBaseImpl
    implements MissionEntryLocalService {
    @BeanReference(name = "com.ext.portlet.missionhandling.service.MissionEntryLocalService.impl")
    protected MissionEntryLocalService missionEntryLocalService;
    @BeanReference(name = "com.ext.portlet.missionhandling.service.MissionEntryService.impl")
    protected MissionEntryService missionEntryService;
    @BeanReference(name = "com.ext.portlet.missionhandling.service.persistence.MissionEntryPersistence.impl")
    protected MissionEntryPersistence missionEntryPersistence;

    public MissionEntry addMissionEntry(MissionEntry missionEntry)
        throws SystemException {
        missionEntry.setNew(true);

        return missionEntryPersistence.update(missionEntry, false);
    }

    public MissionEntry createMissionEntry(long missionEntryId) {
        return missionEntryPersistence.create(missionEntryId);
    }

    public void deleteMissionEntry(long missionEntryId)
        throws PortalException, SystemException {
        missionEntryPersistence.remove(missionEntryId);
    }

    public void deleteMissionEntry(MissionEntry missionEntry)
        throws SystemException {
        missionEntryPersistence.remove(missionEntry);
    }

    public List<Object> dynamicQuery(DynamicQuery dynamicQuery)
        throws SystemException {
        return missionEntryPersistence.findWithDynamicQuery(dynamicQuery);
    }

    public List<Object> dynamicQuery(DynamicQuery dynamicQuery, int start,
        int end) throws SystemException {
        return missionEntryPersistence.findWithDynamicQuery(dynamicQuery,
            start, end);
    }

    public MissionEntry getMissionEntry(long missionEntryId)
        throws PortalException, SystemException {
        return missionEntryPersistence.findByPrimaryKey(missionEntryId);
    }

    public List<MissionEntry> getMissionEntries(int start, int end)
        throws SystemException {
        return missionEntryPersistence.findAll(start, end);
    }

    public int getMissionEntriesCount() throws SystemException {
        return missionEntryPersistence.countAll();
    }

    public MissionEntry updateMissionEntry(MissionEntry missionEntry)
        throws SystemException {
        missionEntry.setNew(false);

        return missionEntryPersistence.update(missionEntry, true);
    }

    public MissionEntry updateMissionEntry(MissionEntry missionEntry,
        boolean merge) throws SystemException {
        missionEntry.setNew(false);

        return missionEntryPersistence.update(missionEntry, merge);
    }

    public MissionEntryLocalService getMissionEntryLocalService() {
        return missionEntryLocalService;
    }

    public void setMissionEntryLocalService(
        MissionEntryLocalService missionEntryLocalService) {
        this.missionEntryLocalService = missionEntryLocalService;
    }

    public MissionEntryService getMissionEntryService() {
        return missionEntryService;
    }

    public void setMissionEntryService(MissionEntryService missionEntryService) {
        this.missionEntryService = missionEntryService;
    }

    public MissionEntryPersistence getMissionEntryPersistence() {
        return missionEntryPersistence;
    }

    public void setMissionEntryPersistence(
        MissionEntryPersistence missionEntryPersistence) {
        this.missionEntryPersistence = missionEntryPersistence;
    }

    protected void runSQL(String sql) throws SystemException {
        try {
            PortalUtil.runSQL(sql);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }
}
