package com.ext.portlet.freestyler.service.impl;

import java.io.File;

import com.ext.portlet.freestyler.model.FreestylerEntry;
import com.ext.portlet.freestyler.model.FreestylerImage;
import com.ext.portlet.freestyler.service.base.FreestylerImageServiceBaseImpl;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.imagegallery.service.permission.IGFolderPermission;

public class FreestylerImageServiceImpl extends FreestylerImageServiceBaseImpl {

	public FreestylerImage addImage(long folderId, String name, String description, File file, String contentType, ServiceContext serviceContext, FreestylerEntry entry)
			throws PortalException, SystemException {

		IGFolderPermission.check(getPermissionChecker(), folderId, ActionKeys.ADD_IMAGE);

		return freestylerImageLocalService.addImage(getUserId(), folderId, name, description, file, contentType, serviceContext, entry);
	}

	@Override
	public FreestylerImage addImage(long folderId, String name, String description, File file, String contentType, ServiceContext serviceContext)
			throws PortalException, SystemException {
		// TODO Auto-generated method stub
		return null;
	}

}
