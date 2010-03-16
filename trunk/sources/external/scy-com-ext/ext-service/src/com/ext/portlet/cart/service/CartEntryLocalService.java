package com.ext.portlet.cart.service;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.Isolation;
import com.liferay.portal.kernel.annotation.Propagation;
import com.liferay.portal.kernel.annotation.Transactional;


/**
 * <a href="CartEntryLocalService.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface defines the service. The default implementation is
 * {@link
 * com.ext.portlet.cart.service.impl.CartEntryLocalServiceImpl}}.
 * Modify methods in that class and rerun ServiceBuilder to populate this class
 * and all other generated classes.
 * </p>
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       CartEntryLocalServiceUtil
 * @generated
 */
@Transactional(isolation = Isolation.PORTAL, rollbackFor =  {
    PortalException.class, SystemException.class}
)
public interface CartEntryLocalService {
    public com.ext.portlet.cart.model.CartEntry addCartEntry(
        com.ext.portlet.cart.model.CartEntry cartEntry)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.CartEntry createCartEntry(
        long cartEntryId);

    public void deleteCartEntry(long cartEntryId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException;

    public void deleteCartEntry(com.ext.portlet.cart.model.CartEntry cartEntry)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.SystemException;

    public java.util.List<Object> dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public com.ext.portlet.cart.model.CartEntry getCartEntry(long cartEntryId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public java.util.List<com.ext.portlet.cart.model.CartEntry> getCartEntries(
        int start, int end) throws com.liferay.portal.SystemException;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int getCartEntriesCount() throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.CartEntry updateCartEntry(
        com.ext.portlet.cart.model.CartEntry cartEntry)
        throws com.liferay.portal.SystemException;

    public com.ext.portlet.cart.model.CartEntry updateCartEntry(
        com.ext.portlet.cart.model.CartEntry cartEntry, boolean merge)
        throws com.liferay.portal.SystemException;
}
