package com.ext.portlet.cart.service.persistence;

import com.liferay.portal.service.persistence.BasePersistence;


public interface CartPersistence extends BasePersistence {
    public void cacheResult(com.ext.portlet.cart.model.Cart cart);

    public void cacheResult(
        java.util.List<com.ext.portlet.cart.model.Cart> carts);

    public void clearCache();

    public com.ext.portlet.cart.model.Cart create(long cartId);

    public com.ext.portlet.cart.model.Cart remove(long cartId)
        throws com.ext.portlet.cart.NoSuchCartException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Cart remove(
        com.ext.portlet.cart.model.Cart cart)
        throws com.liferay.portal.SystemException;

    /**
     * @deprecated Use <code>update(Cart cart, boolean merge)</code>.
     */
    public com.ext.portlet.cart.model.Cart update(
        com.ext.portlet.cart.model.Cart cart)
        throws com.liferay.portal.SystemException;

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
    public com.ext.portlet.cart.model.Cart update(
        com.ext.portlet.cart.model.Cart cart, boolean merge)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Cart updateImpl(
        com.ext.portlet.cart.model.Cart cart, boolean merge)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Cart findByPrimaryKey(long cartId)
        throws com.ext.portlet.cart.NoSuchCartException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Cart fetchByPrimaryKey(long cartId)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Cart> findByCompanyId(
        long companyId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Cart> findByCompanyId(
        long companyId, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Cart> findByCompanyId(
        long companyId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Cart findByCompanyId_First(
        long companyId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Cart findByCompanyId_Last(
        long companyId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Cart[] findByCompanyId_PrevAndNext(
        long cartId, long companyId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartException,
            com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Cart> findAll()
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Cart> findAll(int start,
        int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Cart> findAll(int start,
        int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public void removeByCompanyId(long companyId)
        throws com.liferay.portal.SystemException;

    public void removeAll() throws com.liferay.portal.SystemException;

    public int countByCompanyId(long companyId)
        throws com.liferay.portal.SystemException;

    public int countAll() throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.CartEntry> getCartEntries(
        long pk) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.CartEntry> getCartEntries(
        long pk, int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.CartEntry> getCartEntries(
        long pk, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public int getCartEntriesSize(long pk)
        throws com.liferay.portal.SystemException;

    public boolean containsCartEntry(long pk, long cartEntryPK)
        throws com.liferay.portal.SystemException;

    public boolean containsCartEntries(long pk)
        throws com.liferay.portal.SystemException;

    public void addCartEntry(long pk, long cartEntryPK)
        throws com.liferay.portal.SystemException;

    public void addCartEntry(long pk,
        com.ext.portlet.cart.model.CartEntry cartEntry)
        throws com.liferay.portal.SystemException;

    public void addCartEntries(long pk, long[] cartEntryPKs)
        throws com.liferay.portal.SystemException;

    public void addCartEntries(long pk,
        java.util.List<com.ext.portlet.cart.model.CartEntry> cartEntries)
        throws com.liferay.portal.SystemException;

    public void clearCartEntries(long pk)
        throws com.liferay.portal.SystemException;

    public void removeCartEntry(long pk, long cartEntryPK)
        throws com.liferay.portal.SystemException;

    public void removeCartEntry(long pk,
        com.ext.portlet.cart.model.CartEntry cartEntry)
        throws com.liferay.portal.SystemException;

    public void removeCartEntries(long pk, long[] cartEntryPKs)
        throws com.liferay.portal.SystemException;

    public void removeCartEntries(long pk,
        java.util.List<com.ext.portlet.cart.model.CartEntry> cartEntries)
        throws com.liferay.portal.SystemException;

    public void setCartEntries(long pk, long[] cartEntryPKs)
        throws com.liferay.portal.SystemException;

    public void setCartEntries(long pk,
        java.util.List<com.ext.portlet.cart.model.CartEntry> cartEntries)
        throws com.liferay.portal.SystemException;
}
