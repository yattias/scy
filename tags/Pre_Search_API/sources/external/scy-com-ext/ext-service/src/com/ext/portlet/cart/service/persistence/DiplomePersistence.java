package com.ext.portlet.cart.service.persistence;

import com.liferay.portal.service.persistence.BasePersistence;


/**
 * <a href="DiplomePersistence.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       DiplomePersistenceImpl
 * @see       DiplomeUtil
 * @generated
 */
public interface DiplomePersistence extends BasePersistence {
    public void cacheResult(com.ext.portlet.cart.model.Diplome diplome);

    public void cacheResult(
        java.util.List<com.ext.portlet.cart.model.Diplome> diplomes);

    public void clearCache();

    public com.ext.portlet.cart.model.Diplome create(java.lang.String diplomeId);

    public com.ext.portlet.cart.model.Diplome remove(java.lang.String diplomeId)
        throws com.ext.portlet.cart.NoSuchDiplomeException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Diplome remove(
        com.ext.portlet.cart.model.Diplome diplome)
        throws com.liferay.portal.SystemException;

    /**
     * @deprecated Use {@link #update(Diplome, boolean merge)}.
     */
    public com.ext.portlet.cart.model.Diplome update(
        com.ext.portlet.cart.model.Diplome diplome)
        throws com.liferay.portal.SystemException;

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param  diplome the entity to add, update, or merge
     * @param  merge boolean value for whether to merge the entity. The default
     *         value is false. Setting merge to true is more expensive and
     *         should only be true when diplome is transient. See
     *         LEP-5473 for a detailed discussion of this method.
     * @return the entity that was added, updated, or merged
     */
    public com.ext.portlet.cart.model.Diplome update(
        com.ext.portlet.cart.model.Diplome diplome, boolean merge)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Diplome updateImpl(
        com.ext.portlet.cart.model.Diplome diplome, boolean merge)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Diplome findByPrimaryKey(
        java.lang.String diplomeId)
        throws com.ext.portlet.cart.NoSuchDiplomeException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Diplome fetchByPrimaryKey(
        java.lang.String diplomeId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Diplome> findByCvId(
        java.lang.String cvId) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Diplome> findByCvId(
        java.lang.String cvId, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Diplome> findByCvId(
        java.lang.String cvId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Diplome findByCvId_First(
        java.lang.String cvId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchDiplomeException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Diplome findByCvId_Last(
        java.lang.String cvId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchDiplomeException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Diplome[] findByCvId_PrevAndNext(
        java.lang.String diplomeId, java.lang.String cvId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchDiplomeException,
            com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Diplome> findAll()
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Diplome> findAll(
        int start, int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Diplome> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public void removeByCvId(java.lang.String cvId)
        throws com.liferay.portal.SystemException;

    public void removeAll() throws com.liferay.portal.SystemException;

    public int countByCvId(java.lang.String cvId)
        throws com.liferay.portal.SystemException;

    public int countAll() throws com.liferay.portal.SystemException;
}
