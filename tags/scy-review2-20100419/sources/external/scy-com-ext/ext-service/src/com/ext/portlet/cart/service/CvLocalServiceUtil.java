package com.ext.portlet.cart.service;


/**
 * <a href="CvLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * {@link CvLocalService} bean. The static methods of
 * this class calls the same methods of the bean instance. It's convenient to be
 * able to just write one line to call a method on a bean instead of writing a
 * lookup call and a method call.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       CvLocalService
 * @generated
 */
public class CvLocalServiceUtil {
    private static CvLocalService _service;

    public static com.ext.portlet.cart.model.Cv addCv(
        com.ext.portlet.cart.model.Cv cv)
        throws com.liferay.portal.SystemException {
        return getService().addCv(cv);
    }

    public static com.ext.portlet.cart.model.Cv createCv(java.lang.String cvId) {
        return getService().createCv(cvId);
    }

    public static void deleteCv(java.lang.String cvId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        getService().deleteCv(cvId);
    }

    public static void deleteCv(com.ext.portlet.cart.model.Cv cv)
        throws com.liferay.portal.SystemException {
        getService().deleteCv(cv);
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

    public static com.ext.portlet.cart.model.Cv getCv(java.lang.String cvId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        return getService().getCv(cvId);
    }

    public static java.util.List<com.ext.portlet.cart.model.Cv> getCvs(
        int start, int end) throws com.liferay.portal.SystemException {
        return getService().getCvs(start, end);
    }

    public static int getCvsCount() throws com.liferay.portal.SystemException {
        return getService().getCvsCount();
    }

    public static com.ext.portlet.cart.model.Cv updateCv(
        com.ext.portlet.cart.model.Cv cv)
        throws com.liferay.portal.SystemException {
        return getService().updateCv(cv);
    }

    public static com.ext.portlet.cart.model.Cv updateCv(
        com.ext.portlet.cart.model.Cv cv, boolean merge)
        throws com.liferay.portal.SystemException {
        return getService().updateCv(cv, merge);
    }

    public static CvLocalService getService() {
        if (_service == null) {
            throw new RuntimeException("CvLocalService is not set");
        }

        return _service;
    }

    public void setService(CvLocalService service) {
        _service = service;
    }
}
