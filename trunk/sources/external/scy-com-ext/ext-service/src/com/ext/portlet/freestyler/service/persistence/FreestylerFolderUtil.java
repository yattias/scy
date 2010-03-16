package com.ext.portlet.freestyler.service.persistence;

public class FreestylerFolderUtil {
    private static FreestylerFolderPersistence _persistence;

    public static void cacheResult(
        com.ext.portlet.freestyler.model.FreestylerFolder freestylerFolder) {
        getPersistence().cacheResult(freestylerFolder);
    }

    public static void cacheResult(
        java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> freestylerFolders) {
        getPersistence().cacheResult(freestylerFolders);
    }

    public static void clearCache() {
        getPersistence().clearCache();
    }

    public static com.ext.portlet.freestyler.model.FreestylerFolder create(
        long folderId) {
        return getPersistence().create(folderId);
    }

    public static com.ext.portlet.freestyler.model.FreestylerFolder remove(
        long folderId)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException {
        return getPersistence().remove(folderId);
    }

    public static com.ext.portlet.freestyler.model.FreestylerFolder remove(
        com.ext.portlet.freestyler.model.FreestylerFolder freestylerFolder)
        throws com.liferay.portal.SystemException {
        return getPersistence().remove(freestylerFolder);
    }

    /**
     * @deprecated Use <code>update(FreestylerFolder freestylerFolder, boolean merge)</code>.
     */
    public static com.ext.portlet.freestyler.model.FreestylerFolder update(
        com.ext.portlet.freestyler.model.FreestylerFolder freestylerFolder)
        throws com.liferay.portal.SystemException {
        return getPersistence().update(freestylerFolder);
    }

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param                freestylerFolder the entity to add, update, or merge
     * @param                merge boolean value for whether to merge the entity. The
     *                                default value is false. Setting merge to true is more
     *                                expensive and should only be true when freestylerFolder is
     *                                transient. See LEP-5473 for a detailed discussion of this
     *                                method.
     * @return                true if the portlet can be displayed via Ajax
     */
    public static com.ext.portlet.freestyler.model.FreestylerFolder update(
        com.ext.portlet.freestyler.model.FreestylerFolder freestylerFolder,
        boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().update(freestylerFolder, merge);
    }

    public static com.ext.portlet.freestyler.model.FreestylerFolder updateImpl(
        com.ext.portlet.freestyler.model.FreestylerFolder freestylerFolder,
        boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().updateImpl(freestylerFolder, merge);
    }

    public static com.ext.portlet.freestyler.model.FreestylerFolder findByPrimaryKey(
        long folderId)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException {
        return getPersistence().findByPrimaryKey(folderId);
    }

    public static com.ext.portlet.freestyler.model.FreestylerFolder fetchByPrimaryKey(
        long folderId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByPrimaryKey(folderId);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findByGroupId(
        long groupId) throws com.liferay.portal.SystemException {
        return getPersistence().findByGroupId(groupId);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findByGroupId(
        long groupId, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByGroupId(groupId, start, end);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findByGroupId(
        long groupId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByGroupId(groupId, start, end, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerFolder findByGroupId_First(
        long groupId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException {
        return getPersistence().findByGroupId_First(groupId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerFolder findByGroupId_Last(
        long groupId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException {
        return getPersistence().findByGroupId_Last(groupId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerFolder[] findByGroupId_PrevAndNext(
        long folderId, long groupId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException {
        return getPersistence().findByGroupId_PrevAndNext(folderId, groupId, obc);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findByCompanyId(
        long companyId) throws com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId(companyId);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findByCompanyId(
        long companyId, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId(companyId, start, end);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findByCompanyId(
        long companyId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId(companyId, start, end, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerFolder findByCompanyId_First(
        long companyId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId_First(companyId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerFolder findByCompanyId_Last(
        long companyId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId_Last(companyId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerFolder[] findByCompanyId_PrevAndNext(
        long folderId, long companyId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByCompanyId_PrevAndNext(folderId, companyId, obc);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findByG_P(
        long groupId, long parentFolderId)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByG_P(groupId, parentFolderId);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findByG_P(
        long groupId, long parentFolderId, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByG_P(groupId, parentFolderId, start, end);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findByG_P(
        long groupId, long parentFolderId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence()
                   .findByG_P(groupId, parentFolderId, start, end, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerFolder findByG_P_First(
        long groupId, long parentFolderId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException {
        return getPersistence().findByG_P_First(groupId, parentFolderId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerFolder findByG_P_Last(
        long groupId, long parentFolderId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException {
        return getPersistence().findByG_P_Last(groupId, parentFolderId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerFolder[] findByG_P_PrevAndNext(
        long folderId, long groupId, long parentFolderId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByG_P_PrevAndNext(folderId, groupId, parentFolderId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerFolder findByG_P_N(
        long groupId, long parentFolderId, java.lang.String name)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException {
        return getPersistence().findByG_P_N(groupId, parentFolderId, name);
    }

    public static com.ext.portlet.freestyler.model.FreestylerFolder fetchByG_P_N(
        long groupId, long parentFolderId, java.lang.String name)
        throws com.liferay.portal.SystemException {
        return getPersistence().fetchByG_P_N(groupId, parentFolderId, name);
    }

    public static com.ext.portlet.freestyler.model.FreestylerFolder fetchByG_P_N(
        long groupId, long parentFolderId, java.lang.String name,
        boolean retrieveFromCache) throws com.liferay.portal.SystemException {
        return getPersistence()
                   .fetchByG_P_N(groupId, parentFolderId, name,
            retrieveFromCache);
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

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findAll()
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll();
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findAll(
        int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end, obc);
    }

    public static void removeByGroupId(long groupId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByGroupId(groupId);
    }

    public static void removeByCompanyId(long companyId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByCompanyId(companyId);
    }

    public static void removeByG_P(long groupId, long parentFolderId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByG_P(groupId, parentFolderId);
    }

    public static void removeByG_P_N(long groupId, long parentFolderId,
        java.lang.String name)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException {
        getPersistence().removeByG_P_N(groupId, parentFolderId, name);
    }

    public static void removeAll() throws com.liferay.portal.SystemException {
        getPersistence().removeAll();
    }

    public static int countByGroupId(long groupId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByGroupId(groupId);
    }

    public static int countByCompanyId(long companyId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByCompanyId(companyId);
    }

    public static int countByG_P(long groupId, long parentFolderId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByG_P(groupId, parentFolderId);
    }

    public static int countByG_P_N(long groupId, long parentFolderId,
        java.lang.String name) throws com.liferay.portal.SystemException {
        return getPersistence().countByG_P_N(groupId, parentFolderId, name);
    }

    public static int countAll() throws com.liferay.portal.SystemException {
        return getPersistence().countAll();
    }

    public static FreestylerFolderPersistence getPersistence() {
        return _persistence;
    }

    public void setPersistence(FreestylerFolderPersistence persistence) {
        _persistence = persistence;
    }
}
