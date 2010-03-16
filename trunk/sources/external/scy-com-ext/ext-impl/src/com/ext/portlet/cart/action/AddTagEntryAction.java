package com.ext.portlet.cart.action;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ext.portlet.cart.model.Cart;
import com.ext.portlet.cart.model.CartEntry;
import com.ext.portlet.cart.service.CartLocalServiceUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.tags.model.TagsAsset;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.util.CartActionUtil;
import com.liferay.util.TagActionUtil;

/**
 * This class adds new tags to an exist tagsAsset. It is fire after pressing the
 * the taglib ui add own tags. Afer the user enter his tags and press save
 * button, this class persist the tags.
 * 
 * @author Daniel
 * 
 */

public class AddTagEntryAction extends ViewCartAction {

	public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest req, ActionResponse res) throws Exception {

		long entryId = Long.valueOf(req.getParameter("entryId"));
		String cmd = req.getParameter("cmd");
		String className = req.getParameter("className");
		String struts_action = req.getParameter("struts_action");

		TagsAsset entry = TagsAssetLocalServiceUtil.getAsset(className, entryId);

		ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
		Long userId = themeDisplay.getUserId();
		List<CartEntry> cartEntryList = CartActionUtil.getCartEntries(entryId, userId);
		List<Cart> cartList = CartLocalServiceUtil.getCarts(0, CartLocalServiceUtil.getCartsCount());

		List<TagsAsset> assetList = getAssetList(cartEntryList);
		List<Cart> userCartList = getUserCartList(userId, cartList);

		if (cmd.equals(Constants.VIEW)) {
			// set Attributes for render page
			req.setAttribute("entryId", entryId);
			req.setAttribute("assetEntry:view_addTag.jsp", entry);
			req.setAttribute("className", className);
			req.setAttribute("struts_action", struts_action);
			req.setAttribute("cartEntryList2", cartEntryList);
			req.setAttribute("assetList2", assetList);
			req.setAttribute("cartList2", userCartList);
			req.setAttribute("actualCart", CartLocalServiceUtil.getCart(entryId));
			setForward(req, "portlet.ext.cart.view_addTag");
			return;
		}

		TagActionUtil.addNewTags(req, entry.getAssetId());

		// set Attributes for render page
		req.setAttribute("cartEntryList2", cartEntryList);
		req.setAttribute("assetList2", assetList);
		req.setAttribute("cartList2", userCartList);
		req.setAttribute("actualCart", CartLocalServiceUtil.getCart(entryId));
		req.setAttribute("entryId", entryId);
		req.setAttribute("cart", entry);

		// set Forward
		setForward(req, "portlet.ext.cart.view_cartListEntry");
	}
}
