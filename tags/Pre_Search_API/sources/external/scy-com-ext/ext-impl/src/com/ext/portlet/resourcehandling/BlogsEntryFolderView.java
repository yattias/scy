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
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.service.BlogsEntryLocalServiceUtil;
import com.liferay.portlet.blogs.service.permission.BlogsEntryPermission;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.util.LinkToolUtil;

/**
 * Get all needed resources to show blog entries. The parent folder are split by each user how has entered one entry. 
 * Child entries are all files from this user in this community.
 * 
 * @author Daniel
 *
 */
public class BlogsEntryFolderView implements FolderView {

	private ActionRequest req;
	private FolderView folderView;

	public BlogsEntryFolderView(ActionRequest req) throws SystemException, PortalException {
		this.req = req;
		getChildren();
		getParent();
	}

	@Override
	public FolderView getChildren() throws SystemException, PortalException {
		
		if (req.getParameter("rootUserId") != null) {
			Long rootUserId = Long.valueOf(req.getParameter("rootUserId"));
			List<BlogsEntry> blogList = getBlogList(rootUserId);
			req.setAttribute("blogList", blogList);
			req.setAttribute("rootUserId", rootUserId);
			return folderView;
		}
		return null;
	}

	@Override
	public FolderView getParent() throws SystemException, PortalException {
		
		List<User> userList;
		userList = getBlogUserList(req);
		req.setAttribute("blogUserList", userList);
		return folderView;
	}

	/**
	 * Get all users which add minimum one blog entry to theme display company
	 * in portal.
	 * 
	 * @param req
	 *            ActionRequest
	 * @return list of users which wrote blogs
	 * @throws SystemException
	 * @throws PrincipalException
	 * @throws PortalException
	 * @throws PortalException
	 */
	public List<User> getBlogUserList(ActionRequest req) throws SystemException, PortalException {
		ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
		List<BlogsEntry> companyBlogList = BlogsEntryLocalServiceUtil.getCompanyEntries(themeDisplay.getCompanyId(), 0, BlogsEntryLocalServiceUtil
				.getCompanyEntriesCount(themeDisplay.getCompanyId()));
		List<Long> userIdList = new ArrayList<Long>();
		for (BlogsEntry blogsEntry : companyBlogList) {
			if (BlogsEntryPermission.contains(LinkToolUtil.getPermissionChecker(), blogsEntry, ActionKeys.VIEW)) {
				userIdList.add(blogsEntry.getUserId());
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
	 * Get all blogs from chosen user where user has permission for.
	 * 
	 * @return blogList from portal
	 * @throws SystemException
	 * @throws PortalException
	 */
	protected List<BlogsEntry> getBlogList(long rootUserId) throws SystemException, PortalException {
		boolean isTagsAsset;
		List<BlogsEntry> blogListComplete = BlogsEntryLocalServiceUtil.getBlogsEntries(0, BlogsEntryLocalServiceUtil.getBlogsEntriesCount());
		List<BlogsEntry> blogList = new ArrayList<BlogsEntry>();

		// add only blogEntry to list where user has permission to view
		for (BlogsEntry blogsEntry : blogListComplete) {
			try {
				TagsAssetLocalServiceUtil.getAsset(BlogsEntry.class.getName(), blogsEntry.getEntryId());
				isTagsAsset = true;
			} catch (Exception e) {
				isTagsAsset = false;
			}
			if (isTagsAsset && blogsEntry.getUserId() == rootUserId
					&& BlogsEntryPermission.contains(LinkToolUtil.getPermissionChecker(), blogsEntry, ActionKeys.VIEW)) {
				blogList.add(blogsEntry);
			}

		}
		return blogList;
	}

}
