package com.ext.portlet.metadata.service.persistence;

public class MetadataEntryUtil {
    private static MetadataEntryPersistence _persistence;

    public static void cacheResult(
        com.ext.portlet.metadata.model.MetadataEntry metadataEntry) {
        getPersistence().cacheResult(metadataEntry);
    }

    public static void cacheResult(
        java.util.List<com.ext.portlet.metadata.model.MetadataEntry> metadataEntries) {
        getPersistence().cacheResult(metadataEntries);
    }

    public static void clearCache() {
        getPersistence().clearCache();
    }

    public static com.ext.portlet.metadata.model.MetadataEntry create(
        java.lang.Long entryId) {
        return getPersistence().create(entryId);
    }

    public static com.ext.portlet.metadata.model.MetadataEntry remove(
        java.lang.Long entryId)
        throws com.ext.portlet.metadata.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().remove(entryId);
    }

    public static com.ext.portlet.metadata.model.MetadataEntry remove(
        com.ext.portlet.metadata.model.MetadataEntry metadataEntry)
        throws com.liferay.portal.SystemException {
        return getPersistence().remove(metadataEntry);
    }

    /**
     * @deprecated Use <code>update(MetadataEntry metadataEntry, boolean merge)</code>.
     */
    public static com.ext.portlet.metadata.model.MetadataEntry update(
        com.ext.portlet.metadata.model.MetadataEntry metadataEntry)
        throws com.liferay.portal.SystemException {
        return getPersistence().update(metadataEntry);
    }

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param                metadataEntry the entity to add, update, or merge
     * @param                merge boolean value for whether to merge the entity. The
     *                                default value is false. Setting merge to true is more
     *                                expensive and should only be true when metadataEntry is
     *                                transient. See LEP-5473 for a detailed discussion of this
     *                                method.
     * @return                true if the portlet can be displayed via Ajax
     */
    public static com.ext.portlet.metadata.model.MetadataEntry update(
        com.ext.portlet.metadata.model.MetadataEntry metadataEntry,
        boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().update(metadataEntry, merge);
    }

    public static com.ext.portlet.metadata.model.MetadataEntry updateImpl(
        com.ext.portlet.metadata.model.MetadataEntry metadataEntry,
        boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().updateImpl(metadataEntry, merge);
    }

    public static com.ext.portlet.metadata.model.MetadataEntry findByPrimaryKey(
        java.lang.Long entryId)
        throws com.ext.portlet.metadata.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByPrimaryKey(entryId);
    }

    public static com.ext.portlet.metadata.model.MetadataEntry fetchByPrimaryKey(
        java.lang.Long entryId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByPrimaryKey(entryId);
    }

    public static com.ext.portlet.metadata.model.MetadataEntry findByAssertEntryId(
        java.lang.Long assertEntryId)
        throws com.ext.portlet.metadata.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByAssertEntryId(assertEntryId);
    }

    public static com.ext.portlet.metadata.model.MetadataEntry fetchByAssertEntryId(
        java.lang.Long assertEntryId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByAssertEntryId(assertEntryId);
    }

    public static com.ext.portlet.metadata.model.MetadataEntry fetchByAssertEntryId(
        java.lang.Long assertEntryId, boolean retrieveFromCache)
        throws com.liferay.portal.SystemException {
        return getPersistence()
                   .fetchByAssertEntryId(assertEntryId, retrieveFromCache);
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

    public static java.util.List<com.ext.portlet.metadata.model.MetadataEntry> findAll()
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll();
    }

    public static java.util.List<com.ext.portlet.metadata.model.MetadataEntry> findAll(
        int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end);
    }

    public static java.util.List<com.ext.portlet.metadata.model.MetadataEntry> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end, obc);
    }

    public static void removeByAssertEntryId(java.lang.Long assertEntryId)
        throws com.ext.portlet.metadata.NoSuchEntryException,
            com.liferay.portal.SystemException {
        getPersistence().removeByAssertEntryId(assertEntryId);
    }

    public static void removeAll() throws com.liferay.portal.SystemException {
        getPersistence().removeAll();
    }

    public static int countByAssertEntryId(java.lang.Long assertEntryId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByAssertEntryId(assertEntryId);
    }

    public static int countAll() throws com.liferay.portal.SystemException {
        return getPersistence().countAll();
    }

    public static MetadataEntryPersistence getPersistence() {
        return _persistence;
    }

    public void setPersistence(MetadataEntryPersistence persistence) {
        _persistence = persistence;
    }
}
