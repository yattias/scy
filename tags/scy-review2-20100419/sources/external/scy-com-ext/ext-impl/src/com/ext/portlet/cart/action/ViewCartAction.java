package com.ext.portlet.cart.action;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ext.portlet.cart.model.Cart;
import com.ext.portlet.cart.model.CartEntry;
import com.ext.portlet.cart.service.CartLocalServiceUtil;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.tags.model.TagsAsset;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.util.CartActionUtil;

/**
 * Render the cart portlet. Show all carts from user in session and the actual
 * not saved cart entries. The portlet has different views for normal window
 * state and maximized windows state. In normal windows state the carts are
 * arrange in table in other view the full content were shown.
 * 
 * @author Daniel
 * 
 */
public class ViewCartAction extends PortletAction {

	@SuppressWarnings("unchecked")
	public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse)
			throws Exception {

		boolean windowStateNormal;
		HttpSession oHttpSession = CartActionUtil.getUserSessionRenderRes(renderRequest);
		List<CartEntry> cartEntryList = (List<CartEntry>) oHttpSession.getAttribute("cartEntryList");
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		Long userId = themeDisplay.getUserId();

		// all cart entires
		List<Cart> cartList = CartLocalServiceUtil.getCarts(0, CartLocalServiceUtil.getCartsCount());

		if (cartEntryList == null) {
			cartEntryList = new ArrayList<CartEntry>();
		}
		List<TagsAsset> assetList = getAssetList(cartEntryList);
		List<Cart> userCartList = getUserCartList(userId, cartList);

		// variable for different views table or full content
		if (renderRequest.getWindowState().equals(WindowState.NORMAL)) {
			windowStateNormal = true;
		} else {
			windowStateNormal = false;
		}

		// set Attributes for render page
		renderRequest.setAttribute("assetList", assetList);
		renderRequest.setAttribute("cartEntryList", cartEntryList);
		renderRequest.setAttribute("cartList", userCartList);
		renderRequest.setAttribute("windowStateNormal", windowStateNormal);

		return mapping.findForward(getForward(renderRequest, "portlet.ext.cart.view_cart"));

	}

	/**
	 * Return only carts from user.
	 * 
	 * @param userId
	 *            id from user from actual theme display view
	 * @param cartList
	 *            list of all carts from portal
	 * @return only carts from user with given user id
	 */
	protected List<Cart> getUserCartList(Long userId, List<Cart> cartList) {
		List<Cart> userCartList = new ArrayList<Cart>();

		if (cartList.size() > 0) {

			for (Cart cart : cartList) {
				if (userId.equals(cart.getUserId())) {
					userCartList.add(cart);
				}
			}
		}
		return userCartList;
	}

	/**
	 * Get asset entries from cart entries for view_dyamic_list_asset.jspf
	 * 
	 * @param cartEntryList
	 *            all cartEntries from db
	 * @return list of asset entries for all cart entries
	 * @throws PortalException
	 * @throws SystemException
	 */
	protected List<TagsAsset> getAssetList(List<CartEntry> cartEntryList) throws PortalException, SystemException {
		List<TagsAsset> assetList = new ArrayList<TagsAsset>();
		for (CartEntry cartEntry : cartEntryList) {
			try{
				TagsAsset assetEntry = TagsAssetLocalServiceUtil.getAsset(cartEntry.getResourceType(), cartEntry.getResourceId());
				assetList.add(assetEntry);				
			}catch (Exception e) {
				
			}			
		}

		return assetList;
	}

	@Override
	protected boolean isCheckMethodOnProcessAction() {
		return _CHECK_METHOD_ON_PROCESS_ACTION;
	}

	private static final boolean _CHECK_METHOD_ON_PROCESS_ACTION = false;

}
