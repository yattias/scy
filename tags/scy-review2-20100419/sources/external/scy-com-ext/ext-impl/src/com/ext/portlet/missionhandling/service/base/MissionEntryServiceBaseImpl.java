package com.ext.portlet.missionhandling.service.base;

import com.ext.portlet.missionhandling.service.MissionEntryLocalService;
import com.ext.portlet.missionhandling.service.MissionEntryService;
import com.ext.portlet.missionhandling.service.persistence.MissionEntryPersistence;

import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.BeanReference;
import com.liferay.portal.service.base.PrincipalBean;
import com.liferay.portal.util.PortalUtil;


public abstract class MissionEntryServiceBaseImpl extends PrincipalBean
    implements MissionEntryService {
    @BeanReference(name = "com.ext.portlet.missionhandling.service.MissionEntryLocalService.impl")
    protected MissionEntryLocalService missionEntryLocalService;
    @BeanReference(name = "com.ext.portlet.missionhandling.service.MissionEntryService.impl")
    protected MissionEntryService missionEntryService;
    @BeanReference(name = "com.ext.portlet.missionhandling.service.persistence.MissionEntryPersistence.impl")
    protected MissionEntryPersistence missionEntryPersistence;

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
