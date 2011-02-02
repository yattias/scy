package com.ext.portlet.cart.service.persistence;


/**
 * <a href="DiplomeUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       DiplomePersistence
 * @see       DiplomePersistenceImpl
 * @generated
 */
public class DiplomeUtil {
    private static DiplomePersistence _persistence;

    public static void cacheResult(com.ext.portlet.cart.model.Diplome diplome) {
        getPersistence().cacheResult(diplome);
    }

    public static void cacheResult(
        java.util.List<com.ext.portlet.cart.model.Diplome> diplomes) {
        getPersistence().cacheResult(diplomes);
    }

    public static void clearCache() {
        getPersistence().clearCache();
    }

    public static com.ext.portlet.cart.model.Diplome create(
        java.lang.String diplomeId) {
        return getPersistence().create(diplomeId);
    }

    public static com.ext.portlet.cart.model.Diplome remove(
        java.lang.String diplomeId)
        throws com.ext.portlet.cart.NoSuchDiplomeException,
            com.liferay.portal.SystemException {
        return getPersistence().remove(diplomeId);
    }

    public static com.ext.portlet.cart.model.Diplome remove(
        com.ext.portlet.cart.model.Diplome diplome)
        throws com.liferay.portal.SystemException {
        return getPersistence().remove(diplome);
    }

    /**
     * @deprecated Use {@link #update(Diplome, boolean merge)}.
     */
    public static com.ext.portlet.cart.model.Diplome update(
        com.ext.portlet.cart.model.Diplome diplome)
        throws com.liferay.portal.SystemException {
        return getPersistence().update(diplome);
    }

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
    public static com.ext.portlet.cart.model.Diplome update(
        com.ext.portlet.cart.model.Diplome diplome, boolean merge)
        throws com.liferay.portal.SystemException {
        return getPersistence().update(diplome, merge);
    }

    public static com.ext.portlet.cart.model.Diplome updateImpl(
        com.ext.portlet.cart.model.Diplome diplome, boolean merge)
        throws com.liferay.portal.SystemException {
        return getPersistence().updateImpl(diplome, merge);
    }

    public static com.ext.portlet.cart.model.Diplome findByPrimaryKey(
        java.lang.String diplomeId)
        throws com.ext.portlet.cart.NoSuchDiplomeException,
            com.liferay.portal.SystemException {
        return getPersistence().findByPrimaryKey(diplomeId);
    }

    public static com.ext.portlet.cart.model.Diplome fetchByPrimaryKey(
        java.lang.String diplomeId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByPrimaryKey(diplomeId);
    }

    public static java.util.List<com.ext.portlet.cart.model.Diplome> findByCvId(
        java.lang.String cvId) throws com.liferay.portal.SystemException {
        return getPersistence().findByCvId(cvId);
    }

    public static java.util.List<com.ext.portlet.cart.model.Diplome> findByCvId(
        java.lang.String cvId, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByCvId(cvId, start, end);
    }

    public static java.util.List<com.ext.portlet.cart.model.Diplome> findByCvId(
        java.lang.String cvId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findByCvId(cvId, start, end, obc);
    }

    public static com.ext.portlet.cart.model.Diplome findByCvId_First(
        java.lang.String cvId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchDiplomeException,
            com.liferay.portal.SystemException {
        return getPersistence().findByCvId_First(cvId, obc);
    }

    public static com.ext.portlet.cart.model.Diplome findByCvId_Last(
        java.lang.String cvId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchDiplomeException,
            com.liferay.portal.SystemException {
        return getPersistence().findByCvId_Last(cvId, obc);
    }

    public static com.ext.portlet.cart.model.Diplome[] findByCvId_PrevAndNext(
        java.lang.String diplomeId, java.lang.String cvId,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.ext.portlet.cart.NoSuchDiplomeException,
            com.liferay.portal.SystemException {
        return getPersistence().findByCvId_PrevAndNext(diplomeId, cvId, obc);
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

    public static java.util.List<com.ext.portlet.cart.model.Diplome> findAll()
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll();
    }

    public static java.util.List<com.ext.portlet.cart.model.Diplome> findAll(
        int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end);
    }

    public static java.util.List<com.ext.portlet.cart.model.Diplome> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end, obc);
    }

    public static void removeByCvId(java.lang.String cvId)
        throws com.liferay.portal.SystemException {
        getPersistence().removeByCvId(cvId);
    }

    public static void removeAll() throws com.liferay.portal.SystemException {
        getPersistence().removeAll();
    }

    public static int countByCvId(java.lang.String cvId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByCvId(cvId);
    }

    public static int countAll() throws com.liferay.portal.SystemException {
        return getPersistence().countAll();
    }

    public static DiplomePersistence getPersistence() {
        return _persistence;
    }

    public void setPersistence(DiplomePersistence persistence) {
        _persistence = persistence;
    }
}
