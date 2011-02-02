package com.liferay.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.portlet.ActionRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.servlet.http.HttpSession;

import com.ext.portlet.cart.model.Cart;
import com.ext.portlet.cart.model.CartEntry;
import com.ext.portlet.cart.service.CartEntryLocalServiceUtil;
import com.ext.portlet.cart.service.CartLocalServiceUtil;
import com.ext.portlet.cart.service.persistence.CartUtil;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.ActionRequestImpl;
import com.liferay.portlet.PortletSessionImpl;
import com.liferay.portlet.tags.model.TagsAsset;
import com.liferay.portlet.tags.model.TagsEntry;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.portlet.tags.service.TagsAssetServiceUtil;
import com.liferay.portlet.tags.service.TagsEntryLocalServiceUtil;
import com.liferay.portlet.tags.service.TagsEntryServiceUtil;
import com.liferay.portlet.tags.service.persistence.TagsAssetUtil;

/**
 * Util class for cart portlet.
 * 
 * @author Daniel
 * 
 */
public class CartActionUtil {

	/**
	 * Get httpSession from actionRequest to add data to user session.
	 * 
	 * @param req
	 * @return httpSession with user session.
	 */
	public static HttpSession getUserSessionActionReq(ActionRequest req) {
		ActionRequestImpl oActionRequestImpl = (ActionRequestImpl) req;
		PortletSession oPortletSession = oActionRequestImpl.getPortletSession(false);
		PortletSessionImpl oPortletSessionImpl = (PortletSessionImpl) oPortletSession;
		HttpSession oHttpSession = oPortletSessionImpl.getHttpSession();
		return oHttpSession;
	}

	/**
	 * Get httpSession from renderReq get data from user session.
	 * 
	 * @param req
	 * @return httpSession with user session.
	 */
	public static HttpSession getUserSessionRenderRes(RenderRequest renderReq) {
		RenderRequest oActionRequestImpl = (RenderRequest) renderReq;
		PortletSession oPortletSession = oActionRequestImpl.getPortletSession(false);
		PortletSessionImpl oPortletSessionImpl = (PortletSessionImpl) oPortletSession;
		HttpSession oHttpSession = oPortletSessionImpl.getHttpSession();
		return oHttpSession;
	}

	/**
	 * Create new cart entry.
	 * 
	 * @param req
	 * @param className
	 *            the class name from given resource.
	 * @param classPk
	 *            the id from given resource.
	 * @return created cart entry.
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static CartEntry cartEntryElement(ActionRequest req, String className, Long classPk) throws PortalException, SystemException {
		ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
		long userId = themeDisplay.getUserId();
		User user = UserLocalServiceUtil.getUser(userId);
		Date now = new Date();
		Random r = new Random();

		CartEntry cartEntry = CartEntryLocalServiceUtil.createCartEntry(0);
		cartEntry.setCreateDate(now);
		cartEntry.setModifiedDate(now);
		cartEntry.setGroupId(themeDisplay.getScopeGroupId());
		cartEntry.setCompanyId(themeDisplay.getCompanyId());
		cartEntry.setResourceId(classPk);
		cartEntry.setResourceType(className);
		cartEntry.setUserId(userId);
		cartEntry.setUserName(user.getScreenName());
		cartEntry.setPrimaryKey(r.nextInt(1000000000));

		return cartEntry;

	}

	/**
	 * Add new cart element to cartEntry list in user session.
	 * 
	 * @param req
	 * @param cartEntry
	 *            new added cart entry.
	 */
	public static void addNewAddToCartElement(ActionRequest req, CartEntry cartEntry) {
		// get httpSession
		HttpSession oHttpSession = com.liferay.util.CartActionUtil.getUserSessionActionReq(req);

		List<CartEntry> cartEntryList = (List<CartEntry>) oHttpSession.getAttribute("cartEntryList");

		// instantiate new cartEntryList if no element insert yet
		if (cartEntryList == null) {
			cartEntryList = new ArrayList<CartEntry>();
		}
		// check that no cartEntry exist for this ressourceId
		for (CartEntry cartEntryCheck : cartEntryList) {
			Long checkId = cartEntryCheck.getResourceId();
			if (checkId.equals(cartEntry.getResourceId())) {
				return;
			}
		}
		cartEntryList.add(cartEntry);

		// set cart object to session
		oHttpSession.setAttribute("cartEntryList", cartEntryList);

	}

	/**
	 * Persist the actual cart entry list from user session. After persist the
	 * user session cart entry list will be cleared.
	 * 
	 * @param req
	 * @param cartEntryList
	 *            cart list from user session.
	 * @throws Exception
	 */
	public static void saveCartAndCartEntries(ActionRequest req, List<CartEntry> cartEntryList) throws Exception {

		Date now = new Date();
		ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
		long userId = themeDisplay.getUserId();
		String title = req.getParameter("title");
		int cartEntryCount = CartEntryLocalServiceUtil.getCartEntriesCount();
		int assetCount = TagsAssetLocalServiceUtil.getTagsAssetsCount();
		long cartId;
		long cartEntryId;
		// get maxId
		if (assetCount > 0) {
			TagsAsset maxCart = (TagsAsset) TagsAssetLocalServiceUtil.getTagsAssets(assetCount - 1, assetCount).get(0);
			cartId = maxCart.getAssetId() + 1;
		} else {
			cartId = 1;

		}
		if (cartEntryCount > 0) {
			CartEntry maxCartEntry = CartEntryLocalServiceUtil.getCartEntries(0, cartEntryCount).get(cartEntryCount - 1);
			cartEntryId = maxCartEntry.getCartEntryId() + 1;
		} else {
			cartEntryId = 1;
		}
		Cart cart = saveCart(now, themeDisplay, userId, title, cartId);

		TagsAsset tagsAsset = saveTagsAssetEntry(now, themeDisplay, title, cart);

		saveCartTags(req, themeDisplay, tagsAsset);

		for (CartEntry cartEntry : cartEntryList) {
			cartEntryId = saveCartEntry(now, cartEntryId, cart, cartEntry);

		}
	}

