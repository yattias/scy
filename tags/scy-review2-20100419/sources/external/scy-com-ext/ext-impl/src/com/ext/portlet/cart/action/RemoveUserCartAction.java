package com.ext.portlet.cart.action;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ext.portlet.cart.model.CartEntry;
import com.ext.portlet.cart.service.CartEntryLocalServiceUtil;
import com.ext.portlet.cart.service.CartLocalServiceUtil;
import com.ext.portlet.cart.service.persistence.CartUtil;
import com.liferay.portal.util.WebKeys;

/**
 * remove all db entries for chosen cart.
 * 
 * @author Daniel
 * 
 */
public class RemoveUserCartAction extends ViewCartAction {

	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest req, ActionResponse res) throws Exception {

		String currentURL = (String)req.getAttribute(
				WebKeys.CURRENT_URL);
		long entryId = Long.valueOf(req.getParameter("entryId"));
		String redirect;
		
		List<CartEntry> cartEntryList = CartLocalServiceUtil.getCartEntries(entryId);
		if (!cartEntryList.isEmpty()) {
			for (CartEntry cartEntry : cartEntryList) {
				CartEntryLocalServiceUtil.deleteCartEntry(cartEntry);
			}

		}
		CartUtil.removeCartEntries(entryId, cartEntryList);
		CartLocalServiceUtil.deleteCart(entryId);

		if (currentURL != null) {
			String[] newRed = currentURL.split("\\?", 2);
			currentURL = newRed[0];
		}
		redirect = currentURL;
		// set Forward
		res.sendRedirect(redirect);
	}
}
