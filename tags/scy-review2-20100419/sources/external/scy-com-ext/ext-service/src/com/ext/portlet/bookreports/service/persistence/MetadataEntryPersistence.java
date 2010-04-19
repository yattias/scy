package com.ext.portlet.bookreports.service.persistence;

import com.liferay.portal.service.persistence.BasePersistence;


/**
 * <a href="MetadataEntryPersistence.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       MetadataEntryPersistenceImpl
 * @see       MetadataEntryUtil
 * @generated
 */
public interface MetadataEntryPersistence extends BasePersistence {
    public void cacheResult(
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry);

    public void cacheResult(
        java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> metadataEntries);

    public void clearCache();

    public com.ext.portlet.bookreports.model.MetadataEntry create(
        java.lang.Long entryId);

    public com.ext.portlet.bookreports.model.MetadataEntry remove(
        java.lang.Long entryId)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.MetadataEntry remove(
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry)
        throws com.liferay.portal.SystemException;

    /**
     * @deprecated Use {@link #update(MetadataEntry, boolean merge)}.
     */
    public com.ext.portlet.bookreports.model.MetadataEntry update(
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry)
        throws com.liferay.portal.SystemException;

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param  metadataEntry the entity to add, update, or merge
     * @param  merge boolean value for whether to merge the entity. The default
     *         value is false. Setting merge to true is more expensive and
     *         should only be true when metadataEntry is transient. See
     *         LEP-5473 for a detailed discussion of this method.
     * @return the entity that was added, updated, or merged
     */
    public com.ext.portlet.bookreports.model.MetadataEntry update(
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry,
        boolean merge) throws com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.MetadataEntry updateImpl(
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry,
        boolean merge) throws com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.MetadataEntry findByPrimaryKey(
        java.lang.Long entryId)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.MetadataEntry fetchByPrimaryKey(
        java.lang.Long entryId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> findByUuid(
        java.lang.String uuid) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> findByUuid(
        java.lang.String uuid, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> findByUuid(
        java.lang.String uuid, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.MetadataEntry findByUuid_First(
        java.lang.String uuid,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.MetadataEntry findByUuid_Last(
        java.lang.String uuid,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.MetadataEntry[] findByUuid_PrevAndNext(
        java.lang.Long entryId, java.lang.String uuid,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.MetadataEntry findByUUID_G(
        java.lang.String uuid, java.lang.Long groupId)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.MetadataEntry fetchByUUID_G(
        java.lang.String uuid, java.lang.Long groupId)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.MetadataEntry fetchByUUID_G(
        java.lang.String uuid, java.lang.Long groupId, boolean retrieveFromCache)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> findAll()
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> findAll(
        int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public void removeByUuid(java.lang.String uuid)
        throws com.liferay.portal.SystemException;

    public void removeByUUID_G(java.lang.String uuid, java.lang.Long groupId)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public void removeAll() throws com.liferay.portal.SystemException;

    public int countByUuid(java.lang.String uuid)
        throws com.liferay.portal.SystemException;

    public int countByUUID_G(java.lang.String uuid, java.lang.Long groupId)
        throws com.liferay.portal.SystemException;

    public int countAll() throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> getMetadataEntries(
        java.lang.Long pk) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> getMetadataEntries(
        java.lang.Long pk, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> getMetadataEntries(
        java.lang.Long pk, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public int getMetadataEntriesSize(java.lang.Long pk)
        throws com.liferay.portal.SystemException;

    public boolean containsMetadataEntry(java.lang.Long pk,
        java.lang.Long metadataEntryPK)
        throws com.liferay.portal.SystemException;

    public boolean containsMetadataEntries(java.lang.Long pk)
        throws com.liferay.portal.SystemException;

    public void addMetadataEntry(java.lang.Long pk,
        java.lang.Long metadataEntryPK)
        throws com.liferay.portal.SystemException;

    public void addMetadataEntry(java.lang.Long pk,
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry)
        throws com.liferay.portal.SystemException;

    public void addMetadataEntries(java.lang.Long pk,
        java.lang.Long[] metadataEntryPKs)
        throws com.liferay.portal.SystemException;

    public void addMetadataEntries(java.lang.Long pk,
        java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> metadataEntries)
        throws com.liferay.portal.SystemException;

    public void clearMetadataEntries(java.lang.Long pk)
        throws com.liferay.portal.SystemException;

    public void removeMetadataEntry(java.lang.Long pk,
        java.lang.Long metadataEntryPK)
        throws com.liferay.portal.SystemException;

    public void removeMetadataEntry(java.lang.Long pk,
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry)
        throws com.liferay.portal.SystemException;

    public void removeMetadataEntries(java.lang.Long pk,
        java.lang.Long[] metadataEntryPKs)
        throws com.liferay.portal.SystemException;

    public void removeMetadataEntries(java.lang.Long pk,
        java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> metadataEntries)
        throws com.liferay.portal.SystemException;

    public void setMetadataEntries(java.lang.Long pk,
        java.lang.Long[] metadataEntryPKs)
        throws com.liferay.portal.SystemException;

    public void setMetadataEntries(java.lang.Long pk,
        java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> metadataEntries)
        throws com.liferay.portal.SystemException;
}
