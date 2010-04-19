package com.ext.portlet.freestyler.service.impl;

import java.util.List;

import com.ext.portlet.freestyler.model.FreestylerFolder;
import com.ext.portlet.freestyler.service.base.FreestylerFolderLocalServiceBaseImpl;
import com.liferay.portal.SystemException;

public class FreestylerFolderLocalServiceImpl extends FreestylerFolderLocalServiceBaseImpl {

	public List<FreestylerFolder> getFolders(long groupId) throws SystemException {
		return freestylerFolderPersistence.findByGroupId(groupId);
	}

	public List<FreestylerFolder> getFolders(long groupId, long parentFolderId) throws SystemException {

		return freestylerFolderPersistence.findByG_P(groupId, parentFolderId);
	}

	public List<FreestylerFolder> getFolders(long groupId, long parentFolderId, int start, int end) throws SystemException {

		return freestylerFolderPersistence.findByG_P(groupId, parentFolderId, start, end);
	}

	public int getFoldersCount(long groupId, long parentFolderId) throws SystemException {

		return freestylerFolderPersistence.countByG_P(groupId, parentFolderId);
	}
}
