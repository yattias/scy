package com.ext.portlet.missionhandling.service;


/**
 * <a href="MissionEntryLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * <code>com.ext.portlet.missionhandling.service.MissionEntryLocalService</code>
 * bean. The static methods of this class calls the same methods of the bean
 * instance. It's convenient to be able to just write one line to call a method
 * on a bean instead of writing a lookup call and a method call.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.ext.portlet.missionhandling.service.MissionEntryLocalService
 *
 */
public class MissionEntryLocalServiceUtil {
    private static MissionEntryLocalService _service;

    public static com.ext.portlet.missionhandling.model.MissionEntry addMissionEntry(
        com.ext.portlet.missionhandling.model.MissionEntry missionEntry)
        throws com.liferay.portal.SystemException {
        return getService().addMissionEntry(missionEntry);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry createMissionEntry(
        long missionEntryId) {
        return getService().createMissionEntry(missionEntryId);
    }

    public static void deleteMissionEntry(long missionEntryId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        getService().deleteMissionEntry(missionEntryId);
    }

    public static void deleteMissionEntry(
        com.ext.portlet.missionhandling.model.MissionEntry missionEntry)
        throws com.liferay.portal.SystemException {
        getService().deleteMissionEntry(missionEntry);
    }

    public static java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException {
        return getService().dynamicQuery(dynamicQuery);
    }

    public static java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException {
        return getService().dynamicQuery(dynamicQuery, start, end);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry getMissionEntry(
        long missionEntryId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        return getService().getMissionEntry(missionEntryId);
    }

    public static java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> getMissionEntries(
        int start, int end) throws com.liferay.portal.SystemException {
        return getService().getMissionEntries(start, end);
    }
    
    public static java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> getMissionEntries(long groupId) throws com.liferay.portal.SystemException {
    	return getService().getMissionEntries(groupId);
    }

    public static int getMissionEntriesCount()
        throws com.liferay.portal.SystemException {
        return getService().getMissionEntriesCount();
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry updateMissionEntry(
        com.ext.portlet.missionhandling.model.MissionEntry missionEntry)
        throws com.liferay.portal.SystemException {
        return getService().updateMissionEntry(missionEntry);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry updateMissionEntry(
        com.ext.portlet.missionhandling.model.MissionEntry missionEntry,
        boolean merge) throws com.liferay.portal.SystemException {
        return getService().updateMissionEntry(missionEntry, merge);
    }

    public static MissionEntryLocalService getService() {
        if (_service == null) {
            throw new RuntimeException("MissionEntryLocalService is not set");
        }

        return _service;
    }

    public void setService(MissionEntryLocalService service) {
        _service = service;
    }
}
