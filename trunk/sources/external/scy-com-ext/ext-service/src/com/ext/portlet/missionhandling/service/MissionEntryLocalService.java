package com.ext.portlet.missionhandling.service;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.Isolation;
import com.liferay.portal.kernel.annotation.Propagation;
import com.liferay.portal.kernel.annotation.Transactional;


/**
 * <a href="MissionEntryLocalService.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface defines the service. The default implementation is
 * <code>com.ext.portlet.missionhandling.service.impl.MissionEntryLocalServiceImpl</code>.
 * Modify methods in that class and rerun ServiceBuilder to populate this class
 * and all other generated classes.
 * </p>
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.missionhandling.service.MissionEntryLocalServiceUtil
 *
 */
@Transactional(isolation = Isolation.PORTAL, rollbackFor =  {
    PortalException.class, SystemException.class}
)
public interface MissionEntryLocalService {
    public com.ext.portlet.missionhandling.model.MissionEntry addMissionEntry(
        com.ext.portlet.missionhandling.model.MissionEntry missionEntry)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.missionhandling.model.MissionEntry createMissionEntry(
        long missionEntryId);

    public void deleteMissionEntry(long missionEntryId)
        throws com.liferay.portal.SystemException,
            com.liferay.portal.PortalException;

    public void deleteMissionEntry(
        com.ext.portlet.missionhandling.model.MissionEntry missionEntry)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public com.ext.portlet.missionhandling.model.MissionEntry getMissionEntry(
        long missionEntryId)
        throws com.liferay.portal.SystemException,
            com.liferay.portal.PortalException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> getMissionEntries(
        int start, int end) throws com.liferay.portal.SystemException;
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> getMissionEntries(long groupId) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int getMissionEntriesCount()
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.missionhandling.model.MissionEntry updateMissionEntry(
        com.ext.portlet.missionhandling.model.MissionEntry missionEntry)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.missionhandling.model.MissionEntry updateMissionEntry(
        com.ext.portlet.missionhandling.model.MissionEntry missionEntry,
        boolean merge) throws com.liferay.portal.SystemException;
}
