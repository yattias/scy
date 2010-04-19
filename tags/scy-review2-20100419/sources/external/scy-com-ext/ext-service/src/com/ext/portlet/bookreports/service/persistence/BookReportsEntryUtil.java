package com.ext.portlet.bookreports.service.persistence;

public class BookReportsEntryUtil {
    private static BookReportsEntryPersistence _persistence;

    public static void cacheResult(
        com.ext.portlet.bookreports.model.BookReportsEntry bookReportsEntry) {
        getPersistence().cacheResult(bookReportsEntry);
    }

    public static void cacheResult(
        java.util.List<com.ext.portlet.bookreports.model.BookReportsEntry> bookReportsEntries) {
        getPersistence().cacheResult(bookReportsEntries);
    }

    public static void clearCache() {
        getPersistence().clearCache();
    }

    public static com.ext.portlet.bookreports.model.BookReportsEntry create(
        java.lang.Long entryId) {
        return getPersistence().create(entryId);
    }

    public static com.ext.portlet.bookreports.model.BookReportsEntry remove(
        java.lang.Long entryId)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().remove(entryId);
    }

    public static com.ext.portlet.bookreports.model.BookReportsEntry remove(
        com.ext.portlet.bookreports.model.BookReportsEntry bookReportsEntry)
        throws com.liferay.portal.SystemException {
        return getPersistence().remove(bookReportsEntry);
    }

    /**
     * @deprecated Use <code>update(BookReportsEntry bookReportsEntry, boolean merge)</code>.
     */
    public static com.ext.portlet.bookreports.model.BookReportsEntry update(
        com.ext.portlet.bookreports.model.BookReportsEntry bookReportsEntry)
        throws com.liferay.portal.SystemException {
        return getPersistence().update(bookReportsEntry);
    }

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param                bookReportsEntry the entity to add, update, or merge
     * @param                merge boolean value for whether to merge the entity. The
     *                                default value is false. Setting merge to true is more
     *                                expensive and should only be true when bookReportsEntry is
     *                                transient. See LEP-5473 for a detailed discussion of this
     *                                method.
     * @return                true if the portlet can be displayed via Ajax
     */
    public static com.ext.portlet.bookreports.model.BookReportsEntry update(
        com.ext.portlet.bookreports.model.BookReportsEntry bookReportsEntry,
        boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().update(bookReportsEntry, merge);
    }

    public static com.ext.portlet.bookreports.model.BookReportsEntry updateImpl(
        com.ext.portlet.bookreports.model.BookReportsEntry bookReportsEntry,
        boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().updateImpl(bookReportsEntry, merge);
    }

    public static com.ext.portlet.bookreports.model.BookReportsEntry findByPrimaryKey(
        java.lang.Long entryId)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByPrimaryKey(entryId);
    }

    public static com.ext.portlet.bookreports.model.BookReportsEntry fetchByPrimaryKey(
        java.lang.Long entryId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByPrimaryKey(entryId);
    }

    public static java.util.List<com.ext.portlet.bookreports.model.BookReportsEntry> findByUuid(
        java.lang.String uuid) throws com.liferay.portal.SystemException {
        return getPersistence().findByUuid(uuid);
    }

    public static java.util.List<com.ext.portlet.bookreports.model.BookReportsEntry> findByUuid(
        java.lang.String uuid, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByUuid(uuid, start, end);
    }

    public static java.util.List<com.ext.portlet.bookreports.model.BookReportsEntry> findByUuid(
        java.lang.String uuid, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByUuid(uuid, start, end, obc);
    }

    public static com.ext.portlet.bookreports.model.BookReportsEntry findByUuid_First(
        java.lang.String uuid,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByUuid_First(uuid, obc);
    }

    public static com.ext.portlet.bookreports.model.BookReportsEntry findByUuid_Last(
        java.lang.String uuid,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByUuid_Last(uuid, obc);
    }

    public static com.ext.portlet.bookreports.model.BookReportsEntry[] findByUuid_PrevAndNext(
        java.lang.Long entryId, java.lang.String uuid,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByUuid_PrevAndNext(entryId, uuid, obc);
    }

    public static com.ext.portlet.bookreports.model.BookReportsEntry findByUUID_G(
        java.lang.String uuid, java.lang.Long groupId)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByUUID_G(uuid, groupId);
    }

    public static com.ext.portlet.bookreports.model.BookReportsEntry fetchByUUID_G(
        java.lang.String uuid, java.lang.Long groupId)
        throws com.liferay.portal.SystemException {
        return getPersistence().fetchByUUID_G(uuid, groupId);
    }

    public static com.ext.portlet.bookreports.model.BookReportsEntry fetchByUUID_G(
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

    public static java.util.List<com.ext.portlet.bookreports.model.BookReportsEntry> findAll()
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll();
    }

    public static java.util.List<com.ext.portlet.bookreports.model.BookReportsEntry> findAll(
        int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end);
    }

    public static java.util.List<com.ext.portlet.bookreports.model.BookReportsEntry> findAll(
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

    public static BookReportsEntryPersistence getPersistence() {
        return _persistence;
    }

    public void setPersistence(BookReportsEntryPersistence persistence) {
        _persistence = persistence;
    }
}
