package com.ext.portlet.resourcehandling;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;

import com.ext.portlet.resourcehandling.interfaces.FolderView;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.portlet.wiki.model.WikiPage;
import com.liferay.portlet.wiki.service.WikiPageLocalServiceUtil;
import com.liferay.portlet.wiki.service.permission.WikiPagePermission;
import com.liferay.util.LinkToolUtil;

/**
 * Get all needed resources to show wiki entries. The parent folder are split by each user how has entered one entry. 
 * Child entries are all files from this user in this community.
 * 
 * @author Daniel
 *
 */
public class WikiPageFolderView implements FolderView {

	private ActionRequest req;

	public WikiPageFolderView(ActionRequest req) throws SystemException, PortalException {
		this.req = req;
		getParent();
		getChildren();
	}

	@Override
	public FolderView getChildren() throws SystemException, PortalException {
		if (req.getParameter("rootUserId") != null) {
			Long rootUserId = Long.valueOf(req.getParameter("rootUserId"));
			List<WikiPage> wikiList = getWikiList(rootUserId);
			req.setAttribute("wikiList", wikiList);
			req.setAttribute("rootUserId", rootUserId);
		}
		return null;
	}

	@Override
	public FolderView getParent() throws SystemException, PortalException {
		List<User> userList = getWikiUserList(req);
		req.setAttribute("wikiUserList", userList);
		return null;
	}

	/**
	 * Get all users which add minimum one wiki entry to theme display in
	 * portal.
	 * 
	 * @param req
	 *            ActionRequest
	 * @return list of users which wrote wiki in this company
	 * @throws SystemException
	 * @throws PrincipalException
	 * @throws PortalException
	 */
	private List<User> getWikiUserList(ActionRequest req) throws SystemException, PrincipalException, PortalException {
		ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
		List<WikiPage> companyWikiList = WikiPageLocalServiceUtil.getWikiPages(0, WikiPageLocalServiceUtil.getWikiPagesCount());
		List<Long> userIdList = new ArrayList<Long>();
		for (WikiPage wikiEntry : companyWikiList) {
			if (WikiPagePermission.contains(LinkToolUtil.getPermissionChecker(), wikiEntry, ActionKeys.VIEW)
					&& wikiEntry.getCompanyId() == themeDisplay.getCompanyId()) {
				userIdList.add(wikiEntry.getUserId());
			}
		}
		List<Long> userWithout = LinkToolUtil.removeDoubleEntries(userIdList);
		List<User> userList = new ArrayList<User>();
		for (Long userId : userWithout) {
			userList.add(UserLocalServiceUtil.getUser(userId));
		}
		return userList;
	}

	/**
	 * Get all wiki pages from portal where user has permission for.
	 * 
	 * @return wikiList from portal
	 * @throws SystemException
	 * @throws PrincipalException
	 */
	protected List<WikiPage> getWikiList(long rootUserId) throws SystemException, PortalException {
		boolean isTagsAsset;
		List<WikiPage> wikiListComplete = WikiPageLocalServiceUtil.getWikiPages(0, WikiPageLocalServiceUtil.getWikiPagesCount());

		List<WikiPage> wikiList = new ArrayList<WikiPage>();

		List<String> primKey = new ArrayList<String>();
		for (WikiPage wiki : wikiListComplete) {
			try {
				TagsAssetLocalServiceUtil.getAsset(WikiPage.class.getName(), wiki.getResourcePrimKey());
				isTagsAsset = true;
			} catch (Exception e) {
				isTagsAsset = false;
			}
			if (isTagsAsset && wiki.getUserId() == rootUserId && WikiPagePermission.contains(LinkToolUtil.getPermissionChecker(), wiki, ActionKeys.VIEW)) {
				primKey.add(String.valueOf(wiki.getResourcePrimKey()));
			}
		}

		List<String> primKeyOnlyNew = LinkToolUtil.removeDoubleEntries(primKey);

		// add only blogEntry to list where user has permission to view
		for (String string : primKeyOnlyNew) {
			wikiList.add(WikiPageLocalServiceUtil.getPage(Long.valueOf(string)));

		}

		return wikiList;
	}

}
