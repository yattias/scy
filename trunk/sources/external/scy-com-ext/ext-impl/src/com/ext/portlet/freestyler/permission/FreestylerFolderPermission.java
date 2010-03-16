package com.ext.portlet.freestyler.permission;

import com.ext.portlet.freestyler.model.FreestylerFolder;
import com.ext.portlet.freestyler.model.impl.FreestylerFolderImpl;
import com.ext.portlet.freestyler.service.FreestylerFolderLocalServiceUtil;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portlet.imagegallery.service.permission.IGPermission;

/**
 * Permission class for freestyler folder. Check that current user from portlet
 * view has permission or not.
 * 
 * @author Daniel
 * 
 */
public class FreestylerFolderPermission {

	public static void check(PermissionChecker permissionChecker, long groupId, long folderId, String actionId) throws PortalException, SystemException {

		if (!contains(permissionChecker, groupId, folderId, actionId)) {
			throw new PrincipalException();
		}
	}

	public static void check(PermissionChecker permissionChecker, long folderId, String actionId) throws PortalException, SystemException {

		if (!contains(permissionChecker, folderId, actionId)) {
			throw new PrincipalException();
		}
	}

	public static void check(PermissionChecker permissionChecker, FreestylerFolder folder, String actionId) throws PortalException, SystemException {

		if (!contains(permissionChecker, folder, actionId)) {
			throw new PrincipalException();
		}
	}

	public static boolean contains(PermissionChecker permissionChecker, long groupId, long folderId, String actionId) throws PortalException, SystemException {

		if (folderId == FreestylerFolderImpl.DEFAULT_PARENT_FOLDER_ID) {
			return IGPermission.contains(permissionChecker, groupId, actionId);
		} else {
			return contains(permissionChecker, folderId, actionId);
		}
	}

	public static boolean contains(PermissionChecker permissionChecker, long folderId, String actionId) throws PortalException, SystemException {

		FreestylerFolder folder = FreestylerFolderLocalServiceUtil.getFreestylerFolder(folderId);

		return contains(permissionChecker, folder.getFolderId(), actionId);
	}

	public static boolean contains(PermissionChecker permissionChecker, FreestylerFolder folder, String actionId) throws PortalException, SystemException {

		if (actionId.equals(ActionKeys.ADD_FOLDER)) {
			actionId = ActionKeys.ADD_SUBFOLDER;
		}

		long folderId = folder.getFolderId();

		while (folderId != FreestylerFolderImpl.DEFAULT_PARENT_FOLDER_ID) {
			if (permissionChecker.hasOwnerPermission(folder.getCompanyId(), FreestylerFolder.class.getName(), folder.getFolderId(), folder.getUserId(),
					actionId)) {

				return true;
			}

			if (permissionChecker.hasPermission(folder.getGroupId(), FreestylerFolder.class.getName(), folder.getFolderId(), actionId)) {

				return true;
			}

			if (actionId.equals(ActionKeys.VIEW)) {
				break;
			}

			folder = FreestylerFolderLocalServiceUtil.getFreestylerFolder(folderId);

			folderId = folder.getParentFolderId();
		}

		return false;
	}

}