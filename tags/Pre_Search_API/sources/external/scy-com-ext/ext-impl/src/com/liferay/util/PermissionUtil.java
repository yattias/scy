package com.liferay.util;

import com.liferay.portal.PortalException;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;

/**
 * Checks permissions for taglib ui buttons-
 * 
 * @author Daniel
 * 
 */

public class PermissionUtil {

	public static void check(PermissionChecker permissionChecker, String modelClassName, long groupId, String actionId) throws PortalException {

		if (!contains(permissionChecker, modelClassName, groupId, actionId)) {
			throw new PrincipalException();
		}
	}

	public static boolean contains(PermissionChecker permissionChecker, String modelClassName, long groupId, String actionId) {

		return permissionChecker.hasPermission(groupId, modelClassName, groupId, actionId);
	}

	public static String portletModelName(String className) {
		String[] splitClassName = className.split(".model");
		return splitClassName[0];
	}
}
