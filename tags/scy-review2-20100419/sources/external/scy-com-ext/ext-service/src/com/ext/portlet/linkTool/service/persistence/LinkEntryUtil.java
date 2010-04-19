package com.ext.portlet.linkTool.service.persistence;

public class LinkEntryUtil {
    private static LinkEntryPersistence _persistence;

    public static void cacheResult(
        com.ext.portlet.linkTool.model.LinkEntry linkEntry) {
        getPersistence().cacheResult(linkEntry);
    }

    public static void cacheResult(
        java.util.List<com.ext.portlet.linkTool.model.LinkEntry> linkEntries) {
        getPersistence().cacheResult(linkEntries);
    }

    public static void clearCache() {
        getPersistence().clearCache();
    }

    public static com.ext.portlet.linkTool.model.LinkEntry create(long linkId) {
        return getPersistence().create(linkId);
    }

    public static com.ext.portlet.linkTool.model.LinkEntry remove(long linkId)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().remove(linkId);
    }

    public static com.ext.portlet.linkTool.model.LinkEntry remove(
        com.ext.portlet.linkTool.model.LinkEntry linkEntry)
        throws com.liferay.portal.SystemException {
        return getPersistence().remove(linkEntry);
    }

    /**
     * @deprecated Use <code>update(LinkEntry linkEntry, boolean merge)</code>.
     */
    public static com.ext.portlet.linkTool.model.LinkEntry update(
        com.ext.portlet.linkTool.model.LinkEntry linkEntry)
        throws com.liferay.portal.SystemException {
        return getPersistence().update(linkEntry);
    }

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
    public static com.ext.portlet.linkTool.model.LinkEntry update(
        com.ext.portlet.linkTool.model.LinkEntry linkEntry, boolean merge)
        throws com.liferay.portal.SystemException {
        return getPersistence().update(linkEntry, merge);
    }

    public static com.ext.portlet.linkTool.model.LinkEntry updateImpl(
        com.ext.portlet.linkTool.model.LinkEntry linkEntry, boolean merge)
        throws com.liferay.portal.SystemException {
        return getPersistence().updateImpl(linkEntry, merge);
    }

    public static com.ext.portlet.linkTool.model.LinkEntry findByPrimaryKey(
        long linkId)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByPrimaryKey(linkId);
    }

    public static com.ext.portlet.linkTool.model.LinkEntry fetchByPrimaryKey(
        long linkId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByPrimaryKey(linkId);
    }

    public static java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findByResourceId(
        java.lang.String resourceId) throws com.liferay.portal.SystemException {
        return getPersistence().findByResourceId(resourceId);
    }

    public static java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findByResourceId(
        java.lang.String resourceId, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByResourceId(resourceId, start, end);
    }

    public static java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findByResourceId(
        java.lang.String resourceId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByResourceId(resourceId, start, end, obc);
    }

    public static com.ext.portlet.linkTool.model.LinkEntry findByResourceId_First(
        java.lang.String resourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByResourceId_First(resourceId, obc);
    }

    public static com.ext.portlet.linkTool.model.LinkEntry findByResourceId_Last(
        java.lang.String resourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByResourceId_Last(resourceId, obc);
    }

    public static com.ext.portlet.linkTool.model.LinkEntry[] findByResourceId_PrevAndNext(
        long linkId, java.lang.String resourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByResourceId_PrevAndNext(linkId, resourceId, obc);
    }

    public static java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findByLinkedResourceId(
        java.lang.String linkedResourceId)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByLinkedResourceId(linkedResourceId);
    }

    public static java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findByLinkedResourceId(
        java.lang.String linkedResourceId, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence()
                   .findByLinkedResourceId(linkedResourceId, start, end);
    }

    public static java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findByLinkedResourceId(
        java.lang.String linkedResourceId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence()
                   .findByLinkedResourceId(linkedResourceId, start, end, obc);
    }

    public static com.ext.portlet.linkTool.model.LinkEntry findByLinkedResourceId_First(
        java.lang.String linkedResourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByLinkedResourceId_First(linkedResourceId, obc);
    }

    public static com.ext.portlet.linkTool.model.LinkEntry findByLinkedResourceId_Last(
        java.lang.String linkedResourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByLinkedResourceId_Last(linkedResourceId, obc);
    }

    public static com.ext.portlet.linkTool.model.LinkEntry[] findByLinkedResourceId_PrevAndNext(
        long linkId, java.lang.String linkedResourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByLinkedResourceId_PrevAndNext(linkId,
            linkedResourceId, obc);
    }

    public static java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findByR_L(
        java.lang.String resourceId, java.lang.String linkedResourceId)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByR_L(resourceId, linkedResourceId);
    }

    public static java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findByR_L(
        java.lang.String resourceId, java.lang.String linkedResourceId,
        int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence()
                   .findByR_L(resourceId, linkedResourceId, start, end);
    }

    public static java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findByR_L(
        java.lang.String resourceId, java.lang.String linkedResourceId,
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence()
                   .findByR_L(resourceId, linkedResourceId, start, end, obc);
    }

    public static com.ext.portlet.linkTool.model.LinkEntry findByR_L_First(
        java.lang.String resourceId, java.lang.String linkedResourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByR_L_First(resourceId, linkedResourceId, obc);
    }

    public static com.ext.portlet.linkTool.model.LinkEntry findByR_L_Last(
        java.lang.String resourceId, java.lang.String linkedResourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByR_L_Last(resourceId, linkedResourceId, obc);
    }

    public static com.ext.portlet.linkTool.model.LinkEntry[] findByR_L_PrevAndNext(
        long linkId, java.lang.String resourceId,
        java.lang.String linkedResourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.linkTool.NoSuchLinkEntryException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByR_L_PrevAndNext(linkId, resourceId, linkedResourceId,
            obc);
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

    public static java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findAll()
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll();
    }

    public static java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findAll(
        int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end);
    }

    public static java.util.List<com.ext.portlet.linkTool.model.LinkEntry> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end, obc);
    }

    public static void removeByResourceId(java.lang.String resourceId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByResourceId(resourceId);
    }

    public static void removeByLinkedResourceId(
        java.lang.String linkedResourceId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByLinkedResourceId(linkedResourceId);
    }

    public static void removeByR_L(java.lang.String resourceId,
        java.lang.String linkedResourceId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByR_L(resourceId, linkedResourceId);
    }

    public static void removeAll() throws com.liferay.portal.SystemException {
        getPersistence().removeAll();
    }

    public static int countByResourceId(java.lang.String resourceId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByResourceId(resourceId);
    }

    public static int countByLinkedResourceId(java.lang.String linkedResourceId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByLinkedResourceId(linkedResourceId);
    }

    public static int countByR_L(java.lang.String resourceId,
        java.lang.String linkedResourceId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByR_L(resourceId, linkedResourceId);
    }

    public static int countAll() throws com.liferay.portal.SystemException {
        return getPersistence().countAll();
    }

    public static LinkEntryPersistence getPersistence() {
        return _persistence;
    }

    public void setPersistence(LinkEntryPersistence persistence) {
        _persistence = persistence;
    }
}
