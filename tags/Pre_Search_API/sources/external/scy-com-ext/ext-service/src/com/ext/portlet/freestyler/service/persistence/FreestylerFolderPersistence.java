package com.ext.portlet.freestyler.service.persistence;

import com.liferay.portal.service.persistence.BasePersistence;


public interface FreestylerFolderPersistence extends BasePersistence {
    public void cacheResult(
        com.ext.portlet.freestyler.model.FreestylerFolder freestylerFolder);

    public void cacheResult(
        java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> freestylerFolders);

    public void clearCache();

    public com.ext.portlet.freestyler.model.FreestylerFolder create(
        long folderId);

    public com.ext.portlet.freestyler.model.FreestylerFolder remove(
        long folderId)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerFolder remove(
        com.ext.portlet.freestyler.model.FreestylerFolder freestylerFolder)
        throws com.liferay.portal.SystemException;

    /**
     * @deprecated Use <code>update(FreestylerFolder freestylerFolder, boolean merge)</code>.
     */
    public com.ext.portlet.freestyler.model.FreestylerFolder update(
        com.ext.portlet.freestyler.model.FreestylerFolder freestylerFolder)
        throws com.liferay.portal.SystemException;

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
    public com.ext.portlet.freestyler.model.FreestylerFolder update(
        com.ext.portlet.freestyler.model.FreestylerFolder freestylerFolder,
        boolean merge) throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerFolder updateImpl(
        com.ext.portlet.freestyler.model.FreestylerFolder freestylerFolder,
        boolean merge) throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerFolder findByPrimaryKey(
        long folderId)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerFolder fetchByPrimaryKey(
        long folderId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findByGroupId(
        long groupId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findByGroupId(
        long groupId, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findByGroupId(
        long groupId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerFolder findByGroupId_First(
        long groupId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerFolder findByGroupId_Last(
        long groupId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerFolder[] findByGroupId_PrevAndNext(
        long folderId, long groupId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findByCompanyId(
        long companyId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findByCompanyId(
        long companyId, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findByCompanyId(
        long companyId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerFolder findByCompanyId_First(
        long companyId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerFolder findByCompanyId_Last(
        long companyId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerFolder[] findByCompanyId_PrevAndNext(
        long folderId, long companyId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findByG_P(
        long groupId, long parentFolderId)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findByG_P(
        long groupId, long parentFolderId, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findByG_P(
        long groupId, long parentFolderId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerFolder findByG_P_First(
        long groupId, long parentFolderId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerFolder findByG_P_Last(
        long groupId, long parentFolderId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerFolder[] findByG_P_PrevAndNext(
        long folderId, long groupId, long parentFolderId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerFolder findByG_P_N(
        long groupId, long parentFolderId, java.lang.String name)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerFolder fetchByG_P_N(
        long groupId, long parentFolderId, java.lang.String name)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.freestyler.model.FreestylerFolder fetchByG_P_N(
        long groupId, long parentFolderId, java.lang.String name,
        boolean retrieveFromCache) throws com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findAll()
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findAll(
        int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.freestyler.model.FreestylerFolder> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public void removeByGroupId(long groupId)
        throws com.liferay.portal.SystemException;

    public void removeByCompanyId(long companyId)
        throws com.liferay.portal.SystemException;

    public void removeByG_P(long groupId, long parentFolderId)
        throws com.liferay.portal.SystemException;

    public void removeByG_P_N(long groupId, long parentFolderId,
        java.lang.String name)
        throws com.ext.portlet.freestyler.NoSuchFolderException,
            com.liferay.portal.SystemException;

    public void removeAll() throws com.liferay.portal.SystemException;

    public int countByGroupId(long groupId)
        throws com.liferay.portal.SystemException;

    public int countByCompanyId(long companyId)
        throws com.liferay.portal.SystemException;

    public int countByG_P(long groupId, long parentFolderId)
        throws com.liferay.portal.SystemException;

    public int countByG_P_N(long groupId, long parentFolderId,
        java.lang.String name) throws com.liferay.portal.SystemException;

    public int countAll() throws com.liferay.portal.SystemException;
}
