package com.ext.portlet.bookreports.service.persistence;


/**
 * <a href="MetadataEntryUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       MetadataEntryPersistence
 * @see       MetadataEntryPersistenceImpl
 * @generated
 */
public class MetadataEntryUtil {
    private static MetadataEntryPersistence _persistence;

    public static void cacheResult(
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry) {
        getPersistence().cacheResult(metadataEntry);
    }

    public static void cacheResult(
        java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> metadataEntries) {
        getPersistence().cacheResult(metadataEntries);
    }

    public static void clearCache() {
        getPersistence().clearCache();
    }

    public static com.ext.portlet.bookreports.model.MetadataEntry create(
        java.lang.Long entryId) {
        return getPersistence().create(entryId);
    }

    public static com.ext.portlet.bookreports.model.MetadataEntry remove(
        java.lang.Long entryId)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().remove(entryId);
    }

    public static com.ext.portlet.bookreports.model.MetadataEntry remove(
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry)
        throws com.liferay.portal.SystemException {
        return getPersistence().remove(metadataEntry);
    }

    /**
     * @deprecated Use {@link #update(MetadataEntry, boolean merge)}.
     */
    public static com.ext.portlet.bookreports.model.MetadataEntry update(
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry)
        throws com.liferay.portal.SystemException {
        return getPersistence().update(metadataEntry);
    }

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
    public static com.ext.portlet.bookreports.model.MetadataEntry update(
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry,
        boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().update(metadataEntry, merge);
    }

    public static com.ext.portlet.bookreports.model.MetadataEntry updateImpl(
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry,
        boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().updateImpl(metadataEntry, merge);
    }

    public static com.ext.portlet.bookreports.model.MetadataEntry findByPrimaryKey(
        java.lang.Long entryId)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByPrimaryKey(entryId);
    }

    public static com.ext.portlet.bookreports.model.MetadataEntry fetchByPrimaryKey(
        java.lang.Long entryId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByPrimaryKey(entryId);
    }

    public static java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> findByUuid(
        java.lang.String uuid) throws com.liferay.portal.SystemException {
        return getPersistence().findByUuid(uuid);
    }

    public static java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> findByUuid(
        java.lang.String uuid, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByUuid(uuid, start, end);
    }

    public static java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> findByUuid(
        java.lang.String uuid, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByUuid(uuid, start, end, obc);
    }

    public static com.ext.portlet.bookreports.model.MetadataEntry findByUuid_First(
        java.lang.String uuid,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByUuid_First(uuid, obc);
    }

    public static com.ext.portlet.bookreports.model.MetadataEntry findByUuid_Last(
        java.lang.String uuid,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByUuid_Last(uuid, obc);
    }

    public static com.ext.portlet.bookreports.model.MetadataEntry[] findByUuid_PrevAndNext(
        java.lang.Long entryId, java.lang.String uuid,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByUuid_PrevAndNext(entryId, uuid, obc);
    }

    public static com.ext.portlet.bookreports.model.MetadataEntry findByUUID_G(
        java.lang.String uuid, java.lang.Long groupId)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByUUID_G(uuid, groupId);
    }

    public static com.ext.portlet.bookreports.model.MetadataEntry fetchByUUID_G(
        java.lang.String uuid, java.lang.Long groupId)
        throws com.liferay.portal.SystemException {
        return getPersistence().fetchByUUID_G(uuid, groupId);
    }

    public static com.ext.portlet.bookreports.model.MetadataEntry fetchByUUID_G(
        java.lang.String uuid, java.lang.Long groupId, boolean retrieveFromCache)
        throws com.liferay.portal.SystemException {
        return getPersistence().fetchByUUID_G(uuid, groupId, retrieveFromCache);
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

    public static java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> findAll()
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll();
    }

    public static java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> findAll(
        int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end);
    }

    public static java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end, obc);
    }

    public static void removeByUuid(java.lang.String uuid)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByUuid(uuid);
    }

    public static void removeByUUID_G(java.lang.String uuid,
        java.lang.Long groupId)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException {
        getPersistence().removeByUUID_G(uuid, groupId);
    }

    public static void removeAll() throws com.liferay.portal.SystemException {
        getPersistence().removeAll();
    }

    public static int countByUuid(java.lang.String uuid)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByUuid(uuid);
    }

    public static int countByUUID_G(java.lang.String uuid,
        java.lang.Long groupId) throws com.liferay.portal.SystemException {
        return getPersistence().countByUUID_G(uuid, groupId);
    }

    public static int countAll() throws com.liferay.portal.SystemException {
        return getPersistence().countAll();
    }

    public static java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> getMetadataEntries(
        java.lang.Long pk) throws com.liferay.portal.SystemException {
        return getPersistence().getMetadataEntries(pk);
    }

    public static java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> getMetadataEntries(
        java.lang.Long pk, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().getMetadataEntries(pk, start, end);
    }

    public static java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> getMetadataEntries(
        java.lang.Long pk, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().getMetadataEntries(pk, start, end, obc);
    }

    public static int getMetadataEntriesSize(java.lang.Long pk)
        throws com.liferay.portal.SystemException {
        return getPersistence().getMetadataEntriesSize(pk);
    }

    public static boolean containsMetadataEntry(java.lang.Long pk,
        java.lang.Long metadataEntryPK)
        throws com.liferay.portal.SystemException {
        return getPersistence().containsMetadataEntry(pk, metadataEntryPK);
    }

    public static boolean containsMetadataEntries(java.lang.Long pk)
        throws com.liferay.portal.SystemException {
        return getPersistence().containsMetadataEntries(pk);
    }

    public static void addMetadataEntry(java.lang.Long pk,
        java.lang.Long metadataEntryPK)
        throws com.liferay.portal.SystemException {
        getPersistence().addMetadataEntry(pk, metadataEntryPK);
    }

    public static void addMetadataEntry(java.lang.Long pk,
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry)
        throws com.liferay.portal.SystemException {
        getPersistence().addMetadataEntry(pk, metadataEntry);
    }

    public static void addMetadataEntries(java.lang.Long pk,
        java.lang.Long[] metadataEntryPKs)
        throws com.liferay.portal.SystemException {
        getPersistence().addMetadataEntries(pk, metadataEntryPKs);
    }

    public static void addMetadataEntries(java.lang.Long pk,
        java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> metadataEntries)
        throws com.liferay.portal.SystemException {
        getPersistence().addMetadataEntries(pk, metadataEntries);
    }

    public static void clearMetadataEntries(java.lang.Long pk)
        throws com.liferay.portal.SystemException {
        getPersistence().clearMetadataEntries(pk);
    }

    public static void removeMetadataEntry(java.lang.Long pk,
        java.lang.Long metadataEntryPK)
        throws com.liferay.portal.SystemException {
        getPersistence().removeMetadataEntry(pk, metadataEntryPK);
    }

    public static void removeMetadataEntry(java.lang.Long pk,
        com.ext.portlet.bookreports.model.MetadataEntry metadataEntry)
        throws com.liferay.portal.SystemException {
        getPersistence().removeMetadataEntry(pk, metadataEntry);
    }

    public static void removeMetadataEntries(java.lang.Long pk,
        java.lang.Long[] metadataEntryPKs)
        throws com.liferay.portal.SystemException {
        getPersistence().removeMetadataEntries(pk, metadataEntryPKs);
    }

    public static void removeMetadataEntries(java.lang.Long pk,
        java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> metadataEntries)
        throws com.liferay.portal.SystemException {
        getPersistence().removeMetadataEntries(pk, metadataEntries);
    }

    public static void setMetadataEntries(java.lang.Long pk,
        java.lang.Long[] metadataEntryPKs)
        throws com.liferay.portal.SystemException {
        getPersistence().setMetadataEntries(pk, metadataEntryPKs);
    }

    public static void setMetadataEntries(java.lang.Long pk,
        java.util.List<com.ext.portlet.bookreports.model.MetadataEntry> metadataEntries)
        throws com.liferay.portal.SystemException {
        getPersistence().setMetadataEntries(pk, metadataEntries);
    }

    public static MetadataEntryPersistence getPersistence() {
        return _persistence;
    }

    public void setPersistence(MetadataEntryPersistence persistence) {
        _persistence = persistence;
    }
}
