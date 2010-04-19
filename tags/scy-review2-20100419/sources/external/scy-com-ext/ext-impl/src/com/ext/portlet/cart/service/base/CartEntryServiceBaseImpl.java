package com.ext.portlet.cart.service.base;

import com.ext.portlet.cart.service.CartEntryLocalService;
import com.ext.portlet.cart.service.CartEntryService;
import com.ext.portlet.cart.service.CartLocalService;
import com.ext.portlet.cart.service.CartService;
import com.ext.portlet.cart.service.persistence.CartEntryPersistence;
import com.ext.portlet.cart.service.persistence.CartPersistence;

import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.BeanReference;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.service.UserService;
import com.liferay.portal.service.base.PrincipalBean;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.util.PortalUtil;


public abstract class CartEntryServiceBaseImpl extends PrincipalBean
    implements CartEntryService {
    @BeanReference(name = "com.ext.portlet.cart.service.CartEntryLocalService.impl")
    protected CartEntryLocalService cartEntryLocalService;
    @BeanReference(name = "com.ext.portlet.cart.service.CartEntryService.impl")
    protected CartEntryService cartEntryService;
    @BeanReference(name = "com.ext.portlet.cart.service.persistence.CartEntryPersistence.impl")
    protected CartEntryPersistence cartEntryPersistence;
    @BeanReference(name = "com.ext.portlet.cart.service.CartLocalService.impl")
    protected CartLocalService cartLocalService;
    @BeanReference(name = "com.ext.portlet.cart.service.CartService.impl")
    protected CartService cartService;
    @BeanReference(name = "com.ext.portlet.cart.service.persistence.CartPersistence.impl")
    protected CartPersistence cartPersistence;
    @BeanReference(name = "com.liferay.portal.service.UserLocalService.impl")
    protected UserLocalService userLocalService;
    @BeanReference(name = "com.liferay.portal.service.UserService.impl")
    protected UserService userService;
    @BeanReference(name = "com.liferay.portal.service.persistence.UserPersistence.impl")
    protected UserPersistence userPersistence;

    public CartEntryLocalService getCartEntryLocalService() {
        return cartEntryLocalService;
    }

    public void setCartEntryLocalService(
        CartEntryLocalService cartEntryLocalService) {
        this.cartEntryLocalService = cartEntryLocalService;
    }

    public CartEntryService getCartEntryService() {
        return cartEntryService;
    }

    public void setCartEntryService(CartEntryService cartEntryService) {
        this.cartEntryService = cartEntryService;
    }

    public CartEntryPersistence getCartEntryPersistence() {
        return cartEntryPersistence;
    }

    public void setCartEntryPersistence(
        CartEntryPersistence cartEntryPersistence) {
        this.cartEntryPersistence = cartEntryPersistence;
    }

    public CartLocalService getCartLocalService() {
        return cartLocalService;
    }

    public void setCartLocalService(CartLocalService cartLocalService) {
        this.cartLocalService = cartLocalService;
    }

    public CartService getCartService() {
        return cartService;
    }

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    public CartPersistence getCartPersistence() {
        return cartPersistence;
    }

    public void setCartPersistence(CartPersistence cartPersistence) {
        this.cartPersistence = cartPersistence;
    }

    public UserLocalService getUserLocalService() {
        return userLocalService;
    }

    public void setUserLocalService(UserLocalService userLocalService) {
        this.userLocalService = userLocalService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserPersistence getUserPersistence() {
        return userPersistence;
    }

    public void setUserPersistence(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    protected void runSQL(String sql) throws SystemException {
        try {
            PortalUtil.runSQL(sql);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }
}
