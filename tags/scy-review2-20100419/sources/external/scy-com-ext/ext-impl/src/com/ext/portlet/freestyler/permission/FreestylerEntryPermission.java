package com.ext.portlet.freestyler.permission;

import com.ext.portlet.freestyler.model.FreestylerEntry;
import com.ext.portlet.freestyler.service.FreestylerEntryLocalServiceUtil;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;

/**
 * Permission class for freestyler entry. Check that current user from portlet
 * view has permission or not.
 * 
 * @author Daniel
 * 
 */
public class FreestylerEntryPermission {

	public static void check(PermissionChecker permissionChecker, long entryId, String actionId) throws PortalException, SystemException {

		if (!contains(permissionChecker, entryId, actionId)) {
			throw new PrincipalException();
		}
	}

	public static void check(PermissionChecker permissionChecker, FreestylerEntry entry, String actionId) throws PortalException {

		if (!contains(permissionChecker, entry, actionId)) {
			throw new PrincipalException();
		}
	}

	public static boolean contains(PermissionChecker permissionChecker, long entryId, String actionId) throws PortalException, SystemException {

		FreestylerEntry entry = FreestylerEntryLocalServiceUtil.getFreestylerEntry(entryId);

		return contains(permissionChecker, entry, actionId);
	}

	public static boolean contains(PermissionChecker permissionChecker, FreestylerEntry entry, String actionId) {

		if (permissionChecker.hasOwnerPermission(entry.getCompanyId(), FreestylerEntry.class.getName(), entry.getFreestylerId(), entry.getUserId(), actionId)) {

			return true;
		}

		return permissionChecker.hasPermission(entry.getGroupId(), FreestylerEntry.class.getName(), entry.getFreestylerId(), actionId);
	}

}