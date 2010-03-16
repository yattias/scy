package com.ext.portlet.bookreports.service.persistence;

import com.liferay.portal.service.persistence.BasePersistence;


public interface BookReportsEntryPersistence extends BasePersistence {
    public void cacheResult(
        com.ext.portlet.bookreports.model.BookReportsEntry bookReportsEntry);

    public void cacheResult(
        java.util.List<com.ext.portlet.bookreports.model.BookReportsEntry> bookReportsEntries);

    public void clearCache();

    public com.ext.portlet.bookreports.model.BookReportsEntry create(
        java.lang.Long entryId);

    public com.ext.portlet.bookreports.model.BookReportsEntry remove(
        java.lang.Long entryId)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.BookReportsEntry remove(
        com.ext.portlet.bookreports.model.BookReportsEntry bookReportsEntry)
        throws com.liferay.portal.SystemException;

    /**
     * @deprecated Use <code>update(BookReportsEntry bookReportsEntry, boolean merge)</code>.
     */
    public com.ext.portlet.bookreports.model.BookReportsEntry update(
        com.ext.portlet.bookreports.model.BookReportsEntry bookReportsEntry)
        throws com.liferay.portal.SystemException;

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
    public com.ext.portlet.bookreports.model.BookReportsEntry update(
        com.ext.portlet.bookreports.model.BookReportsEntry bookReportsEntry,
        boolean merge) throws com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.BookReportsEntry updateImpl(
        com.ext.portlet.bookreports.model.BookReportsEntry bookReportsEntry,
        boolean merge) throws com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.BookReportsEntry findByPrimaryKey(
        java.lang.Long entryId)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.BookReportsEntry fetchByPrimaryKey(
        java.lang.Long entryId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.bookreports.model.BookReportsEntry> findByUuid(
        java.lang.String uuid) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.bookreports.model.BookReportsEntry> findByUuid(
        java.lang.String uuid, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.bookreports.model.BookReportsEntry> findByUuid(
        java.lang.String uuid, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.BookReportsEntry findByUuid_First(
        java.lang.String uuid,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.BookReportsEntry findByUuid_Last(
        java.lang.String uuid,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.BookReportsEntry[] findByUuid_PrevAndNext(
        java.lang.Long entryId, java.lang.String uuid,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.BookReportsEntry findByUUID_G(
        java.lang.String uuid, java.lang.Long groupId)
        throws com.ext.portlet.bookreports.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.BookReportsEntry fetchByUUID_G(
        java.lang.String uuid, java.lang.Long groupId)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.bookreports.model.BookReportsEntry fetchByUUID_G(
        java.lang.String uuid, java.lang.Long groupId, boolean retrieveFromCache)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.bookreports.model.BookReportsEntry> findAll()
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.bookreports.model.BookReportsEntry> findAll(
        int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.bookreports.model.BookReportsEntry> findAll(
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
}
