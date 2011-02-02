package com.ext.portlet.cart.service.persistence;

public class CartEntryUtil {
    private static CartEntryPersistence _persistence;

    public static void cacheResult(
        com.ext.portlet.cart.model.CartEntry cartEntry) {
        getPersistence().cacheResult(cartEntry);
    }

    public static void cacheResult(
        java.util.List<com.ext.portlet.cart.model.CartEntry> cartEntries) {
        getPersistence().cacheResult(cartEntries);
    }

    public static void clearCache() {
        getPersistence().clearCache();
    }

    public static com.ext.portlet.cart.model.CartEntry create(long cartEntryId) {
        return getPersistence().create(cartEntryId);
    }

    public static com.ext.portlet.cart.model.CartEntry remove(long cartEntryId)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().remove(cartEntryId);
    }

    public static com.ext.portlet.cart.model.CartEntry remove(
        com.ext.portlet.cart.model.CartEntry cartEntry)
        throws com.liferay.portal.SystemException {
        return getPersistence().remove(cartEntry);
    }

    /**
     * @deprecated Use <code>update(CartEntry cartEntry, boolean merge)</code>.
     */
    public static com.ext.portlet.cart.model.CartEntry update(
        com.ext.portlet.cart.model.CartEntry cartEntry)
        throws com.liferay.portal.SystemException {
        return getPersistence().update(cartEntry);
    }

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param                cartEntry the entity to add, update, or merge
     * @param                merge boolean value for whether to merge the entity. The
     *                                default value is false. Setting merge to true is more
     *                                expensive and should only be true when cartEntry is
     *                                transient. See LEP-5473 for a detailed discussion of this
     *                                method.
     * @return                true if the portlet can be displayed via Ajax
     */
    public static com.ext.portlet.cart.model.CartEntry update(
        com.ext.portlet.cart.model.CartEntry cartEntry, boolean merge)
        throws com.liferay.portal.SystemException {
        return getPersistence().update(cartEntry, merge);
    }

    public static com.ext.portlet.cart.model.CartEntry updateImpl(
        com.ext.portlet.cart.model.CartEntry cartEntry, boolean merge)
        throws com.liferay.portal.SystemException {
        return getPersistence().updateImpl(cartEntry, merge);
    }

    public static com.ext.portlet.cart.model.CartEntry findByPrimaryKey(
        long cartEntryId)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByPrimaryKey(cartEntryId);
    }

    public static com.ext.portlet.cart.model.CartEntry fetchByPrimaryKey(
        long cartEntryId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByPrimaryKey(cartEntryId);
    }

    public static java.util.List<com.ext.portlet.cart.model.CartEntry> findByTagNames(
        java.lang.String tagNames) throws com.liferay.portal.SystemException {
        return getPersistence().findByTagNames(tagNames);
    }

    public static java.util.List<com.ext.portlet.cart.model.CartEntry> findByTagNames(
        java.lang.String tagNames, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByTagNames(tagNames, start, end);
    }

    public static java.util.List<com.ext.portlet.cart.model.CartEntry> findByTagNames(
        java.lang.String tagNames, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByTagNames(tagNames, start, end, obc);
    }

    public static com.ext.portlet.cart.model.CartEntry findByTagNames_First(
        java.lang.String tagNames,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByTagNames_First(tagNames, obc);
    }

    public static com.ext.portlet.cart.model.CartEntry findByTagNames_Last(
        java.lang.String tagNames,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByTagNames_Last(tagNames, obc);
    }

    public static com.ext.portlet.cart.model.CartEntry[] findByTagNames_PrevAndNext(
        long cartEntryId, java.lang.String tagNames,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByTagNames_PrevAndNext(cartEntryId, tagNames, obc);
    }

    public static java.util.List<com.ext.portlet.cart.model.CartEntry> findByResourceType(
        java.lang.String resourceType)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByResourceType(resourceType);
    }

    public static java.util.List<com.ext.portlet.cart.model.CartEntry> findByResourceType(
        java.lang.String resourceType, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByResourceType(resourceType, start, end);
    }

    public static java.util.List<com.ext.portlet.cart.model.CartEntry> findByResourceType(
        java.lang.String resourceType, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByResourceType(resourceType, start, end, obc);
    }

    public static com.ext.portlet.cart.model.CartEntry findByResourceType_First(
        java.lang.String resourceType,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByResourceType_First(resourceType, obc);
    }

    public static com.ext.portlet.cart.model.CartEntry findByResourceType_Last(
        java.lang.String resourceType,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByResourceType_Last(resourceType, obc);
    }

    public static com.ext.portlet.cart.model.CartEntry[] findByResourceType_PrevAndNext(
        long cartEntryId, java.lang.String resourceType,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByResourceType_PrevAndNext(cartEntryId, resourceType,
            obc);
    }

    public static java.util.List<com.ext.portlet.cart.model.CartEntry> findByResourceId(
        long resourceId) throws com.liferay.portal.SystemException {
        return getPersistence().findByResourceId(resourceId);
    }

    public static java.util.List<com.ext.portlet.cart.model.CartEntry> findByResourceId(
        long resourceId, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByResourceId(resourceId, start, end);
    }

    public static java.util.List<com.ext.portlet.cart.model.CartEntry> findByResourceId(
        long resourceId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByResourceId(resourceId, start, end, obc);
    }

    public static com.ext.portlet.cart.model.CartEntry findByResourceId_First(
        long resourceId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByResourceId_First(resourceId, obc);
    }

    public static com.ext.portlet.cart.model.CartEntry findByResourceId_Last(
        long resourceId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException {
        return getPersistence().findByResourceId_Last(resourceId, obc);
    }

    public static com.ext.portlet.cart.model.CartEntry[] findByResourceId_PrevAndNext(
        long cartEntryId, long resourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByResourceId_PrevAndNext(cartEntryId, resourceId, obc);
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

    public static java.util.List<com.ext.portlet.cart.model.CartEntry> findAll()
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll();
    }

    public static java.util.List<com.ext.portlet.cart.model.CartEntry> findAll(
        int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end);
    }

    public static java.util.List<com.ext.portlet.cart.model.CartEntry> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end, obc);
    }

    public static void removeByTagNames(java.lang.String tagNames)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByTagNames(tagNames);
    }

    public static void removeByResourceType(java.lang.String resourceType)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByResourceType(resourceType);
    }

    public static void removeByResourceId(long resourceId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByResourceId(resourceId);
    }

    public static void removeAll() throws com.liferay.portal.SystemException {
        getPersistence().removeAll();
    }

    public static int countByTagNames(java.lang.String tagNames)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByTagNames(tagNames);
    }

    public static int countByResourceType(java.lang.String resourceType)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByResourceType(resourceType);
    }

    public static int countByResourceId(long resourceId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByResourceId(resourceId);
    }

    public static int countAll() throws com.liferay.portal.SystemException {
        return getPersistence().countAll();
    }

    public static java.util.List<com.ext.portlet.cart.model.Cart> getCarts(
        long pk) throws com.liferay.portal.SystemException {
        return getPersistence().getCarts(pk);
    }

    public static java.util.List<com.ext.portlet.cart.model.Cart> getCarts(
        long pk, int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().getCarts(pk, start, end);
    }

    public static java.util.List<com.ext.portlet.cart.model.Cart> getCarts(
        long pk, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().getCarts(pk, start, end, obc);
    }

    public static int getCartsSize(long pk)
        throws com.liferay.portal.SystemException {
        return getPersistence().getCartsSize(pk);
    }

    public static boolean containsCart(long pk, long cartPK)
        throws com.liferay.portal.SystemException {
        return getPersistence().containsCart(pk, cartPK);
    }

    public static boolean containsCarts(long pk)
        throws com.liferay.portal.SystemException {
        return getPersistence().containsCarts(pk);
    }

    public static void addCart(long pk, long cartPK)
        throws com.liferay.portal.SystemException {
        getPersistence().addCart(pk, cartPK);
    }

    public static void addCart(long pk, com.ext.portlet.cart.model.Cart cart)
        throws com.liferay.portal.SystemException {
        getPersistence().addCart(pk, cart);
    }

    public static void addCarts(long pk, long[] cartPKs)
        throws com.liferay.portal.SystemException {
        getPersistence().addCarts(pk, cartPKs);
    }

    public static void addCarts(long pk,
        java.util.List<com.ext.portlet.cart.model.Cart> carts)
        throws com.liferay.portal.SystemException {
        getPersistence().addCarts(pk, carts);
    }

    public static void clearCarts(long pk)
        throws com.liferay.portal.SystemException {
        getPersistence().clearCarts(pk);
    }

    public static void removeCart(long pk, long cartPK)
        throws com.liferay.portal.SystemException {
        getPersistence().removeCart(pk, cartPK);
    }

    public static void removeCart(long pk, com.ext.portlet.cart.model.Cart cart)
        throws com.liferay.portal.SystemException {
        getPersistence().removeCart(pk, cart);
    }

    public static void removeCarts(long pk, long[] cartPKs)
        throws com.liferay.portal.SystemException {
        getPersistence().removeCarts(pk, cartPKs);
    }

    public static void removeCarts(long pk,
        java.util.List<com.ext.portlet.cart.model.Cart> carts)
        throws com.liferay.portal.SystemException {
        getPersistence().removeCarts(pk, carts);
    }

    public static void setCarts(long pk, long[] cartPKs)
        throws com.liferay.portal.SystemException {
        getPersistence().setCarts(pk, cartPKs);
    }

    public static void setCarts(long pk,
        java.util.List<com.ext.portlet.cart.model.Cart> carts)
        throws com.liferay.portal.SystemException {
        getPersistence().setCarts(pk, carts);
    }

    public static CartEntryPersistence getPersistence() {
        return _persistence;
    }

    public void setPersistence(CartEntryPersistence persistence) {
        _persistence = persistence;
    }
}
