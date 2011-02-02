package com.ext.portlet.freestyler.service.persistence;

import com.liferay.portal.service.persistence.BasePersistence;


public interface FreestylerImagePersistence extends BasePersistence {
    public void cacheResult(
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage);

    public void cacheResult(
        java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> freestylerImages);

    public void clearCache();

    public com.ext.portlet.freestyler.model.FreestylerImage create(long imageId);

    public com.ext.portlet.freestyler.model.FreestylerImage remove(long imageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage remove(
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage)
        throws com.liferay.portal.SystemException;

    /**
     * @deprecated Use <code>update(FreestylerImage freestylerImage, boolean merge)</code>.
     */
    public com.ext.portlet.freestyler.model.FreestylerImage update(
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage)
        throws com.liferay.portal.SystemException;

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
    public com.ext.portlet.freestyler.model.FreestylerImage update(
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage,
        boolean merge) throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage updateImpl(
        com.ext.portlet.freestyler.model.FreestylerImage freestylerImage,
        boolean merge) throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage findByPrimaryKey(
        long imageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage fetchByPrimaryKey(
        long imageId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByGroupId(
        long groupId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByGroupId(
        long groupId, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByGroupId(
        long groupId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage findByGroupId_First(
        long groupId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage findByGroupId_Last(
        long groupId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage[] findByGroupId_PrevAndNext(
        long imageId, long groupId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByFolderId(
        long folderId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByFolderId(
        long folderId, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByFolderId(
        long folderId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage findByFolderId_First(
        long folderId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage findByFolderId_Last(
        long folderId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage[] findByFolderId_PrevAndNext(
        long imageId, long folderId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByFreestylerId(
        long freestylerId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByFreestylerId(
        long freestylerId, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByFreestylerId(
        long freestylerId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage findByFreestylerId_First(
        long freestylerId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage findByFreestylerId_Last(
        long freestylerId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage[] findByFreestylerId_PrevAndNext(
        long imageId, long freestylerId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage findBySmallImageId(
        long smallImageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage fetchBySmallImageId(
        long smallImageId) throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage fetchBySmallImageId(
        long smallImageId, boolean retrieveFromCache)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage findByLargeImageId(
        long largeImageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage fetchByLargeImageId(
        long largeImageId) throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage fetchByLargeImageId(
        long largeImageId, boolean retrieveFromCache)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage findByCustom1ImageId(
        long custom1ImageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage fetchByCustom1ImageId(
        long custom1ImageId) throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage fetchByCustom1ImageId(
        long custom1ImageId, boolean retrieveFromCache)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage findByCustom2ImageId(
        long custom2ImageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage fetchByCustom2ImageId(
        long custom2ImageId) throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage fetchByCustom2ImageId(
        long custom2ImageId, boolean retrieveFromCache)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByG_U(
        long groupId, long userId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByG_U(
        long groupId, long userId, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByG_U(
        long groupId, long userId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage findByG_U_First(
        long groupId, long userId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage findByG_U_Last(
        long groupId, long userId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage[] findByG_U_PrevAndNext(
        long imageId, long groupId, long userId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByF_N(
        long folderId, java.lang.String name)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByF_N(
        long folderId, java.lang.String name, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByF_N(
        long folderId, java.lang.String name, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage findByF_N_First(
        long folderId, java.lang.String name,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage findByF_N_Last(
        long folderId, java.lang.String name,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage[] findByF_N_PrevAndNext(
        long imageId, long folderId, java.lang.String name,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByF_F(
        long folderId, long freestylerId)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByF_F(
        long folderId, long freestylerId, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findByF_F(
        long folderId, long freestylerId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage findByF_F_First(
        long folderId, long freestylerId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage findByF_F_Last(
        long folderId, long freestylerId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerImage[] findByF_F_PrevAndNext(
        long imageId, long folderId, long freestylerId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findAll()
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findAll(
        int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerImage> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public void removeByGroupId(long groupId)
        throws com.liferay.portal.SystemException;

    public void removeByFolderId(long folderId)
        throws com.liferay.portal.SystemException;

    public void removeByFreestylerId(long freestylerId)
        throws com.liferay.portal.SystemException;

    public void removeBySmallImageId(long smallImageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public void removeByLargeImageId(long largeImageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public void removeByCustom1ImageId(long custom1ImageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public void removeByCustom2ImageId(long custom2ImageId)
        throws com.ext.portlet.freestyler.NoSuchImageException,
            com.liferay.portal.SystemException;

    public void removeByG_U(long groupId, long userId)
        throws com.liferay.portal.SystemException;

    public void removeByF_N(long folderId, java.lang.String name)
        throws com.liferay.portal.SystemException;

    public void removeByF_F(long folderId, long freestylerId)
        throws com.liferay.portal.SystemException;

    public void removeAll() throws com.liferay.portal.SystemException;

    public int countByGroupId(long groupId)
        throws com.liferay.portal.SystemException;

    public int countByFolderId(long folderId)
        throws com.liferay.portal.SystemException;

    public int countByFreestylerId(long freestylerId)
        throws com.liferay.portal.SystemException;

    public int countBySmallImageId(long smallImageId)
        throws com.liferay.portal.SystemException;

    public int countByLargeImageId(long largeImageId)
        throws com.liferay.portal.SystemException;

    public int countByCustom1ImageId(long custom1ImageId)
        throws com.liferay.portal.SystemException;

    public int countByCustom2ImageId(long custom2ImageId)
        throws com.liferay.portal.SystemException;

    public int countByG_U(long groupId, long userId)
        throws com.liferay.portal.SystemException;

    public int countByF_N(long folderId, java.lang.String name)
        throws com.liferay.portal.SystemException;

    public int countByF_F(long folderId, long freestylerId)
        throws com.liferay.portal.SystemException;

    public int countAll() throws com.liferay.portal.SystemException;
}
