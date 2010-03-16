package com.ext.portlet.resourcehandling;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;

import com.ext.portlet.resourcehandling.interfaces.FolderView;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.permission.JournalArticlePermission;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.util.LinkToolUtil;

/**
 * Get all needed resources to show journal article entries. The parent folder are split by each user how has entered one entry. 
 * Child entries are all files from this user in this community.
 * 
 * @author Daniel
 *
 */
public class JournalArticleFolderView implements FolderView {

	private ActionRequest req;

	public JournalArticleFolderView(ActionRequest req) throws SystemException, PortalException {
		this.req = req;
		getParent();
		getChildren();
	}

	@Override
	public FolderView getChildren() throws SystemException, PortalException {
		if (req.getParameter("rootUserId") != null) {
			Long rootUserId = Long.valueOf(req.getParameter("rootUserId"));
			List<JournalArticle> journalList = getJournalList(rootUserId);
			req.setAttribute("journalList", journalList);
			req.setAttribute("rootUserId", rootUserId);
		}
		return null;
	}

	@Override
	public FolderView getParent() throws SystemException, PortalException {
		List<User> userList = getJournalUserList(req);
		req.setAttribute("journalUserList", userList);
		return null;
	}

	/**
	 * Get all users which add minimum one webcontent entry to theme display in
	 * portal.
	 * 
	 * @param req
	 *            ActionRequest
	 * @return list of users which wrote webcontent in this company
	 * @throws SystemException
	 * @throws PrincipalException
	 * @throws PortalException
	 */
	private List<User> getJournalUserList(ActionRequest req) throws SystemException, PrincipalException, PortalException {
		ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
		List<JournalArticle> companyJournalList = JournalArticleLocalServiceUtil.getArticles();
		List<Long> userIdList = new ArrayList<Long>();
		for (JournalArticle journalEntry : companyJournalList) {
			if (JournalArticlePermission.contains(LinkToolUtil.getPermissionChecker(), journalEntry, ActionKeys.VIEW)
					&& journalEntry.getCompanyId() == themeDisplay.getCompanyId()) {
				userIdList.add(journalEntry.getUserId());
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
	 * Get all journal pages from portal where user has permission for.
	 * 
	 * @return journalList from portal
	 * @throws SystemException
	 * @throws PrincipalException
	 */
	protected List<JournalArticle> getJournalList(long rootUserId) throws SystemException, PortalException {
		boolean isTagsAsset;

		List<JournalArticle> journalListComplete = JournalArticleLocalServiceUtil.getArticles();
		List<JournalArticle> journalList = new ArrayList<JournalArticle>();

		for (JournalArticle journal : journalListComplete) {
			try {
				TagsAssetLocalServiceUtil.getAsset(JournalArticle.class.getName(), journal.getResourcePrimKey());
				isTagsAsset = true;
			} catch (Exception e) {
				isTagsAsset = false;
			}
			if (isTagsAsset && journal.getUserId() == rootUserId
					&& JournalArticlePermission.contains(LinkToolUtil.getPermissionChecker(), journal, ActionKeys.VIEW) && journal.isApproved()
					&& journal.getUserId() == rootUserId) {
				journalList.add(journal);
			}
		}

		return journalList;
	}

}
