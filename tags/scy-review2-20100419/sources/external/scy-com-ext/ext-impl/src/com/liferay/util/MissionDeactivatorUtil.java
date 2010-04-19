package com.liferay.util;

import java.util.Date;
import java.util.List;

import com.ext.portlet.missionhandling.model.MissionEntry;
import com.ext.portlet.missionhandling.service.MissionEntryLocalServiceUtil;
import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroupRole;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

public class MissionDeactivatorUtil {

	/**
	 * Set the current date to date in future by entered days.
	 * 
	 * @param days
	 *            days to set day in future
	 * @return date in future of entered days
	 */
	public static Date getDateAfterDays(int days) {
		long backDateMS = System.currentTimeMillis() + ((long) days) * 24 * 60 * 60 * 1000;
		Date backDate = new Date();
		backDate.setTime(backDateMS);
		return backDate;
	}

	/**
	 * Set missionEntry to inactive and delete UserGroupRole entries for this
	 * organization and roleId from ORGANIZATION_ACTIVE_MEMBERS.
	 * 
	 * @param missionEntry
	 *            missionEntry to deactivate from missionEntry db and endDate
	 *            before current date.
	 * @return deleted missionEntry
	 * @throws SystemException
	 */
	private static MissionEntry clearMissionEntry(MissionEntry missionEntry) throws SystemException {

		Role role;
		try {
			missionEntry.setEndDate(null);
			missionEntry.setActive(false);
			MissionEntryLocalServiceUtil.updateMissionEntry(missionEntry);
			role = RoleLocalServiceUtil.getRole(missionEntry.getCompanyId(), RoleConstants.ORGANIZATION_ACTIVE_MEMBERS);
			List<UserGroupRole> userGroupRolesList = UserGroupRoleLocalServiceUtil.getUserGroupRolesByGroupAndRole(missionEntry.getGroupId(), role.getRoleId());

			for (UserGroupRole userGroupRole : userGroupRolesList) {
				UserGroupRoleLocalServiceUtil.deleteUserGroupRole(userGroupRole);
				_log.info("delete userGroupRole with userId: " + userGroupRole.getUserId() + " and groupId: " + userGroupRole.getGroupId());

			}
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return missionEntry;

	}

	/**
	 * Delete all missionEntries and role groups for missionList entries with
	 * endDate before current system date.
	 */
	public static void missionDeactiveJob() {
		try {
			List<MissionEntry> missionList = MissionEntryLocalServiceUtil.getMissionEntries(0, MissionEntryLocalServiceUtil.getMissionEntriesCount());
			Date now = new Date();
			now.setTime(System.currentTimeMillis());

			for (MissionEntry missionEntry : missionList) {

				if (missionEntry.getEndDate() != null && now.after(missionEntry.getEndDate()) && missionEntry.isActive()) {
					clearMissionEntry(missionEntry);
					_log.info("deactivate mission for group with groupId: " + missionEntry.getGroupId() + " and missionEntryId: "
							+ missionEntry.getMissionEntryId());
				}
			}

		} catch (SystemException e) {
			_log.error("Cant get missionList from MissionEntryLocalServiceUtil or delete missionEntry");
			_log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Add new missionEntry. This will deactivate the mission in future by given
	 * days.
	 * 
	 * @param days
	 *            time in days to set the mission inactive
	 * @param role
	 *            the role from mission
	 */
	public static MissionEntry addMissionEntry(String days, Organization organization) {
		Date date = new Date();
		date.setTime(System.currentTimeMillis());

		try {
			List<MissionEntry> missionEntryList = MissionEntryLocalServiceUtil.getMissionEntries(organization.getGroup().getGroupId());
			if (missionEntryList.size() <= 0) {
				long missionEntryId = CounterLocalServiceUtil.increment(MissionEntry.class.getName());
				MissionEntry missionEntry = MissionEntryLocalServiceUtil.createMissionEntry(missionEntryId);

				missionEntry.setCompanyId(organization.getCompanyId());
				missionEntry.setGroupId(organization.getGroup().getGroupId());
				missionEntry.setOrganizationId(organization.getOrganizationId());
				missionEntry.setActive(true);
				missionEntry.setCreateDate(date);
				if (days.length() == 0) {
					days = "0";
				}
				missionEntry.setEndDate(MissionDeactivatorUtil.getDateAfterDays(Integer.valueOf(days)));
				MissionEntryLocalServiceUtil.addMissionEntry(missionEntry);

				StringBuffer infoSB = new StringBuffer();
				infoSB.append("Add new missionEntry with id: ");
				infoSB.append(missionEntryId);
				infoSB.append(" and oragizationId: ");
				infoSB.append(missionEntry.getOrganizationId());
				_log.info(infoSB.toString());
				return missionEntry;
			} else {
				MissionEntry missionChange = missionEntryList.get(0);
				missionChange.setEndDate(MissionDeactivatorUtil.getDateAfterDays(Integer.valueOf(days)));
				MissionEntryLocalServiceUtil.updateMissionEntry(missionChange);
				_log.info("set new end date for mission with id: " + missionChange.getMissionEntryId());
			}
		} catch (SystemException e) {
			_log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Change missionEntry. If days equals 0 the missionEntry will delete from
	 * system otherwise the endDate will be change that indicates the day to
	 * deactivate the mission.
	 * 
	 * @param days
	 *            the time in days that define the endDate
	 * @param missionEntry
	 *            the given mission to change
	 */
	public static void changeMissionEntry(String days, MissionEntry missionEntry) {

		if (days.length() > 0) {
			Date newEndDate = getDateAfterDays(Integer.valueOf(days));
			missionEntry.setEndDate(newEndDate);
			try {
				MissionEntry changeMissionEntry = MissionEntryLocalServiceUtil.updateMissionEntry(missionEntry);
				StringBuffer infoSB = new StringBuffer();
				infoSB.append("Change endDate for missionEntry with id: ");
				infoSB.append(changeMissionEntry.getMissionEntryId());
				_log.info(infoSB.toString());
			} catch (SystemException e) {
				_log.error(e.getMessage());
			}
		} else {
			_log.error("the enterd days in future is 0");
		}

	}

	/**
	 * Delete mission entry.
	 * 
	 * @param missionEntry
	 *            the missionEntry to delete
	 */
	public static void deleteMissionEntry(MissionEntry missionEntry) {
		try {
			MissionEntryLocalServiceUtil.deleteMissionEntry(missionEntry.getMissionEntryId());
			_log.info("Delete mission entry with id: " + missionEntry.getMissionEntryId());
		} catch (PortalException e) {
			_log.error(e.getMessage());
			e.printStackTrace();
		} catch (SystemException e) {
			_log.error(e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * Deactivate a mission with given roleId;
	 * 
	 * @param roleId
	 *            the roleId from mission
	 */
	public static void deactivateMission(long organizationId) {
		Organization organization;
		try {
			organization = OrganizationLocalServiceUtil.getOrganization(organizationId);
			MissionEntry missionEntry = addMissionEntry("1", organization);
			missionEntry.setEndDate(null);
			missionEntry.setActive(false);
			MissionEntryLocalServiceUtil.updateMissionEntry(missionEntry);

			Role role = RoleLocalServiceUtil.getRole(organization.getCompanyId(), RoleConstants.ORGANIZATION_ACTIVE_MEMBERS);
			List<UserGroupRole> userGroupRolesList = UserGroupRoleLocalServiceUtil.getUserGroupRolesByGroupAndRole(organization.getGroup().getGroupId(), role
					.getRoleId());

			for (UserGroupRole userGroupRole : userGroupRolesList) {
				UserGroupRoleLocalServiceUtil.deleteUserGroupRole(userGroupRole);
				_log.info("delete userGroupRole with userId: " + userGroupRole.getUserId() + " and groupId: " + userGroupRole.getGroupId());

			}

			_log.info("deactivate mission with orgaizationId: " + organizationId);
		} catch (PortalException e1) {
			_log.error(e1.getMessage());
			e1.printStackTrace();
		} catch (SystemException e1) {
			_log.error(e1.getMessage());
			e1.printStackTrace();
		}

	}

	public static void activateOrganization(Long missionEntryId) throws PortalException, SystemException {
		MissionEntry missionEntryToActivate = MissionEntryLocalServiceUtil.getMissionEntry(missionEntryId);
		missionEntryToActivate.setActive(true);
		MissionEntryLocalServiceUtil.updateMissionEntry(missionEntryToActivate);
		_log.info("activate missionEntry with id: " + missionEntryToActivate.getMissionEntryId());

		Role role = RoleLocalServiceUtil.getRole(missionEntryToActivate.getCompanyId(), RoleConstants.ORGANIZATION_ACTIVE_MEMBERS);
		List<User> orgUserList = UserLocalServiceUtil.getOrganizationUsers(missionEntryToActivate.getOrganizationId());
		for (User user : orgUserList) {
			UserGroupRoleLocalServiceUtil.addUserGroupRoles(user.getUserId(), missionEntryToActivate.getGroupId(), new long[] { role.getRoleId() });
		}
		_log.info("add userGroupRoles");

	}

	private static Log _log = LogFactoryUtil.getLog(MissionDeactivatorUtil.class);

}
