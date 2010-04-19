package com.ext.portlet.cart.action;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ext.portlet.cart.model.Cart;
import com.ext.portlet.cart.model.CartEntry;
import com.ext.portlet.cart.service.CartLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.tags.model.TagsAsset;
import com.liferay.util.CartActionUtil;

/**
 * Method to get all information to view chosen cart.
 * 
 * @author Daniel
 * 
 */
public class ViewCartEntryListAction extends ViewCartAction {

	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest req, ActionResponse res) throws Exception {

		long entryId = Long.valueOf(req.getParameter("entryId"));
		ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
		Long userId = themeDisplay.getUserId();
		List<CartEntry> cartEntryList = CartActionUtil.getCartEntries(entryId, userId);
		List<Cart> cartList = CartLocalServiceUtil.getCarts(0, CartLocalServiceUtil.getCartsCount());

		if (cartEntryList == null) {
			cartEntryList = new ArrayList<CartEntry>();
		}

		List<TagsAsset> assetList = getAssetList(cartEntryList);
		List<Cart> userCartList = getUserCartList(userId, cartList);

		// set Attributes for render page
		req.setAttribute("cartEntryList2", cartEntryList);
		req.setAttribute("assetList2", assetList);
		req.setAttribute("cartList2", userCartList);
		req.setAttribute("actualCart", CartLocalServiceUtil.getCart(entryId));

		// set Forward
		setForward(req, "portlet.ext.cart.view_cartListEntry");
	}

	@Override
	protected boolean isCheckMethodOnProcessAction() {
		return _CHECK_METHOD_ON_PROCESS_ACTION;
	}

	private static final boolean _CHECK_METHOD_ON_PROCESS_ACTION = false;
}
