package com.ext.portlet.cart.service.persistence;

public class CartUtil {
    private static CartPersistence _persistence;

    public static void cacheResult(com.ext.portlet.cart.model.Cart cart) {
        getPersistence().cacheResult(cart);
    }

    public static void cacheResult(
        java.util.List<com.ext.portlet.cart.model.Cart> carts) {
        getPersistence().cacheResult(carts);
    }

    public static void clearCache() {
        getPersistence().clearCache();
    }

    public static com.ext.portlet.cart.model.Cart create(long cartId) {
        return getPersistence().create(cartId);
    }

    public static com.ext.portlet.cart.model.Cart remove(long cartId)
        throws com.ext.portlet.cart.NoSuchCartException,
            com.liferay.portal.SystemException {
        return getPersistence().remove(cartId);
    }

    public static com.ext.portlet.cart.model.Cart remove(
        com.ext.portlet.cart.model.Cart cart)
        throws com.liferay.portal.SystemException {
        return getPersistence().remove(cart);
    }

    /**
     * @deprecated Use <code>update(Cart cart, boolean merge)</code>.
     */
    public static com.ext.portlet.cart.model.Cart update(
        com.ext.portlet.cart.model.Cart cart)
        throws com.liferay.portal.SystemException {
        return getPersistence().update(cart);
    }

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param                cart the entity to add, update, or merge
     * @param                merge boolean value for whether to merge the entity. The
     *                                default value is false. Setting merge to true is more
     *                                expensive and should only be true when cart is
     *                                transient. See LEP-5473 for a detailed discussion of this
     *                                method.
     * @return                true if the portlet can be displayed via Ajax
     */
    public static com.ext.portlet.cart.model.Cart update(
        com.ext.portlet.cart.model.Cart cart, boolean merge)
        throws com.liferay.portal.SystemException {
        return getPersistence().update(cart, merge);
    }

    public static com.ext.portlet.cart.model.Cart updateImpl(
        com.ext.portlet.cart.model.Cart cart, boolean merge)
        throws com.liferay.portal.SystemException {
        return getPersistence().updateImpl(cart, merge);
    }

    public static com.ext.portlet.cart.model.Cart findByPrimaryKey(long cartId)
        throws com.ext.portlet.cart.NoSuchCartException,
            com.liferay.portal.SystemException {
        return getPersistence().findByPrimaryKey(cartId);
    }

    public static com.ext.portlet.cart.model.Cart fetchByPrimaryKey(long cartId)
        throws com.liferay.portal.SystemException {
        return getPersistence().fetchByPrimaryKey(cartId);
    }

