package com.ext.portlet.cart.service;


/**
 * <a href="DiplomeLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * {@link DiplomeLocalService} bean. The static methods of
 * this class calls the same methods of the bean instance. It's convenient to be
 * able to just write one line to call a method on a bean instead of writing a
 * lookup call and a method call.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       DiplomeLocalService
 * @generated
 */
public class DiplomeLocalServiceUtil {
    private static DiplomeLocalService _service;

    public static com.ext.portlet.cart.model.Diplome addDiplome(
        com.ext.portlet.cart.model.Diplome diplome)
        throws com.liferay.portal.SystemException {
        return getService().addDiplome(diplome);
    }

    public static com.ext.portlet.cart.model.Diplome createDiplome(
        java.lang.String diplomeId) {
        return getService().createDiplome(diplomeId);
    }

    public static void deleteDiplome(java.lang.String diplomeId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        getService().deleteDiplome(diplomeId);
    }

    public static void deleteDiplome(com.ext.portlet.cart.model.Diplome diplome)
        throws com.liferay.portal.SystemException {
        getService().deleteDiplome(diplome);
    }

    public static java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException {
        return getService().dynamicQuery(dynamicQuery);
    }

    public static java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException {
        return getService().dynamicQuery(dynamicQuery, start, end);
    }

    public static com.ext.portlet.cart.model.Diplome getDiplome(
        java.lang.String diplomeId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        return getService().getDiplome(diplomeId);
    }

    public static java.util.List<com.ext.portlet.cart.model.Diplome> getDiplomes(
        int start, int end) throws com.liferay.portal.SystemException {
        return getService().getDiplomes(start, end);
    }

    public static int getDiplomesCount()
        throws com.liferay.portal.SystemException {
        return getService().getDiplomesCount();
    }

    public static com.ext.portlet.cart.model.Diplome updateDiplome(
        com.ext.portlet.cart.model.Diplome diplome)
        throws com.liferay.portal.SystemException {
        return getService().updateDiplome(diplome);
    }

    public static com.ext.portlet.cart.model.Diplome updateDiplome(
        com.ext.portlet.cart.model.Diplome diplome, boolean merge)
        throws com.liferay.portal.SystemException {
        return getService().updateDiplome(diplome, merge);
    }

    public static DiplomeLocalService getService() {
        if (_service == null) {
            throw new RuntimeException("DiplomeLocalService is not set");
        }

        return _service;
    }

    public void setService(DiplomeLocalService service) {
        _service = service;
    }
}
