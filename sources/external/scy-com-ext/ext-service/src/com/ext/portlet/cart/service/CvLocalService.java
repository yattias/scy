package com.ext.portlet.cart.service;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.Isolation;
import com.liferay.portal.kernel.annotation.Propagation;
import com.liferay.portal.kernel.annotation.Transactional;


/**
 * <a href="CvLocalService.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface defines the service. The default implementation is
 * {@link
 * com.ext.portlet.cart.service.impl.CvLocalServiceImpl}}.
 * Modify methods in that class and rerun ServiceBuilder to populate this class
 * and all other generated classes.
 * </p>
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       CvLocalServiceUtil
 * @generated
 */
@Transactional(isolation = Isolation.PORTAL, rollbackFor =  {
    PortalException.class, SystemException.class}
)
public interface CvLocalService {
    public com.ext.portlet.cart.model.Cv addCv(com.ext.portlet.cart.model.Cv cv)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Cv createCv(java.lang.String cvId);

    public void deleteCv(java.lang.String cvId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException;

    public void deleteCv(com.ext.portlet.cart.model.Cv cv)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public com.ext.portlet.cart.model.Cv getCv(java.lang.String cvId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public java.util.List<com.ext.portlet.cart.model.Cv> getCvs(int start,
        int end) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int getCvsCount() throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Cv updateCv(
        com.ext.portlet.cart.model.Cv cv)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Cv updateCv(
        com.ext.portlet.cart.model.Cv cv, boolean merge)
        throws com.liferay.portal.SystemException;
}
