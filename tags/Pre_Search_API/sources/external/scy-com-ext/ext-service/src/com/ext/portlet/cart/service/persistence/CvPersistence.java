package com.ext.portlet.cart.service.persistence;

import com.liferay.portal.service.persistence.BasePersistence;


/**
 * <a href="CvPersistence.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       CvPersistenceImpl
 * @see       CvUtil
 * @generated
 */
public interface CvPersistence extends BasePersistence {
    public void cacheResult(com.ext.portlet.cart.model.Cv cv);

    public void cacheResult(java.util.List<com.ext.portlet.cart.model.Cv> cvs);

    public void clearCache();

    public com.ext.portlet.cart.model.Cv create(java.lang.String cvId);

    public com.ext.portlet.cart.model.Cv remove(java.lang.String cvId)
        throws com.ext.portlet.cart.NoSuchCvException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Cv remove(
        com.ext.portlet.cart.model.Cv cv)
        throws com.liferay.portal.SystemException;

    /**
     * @deprecated Use {@link #update(Cv, boolean merge)}.
     */
    public com.ext.portlet.cart.model.Cv update(
        com.ext.portlet.cart.model.Cv cv)
        throws com.liferay.portal.SystemException;

    /**
     * Add, update, or merge, the entity. This method also calls the model
     * listeners to trigger the proper events associated with adding, deleting,
     * or updating an entity.
     *
     * @param  cv the entity to add, update, or merge
     * @param  merge boolean value for whether to merge the entity. The default
     *         value is false. Setting merge to true is more expensive and
     *         should only be true when cv is transient. See
     *         LEP-5473 for a detailed discussion of this method.
     * @return the entity that was added, updated, or merged
     */
    public com.ext.portlet.cart.model.Cv update(
        com.ext.portlet.cart.model.Cv cv, boolean merge)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Cv updateImpl(
        com.ext.portlet.cart.model.Cv cv, boolean merge)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Cv findByPrimaryKey(java.lang.String cvId)
        throws com.ext.portlet.cart.NoSuchCvException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Cv fetchByPrimaryKey(
        java.lang.String cvId) throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Cv findByUserId(java.lang.String userId)
        throws com.ext.portlet.cart.NoSuchCvException,
            com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Cv fetchByUserId(java.lang.String userId)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Cv fetchByUserId(
        java.lang.String userId, boolean retrieveFromCache)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> findWithDynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Cv> findAll()
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Cv> findAll(int start,
        int end) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Cv> findAll(int start,
        int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public void removeByUserId(java.lang.String userId)
        throws com.ext.portlet.cart.NoSuchCvException,
            com.liferay.portal.SystemException;

    public void removeAll() throws com.liferay.portal.SystemException;

    public int countByUserId(java.lang.String userId)
        throws com.liferay.portal.SystemException;

    public int countAll() throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Diplome> getDiplomes(
        java.lang.String pk) throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Diplome> getDiplomes(
        java.lang.String pk, int start, int end)
        throws com.liferay.portal.SystemException;

    public java.util.List<com.ext.portlet.cart.model.Diplome> getDiplomes(
        java.lang.String pk, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException;

    public int getDiplomesSize(java.lang.String pk)
        throws com.liferay.portal.SystemException;

    public boolean containsDiplome(java.lang.String pk,
        java.lang.String diplomePK) throws com.liferay.portal.SystemException;

    public boolean containsDiplomes(java.lang.String pk)
        throws com.liferay.portal.SystemException;
}