    public static java.util.List<com.ext.portlet.cart.model.Cart> findByCompanyId(
        long companyId) throws com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId(companyId);
    }

    public static java.util.List<com.ext.portlet.cart.model.Cart> findByCompanyId(
        long companyId, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId(companyId, start, end);
    }

    public static java.util.List<com.ext.portlet.cart.model.Cart> findByCompanyId(
        long companyId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId(companyId, start, end, obc);
    }

    public static com.ext.portlet.cart.model.Cart findByCompanyId_First(
        long companyId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartException,
            com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId_First(companyId, obc);
    }

    public static com.ext.portlet.cart.model.Cart findByCompanyId_Last(
        long companyId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartException,
            com.liferay.portal.SystemException {
        return getPersistence().findByCompanyId_Last(companyId, obc);
    }

    public static com.ext.portlet.cart.model.Cart[] findByCompanyId_PrevAndNext(
        long cartId, long companyId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartException,
            com.liferay.portal.SystemException {
        return getPersistence()
                   .findByCompanyId_PrevAndNext(cartId, companyId, obc);
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

    public static java.util.List<com.ext.portlet.cart.model.Cart> findAll()
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll();
    }

    public static java.util.List<com.ext.portlet.cart.model.Cart> findAll(
        int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end);
    }

    public static java.util.List<com.ext.portlet.cart.model.Cart> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end, obc);
    }

    public static void removeByCompanyId(long companyId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByCompanyId(companyId);
    }

    public static void removeAll() throws com.liferay.portal.SystemException {
        getPersistence().removeAll();
    }

    public static int countByCompanyId(long companyId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByCompanyId(companyId);
    }

    public static int countAll() throws com.liferay.portal.SystemException {
        return getPersistence().countAll();
    }

    public static java.util.List<com.ext.portlet.cart.model.CartEntry> getCartEntries(
        long pk) throws com.liferay.portal.SystemException {
        return getPersistence().getCartEntries(pk);
    }

    public static java.util.List<com.ext.portlet.cart.model.CartEntry> getCartEntries(
        long pk, int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().getCartEntries(pk, start, end);
    }

    public static java.util.List<com.ext.portlet.cart.model.CartEntry> getCartEntries(
        long pk, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().getCartEntries(pk, start, end, obc);
    }

    public static int getCartEntriesSize(long pk)
        throws com.liferay.portal.SystemException {
        return getPersistence().getCartEntriesSize(pk);
    }

    public static boolean containsCartEntry(long pk, long cartEntryPK)
        throws com.liferay.portal.SystemException {
        return getPersistence().containsCartEntry(pk, cartEntryPK);
    }

    public static boolean containsCartEntries(long pk)
        throws com.liferay.portal.SystemException {
        return getPersistence().containsCartEntries(pk);
    }

    public static void addCartEntry(long pk, long cartEntryPK)
        throws com.liferay.portal.SystemException {
        getPersistence().addCartEntry(pk, cartEntryPK);
    }

    public static void addCartEntry(long pk,
        com.ext.portlet.cart.model.CartEntry cartEntry)
        throws com.liferay.portal.SystemException {
        getPersistence().addCartEntry(pk, cartEntry);
    }

    public static void addCartEntries(long pk, long[] cartEntryPKs)
        throws com.liferay.portal.SystemException {
        getPersistence().addCartEntries(pk, cartEntryPKs);
    }

    public static void addCartEntries(long pk,
        java.util.List<com.ext.portlet.cart.model.CartEntry> cartEntries)
        throws com.liferay.portal.SystemException {
        getPersistence().addCartEntries(pk, cartEntries);
    }

    public static void clearCartEntries(long pk)
        throws com.liferay.portal.SystemException {
        getPersistence().clearCartEntries(pk);
    }

    public static void removeCartEntry(long pk, long cartEntryPK)
        throws com.liferay.portal.SystemException {
        getPersistence().removeCartEntry(pk, cartEntryPK);
    }

    public static void removeCartEntry(long pk,
        com.ext.portlet.cart.model.CartEntry cartEntry)
        throws com.liferay.portal.SystemException {
        getPersistence().removeCartEntry(pk, cartEntry);
    }

    public static void removeCartEntries(long pk, long[] cartEntryPKs)
        throws com.liferay.portal.SystemException {
        getPersistence().removeCartEntries(pk, cartEntryPKs);
    }

    public static void removeCartEntries(long pk,
        java.util.List<com.ext.portlet.cart.model.CartEntry> cartEntries)
        throws com.liferay.portal.SystemException {
        getPersistence().removeCartEntries(pk, cartEntries);
    }

    public static void setCartEntries(long pk, long[] cartEntryPKs)
        throws com.liferay.portal.SystemException {
        getPersistence().setCartEntries(pk, cartEntryPKs);
    }

    public static void setCartEntries(long pk,
        java.util.List<com.ext.portlet.cart.model.CartEntry> cartEntries)
        throws com.liferay.portal.SystemException {
        getPersistence().setCartEntries(pk, cartEntries);
    }

    public static CartPersistence getPersistence() {
        return _persistence;
    }

    public void setPersistence(CartPersistence persistence) {
        _persistence = persistence;
    }
}
