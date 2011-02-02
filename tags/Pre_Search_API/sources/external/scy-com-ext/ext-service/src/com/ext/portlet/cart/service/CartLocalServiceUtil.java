package com.ext.portlet.cart.service;


/**
 * <a href="CartLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * {@link CartLocalService} bean. The static methods of
 * this class calls the same methods of the bean instance. It's convenient to be
 * able to just write one line to call a method on a bean instead of writing a
 * lookup call and a method call.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       CartLocalService
 * @generated
 */
public class CartLocalServiceUtil {
    private static CartLocalService _service;

    public static com.ext.portlet.cart.model.Cart addCart(
        com.ext.portlet.cart.model.Cart cart)
        throws com.liferay.portal.SystemException {
        return getService().addCart(cart);
    }

    public static com.ext.portlet.cart.model.Cart createCart(long cartId) {
        return getService().createCart(cartId);
    }

    public static void deleteCart(long cartId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        getService().deleteCart(cartId);
    }

    public static void deleteCart(com.ext.portlet.cart.model.Cart cart)
        throws com.liferay.portal.SystemException {
        getService().deleteCart(cart);
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

    public static com.ext.portlet.cart.model.Cart getCart(long cartId)
        throws com.liferay.portal.PortalException,
            com.liferay.portal.SystemException {
        return getService().getCart(cartId);
    }

    public static java.util.List<com.ext.portlet.cart.model.Cart> getCarts(
        int start, int end) throws com.liferay.portal.SystemException {
        return getService().getCarts(start, end);
    }
    
    public static java.util.List<com.ext.portlet.cart.model.CartEntry> getCartEntries(
    		long pk) throws com.liferay.portal.SystemException {
    	return getService().getCartEntries(pk);
    }

    public static int getCartsCount() throws com.liferay.portal.SystemException {
        return getService().getCartsCount();
    }

    public static com.ext.portlet.cart.model.Cart updateCart(
        com.ext.portlet.cart.model.Cart cart)
        throws com.liferay.portal.SystemException {
        return getService().updateCart(cart);
    }

    public static com.ext.portlet.cart.model.Cart updateCart(
        com.ext.portlet.cart.model.Cart cart, boolean merge)
        throws com.liferay.portal.SystemException {
        return getService().updateCart(cart, merge);
    }

    public static CartLocalService getService() {
        if (_service == null) {
            throw new RuntimeException("CartLocalService is not set");
        }

        return _service;
    }

    public void setService(CartLocalService service) {
        _service = service;
    }
}
