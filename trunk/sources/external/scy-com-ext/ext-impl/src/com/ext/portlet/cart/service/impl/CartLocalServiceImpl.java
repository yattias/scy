package com.ext.portlet.cart.service.impl;

import java.util.List;

import com.ext.portlet.cart.model.CartEntry;
import com.ext.portlet.cart.service.base.CartLocalServiceBaseImpl;
import com.ext.portlet.cart.service.persistence.CartPersistence;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.annotation.BeanReference;

public class CartLocalServiceImpl extends CartLocalServiceBaseImpl {
	@BeanReference(name = "com.ext.portlet.cart.service.persistence.CartPersistence.impl")
	protected CartPersistence cartPersistence;

	@Override
	public List<CartEntry> getCartEntries(long pk) throws SystemException {
		// TODO Auto-generated method stub;
		return cartPersistence.getCartEntries(pk);
	}

}
