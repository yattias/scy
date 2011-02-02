package com.ext.portlet.cart.service;


/**
 * <a href="CartEntryLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * {@link CartEntryLocalService} bean. The static methods of
 * this class calls the same methods of the bean instance. It's convenient to be
 * able to just write one line to call a method on a bean instead of writing a
 * lookup call and a method call.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       CartEntryLocalService
 * @generated
 */
public class CartEntryLocalServiceUtil {
    private static CartEntryLocalService _service;

    public static com.ext.portlet.cart.model.CartEntry addCartEntry(
        com.ext.portlet.cart.model.CartEntry cartEntry)
        throws com.liferay.portal.SystemException {
        return getService().addCartEntry(cartEntry);
    }

    public static com.ext.portlet.cart.model.CartEntry createCartEntry(
        long cartEntryId) {
        return getService().createCartEntry(cartEntryId);
    }

    public static void deleteCartEntry(long cartEntryId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        getService().deleteCartEntry(cartEntryId);
    }

    public static void deleteCartEntry(
        com.ext.portlet.cart.model.CartEntry cartEntry)
        throws com.liferay.portal.SystemException {
        getService().deleteCartEntry(cartEntry);
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

    public static com.ext.portlet.cart.model.CartEntry getCartEntry(
        long cartEntryId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        return getService().getCartEntry(cartEntryId);
    }

    public static java.util.List<com.ext.portlet.cart.model.CartEntry> getCartEntries(
        int start, int end) throws com.liferay.portal.SystemException {
        return getService().getCartEntries(start, end);
    }

    public static int getCartEntriesCount()
        throws com.liferay.portal.SystemException {
        return getService().getCartEntriesCount();
    }

    public static com.ext.portlet.cart.model.CartEntry updateCartEntry(
        com.ext.portlet.cart.model.CartEntry cartEntry)
        throws com.liferay.portal.SystemException {
        return getService().updateCartEntry(cartEntry);
    }

    public static com.ext.portlet.cart.model.CartEntry updateCartEntry(
        com.ext.portlet.cart.model.CartEntry cartEntry, boolean merge)
        throws com.liferay.portal.SystemException {
        return getService().updateCartEntry(cartEntry, merge);
    }

    public static CartEntryLocalService getService() {
        if (_service == null) {
            throw new RuntimeException("CartEntryLocalService is not set");
        }

        return _service;
    }

    public void setService(CartEntryLocalService service) {
        _service = service;
    }
}
