package com.ext.portlet.missionhandling.service.persistence;

public class MissionEntryUtil {
    private static MissionEntryPersistence _persistence;

    public static void cacheResult(
        com.ext.portlet.missionhandling.model.MissionEntry missionEntry) {
        getPersistence().cacheResult(missionEntry);
    }

    public static void cacheResult(
        java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> missionEntries) {
        getPersistence().cacheResult(missionEntries);
    }

    public static void clearCache() {
        getPersistence().clearCache();
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry create(
        long missionEntryId) {
        return getPersistence().create(missionEntryId);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry remove(
        long missionEntryId)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().remove(missionEntryId);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry remove(
        com.ext.portlet.missionhandling.model.MissionEntry missionEntry)
        throws com.liferay.portal.SystemException {
        return getPersistence().remove(missionEntry);
    }

    /**
     * @deprecated Use <code>update(MissionEntry missionEntry, boolean merge)</code>.
     */
    public static com.ext.portlet.missionhandling.model.MissionEntry update(
        com.ext.portlet.missionhandling.model.MissionEntry missionEntry)
        throws com.liferay.portal.SystemException {
        return getPersistence().update(missionEntry);
    }

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param                missionEntry the entity to add, update, or merge
     * @param                merge boolean value for whether to merge the entity. The
     *                                default value is false. Setting merge to true is more
     *                                expensive and should only be true when missionEntry is
     *                                transient. See LEP-5473 for a detailed discussion of this
     *                                method.
     * @return                true if the portlet can be displayed via Ajax
     */
    public static com.ext.portlet.missionhandling.model.MissionEntry update(
        com.ext.portlet.missionhandling.model.MissionEntry missionEntry,
        boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().update(missionEntry, merge);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry updateImpl(
        com.ext.portlet.missionhandling.model.MissionEntry missionEntry,
        boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().updateImpl(missionEntry, merge);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry findByPrimaryKey(
        long missionEntryId)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByPrimaryKey(missionEntryId);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry fetchByPrimaryKey(
        long missionEntryId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByPrimaryKey(missionEntryId);
    }

    public static java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByCreateDate(
        java.util.Date createDate) throws com.liferay.portal.SystemException {
        return getPersistence().findByCreateDate(createDate);
    }

    public static java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByCreateDate(
        java.util.Date createDate, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByCreateDate(createDate, start, end);
    }

    public static java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByCreateDate(
        java.util.Date createDate, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByCreateDate(createDate, start, end, obc);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry findByCreateDate_First(
        java.util.Date createDate,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByCreateDate_First(createDate, obc);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry findByCreateDate_Last(
        java.util.Date createDate,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByCreateDate_Last(createDate, obc);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry[] findByCreateDate_PrevAndNext(
        long missionEntryId, java.util.Date createDate,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByCreateDate_PrevAndNext(missionEntryId, createDate, obc);
    }

    public static java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByOrganizationId(
        long organizationId) throws com.liferay.portal.SystemException {
        return getPersistence().findByOrganizationId(organizationId);
    }

    public static java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByOrganizationId(
        long organizationId, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByOrganizationId(organizationId, start, end);
    }

    public static java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByOrganizationId(
        long organizationId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence()
                   .findByOrganizationId(organizationId, start, end, obc);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry findByOrganizationId_First(
        long organizationId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByOrganizationId_First(organizationId, obc);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry findByOrganizationId_Last(
        long organizationId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByOrganizationId_Last(organizationId, obc);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry[] findByOrganizationId_PrevAndNext(
        long missionEntryId, long organizationId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByOrganizationId_PrevAndNext(missionEntryId,
            organizationId, obc);
    }

    public static java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByGroupId(
        long groupId) throws com.liferay.portal.SystemException {
        return getPersistence().findByGroupId(groupId);
    }

    public static java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByGroupId(
        long groupId, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByGroupId(groupId, start, end);
    }

    public static java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByGroupId(
        long groupId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByGroupId(groupId, start, end, obc);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry findByGroupId_First(
        long groupId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByGroupId_First(groupId, obc);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry findByGroupId_Last(
        long groupId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByGroupId_Last(groupId, obc);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry[] findByGroupId_PrevAndNext(
        long missionEntryId, long groupId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByGroupId_PrevAndNext(missionEntryId, groupId, obc);
    }

    public static java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByCompanyId(
        long companyId) throws com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId(companyId);
    }

    public static java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByCompanyId(
        long companyId, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId(companyId, start, end);
    }

    public static java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByCompanyId(
        long companyId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId(companyId, start, end, obc);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry findByCompanyId_First(
        long companyId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId_First(companyId, obc);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry findByCompanyId_Last(
        long companyId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId_Last(companyId, obc);
    }

    public static com.ext.portlet.missionhandling.model.MissionEntry[] findByCompanyId_PrevAndNext(
        long missionEntryId, long companyId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByCompanyId_PrevAndNext(missionEntryId, companyId, obc);
    }

    public static java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException {
        return getPersistence().findWithDynamicQuery(dynamicQuery);
    }

    public static java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException {
        return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
    }

    public static java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findAll()
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll();
    }

    public static java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findAll(
        int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end);
    }

    public static java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end, obc);
    }

    public static void removeByCreateDate(java.util.Date createDate)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByCreateDate(createDate);
    }

    public static void removeByOrganizationId(long organizationId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByOrganizationId(organizationId);
    }

    public static void removeByGroupId(long groupId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByGroupId(groupId);
    }

    public static void removeByCompanyId(long companyId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByCompanyId(companyId);
    }

    public static void removeAll() throws com.liferay.portal.SystemException {
        getPersistence().removeAll();
    }

    public static int countByCreateDate(java.util.Date createDate)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByCreateDate(createDate);
    }

    public static int countByOrganizationId(long organizationId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByOrganizationId(organizationId);
    }

    public static int countByGroupId(long groupId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByGroupId(groupId);
    }

    public static int countByCompanyId(long companyId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByCompanyId(companyId);
    }

    public static int countAll() throws com.liferay.portal.SystemException {
        return getPersistence().countAll();
    }

    public static MissionEntryPersistence getPersistence() {
        return _persistence;
    }

    public void setPersistence(MissionEntryPersistence persistence) {
        _persistence = persistence;
    }
}
