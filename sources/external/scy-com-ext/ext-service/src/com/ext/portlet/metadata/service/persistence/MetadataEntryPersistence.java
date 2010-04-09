package com.ext.portlet.metadata.service.persistence;

import com.liferay.portal.service.persistence.BasePersistence;


public interface MetadataEntryPersistence extends BasePersistence {
    public void cacheResult(
        com.ext.portlet.metadata.model.MetadataEntry metadataEntry);

    public void cacheResult(
        java.util.List<com.ext.portlet.metadata.model.MetadataEntry> metadataEntries);

    public void clearCache();

    public com.ext.portlet.metadata.model.MetadataEntry create(
        java.lang.Long entryId);

    public com.ext.portlet.metadata.model.MetadataEntry remove(
        java.lang.Long entryId)
        throws com.ext.portlet.metadata.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.metadata.model.MetadataEntry remove(
        com.ext.portlet.metadata.model.MetadataEntry metadataEntry)
        throws com.liferay.portal.SystemException;

    /**
     * @deprecated Use <code>update(MetadataEntry metadataEntry, boolean merge)</code>.
     */
    public com.ext.portlet.metadata.model.MetadataEntry update(
        com.ext.portlet.metadata.model.MetadataEntry metadataEntry)
        throws com.liferay.portal.SystemException;

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
    public com.ext.portlet.metadata.model.MetadataEntry update(
        com.ext.portlet.metadata.model.MetadataEntry metadataEntry,
        boolean merge) throws com.liferay.portal.SystemException;

    public com.ext.portlet.metadata.model.MetadataEntry updateImpl(
        com.ext.portlet.metadata.model.MetadataEntry metadataEntry,
        boolean merge) throws com.liferay.portal.SystemException;

    public com.ext.portlet.metadata.model.MetadataEntry findByPrimaryKey(
        java.lang.Long entryId)
        throws com.ext.portlet.metadata.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.metadata.model.MetadataEntry fetchByPrimaryKey(
        java.lang.Long entryId) throws com.liferay.portal.SystemException;

    public com.ext.portlet.metadata.model.MetadataEntry findByAssertEntryId(
        java.lang.Long assertEntryId)
        throws com.ext.portlet.metadata.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.metadata.model.MetadataEntry fetchByAssertEntryId(
        java.lang.Long assertEntryId) throws com.liferay.portal.SystemException;

    public com.ext.portlet.metadata.model.MetadataEntry fetchByAssertEntryId(
        java.lang.Long assertEntryId, boolean retrieveFromCache)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.metadata.model.MetadataEntry> findAll()
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.metadata.model.MetadataEntry> findAll(
        int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.metadata.model.MetadataEntry> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public void removeByAssertEntryId(java.lang.Long assertEntryId)
        throws com.ext.portlet.metadata.NoSuchEntryException,
            com.liferay.portal.SystemException;

    public void removeAll() throws com.liferay.portal.SystemException;

    public int countByAssertEntryId(java.lang.Long assertEntryId)
        throws com.liferay.portal.SystemException;

    public int countAll() throws com.liferay.portal.SystemException;
}
