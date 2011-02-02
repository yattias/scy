package com.ext.portlet.cart.action;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ext.portlet.cart.model.CartEntry;

/**
 * remove all checked actual cart enties from session cartEntryList.
 * 
 * @author Daniel
 * 
 */
public class RemoveCartAction extends ViewCartAction {

	@SuppressWarnings("unchecked")
	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest req, ActionResponse res) throws Exception {

		String[] resourceId = req.getParameterValues("cartId");

		// get httpSession
		HttpSession oHttpSession = com.liferay.util.CartActionUtil.getUserSessionActionReq(req);
		List<CartEntry> cartEntryList = (List<CartEntry>) oHttpSession.getAttribute("cartEntryList");
		List<CartEntry> cartEntryRemoveList = new ArrayList<CartEntry>();

		if (resourceId == null) {
			req.setAttribute("cartEntryList", cartEntryList);
			setForward(req, "portlet.ext.cart.view_cart");
			return;
		}

		for (CartEntry cartEntry : cartEntryList) {
			for (int i = 0; i < resourceId.length; i++) {
				Long removeId = Long.valueOf(resourceId[i]);
				if (removeId.equals(cartEntry.getResourceId())) {
					cartEntryRemoveList.add(cartEntry);
				}
			}
		}
		for (CartEntry cartToRemove : cartEntryRemoveList) {
			cartEntryList.remove(cartToRemove);
		}

		// set Attributes for render page
		req.setAttribute("cartEntryList", cartEntryList);

		// set Forward
		setForward(req, "portlet.ext.cart.view_cart");

	}
}
