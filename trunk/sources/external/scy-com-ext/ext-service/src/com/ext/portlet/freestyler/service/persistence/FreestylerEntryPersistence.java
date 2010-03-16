package com.ext.portlet.freestyler.service.persistence;

import com.liferay.portal.service.persistence.BasePersistence;


public interface FreestylerEntryPersistence extends BasePersistence {
    public void cacheResult(
        com.ext.portlet.freestyler.model.FreestylerEntry freestylerEntry);

    public void cacheResult(
        java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> freestylerEntries);

    public void clearCache();

    public com.ext.portlet.freestyler.model.FreestylerEntry create(
        long freestylerId);

    public com.ext.portlet.freestyler.model.FreestylerEntry remove(
        long freestylerId)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerEntry remove(
        com.ext.portlet.freestyler.model.FreestylerEntry freestylerEntry)
        throws com.liferay.portal.SystemException;

    /**
     * @deprecated Use <code>update(FreestylerEntry freestylerEntry, boolean merge)</code>.
     */
    public com.ext.portlet.freestyler.model.FreestylerEntry update(
        com.ext.portlet.freestyler.model.FreestylerEntry freestylerEntry)
        throws com.liferay.portal.SystemException;

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param                freestylerEntry the entity to add, update, or merge
     * @param                merge boolean value for whether to merge the entity. The
     *                                default value is false. Setting merge to true is more
     *                                expensive and should only be true when freestylerEntry is
     *                                transient. See LEP-5473 for a detailed discussion of this
     *                                method.
     * @return                true if the portlet can be displayed via Ajax
     */
    public com.ext.portlet.freestyler.model.FreestylerEntry update(
        com.ext.portlet.freestyler.model.FreestylerEntry freestylerEntry,
        boolean merge) throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerEntry updateImpl(
        com.ext.portlet.freestyler.model.FreestylerEntry freestylerEntry,
        boolean merge) throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerEntry findByPrimaryKey(
        long freestylerId)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerEntry fetchByPrimaryKey(
        long freestylerId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findByCompanyId(
        long companyId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findByCompanyId(
        long companyId, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findByCompanyId(
        long companyId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerEntry findByCompanyId_First(
        long companyId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerEntry findByCompanyId_Last(
        long companyId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerEntry[] findByCompanyId_PrevAndNext(
        long freestylerId, long companyId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findByUserId(
        long userId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findByUserId(
        long userId, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findByUserId(
        long userId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerEntry findByUserId_First(
        long userId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerEntry findByUserId_Last(
        long userId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerEntry[] findByUserId_PrevAndNext(
        long freestylerId, long userId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerEntry findByFreestylerId(
        long freestylerId)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerEntry fetchByFreestylerId(
        long freestylerId) throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerEntry fetchByFreestylerId(
        long freestylerId, boolean retrieveFromCache)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findByGroupId(
        long groupId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findByGroupId(
        long groupId, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findByGroupId(
        long groupId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerEntry findByGroupId_First(
        long groupId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerEntry findByGroupId_Last(
        long groupId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerEntry[] findByGroupId_PrevAndNext(
        long freestylerId, long groupId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findAll()
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findAll(
        int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public void removeByCompanyId(long companyId)
        throws com.liferay.portal.SystemException;

    public void removeByUserId(long userId)
        throws com.liferay.portal.SystemException;

    public void removeByFreestylerId(long freestylerId)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public void removeByGroupId(long groupId)
        throws com.liferay.portal.SystemException;

    public void removeAll() throws com.liferay.portal.SystemException;

    public int countByCompanyId(long companyId)
        throws com.liferay.portal.SystemException;

    public int countByUserId(long userId)
        throws com.liferay.portal.SystemException;

    public int countByFreestylerId(long freestylerId)
        throws com.liferay.portal.SystemException;

    public int countByGroupId(long groupId)
        throws com.liferay.portal.SystemException;

    public int countAll() throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> getFreestylerImages(
        long pk) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> getFreestylerImages(
        long pk, int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> getFreestylerImages(
        long pk, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public int getFreestylerImagesSize(long pk)
        throws com.liferay.portal.SystemException;

    public boolean containsFreestylerImage(long pk, long freestylerImagePK)
        throws com.liferay.portal.SystemException;

    public boolean containsFreestylerImages(long pk)
        throws com.liferay.portal.SystemException;

    public void addFreestylerImage(long pk, long freestylerImagePK)
        throws com.liferay.portal.SystemException;

    public void addFreestylerImage(long pk,
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage)
        throws com.liferay.portal.SystemException;

    public void addFreestylerImages(long pk, long[] freestylerImagePKs)
        throws com.liferay.portal.SystemException;

    public void addFreestylerImages(long pk,
        java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> freestylerImages)
        throws com.liferay.portal.SystemException;

    public void clearFreestylerImages(long pk)
        throws com.liferay.portal.SystemException;

    public void removeFreestylerImage(long pk, long freestylerImagePK)
        throws com.liferay.portal.SystemException;

    public void removeFreestylerImage(long pk,
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage)
        throws com.liferay.portal.SystemException;

    public void removeFreestylerImages(long pk, long[] freestylerImagePKs)
        throws com.liferay.portal.SystemException;

    public void removeFreestylerImages(long pk,
        java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> freestylerImages)
        throws com.liferay.portal.SystemException;

    public void setFreestylerImages(long pk, long[] freestylerImagePKs)
        throws com.liferay.portal.SystemException;

    public void setFreestylerImages(long pk,
        java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> freestylerImages)
        throws com.liferay.portal.SystemException;
}
