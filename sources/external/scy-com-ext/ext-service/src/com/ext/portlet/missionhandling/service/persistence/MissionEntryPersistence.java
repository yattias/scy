package com.ext.portlet.missionhandling.service.persistence;

import com.liferay.portal.service.persistence.BasePersistence;


public interface MissionEntryPersistence extends BasePersistence {
    public void cacheResult(
        com.ext.portlet.missionhandling.model.MissionEntry missionEntry);

    public void cacheResult(
        java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> missionEntries);

    public void clearCache();

    public com.ext.portlet.missionhandling.model.MissionEntry create(
        long missionEntryId);

    public com.ext.portlet.missionhandling.model.MissionEntry remove(
        long missionEntryId)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.missionhandling.model.MissionEntry remove(
        com.ext.portlet.missionhandling.model.MissionEntry missionEntry)
        throws com.liferay.portal.SystemException;

    /**
     * @deprecated Use <code>update(MissionEntry missionEntry, boolean merge)</code>.
     */
    public com.ext.portlet.missionhandling.model.MissionEntry update(
        com.ext.portlet.missionhandling.model.MissionEntry missionEntry)
        throws com.liferay.portal.SystemException;

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
    public com.ext.portlet.missionhandling.model.MissionEntry update(
        com.ext.portlet.missionhandling.model.MissionEntry missionEntry,
        boolean merge) throws com.liferay.portal.SystemException;

    public com.ext.portlet.missionhandling.model.MissionEntry updateImpl(
        com.ext.portlet.missionhandling.model.MissionEntry missionEntry,
        boolean merge) throws com.liferay.portal.SystemException;

    public com.ext.portlet.missionhandling.model.MissionEntry findByPrimaryKey(
        long missionEntryId)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.missionhandling.model.MissionEntry fetchByPrimaryKey(
        long missionEntryId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByCreateDate(
        java.util.Date createDate) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByCreateDate(
        java.util.Date createDate, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByCreateDate(
        java.util.Date createDate, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.missionhandling.model.MissionEntry findByCreateDate_First(
        java.util.Date createDate,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.missionhandling.model.MissionEntry findByCreateDate_Last(
        java.util.Date createDate,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.missionhandling.model.MissionEntry[] findByCreateDate_PrevAndNext(
        long missionEntryId, java.util.Date createDate,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByOrganizationId(
        long organizationId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByOrganizationId(
        long organizationId, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByOrganizationId(
        long organizationId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.missionhandling.model.MissionEntry findByOrganizationId_First(
        long organizationId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.missionhandling.model.MissionEntry findByOrganizationId_Last(
        long organizationId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.missionhandling.model.MissionEntry[] findByOrganizationId_PrevAndNext(
        long missionEntryId, long organizationId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByGroupId(
        long groupId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByGroupId(
        long groupId, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByGroupId(
        long groupId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.missionhandling.model.MissionEntry findByGroupId_First(
        long groupId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.missionhandling.model.MissionEntry findByGroupId_Last(
        long groupId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.missionhandling.model.MissionEntry[] findByGroupId_PrevAndNext(
        long missionEntryId, long groupId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByCompanyId(
        long companyId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByCompanyId(
        long companyId, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findByCompanyId(
        long companyId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.missionhandling.model.MissionEntry findByCompanyId_First(
        long companyId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.missionhandling.model.MissionEntry findByCompanyId_Last(
        long companyId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.missionhandling.model.MissionEntry[] findByCompanyId_PrevAndNext(
        long missionEntryId, long companyId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.missionhandling.NoSuchMissionEntryException,
            com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findAll()
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findAll(
        int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.missionhandling.model.MissionEntry> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public void removeByCreateDate(java.util.Date createDate)
        throws com.liferay.portal.SystemException;

    public void removeByOrganizationId(long organizationId)
        throws com.liferay.portal.SystemException;

    public void removeByGroupId(long groupId)
        throws com.liferay.portal.SystemException;

    public void removeByCompanyId(long companyId)
        throws com.liferay.portal.SystemException;

    public void removeAll() throws com.liferay.portal.SystemException;

    public int countByCreateDate(java.util.Date createDate)
        throws com.liferay.portal.SystemException;

    public int countByOrganizationId(long organizationId)
        throws com.liferay.portal.SystemException;

    public int countByGroupId(long groupId)
        throws com.liferay.portal.SystemException;

    public int countByCompanyId(long companyId)
        throws com.liferay.portal.SystemException;

    public int countAll() throws com.liferay.portal.SystemException;
}
