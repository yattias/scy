package com.ext.portlet.linkTool.service.persistence;

import com.liferay.portal.service.persistence.BasePersistence;


public interface LinkEntryPersistence extends BasePersistence {
    public void cacheResult(com.ext.portlet.linkTool.model.LinkEntry linkEntry);

    public void cacheResult(
        java.util.List<com.ext.portlet.linkTool.model.LinkEntry> linkEntries);

    public void clearCache();

    public com.ext.portlet.linkTool.model.LinkEntry create(long linkId);

    public com.ext.portlet.linkTool.model.LinkEntry remove(long linkId)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.linkTool.model.LinkEntry remove(
        com.ext.portlet.linkTool.model.LinkEntry linkEntry)
        throws com.liferay.portal.SystemException;

    /**
     * @deprecated Use <code>update(LinkEntry linkEntry, boolean merge)</code>.
     */
    public com.ext.portlet.linkTool.model.LinkEntry update(
        com.ext.portlet.linkTool.model.LinkEntry linkEntry)
        throws com.liferay.portal.SystemException;

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param                linkEntry the entity to add, update, or merge
     * @param                merge boolean value for whether to merge the entity. The
     *                                default value is false. Setting merge to true is more
     *                                expensive and should only be true when linkEntry is
     *                                transient. See LEP-5473 for a detailed discussion of this
     *                                method.
     * @return                true if the portlet can be displayed via Ajax
     */
    public com.ext.portlet.linkTool.model.LinkEntry update(
        com.ext.portlet.linkTool.model.LinkEntry linkEntry, boolean merge)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.linkTool.model.LinkEntry updateImpl(
        com.ext.portlet.linkTool.model.LinkEntry linkEntry, boolean merge)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.linkTool.model.LinkEntry findByPrimaryKey(
        long linkId)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.linkTool.model.LinkEntry fetchByPrimaryKey(
        long linkId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findByResourceId(
        java.lang.String resourceId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findByResourceId(
        java.lang.String resourceId, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findByResourceId(
        java.lang.String resourceId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.linkTool.model.LinkEntry findByResourceId_First(
        java.lang.String resourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.linkTool.model.LinkEntry findByResourceId_Last(
        java.lang.String resourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.linkTool.model.LinkEntry[] findByResourceId_PrevAndNext(
        long linkId, java.lang.String resourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findByLinkedResourceId(
        java.lang.String linkedResourceId)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findByLinkedResourceId(
        java.lang.String linkedResourceId, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findByLinkedResourceId(
        java.lang.String linkedResourceId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.linkTool.model.LinkEntry findByLinkedResourceId_First(
        java.lang.String linkedResourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.linkTool.model.LinkEntry findByLinkedResourceId_Last(
        java.lang.String linkedResourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.linkTool.model.LinkEntry[] findByLinkedResourceId_PrevAndNext(
        long linkId, java.lang.String linkedResourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findByR_L(
        java.lang.String resourceId, java.lang.String linkedResourceId)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findByR_L(
        java.lang.String resourceId, java.lang.String linkedResourceId,
        int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findByR_L(
        java.lang.String resourceId, java.lang.String linkedResourceId,
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.linkTool.model.LinkEntry findByR_L_First(
        java.lang.String resourceId, java.lang.String linkedResourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.linkTool.model.LinkEntry findByR_L_Last(
        java.lang.String resourceId, java.lang.String linkedResourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.linkTool.model.LinkEntry[] findByR_L_PrevAndNext(
        long linkId, java.lang.String resourceId,
        java.lang.String linkedResourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findAll()
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findAll(
        int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public void removeByResourceId(java.lang.String resourceId)
        throws com.liferay.portal.SystemException;

    public void removeByLinkedResourceId(java.lang.String linkedResourceId)
        throws com.liferay.portal.SystemException;

    public void removeByR_L(java.lang.String resourceId,
        java.lang.String linkedResourceId)
        throws com.liferay.portal.SystemException;

    public void removeAll() throws com.liferay.portal.SystemException;

    public int countByResourceId(java.lang.String resourceId)
        throws com.liferay.portal.SystemException;

    public int countByLinkedResourceId(java.lang.String linkedResourceId)
        throws com.liferay.portal.SystemException;

    public int countByR_L(java.lang.String resourceId,
        java.lang.String linkedResourceId)
        throws com.liferay.portal.SystemException;

    public int countAll() throws com.liferay.portal.SystemException;
}
