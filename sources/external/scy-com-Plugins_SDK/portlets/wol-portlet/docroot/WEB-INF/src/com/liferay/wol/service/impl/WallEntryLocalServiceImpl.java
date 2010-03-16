/**
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.wol.service.impl;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.social.service.SocialActivityLocalServiceUtil;
import com.liferay.wol.model.WallEntry;
import com.liferay.wol.service.base.WallEntryLocalServiceBaseImpl;
import com.liferay.wol.wall.social.WallActivityKeys;

import java.util.Date;
import java.util.List;

import javax.mail.internet.InternetAddress;

/**
 * <a href="WallEntryLocalServiceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class WallEntryLocalServiceImpl extends WallEntryLocalServiceBaseImpl {

	public WallEntry addWallEntry(
			long groupId, long userId, String comments,
			ThemeDisplay themeDisplay)
		throws PortalException, SystemException {

		// Wall entry

		Group group = GroupLocalServiceUtil.getGroup(groupId);
		User user = UserLocalServiceUtil.getUserById(userId);
		Date now = new Date();

		long wallEntryId = CounterLocalServiceUtil.increment();

		WallEntry wallEntry = wallEntryPersistence.create(wallEntryId);

		wallEntry.setGroupId(groupId);
		wallEntry.setCompanyId(user.getCompanyId());
		wallEntry.setUserId(user.getUserId());
		wallEntry.setUserName(user.getFullName());
		wallEntry.setCreateDate(now);
		wallEntry.setModifiedDate(now);
		wallEntry.setComments(comments);

		wallEntryPersistence.update(wallEntry, false);

		// Email

		try {
			sendEmail(wallEntry, themeDisplay);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}

		// Social

		if (userId != group.getClassPK()) {
			SocialActivityLocalServiceUtil.addActivity(
				userId, groupId, WallEntry.class.getName(), wallEntryId,
				WallActivityKeys.ADD_ENTRY, StringPool.BLANK,
				group.getClassPK());
		}

		return wallEntry;
	}

	public void deleteWallEntries(long groupId) throws SystemException {
		List<WallEntry> wallEntries = wallEntryPersistence.findByGroupId(
			groupId);

		for (WallEntry wallEntry : wallEntries) {
			deleteWallEntry(wallEntry);
		}
	}

	public void deleteWallEntry(long wallEntryId)
		throws PortalException, SystemException {

		WallEntry wallEntry = wallEntryPersistence.findByPrimaryKey(
			wallEntryId);

		deleteWallEntry(wallEntry);
	}

	public void deleteWallEntry(WallEntry wallEntry) throws SystemException {

		// Social

		SocialActivityLocalServiceUtil.deleteActivities(
			WallEntry.class.getName(), wallEntry.getWallEntryId());

		// Entry

		wallEntryPersistence.remove(wallEntry);
	}

	public List<WallEntry> getWallEntries(long groupId, int start, int end)
		throws SystemException {

		return wallEntryPersistence.findByGroupId(groupId, start, end);
	}

	public int getWallEntriesCount(long groupId) throws SystemException {
		return wallEntryPersistence.countByGroupId(groupId);
	}

	public WallEntry getWallEntry(long wallEntryId)
		throws PortalException, SystemException {

		return wallEntryPersistence.findByPrimaryKey(wallEntryId);
	}

	public List<WallEntry> getWallToWallEntries(
			long groupId1, long groupId2, long userId1, long userId2, int start,
			int end)
		throws SystemException {

		return wallEntryFinder.findByG1_G2_U1_U2(
			groupId1, groupId2, userId1, userId2, start, end);
	}

	public int getWallToWallEntriesCount(
			long groupId1, long groupId2, long userId1, long userId2)
		throws SystemException {

		return wallEntryFinder.countByG1_G2_U1_U2(
			groupId1, groupId2, userId1, userId2);
	}

	public WallEntry updateWallEntry(long wallEntryId, String comments)
		throws PortalException, SystemException {

		WallEntry wallEntry = wallEntryPersistence.findByPrimaryKey(
			wallEntryId);

		wallEntry.setModifiedDate(new Date());
		wallEntry.setComments(comments);

		wallEntryPersistence.update(wallEntry, false);

		return wallEntry;
	}

	protected void sendEmail(WallEntry wallEntry, ThemeDisplay themeDisplay)
		throws Exception {

		long companyId = wallEntry.getCompanyId();

		String portalURL = PortalUtil.getPortalURL(themeDisplay);
		String layoutURL = PortalUtil.getLayoutURL(themeDisplay);

		String wallEntryURL = portalURL + layoutURL;

		Group group = GroupLocalServiceUtil.getGroup(wallEntry.getGroupId());

		User user = UserLocalServiceUtil.getUserById(group.getClassPK());
		User wallEntryUser = UserLocalServiceUtil.getUserById(
			wallEntry.getUserId());

		String fromName = PrefsPropsUtil.getString(
			companyId, "admin.email.from.name");
		String fromAddress = PrefsPropsUtil.getString(
			companyId, "admin.email.from.address");

		String toName = user.getFullName();
		String toAddress = user.getEmailAddress();

		ClassLoader classLoader = getClass().getClassLoader();

		String subject = StringUtil.read(
			classLoader,
			"com/liferay/wol/wall/dependencies/wall_entry_added_subject.tmpl");
		String body = StringUtil.read(
			classLoader,
			"com/liferay/wol/wall/dependencies/wall_entry_added_body.tmpl");

		subject = StringUtil.replace(
			subject,
			new String[] {
				"[$FROM_ADDRESS$]",
				"[$FROM_NAME$]",
				"[$TO_ADDRESS$]",
				"[$TO_NAME$]",
				"[$WALL_ENTRY_URL$]",
				"[$WALL_ENTRY_USER_ADDRESS$]",
				"[$WALL_ENTRY_USER_NAME$]"
			},
			new String[] {
				fromAddress,
				fromName,
				toAddress,
				toName,
				wallEntryURL,
				wallEntryUser.getEmailAddress(),
				wallEntryUser.getFullName()
			});

		body = StringUtil.replace(
			body,
			new String[] {
				"[$FROM_ADDRESS$]",
				"[$FROM_NAME$]",
				"[$TO_ADDRESS$]",
				"[$TO_NAME$]",
				"[$WALL_ENTRY_URL$]",
				"[$WALL_ENTRY_USER_ADDRESS$]",
				"[$WALL_ENTRY_USER_NAME$]"
			},
			new String[] {
				fromAddress,
				fromName,
				toAddress,
				toName,
				wallEntryURL,
				wallEntryUser.getEmailAddress(),
				wallEntryUser.getFullName()
			});

		InternetAddress from = new InternetAddress(fromAddress, fromName);

		InternetAddress to = new InternetAddress(toAddress, toName);

		MailMessage mailMessage = new MailMessage(
			from, to, subject, body, true);

		MailServiceUtil.sendEmail(mailMessage);
	}

}