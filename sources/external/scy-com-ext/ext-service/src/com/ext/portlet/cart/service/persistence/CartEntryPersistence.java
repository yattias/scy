package com.ext.portlet.cart.service.persistence;

import com.liferay.portal.service.persistence.BasePersistence;


public interface CartEntryPersistence extends BasePersistence {
    public void cacheResult(com.ext.portlet.cart.model.CartEntry cartEntry);

    public void cacheResult(
        java.util.List<com.ext.portlet.cart.model.CartEntry> cartEntries);

    public void clearCache();

    public com.ext.portlet.cart.model.CartEntry create(long cartEntryId);

    public com.ext.portlet.cart.model.CartEntry remove(long cartEntryId)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.CartEntry remove(
        com.ext.portlet.cart.model.CartEntry cartEntry)
        throws com.liferay.portal.SystemException;

    /**
     * @deprecated Use <code>update(CartEntry cartEntry, boolean merge)</code>.
     */
    public com.ext.portlet.cart.model.CartEntry update(
        com.ext.portlet.cart.model.CartEntry cartEntry)
        throws com.liferay.portal.SystemException;

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
    public com.ext.portlet.cart.model.CartEntry update(
        com.ext.portlet.cart.model.CartEntry cartEntry, boolean merge)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.CartEntry updateImpl(
        com.ext.portlet.cart.model.CartEntry cartEntry, boolean merge)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.CartEntry findByPrimaryKey(
        long cartEntryId)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.CartEntry fetchByPrimaryKey(
        long cartEntryId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.CartEntry> findByTagNames(
        java.lang.String tagNames) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.CartEntry> findByTagNames(
        java.lang.String tagNames, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.CartEntry> findByTagNames(
        java.lang.String tagNames, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.CartEntry findByTagNames_First(
        java.lang.String tagNames,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.CartEntry findByTagNames_Last(
        java.lang.String tagNames,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.CartEntry[] findByTagNames_PrevAndNext(
        long cartEntryId, java.lang.String tagNames,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.CartEntry> findByResourceType(
        java.lang.String resourceType)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.CartEntry> findByResourceType(
        java.lang.String resourceType, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.CartEntry> findByResourceType(
        java.lang.String resourceType, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.CartEntry findByResourceType_First(
        java.lang.String resourceType,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.CartEntry findByResourceType_Last(
        java.lang.String resourceType,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.CartEntry[] findByResourceType_PrevAndNext(
        long cartEntryId, java.lang.String resourceType,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.CartEntry> findByResourceId(
        long resourceId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.CartEntry> findByResourceId(
        long resourceId, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.CartEntry> findByResourceId(
        long resourceId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.CartEntry findByResourceId_First(
        long resourceId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.CartEntry findByResourceId_Last(
        long resourceId, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.CartEntry[] findByResourceId_PrevAndNext(
        long cartEntryId, long resourceId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchCartEntryException,
            com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.CartEntry> findAll()
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.CartEntry> findAll(
        int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.CartEntry> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public void removeByTagNames(java.lang.String tagNames)
        throws com.liferay.portal.SystemException;

    public void removeByResourceType(java.lang.String resourceType)
        throws com.liferay.portal.SystemException;

    public void removeByResourceId(long resourceId)
        throws com.liferay.portal.SystemException;

    public void removeAll() throws com.liferay.portal.SystemException;

    public int countByTagNames(java.lang.String tagNames)
        throws com.liferay.portal.SystemException;

    public int countByResourceType(java.lang.String resourceType)
        throws com.liferay.portal.SystemException;

    public int countByResourceId(long resourceId)
        throws com.liferay.portal.SystemException;

    public int countAll() throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Cart> getCarts(long pk)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Cart> getCarts(long pk,
        int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Cart> getCarts(long pk,
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public int getCartsSize(long pk) throws com.liferay.portal.SystemException;

    public boolean containsCart(long pk, long cartPK)
        throws com.liferay.portal.SystemException;

    public boolean containsCarts(long pk)
        throws com.liferay.portal.SystemException;

    public void addCart(long pk, long cartPK)
        throws com.liferay.portal.SystemException;

    public void addCart(long pk, com.ext.portlet.cart.model.Cart cart)
        throws com.liferay.portal.SystemException;

    public void addCarts(long pk, long[] cartPKs)
        throws com.liferay.portal.SystemException;

    public void addCarts(long pk,
        java.util.List<com.ext.portlet.cart.model.Cart> carts)
        throws com.liferay.portal.SystemException;

    public void clearCarts(long pk) throws com.liferay.portal.SystemException;

    public void removeCart(long pk, long cartPK)
        throws com.liferay.portal.SystemException;

    public void removeCart(long pk, com.ext.portlet.cart.model.Cart cart)
        throws com.liferay.portal.SystemException;

    public void removeCarts(long pk, long[] cartPKs)
        throws com.liferay.portal.SystemException;

    public void removeCarts(long pk,
        java.util.List<com.ext.portlet.cart.model.Cart> carts)
        throws com.liferay.portal.SystemException;

    public void setCarts(long pk, long[] cartPKs)
        throws com.liferay.portal.SystemException;

    public void setCarts(long pk,
        java.util.List<com.ext.portlet.cart.model.Cart> carts)
        throws com.liferay.portal.SystemException;
}