	/**
	 * Save tags which user has enter for actual cart.
	 * 
	 * @param req
	 * @param themeDisplay
	 * @param tagsAsset
	 *            saved tagsAsset entry for cart.
	 * @throws PortalException
	 * @throws SystemException
	 */
	private static void saveCartTags(ActionRequest req, ThemeDisplay themeDisplay, TagsAsset tagsAsset) throws PortalException, SystemException {
		String parentEntryName = "";
		String name;
		String vocabularyName = "";
		String[] properties = {};
		ServiceContext serviceContext = ServiceContextFactory.getInstance(Cart.class.getName(), req);
		String[] tagEntries = req.getParameterValues("tagsEntries");

		tagEntries = StringUtil.split(tagEntries[0], ",");

		List<TagsEntry> tagsList = new ArrayList<TagsEntry>();
		TagsEntry tagsEntry;

		for (int i = 0; i < tagEntries.length; i++) {

			name = tagEntries[i];

			try {
				TagsEntryLocalServiceUtil.getEntry(themeDisplay.getScopeGroupId(), name);
				tagsEntry = TagsEntryLocalServiceUtil.getEntry(themeDisplay.getScopeGroupId(), name);
				tagsList.add(tagsEntry);
			} catch (Exception e) {
				try {
					tagsEntry = TagsEntryServiceUtil.addEntry(parentEntryName, name, vocabularyName, properties, serviceContext);
					tagsList.add(tagsEntry);

				} catch (Exception e2) {
				}

			}

		}

		if (tagsList.size() > 0) {
			TagsAssetUtil.addTagsEntries(tagsAsset.getAssetId(), tagsList);
		}
	}

	/**
	 * Save asset entry for cart.
	 * 
	 * @param now
	 *            actual date
	 * @param themeDisplay
	 * @param title
	 *            cart title
	 * @param cart
	 *            actual cart
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	private static TagsAsset saveTagsAssetEntry(Date now, ThemeDisplay themeDisplay, String title, Cart cart) throws PortalException, SystemException {
		long groupId = themeDisplay.getScopeGroupId();
		String className = Cart.class.getName();
		long classPK = cart.getPrimaryKey();
		String[] categoryNames = {};
		String[] entryNames = {};
		boolean visible = true;
		Date startDate = null;
		Date endDate = null;
		Date publishDate = now;
		Date expirationDate = null;
		String mimeType = "";
		String description = "";
		String summary = "";
		String url = "";
		int height = 0;
		int width = 0;
		Integer priority = 0;
		TagsAsset tagsAsset = TagsAssetServiceUtil.updateAsset(groupId, className, classPK, categoryNames, entryNames, visible, startDate, endDate,
				publishDate, expirationDate, mimeType, title, description, summary, url, height, width, priority);
		return tagsAsset;
	}

	/**
	 * Persist cart entry.
	 * 
	 * @param now
	 *            current date.
	 * @param cartEntryId
	 *            id from cartEntry
	 * @param cart
	 *            the cart.
	 * @param cartEntry
	 * @return
	 * @throws SystemException
	 */
	private static long saveCartEntry(Date now, long cartEntryId, Cart cart, CartEntry cartEntry) throws SystemException {
		CartEntry saveCartEntry = CartEntryLocalServiceUtil.createCartEntry(cartEntryId);
		saveCartEntry.setCreateDate(now);
		saveCartEntry.setModifiedDate(now);
		saveCartEntry.setResourceId(cartEntry.getResourceId());
		saveCartEntry.setResourceType(cartEntry.getResourceType());
		saveCartEntry.setUserId(cartEntry.getUserId());
		saveCartEntry.setUserName(cartEntry.getUserName());
		CartEntryLocalServiceUtil.addCartEntry(saveCartEntry);
		CartUtil.addCartEntry(cart.getCartId(), saveCartEntry);
		cartEntryId++;
		return cartEntryId;
	}

	/**
	 * Persist cart as parent for all cart entries.
	 * 
	 * @param now
	 * @param themeDisplay
	 * @param userId
	 * @param title
	 * @param cartId
	 * @return
	 * @throws SystemException
	 */
	private static Cart saveCart(Date now, ThemeDisplay themeDisplay, long userId, String title, long cartId) throws SystemException {
		Cart cart = CartLocalServiceUtil.createCart(cartId);
		cart.setCreateDate(now);
		cart.setModifiedDate(now);
		cart.setTitle(title);
		cart.setUserId(userId);
		cart.setGroupId(themeDisplay.getScopeGroupId());
		cart.setCompanyId(themeDisplay.getCompanyId());
		CartLocalServiceUtil.addCart(cart);
		return cart;
	}

	/**
	 * Get all cart entries from cart.
	 * 
	 * @param entryId
	 *            the id from cart.
	 * @param userId
	 *            current user id.
	 * @return list of cart entries from cart.
	 * @throws SystemException
	 */
	public static List<CartEntry> getCartEntries(long entryId, Long userId) throws SystemException {
		List<CartEntry> listCartEntries = CartLocalServiceUtil.getCartEntries(entryId);
		if (listCartEntries.equals(null)) {
			listCartEntries = new ArrayList<CartEntry>();
		}
		return listCartEntries;
	}

}
