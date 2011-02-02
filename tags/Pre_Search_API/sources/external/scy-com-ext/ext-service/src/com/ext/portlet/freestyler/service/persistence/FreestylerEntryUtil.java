package com.ext.portlet.freestyler.service.persistence;

public class FreestylerEntryUtil {
    private static FreestylerEntryPersistence _persistence;

    public static void cacheResult(
        com.ext.portlet.freestyler.model.FreestylerEntry freestylerEntry) {
        getPersistence().cacheResult(freestylerEntry);
    }

    public static void cacheResult(
        java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> freestylerEntries) {
        getPersistence().cacheResult(freestylerEntries);
    }

    public static void clearCache() {
        getPersistence().clearCache();
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry create(
        long freestylerId) {
        return getPersistence().create(freestylerId);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry remove(
        long freestylerId)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().remove(freestylerId);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry remove(
        com.ext.portlet.freestyler.model.FreestylerEntry freestylerEntry)
        throws com.liferay.portal.SystemException {
        return getPersistence().remove(freestylerEntry);
    }

    /**
     * @deprecated Use <code>update(FreestylerEntry freestylerEntry, boolean merge)</code>.
     */
    public static com.ext.portlet.freestyler.model.FreestylerEntry update(
        com.ext.portlet.freestyler.model.FreestylerEntry freestylerEntry)
        throws com.liferay.portal.SystemException {
        return getPersistence().update(freestylerEntry);
    }

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
    public static com.ext.portlet.freestyler.model.FreestylerEntry update(
        com.ext.portlet.freestyler.model.FreestylerEntry freestylerEntry,
        boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().update(freestylerEntry, merge);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry updateImpl(
        com.ext.portlet.freestyler.model.FreestylerEntry freestylerEntry,
        boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().updateImpl(freestylerEntry, merge);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry findByPrimaryKey(
        long freestylerId)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByPrimaryKey(freestylerId);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry fetchByPrimaryKey(
        long freestylerId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByPrimaryKey(freestylerId);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findByCompanyId(
        long companyId) throws com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId(companyId);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findByCompanyId(
        long companyId, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId(companyId, start, end);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findByCompanyId(
        long companyId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId(companyId, start, end, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry findByCompanyId_First(
        long companyId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId_First(companyId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry findByCompanyId_Last(
        long companyId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId_Last(companyId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry[] findByCompanyId_PrevAndNext(
        long freestylerId, long companyId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByCompanyId_PrevAndNext(freestylerId, companyId, obc);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findByUserId(
        long userId) throws com.liferay.portal.SystemException {
        return getPersistence().findByUserId(userId);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findByUserId(
        long userId, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByUserId(userId, start, end);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findByUserId(
        long userId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByUserId(userId, start, end, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry findByUserId_First(
        long userId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByUserId_First(userId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry findByUserId_Last(
        long userId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByUserId_Last(userId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry[] findByUserId_PrevAndNext(
        long freestylerId, long userId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByUserId_PrevAndNext(freestylerId, userId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry findByFreestylerId(
        long freestylerId)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByFreestylerId(freestylerId);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry fetchByFreestylerId(
        long freestylerId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByFreestylerId(freestylerId);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry fetchByFreestylerId(
        long freestylerId, boolean retrieveFromCache)
        throws com.liferay.portal.SystemException {
        return getPersistence()
                   .fetchByFreestylerId(freestylerId, retrieveFromCache);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findByGroupId(
        long groupId) throws com.liferay.portal.SystemException {
        return getPersistence().findByGroupId(groupId);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findByGroupId(
        long groupId, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByGroupId(groupId, start, end);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findByGroupId(
        long groupId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByGroupId(groupId, start, end, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry findByGroupId_First(
        long groupId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByGroupId_First(groupId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry findByGroupId_Last(
        long groupId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByGroupId_Last(groupId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerEntry[] findByGroupId_PrevAndNext(
        long freestylerId, long groupId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByGroupId_PrevAndNext(freestylerId, groupId, obc);
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

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findAll()
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll();
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findAll(
        int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerEntry> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end, obc);
    }

    public static void removeByCompanyId(long companyId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByCompanyId(companyId);
    }

    public static void removeByUserId(long userId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByUserId(userId);
    }

    public static void removeByFreestylerId(long freestylerId)
        throws com.ext.portlet.freestyler.NoSuchEntryException,
            com.liferay.portal.SystemException {
        getPersistence().removeByFreestylerId(freestylerId);
    }

    public static void removeByGroupId(long groupId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByGroupId(groupId);
    }

    public static void removeAll() throws com.liferay.portal.SystemException {
        getPersistence().removeAll();
    }

    public static int countByCompanyId(long companyId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByCompanyId(companyId);
    }

    public static int countByUserId(long userId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByUserId(userId);
    }

    public static int countByFreestylerId(long freestylerId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByFreestylerId(freestylerId);
    }

    public static int countByGroupId(long groupId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByGroupId(groupId);
    }

    public static int countAll() throws com.liferay.portal.SystemException {
        return getPersistence().countAll();
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> getFreestylerImages(
        long pk) throws com.liferay.portal.SystemException {
        return getPersistence().getFreestylerImages(pk);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> getFreestylerImages(
        long pk, int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().getFreestylerImages(pk, start, end);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> getFreestylerImages(
        long pk, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().getFreestylerImages(pk, start, end, obc);
    }

    public static int getFreestylerImagesSize(long pk)
        throws com.liferay.portal.SystemException {
        return getPersistence().getFreestylerImagesSize(pk);
    }

    public static boolean containsFreestylerImage(long pk,
        long freestylerImagePK) throws com.liferay.portal.SystemException {
        return getPersistence().containsFreestylerImage(pk, freestylerImagePK);
    }

    public static boolean containsFreestylerImages(long pk)
        throws com.liferay.portal.SystemException {
        return getPersistence().containsFreestylerImages(pk);
    }

    public static void addFreestylerImage(long pk, long freestylerImagePK)
        throws com.liferay.portal.SystemException {
        getPersistence().addFreestylerImage(pk, freestylerImagePK);
    }

    public static void addFreestylerImage(long pk,
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage)
        throws com.liferay.portal.SystemException {
        getPersistence().addFreestylerImage(pk, freestylerImage);
    }

    public static void addFreestylerImages(long pk, long[] freestylerImagePKs)
        throws com.liferay.portal.SystemException {
        getPersistence().addFreestylerImages(pk, freestylerImagePKs);
    }

    public static void addFreestylerImages(long pk,
        java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> freestylerImages)
        throws com.liferay.portal.SystemException {
        getPersistence().addFreestylerImages(pk, freestylerImages);
    }

    public static void clearFreestylerImages(long pk)
        throws com.liferay.portal.SystemException {
        getPersistence().clearFreestylerImages(pk);
    }

    public static void removeFreestylerImage(long pk, long freestylerImagePK)
        throws com.liferay.portal.SystemException {
        getPersistence().removeFreestylerImage(pk, freestylerImagePK);
    }

    public static void removeFreestylerImage(long pk,
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage)
        throws com.liferay.portal.SystemException {
        getPersistence().removeFreestylerImage(pk, freestylerImage);
    }

    public static void removeFreestylerImages(long pk, long[] freestylerImagePKs)
        throws com.liferay.portal.SystemException {
        getPersistence().removeFreestylerImages(pk, freestylerImagePKs);
    }

    public static void removeFreestylerImages(long pk,
        java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> freestylerImages)
        throws com.liferay.portal.SystemException {
        getPersistence().removeFreestylerImages(pk, freestylerImages);
    }

    public static void setFreestylerImages(long pk, long[] freestylerImagePKs)
        throws com.liferay.portal.SystemException {
        getPersistence().setFreestylerImages(pk, freestylerImagePKs);
    }

    public static void setFreestylerImages(long pk,
        java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> freestylerImages)
        throws com.liferay.portal.SystemException {
        getPersistence().setFreestylerImages(pk, freestylerImages);
    }

    public static FreestylerEntryPersistence getPersistence() {
        return _persistence;
    }

    public void setPersistence(FreestylerEntryPersistence persistence) {
        _persistence = persistence;
    }
}
