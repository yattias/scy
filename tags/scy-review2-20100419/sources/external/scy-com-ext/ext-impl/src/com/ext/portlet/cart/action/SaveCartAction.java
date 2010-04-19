package com.ext.portlet.cart.action;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ext.portlet.cart.model.CartEntry;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.CartActionUtil;

/**
 * Save actual cart into db and clear actual session cart entries.
 * 
 * @author Daniel
 * 
 */
public class SaveCartAction extends ViewCartAction {

	@SuppressWarnings("unchecked")
	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest req, ActionResponse res) throws Exception {

		// get httpSession
		HttpSession oHttpSession = com.liferay.util.CartActionUtil.getUserSessionActionReq(req);
		List<CartEntry> cartEntryList = (List<CartEntry>) oHttpSession.getAttribute("cartEntryList");
		String redirect = ParamUtil.getString(req, "redirect");

		CartActionUtil.saveCartAndCartEntries(req, cartEntryList);
		cartEntryList.clear();

		// set Attributes for render page
		req.setAttribute("cartEntryList", cartEntryList);

		if (redirect != null) {
			String[] newRed = redirect.split("\\?", 2);
			redirect = newRed[0];
		}
		// set Forward
		res.sendRedirect(redirect);
	}
}
