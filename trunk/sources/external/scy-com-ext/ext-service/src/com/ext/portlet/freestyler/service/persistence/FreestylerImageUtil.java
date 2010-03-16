package com.ext.portlet.freestyler.service.persistence;

public class FreestylerImageUtil {
    private static FreestylerImagePersistence _persistence;

    public static void cacheResult(
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage) {
        getPersistence().cacheResult(freestylerImage);
    }

    public static void cacheResult(
        java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> freestylerImages) {
        getPersistence().cacheResult(freestylerImages);
    }

    public static void clearCache() {
        getPersistence().clearCache();
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage create(
        long imageId) {
        return getPersistence().create(imageId);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage remove(
        long imageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence().remove(imageId);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage remove(
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage)
        throws com.liferay.portal.SystemException {
        return getPersistence().remove(freestylerImage);
    }

    /**
     * @deprecated Use <code>update(FreestylerImage freestylerImage, boolean merge)</code>.
     */
    public static com.ext.portlet.freestyler.model.FreestylerImage update(
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage)
        throws com.liferay.portal.SystemException {
        return getPersistence().update(freestylerImage);
    }

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param                freestylerImage the entity to add, update, or merge
     * @param                merge boolean value for whether to merge the entity. The
     *                                default value is false. Setting merge to true is more
     *                                expensive and should only be true when freestylerImage is
     *                                transient. See LEP-5473 for a detailed discussion of this
     *                                method.
     * @return                true if the portlet can be displayed via Ajax
     */
    public static com.ext.portlet.freestyler.model.FreestylerImage update(
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage,
        boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().update(freestylerImage, merge);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage updateImpl(
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage,
        boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().updateImpl(freestylerImage, merge);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage findByPrimaryKey(
        long imageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence().findByPrimaryKey(imageId);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage fetchByPrimaryKey(
        long imageId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByPrimaryKey(imageId);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByGroupId(
        long groupId) throws com.liferay.portal.SystemException {
        return getPersistence().findByGroupId(groupId);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByGroupId(
        long groupId, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByGroupId(groupId, start, end);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByGroupId(
        long groupId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByGroupId(groupId, start, end, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage findByGroupId_First(
        long groupId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence().findByGroupId_First(groupId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage findByGroupId_Last(
        long groupId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence().findByGroupId_Last(groupId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage[] findByGroupId_PrevAndNext(
        long imageId, long groupId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence().findByGroupId_PrevAndNext(imageId, groupId, obc);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByFolderId(
        long folderId) throws com.liferay.portal.SystemException {
        return getPersistence().findByFolderId(folderId);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByFolderId(
        long folderId, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByFolderId(folderId, start, end);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByFolderId(
        long folderId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByFolderId(folderId, start, end, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage findByFolderId_First(
        long folderId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence().findByFolderId_First(folderId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage findByFolderId_Last(
        long folderId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence().findByFolderId_Last(folderId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage[] findByFolderId_PrevAndNext(
        long imageId, long folderId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByFolderId_PrevAndNext(imageId, folderId, obc);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByFreestylerId(
        long freestylerId) throws com.liferay.portal.SystemException {
        return getPersistence().findByFreestylerId(freestylerId);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByFreestylerId(
        long freestylerId, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByFreestylerId(freestylerId, start, end);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByFreestylerId(
        long freestylerId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByFreestylerId(freestylerId, start, end, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage findByFreestylerId_First(
        long freestylerId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence().findByFreestylerId_First(freestylerId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage findByFreestylerId_Last(
        long freestylerId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence().findByFreestylerId_Last(freestylerId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage[] findByFreestylerId_PrevAndNext(
        long imageId, long freestylerId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByFreestylerId_PrevAndNext(imageId, freestylerId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage findBySmallImageId(
        long smallImageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence().findBySmallImageId(smallImageId);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage fetchBySmallImageId(
        long smallImageId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchBySmallImageId(smallImageId);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage fetchBySmallImageId(
        long smallImageId, boolean retrieveFromCache)
        throws com.liferay.portal.SystemException {
        return getPersistence()
                   .fetchBySmallImageId(smallImageId, retrieveFromCache);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage findByLargeImageId(
        long largeImageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence().findByLargeImageId(largeImageId);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage fetchByLargeImageId(
        long largeImageId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByLargeImageId(largeImageId);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage fetchByLargeImageId(
        long largeImageId, boolean retrieveFromCache)
        throws com.liferay.portal.SystemException {
        return getPersistence()
                   .fetchByLargeImageId(largeImageId, retrieveFromCache);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage findByCustom1ImageId(
        long custom1ImageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence().findByCustom1ImageId(custom1ImageId);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage fetchByCustom1ImageId(
        long custom1ImageId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByCustom1ImageId(custom1ImageId);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage fetchByCustom1ImageId(
        long custom1ImageId, boolean retrieveFromCache)
        throws com.liferay.portal.SystemException {
        return getPersistence()
                   .fetchByCustom1ImageId(custom1ImageId, retrieveFromCache);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage findByCustom2ImageId(
        long custom2ImageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence().findByCustom2ImageId(custom2ImageId);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage fetchByCustom2ImageId(
        long custom2ImageId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByCustom2ImageId(custom2ImageId);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage fetchByCustom2ImageId(
        long custom2ImageId, boolean retrieveFromCache)
        throws com.liferay.portal.SystemException {
        return getPersistence()
                   .fetchByCustom2ImageId(custom2ImageId, retrieveFromCache);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByG_U(
        long groupId, long userId) throws com.liferay.portal.SystemException {
        return getPersistence().findByG_U(groupId, userId);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByG_U(
        long groupId, long userId, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByG_U(groupId, userId, start, end);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByG_U(
        long groupId, long userId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByG_U(groupId, userId, start, end, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage findByG_U_First(
        long groupId, long userId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence().findByG_U_First(groupId, userId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage findByG_U_Last(
        long groupId, long userId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence().findByG_U_Last(groupId, userId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage[] findByG_U_PrevAndNext(
        long imageId, long groupId, long userId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByG_U_PrevAndNext(imageId, groupId, userId, obc);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByF_N(
        long folderId, java.lang.String name)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByF_N(folderId, name);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByF_N(
        long folderId, java.lang.String name, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByF_N(folderId, name, start, end);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByF_N(
        long folderId, java.lang.String name, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByF_N(folderId, name, start, end, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage findByF_N_First(
        long folderId, java.lang.String name,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence().findByF_N_First(folderId, name, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage findByF_N_Last(
        long folderId, java.lang.String name,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence().findByF_N_Last(folderId, name, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage[] findByF_N_PrevAndNext(
        long imageId, long folderId, java.lang.String name,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByF_N_PrevAndNext(imageId, folderId, name, obc);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByF_F(
        long folderId, long freestylerId)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByF_F(folderId, freestylerId);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByF_F(
        long folderId, long freestylerId, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByF_F(folderId, freestylerId, start, end);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByF_F(
        long folderId, long freestylerId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence()
                   .findByF_F(folderId, freestylerId, start, end, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage findByF_F_First(
        long folderId, long freestylerId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence().findByF_F_First(folderId, freestylerId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage findByF_F_Last(
        long folderId, long freestylerId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence().findByF_F_Last(folderId, freestylerId, obc);
    }

    public static com.ext.portlet.freestyler.model.FreestylerImage[] findByF_F_PrevAndNext(
        long imageId, long folderId, long freestylerId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByF_F_PrevAndNext(imageId, folderId, freestylerId, obc);
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

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findAll()
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll();
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findAll(
        int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end);
    }

    public static java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end, obc);
    }

    public static void removeByGroupId(long groupId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByGroupId(groupId);
    }

    public static void removeByFolderId(long folderId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByFolderId(folderId);
    }

    public static void removeByFreestylerId(long freestylerId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByFreestylerId(freestylerId);
    }

    public static void removeBySmallImageId(long smallImageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        getPersistence().removeBySmallImageId(smallImageId);
    }

    public static void removeByLargeImageId(long largeImageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        getPersistence().removeByLargeImageId(largeImageId);
    }

    public static void removeByCustom1ImageId(long custom1ImageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        getPersistence().removeByCustom1ImageId(custom1ImageId);
    }

    public static void removeByCustom2ImageId(long custom2ImageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException {
        getPersistence().removeByCustom2ImageId(custom2ImageId);
    }

    public static void removeByG_U(long groupId, long userId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByG_U(groupId, userId);
    }

    public static void removeByF_N(long folderId, java.lang.String name)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByF_N(folderId, name);
    }

    public static void removeByF_F(long folderId, long freestylerId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByF_F(folderId, freestylerId);
    }

    public static void removeAll() throws com.liferay.portal.SystemException {
        getPersistence().removeAll();
    }

    public static int countByGroupId(long groupId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByGroupId(groupId);
    }

    public static int countByFolderId(long folderId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByFolderId(folderId);
    }

    public static int countByFreestylerId(long freestylerId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByFreestylerId(freestylerId);
    }

    public static int countBySmallImageId(long smallImageId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countBySmallImageId(smallImageId);
    }

    public static int countByLargeImageId(long largeImageId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByLargeImageId(largeImageId);
    }

    public static int countByCustom1ImageId(long custom1ImageId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByCustom1ImageId(custom1ImageId);
    }

    public static int countByCustom2ImageId(long custom2ImageId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByCustom2ImageId(custom2ImageId);
    }

    public static int countByG_U(long groupId, long userId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByG_U(groupId, userId);
    }

    public static int countByF_N(long folderId, java.lang.String name)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByF_N(folderId, name);
    }

    public static int countByF_F(long folderId, long freestylerId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByF_F(folderId, freestylerId);
    }

    public static int countAll() throws com.liferay.portal.SystemException {
        return getPersistence().countAll();
    }

    public static FreestylerImagePersistence getPersistence() {
        return _persistence;
    }

    public void setPersistence(FreestylerImagePersistence persistence) {
        _persistence = persistence;
    }
}
