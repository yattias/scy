package com.ext.portlet.cart.service;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.Isolation;
import com.liferay.portal.kernel.annotation.Propagation;
import com.liferay.portal.kernel.annotation.Transactional;


/**
 * <a href="DiplomeLocalService.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface defines the service. The default implementation is
 * {@link
 * com.ext.portlet.cart.service.impl.DiplomeLocalServiceImpl}}.
 * Modify methods in that class and rerun ServiceBuilder to populate this class
 * and all other generated classes.
 * </p>
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       DiplomeLocalServiceUtil
 * @generated
 */
@Transactional(isolation = Isolation.PORTAL, rollbackFor =  {
    PortalException.class, SystemException.class}
)
public interface DiplomeLocalService {
    public com.ext.portlet.cart.model.Diplome addDiplome(
        com.ext.portlet.cart.model.Diplome diplome)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Diplome createDiplome(
        java.lang.String diplomeId);

    public void deleteDiplome(java.lang.String diplomeId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException;

    public void deleteDiplome(com.ext.portlet.cart.model.Diplome diplome)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public com.ext.portlet.cart.model.Diplome getDiplome(
        java.lang.String diplomeId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public java.util.List<com.ext.portlet.cart.model.Diplome> getDiplomes(
        int start, int end) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int getDiplomesCount() throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Diplome updateDiplome(
        com.ext.portlet.cart.model.Diplome diplome)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.Diplome updateDiplome(
        com.ext.portlet.cart.model.Diplome diplome, boolean merge)
        throws com.liferay.portal.SystemException;
}
