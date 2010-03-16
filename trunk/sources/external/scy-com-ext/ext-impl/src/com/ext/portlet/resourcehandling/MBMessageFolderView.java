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
import com.liferay.portlet.messageboards.model.MBCategory;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.service.MBCategoryLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.portlet.messageboards.service.permission.MBMessagePermission;
import com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil;
import com.liferay.util.LinkToolUtil;

/**
 * Get all needed resources to show message board entries. The parent folder are split by each user how has entered one entry. 
 * Child entries are all files from this user in this community.
 * 
 * @author Daniel
 *
 */
public class MBMessageFolderView implements FolderView {

	private ActionRequest req;

	public MBMessageFolderView(ActionRequest req) throws SystemException, PortalException {
		this.req = req;
		getParent();
		getChildren();
	}

	@Override
	public FolderView getChildren() throws SystemException, PortalException {
		if (req.getParameter("rootUserId") != null) {
		Long rootUserId = Long.valueOf(req.getParameter("rootUserId"));
		List<MBMessage> messageList = getMessageList(rootUserId);
		req.setAttribute("messageList", messageList);
		req.setAttribute("rootUserId", rootUserId);
		}
		return null;
	}

	@Override
	public FolderView getParent() throws SystemException, PortalException {
		List<User> userList = getMessageBoardUserList(req);
		req.setAttribute("messageUserList", userList);
		return null;
	}
	
	/**
	 * Get all users which add minimum one message board entry to theme display
	 * in portal.
	 * 
	 * @param req
	 *            ActionRequest
	 * @return list of users which wrote message board in this company
	 * @throws SystemException
	 * @throws PrincipalException
	 * @throws PortalException
	 */
	private List<User> getMessageBoardUserList(ActionRequest req) throws SystemException, PrincipalException, PortalException {
		List<MBCategory> mbcategoryList = MBCategoryLocalServiceUtil.getMBCategories(0, MBCategoryLocalServiceUtil.getMBCategoriesCount());
		List<MBMessage> companyMessageBoardList = new ArrayList<MBMessage>();
		for (MBCategory mbCategory : mbcategoryList) {
			if (mbCategory.getCategoryId() > 0) {
				companyMessageBoardList.addAll(MBMessageLocalServiceUtil.getCategoryMessages(mbCategory.getCategoryId(), 0, MBMessageLocalServiceUtil
						.getCategoryMessagesCount(mbCategory.getCategoryId())));
			}
		}
		List<Long> userIdList = new ArrayList<Long>();
		for (MBMessage messageEntry : companyMessageBoardList) {
			if (MBMessagePermission.contains(LinkToolUtil.getPermissionChecker(), messageEntry, ActionKeys.VIEW)) {
				userIdList.add(messageEntry.getUserId());
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
	 * Get all message board entries from portal where user has permission for.
	 * 
	 * @return messageList from portal
	 * @throws SystemException
	 * @throws PrincipalException
	 */
	protected List<MBMessage> getMessageList(long rootUserId) throws SystemException, PortalException {
		boolean isTagsAsset;
		List<MBMessage> messageListComplete = MBMessageLocalServiceUtil.getMBMessages(0, MBMessageLocalServiceUtil.getMBMessagesCount());

		List<MBMessage> messageList = new ArrayList<MBMessage>();
		// add only blogEntry to list where user has permission to view
		for (MBMessage message : messageListComplete) {
			try {
				TagsAssetLocalServiceUtil.getAsset(MBMessage.class.getName(), message.getMessageId());
				isTagsAsset = true;
			} catch (Exception e) {
				isTagsAsset = false;
			}
			if (isTagsAsset && message.getUserId() == rootUserId && MBMessagePermission.contains(LinkToolUtil.getPermissionChecker(), message, ActionKeys.VIEW)) {
				messageList.add(message);
			}

		}
		return messageList;
	}

}
