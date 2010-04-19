package com.ext.portlet.cart.service.persistence;


/**
 * <a href="CvUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       CvPersistence
 * @see       CvPersistenceImpl
 * @generated
 */
public class CvUtil {
    private static CvPersistence _persistence;

    public static void cacheResult(com.ext.portlet.cart.model.Cv cv) {
        getPersistence().cacheResult(cv);
    }

    public static void cacheResult(
        java.util.List<com.ext.portlet.cart.model.Cv> cvs) {
        getPersistence().cacheResult(cvs);
    }

    public static void clearCache() {
        getPersistence().clearCache();
    }

    public static com.ext.portlet.cart.model.Cv create(java.lang.String cvId) {
        return getPersistence().create(cvId);
    }

    public static com.ext.portlet.cart.model.Cv remove(java.lang.String cvId)
        throws com.ext.portlet.cart.NoSuchCvException,
            com.liferay.portal.SystemException {
        return getPersistence().remove(cvId);
    }

    public static com.ext.portlet.cart.model.Cv remove(
        com.ext.portlet.cart.model.Cv cv)
        throws com.liferay.portal.SystemException {
        return getPersistence().remove(cv);
    }

    /**
     * @deprecated Use {@link #update(Cv, boolean merge)}.
     */
    public static com.ext.portlet.cart.model.Cv update(
        com.ext.portlet.cart.model.Cv cv)
        throws com.liferay.portal.SystemException {
        return getPersistence().update(cv);
    }

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
    public static com.ext.portlet.cart.model.Cv update(
        com.ext.portlet.cart.model.Cv cv, boolean merge)
        throws com.liferay.portal.SystemException {
        return getPersistence().update(cv, merge);
    }

    public static com.ext.portlet.cart.model.Cv updateImpl(
        com.ext.portlet.cart.model.Cv cv, boolean merge)
        throws com.liferay.portal.SystemException {
        return getPersistence().updateImpl(cv, merge);
    }

    public static com.ext.portlet.cart.model.Cv findByPrimaryKey(
        java.lang.String cvId)
        throws com.ext.portlet.cart.NoSuchCvException,
            com.liferay.portal.SystemException {
        return getPersistence().findByPrimaryKey(cvId);
    }

    public static com.ext.portlet.cart.model.Cv fetchByPrimaryKey(
        java.lang.String cvId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByPrimaryKey(cvId);
    }

    public static com.ext.portlet.cart.model.Cv findByUserId(
        java.lang.String userId)
        throws com.ext.portlet.cart.NoSuchCvException,
            com.liferay.portal.SystemException {
        return getPersistence().findByUserId(userId);
    }

    public static com.ext.portlet.cart.model.Cv fetchByUserId(
        java.lang.String userId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByUserId(userId);
    }

    public static com.ext.portlet.cart.model.Cv fetchByUserId(
        java.lang.String userId, boolean retrieveFromCache)
        throws com.liferay.portal.SystemException {
        return getPersistence().fetchByUserId(userId, retrieveFromCache);
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

    public static java.util.List<com.ext.portlet.cart.model.Cv> findAll()
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll();
    }

    public static java.util.List<com.ext.portlet.cart.model.Cv> findAll(
        int start, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end);
    }

    public static java.util.List<com.ext.portlet.cart.model.Cv> findAll(
        int start, int end, com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().findAll(start, end, obc);
    }

    public static void removeByUserId(java.lang.String userId)
        throws com.ext.portlet.cart.NoSuchCvException,
            com.liferay.portal.SystemException {
        getPersistence().removeByUserId(userId);
    }

    public static void removeAll() throws com.liferay.portal.SystemException {
        getPersistence().removeAll();
    }

    public static int countByUserId(java.lang.String userId)
        throws com.liferay.portal.SystemException {
        return getPersistence().countByUserId(userId);
    }

    public static int countAll() throws com.liferay.portal.SystemException {
        return getPersistence().countAll();
    }

    public static java.util.List<com.ext.portlet.cart.model.Diplome> getDiplomes(
        java.lang.String pk) throws com.liferay.portal.SystemException {
        return getPersistence().getDiplomes(pk);
    }

    public static java.util.List<com.ext.portlet.cart.model.Diplome> getDiplomes(
        java.lang.String pk, int start, int end)
        throws com.liferay.portal.SystemException {
        return getPersistence().getDiplomes(pk, start, end);
    }

    public static java.util.List<com.ext.portlet.cart.model.Diplome> getDiplomes(
        java.lang.String pk, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator obc)
        throws com.liferay.portal.SystemException {
        return getPersistence().getDiplomes(pk, start, end, obc);
    }

    public static int getDiplomesSize(java.lang.String pk)
        throws com.liferay.portal.SystemException {
        return getPersistence().getDiplomesSize(pk);
    }

    public static boolean containsDiplome(java.lang.String pk,
        java.lang.String diplomePK) throws com.liferay.portal.SystemException {
        return getPersistence().containsDiplome(pk, diplomePK);
    }

    public static boolean containsDiplomes(java.lang.String pk)
        throws com.liferay.portal.SystemException {
        return getPersistence().containsDiplomes(pk);
    }

    public static CvPersistence getPersistence() {
        return _persistence;
    }

    public void setPersistence(CvPersistence persistence) {
        _persistence = persistence;
    }
}
